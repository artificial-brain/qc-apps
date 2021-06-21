package com.artificialbrain.quantumwheel.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.artificialbrain.quantumwheel.BuildConfig;
import com.artificialbrain.quantumwheel.MainApplication;
import com.artificialbrain.quantumwheel.models.Choice;
import com.artificialbrain.quantumwheel.models.QuantumRandomNumber;
import com.artificialbrain.quantumwheel.R;
import com.artificialbrain.quantumwheel.models.RandomNumberInput;
import com.artificialbrain.quantumwheel.utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rubikstudio.library.LuckyWheelView;
import rubikstudio.library.model.LuckyItem;

public class SpinningWheelActivity extends Activity {
    private List<LuckyItem> data = new ArrayList<>();
    private List<Integer> wheel_1st_color = Arrays.asList(0, 4, 6, 9, 13, 14);
    private List<Integer> wheel_2nd_color = Arrays.asList(1, 3, 7, 10, 12);
    private List<Integer> wheel_3rd_color = Arrays.asList(2, 5, 8, 11, 15);
    private ArrayList<Choice> choiceList = null;
    private boolean customChoices = false;

    private ProgressBar progressBar;
    private LuckyWheelView luckyWheelView;
    private EditText numberEditText;
    private Switch realDeviceSwitch;
    private Button playButton;
    private ImageButton shareButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.spinning_wheel_activity);

        luckyWheelView  = findViewById(R.id.luckyWheel);

        shareButton = findViewById(R.id.share_button);


        if (getIntent().getSerializableExtra("choiceList")!=null) {
            choiceList = (ArrayList<Choice>) getIntent().getExtras().getSerializable("choiceList");
            for (int i = 0; i < choiceList.size(); i++) {
                LuckyItem luckyItem = new LuckyItem();
                Choice choice = choiceList.get(i);
                luckyItem.topText = choice.getChoiceName();
                if (wheel_1st_color.contains(i)){
                    luckyItem.color =  getResources().getColor(R.color.wheel_1st_color);
                } else if (wheel_2nd_color.contains(i)){
                    luckyItem.color =  getResources().getColor(R.color.wheel_2nd_color);
                } else if (wheel_3rd_color.contains(i)){
                    luckyItem.color =  getResources().getColor(R.color.wheel_3rd_color);
                }
                data.add(luckyItem);
                customChoices = true;
            }
        } else {
            addDefaultItems();
            customChoices = false;
        }

        luckyWheelView.setData(data);
        luckyWheelView.setRound(5);

        /*luckyWheelView.setLuckyWheelBackgrouldColor(0xff0000ff);
        luckyWheelView.setLuckyWheelTextColor(0xffcc0000);
        luckyWheelView.setLuckyWheelCenterImage(getResources().getDrawable(R.drawable.icon));
        luckyWheelView.setLuckyWheelCursorImage(R.drawable.ic_cursor);*/

        progressBar = findViewById(R.id.quantum_progress_bar);

        numberEditText = (EditText) findViewById(R.id.enter_text);

        realDeviceSwitch = findViewById(R.id.real_device_switch);

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
        playButton = findViewById(R.id.play);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!customChoices && numberEditText.getText().toString().trim().trim().length() == 0){
                    Toast.makeText(SpinningWheelActivity.this,
                            R.string.no_input_to_bet, Toast.LENGTH_LONG).show();
                } else if (!customChoices && !(Integer.parseInt(numberEditText.getText().toString().trim()) > 0
                        && Integer.parseInt(numberEditText.getText().toString().trim()) <9)) {
                    Toast.makeText(SpinningWheelActivity.this,R.string.input_not_in_range, Toast.LENGTH_LONG).show();
                } else {
                    callRandomNumberAPI();
                }
            }
        });

        luckyWheelView.setLuckyRoundItemSelectedListener(new LuckyWheelView.LuckyRoundItemSelectedListener() {
            @Override
            public void LuckyRoundItemSelected(int index) {
                String result;
                if (customChoices){
                    result = data.get(index).topText + " is chosen by Nature for you!";
                } else {
                    result = numberEditText.getText().toString().equals(data.get(index).topText)
                            ? getResources().getString(R.string.won_message) :
                            getResources().getString(R.string.lost_message);
                }
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            }
        });

        Button customize = findViewById(R.id.customize_button);
        customize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(SpinningWheelActivity.this, ChoiceActivity.class);
                startActivity(intent);
            }
        });
        if (customChoices){
            findViewById(R.id.enter_text_layout).setVisibility(View.GONE);
        }
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareApp();
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

    private void addDefaultItems() {
        LuckyItem luckyItem1 = new LuckyItem();
        luckyItem1.topText = "1";
        luckyItem1.color = getResources().getColor(R.color.wheel_1st_color);
        data.add(luckyItem1);

        LuckyItem luckyItem2 = new LuckyItem();
        luckyItem2.topText = "2";
        luckyItem2.color = getResources().getColor(R.color.wheel_2nd_color);
        data.add(luckyItem2);

        LuckyItem luckyItem3 = new LuckyItem();
        luckyItem3.topText = "3";
        luckyItem3.color = getResources().getColor(R.color.wheel_3rd_color);
        data.add(luckyItem3);

        LuckyItem luckyItem4 = new LuckyItem();
        luckyItem4.topText = "4";
        luckyItem4.color = getResources().getColor(R.color.wheel_1st_color);
        data.add(luckyItem4);

        LuckyItem luckyItem5 = new LuckyItem();
        luckyItem5.topText = "5";
        luckyItem5.color = getResources().getColor(R.color.wheel_2nd_color);
        data.add(luckyItem5);

        LuckyItem luckyItem6 = new LuckyItem();
        luckyItem6.topText = "6";
        luckyItem6.color = getResources().getColor(R.color.wheel_3rd_color);
        data.add(luckyItem6);

        LuckyItem luckyItem7 = new LuckyItem();
        luckyItem7.topText = "7";
        luckyItem7.color = getResources().getColor(R.color.wheel_1st_color);
        data.add(luckyItem7);

        LuckyItem luckyItem8 = new LuckyItem();
        luckyItem8.topText = "8";
        luckyItem8.color = getResources().getColor(R.color.wheel_2nd_color);
        data.add(luckyItem8);
    }

    private void callRandomNumberAPI() {
        playButton.setEnabled(false);
        int length = (int)(Math.log(data.size()) / Math.log(2));
        String provider = realDeviceSwitch.isChecked() ?
                Constants.IBM_PROVIDER : Constants.GOOGLE_PROVIDER;
        String api = realDeviceSwitch.isChecked() ? Constants.API : "";
        String device = realDeviceSwitch.isChecked() ? Constants.IBM_DEVICE : "";
        RandomNumberInput randomNumberInput = new RandomNumberInput(length, provider, api, device);

        progressBar.setVisibility(View.VISIBLE);
        MainApplication.apiManager.generateRandomNumber(randomNumberInput, new Callback<QuantumRandomNumber>() {
            @Override
            public void onResponse(Call<QuantumRandomNumber> call, Response<QuantumRandomNumber> response) {
                playButton.setEnabled(true);
                progressBar.setVisibility(View.GONE);
                QuantumRandomNumber quantumRandomNumber = response.body();
                if (quantumRandomNumber != null) {
                    luckyWheelView.startLuckyWheelWithTargetIndex
                            (Integer.parseInt(quantumRandomNumber.getQuantum_random_num()));
                } else {
                    Toast.makeText(SpinningWheelActivity.this, R.string.something_wrong_error, Toast.LENGTH_LONG).show();
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

    private void shareApp(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Hi, I just now made a choice using Quantum Wheel using the principle of Quantum Superposition. To download the app: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
}
