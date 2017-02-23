package net.hongzhang.message.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import net.hongzhang.baselibrary.contract.ContractsDb;
import net.hongzhang.baselibrary.contract.ContractsDbHelper;
import net.hongzhang.baselibrary.mode.ContractInfoVo;
import net.hongzhang.message.R;
import net.hongzhang.message.adapter.SearchApapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： wh
 * 时间： 2016/7/20
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class SearchActivity extends Activity implements View.OnClickListener{
    /**
     * 搜索的联系人
     */
    private EditText et_contract;
    /**
     * 返回前一页
     */
    private ImageView iv_back;
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
    /**
     * 适配器
     */
    private SearchApapter apapter;
    /**
     * 本地联系人列表数据库
     */
    private SQLiteDatabase db;
    private ContractsDbHelper helper;
    /**
     * 数据列表
     */
   private List<ContractInfoVo> friendInforVos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initview();
    }
    private void initview(){
        et_contract = (EditText) findViewById(R.id.et_contract);
        iv_delete = (ImageView) findViewById(R.id.iv_delete);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_cancle = (TextView) findViewById(R.id.tv_cancel);
        lv_serach = (ListView) findViewById(R.id.lv_search);
        tv_cancle.setOnClickListener(this);
        iv_delete.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        friendInforVos = new ArrayList<>();
        db = new ContractsDb(this).getReadableDatabase();
        helper = ContractsDbHelper.getinstance();
        setlistview();
        setFriendInforVos();
    }
    private void setFriendInforVos(){
        et_contract.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String name = editable.toString();
                friendInforVos = helper.getFriendInform(db,name);
                apapter = new SearchApapter(SearchActivity.this,friendInforVos);
                lv_serach.setAdapter(apapter);
            }
        });
    }
   private void setlistview(){
       lv_serach.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               ContractInfoVo friendInforVo = friendInforVos.get(i);
               String userid = friendInforVo.getTsId();
               Intent intent = new Intent();
               intent.setClass(SearchActivity.this,PersonDetailActivity.class);
               intent.putExtra("targetId",userid);
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
        }else if (view.getId()==R.id.iv_back){
            finish();
        }
    }
}
