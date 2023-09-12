package org.lineageos.setupwizard;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import java.lang.reflect.*;
import android.text.Editable;
import android.widget.EditText;
import android.content.Context;
import android.text.TextWatcher;
import android.widget.ProgressBar;

import com.google.android.setupcompat.util.SystemBarHelper;

public class EthOSWalletActivity extends BaseSetupWizardActivity {

    public static final String TAG = EthOSWalletActivity.class.getSimpleName();

    private boolean walletExists = false;
    private Context context;
    private EditText walletInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the navigation bar color to white using the Window
        getWindow().setNavigationBarColor(getResources().getColor(R.color.settings_bg));
        makeNextButtonInvisible();
        findViewById(R.id.generate_wallet_button).setOnClickListener(view -> generateNewWallet());
        walletInput = findViewById(R.id.wallet_input);
        walletInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String wallet = s.toString();
                // Check if its a valid ethereum private key
                if (wallet.length() == 64) {
                    makeNextButtonVisible();
                } else {
                    makeNextButtonInvisible();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Do nothing
            }
        });
        context = this;
    }

    public void changeSpinner(Context inContext, boolean turnOn) {
        final ProgressBar progressBar = findViewById(R.id.loading_spinner);
        if (turnOn) {
            ((Activity) inContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.VISIBLE);
                }
            });
        } else {
            ((Activity) inContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void onNavigateNext() {
        super.onNavigateNext();
        String wallet = walletInput.getText().toString();
        if (wallet.length() == 64) {
            changeSpinner(context, true);
            Runnable runImportWallet = new Runnable() {
                @Override
                public void run() {
                    importWallet(wallet);
                    changeSpinner(context, false);
                }
            };
            new Thread(runImportWallet).start();
        }
    }

    public void importWallet(String wallet) {
        try {
            Object walletProxy = context.getSystemService("privatewallet");
            Class cls = Class.forName("android.os.PrivateWalletProxy");
            Method method = cls.getDeclaredMethod("importNewWallet", String.class);
            method.invoke(walletProxy, wallet);
        } catch (Exception e) {
            System.out.println(TAG+": Failed to import wallet");
            e.printStackTrace();
        }
    }

    protected void generateNewWallet() {
        changeSpinner(context, true);
        Runnable runGenerateWallet = new Runnable() {
            @Override
            public void run() {
                try {
                    Object walletProxy = context.getSystemService("privatewallet");
                    Class cls = Class.forName("android.os.PrivateWalletProxy");
                    Method method = cls.getDeclaredMethod("createNewWallet");
                    method.invoke(walletProxy);
                    onNextPressed();
                } catch (Exception e) {
                    System.out.println(TAG+": Failed to generate wallet");
                    e.printStackTrace();
                }
                changeSpinner(context, false);
            }
        };
        new Thread(runGenerateWallet).start();
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.wallet_activity;
    }
}
