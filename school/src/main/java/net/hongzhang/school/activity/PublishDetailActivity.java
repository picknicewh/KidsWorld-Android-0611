package net.hongzhang.school.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.school.R;
import net.hongzhang.school.bean.PublishVo;
import net.hongzhang.school.util.PublishPhotoUtil;

import java.util.ArrayList;

public class PublishDetailActivity extends BaseActivity {
    private ImageView lv_holad;
    private TextView tv_title;
    private TextView tv_date;
    private TextView tv_content;
    private ImageView iv_image;
    private TextView tv_ptitle;

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

    private void initView() {
        lv_holad = (ImageView) findViewById(R.id.lv_holad);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_content = (TextView) findViewById(R.id.tv_content);
        iv_image = (ImageView) findViewById(R.id.iv_image);
        tv_ptitle = (TextView) findViewById(R.id.tv_ptitle);
    }
    private void initDate() {
        final PublishVo vo = (PublishVo) getIntent().getSerializableExtra("publish");
        ImageCache.imageLoader(vo.getImgUrl(), lv_holad);
        if (G.isEmteny(vo.getImgUrl())) {
            lv_holad.setImageResource(R.mipmap.ic_headmaster);//校长的头像
        } else {
            ImageCache.imageLoader(vo.getImgUrl(), lv_holad);
        }
        if (G.isEmteny(vo.getTsName())) {
            tv_title.setText("校长");
        } else {
            tv_title.setText(vo.getTsName());
        }
        if (vo.getMessageUrl() != null && vo.getMessageUrl().size() > 0) {
            ImageCache.imageLoader(vo.getMessageUrl().get(0), iv_image);
            G.setParam(this, vo.getMessageUrl().get(0), iv_image);
            iv_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PublishPhotoUtil.imageBrowernet(0, (ArrayList<String>) vo.getMessageUrl(), PublishDetailActivity.this);
                }
            });
        }
        if (!G.isEmteny(vo.getTitle())) {
            tv_ptitle.setText(vo.getTitle());
            tv_ptitle.setVisibility(View.VISIBLE);
        } else {
            tv_ptitle.setVisibility(View.GONE);
        }
        tv_date.setText(vo.getDateTime().substring(0, 11));
        tv_content.setText(vo.getMessage());
    }
}


