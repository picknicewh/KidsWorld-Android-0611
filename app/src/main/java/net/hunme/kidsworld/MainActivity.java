package net.hunme.kidsworld;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.widget.RadioGroup;

import net.hunme.kidsworld.base.ActionFragement;
import net.hunme.kidsworld.base.FindFragement;
import net.hunme.kidsworld.base.MessageFragement;
import net.hunme.kidsworld.base.SchoolFragement;

public class MainActivity extends Activity {

    private FragmentManager fragmentManager;
    private RadioGroup radioGroup;
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         fragmentManager = getFragmentManager();
         radioGroup = (RadioGroup) findViewById(R.id.rg_tab);
         FragmentTransaction transaction =fragmentManager.beginTransaction();
         transaction.replace(R.id.content,  new ActionFragement());
         transaction.commit();
         radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId){
             FragmentTransaction transaction =fragmentManager.beginTransaction();
                Fragment fragment = null;
                switch (checkedId){
                        case R.id.rb_action:
                            fragment = new ActionFragement();
                            break;
                        case R.id.rb_school:
                            fragment = new SchoolFragement();
                            break;
                        case R.id.rb_find:
                            fragment = new FindFragement();
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

