package net.hunme.message.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.message.R;
import net.hunme.message.adapter.SearchApapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者： wh
 * 时间： 2016/7/20
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener{
    /**
     * 搜索的联系人
     */
    private EditText et_contract;
    /**
     * 回退
     */
    private ImageView iv_delete;
    /**
     * 取消搜索
     */
    private TextView tv_cancle;
    /**
     * 列表
     */
    private ListView lv_serach;

    private List<Map<String,Object>> mlist;
    /**
     * 适配器
     */
    private SearchApapter apapter;
    private   String[] name = new String[]{"王小二","刘德华","吴亦凡","吴用","周磊","鹿晗","郑爽","大幂幂","吴彦祖","刘诗诗"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initview();
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setCententTitle("搜索");
        setLiftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void initview(){
        et_contract = $(R.id.et_contract);
        iv_delete = $(R.id.iv_delete);
        tv_cancle = $(R.id.tv_cancel);
        lv_serach = $(R.id.lv_search);
        tv_cancle.setOnClickListener(this);
        iv_delete.setOnClickListener(this);
        mlist = new ArrayList<>();
        for (int i = 0;i<name.length;i++){
            Map<String,Object>  map = new HashMap<>();
            map.put("image",R.mipmap.person);
            map.put("name",name[i]);
            if (i==name.length-1){
                map.put("userid","1100");
            }
            map.put("userid","100"+(i+1));
            mlist.add(map);
        }

        apapter = new SearchApapter(this);
        lv_serach.setAdapter(apapter);
        lv_serach.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String userid = (String) mlist.get(i).get("userid");
                String username = (String) mlist.get(i).get("name");
                Intent intent = new Intent();
                intent.setClass(SearchActivity.this,PersonDetailActivity.class);
                intent.putExtra("userid",userid);
                intent.putExtra("name",username);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.tv_cancel){
            finish();
        }else if (view.getId()==R.id.iv_delete){
            et_contract.setText("");
        }
    }

}
