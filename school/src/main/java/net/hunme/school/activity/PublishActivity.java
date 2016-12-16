package net.hunme.school.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.baselibrary.mode.Result;
import net.hunme.baselibrary.network.OkHttpListener;
import net.hunme.baselibrary.network.OkHttps;
import net.hunme.baselibrary.util.G;
import net.hunme.baselibrary.database.PublishDb;
import net.hunme.baselibrary.database.PublishDbHelp;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.school.R;
import net.hunme.school.adapter.PublishAdapter;
import net.hunme.school.bean.PublishVo;

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
    private int flag = 0;
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
        if (publishList.size()>0&&publishList!=null&&flag==1){
            publishList.clear();
            getPublishMessage();
            flag=0;
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
            for (int i = publishVos.size()-1;i>0;i--){
                PublishVo vo= publishVos.get(i);
                boolean isRead= PublishDbHelp.select(PublishActivity.db.getReadableDatabase(),vo.getMessageId()+um.getTsId());
              /*  if(!isRead)
                    //将未读信息排在最前列
                    publishList.add(0,vo);
                else*/
                    publishList.add(vo);
                vo.setRead(isRead);
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
                flag=1;
            }

        }
    }
}
