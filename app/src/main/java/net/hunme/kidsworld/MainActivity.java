package net.hunme.kidsworld;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.hunme.message.MessageActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, MessageActivity.class);
        startActivity(intent);
    }
}
