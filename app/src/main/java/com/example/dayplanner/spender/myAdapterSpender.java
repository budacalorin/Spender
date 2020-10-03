package com.example.dayplanner.spender;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.anychart.enums.BackgroundCornersType;
import com.example.dayplanner.R;
import com.google.android.material.shape.CornerTreatment;

import java.util.ArrayList;

public class myAdapterSpender extends BaseAdapter {

    ArrayList<itemInList> list;
    LayoutInflater mInflater;
    Context context;
    ArrayList<Long> selected;
    double maximIncome=1;
    double maximExpence=1;

    public myAdapterSpender(Context c, ArrayList<itemInList> lista){
        selected = new ArrayList<>();
        list=lista;
        context = c;
        mInflater= (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        updateMaxim();
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
        if (position<0||position>=list.size())
            return -1;
        return list.get(position).id;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        RelativeLayout rel;

        if (!selected.contains(new Long(position))) {
            v = mInflater.inflate(R.layout.my_layout_adapter, null);

            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.list_adapter_background);
            drawable.setTint(Color.RED);

            rel = v.findViewById(R.id.spendingRelativeLayout);
            rel.setBackground(ContextCompat.getDrawable(context, R.drawable.list_adapter_background));
        } else {
            v = mInflater.inflate(R.layout.my_layout_adapter_details, null);

            rel = v.findViewById(R.id.spendingRelativeLayoutDetailed);
            rel.setBackground(ContextCompat.getDrawable(context, R.drawable.selected_view));

            TextView detailsTextView = (TextView) v.findViewById(R.id.detailTextView);
            detailsTextView.setText("Details: " + list.get(position).details);
        }

        TextView textView = (TextView) v.findViewById(R.id.textView);
        TextView valuesTextView = (TextView) v.findViewById(R.id.valueTextView);
        TextView dateTextView = (TextView) v.findViewById(R.id.dateTextView);

        textView.setText(list.get(position).description.toString());
        valuesTextView.setText(list.get(position).value.toString());
        dateTextView.setText(list.get(position).date);

        double val = list.get(position).value;
        boolean income = val > 0;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Color color = Color.valueOf(context.getColor(R.color.colorListEntry));

            if (income) {
                float green = (val == 0) ? 0 : (float) (color.green() + (color.green() * (val / maximIncome)));
                rel.getBackground().setTint(Color.argb(color.alpha(), color.red(), green, color.blue()));
            }
            else {
                float red = (val==0) ? 0 : (float) (color.red() + (color.red() * (val / maximExpence)));
                rel.getBackground().setTint(Color.argb(color.alpha(), red, color.green(), color.blue()));
            }
        }
        return v;
    }

    public ArrayList<Long> getSelected(){ return selected;}

    public void updateMaxim(){
        maximIncome=maximExpence=0;
        for (int i=0; i<list.size(); i++){
            if (list.get(i).value<0)
                if (maximExpence>list.get(i).value)
                    maximExpence=list.get(i).value;
            if (list.get(i).value>0)
                if (maximIncome<list.get(i).value)
                    maximIncome=list.get(i).value;
        }
    }

//    private Drawable getBackgroundForPosition(int position, int size){
//        switch (position){
//
//        }
//    }
}
