package net.hunme.status.activity;

import android.os.Bundle;
import android.widget.ListView;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.status.R;
import net.hunme.status.adapter.MessageDetailAdapter;

public class MessageDetailActivity extends BaseActivity {
    /**
     * 消息列表
     */
    private ListView lv_message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        initview();
    }
     private  void initview(){
         lv_message=$(R.id.lv_message_detail);
         lv_message.setAdapter(new MessageDetailAdapter(this));
    }
    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        setCententTitle("消息");
    }
}
