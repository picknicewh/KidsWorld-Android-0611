package net.hongzhang.school.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.school.R;
import net.hongzhang.school.adapter.ChooseMyClassAdapter;
import net.hongzhang.school.bean.ClassVo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者： wh
 * 时间： 2016/7/19
 * 名称：选择发布通知的可见范围
 * 版本说明：
 * 附加注释：
 * 主要接口：1获取班级列表
 *          2.发布通知
 */
public class ChooseClassActivity extends BaseActivity implements View.OnClickListener, OkHttpListener {
    public static  final  int CHOOSE_CLASS=2;
    private ListView lv_class;

    /**
     * 顶部右侧
     */
    private TextView tv_right;
    /**
     * 是否选中对话框的item
     */
    private HashMap<Integer, Boolean> isSelected;
    /**
     * 适配器
     */
    private ChooseMyClassAdapter adapter;
    /**
     * 选择的班级
     */
    private List<String> chooseClassList;
    /**
     * 班级列表对象
     */
    private List<ClassVo> classVos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_class);
        initView();
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setCententTitle("选择班级");
        setLiftOnClickClose();
        setSubTitle("完成");
    }
    private void  initView(){
        lv_class = $(R.id.lv_class);
        tv_right = $(R.id.tv_subtitle);
        chooseClassList = new ArrayList<>();
        tv_right.setOnClickListener(this);
        classVos = new ArrayList<>();
        getDynamicHead();
        setListview();
    }
    /**
     * 设置列表
     */
    private void setListview(){
        lv_class.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChooseMyClassAdapter.ViewHolder vholder = (ChooseMyClassAdapter.ViewHolder) view.getTag();
                vholder.cb_class.setChecked(!vholder.cb_class.isChecked());
                adapter.getIsSelected().put(position,vholder.cb_class.isChecked());
                isSelected = adapter.getIsSelected();
                chooseClassList= getClassItem(isSelected,classVos);
            }
        });
    }
    /**
     * 获取班级列表
     */
    private void getDynamicHead(){
        if(G.isEmteny(UserMessage.getInstance(this).getTsId())){
            return;
        }
        Map<String,Object> map=new HashMap<>();
        map.put("tsId", UserMessage.getInstance(this).getTsId());
        Type type=new TypeToken<Result<List<ClassVo>>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.SCHOOL_GETCLASSLIST,map,this);
    }
    /**
     * 获取被选中的班级
     * @param isSelected 是否被选中的列表
     * @param dynamicList 班级列表
     */
    private List<String> getClassItem(HashMap<Integer, Boolean> isSelected,  List<ClassVo> dynamicList) {
        List<String> classids = new ArrayList<>();
        for (int i = 0; i < isSelected.size(); i++) {
            boolean ischeck = isSelected.get(i);
            if (ischeck) {
                String name =dynamicList.get(i).getClass_id();
                classids.add(name);
            }
        }
        return classids;
    }
    /**
     * 把list转换成以,分割的字符串
     * @param  chooseClassList
     */
    private String getChooseName(List<String> chooseClassList ){
       if (chooseClassList.size()>0){
           StringBuffer classids = new StringBuffer();
           for (int i = 0 ;i<chooseClassList.size();i++){
               classids.append(chooseClassList.get(i)).append(",");
           }
           classids.deleteCharAt(classids.length()-1);
           return classids.toString();
       }else {
           return null;
       }

    }
    @Override
    public void onClick(View view) {
        int viewId =view.getId();
        if (viewId==R.id.tv_subtitle){
            if (G.isEmteny(getChooseName(chooseClassList))){
                Toast.makeText(this,"未选择发送对象",Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(this,PublishActivity.class);
            intent.putExtra("classIds",getChooseName(chooseClassList));
            setResult(CHOOSE_CLASS,intent);
            finish();
        }
    }
    @Override
    public void onSuccess(String uri, Object date) {
        if(Apiurl.SCHOOL_GETCLASSLIST.equals(uri)){
            Result<List<ClassVo>> data = (Result<List<ClassVo>>) date;
            if (data!=null){
                classVos = data.getData();
                adapter = new ChooseMyClassAdapter(this,classVos);
                lv_class.setAdapter(adapter);
            }
        }
    }
    @Override
    public void onError(String uri, String error) {
        Toast.makeText(this,error,Toast.LENGTH_SHORT).show();
    }
}
