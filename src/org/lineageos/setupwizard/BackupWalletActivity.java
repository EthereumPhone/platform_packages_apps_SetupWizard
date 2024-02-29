package org.lineageos.setupwizard;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import java.lang.reflect.*;
import java.security.SecureRandom;
import java.security.Security;

import android.text.Editable;
import android.widget.EditText;
import android.content.Context;
import android.text.TextWatcher;
import android.widget.ProgressBar;
import android.widget.Button;
import android.widget.TextView;
import android.animation.ObjectAnimator;

import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.SecureRandom;
import java.security.Security;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.util.encoders.Hex;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;

import com.google.android.setupcompat.util.SystemBarHelper;

public class BackupWalletActivity extends BaseSetupWizardActivity {

    public static final String TAG = BackupWalletActivity.class.getSimpleName();
    private SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the navigation bar color to white using the Window
        getWindow().setNavigationBarColor(getResources().getColor(R.color.settings_bg));
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.backup_wallet_activity;
    }

    @Override
    protected int getTitleResId() {
        return R.string.wallet_backup_title;
    }

    @Override
    protected int getIconResId() {
        return R.drawable.ic_lock_screen;
    }
}
