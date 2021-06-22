package com.artificial.brain.quantumwheel.activities;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.artificial.brain.quantumwheel.MainApplication;
import com.artificial.brain.quantumwheel.R;
import com.artificial.brain.quantumwheel.models.QuantumRandomNumber;
import com.artificial.brain.quantumwheel.models.RandomNumberInput;
import com.artificial.brain.quantumwheel.utils.Constants;

import java.io.PrintStream;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainSpinner extends AppCompatActivity {
    private static float FACTOR;
    int degree = 0;
    int degree_old = 0;
    TextView mCurrentRouletteTitle;
    ImageView mPanel;
    TextView mRouletteResult;
    ImageView mSelectedRouletteImg;
    Button mSpinBtn;
    String name;
    int pos;
    private MediaPlayer mMediaPlayer = new MediaPlayer();



    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_main_spin);
        mSpinBtn = findViewById(R.id.spin_btn);
        mRouletteResult = findViewById(R.id.current_text);
        mSelectedRouletteImg = findViewById(R.id.ic_wheel);
        mCurrentRouletteTitle = findViewById(R.id.current_roulette_title);
        mPanel = findViewById(R.id.panel);
        Intent intent = getIntent();
        mSelectedRouletteImg.setImageResource(intent.getIntExtra("image", 0));
        pos = intent.getIntExtra("position", 0);
        mCurrentRouletteTitle.setText(name);
        mSpinBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mPanel.setVisibility(View.VISIBLE);
                MainSpinner mainSpinner = MainSpinner.this;
                mainSpinner.degree_old = mainSpinner.degree % 360;
                MainSpinner mainSpinner2 = MainSpinner.this;
                int length = 12;
                String provider = false ?
                        Constants.IBM_PROVIDER : Constants.GOOGLE_PROVIDER;
                String api = false ? Constants.API : "";
                String device = false ? Constants.IBM_DEVICE : "";
                RandomNumberInput randomNumberInput = new RandomNumberInput(length, provider, api, device);
                MainApplication.apiManager.generateRandomNumber(randomNumberInput, new Callback<QuantumRandomNumber>() {
                    @Override
                    public void onResponse(Call<QuantumRandomNumber> call,
                                           Response<QuantumRandomNumber> response) {
                        QuantumRandomNumber quantumRandomNumber = response.body();
                        mPanel.setVisibility(View.VISIBLE);
                        if (quantumRandomNumber != null) {
                            mainSpinner2.degree = quantumRandomNumber.getQuantum_random_num();
                            spin(mainSpinner2);
                        } else {
                            Toast.makeText(MainSpinner.this, R.string.something_wrong_error,
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<QuantumRandomNumber> call, Throwable t) {
                        mPanel.setVisibility(View.VISIBLE);
                        Toast.makeText(MainSpinner.this, R.string.something_wrong_error,
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        mPanel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mPanel.setVisibility(View.GONE);
                mRouletteResult.setText("");
                mSpinBtn.setAlpha(1.0f);
                mSpinBtn.setClickable(true);
            }
        });
    }

    private void spin(MainSpinner mainSpinner2){
//      mainSpinner2.degree = mainSpinner2.rand.nextInt(3600) + 720;
        RotateAnimation rotateAnimation = new RotateAnimation((float) degree_old,
                (float) degree, 1, 0.5f, 1, 0.5f);
        rotateAnimation.setDuration(3600);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setInterpolator(new DecelerateInterpolator());
        rotateAnimation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
                mSpinBtn.setAlpha(0.95f);
                mSpinBtn.setClickable(false);
                mPanel.setVisibility(View.GONE);
                playAssetSound();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                PrintStream printStream = System.out;
                printStream.println("*******" + pos);
                mMediaPlayer.reset();
                switch (pos) {
                    case 0:
                        MainSpinner.FACTOR = 22.5f;
                        mRouletteResult.setText(currentNumber(360 - (degree % 360)));
                        mPanel.setVisibility(View.VISIBLE);
                        return;
                    case 1:
                        MainSpinner.FACTOR = 30.0f;
                        mRouletteResult.setText(currentNumber1(360 - (degree % 360)));
                        mPanel.setVisibility(View.VISIBLE);
                        return;
                    case 2:
                        MainSpinner.FACTOR = 20.0f;
                        mRouletteResult.setText(currentNumber2(360 - (degree % 360)));
                        mPanel.setVisibility(View.VISIBLE);
                        return;
                    case 3:
                        MainSpinner.FACTOR = 30.0f;
                        mRouletteResult.setText(currentNumber3(360 - (degree % 360)));
                        mPanel.setVisibility(View.VISIBLE);
                        return;
                    case 4:
                        MainSpinner.FACTOR = 22.5f;
                        mRouletteResult.setText(currentNumber4(360 - (degree % 360)));
                        mPanel.setVisibility(View.VISIBLE);
                        return;
                    case 5:
                        MainSpinner.FACTOR = 4.86f;
                        mRouletteResult.setText(currentNumber5(360 - (degree % 360)));
                        mPanel.setVisibility(View.VISIBLE);
                        return;
                    case 6:
                        MainSpinner.FACTOR = 15.0f;
                        mRouletteResult.setText(currentNumber6(360 - (degree % 360)));
                        mPanel.setVisibility(View.VISIBLE);
                        return;
                    case 7:
                        MainSpinner.FACTOR = 15.0f;
                        mRouletteResult.setText(currentNumber7(360 - (degree % 360)));
                        mPanel.setVisibility(View.VISIBLE);
                        return;
                    case 8:
                        MainSpinner.FACTOR = 18.0f;
                        mRouletteResult.setText(currentNumber8(360 - (degree % 360)));
                        mPanel.setVisibility(View.VISIBLE);
                        return;
                    case 9:
                        MainSpinner.FACTOR = 15.0f;
                        mRouletteResult.setText(currentNumber9(360 - (degree % 360)));
                        mPanel.setVisibility(View.VISIBLE);
                        return;
                    case 10:
                        MainSpinner.FACTOR = 25.7f;
                        mRouletteResult.setText(currentNumber10(360 - (degree % 360)));
                        mPanel.setVisibility(View.VISIBLE);
                        return;
                    case 11:
                        MainSpinner.FACTOR = 15.0f;
                        mRouletteResult.setText(currentNumber11(360 - (degree % 360)));
                        mPanel.setVisibility(View.VISIBLE);
                        return;
                    case 12:
                        MainSpinner.FACTOR = 15.0f;
                        mRouletteResult.setText(currentNumber12(360 - (degree % 360)));
                        mPanel.setVisibility(View.VISIBLE);
                        return;
                    case 13:
                        MainSpinner.FACTOR = 30.0f;
                        mRouletteResult.setText(currentNumber13(360 - (degree % 360)));
                        mPanel.setVisibility(View.VISIBLE);
                        return;
                    default:
                        return;
                }
            }
        });
        mSelectedRouletteImg.startAnimation(rotateAnimation);
    }

    private void playAssetSound() {
        try {
            String assetName = "slot_machine_jackpot.wav";
            AssetFileDescriptor afd = this.getAssets().openFd(assetName);
            mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (Exception ex) {
        }
    }

    private String currentNumber(int i) {
        float f = (float) i;
        float f2 = FACTOR;
        String str = "Tails";
        String str2 = (f < f2 * 1.0f || f > f2 * 3.0f) ? "" : str;
        float f3 = FACTOR;
        String str3 = "Heads";
        if (f >= 3.0f * f3 && f <= f3 * 5.0f) {
            str2 = str3;
        }
        float f4 = FACTOR;
        if (f >= 5.0f * f4 && f <= f4 * 7.0f) {
            str2 = str;
        }
        float f5 = FACTOR;
        if (f >= 7.0f * f5 && f <= f5 * 9.0f) {
            str2 = str3;
        }
        float f6 = FACTOR;
        if (f >= 9.0f * f6 && f <= f6 * 11.0f) {
            str2 = str;
        }
        float f7 = FACTOR;
        if (f >= 11.0f * f7 && f <= f7 * 13.0f) {
            str2 = str3;
        }
        float f8 = FACTOR;
        if (f >= 13.0f * f8 && f <= f8 * 15.0f) {
            str2 = str;
        }
        return ((f < FACTOR * 15.0f || i >= 360) && (i < 0 || f > FACTOR * 1.0f)) ? str2 : str3;
    }

    private String currentNumber1(int i) {
        float f = (float) i;
        float f2 = FACTOR;
        String str = "No";
        String str2 = (f < f2 * 1.0f || f > f2 * 3.0f) ? "" : str;
        float f3 = FACTOR;
        String str3 = "Yes";
        if (f >= 3.0f * f3 && f <= f3 * 5.0f) {
            str2 = str3;
        }
        float f4 = FACTOR;
        if (f >= 5.0f * f4 && f <= f4 * 7.0f) {
            str2 = str;
        }
        float f5 = FACTOR;
        if (f >= 7.0f * f5 && f <= f5 * 9.0f) {
            str2 = str3;
        }
        float f6 = FACTOR;
        if (f >= 9.0f * f6 && f <= f6 * 11.0f) {
            str2 = str;
        }
        return ((f < FACTOR * 11.0f || i >= 360) && (i < 0 || f > FACTOR * 1.0f)) ? str2 : str3;
    }

    private String currentNumber2(int i) {
        float f = (float) i;
        float f2 = FACTOR;
        String str = "Paper";
        String str2 = (f < f2 * 1.0f || f > f2 * 3.0f) ? "" : str;
        float f3 = FACTOR;
        String str3 = "Scissors";
        if (f >= 3.0f * f3 && f <= f3 * 5.0f) {
            str2 = str3;
        }
        float f4 = FACTOR;
        String str4 = "Rock";
        if (f >= 5.0f * f4 && f <= f4 * 7.0f) {
            str2 = str4;
        }
        float f5 = FACTOR;
        if (f >= 7.0f * f5 && f <= f5 * 9.0f) {
            str2 = str;
        }
        float f6 = FACTOR;
        if (f >= 9.0f * f6 && f <= f6 * 11.0f) {
            str2 = str3;
        }
        float f7 = FACTOR;
        if (f >= 11.0f * f7 && f <= f7 * 13.0f) {
            str2 = str4;
        }
        float f8 = FACTOR;
        if (f >= 13.0f * f8 && f <= f8 * 15.0f) {
            str2 = str;
        }
        float f9 = FACTOR;
        if (f >= 15.0f * f9 && f <= f9 * 17.0f) {
            str2 = str3;
        }
        return ((f < FACTOR * 17.0f || i >= 360) && (i < 0 || f > FACTOR * 1.0f)) ? str2 : str4;
    }

    private String currentNumber3(int i) {
        float f = (float) i;
        float f2 = FACTOR;
        String str = (f < f2 * 1.0f || f > f2 * 3.0f) ? "" : "6";
        float f3 = FACTOR;
        if (f >= 3.0f * f3 && f <= f3 * 5.0f) {
            str = "5";
        }
        float f4 = FACTOR;
        if (f >= 5.0f * f4 && f <= f4 * 7.0f) {
            str = "4";
        }
        float f5 = FACTOR;
        if (f >= 7.0f * f5 && f <= f5 * 9.0f) {
            str = "3";
        }
        float f6 = FACTOR;
        if (f >= 9.0f * f6 && f <= f6 * 11.0f) {
            str = "2";
        }
        return ((f < FACTOR * 11.0f || i >= 360) && (i < 0 || f > FACTOR * 1.0f)) ? str : "1";
    }

    private String currentNumber4(int i) {
        float f = (float) i;
        float f2 = FACTOR;
        String str = "Loves me not";
        String str2 = (f < f2 * 1.0f || f > f2 * 3.0f) ? "" : str;
        float f3 = FACTOR;
        String str3 = "Loves me";
        if (f >= 3.0f * f3 && f <= f3 * 5.0f) {
            str2 = str3;
        }
        float f4 = FACTOR;
        if (f >= 5.0f * f4 && f <= f4 * 7.0f) {
            str2 = str;
        }
        float f5 = FACTOR;
        if (f >= 7.0f * f5 && f <= f5 * 9.0f) {
            str2 = str3;
        }
        float f6 = FACTOR;
        if (f >= 9.0f * f6 && f <= f6 * 11.0f) {
            str2 = str;
        }
        float f7 = FACTOR;
        if (f >= 11.0f * f7 && f <= f7 * 13.0f) {
            str2 = str3;
        }
        float f8 = FACTOR;
        if (f >= 13.0f * f8 && f <= f8 * 15.0f) {
            str2 = str;
        }
        return ((f < FACTOR * 15.0f || i >= 360) && (i < 0 || f > FACTOR * 1.0f)) ? str2 : str3;
    }

    private String currentNumber5(int i) {
        float f = (float) i;
        float f2 = FACTOR;
        String str = (f < f2 * 1.0f || f > f2 * 3.0f) ? "" : "35";
        float f3 = FACTOR;
        if (f >= 3.0f * f3 && f <= f3 * 5.0f) {
            str = "3";
        }
        float f4 = FACTOR;
        if (f >= 5.0f * f4 && f <= f4 * 7.0f) {
            str = "26";
        }
        float f5 = FACTOR;
        if (f >= 7.0f * f5 && f <= f5 * 9.0f) {
            str = "0";
        }
        float f6 = FACTOR;
        if (f >= 9.0f * f6 && f <= f6 * 11.0f) {
            str = "32";
        }
        float f7 = FACTOR;
        if (f >= 11.0f * f7 && f <= f7 * 13.0f) {
            str = "15";
        }
        float f8 = FACTOR;
        if (f >= 13.0f * f8 && f <= f8 * 15.0f) {
            str = "19";
        }
        float f9 = FACTOR;
        if (f >= 15.0f * f9 && f <= f9 * 17.0f) {
            str = "4";
        }
        float f10 = FACTOR;
        if (f >= 17.0f * f10 && f <= f10 * 19.0f) {
            str = "21";
        }
        float f11 = FACTOR;
        if (f >= 19.0f * f11 && f <= f11 * 21.0f) {
            str = "2";
        }
        float f12 = FACTOR;
        if (f >= 21.0f * f12 && f <= f12 * 23.0f) {
            str = "25";
        }
        float f13 = FACTOR;
        if (f >= 23.0f * f13 && f <= f13 * 25.0f) {
            str = "17";
        }
        float f14 = FACTOR;
        if (f >= 25.0f * f14 && f <= f14 * 27.0f) {
            str = "34";
        }
        float f15 = FACTOR;
        if (f >= 27.0f * f15 && f <= f15 * 29.0f) {
            str = "6";
        }
        float f16 = FACTOR;
        if (f >= 29.0f * f16 && f <= f16 * 31.0f) {
            str = "27";
        }
        float f17 = FACTOR;
        if (f >= 31.0f * f17 && f <= f17 * 33.0f) {
            str = "13";
        }
        float f18 = FACTOR;
        if (f >= 33.0f * f18 && f <= f18 * 35.0f) {
            str = "36";
        }
        float f19 = FACTOR;
        if (f >= 35.0f * f19 && f <= f19 * 37.0f) {
            str = "11";
        }
        float f20 = FACTOR;
        if (f >= 37.0f * f20 && f <= f20 * 39.0f) {
            str = "30";
        }
        float f21 = FACTOR;
        if (f >= 39.0f * f21 && f <= f21 * 41.0f) {
            str = "8";
        }
        float f22 = FACTOR;
        if (f >= 41.0f * f22 && f <= f22 * 43.0f) {
            str = "23";
        }
        float f23 = FACTOR;
        if (f >= 43.0f * f23 && f <= f23 * 45.0f) {
            str = "10";
        }
        float f24 = FACTOR;
        if (f >= 45.0f * f24 && f <= f24 * 47.0f) {
            str = "5";
        }
        float f25 = FACTOR;
        if (f >= 47.0f * f25 && f <= f25 * 49.0f) {
            str = "24";
        }
        float f26 = FACTOR;
        if (f >= 49.0f * f26 && f <= f26 * 51.0f) {
            str = "16";
        }
        float f27 = FACTOR;
        if (f >= 51.0f * f27 && f <= f27 * 53.0f) {
            str = "33";
        }
        float f28 = FACTOR;
        if (f >= 53.0f * f28 && f <= f28 * 55.0f) {
            str = "1";
        }
        float f29 = FACTOR;
        if (f >= 55.0f * f29 && f <= f29 * 57.0f) {
            str = "20";
        }
        float f30 = FACTOR;
        if (f >= 57.0f * f30 && f <= f30 * 59.0f) {
            str = "14";
        }
        float f31 = FACTOR;
        if (f >= 59.0f * f31 && f <= f31 * 61.0f) {
            str = "31";
        }
        float f32 = FACTOR;
        if (f >= 61.0f * f32 && f <= f32 * 63.0f) {
            str = "9";
        }
        float f33 = FACTOR;
        if (f >= 63.0f * f33 && f <= f33 * 65.0f) {
            str = "22";
        }
        float f34 = FACTOR;
        if (f >= 65.0f * f34 && f <= f34 * 67.0f) {
            str = "18";
        }
        float f35 = FACTOR;
        if (f >= 67.0f * f35 && f <= f35 * 69.0f) {
            str = "29";
        }
        float f36 = FACTOR;
        if (f >= 69.0f * f36 && f <= f36 * 71.0f) {
            str = "7";
        }
        float f37 = FACTOR;
        if (f >= 71.0f * f37 && f <= f37 * 73.0f) {
            str = "28";
        }
        return ((f < FACTOR * 75.0f || i >= 360) && (i < 0 || f > FACTOR * 1.0f)) ? str : "12";
    }

    private String currentNumber6(int i) {
        float f = (float) i;
        float f2 = FACTOR;
        String str = (f < f2 * 1.0f || f > f2 * 3.0f) ? "" : "White";
        float f3 = FACTOR;
        if (f >= 3.0f * f3 && f <= f3 * 5.0f) {
            str = "Yellow";
        }
        float f4 = FACTOR;
        if (f >= 5.0f * f4 && f <= f4 * 7.0f) {
            str = "Orange";
        }
        float f5 = FACTOR;
        if (f >= 7.0f * f5 && f <= f5 * 9.0f) {
            str = "Red";
        }
        float f6 = FACTOR;
        if (f >= 9.0f * f6 && f <= f6 * 11.0f) {
            str = "Pink";
        }
        float f7 = FACTOR;
        if (f >= 11.0f * f7 && f <= f7 * 13.0f) {
            str = "Purple";
        }
        float f8 = FACTOR;
        if (f >= 13.0f * f8 && f <= f8 * 15.0f) {
            str = "Cyan";
        }
        float f9 = FACTOR;
        if (f >= 15.0f * f9 && f <= f9 * 17.0f) {
            str = "Blue";
        }
        float f10 = FACTOR;
        if (f >= 17.0f * f10 && f <= f10 * 19.0f) {
            str = "Green";
        }
        float f11 = FACTOR;
        if (f >= 19.0f * f11 && f <= f11 * 21.0f) {
            str = "Brown";
        }
        float f12 = FACTOR;
        if (f >= 21.0f * f12 && f <= f12 * 23.0f) {
            str = "Grey";
        }
        return ((f < FACTOR * 23.0f || i >= 360) && (i < 0 || f > FACTOR * 1.0f)) ? str : "Black";
    }

    private String currentNumber7(int i) {
        float f = (float) i;
        float f2 = FACTOR;
        String str = (f < f2 * 1.0f || f > f2 * 3.0f) ? "" : "Pizza";
        float f3 = FACTOR;
        if (f >= 3.0f * f3 && f <= f3 * 5.0f) {
            str = "Burger";
        }
        float f4 = FACTOR;
        if (f >= 5.0f * f4 && f <= f4 * 7.0f) {
            str = "Hot dog";
        }
        float f5 = FACTOR;
        if (f >= 7.0f * f5 && f <= f5 * 9.0f) {
            str = "Taco";
        }
        float f6 = FACTOR;
        if (f >= 9.0f * f6 && f <= f6 * 11.0f) {
            str = "Noodles";
        }
        float f7 = FACTOR;
        if (f >= 11.0f * f7 && f <= f7 * 13.0f) {
            str = "Muffins";
        }
        float f8 = FACTOR;
        if (f >= 13.0f * f8 && f <= f8 * 15.0f) {
            str = "Salad";
        }
        float f9 = FACTOR;
        if (f >= 15.0f * f9 && f <= f9 * 17.0f) {
            str = "Sandwich";
        }
        float f10 = FACTOR;
        if (f >= 17.0f * f10 && f <= f10 * 19.0f) {
            str = "Chicken Biryani";
        }
        float f11 = FACTOR;
        if (f >= 19.0f * f11 && f <= f11 * 21.0f) {
            str = "Sushi";
        }
        float f12 = FACTOR;
        if (f >= 21.0f * f12 && f <= f12 * 23.0f) {
            str = "French fries";
        }
        return ((f < FACTOR * 23.0f || i >= 360) && (i < 0 || f > FACTOR * 1.0f)) ? str : "Pasta";
    }

    private String currentNumber8(int i) {
        float f = (float) i;
        float f2 = FACTOR;
        String str = "Truth";
        String str2 = (f < f2 * 1.0f || f > f2 * 3.0f) ? "" : str;
        float f3 = FACTOR;
        String str3 = "Maybe";
        if (f >= 3.0f * f3 && f <= f3 * 5.0f) {
            str2 = str3;
        }
        float f4 = FACTOR;
        String str4 = "Always";
        if (f >= 5.0f * f4 && f <= f4 * 7.0f) {
            str2 = str4;
        }
        float f5 = FACTOR;
        String str5 = "Never";
        if (f >= 7.0f * f5 && f <= f5 * 9.0f) {
            str2 = str5;
        }
        float f6 = FACTOR;
        String str6 = "Lie";
        if (f >= 9.0f * f6 && f <= f6 * 11.0f) {
            str2 = str6;
        }
        float f7 = FACTOR;
        if (f >= 11.0f * f7 && f <= f7 * 13.0f) {
            str2 = str;
        }
        float f8 = FACTOR;
        if (f >= 13.0f * f8 && f <= f8 * 15.0f) {
            str2 = str3;
        }
        float f9 = FACTOR;
        if (f >= 15.0f * f9 && f <= f9 * 17.0f) {
            str2 = str4;
        }
        float f10 = FACTOR;
        if (f >= 17.0f * f10 && f <= f10 * 19.0f) {
            str2 = str5;
        }
        return ((f < FACTOR * 19.0f || i >= 360) && (i < 0 || f > FACTOR * 1.0f)) ? str2 : str6;
    }

    private String currentNumber9(int i) {
        float f = (float) i;
        float f2 = FACTOR;
        String str = (f < f2 * 1.0f || f > f2 * 3.0f) ? "" : "Take Photos";
        float f3 = FACTOR;
        if (f >= 3.0f * f3 && f <= f3 * 5.0f) {
            str = "Chatting with friends";
        }
        float f4 = FACTOR;
        if (f >= 5.0f * f4 && f <= f4 * 7.0f) {
            str = "Do Cycling";
        }
        float f5 = FACTOR;
        if (f >= 7.0f * f5 && f <= f5 * 9.0f) {
            str = "Party";
        }
        float f6 = FACTOR;
        if (f >= 9.0f * f6 && f <= f6 * 11.0f) {
            str = "Do Jogging";
        }
        float f7 = FACTOR;
        if (f >= 11.0f * f7 && f <= f7 * 13.0f) {
            str = "Play Games";
        }
        float f8 = FACTOR;
        if (f >= 13.0f * f8 && f <= f8 * 15.0f) {
            str = "Do Homework";
        }
        float f9 = FACTOR;
        if (f >= 15.0f * f9 && f <= f9 * 17.0f) {
            str = "Reading / Read Something New";
        }
        float f10 = FACTOR;
        if (f >= 17.0f * f10 && f <= f10 * 19.0f) {
            str = "Go Outside";
        }
        float f11 = FACTOR;
        if (f >= 19.0f * f11 && f <= f11 * 21.0f) {
            str = "Make Food";
        }
        float f12 = FACTOR;
        if (f >= 21.0f * f12 && f <= f12 * 23.0f) {
            str = "Watch TV / Movies";
        }
        return ((f < FACTOR * 23.0f || i >= 360) && (i < 0 || f > FACTOR * 1.0f)) ? str : "Take a Sleep";
    }

    private String currentNumber10(int i) {
        float f = (float) i;
        float f2 = FACTOR;
        String str = (f < f2 * 1.0f || f > f2 * 3.0f) ? "" : "Monday";
        float f3 = FACTOR;
        if (f >= 3.0f * f3 && f <= f3 * 5.0f) {
            str = "Tuesday";
        }
        float f4 = FACTOR;
        if (f >= 5.0f * f4 && f <= f4 * 7.0f) {
            str = "Wednesday";
        }
        float f5 = FACTOR;
        if (f >= 7.0f * f5 && f <= f5 * 9.0f) {
            str = "Thursday";
        }
        float f6 = FACTOR;
        if (f >= 9.0f * f6 && f <= f6 * 11.0f) {
            str = "Friday";
        }
        float f7 = FACTOR;
        if (f >= 11.0f * f7 && f <= f7 * 13.0f) {
            str = "Saturday";
        }
        return ((f < FACTOR * 13.0f || i >= 360) && (i < 0 || f > FACTOR * 1.0f)) ? str : "Sunday";
    }

    private String currentNumber11(int i) {
        float f = (float) i;
        float f2 = FACTOR;
        String str = (f < f2 * 1.0f || f > f2 * 3.0f) ? "" : "Australia";
        float f3 = FACTOR;
        if (f >= 3.0f * f3 && f <= f3 * 5.0f) {
            str = "Greece";
        }
        float f4 = FACTOR;
        if (f >= 5.0f * f4 && f <= f4 * 7.0f) {
            str = "India";
        }
        float f5 = FACTOR;
        if (f >= 7.0f * f5 && f <= f5 * 9.0f) {
            str = "Singapore";
        }
        float f6 = FACTOR;
        if (f >= 9.0f * f6 && f <= f6 * 11.0f) {
            str = "Canada";
        }
        float f7 = FACTOR;
        if (f >= 11.0f * f7 && f <= f7 * 13.0f) {
            str = "Indonesia";
        }
        float f8 = FACTOR;
        if (f >= 13.0f * f8 && f <= f8 * 15.0f) {
            str = "Spain";
        }
        float f9 = FACTOR;
        if (f >= 15.0f * f9 && f <= f9 * 17.0f) {
            str = "Thailand";
        }
        float f10 = FACTOR;
        if (f >= 17.0f * f10 && f <= f10 * 19.0f) {
            str = "France";
        }
        float f11 = FACTOR;
        if (f >= 19.0f * f11 && f <= f11 * 21.0f) {
            str = "London";
        }
        float f12 = FACTOR;
        if (f >= 21.0f * f12 && f <= f12 * 23.0f) {
            str = "Italy";
        }
        return ((f < FACTOR * 23.0f || i >= 360) && (i < 0 || f > FACTOR * 1.0f)) ? str : "USA";
    }

    private String currentNumber12(int i) {
        float f = (float) i;
        float f2 = FACTOR;
        String str = (f < f2 * 1.0f || f > f2 * 3.0f) ? "" : "Capricorn";
        float f3 = FACTOR;
        if (f >= 3.0f * f3 && f <= f3 * 5.0f) {
            str = "Sagittarius";
        }
        float f4 = FACTOR;
        if (f >= 5.0f * f4 && f <= f4 * 7.0f) {
            str = "Scorpio";
        }
        float f5 = FACTOR;
        if (f >= 7.0f * f5 && f <= f5 * 9.0f) {
            str = "Libra";
        }
        float f6 = FACTOR;
        if (f >= 9.0f * f6 && f <= f6 * 11.0f) {
            str = "Virgo";
        }
        float f7 = FACTOR;
        if (f >= 11.0f * f7 && f <= f7 * 13.0f) {
            str = "Leo";
        }
        float f8 = FACTOR;
        if (f >= 13.0f * f8 && f <= f8 * 15.0f) {
            str = "Cancer";
        }
        float f9 = FACTOR;
        if (f >= 15.0f * f9 && f <= f9 * 17.0f) {
            str = "Gemini";
        }
        float f10 = FACTOR;
        if (f >= 17.0f * f10 && f <= f10 * 19.0f) {
            str = "Taurus";
        }
        float f11 = FACTOR;
        if (f >= 19.0f * f11 && f <= f11 * 21.0f) {
            str = "Aries";
        }
        float f12 = FACTOR;
        if (f >= 21.0f * f12 && f <= f12 * 23.0f) {
            str = "Pisces";
        }
        return ((f < FACTOR * 23.0f || i >= 360) && (i < 0 || f > FACTOR * 1.0f)) ? str : "Aquarius";
    }

    private String currentNumber13(int i) {
        float f = (float) i;
        float f2 = FACTOR;
        String str = "Lucky";
        String str2 = (f < f2 * 1.0f || f > f2 * 3.0f) ? "" : str;
        float f3 = FACTOR;
        String str3 = "Unlucky";
        if (f >= 3.0f * f3 && f <= f3 * 5.0f) {
            str2 = str3;
        }
        float f4 = FACTOR;
        if (f >= 5.0f * f4 && f <= f4 * 7.0f) {
            str2 = str;
        }
        float f5 = FACTOR;
        if (f >= 7.0f * f5 && f <= f5 * 9.0f) {
            str2 = str3;
        }
        float f6 = FACTOR;
        if (f >= 9.0f * f6 && f <= f6 * 11.0f) {
            str2 = str;
        }
        return ((f < FACTOR * 11.0f || i >= 360) && (i < 0 || f > FACTOR * 1.0f)) ? str2 : str3;
    }
}
