package com.acpp.Instant_SMS;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by misko on 10.1.2015.
 */
public class NewMessage extends ActionBarActivity {

    protected EditText what;
    protected Button save;
    protected EditText saveAs;
    protected AutoCompleteTextView to;
    protected SimpleAdapter adapter;
    protected ArrayAdapter<HashMap<String, String>> hashAdapter;
    protected ArrayList<String> names = new ArrayList<String>();
    protected ArrayList<String> numbers = new ArrayList<String>();
    protected ArrayList<Map<String, String>> contacts = new ArrayList<Map<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_message);

        readContacts();

        adapter = new SimpleAdapter(this, contacts, R.layout.contact, new String[] {"Name", "Number"}, new int[] {R.id.name, R.id.number});


        to = (AutoCompleteTextView) findViewById(R.id.to);
        what = (EditText) findViewById(R.id.what);
        save = (Button) findViewById(R.id.b_save);
        saveAs = (EditText) findViewById(R.id.save_as);

        to.setAdapter(adapter);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getToMainActivity();
            }
        });

    }

    public void onBackPressed() {
        Intent intent = new Intent(NewMessage.this, MainActivity.class);
        setResult(NewMessage.RESULT_OK, intent);
        intent.putExtra("onBack", true);
        finish();
    }

    public void getToMainActivity() {
        Intent intent = new Intent(NewMessage.this, MainActivity.class);

        if (to.getText().toString().equals("")) {
            new AlertDialog.Builder(this)
                    .setTitle("Empty receiver.")
                    .setMessage("To create new message field To: should not be empty.")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }

        intent.putExtra("to", to.getText().toString());
        intent.putExtra("what", what.getText().toString());
        intent.putExtra("saveAs", saveAs.getText().toString());
        intent.putExtra("onBack", false);

        setResult(NewMessage.RESULT_OK, intent);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatementeturn true;
        return true;
    }


    public void readContacts() {


        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if (cur.getCount() > 0) while (cur.moveToNext()) {
            String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));

            if (Integer.parseInt(cur.getString(
                    cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                Cursor pCur = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        new String[]{id}, null);
                while (pCur.moveToNext()) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("Name", cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                    map.put("Number", pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                    contacts.add(map);
                }
                pCur.close();
            }
        }
    }

    private ArrayList<Integer> getIntegerArray(ArrayList<String> stringArray) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        for(String stringValue : stringArray) {
            try {
                //Convert String to Integer, and store it into integer array list.
                result.add(Integer.parseInt(stringValue));
            } catch(NumberFormatException nfe) {
                //System.out.println("Could not parse " + nfe);
                System.out.println("Parsing failed! " + stringValue + " can not be an integer");
            }
        }
        return result;
    }
    public static int[] convertIntegers(ArrayList<Integer> integers) {
        int[] ret = new int[integers.size()];
        for (int i=0; i < ret.length; i++) {
            ret[i] = integers.get(i).intValue();
        }
        return ret;
    }
}