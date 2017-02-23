package net.hongzhang.school.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.school.R;
import net.hongzhang.school.TestActivity;
import net.hongzhang.status.mode.DynamicVo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassListActivity extends BaseActivity implements OkHttpListener {
    private ListView lv_class;
    /**
     * 班级列表对象
     */
    private List<DynamicVo> dynamicList;
    private List<String>classList;
    private ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);
        lv_class= (ListView) findViewById(R.id.lv_class);
        classList=new ArrayList<>();
        adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,classList);
        lv_class.setAdapter(adapter);
        getDynamicHead();
        lv_class.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    startActivity(new Intent(ClassListActivity.this,TestActivity.class));
                }else{
                    G.showToast(ClassListActivity.this,"该班级暂无次功能");
                }
            }
        });
    }

    /**
     * 获取班级列表
     */
    private void getDynamicHead(){
        Map<String,Object> map=new HashMap<>();
        map.put("tsId", UserMessage.getInstance(this).getTsId());
        Type type=new TypeToken<Result<List<DynamicVo>>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.DYNAMICHEAD,map,this,2,"DYNAMIC");
        showLoadingDialog();
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        setCententTitle("选择班级");
    }

    @Override
    public void onSuccess(String uri, Object date) {
        stopLoadingDialog();
        dynamicList = ((Result<List<DynamicVo>>) date).getData();
        for(int i=0;i<dynamicList.size();i++){
            if(dynamicList.get(i).getGroupType().equals("1"))
            classList.add(dynamicList.get(i).getGroupName());
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onError(String uri, String error) {
        stopLoadingDialog();
    }
}
