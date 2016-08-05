package net.hunme.school.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.baselibrary.image.ImageCache;
import net.hunme.school.R;
import net.hunme.school.bean.PublishVo;

public class PublishDetailActivity extends BaseActivity {
    private ImageView lv_holad;
    private TextView tv_title;
    private TextView tv_date;
    private TextView tv_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_detail);
        initView();
        initDate();
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        setCententTitle("通知详情");
    }

    private void initView(){
        lv_holad = (ImageView)findViewById(R.id.lv_holad);
        tv_title= (TextView) findViewById(R.id.tv_title);
        tv_date= (TextView)findViewById(R.id.tv_date);
        tv_content= (TextView)findViewById(R.id.tv_content);
    }

    private void initDate(){
        PublishVo vo= (PublishVo) getIntent().getSerializableExtra("publish");
        ImageCache.imageLoader(vo.getImgUrl(),lv_holad);
        tv_title.setText(vo.getTsName());
        tv_date.setText(vo.getDateTime().substring(0,11));
        tv_content.setText(vo.getMessage());
    }
}
