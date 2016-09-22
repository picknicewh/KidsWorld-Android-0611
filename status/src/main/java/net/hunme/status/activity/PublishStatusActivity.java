package net.hunme.status.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import net.hunme.baselibrary.util.MyAlertDialog;
import net.hunme.baselibrary.util.PermissionsChecker;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.baselibrary.widget.LoadingDialog;
import net.hunme.status.R;
import net.hunme.status.StatusFragement;
import net.hunme.status.widget.StatusPublishPopWindow;
import net.hunme.user.adapter.GridAlbumAdapter;
import net.hunme.user.util.BitmapCache;

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
    private final String DYNAMIC ="/dynamics/issueDynamic.do";
    /**
     * 发布类型
     */
    private String dynamicType;
    /**
     * 可见范围
     */
    private String dynamicVisicty="1";
    /**
     * 限制内容
     */
    private RelativeLayout rl_restrict;
    private LoadingDialog loadingDialog;
    private TextView tv_subtilte;
    // 访问相册所需的全部权限
    private final String[] PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE, //读写权限
            Manifest.permission.CAMERA
    };
    private int maxContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_status);
        initView();
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setSubTitle("发送");
        setSubTitleOnClickListener(this);
        setLiftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!G.isEmteny(et_content.getText().toString().trim())
                        ||dynamicType.equals("1")&&itemList.size()>=1
                        ||dynamicType.equals("4")&&itemList.size()==1){
                    getExitPrompt();
                }else{
                    finish();
                }
            }
        });
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
                maxContent=9;
                goSelectImager();
                showPhoto(false);
                dynamicType="1";

                break;
            case StatusPublishPopWindow.VEDIO:
                setCententTitle("发布视频");
                dynamicType="2";
                break;
            case 4:
                rl_restrict.setVisibility(View.GONE);
                setCententTitle("发布课程");
                maxContent=1;
                goSelectImager();
                showPhoto(true);
                dynamicType="4";
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
        tv_subtilte=$(R.id.tv_subtitle);
        tv_subtilte.setOnClickListener(this);
        ll_permitchoose.setOnClickListener(this);
        setEditContent();
        rl_restrict.setVisibility(View.VISIBLE);
        itemList=new ArrayList<>();
        showView(getIntent().getIntExtra("type",1));
        loadingDialog =new LoadingDialog(this,R.style.LoadingDialogTheme);
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
    private void showPhoto(boolean isSchool){
        mAdapter=new GridAlbumAdapter(itemList,this,isSchool);
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
        int viewId = view.getId();
        if (viewId==R.id.ll_permitchoose){
            Intent intent = new Intent();
            intent.setClass(this,ChoosePermitActivity.class);
            intent.putExtra("permit",tv_permitchoose.getText().toString());
            startActivityForResult(intent,ChoosePermitActivity.CHOOSE_PERMIT);
        }else if(viewId==R.id.tv_subtitle){
            tv_subtilte.setEnabled(false);
            String dyContent=et_content.getText().toString().trim();
            if (dynamicType.equals("4")){
                publishcaurse(dyContent);
            }else{
                publishstatus(dyContent);
            }
        }
    }
    /**
     * 发布课程
     */
    private void publishcaurse(String dyContent){
        if (G.isEmteny(dyContent) || itemList.size()<1){
            G.showToast(this,"发布的内容不能为空");
            tv_subtilte.setEnabled(true);
            return;
        }
        loadingDialog.show();
        Map<String,Object>map=new HashMap<>();
        map.put("tsId",UserMessage.getInstance(this).getTsId());
        map.put("content",dyContent);
        Type type =new TypeToken<Result<String>>(){}.getType();
        List<File>list= BitmapCache.getFileList(itemList);
        OkHttps.sendPost(type, Apiurl.SCHOOL_PUBLISHCAURSE,map,list,this);
    }
    /**
     * 发布状态
     */
    private void publishstatus(String dyContent){
        if(G.isEmteny(dyContent)&&dynamicType.equals("3")||dynamicType.equals("1")&&itemList.size()<1&&G.isEmteny(dyContent)){
            G.showToast(this,"发布的内容不能为空");
            tv_subtilte.setEnabled(true);
            return;
        }
        loadingDialog.show();
        Map<String,Object>map=new HashMap<>();
        map.put("tsId",UserMessage.getInstance(this).getTsId());
        map.put("text",dyContent);
        map.put("dynamicVisicty",dynamicVisicty);
        map.put("classId", StatusFragement.CLASSID);
        Type type =new TypeToken<Result<String>>(){}.getType();
        if(dynamicType.equals("1")&&itemList.size()>0){
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
        PermissionsChecker checker=PermissionsChecker.getInstance(this);
        if(checker.lacksPermissions(PERMISSIONS)){
            checker.getPerMissions(PERMISSIONS);
            return;
        }
        AndroidImagePicker androidImagePicker =   AndroidImagePicker.getInstance();
        androidImagePicker.setSelectLimit(maxContent-itemList.size());
        androidImagePicker.pickMulti(PublishStatusActivity.this, true, new AndroidImagePicker.OnImagePickCompleteListener() {
            @Override
            public void onImagePickComplete(List<ImageItem> items) {
                if(items != null && items.size() > 0){
                    for(ImageItem item:items){
                        G.log("选择了===="+item.path);
                            itemList.add(item.path);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onSuccess(String uri, Object date) {
        tv_subtilte.setEnabled(true);
        G.showToast(this,"发布成功!");
        G.KisTyep.isReleaseSuccess=true;
        loadingDialog.dismiss();
        finish();
    }

    @Override
    public void onError(String uri, String error) {
        G.showToast(this,error);
        tv_subtilte.setEnabled(true);
        G.KisTyep.isReleaseSuccess=false;
        loadingDialog.dismiss();
    }

    /**
     * 退出提示
     */
    private void getExitPrompt(){
        View coupons_view = LayoutInflater.from(this).inflate(R.layout.alertdialog_message, null);
        final AlertDialog alertDialog = MyAlertDialog.getDialog(coupons_view, this,1);
        Button b_notrigst = (Button) coupons_view.findViewById(R.id.pop_notrigst);
        Button b_mastrigst = (Button) coupons_view.findViewById(R.id.pop_mastrigst);
        TextView pop_title = (TextView) coupons_view.findViewById(R.id.tv_poptitle);
        b_mastrigst.setText("确认");
        pop_title.setText("是否放弃本次编辑？");
        b_notrigst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        b_mastrigst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                finish();
            }
        });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if(!G.isEmteny(et_content.getText().toString().trim())||dynamicType.equals("1")&&itemList.size()>1){
                getExitPrompt();
            }else{
                finish();
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(dynamicType.equals("4")&&itemList.size()<1||null!=dynamicType&&dynamicType.equals("1")&&itemList.size()<1){
            finish();
        }
    }
}

