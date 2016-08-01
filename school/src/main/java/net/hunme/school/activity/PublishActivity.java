package net.hunme.school.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.baselibrary.mode.Result;
import net.hunme.baselibrary.network.OkHttpListener;
import net.hunme.baselibrary.network.OkHttps;
import net.hunme.baselibrary.util.G;
import net.hunme.baselibrary.util.PublishDb;
import net.hunme.baselibrary.util.PublishDbHelp;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.school.R;
import net.hunme.school.adapter.PublishAdapter;
import net.hunme.school.bean.PublishVo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PublishActivity extends BaseActivity implements OkHttpListener {
    private ListView lv_publish;
    private PublishAdapter adapter;
    private List<PublishVo> publishList;
    public static PublishDb db;
    private final String MESSAGE="/school/message.do";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        initView();
        initDate();
    }
    private void initView(){
        lv_publish=$(R.id.lv_publish);
    }

    private void initDate(){
        db=new PublishDb(this);
        publishList=new ArrayList<>();
        adapter=new PublishAdapter(this,publishList);
        lv_publish.setAdapter(adapter);
        lv_publish.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(PublishActivity.this,PublishDetailActivity.class);
//                intent.putExtra("publish",publishList.get(i));
                PublishDbHelp.insert(db.getWritableDatabase(),publishList.get(i).getMessageId());
                publishList.get(i).setRead(true);
                startActivityForResult(intent,0);
            }
        });
//        getPublishMessage();
        testDate();
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        setCententTitle("通知");
        setSubTitle("全部阅读");
        setSubTitleOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(publishList.size()<0){
                    //通知列表为空返回
                    return;
                }
                //遍历消息列表
                for (PublishVo p:publishList){
                    //所有未读的插入数据库
                    if(!p.isRead()){
                        PublishDbHelp.insert(db.getWritableDatabase(),p.getMessageId());
                        p.setRead(true);
                    }
                }
                //刷新适配器
                adapter.notifyDataSetChanged();
            }
        });
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0){
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSuccess(String uri, Object date) {
        List<PublishVo> publishVos=((Result<List<PublishVo>>)date).getData();
        for (int i=0;i<publishVos.size();i++){
            PublishVo vo=new PublishVo();
            boolean isRead= PublishDbHelp.select(PublishActivity.db.getReadableDatabase(),vo.getMessageId());
            if(!isRead)
                //将未读信息排在最前列
                publishList.add(0,vo);
            else
                publishList.add(vo);
            vo.setRead(isRead);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onError(String uri, String error) {
        G.showToast(this,error);
    }

    private void testDate(){
        for (int i=0;i<10;i++){
            PublishVo vo=new PublishVo();
            vo.setDateTime("2016-08-01");
            vo.setMessage("近期接到部分win10系统的用户反馈，在登陆游戏中会弹出警告码为（20,80002400,215）或（20,80001000,56）的弹框，且该弹窗无法关闭，导致游戏无法正常进行。经过紧急定位确认，微软最新发布的win10 1607版本，对签名机制进行了变更，导致与大部分游戏、部分应用程序有所冲突。经过测试验证，《地下城与勇士》也会受到影响。");
            vo.setTsName("DNF");
            vo.setMessageId(i+"");
            boolean isRead= PublishDbHelp.select(PublishActivity.db.getReadableDatabase(),vo.getMessageId());
            if(!isRead)
                publishList.add(0,vo);
            else
                publishList.add(vo);
            vo.setRead(isRead);
        }
        adapter.notifyDataSetChanged();
    }
}
