package com.artificial.brain.quantumwheel.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import com.artificial.brain.quantumwheel.MainApplication;
import com.artificial.brain.quantumwheel.R;
import com.artificial.brain.quantumwheel.adapter.CustomAdapter;
import com.artificial.brain.quantumwheel.utils.Constants;

public class MainController extends AppCompatActivity {
    GridView mGridView;
    int[] rouletteImg = {R.drawable.fast_food, R.drawable.countries, R.drawable.freetime_activity, R.drawable.casino, R.drawable.heads_tails, R.drawable.yes_no, R.drawable.rock_paper, R.drawable.die, R.drawable.loves_me, R.drawable.color, R.drawable.lie_detector, R.drawable.days, R.drawable.zodiac, R.drawable.luck};
    String[] rouletteTitle = {"What to eat", "Where to go on Holidays?", "Freetime Activity", "Casino", "Heads/Tails", "Yes/No", "Rock-Paper-Scissors", "Roll the dice", "Loves me?", "Lucky Color", "Lie Detector", "Pick a day", "Zodiac Sign for You", "Lucky or Unlucky"};

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_main);
        mGridView = findViewById(R.id.gridView);
        mGridView.setAdapter(new CustomAdapter(getApplicationContext()));
        mGridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                pos(i);
            }
        });
    }

    private void pos(int i) {
        Intent intent = new Intent(this, MainSpinner.class);
        intent.putExtra("image", rouletteImg[i]);
        intent.putExtra("position", i);
        intent.putExtra("rouletteTitle", rouletteTitle[i]);
        Bundle logEventBundle = new Bundle();
        logEventBundle.putString(Constants.Roulette_Clicked, rouletteTitle[i]);
        MainApplication.firebaseAnalytics.logEvent(Constants.Roulette_Clicked, logEventBundle);
        startActivity(intent);
    }
}
