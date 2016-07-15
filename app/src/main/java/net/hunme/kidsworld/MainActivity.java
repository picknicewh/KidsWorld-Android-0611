package net.hunme.kidsworld;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;

import net.hunme.discovery.DiscoveryFragement;
import net.hunme.message.fragment.MessageFragement;
import net.hunme.school.SchoolFragement;
import net.hunme.status.StatusFragement;


public class MainActivity extends FragmentActivity {

    private FragmentManager fragmentManager;
    private RadioGroup radioGroup;
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         fragmentManager =getSupportFragmentManager();
         radioGroup = (RadioGroup) findViewById(R.id.rg_tab);
         FragmentTransaction transaction =fragmentManager.beginTransaction();
         transaction.replace(R.id.content,  new StatusFragement());
         transaction.commit();
         radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId){
             FragmentTransaction transaction =fragmentManager.beginTransaction();
                Fragment fragment = null;
                switch (checkedId){
                        case R.id.rb_action:
                            fragment = new StatusFragement();
                            break;
                        case R.id.rb_school:
                            fragment = new SchoolFragement();
                            break;
                        case R.id.rb_find:
                            fragment = new DiscoveryFragement();
                            break;
                        case R.id.rb_message:
                            fragment = new MessageFragement();
                            break;
                }
                transaction.replace(R.id.content, fragment);
                transaction.commit();
            }
        });
    }
}

