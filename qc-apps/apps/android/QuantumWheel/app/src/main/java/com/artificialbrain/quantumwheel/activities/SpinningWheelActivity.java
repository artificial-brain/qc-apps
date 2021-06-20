package com.artificialbrain.quantumwheel.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.artificialbrain.quantumwheel.MainApplication;
import com.artificialbrain.quantumwheel.models.Choice;
import com.artificialbrain.quantumwheel.models.QuantumRandomNumber;
import com.artificialbrain.quantumwheel.R;
import com.artificialbrain.quantumwheel.models.RandomNumberInput;
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
    ArrayList<Choice> choiceList = null;
    boolean customChoices = false;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.spinning_wheel_activity);

        final LuckyWheelView luckyWheelView = findViewById(R.id.luckyWheel);

        if (getIntent().getSerializableExtra("choiceList")!=null) {
            choiceList = (ArrayList<Choice>) getIntent().getExtras().getSerializable("choiceList");
            for (int i = 0; i < choiceList.size(); i++) {
                LuckyItem luckyItem = new LuckyItem();
                Choice choice = choiceList.get(i);
                luckyItem.topText = choice.getChoiceName();
                luckyItem.color = 0xffFFF3E0;
                data.add(luckyItem);
                customChoices = true;
            }
        } else {
            addDefaultItems();
        }

        luckyWheelView.setData(data);
        luckyWheelView.setRound(5);

        /*luckyWheelView.setLuckyWheelBackgrouldColor(0xff0000ff);
        luckyWheelView.setLuckyWheelTextColor(0xffcc0000);
        luckyWheelView.setLuckyWheelCenterImage(getResources().getDrawable(R.drawable.icon));
        luckyWheelView.setLuckyWheelCursorImage(R.drawable.ic_cursor);*/

        progressBar = findViewById(R.id.quantum_progress_bar);

        EditText numberEditText = (EditText) findViewById(R.id.enter_text);

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
//                Toast.makeText(SpinningWheelActivity.this,
//                        R.string.no_input_to_bet, Toast.LENGTH_LONG).show();
                if (!customChoices & numberEditText.getText().toString().trim().trim().length() == 0){
                    Toast.makeText(SpinningWheelActivity.this,
                            R.string.no_input_to_bet, Toast.LENGTH_LONG).show();
                    return;
                }

                int inputNumber = Integer.parseInt(numberEditText.getText().toString().trim());

                if (!(inputNumber > 0 && inputNumber <9))
                {
                    Toast.makeText(SpinningWheelActivity.this,R.string.input_not_in_range, Toast.LENGTH_LONG).show();
                    return;
                }

                playButton.setEnabled(false);
                int length = (int)(Math.log(data.size()) / Math.log(2));
                String api = realDeviceSwitch.isChecked() ? Constants.API : "";
                String device = realDeviceSwitch.isChecked() ? Constants.IBM_DEVICE : "";
                RandomNumberInput randomNumberInput = new RandomNumberInput(length, api, device);

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
        });

        luckyWheelView.setLuckyRoundItemSelectedListener(new LuckyWheelView.LuckyRoundItemSelectedListener() {
            @Override
            public void LuckyRoundItemSelected(int index) {
                String result;
                if (customChoices){
                    result = data.get(index).topText;
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
        luckyItem1.color = 0xffFFF3E0;
        data.add(luckyItem1);

        LuckyItem luckyItem2 = new LuckyItem();
        luckyItem2.topText = "2";
        luckyItem2.color = 0xffFFE0B2;
        data.add(luckyItem2);

        LuckyItem luckyItem3 = new LuckyItem();
        luckyItem3.topText = "3";
        luckyItem3.color = 0xffFFCC80;
        data.add(luckyItem3);

        LuckyItem luckyItem4 = new LuckyItem();
        luckyItem4.topText = "4";
        luckyItem4.color = 0xffFFF3E0;
        data.add(luckyItem4);

        LuckyItem luckyItem5 = new LuckyItem();
        luckyItem5.topText = "5";
        luckyItem5.color = 0xffFFE0B2;
        data.add(luckyItem5);

        LuckyItem luckyItem6 = new LuckyItem();
        luckyItem6.topText = "6";
        luckyItem6.color = 0xffFFCC80;
        data.add(luckyItem6);

        LuckyItem luckyItem7 = new LuckyItem();
        luckyItem7.topText = "7";
        luckyItem7.color = 0xffFFF3E0;
        data.add(luckyItem7);

        LuckyItem luckyItem8 = new LuckyItem();
        luckyItem8.topText = "8";
        luckyItem8.color = 0xffFFE0B2;
        data.add(luckyItem8);
    }
}
