package net.hongzhang.school.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.database.PublishDb;
import net.hongzhang.baselibrary.database.PublishDbHelp;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.school.R;
import net.hongzhang.school.adapter.PublishAdapter;
import net.hongzhang.school.bean.PublishVo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PublishActivity extends BaseActivity implements OkHttpListener, View.OnClickListener {
    private ListView lv_publish;
    private PublishAdapter adapter;
    private List<PublishVo> publishList;
    public static PublishDb db;
    private final String MESSAGE="/school/message.do";
    private UserMessage um;
    /**
     *  无网络状态
     */
    private RelativeLayout rl_nonetwork;
    private int type;
    /**
     * 没有数据
     */
    private TextView tv_nodata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        initView();
        initDate();
    }
    /**
     *初始数据
     */
    private void initView(){
        lv_publish=$(R.id.lv_publish);
        rl_nonetwork = $(R.id.rl_nonetwork);
        tv_nodata = $(R.id.tv_nodata);
        rl_nonetwork.setOnClickListener(this);
    }

    private void initDate(){
        db=new PublishDb(this);
        um=UserMessage.getInstance(this);
        publishList=new ArrayList<>();
        adapter=new PublishAdapter(this,publishList);
        lv_publish.setAdapter(adapter);
        lv_publish.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(PublishActivity.this,PublishDetailActivity.class);
                intent.putExtra("publish",publishList.get(i));
                if(!PublishDbHelp.select(db.getReadableDatabase(),publishList.get(i).getMessageId()+um.getTsId())){
                    PublishDbHelp.insert(db.getWritableDatabase(),publishList.get(i).getMessageId()+um.getTsId());
                    publishList.get(i).setRead(true);
                }
                startActivityForResult(intent,0);
            }
        });
        getPublishMessage();
//        testDate();
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        setCententTitle("通知");
        if (!UserMessage.getInstance(this).getType().equals("1")){
            setSubTitle("发布通知");
            type=1;
        }else {
            setSubTitle("全部阅读");
            type=2;
        }

        setSubTitleOnClickListener(this);
    }

    /**
     * 获取通知
     */
    private void getPublishMessage(){
        Map<String,Object>map=new HashMap<>();
        map.put("tsId", UserMessage.getInstance(this).getTsId());
        map.put("dateTime",UserMessage.getInstance(this).getPublishDateTime());
        Type type=new TypeToken<Result<List<PublishVo>>>(){}.getType();
        OkHttps.sendPost(type,MESSAGE,map,this,2,"PUBLISH");
        if (G.isNetworkConnected(this)){
            rl_nonetwork.setVisibility(View.GONE);
            showLoadingDialog();
        }else {
            rl_nonetwork.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (publishList!=null){
            publishList.clear();
            getPublishMessage();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0){
            if (publishList.size()>0&&publishList!=null){
                publishList.clear();
                getPublishMessage();
            }
        }
    }

    @Override
    public void onSuccess(String uri, Object date) {
        if (date!=null){
            stopLoadingDialog();
            List<PublishVo> publishVos=((Result<List<PublishVo>>)date).getData();
            for (int i = publishVos.size()-1;i>=0;i--){
                 PublishVo vo= publishVos.get(i);
                 boolean isRead= PublishDbHelp.select(PublishActivity.db.getReadableDatabase(),vo.getMessageId()+um.getTsId());
                 vo.setRead(isRead);
                 publishList.add(vo);
            }
            if (publishList.size()==0){
                tv_nodata.setVisibility(View.VISIBLE);
                lv_publish.setVisibility(View.GONE);
            }else {
                tv_nodata.setVisibility(View.GONE);
                lv_publish.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onError(String uri, String error) {
        stopLoadingDialog();
        rl_nonetwork.setVisibility(View.VISIBLE);
        G.showToast(this,error);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.rl_nonetwork){
            publishList.clear();
            getPublishMessage();
        }else if (view.getId()==R.id.tv_subtitle){
            if (type==2){
                //
                if(publishList.size()<0){
                    //通知列表为空返回
                    return;
                }
                //遍历消息列表
                for (PublishVo p:publishList){
                    //所有未读的插入数据库
                    if(!p.isRead()){
                        PublishDbHelp.insert(db.getWritableDatabase(),p.getMessageId()+um.getTsId());
                        p.setRead(true);
                    }
                }
                //刷新适配器
                adapter.notifyDataSetChanged();
            }else {
                Intent intent = new Intent(this,PublishInfoActivity.class);
                startActivity(intent);

            }

        }
    }
}
