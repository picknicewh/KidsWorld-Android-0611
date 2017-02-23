package net.hongzhang.status.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.database.StatusInfoDb;
import net.hongzhang.baselibrary.database.StatusInfoDbHelper;
import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.mode.StatusInfoVo;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.status.R;
import net.hongzhang.status.adapter.MessageDetailAdapter;
import net.hongzhang.status.mode.StatusInfo;
import net.hongzhang.user.activity.StatusDetilsActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageDetailActivity extends BaseActivity implements OkHttpListener,View.OnClickListener {
    /**
     * 消息列表
     */
    private ListView lv_message;
    private   List<StatusInfo> statusInfos;
    /**
     * 未读新消息的推送时间列表
     */
    private ArrayList<String> timeList;
    /**
     * 加载更多
     */
    private TextView tv_more;
    private MessageDetailAdapter adapter;
    private SQLiteDatabase db;
    private  StatusInfoDbHelper helper;
    private String tsid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        initview();
    }
     private  void initview(){
         lv_message=$(R.id.lv_message_detail);
         tv_more = $(R.id.tv_more);
         tv_more.setOnClickListener(this);
         statusInfos = new ArrayList<>();
         tsid = UserMessage.getInstance(this).getTsId();
         db = new StatusInfoDb(this).getWritableDatabase();
         helper =  StatusInfoDbHelper.getInstance();
         timeList =helper.getNoreadTime(db,tsid);
         helper.updateAllread(db,tsid);
         if (timeList.size()>0){
             getInforList(timeList.get(timeList.size()-1),timeList.get(0));
             adapter = new MessageDetailAdapter(this,statusInfos);
             lv_message.setAdapter(adapter);
             onItemClick(statusInfos);
         }
    }
    private void onItemClick(final List<StatusInfo> statusInfos){
        lv_message.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MessageDetailActivity.this, StatusDetilsActivity.class);
                intent.putExtra("dynamicId",statusInfos.get(i).getDynamic_id());
                MessageDetailActivity.this.startActivity(intent);
            }
        });
    }
    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        setCententTitle("消息");
    }
    private void getInforList(String startDate,String endDate){
        Map<String,Object> params = new HashMap<>();
        params.put("tsId", UserMessage.getInstance(this).getTsId());
        params.put("startDate",startDate);
        params.put("endDate",endDate);
        Type type = new TypeToken<Result<List<StatusInfo>>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.STATUSINFO,params,this);
    }
    @Override
    public void onSuccess(String uri, Object date) {
     if (uri.equals(Apiurl.STATUSINFO)){
       //  statusInfos.clear();
         Result<StatusInfo> data = (Result<StatusInfo>) date;
         if (data!=null){
             List<StatusInfo> statusInfoList = (List<StatusInfo>) data.getData();
             for (int i =  statusInfoList.size()-1 ;i>0;i--){
                 statusInfos.add(statusInfoList.get(i));
             }
         }
         adapter.notifyDataSetChanged();
     }
    }
    @Override
    public void onError(String uri, String error) {
        Toast.makeText(this,error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.tv_more){
            SQLiteDatabase db = new StatusInfoDb(this).getWritableDatabase();
            StatusInfoDbHelper helper =  StatusInfoDbHelper.getInstance();
            List<StatusInfoVo> statusInfovs = helper.getStatusInformVos(db,tsid);
            timeList.clear();
            for (int i =statusInfos.size() ; i <statusInfovs.size();i++){
                timeList.add(statusInfovs.get(i).getCreateTime());
            }
            if (timeList.size()>0){
                getInforList(timeList.get(timeList.size()-1),timeList.get(0));
            }
            tv_more.setText("没有更多了");
            if (tv_more.getText().toString().equals("没有更多了")){
                tv_more.setClickable(false);
            }
        }
    }
}
