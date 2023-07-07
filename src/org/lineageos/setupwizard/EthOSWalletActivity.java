package org.lineageos.setupwizard;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.setupcompat.util.SystemBarHelper;

public class EthOSWalletActivity extends BaseSetupWizardActivity {

    public static final String TAG = EthOSWalletActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the navigation bar color to white using the Window
        getWindow().setNavigationBarColor(getResources().getColor(R.color.settings_bg));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.wallet_activity;
    }
}
