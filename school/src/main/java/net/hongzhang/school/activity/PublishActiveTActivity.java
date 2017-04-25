package net.hongzhang.school.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.school.R;
import net.hongzhang.school.adapter.PublishListAdapter;

import butterknife.ButterKnife;

public class PublishActiveTActivity extends BaseActivity {
    private TextView tvPublishCount;
    private ListView lvPublishList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_active_t);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvPublishCount = (TextView) findViewById(R.id.tv_publish_count);
        lvPublishList = (ListView) findViewById(R.id.lv_publish_list);
        lvPublishList.setAdapter(new PublishListAdapter(this));
        lvPublishList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),PublishActiveDetailActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void setToolBar() {
        setCententTitle("活动上传");
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
    }
}
