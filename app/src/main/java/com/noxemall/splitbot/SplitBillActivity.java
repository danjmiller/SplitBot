package com.noxemall.splitbot;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.math.BigDecimal;
import java.util.Currency;

public class SplitBillActivity extends Activity {

    private static final String TAG = SplitBillActivity.class.getName();

    private static int ROUNDING_MODE = BigDecimal.ROUND_HALF_EVEN;
    // TODO: 6/4/16 Guess what the local currency is? 
    private static int DECIMAL_PLACES = Currency.getInstance("USD").getDefaultFractionDigits();
    public final int PICK_CONTACT = 2015;


    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split_bill);

        Intent intent = getIntent();
        String billTotal = intent.getStringExtra(BillEntryActivity.EXTRA_TOTAL);
        String numPeople = intent.getStringExtra(BillEntryActivity.EXTRA_NUM_PEOPLE);


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

                Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(i, PICK_CONTACT);

            }

        });
    }

    BigDecimal getSplit(int total, int people)
    {
        BigDecimal t = new BigDecimal(total);
        BigDecimal p = new BigDecimal(people);
        if(total > 0 && people > 0) {
            return t.divide(p,DECIMAL_PLACES,ROUNDING_MODE);
        }
        else return new BigDecimal(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CONTACT && resultCode == RESULT_OK) {
            Uri contactUri = data.getData();
            Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);
            cursor.moveToFirst();
            int email_column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS);
            int phone_column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int name_column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Identity.DISPLAY_NAME);

            String email = cursor.getString(email_column);
            String name = cursor.getString(name_column);
            String phone = cursor.getString(phone_column);

        }
    }



}
