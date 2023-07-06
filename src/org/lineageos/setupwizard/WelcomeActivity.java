/*
 * Copyright (C) 2016 The CyanogenMod Project
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

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Display;
import android.hardware.display.DisplayManager;
import android.provider.Settings;
import org.json.JSONObject;

import com.google.android.setupcompat.util.SystemBarHelper;

public class WelcomeActivity extends BaseSetupWizardActivity {

    public static final String TAG = WelcomeActivity.class.getSimpleName();

    private static final float DEFAULT_REFRESH_RATE = 60f;

    private View mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setting to peak refresh rate
        final DisplayManager dm = this.getSystemService(DisplayManager.class);
        final Display display = dm.getDisplay(Display.DEFAULT_DISPLAY);
        float peakRefreshRate = DEFAULT_REFRESH_RATE;
        if (display == null) {
            System.out.println(TAG+": No valid default display device");
        } else {
            peakRefreshRate = findPeakRefreshRate(display.getSupportedModes());
        }
        Settings.System.putFloat(this.getContentResolver(), Settings.System.PEAK_REFRESH_RATE, peakRefreshRate);
        System.out.println(TAG+" Setting display refresh rate to " + peakRefreshRate);
        // Set the navigation bar color to white using the Window
        getWindow().setNavigationBarColor(getResources().getColor(R.color.settings_bg));
        SystemBarHelper.setBackButtonVisible(getWindow(), false);
        mRootView = findViewById(R.id.setup_wizard_layout);
        setNextText(R.string.start);
        setSkipText(R.string.emergency_call);
        findViewById(R.id.start).setOnClickListener(view -> onNextPressed());
        findViewById(R.id.emerg_dialer)
                .setOnClickListener(view -> startEmergencyDialer());
        findViewById(R.id.launch_accessibility)
                .setOnClickListener(view -> startAccessibilitySettings());
        try {
            String inputJson = Settings.Secure.getString(getContentResolver(), Settings.Secure.THEME_CUSTOMIZATION_OVERLAY_PACKAGES);
            JSONObject inputJsonObj = new JSONObject(inputJson);
            inputJsonObj.put("android.theme.customization.system_palette", "1E2730");
            inputJsonObj.put("android.theme.customization.accent_color", "1E2730");
            Settings.Secure.putString(
                        getContentResolver(), Settings.Secure.THEME_CUSTOMIZATION_OVERLAY_PACKAGES, inputJsonObj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    float findPeakRefreshRate(Display.Mode[] modes) {
        float peakRefreshRate = DEFAULT_REFRESH_RATE;
        for (Display.Mode mode : modes) {
            if (Math.round(mode.getRefreshRate()) > peakRefreshRate) {
                peakRefreshRate = mode.getRefreshRate();
            }
        }
        return peakRefreshRate;
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.welcome_activity;
    }
}
