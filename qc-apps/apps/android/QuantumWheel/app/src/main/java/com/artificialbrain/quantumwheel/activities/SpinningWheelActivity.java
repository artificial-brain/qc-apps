package com.artificialbrain.quantumwheel.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.artificialbrain.quantumwheel.MainApplication;
import com.artificialbrain.quantumwheel.api.QuantumRandomNumber;
import com.artificialbrain.quantumwheel.R;
import com.artificialbrain.quantumwheel.api.RandomNumberInput;
import com.artificialbrain.quantumwheel.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rubikstudio.library.LuckyWheelView;
import rubikstudio.library.model.LuckyItem;

public class SpinningWheelActivity extends Activity {
    List<LuckyItem> data = new ArrayList<>();

    private ProgressBar progressBar;
    private Switch realDeviceSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.spinning_wheel_activity);

        final LuckyWheelView luckyWheelView = (LuckyWheelView) findViewById(R.id.luckyWheel);

        LuckyItem luckyItem1 = new LuckyItem();
        luckyItem1.topText = "1";
        luckyItem1.icon = R.drawable.test1;
        luckyItem1.color = 0xffFFF3E0;
        data.add(luckyItem1);

        LuckyItem luckyItem2 = new LuckyItem();
        luckyItem2.topText = "2";
        luckyItem2.icon = R.drawable.test2;
        luckyItem2.color = 0xffFFE0B2;
        data.add(luckyItem2);

        LuckyItem luckyItem3 = new LuckyItem();
        luckyItem3.topText = "3";
        luckyItem3.icon = R.drawable.test3;
        luckyItem3.color = 0xffFFCC80;
        data.add(luckyItem3);

        //////////////////
        LuckyItem luckyItem4 = new LuckyItem();
        luckyItem4.topText = "4";
        luckyItem4.icon = R.drawable.test4;
        luckyItem4.color = 0xffFFF3E0;
        data.add(luckyItem4);

        LuckyItem luckyItem5 = new LuckyItem();
        luckyItem5.topText = "5";
        luckyItem5.icon = R.drawable.test5;
        luckyItem5.color = 0xffFFE0B2;
        data.add(luckyItem5);

        LuckyItem luckyItem6 = new LuckyItem();
        luckyItem6.topText = "6";
        luckyItem6.icon = R.drawable.test6;
        luckyItem6.color = 0xffFFCC80;
        data.add(luckyItem6);
        //////////////////

        //////////////////////
        LuckyItem luckyItem7 = new LuckyItem();
        luckyItem7.topText = "7";
        luckyItem7.icon = R.drawable.test7;
        luckyItem7.color = 0xffFFF3E0;
        data.add(luckyItem7);

        LuckyItem luckyItem8 = new LuckyItem();
        luckyItem8.topText = "8";
        luckyItem8.icon = R.drawable.test8;
        luckyItem8.color = 0xffFFE0B2;
        data.add(luckyItem8);


//        LuckyItem luckyItem9 = new LuckyItem();
//        luckyItem9.topText = "900";
//        luckyItem9.icon = R.drawable.test9;
//        luckyItem9.color = 0xffFFCC80;
//        data.add(luckyItem9);
//        ////////////////////////
//
//        LuckyItem luckyItem10 = new LuckyItem();
//        luckyItem10.topText = "1000";
//        luckyItem10.icon = R.drawable.test10;
//        luckyItem10.color = 0xffFFE0B2;
//        data.add(luckyItem10);
//
//        LuckyItem luckyItem11 = new LuckyItem();
//        luckyItem11.topText = "2000";
//        luckyItem11.icon = R.drawable.test10;
//        luckyItem11.color = 0xffFFE0B2;
//        data.add(luckyItem11);
//
//        LuckyItem luckyItem12 = new LuckyItem();
//        luckyItem12.topText = "3000";
//        luckyItem12.icon = R.drawable.test10;
//        luckyItem12.color = 0xffFFE0B2;
//        data.add(luckyItem12);

        /////////////////////

        luckyWheelView.setData(data);
        luckyWheelView.setRound(5);

        /*luckyWheelView.setLuckyWheelBackgrouldColor(0xff0000ff);
        luckyWheelView.setLuckyWheelTextColor(0xffcc0000);
        luckyWheelView.setLuckyWheelCenterImage(getResources().getDrawable(R.drawable.icon));
        luckyWheelView.setLuckyWheelCursorImage(R.drawable.ic_cursor);*/

        progressBar = findViewById(R.id.quantum_progress_bar);

        Switch realDeviceSwitch = findViewById(R.id.real_device_switch);

        realDeviceSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (realDeviceSwitch.isChecked()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SpinningWheelActivity.this);

                    builder.setMessage(R.string.real_device_notification);

                    builder.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
        Button playButton = findViewById(R.id.play);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playButton.setEnabled(false);
                String api  = realDeviceSwitch.isChecked() ? Constants.API : "";
                String device = realDeviceSwitch.isChecked() ? Constants.IBM_DEVICE: "";
                RandomNumberInput randomNumberInput = new RandomNumberInput(3, api, device);

                progressBar.setVisibility(View.VISIBLE);
                MainApplication.apiManager.generateRandomNumber(randomNumberInput, new Callback<QuantumRandomNumber>() {
                    @Override
                    public void onResponse(Call<QuantumRandomNumber> call, Response<QuantumRandomNumber> response) {
                        playButton.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                        QuantumRandomNumber quantumRandomNumber = response.body();
                        if(quantumRandomNumber!=null){
                            luckyWheelView.startLuckyWheelWithTargetIndex
                                    (Integer.parseInt(quantumRandomNumber.getQuantum_random_num()));
                        }
                    }

                    @Override
                    public void onFailure(Call<QuantumRandomNumber> call, Throwable t) {
                        playButton.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SpinningWheelActivity.this,
                                "Error is " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        luckyWheelView.setLuckyRoundItemSelectedListener(new LuckyWheelView.LuckyRoundItemSelectedListener() {
            @Override
            public void LuckyRoundItemSelected(int index) {
                Toast.makeText(getApplicationContext(), data.get(index).topText + " is selected",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private int getRandomIndex() {
        Random rand = new Random();
        System.out.println(data.size());
        return rand.nextInt(data.size() - 1) + 0;
    }

    private int getRandomRound() {
        Random rand = new Random();
        return rand.nextInt(10) + 15;
    }
}
