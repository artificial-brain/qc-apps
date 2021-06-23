package com.artificial.brain.quantumwheel.activities;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.artificial.brain.quantumwheel.MainApplication;
import com.artificial.brain.quantumwheel.R;
import com.artificial.brain.quantumwheel.utils.Constants;

import java.io.PrintStream;

public class Splash extends AppCompatActivity {
    Button mPlay;
    Button mRateUs;
    Button mShare;
    ImageView mSplash_rotating_img;
    TextView instructions;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_splash_screen);
        Typeface face = Typeface.createFromAsset(getAssets(), "champagne.ttf");
        instructions = findViewById(R.id.instructions);
        instructions.setTypeface(face);
        mSplash_rotating_img = findViewById(R.id.splash_rotating_img);
        mPlay = findViewById(R.id.play_btn);
        mShare = findViewById(R.id.share_btn);
        mRateUs = findViewById(R.id.like_btn);
        Animation loadAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        loadAnimation.setFillAfter(true);
        mSplash_rotating_img.startAnimation(loadAnimation);
        mPlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle logEventBundle = new Bundle();
                logEventBundle.putString(Constants.User_Clicked, "Splash Screen");
                MainApplication.firebaseAnalytics.logEvent(Constants.User_Clicked, logEventBundle);
                startActivity(new Intent(Splash.this, MainController.class));
            }
        });
        mShare.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle logEventBundle = new Bundle();
                logEventBundle.putString(Constants.Spin_Share_Clicked, Constants.Splash_Share_Clicked);
                MainApplication.firebaseAnalytics.logEvent(Constants.Splash_Share_Clicked, logEventBundle);

                String appName = getString(R.string.app_name);
                try {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.SEND");
                    intent.setType("text/plain");
                    String sb = getResources().getString(R.string.share_description) +
                            "-\n" +
                            "https://play.google.com/store/apps/details?id=" + getPackageName();
                    intent.putExtra("android.intent.extra.TEXT", sb);
                    startActivity(Intent.createChooser(intent, "Share App : " +appName));
                } catch (Exception unused) {
                }
            }
        });
        mRateUs.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {

                Bundle logEventBundle = new Bundle();
                logEventBundle.putString(Constants.Splash_RateUs_Clicked, Constants.Splash_RateUs_Clicked);
                MainApplication.firebaseAnalytics.logEvent(Constants.Splash_RateUs_Clicked, logEventBundle);

                String str = "android.intent.action.VIEW";
                PrintStream printStream = System.out;
                String sb = "******" +
                        getPackageName();
                printStream.println(sb);
                try {
                    Splash splash = Splash.this;
                    String sb2 = "market://details?id=" +
                            getPackageName();
                    splash.startActivity(new Intent(str, Uri.parse(sb2)));
                } catch (ActivityNotFoundException unused) {
                    Splash splash2 = Splash.this;
                    String sb3 = "https://play.google.com/store/apps/details?id=" +
                            getPackageName();
                    splash2.startActivity(new Intent(str, Uri.parse(sb3)));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    private void showExitDialog() {
        String str = "No";
        new AlertDialog.Builder(this).setIcon(R.mipmap.ic_launcher).setTitle(R.string.app_name).setMessage("Are you sure you want to exit?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        }).setNegativeButton(str, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).show();
    }
}
