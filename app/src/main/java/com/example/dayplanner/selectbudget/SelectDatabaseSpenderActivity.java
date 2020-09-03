package com.example.dayplanner.selectbudget;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dayplanner.chart.AllChartActivity;
import com.example.dayplanner.db.budgets.BudgetNamesContract;
import com.example.dayplanner.db.budgets.BudgetNamesDbHelper;
import com.example.dayplanner.DbUpdater;
import com.example.dayplanner.DynamicListView;
import com.example.dayplanner.R;
import com.example.dayplanner.db.spender.SpenderItemsContract;
import com.example.dayplanner.db.spender.SpenderItemsDbHelper;
import com.example.dayplanner.spender.spender;

import java.util.ArrayList;
import java.util.Iterator;

public class SelectDatabaseSpenderActivity extends AppCompatActivity {

    DynamicListView<itemInListDbNames> dbNamesList;
    ArrayList<itemInListDbNames> namesList;
    Button addNewDbButton;
    Button chartButton;
    EditText addNewDbEditText;
    BudgetNamesDbHelper budgetsDb;
    BudgetNamesContract budgetsContract;
    DbNamesAdapter itemAdapter;
    TextView totalTextView;
    Context THIS;
    ImageView sideBars;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_database_spender);
        THIS = this;

        addNewDbButton = (Button) findViewById(R.id.addNewDbButton);
        addNewDbEditText = (EditText) findViewById(R.id.newDbNameeditText);
        dbNamesList = ((DynamicListView) findViewById(R.id.dbNamesList));
        totalTextView = (TextView) findViewById(R.id.totalSelectDbTextView);

        namesList = new ArrayList<itemInListDbNames>();


        sideBars = (ImageView) findViewById(R.id.side_bars_database_select);
        sideBars.setAlpha(0f);
        //sideBars.setVisibility(View.INVISIBLE);

        dbNamesList.setDbUpdater(new DbUpdater<itemInListDbNames>(){
            @Override
            public void update(ArrayList<itemInListDbNames> list){
                BudgetNamesDbHelper helper = new BudgetNamesDbHelper(THIS);
                SQLiteDatabase db = helper.getWritableDatabase();
                db.execSQL("delete from " + BudgetNamesContract.Budget.TABLE_NAME);
                for (int i=0;i<list.size();i++){
                    helper.insertData(list.get(i).name,list.get(i).sortDate);
                }
            }
            @Override
            public void delete(ArrayList<itemInListDbNames> list, long id){
                BudgetNamesDbHelper helper = new BudgetNamesDbHelper(THIS);
                SQLiteDatabase db = helper.getWritableDatabase();
                db.execSQL("delete from " + BudgetNamesContract.Budget.TABLE_NAME + " WHERE " + BudgetNamesContract.Budget._ID + " LIKE " + id);

                for(int i=0;i<list.size();i++){
                    if (list.get(i).id==id){
                        list.remove(i);
                        break;
                    }
                }
                itemAdapter.notifyDataSetChanged();
            }
        },namesList,sideBars);

        itemAdapter = new DbNamesAdapter(this, namesList);
        dbNamesList.setAdapter(itemAdapter);

        budgetsContract = new BudgetNamesContract();
        budgetsDb = new BudgetNamesDbHelper(this);

        initializeDb();

        final Context THIS = this;

        addNewDbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = addNewDbEditText.getText().toString();

                name=name.replaceAll(" ","");

                if (name.isEmpty())
                    return;

                itemInListDbNames item = new itemInListDbNames(name,(double)0,false);

                item.id = budgetsDb.insertData(name,false);

                namesList.add(item);
                itemAdapter.notifyDataSetChanged();
            }
        });

        dbNamesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(THIS, spender.class);

                String name = namesList.get(position).name;
                name  = name.replaceAll("\\s+","");
                intent.putExtra("dbName",name);
                intent.putExtra("sortDate",namesList.get(position).sortDate);
                startActivityForResult(intent,1);
            }
        });

        chartButton = (Button) findViewById(R.id.chartButtonSpender);
        chartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(THIS, AllChartActivity.class);
                ArrayList<String> extraList = new ArrayList<>();

                for (itemInListDbNames item: namesList){
                    extraList.add(item.name);
                }
                intent.putExtra("names", extraList);
                startActivity(intent);
            }
        });



        /*dbNamesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                SQLiteDatabase db = budgetsDb.getWritableDatabase();

                Integer pos = (int)namesList.get(position).id;
                String w = pos.toString();
                String[] argv = { w };
                String where = "_ID LIKE ?";
                Integer nr = db.delete(BudgetNamesContract.Budget.TABLE_NAME,where, argv);
                Toast.makeText(THIS,nr+"",Toast.LENGTH_LONG).show();

                namesList.remove(position);
                itemAdapter.notifyDataSetChanged();

                return true;
            }
        });*/


    }

    private void initializeDb(){
        namesList.clear();

        SQLiteDatabase db = budgetsDb.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + BudgetNamesContract.Budget.TABLE_NAME,null);
        while(cursor.moveToNext()) {
            String name = cursor.getString(1);
            long id = cursor.getLong(0);
            boolean sortDate = (cursor.getInt(2)==0) ? false : true;
            Double value;
            value = (double)0;


            SpenderItemsContract itemContract = new SpenderItemsContract(name);
            SpenderItemsDbHelper dbHelper = new SpenderItemsDbHelper(this,itemContract);
            SQLiteDatabase dbS = dbHelper.getWritableDatabase();

            Cursor cursor2 = dbS.rawQuery("SELECT * FROM " + name,null);
            while (cursor2.moveToNext()){
                String val = cursor2.getString(2);
                value += Double.parseDouble(val);
            }

            value = (double)((int)(value.doubleValue()*100))/100;
            itemInListDbNames item = new itemInListDbNames(name,value,sortDate);
            item.id = id;
            namesList.add(item);
        }
        cursor.close();

        itemAdapter.notifyDataSetChanged();
        update_total();
    }


    public void update_total(){
        Double sum = (double)0;
        for (int i=0;i<namesList.size();i++){
            sum+=namesList.get(i).value.doubleValue();
        }
        sum = (double)(int)(sum*100)/100;
        totalTextView.setText("Total: " + sum.toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        initializeDb();
    }
}
