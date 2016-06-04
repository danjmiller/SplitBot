package com.noxemall.splitbot;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SplitBillActivity extends Activity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split_bill);

        Intent intent = getIntent();
        String billTotal = intent.getStringExtra(MyActivity.EXTRA_TOTAL);
        String numPeople = intent.getStringExtra(MyActivity.EXTRA_NUM_PEOPLE);


        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.split_list);

        // TODO: 6/4/16 Get the values from the intent
        int total = Integer.parseInt(billTotal);
        int people = Integer.parseInt(numPeople);
        String split = String.valueOf(getSplit(total,people));

        String[] splitList = new String[people];
        for(int i = 0; i < people; i++)
        {
            splitList[i] = split;
        }

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, splitList);


        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                String  itemValue    = (String) listView.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
                        .show();

            }

        });
    }

    int getSplit(int total, int people)
    {
        if(total > 0 && people > 0) {
            return total / people;
        }

        else return total;
    }





}
