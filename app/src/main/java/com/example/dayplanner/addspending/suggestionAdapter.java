package com.example.dayplanner.addspending;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.dayplanner.R;

import java.util.ArrayList;

public class suggestionAdapter extends BaseAdapter {
    ArrayList<suggestionItem> list;
    LayoutInflater mInflater;
    int selected;
    Context context;

    public suggestionAdapter(Context c, ArrayList<suggestionItem> lista) {
        list = lista;
        selected=-1;
        context = c;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        v = mInflater.inflate(R.layout.suggestion_adapter, null);
        TextView textView = (TextView) v.findViewById(R.id.suggestionTextView);
        textView.setText(list.get(position).name);
        TextView usageTextView = (TextView) v.findViewById(R.id.usageTextView);
        usageTextView.setText(list.get(position).usage.toString());

        ConstraintLayout rel = (ConstraintLayout) v.findViewById(R.id.sugggestionRelativeLayout);


        if (selected==position){
            rel.setBackground(ContextCompat.getDrawable(context,R.drawable.selected_view));

        }
        else{
            rel.setBackground(ContextCompat.getDrawable(context,R.drawable.gradient_background_buttons));

        }
        return v;
    }
}
