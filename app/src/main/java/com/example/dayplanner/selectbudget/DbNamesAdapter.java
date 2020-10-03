package com.example.dayplanner.selectbudget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dayplanner.R;
import com.example.dayplanner.selectbudget.itemInListDbNames;

import java.util.ArrayList;

public class DbNamesAdapter extends BaseAdapter {
    ArrayList<itemInListDbNames> namesList;
    LayoutInflater mInflater;

    DbNamesAdapter (Context c,ArrayList<itemInListDbNames> namesList){
        this.namesList = namesList;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return namesList.size();
    }

    @Override
    public Object getItem(int position) {
        return namesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        if (position<0||position>=namesList.size())
            return -1;
        return namesList.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        v = mInflater.inflate(R.layout.db_names_adapter,null );
        TextView textView = (TextView) v.findViewById(R.id.nameTextViewDbNames);
        textView.setText(namesList.get(position).name);

        TextView valueTextView = (TextView) v.findViewById(R.id.valueTextViewDbNames);
        valueTextView.setText(namesList.get(position).value.toString());

        //valueTextView.setBackgroundResource(R.drawable.gradient_background_buttons);
        return v;
    }


}
