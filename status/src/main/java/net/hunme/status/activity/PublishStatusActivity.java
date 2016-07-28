package net.hunme.status.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.pizidea.imagepicker.AndroidImagePicker;
import com.pizidea.imagepicker.bean.ImageItem;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.baselibrary.mode.Result;
import net.hunme.baselibrary.network.OkHttpListener;
import net.hunme.baselibrary.network.OkHttps;
import net.hunme.baselibrary.util.G;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.status.R;
import net.hunme.status.widget.StatusPublishPopWindow;
import net.hunme.user.adapter.GridAlbumAdapter;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private GridView gv_photo;
    private GridAlbumAdapter mAdapter;
    private List<ImageItem> itemList;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_status);
        initView();
        showView(getIntent().getIntExtra("type",-1));
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
        ll_permitchoose.setOnClickListener(this);
        setEditContent();
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
                }
            }
        });
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
            if(G.isEmteny(dyContent)&&dynamicType.equals("3")){
                G.showToast(this,"发布的内容不能为空");
                return;
            }
            Map<String,Object>map=new HashMap<>();
            map.put("tsId",UserMessage.getInstance(this).getTsId());
            map.put("dynamicType",dynamicType);
            map.put("text",dyContent);
            map.put("dynamicVisicty",dynamicVisicty);
            Type type =new TypeToken<Result<String>>(){}.getType();
            if(itemList.size()>1){
                List<File>list=new ArrayList<>();
                for (int i=0;i<itemList.size()-1;i++){
                    G.log(itemList.get(i).path+"-----------------文件地址");
                    list.add(new File(itemList.get(i).path));
                }
                OkHttps.sendPost(type,DYNAMIC,map,list,this);
            }else{
                OkHttps.sendPost(type,DYNAMIC,map,this);
            }
        }
    }

    /**
     * 前往获取图片
     */
    private void goSelectImager(){
        AndroidImagePicker.getInstance().pickMulti(PublishStatusActivity.this, true, new AndroidImagePicker.OnImagePickCompleteListener() {
            @Override
            public void onImagePickComplete(List<ImageItem> items) {
                if(items != null && items.size() > 0){
                    for(ImageItem item:items){
                        G.log("选择了===="+item.path);
                        if(itemList.size()<9){
                            itemList.add(item);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onSuccess(String uri, Object date) {
        if(DYNAMIC.equals(uri)){
            Result<String>result= (Result<String>) date;
            if(result.isSuccess()){
                G.showToast(this,"发布成功!");
                finish();
            }else{
                G.showToast(this,"发布失败，请稍后再试!");
            }
        }
    }

    @Override
    public void onError(String uri, String error) {
        G.showToast(this,"发布失败，请检测网络!");
    }
}

