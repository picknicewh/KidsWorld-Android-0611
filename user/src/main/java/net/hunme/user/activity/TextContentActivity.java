package net.hunme.user.activity;

import android.os.Bundle;
import android.widget.TextView;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.user.R;

import java.io.IOException;
import java.io.InputStream;

public class TextContentActivity extends BaseActivity {
   private int source;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_content);
        initView();
    }
    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        if (getIntent().getIntExtra("source",0)==0){
            setCententTitle("主播认证");
        }else {
            setCententTitle("版权声明");
        }

        setLiftOnClickClose();
    }
    private void initView(){
        TextView tv_copy_right=$(R.id.tv_txt_content);
        source = getIntent().getIntExtra("source",0);
        InputStream is;
        String text =null;
        try {
            if (source==0){
                is=getAssets().open("author.txt");

            }else {
                is=getAssets().open("copy_right.txt");
            }
            int size=is.available();
            byte[] b=new byte[size];
            is.read(b);
            text=new String(b,"utf-8");
            is.close();
            tv_copy_right.setText(text);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
