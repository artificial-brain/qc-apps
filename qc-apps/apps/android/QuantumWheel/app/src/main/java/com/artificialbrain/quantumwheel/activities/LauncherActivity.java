package com.artificialbrain.quantumwheel.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.artificialbrain.quantumwheel.R;


public class LauncherActivity extends Activity {

    private Button launcherButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        launcherButton = findViewById(R.id.launcher_button);
        setOnClickListeners();
    }

    private void setOnClickListeners(){
        launcherButton.setOnClickListener(launcherButtonClickListener);
    }

    View.OnClickListener launcherButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(LauncherActivity.this, SpinningWheelActivity.class);
            startActivity(intent);
            finish();
        }
    };
}