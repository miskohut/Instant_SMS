package com.acpp.Instant_SMS;

import android.content.Intent;
import android.os.Bundle;

/**
 * Created by misko on 12.1.2015.
 */
public class EditMessage extends NewMessage {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        to.setText(intent.getStringExtra("to"));
        what.setText(intent.getStringExtra("what"));
        saveAs.setText(intent.getStringExtra("saveAs"));
    }
}