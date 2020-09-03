package com.example.dayplanner.addspending;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dayplanner.DatePickerFragment;
import com.example.dayplanner.R;
import com.example.dayplanner.db.addspending.SuggestionsContract;
import com.example.dayplanner.db.addspending.SuggestionsDbHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.example.dayplanner.utils.DateUtilsKt.stringToDate;

public class addSpendingActivity extends AppCompatActivity{
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Button button5;
    Button button6;
    Button button7;
    Button button8;
    Button button9;
    Button button0;
    Button buttonB;
    ArrayList<suggestionItem> suggestionArray;
    ListView suggestionList;
    Button newSourceBtn;
    Button expenceBtn;
    Button incomeBtn;
    EditText newSourceEditText;
    Integer total;
    TextView totalTextView;
    SuggestionsDbHelper suggestionsDbHelper;
    suggestionAdapter itemAdapter;
    Context THIS;
    EditText detailsEditText;
    TextView selectedDateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_spending);

        setupButtons();

        total=new Integer(0);
        totalTextView=(TextView) findViewById(R.id.totalTextView);

        THIS = this;

        suggestionList=(ListView) findViewById(R.id.listSuggestions);
        suggestionArray=new ArrayList<suggestionItem>();
        newSourceEditText = (EditText) findViewById(R.id.newSourceEditText);

        suggestionsDbHelper = new SuggestionsDbHelper(this);

        detailsEditText = (EditText) findViewById(R.id.detailsEditText);

        Resources res = getResources();


        itemAdapter = new suggestionAdapter(this,suggestionArray);
        suggestionList.setAdapter(itemAdapter);

        suggestionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemAdapter.selected=position;
                itemAdapter.notifyDataSetChanged();
            }
        });

        updateButton(itemAdapter);

        initialiseDb();

        selectedDateTextView=(TextView) findViewById(R.id.selectedDateTextView);
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        currentDate=currentDate.replace('-','/').substring(0,6)+currentDate.charAt(8)+currentDate.charAt(9);
        selectedDateTextView.setText(currentDate);

        selectedDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDate();
            }
        });

    }

    private void setupButtons(){
        button0=(Button) findViewById(R.id.button0);
        button1=(Button) findViewById(R.id.button1);
        button2=(Button) findViewById(R.id.button2);
        button3=(Button) findViewById(R.id.button3);
        button4=(Button) findViewById(R.id.button4);
        button5=(Button) findViewById(R.id.button5);
        button6=(Button) findViewById(R.id.button6);
        button7=(Button) findViewById(R.id.button7);
        button8=(Button) findViewById(R.id.button8);
        button9=(Button) findViewById(R.id.button9);
        buttonB=(Button) findViewById(R.id.buttonB);
        newSourceBtn=(Button) findViewById(R.id.newSourceBtn);
        expenceBtn=(Button) findViewById(R.id.expenceBtn);
        incomeBtn=(Button) findViewById(R.id.incomeBtn);
        buttonB.setText("<");
    }

    private void updateButton(final suggestionAdapter itemAdapter){

        buttonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                total/=10;
                updateText();
            }
        });
        newSourceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newSourceEditText.getText().length()==0)
                    return;
                String s  = new String();
                s = newSourceEditText.getText().toString();
                int usage = 0;
                long index = insertItem(s,0);

                if (index!=-1){
                    suggestionItem item = new suggestionItem(s,usage,index);
                    suggestionArray.add(item);
                    itemAdapter.selected=suggestionArray.size()-1;
                }
                itemAdapter.notifyDataSetChanged();
            }
        });
        expenceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemAdapter.selected<0) return;
                total=-total;
                endSesion();
            }
        });
        incomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemAdapter.selected<0) return;

                endSesion();
            }
        });

        suggestionList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                SQLiteDatabase db = suggestionsDbHelper.getWritableDatabase();

                Integer pos = (int)suggestionArray.get(position).id;
                String w = pos.toString();
                String[] argv = { w };
                String where = "_ID LIKE ?";
                Integer nr = db.delete(SuggestionsContract.Suggestion.TABLE_NAME,where, argv);
                Toast.makeText(THIS,nr+"",Toast.LENGTH_LONG).show();
                //db.rawQuery("DELETE FROM " + SpenderItemsContract.SpenderItem.TABLE_NAME + " WHERE ID = " + w,null);


                suggestionArray.remove(position);
                itemAdapter.notifyDataSetChanged();
                return true;
            }
        });

    }

    private void closeActivity(suggestionAdapter itemAdapter){
        Intent intent = new Intent();
        intent.putExtra("value",((double)total)/100);
        intent.putExtra("source",suggestionArray.get(itemAdapter.selected).name);
        intent.putExtra("details",detailsEditText.getText().toString());


        intent.putExtra("date",selectedDateTextView.getText().toString());
        setResult(RESULT_OK,intent);
        finish();
    }

    private void updateText(){
        String s = new String();
        Double d = ((double)total)/100;
        s=d.toString();
        totalTextView.setText(s);
    }

    public void buttonPressed(View view){
        Button btn = (Button) view;
        total*=10;
        total+=Integer.parseInt(btn.getText().toString());
        updateText();
    }

    private long insertItem(String name, int value){

        long b =suggestionsDbHelper.insertData(name,value);
        if (b==-1){
            Toast.makeText(this,"error",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this,"ok",Toast.LENGTH_LONG).show();
        }
        return b;
    }

    private void initialiseDb(){
        SQLiteDatabase db = suggestionsDbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + SuggestionsContract.Suggestion.TABLE_NAME + " ORDER BY " + SuggestionsContract.Suggestion.COLUMN_NAME_USAGE + " DESC",null);
        while(cursor.moveToNext()) {
            String name = cursor.getString(1);
            int value = cursor.getInt(2);
            long id = cursor.getLong(0);
            suggestionItem item = new suggestionItem(name,value,id);
            suggestionArray.add(item);
            System.out.println(name+value);
        }
        cursor.close();

        itemAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        suggestionsDbHelper.close();
        super.onDestroy();
    }

    private void endSesion() {
        Integer usage=suggestionArray.get(itemAdapter.selected).usage;
        usage++;
        suggestionArray.get(itemAdapter.selected).usage=usage;

        SQLiteDatabase db = suggestionsDbHelper.getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put(SuggestionsContract.Suggestion.COLUMN_NAME_USAGE,usage);

        String id = suggestionArray.get(itemAdapter.selected).id + "";
        String where = "_ID LIKE ?";
        String[] argvs = {id};
        db.update("Suggestions",value,where,argvs);

        db.close();

        closeActivity(itemAdapter);
    }

    private void changeDate(){
        DatePickerFragment newFragment = new DatePickerFragment(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date="";
                if (dayOfMonth<=9)
                    date = "0";
                date+=dayOfMonth+"/";
                month++;
                if (month<=9)
                    date+='0';
                date=date+month+'/'+(year/10%10)+(year%10);
                selectedDateTextView.setText(date);
            }
        });
        newFragment.setDate(stringToDate(selectedDateTextView.getText().toString()));
        newFragment.show(getSupportFragmentManager(),"datePicker");
    }
}




















