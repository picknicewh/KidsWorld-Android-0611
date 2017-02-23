package net.hongzhang.school.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.activity.PermissionsActivity;
import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.DateUtil;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.PermissionsChecker;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.school.R;
import net.hongzhang.school.util.PublishPhotoUtil;
import net.hongzhang.user.util.BitmapCache;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者： wh
 * 时间： 2016/10/12
 * 名称：发布通知
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class PublishInfoActivity extends BaseActivity implements OkHttpListener ,View.OnClickListener{
    /**
     * 发布照片
     */
    private GridView gv_photo;
    /**
     * 最多照片
     */
    private int maxContent=1;
    /**
     * 获取照片的列表
     */
    private ArrayList<String> itemList;
    /**
     * 内容
     */
    private EditText et_content;
    /**
     * 标题
     */
    private EditText et_title;
    /**
     * 计数
     */
    private TextView tv_count;
    /**
     * 班级ids
     */
    private String classIds;
    /**
     * 选择班级可见范围
     */
    private LinearLayout ll_classchoose;
    private int  flag ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_info);
        initView();
    }
   private void initView(){
       gv_photo = $(R.id.gv_info_photo);
       et_content = $(R.id.et_pcontent);
       et_title = $(R.id.et_ptitle);
       tv_count =$(R.id.tv_count);
       ll_classchoose = $(R.id.ll_classchoose);
       itemList = new ArrayList<>();
       ll_classchoose.setOnClickListener(this);
       flag = 1;
       DateUtil.setEditContent(et_content,tv_count);
       PublishPhotoUtil.showPhoto(this,itemList,gv_photo,maxContent);
   }
    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        setCententTitle("发布通知");
        setSubTitle("确定");
        setSubTitleOnClickListener(this);
    }
    /**
     * 发布通知
     */
    private void publishInfo(){
        if (G.isEmteny(classIds)){
            Toast.makeText(this,"请选择班级范围",Toast.LENGTH_SHORT).show();
            flag=1;
            return;
        }
        if (G.isEmteny(et_content.getText().toString())){
            Toast.makeText(this,"内容不能为空",Toast.LENGTH_SHORT).show();
            flag=1;
            return;
        }
        if (G.isEmteny(et_title.getText().toString())){
            Toast.makeText(this,"标题不能为空",Toast.LENGTH_SHORT).show();
            flag=1;
            return;
        }
        Map<String,Object> params = new HashMap<>();
        params.put("tsId", UserMessage.getInstance(this).getTsId());
        params.put("content",et_content.getText().toString());
        params.put("classIds",classIds);
        params.put("title",et_title.getText().toString());
        Type type=new TypeToken<Result<String>>(){}.getType();
        List<File> list= BitmapCache.getFileList(itemList);
        OkHttps.sendPost(type, Apiurl.SCHOOL_PUBLISHINFOR,params,list,this);
        showLoadingDialog();
    }
    @Override
    public void onSuccess(String uri, Object date) {
        flag=1;
        stopLoadingDialog();
        Result<String> data = (Result<String>) date;
        if (data!=null){
            String  result = data.getData();
            Toast.makeText(this,result,Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    @Override
    public void onError(String uri, String error) {
        flag=1;
        stopLoadingDialog();
        Toast.makeText(this,error,Toast.LENGTH_SHORT).show();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ChooseClassActivity.CHOOSE_CLASS:
                if (data!=null){
                    classIds = data.getStringExtra("classIds");
                }
                break;
            case PermissionsChecker.REQUEST_CODE:
                //检测到没有授取权限 关闭页面
                if(resultCode == PermissionsActivity.PERMISSIONS_DENIED){
                    G.showToast(this,"权限没有授取，本次操作取消，请到权限中心授权");
                }else if(resultCode==PermissionsActivity.PERMISSIONS_GRANTED){
                    G.showToast(this,"权限获取成功！");
                }
                break;
        }
    }
    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.ll_classchoose){
            Intent intent = new Intent();
            intent.setClass(this,ChooseClassActivity.class);
            startActivityForResult(intent,ChooseClassActivity.CHOOSE_CLASS);
        }else if (view.getId()==R.id.tv_subtitle){
            if (flag ==1){
                flag = 0;
                publishInfo();
               // finish();
            }
        }
    }
}
