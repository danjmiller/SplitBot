package com.noxemall.splitbot;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class SplitBillActivity extends Activity {

    private static final String TAG = SplitBillActivity.class.getName();
    public static final String ITEM_ID = "com.noxemall.splitbot.ITEM_ID";

    private static int ROUNDING_MODE = BigDecimal.ROUND_HALF_EVEN;
    // TODO: 6/4/16 Guess what the local currency is? 
    private static int DECIMAL_PLACES = Currency.getInstance("USD").getDefaultFractionDigits();
    public final int PICK_CONTACT = 2015;

    private int currentPosition = -1;
    ListView listView;

    private ArrayList<Person> splitList = null;
    ArrayAdapter adapter = null;

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

        splitList = new ArrayList<Person>(people);
        for(int i = 0; i < people; i++)
        {
            Person p = new Person();
            p.billPortion = split;
            p.displayName = "Unassigned";
            splitList.add(p);
        }

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

     /*   ArrayAdapter<Person> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_2, android.R.id.text1, (List) splitList);
*/

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2, android.R.id.text1, (List) splitList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(splitList.get(position).billPortion);
                text2.setText(splitList.get(position).displayName);
                return view;
            }
        };

        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(i, PICK_CONTACT);
                currentPosition = position;

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

            int id_column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.CONTACT_ID);
            int phone_column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int name_column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Identity.DISPLAY_NAME);

            String id = cursor.getString(id_column);
            String name = cursor.getString(name_column);
            String phone = cursor.getString(phone_column);

            String email = getEmailAddress(id);
            splitList.get(currentPosition).email = email;
            splitList.get(currentPosition).displayName = name;
            splitList.get(currentPosition).phone = phone;

            adapter.notifyDataSetChanged();

            Log.d(TAG,"Name: " + name + " phone: " + phone + " email: " + email);
        }
    }

    private String getEmailAddress(String id) {

        ArrayList<String> emails = new ArrayList<String>();
        Cursor emailCur = getContentResolver().query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                new String[]{id}, null);

        while (emailCur.moveToNext()) {
            // This would allow you get several email addresses
            // if the email addresses were stored in an array
            String email = emailCur.getString(
                    emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
            String emailType = emailCur.getString(
                    emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));

            emails.add(email);
        }
        emailCur.close();

        if( emails != null) {
            return emails.get(0);
        } else {
            return "";
        }
    }

}
