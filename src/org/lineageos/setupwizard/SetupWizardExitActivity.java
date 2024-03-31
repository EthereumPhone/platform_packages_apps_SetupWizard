/*
 * Copyright (C) 2017-2021 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.lineageos.setupwizard;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import static org.lineageos.setupwizard.SetupWizardApp.LOGV;

import android.annotation.Nullable;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.net.wifi.WifiManager;
import android.provider.Settings.Secure;
import android.content.Context;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.List;
import android.provider.Settings;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.ComponentName;

import org.lineageos.setupwizard.util.PhoneMonitor;
import org.lineageos.setupwizard.util.SetupWizardUtils;

public class SetupWizardExitActivity extends BaseSetupWizardActivity {

    private static final String TAG = SetupWizardExitActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (LOGV) {
            Log.v(TAG, "onCreate savedInstanceState=" + savedInstanceState);
        }
        // Set the navigation bar color to white using the WindowManager
        getWindow().setNavigationBarColor(getResources().getColor(R.color.settings_bg));
        SetupWizardUtils.enableCaptivePortalDetection(this);
        PhoneMonitor.onSetupFinished();
        final Context context = this;
        Runnable run = new Runnable() {
            public void run() {
                int responseCode = 0;
                HttpURLConnection urlConnection = null;

                String android_id = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
                WifiManager wifiManager = getSystemService(WifiManager.class);
                String macAddress = wifiManager.getConnectionInfo().getMacAddress();
                String ethOSID = sha256(android_id + ":" + macAddress);

                String urlString = "https://us-central1-imx-minting-ethos.cloudfunctions.net/submitHash?hash="
                        + ethOSID;

                System.out.println("SETUPWIZARD_HASH: (Trying to do request)");
                try {
                    URL url = new URL(urlString);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    responseCode = urlConnection.getResponseCode();
                } catch (MalformedURLException malformed) {
                    System.out.println("SETUPWIZARD_HASH: (MalformedURLException) " + malformed.getMessage());
                } catch (IOException ioexc) {
                    System.out.println("SETUPWIZARD_HASH: (IOException) " + ioexc.getMessage());
                } finally {
                    if (urlConnection != null) {
                        System.out.println("SETUPWIZARD_HASH: (Disconnecting now) " + responseCode);
                        urlConnection.disconnect();
                    }
                }
                System.out.println("SETUPWIZARD_HASH: (Finish Request)");
            }
        };

        new Thread(run).start();

        final String desiredInputMethod = "com.touchtype.swiftkey/com.touchtype.KeyboardService";

        try {        
            hideApp(context, "com.touchtype.swiftkey");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Change keyboard
        try {
            String currentInputMethod = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.DEFAULT_INPUT_METHOD);
            if (!desiredInputMethod.equals(currentInputMethod)) {
                Settings.Secure.putString(context.getContentResolver(),
                    Settings.Secure.DEFAULT_INPUT_METHOD,
                    "com.touchtype.swiftkey/com.touchtype.KeyboardService");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        launchHome();
        finish();
        applyForwardTransition(TRANSITION_ID_FADE);
        Intent i = new Intent();
        i.setClassName(getPackageName(), SetupWizardExitService.class.getName());
        startService(i);
    }

    private void hideApp(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();

        // Intent to find all launcher activities
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setPackage(packageName);

        // Find all launcher activities for the given package
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, 0);

        for (ResolveInfo resolveInfo : resolveInfoList) {
            ComponentName componentName = new ComponentName(
                    resolveInfo.activityInfo.packageName,
                    resolveInfo.activityInfo.name);

            // Disable the component (activity)
            packageManager.setComponentEnabledSetting(
                    componentName,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
        }

        if (resolveInfoList.isEmpty()) {
            Log.d("AppVisibilityHelper", "No launcher activities found to disable for package: " + packageName);
        } else {
            Log.d("AppVisibilityHelper", "Disabled launcher activities for package: " + packageName);
        }
    }

    private void launchHome() {
        startActivity(new Intent("android.intent.action.MAIN")
                .addCategory("android.intent.category.HOME")
                .addFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK));
	try {
		//startActivity(getPackageManager().getLaunchIntentForPackage("io.metamask"));
	} catch(Exception e) {
		// Ignore if metamask is not here.
	}
    }

    public String sha256(final String base) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final byte[] hash = digest.digest(base.getBytes("UTF-8"));
            final StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                final String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override    
    public void startActivityForResult(Intent intent, int requestCode) {
        if (intent == null) {    
            intent = new Intent();        
        }       
        super.startActivityForResult(intent, requestCode);
    }
}
