package com.acpp.Instant_SMS;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.widget.AdapterView.*;


public class MainActivity extends ActionBarActivity {

    public static final int NEW_MESSAGE = 1;
    public static final int EDIT_MESSAGE = 2;
    private ListView listView;
    private Button newMessage;
    private ArrayList<String> to = new ArrayList<String>();
    private ArrayList<String> what = new ArrayList<String>();
    private ArrayList<String> saveAs = new ArrayList<String>();
    private SmsList adapter;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        listView = (ListView)findViewById(R.id.list);
        newMessage = (Button)findViewById(R.id.new_message_button);

        newMessage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewMessage.class);
                startActivityForResult(intent, NEW_MESSAGE);
            }
        });





        listView.setClickable(true);

        // ListView Item Click Listener

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {

                setPosition(position);

                Intent intent = new Intent(MainActivity.this, NewMessage.class);
                intent.putExtra("to", to.get(position));
                intent.putExtra("what", what.get(position));
                intent.putExtra("saveAs", saveAs.get(position));
                startActivityForResult(intent, EDIT_MESSAGE);

            }
        });

        listView.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return true;
            }
        });


    }

    public void sendSms(View v) {

        final View b_view = v;

        new AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage("Do you really want to send a message?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        View view = b_view.getRootView();

                        TextView to = (TextView) view.findViewById(R.id.invisible);
                        TextView what = (TextView) view.findViewById(R.id.view_what);

                        String number = to.getText().toString();
                        String body = what.getText().toString();

                        send(number, body);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    public void setListView(ListView view) {
        listView = view;
    }

    public ListView getListView() {
        return listView;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }







    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch(requestCode) {
            case NEW_MESSAGE:
                if (!intent.getBooleanExtra("onBack", false)) {
                    to.add(intent.getStringExtra("to"));
                    what.add(intent.getStringExtra("what"));
                    saveAs.add(intent.getStringExtra("saveAs"));
                    adapter = new SmsList(to, what, saveAs, this);
                    listView.setAdapter(adapter);
                }
                break;

            case EDIT_MESSAGE:
                if (!intent.getBooleanExtra("onBack", false)) {
                    to.set(position, intent.getStringExtra("to"));
                    what.set(position, intent.getStringExtra("what"));
                    saveAs.set(position, intent.getStringExtra("saveAs"));
                    adapter = new SmsList(to, what, saveAs, this);
                    listView.setAdapter(adapter);
                }

                break;

        }
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

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    private void send (String phoneNumber, String message) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Your message has not been sent.",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }
}