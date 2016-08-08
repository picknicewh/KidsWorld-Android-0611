package net.hunme.status.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.pizidea.imagepicker.AndroidImagePicker;
import com.pizidea.imagepicker.bean.ImageItem;

import net.hunme.baselibrary.activity.PermissionsActivity;
import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.baselibrary.mode.Result;
import net.hunme.baselibrary.network.Apiurl;
import net.hunme.baselibrary.network.OkHttpListener;
import net.hunme.baselibrary.network.OkHttps;
import net.hunme.baselibrary.util.G;
import net.hunme.baselibrary.util.PermissionsChecker;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.baselibrary.widget.LoadingDialog;
import net.hunme.status.R;
import net.hunme.status.widget.StatusPublishPopWindow;
import net.hunme.user.adapter.GridAlbumAdapter;
import net.hunme.user.util.BitmapCache;
import net.hunme.user.util.PermissionUtils;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.picturesee.util.ImagePagerActivity;

/**
 * 作者： wh
 * 时间： 2016/7/19
 * 名称：发布动态
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class PublishStatusActivity extends BaseActivity implements View.OnClickListener, OkHttpListener {
    /**
     * 文字的内容
     */
    private EditText et_content;
    /**
     *文字的长度
     */
    private TextView tv_count;
    /**
     * 显示
     */
    private GridView gv_photo;
    private GridAlbumAdapter mAdapter;
    private ArrayList<String> itemList;
    /**
     * 选择可见范围
     */
    private LinearLayout ll_permitchoose;
    /**
     * 选择的内容
     */
    private TextView tv_permitchoose;
    private final String DYNAMIC ="/dynamic/issueDynamic.do";
    /**
     * 发布类型
     */
    private String dynamicType;
    /**
     * 可见范围
     */
    private String dynamicVisicty="1";
    /**
     * 来源 school 来自发布课程，status发布动态
     */
    private String source;
    /**
     * 限制内容
     */
    private RelativeLayout rl_restrict;
    private LoadingDialog dialog;
    // 访问相册所需的全部权限
    private final String[] PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE, //读写权限
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_status);
        initView();
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        setSubTitle("发送");
        setSubTitleOnClickListener(this);
    }
    /**
     * 显示不同值传过来界面的状态
     * @param  type 类型值
     */
    private void showView(int type){
        switch (type){
            case StatusPublishPopWindow.WORDS:
                setCententTitle("发布文字");
                dynamicType="3";
                break;
            case StatusPublishPopWindow.PICTURE:
                setCententTitle("发布图片");
                goSelectImager();
                showPhoto();
                dynamicType="1";
                break;
            case StatusPublishPopWindow.VEDIO:
                setCententTitle("发布视频");
                dynamicType="2";
                break;
        }
    }
    /**
     * 初始化
     */
    private void initView(){
        et_content = $(R.id.et_pcontent);
        tv_count = $(R.id.tv_pcount);
        gv_photo =$(R.id.gv_photo);
        tv_permitchoose =  $(R.id.tv_permitchoose);
        ll_permitchoose = $(R.id.ll_permitchoose);
        rl_restrict = $(R.id.rl_restrict);
        ll_permitchoose.setOnClickListener(this);
        dialog = new LoadingDialog(this,R.style.LoadingDialogTheme);
        setEditContent();
        source = getIntent().getStringExtra("from");
        if (source.equals("status")){
            rl_restrict.setVisibility(View.VISIBLE);
            showView(getIntent().getIntExtra("type",-1));
        }else if (source.equals("school")){
            rl_restrict.setVisibility(View.GONE);
            setCententTitle("发布课程");
            goSelectImager();
            showPhoto();
        }
        dialog=new LoadingDialog(this,R.style.LoadingDialogTheme);
    }

    /**
     * 计算文字的长度，如果文字的长度大于100则不能再输入
     */
    private void setEditContent() {
        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                int count = 0;
                count = str.length();
                if (count > 140) {
                    deleteSelection(s);
                }else {
                    tv_count.setText(count+"/140");
                }
            }
            private void deleteSelection(Editable s) {
                int selection = et_content.getSelectionStart();
                if (selection > 1) {
                    s.delete(selection - 1, selection);
                }
            }
        });
    }

    /**
     * 发布照片页面
     */
    private void showPhoto(){
        itemList=new ArrayList<>();
        mAdapter=new GridAlbumAdapter(itemList,this);
        gv_photo.setAdapter(mAdapter);
        gv_photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == itemList.size()) {
                    goSelectImager();
                }else {
                    imageBrower(i,itemList);
                }
            }
        });
    }
    /**
     * 照片页面
     * @param position
     * @param urls2
     */
    private void imageBrower(int position, ArrayList<String> urls2) {
        Intent intent = new Intent(this, ImagePagerActivity.class);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        intent.putExtra("source","local");
        startActivity(intent);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ChoosePermitActivity.CHOOSE_PERMIT:
                if (data!=null){
                    String permit = data.getStringExtra("permit");
                    tv_permitchoose.setText(permit);
                    if(permit.equals("班级空间"))
                        dynamicVisicty="1";
                    else
                        dynamicVisicty="2";
                }
                break;
            case PermissionUtils.REQUEST_CODE:
                //检测到没有授取权限 关闭页面
                if(resultCode == PermissionsActivity.PERMISSIONS_DENIED){
                    G.showToast(this,"权限没有授取，本次操作取消，请到权限中心授权");
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId==R.id.ll_permitchoose){
            Intent intent = new Intent();
            intent.setClass(this,ChoosePermitActivity.class);
            intent.putExtra("permit",tv_permitchoose.getText().toString());
            startActivityForResult(intent,ChoosePermitActivity.CHOOSE_PERMIT);
        }else if(viewId==R.id.tv_subtitle){
            String dyContent=et_content.getText().toString().trim();
            if (source.equals("status")){
                publishstatus(dyContent);
            }else if (source.equals("school")){
                publishcaurse(dyContent);
            }
            dialog.show();
        }
    }
    /**
     * 发布课程
     * @param dyContent
     */
    private void publishcaurse(String dyContent){
        if (G.isEmteny(dyContent) || itemList.size()<1){
            G.showToast(this,"发布的内容不能为空");
            return;
        }
        Map<String,Object>map=new HashMap<>();
        map.put("tsId",UserMessage.getInstance(this).getTsId());
        map.put("content",dyContent);
        Type type =new TypeToken<Result<String>>(){}.getType();
        List<File>list= BitmapCache.getFileList(itemList);
        OkHttps.sendPost(type, Apiurl.SCHOOL_PUBLISHCAURSE,map,list,this);
    }
    /**
     * 发布状态
     * @param dyContent
     */
    private void publishstatus(String dyContent){
        if(G.isEmteny(dyContent)&&dynamicType.equals("3")||dynamicType.equals("1")&&itemList.size()<1){
            G.showToast(this,"发布的内容不能为空");
            return;
        }
        Map<String,Object>map=new HashMap<>();
        map.put("tsId",UserMessage.getInstance(this).getTsId());
        map.put("text",dyContent);
        map.put("dynamicVisicty",dynamicVisicty);
        Type type =new TypeToken<Result<String>>(){}.getType();
        if(dynamicType.equals("1")&&itemList.size()>0){
            dialog.show();
            List<File>list= BitmapCache.getFileList(itemList);
            dynamicType="1";
            map.put("dynamicType",dynamicType);
            OkHttps.sendPost(type,DYNAMIC,map,list,this);
        }else{
            dynamicType="3";
            map.put("dynamicType",dynamicType);
            OkHttps.sendPost(type,DYNAMIC,map,this);
        }
    }
    /**
     * 前往获取图片
     */
    private void goSelectImager(){
//        getPermission(this,PERMISSIONS);
        if(new PermissionsChecker(this).lacksPermissions(PERMISSIONS)){
            PermissionsActivity.startActivityForResult(this, PermissionUtils.REQUEST_CODE, PERMISSIONS);
            return;
        }
        AndroidImagePicker.getInstance().pickMulti(PublishStatusActivity.this, true, new AndroidImagePicker.OnImagePickCompleteListener() {
            @Override
            public void onImagePickComplete(List<ImageItem> items) {
                if(items != null && items.size() > 0){
                    for(ImageItem item:items){
                        G.log("选择了===="+item.path);
                        if (source.equals("status")){
                            if(itemList.size()<9){
                                itemList.add(item.path);
                            }
                        }else if (source.equals("school")){
                            if(itemList.size()<1){
                                itemList.add(item.path);
                            }
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onSuccess(String uri, Object date) {
//        if(DYNAMIC.equals(uri)){
//            Result<String>result= (Result<String>) date;
//            if(result.isSuccess()){
//                G.showToast(this,"发布成功!");
//                finish();
//            }else{
//                G.showToast(this,"发布失败，请稍后再试!");
//            }
//        }else if (Apiurl.SCHOOL_PUBLISHCAURSE.equals(uri)){
//            Result<String>result= (Result<String>) date;
//            if(result.isSuccess()){
//                G.showToast(this,"发布成功!");
//                finish();
//            }else{
//                G.showToast(this,"发布失败，请稍后再试!");
//            }
//            isReleaseSuccess=true;
//        }
        dialog.dismiss();
        G.showToast(this,"发布成功!");
        G.KisTyep.isReleaseSuccess=true;
       dialog.dismiss();
        finish();
    }

    @Override
    public void onError(String uri, String error) {
        dialog.dismiss();
//        if (DYNAMIC.equals(uri)){
//            G.showToast(this,"发布失败，请检测网络!");
//        }else if (Apiurl.SCHOOL_PUBLISHCAURSE.equals(uri)){
            G.showToast(this,error);
//        }
        G.KisTyep.isReleaseSuccess=false;
//        G.showToast(this,"发布失败，请检测网络!");
        dialog.dismiss();
    }

}

