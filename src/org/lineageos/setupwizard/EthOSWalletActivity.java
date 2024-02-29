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

public class EthOSWalletActivity extends BaseSetupWizardActivity {

    public static final String TAG = EthOSWalletActivity.class.getSimpleName();
    private SharedPreferences prefs;

    private boolean walletExists = false;
    private Context context;
    private EditText walletInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the navigation bar color to white using the Window
        getWindow().setNavigationBarColor(getResources().getColor(R.color.settings_bg));
        makeNextButtonInvisible();
        prefs = this.getSharedPreferences("WalletPrefs_setup", Context.MODE_PRIVATE);
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
        if (hasWalletCompleted()) {
            onNextPressed();
        }
    }

    public boolean hasWalletCompleted() {
        return prefs.getBoolean("wallet_setup_was_completed", false);
    }

    public void completedWalletSetup() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("wallet_setup_was_completed", true);
        editor.apply();
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
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    public void disableButton(Context inContext) {
        final Button mButton = findViewById(R.id.generate_wallet_button);
        ((Activity) inContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mButton.setEnabled(false);
                }
        });
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
                    completedWalletSetup();
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

    public String createEthereumPrivateKey() {
        // Ensure BouncyCastle is initialized
        Security.addProvider(new BouncyCastleProvider());

        // Create a secure random number generator
        SecureRandom secureRandom = new SecureRandom();

        // Specify the curve parameters for Ethereum (secp256k1)
        X9ECParameters curveParams = org.bouncycastle.asn1.sec.SECNamedCurves.getByName("secp256k1");
        ECDomainParameters domainParameters = new ECDomainParameters(curveParams.getCurve(), curveParams.getG(), curveParams.getN(), curveParams.getH());

        // Generate the key pair
        ECKeyPairGenerator keyGen = new ECKeyPairGenerator();
        keyGen.init(new ECKeyGenerationParameters(domainParameters, secureRandom));
        AsymmetricCipherKeyPair keyPair = keyGen.generateKeyPair();

        // Extract the private key and return it as a hex string
        ECPrivateKeyParameters privateKeyParams = (ECPrivateKeyParameters) keyPair.getPrivate();
        return Hex.toHexString(privateKeyParams.getD().toByteArray());
    }

    protected void generateNewWallet() {
        changeSpinner(context, true);
        disableButton(context);

        final TextView pulsatingText = findViewById(R.id.pulsatingText);
        ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pulsatingText.setVisibility(View.VISIBLE);
                    ObjectAnimator animator = ObjectAnimator.ofFloat(pulsatingText, "alpha", 0.0f, 1.0f);
                    animator.setDuration(1000);
                    animator.setRepeatCount(ObjectAnimator.INFINITE);
                    animator.setRepeatMode(ObjectAnimator.REVERSE);

                    // Start the animation
                    animator.start();
                }
        });

        Runnable runGenerateWallet = new Runnable() {
            @Override
            public void run() {
                String newPrivateKey = createEthereumPrivateKey();
                importWallet(newPrivateKey);
                completedWalletSetup();
                changeSpinner(context, false);
                onNextPressed();
            }
        };
        new Thread(runGenerateWallet).start();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.wallet_activity;
    }
}
