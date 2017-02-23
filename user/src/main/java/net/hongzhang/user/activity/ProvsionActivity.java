package net.hongzhang.user.activity;

import android.os.Bundle;
import android.widget.TextView;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.user.R;

import java.io.IOException;
import java.io.InputStream;

public class ProvsionActivity extends BaseActivity {
    private TextView tv_provsion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provsion);
        initView();
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        setCententTitle("服务条款");
    }
    private void initView(){
        tv_provsion=$(R.id.tv_provsion);
        try {
            InputStream is=getAssets().open("provison.txt");
            int size=is.available();

            byte[] b=new byte[size];
            is.read(b);
            is.close();
            String text=new String(b,"UTF-8");
            tv_provsion.setText(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
