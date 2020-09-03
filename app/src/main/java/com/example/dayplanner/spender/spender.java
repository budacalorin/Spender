package com.example.dayplanner.spender;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dayplanner.db.budgets.BudgetNamesContract;
import com.example.dayplanner.db.budgets.BudgetNamesDbHelper;
import com.example.dayplanner.DatePickerFragment;
import com.example.dayplanner.DbUpdater;
import com.example.dayplanner.DynamicListView;
import com.example.dayplanner.R;
import com.example.dayplanner.db.spender.SpenderItemsContract;
import com.example.dayplanner.db.spender.SpenderItemsDbHelper;
import com.example.dayplanner.addspending.addSpendingActivity;
import com.example.dayplanner.chart.spendingsChartActivity;

import java.util.ArrayList;
import java.util.Comparator;


public class spender extends AppCompatActivity {

    DynamicListView<itemInList> list;
    ArrayList<itemInList> items;
    EditText costText;
    EditText nameText;
    TextView totalTextView;
    myAdapterSpender itemAdapter;
    SpenderItemsDbHelper spenderItemsDbHelper;
    SpenderItemsContract spenderItemsContract;
    spender THIS;
    String dbName;
    CheckBox sortDate;
    Context CTHIS;
    ImageView sideBars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        THIS = this;
        CTHIS = this;
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_spender);

        sideBars = (ImageView) findViewById(R.id.side_bars_spender);
        sideBars.setAlpha(0f);

        Intent data = getIntent();
        dbName = data.getStringExtra("dbName");
        sortDate = (CheckBox) findViewById(R.id.sortDateCheckBox);
        sortDate.setChecked(data.getBooleanExtra("sortDate",false));

        spenderItemsContract = new SpenderItemsContract(dbName);

        TextView titleTextView = (TextView) findViewById(R.id.budgetNameTextView);
        titleTextView.setText(dbName);

        Resources res=getResources();

        list = (DynamicListView<itemInList>) findViewById(R.id.listView);

        items = new ArrayList<itemInList>();
        list.setDbUpdater(new DbUpdater<itemInList>(){
            @Override
            public void update(ArrayList<itemInList> list){
                SpenderItemsDbHelper helper = new SpenderItemsDbHelper(CTHIS,spenderItemsContract);
                SQLiteDatabase db = helper.getWritableDatabase();
                db.execSQL("DELETE FROM "+ spenderItemsContract.TABLE_NAME);
                for(int i=0;i<list.size();i++){
                    helper.insertData(list.get(i).description,(((double)((int)(list.get(i).value*100))/100))+"",list.get(i).details,list.get(i).date);
                }
                itemAdapter.notifyDataSetChanged();
            }
            @Override
            public void delete(ArrayList<itemInList> list, long id){
                SpenderItemsDbHelper helper = new SpenderItemsDbHelper(CTHIS,spenderItemsContract);
                SQLiteDatabase db = helper.getWritableDatabase();
                db.execSQL("delete from " +  spenderItemsContract.TABLE_NAME + " WHERE " + SpenderItemsContract.SpenderItem._ID + " LIKE " + id);

                for(int i=0;i<list.size();i++){
                    if (list.get(i).id==id){
                        list.remove(i);
                        break;
                    }
                }
                itemAdapter.notifyDataSetChanged();
            }

            @Override
            public void edit(long index){
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final int idx = (int)index;
                final View v;
                v=inflater.inflate(R.layout.edit_dialog,null);
                ((EditText)v.findViewById(R.id.nameEditTextEditDialog)).setText(items.get((int)index).description);
                ((TextView) v.findViewById(R.id.dateTextViewEditDialog)).setText(items.get((int)index).date);
                ((EditText)v.findViewById(R.id.valueEditTextEditDialog)).setText(items.get((int)index).value+"");
                ((EditText)v.findViewById(R.id.detailsEditTextEditDialog)).setText(items.get((int)index).details);
                date=items.get((int)index).date;

                ((TextView)v.findViewById(R.id.dateTextViewEditDialog)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerFragment datePickerFragment = new DatePickerFragment(new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String date=dayOfMonth+"/";
                                month++;
                                if (month<=9)
                                    date+='0';
                                date=date+month+'/'+(year/10%10)+(year%10);
                                list.getDbUpdater().date=date;
                            }
                        });
                        datePickerFragment.show(getSupportFragmentManager(),"datePicker");
                    }
                });

                AlertDialog dialog = new AlertDialog.Builder(CTHIS).setTitle("Edit").setView(v).setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SpenderItemsDbHelper helper = new SpenderItemsDbHelper(CTHIS,spenderItemsContract);
                        SQLiteDatabase db = helper.getWritableDatabase();

                        String name = ((EditText) v.findViewById(R.id.nameEditTextEditDialog)).getText().toString();
                        String details = ((EditText) v.findViewById(R.id.detailsEditTextEditDialog)).getText().toString();
//                        String date = ((TextView) v.findViewById(R.id.dateTextViewEditDialog)).apply
                        date = list.getDbUpdater().date;
                        double value = (double)((int)(Double.parseDouble(((EditText) v.findViewById(R.id.valueEditTextEditDialog)).getText().toString())*100))/100;

                        ContentValues replace = new ContentValues();
                        replace.put(SpenderItemsContract.SpenderItem.COLUMN_NAME_TITLE,name);
                        replace.put(SpenderItemsContract.SpenderItem.COLUMN_NAME_DETAILS,details);
                        replace.put(SpenderItemsContract.SpenderItem.COLUMN_NAME_VALUE,value);
                        replace.put(SpenderItemsContract.SpenderItem.COLUMN_NAME_DATE,date);

                        String[] args={items.get(idx).id+""};
                        db.update(spenderItemsContract.TABLE_NAME,replace,SpenderItemsContract.SpenderItem._ID+" LIKE ?",args);

                        items.get(idx).description=name;
                        items.get(idx).value=value;
                        items.get(idx).details=details;
                        items.get(idx).date=date;

                        itemAdapter.notifyDataSetChanged();



                        dialog.dismiss();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();

                dialog.show();
            }
        },items,sideBars);

        totalTextView = (TextView) findViewById(R.id.totalTextView);
        Button addBtn = (Button) findViewById(R.id.addBtn);

        Button chartButton = (Button) findViewById(R.id.chartButton);
        chartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_chart();
            }
        });

        spenderItemsDbHelper = new SpenderItemsDbHelper(this,spenderItemsContract);

        sortDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_sort_date();
            }
        });

        itemAdapter = new myAdapterSpender(this,items);

        list.setAdapter(itemAdapter);

        /*list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                SQLiteDatabase db = spenderItemsDbHelper.getWritableDatabase();

                Integer pos = (int)items.get(position).id;
                String w = pos.toString();
                String[] argv = { w };
                String where = "_ID LIKE ?";
                Integer nr = db.delete(spenderItemsContract.TABLE_NAME,where, argv);
                Toast.makeText(THIS,nr+"",Toast.LENGTH_LONG).show();
                //db.rawQuery("DELETE FROM " + SpenderItemsContract.SpenderItem.TABLE_NAME + " WHERE ID = " + w,null);


                items.remove(position);
                itemAdapter.notifyDataSetChanged();

                updateTotal();

                itemAdapter.updateMaxim();
                return true;
            }
        });*/

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (itemAdapter.selected.contains(new Long(position))) {
                    itemAdapter.selected.remove(new Long(position));
                } else {
                    itemAdapter.selected.add(new Long(position));
                }
                itemAdapter.notifyDataSetChanged();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddActivity();
            }
        });

        initialiseDb();

        updateTotal();

        update_sort_date();

        Button generateButton = (Button) findViewById(R.id.generateButton);
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=1;i<=10;i++){
                    itemInList item = new itemInList();
                    item.description = "Pentru Teste";
                    if(i!=10)
                        item.value = i+0.0;
                    else
                        item.value = -45.0;
                    item.details = "detail";
                    item.date = "00/00/00";

                    item.id = insertItem("Pentru Teste",item.value.toString(),"detail","00/00/00");
                    if (item.id!=-1){
                        items.add(item);
                    } else {
                        Toast.makeText(CTHIS,"Error on generate",Toast.LENGTH_LONG).show();
                    }

                    itemAdapter.notifyDataSetChanged();
                    updateTotal();
                }
            }
        });

    }

    private void openAddActivity(){
        Intent intent = new Intent(this, addSpendingActivity.class);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK){
            Double value;
            value = data.getDoubleExtra("value",0);
            String source = new String();
            source = data.getStringExtra("source");

            String details = new String();
            details = data.getStringExtra("details");

            String date;
            date = data.getStringExtra("date");

            itemInList item = new itemInList();
            item.description = source;
            item.value = value;
            item.details = details;
            item.date = date;

            item.id = insertItem(source,value.toString(),details,date);
            if (item.id!=-1){
                items.add(item);
            }

            itemAdapter.notifyDataSetChanged();
            updateTotal();


        }
        itemAdapter.updateMaxim();

        update_sort_date();
    }

    private long insertItem(String name, String value, String details, String date){

        long b =spenderItemsDbHelper.insertData(name,value,details, date);
        if (b==-1){
            Toast.makeText(this,"error",Toast.LENGTH_LONG).show();
        }
        else{
            //Toast.makeText(this,"ok",Toast.LENGTH_LONG).show();
        }
        updateTotal();
        return b;
    }

    private void initialiseDb(){
        items.clear();
        SQLiteDatabase db = spenderItemsDbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + spenderItemsContract.TABLE_NAME,null);
        while(cursor.moveToNext()) {
            String name = cursor.getString(1);
            String value = cursor.getString(2);
            String details = cursor.getString(3);
            long id = cursor.getLong(0);
            Double valueD = Double.parseDouble(value);
            String date = cursor.getString(4);

            itemInList item = new itemInList(name,valueD,id,details,date);
            items.add(item);
            //System.out.println(id);
        }
        cursor.close();

        itemAdapter.notifyDataSetChanged();

        itemAdapter.updateMaxim();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent();
        setResult(RESULT_OK,intent);

        spenderItemsDbHelper.close();

        finish();
    }

    @Override
    protected void onPause() {
        String name = dbName;
        int bool = (sortDate.isChecked()) ? 1:0;

        BudgetNamesDbHelper helper = new BudgetNamesDbHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        String[] caut={name};
        String testul = "name LIKE ?";

        ContentValues cv = new ContentValues();
        cv.put(BudgetNamesContract.Budget.COLUMN_NAME_SORT_DATE,bool);

        db.update(BudgetNamesContract.Budget.TABLE_NAME, cv, testul, caut);

        db.close();
        super.onPause();
    }

    private void updateTotal(){
        double sum = 0;
        for (int i=0;i<items.size();i++){
            sum += items.get(i).value;
        }
        int s= (int) (sum*100);
        sum=(double)s/100;
        totalTextView.setText("Total: "+sum+"");
    }

    private void open_chart(){
        Intent intent = new Intent(this, spendingsChartActivity.class);
        intent.putExtra("dbName",dbName);
        startActivity(intent);
    }

    private void update_sort_date(){
        if (sortDate.isChecked()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                items.sort(new Comparator<itemInList>() {
                    @Override
                    public int compare(itemInList o1, itemInList o2) {
                        String[] d1 = o1.date.split("/");
                        String[] d2 = o2.date.split("/");
                        if (Integer.parseInt(d1[2])!=Integer.parseInt(d2[2]))
                            if (Integer.parseInt(d1[2])<Integer.parseInt(d2[2]))
                                return -1;
                            else
                                return 1;
                        if (Integer.parseInt(d1[1])!=Integer.parseInt(d2[1]))
                            if (Integer.parseInt(d1[1])<Integer.parseInt(d2[1]))
                                return -1;
                            else
                                return 1;
                        if (Integer.parseInt(d1[0])!=Integer.parseInt(d2[0]))
                            if (Integer.parseInt(d1[0])<Integer.parseInt(d2[0]))
                                return -1;
                            else
                                return 1;
                        return 0;
                    }
                });
            }
            itemAdapter.notifyDataSetChanged();
        } else {
            initialiseDb();
            itemAdapter.notifyDataSetChanged();
        }
    }
/*
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date=dayOfMonth+"/";
        month++;
        if (month<=9)
            date+='0';
        date=date+month+'/'+(year/10%10)+(year%10);
        list.getDbUpdater().date=date;
    }*/
}
