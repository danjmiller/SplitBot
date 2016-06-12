package com.noxemall.splitbot;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.EditText;


public class BillEntryActivity extends AppCompatActivity {

    public final static String EXTRA_TOTAL = "com.noxemall.splitbot.TOTAL";
    public final static String EXTRA_NUM_PEOPLE = "com.noxemall.splitbot.NUM_PEOPLE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_entry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /** Called when the user clicks the Send button */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, SplitBillActivity.class);
        EditText editTotal = (EditText) findViewById(R.id.edit_total);
        String billTotal = editTotal.getText().toString();
        intent.putExtra(EXTRA_TOTAL, billTotal);

        EditText editNumPeople = (EditText) findViewById(R.id.edit_number_people);
        String numPeople = editNumPeople.getText().toString();
        intent.putExtra(EXTRA_NUM_PEOPLE,numPeople);

        startActivity(intent);
    }
}
