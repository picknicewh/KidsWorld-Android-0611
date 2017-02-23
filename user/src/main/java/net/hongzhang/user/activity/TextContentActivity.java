package net.hongzhang.user.activity;

import android.os.Bundle;
import android.widget.TextView;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.user.R;

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
        source = getIntent().getIntExtra("source",0);
        if (source==0){
            setCententTitle("主播认证");
        }else if (source==1){
            setCententTitle("版权声明");
        }else if (source==2){
            setCententTitle("服务条款");
        }

        setLiftOnClickClose();
    }
    private void initView(){
        TextView tv_copy_right=$(R.id.tv_txt_content);
        source = getIntent().getIntExtra("source",0);
        InputStream is=null;
        String text =null;
        try {
            if (source==0){
                is=getAssets().open("author.txt");
            }else if (source==1){
                is=getAssets().open("copy_right.txt");
            }else if (source==2){
                is=getAssets().open("useragreement.txt");
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
