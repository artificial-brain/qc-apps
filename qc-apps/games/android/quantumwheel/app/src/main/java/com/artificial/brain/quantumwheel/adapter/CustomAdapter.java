package com.artificial.brain.quantumwheel.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.artificial.brain.quantumwheel.R;

public class CustomAdapter extends BaseAdapter {
    Context context;
    int[] rouletteGridImg = {R.drawable.heads_tails_small, R.drawable.yes_no_small, R.drawable.rock_paper_small, R.drawable.die_small, R.drawable.loves_me_small, R.drawable.casino_small, R.drawable.color_small, R.drawable.fast_food_small, R.drawable.lie_detector_small, R.drawable.freetime_activity_small, R.drawable.days_small, R.drawable.countries_small, R.drawable.zodiac_small, R.drawable.luck_small};
    String[] rouletteGridTitle = {"Heads/Tails", "Yes/No", "Rock-Paper-Scissors", "Roll the die", "Loves me?", "Casino", "Lucky Color", "What to eat", "Lie Detector", "Freetime Activity", "Pick a day", "Holidays Place", "Zodiac Sign", "Lucky/Unlucky"};

    private class ViewHolder {
        ImageView img;
        TextView txt;

        private ViewHolder() {
        }
    }

    public Object getItem(int i) {
        return null;
    }

    public long getItemId(int i) {
        return 0;
    }

    public CustomAdapter(Context context2) {
        this.context = context2;
    }

    public int getCount() {
        return this.rouletteGridImg.length;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null) {
            view = layoutInflater.inflate(R.layout.griddata, null);
            viewHolder = new ViewHolder();
            viewHolder.txt = view.findViewById(R.id.gridtitle);
            viewHolder.img = view.findViewById(R.id.gridimg);
            Typeface face = Typeface.createFromAsset(context.getAssets(), "champagne.ttf");
            viewHolder.txt.setTypeface(face);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.img.setImageResource(this.rouletteGridImg[i]);
        viewHolder.txt.setText(this.rouletteGridTitle[i]);
        return view;
    }
}
