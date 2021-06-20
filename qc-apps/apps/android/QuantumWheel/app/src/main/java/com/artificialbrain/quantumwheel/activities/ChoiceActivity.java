package com.artificialbrain.quantumwheel.activities;

import android.app.Activity;

import androidx.appcompat.widget.AppCompatSpinner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.content.Context;
import com.artificialbrain.quantumwheel.R;
import com.artificialbrain.quantumwheel.models.Choice;
import java.util.ArrayList;

public class ChoiceActivity extends Activity implements View.OnClickListener {
    LinearLayout layoutList;
    Button buttonAdd;
    Button buttonSubmitList;


    ArrayList<Choice> choiceList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choices_main);

        layoutList = findViewById(R.id.layout_list);
        buttonAdd = findViewById(R.id.button_add);
        buttonSubmitList = findViewById(R.id.button_submit_list);

        buttonAdd.setOnClickListener(this);
        buttonSubmitList.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final Context context = this;

        switch (v.getId()) {

            case R.id.button_add:

                addView();

                break;

            case R.id.button_submit_list:

                if (checkIfValidAndRead()) {
                    Intent intent;
                    intent = new Intent(context, SpinningWheelActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("choiceList", choiceList);
                    intent.putExtras(bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                }
                break;
        }


    }

    private boolean checkIfValidAndRead() {
        choiceList.clear();
        boolean result = true;

        for (int i = 0; i < layoutList.getChildCount(); i++) {

            View choiceView = layoutList.getChildAt(i);

            EditText editTextName = (EditText) choiceView.findViewById(R.id.edit_choice_name);

            Choice ch = new Choice();

            if (!editTextName.getText().toString().equals("")) {
                ch.setChoiceName(editTextName.getText().toString());
            } else {
                result = false;
                break;
            }
            choiceList.add(ch);
        }

        if (choiceList.size() == 0) {
            result = false;
            Toast.makeText(this, "Add Choice First!", Toast.LENGTH_SHORT).show();
        } else if (choiceList.size() != 2 & choiceList.size() != 4
                & choiceList.size() != 8 & choiceList.size() != 16){
            result = false;
            Toast.makeText(this, R.string.choices_in_range, Toast.LENGTH_SHORT).show();
        }
        else if (!result) {
            Toast.makeText(this, "Enter All Details Correctly!", Toast.LENGTH_SHORT).show();
        }

        return result;
    }

    private void addView() {

        final View choiceView = getLayoutInflater().inflate(R.layout.row_add_choice, null, false);

        EditText editText = (EditText) choiceView.findViewById(R.id.edit_choice_name);
        ImageView imageClose = (ImageView) choiceView.findViewById(R.id.image_remove);

        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView(choiceView);
            }
        });

        layoutList.addView(choiceView);

    }

    private void removeView(View view) {
        layoutList.removeView(view);
    }
}