package net.hunme.message;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.hunme.baselibrary.Test;

public class MessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Test.print(this);
    }
}
