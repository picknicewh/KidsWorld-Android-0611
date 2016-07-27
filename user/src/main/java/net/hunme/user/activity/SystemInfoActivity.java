package net.hunme.user.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
import net.hunme.user.R;
import net.hunme.user.adapter.SystemInfoAdapter;
import net.hunme.user.mode.MessageVo;
import net.hunme.user.util.SystemInfoDb;
import net.hunme.user.util.SystemInfoDbHelp;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SystemInfoActivity extends BaseActivity implements OkHttpListener {
    private ListView lv_systeminfo;
    private List<MessageVo>messageList;
    private SystemInfoAdapter adapter;
    private final String SYSTEMESSAGE="/appUser/systemMessages.do";
    public static SystemInfoDb infoDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_info);
        initView();
    }

    @Override
    protected void setToolBar(){
        setCententTitle("系统消息");
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
    }

    private void initView(){
        lv_systeminfo=$(R.id.lv_systeminfo);
        messageList=new ArrayList<>();
        adapter=new SystemInfoAdapter(messageList,this);
        lv_systeminfo.setAdapter(adapter);
        testDate();
//        getMessageDate(UserMessage.getInstance(this).getTsId());
        infoDb=new SystemInfoDb(this);//创建数据库
        lv_systeminfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String value=messageList.get(i).getContent()+messageList.get(i).getDate();
                SQLiteDatabase rdb=infoDb.getReadableDatabase();
                if(!SystemInfoDbHelp.select(rdb,value)){
                    SQLiteDatabase wdb = infoDb.getWritableDatabase();
                    SystemInfoDbHelp.insert(wdb,value);
                }
                startActivity(new Intent(SystemInfoActivity.this,InfoDetailsActivity.class));
            }
        });

    }

    private void getMessageDate(String tsId){
        Map<String,Object>map=new HashMap<>();
        map.put("tsId",tsId);
        Type type=new TypeToken<Result<List<MessageVo>>>(){}.getType();
        OkHttps.sendPost(type,SYSTEMESSAGE,map,this);
    }

    @Override
    public void onSuccess(String uri, Object date) {
        if(SYSTEMESSAGE.equals(uri)){
            Result<List<MessageVo>>result= (Result<List<MessageVo>>) date;
            if(result.isSuccess()){
                messageList=result.getData();
                adapter.notifyDataSetChanged();
            }else {
                G.showToast(this,"消息获取失败，请稍后再试！");
            }
        }
    }

    @Override
    public void onError(String uri, String error) {
        G.showToast(this,"消息获取失败，请检查网络再试！");
    }

    private void testDate(){
        for (int i=0;i<100;i++){
            MessageVo vo=new MessageVo();
            vo.setContent(i*10+"");
            vo.setDate(i+"");
            messageList.add(vo);
        }
        adapter.notifyDataSetChanged();
    }
}
