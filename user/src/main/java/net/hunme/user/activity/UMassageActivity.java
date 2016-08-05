package net.hunme.user.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.pizidea.imagepicker.AndroidImagePicker;

import net.hunme.baselibrary.activity.PermissionsActivity;
import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.baselibrary.image.ImageCache;
import net.hunme.baselibrary.mode.Result;
import net.hunme.baselibrary.network.OkHttpListener;
import net.hunme.baselibrary.network.OkHttps;
import net.hunme.baselibrary.util.G;
import net.hunme.baselibrary.util.PermissionsChecker;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.baselibrary.widget.CircleImageView;
import net.hunme.user.R;
import net.hunme.user.util.BitmapCache;
import net.hunme.user.util.MyAlertDialog;
import net.hunme.user.util.PermissionUtils;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/18
 * 描    述：用户信息
 * 版    本：1.0
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class UMassageActivity extends BaseActivity implements View.OnClickListener, OkHttpListener {
    private RelativeLayout rl_userMessage;
    private LinearLayout ll_sex;
    private LinearLayout ll_sign;
    private CircleImageView cv_head;
    private TextView tv_name;
    private TextView tv_sex;
    private TextView tv_sign;
    private TextView tv_classname;
    private TextView tv_schoolname;
    private UserMessage um;
    private final String SETSIGN="/appUser/setSignature.do";
    private final String AVATAR="/appUser/setAvatar.do";
    private String path;//选择头像保存地址
    private String sign; //用户个性签名
    // 访问相册所需的全部权限
    private final String[] PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE, //读写权限
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_massage);
        initView();
        initData();
    }

    private void initView(){
        rl_userMessage=$(R.id.rl_userMessage);
        cv_head=$(R.id.cv_head);
        tv_name=$(R.id.tv_name);
        tv_sex =$(R.id.tv_sex);
        tv_sign=$(R.id.tv_sign);
        tv_classname=$(R.id.tv_classname);
        tv_schoolname=$(R.id.tv_schoolname);
        ll_sex=$(R.id.ll_sex);
        ll_sign=$(R.id.ll_sign);
        rl_userMessage.setOnClickListener(this);
//        ll_sex.setOnClickListener(this);
        ll_sign.setOnClickListener(this);
    }

    private void initData(){
        um=UserMessage.getInstance(this);
        ImageCache.imageLoader(um.getHoldImgUrl(),cv_head);
        tv_name.setText(um.getUserName());
        tv_sex.setText(um.getSex());
        tv_sign.setText(um.getUserSign());
        tv_classname.setText(um.getClassName());
        tv_schoolname.setText(um.getSchoolName());
        path= Environment.getExternalStorageDirectory().toString() + "/ChatFile/";
    }
    @Override
    protected void setToolBar() {
        setCententTitle("个人信息");
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
    }

    @Override
    public void onClick(View v) {
        int viewID=v.getId();
        if(viewID==R.id.rl_userMessage){
            if(new PermissionsChecker(this).lacksPermissions(PERMISSIONS)){
                PermissionsActivity.startActivityForResult(this, PermissionUtils.REQUEST_CODE, PERMISSIONS);
                return;
            }
            AndroidImagePicker.getInstance().pickAndCrop(UMassageActivity.this, true, 200, new AndroidImagePicker.OnImageCropCompleteListener() {
                @Override
                public void onImageCropComplete(Bitmap bmp, float ratio) {
//                    Log.i(TAG,"=====onImageCropComplete (get bitmap="+bmp.toString());
//                    ivCrop.setVisibility(View.VISIBLE);
                    path=path+ new Date().getTime()+".jpg";
                    BitmapCache.compressBiamp(bmp,path,100);//压缩图片到该路径 path
                    List<File> files=new ArrayList<>();
                    files.add(new File(path));//从该路径拿到图片
                    userAvatarSubmit(files);
                }
            });
        }else if(viewID==R.id.ll_sex){
//            showSexDialog();
        }else if(viewID==R.id.ll_sign){
            showSignDialog();
        }
    }

    /**
     * 性别选择框
     */
    public void showSexDialog(){
        View view= LayoutInflater.from(this).inflate(R.layout.alertdialog_select_sex,null);
        final AlertDialog alertDialog= MyAlertDialog.getDialog(view,this,0);
        final RadioButton rb_boy= (RadioButton) view.findViewById(R.id.rb_boy);
        final RadioButton rb_girl= (RadioButton) view.findViewById(R.id.rb_girl);
        if(um.getSex().equals("女"))
            rb_girl.setChecked(true);
        else
            rb_boy.setChecked(true);
        rb_boy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                um.setSex(rb_boy.getText().toString());
                tv_sex.setText(um.getSex());
                alertDialog.dismiss();
            }
        });
        rb_girl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                um.setSex(rb_girl.getText().toString());
                tv_sex.setText(um.getSex());
                alertDialog.dismiss();
            }
        });
    }

    public void showSignDialog(){
        //获取View
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.alertdialog_add_album, null);
        //获取弹框
        final AlertDialog alertDialog =MyAlertDialog.getDialog(dialogView,this,1);
        Button alertCancel= (Button) dialogView.findViewById(R.id.b_cancel);
        Button alertConfirm= (Button) dialogView.findViewById(R.id.b_confirm);
        final EditText etAlbumName= (EditText) dialogView.findViewById(R.id.et_album_name);
        etAlbumName.setHint("请输入签名");
        etAlbumName.setFocusable(true);
        etAlbumName.setFocusableInTouchMode(true);
        etAlbumName.requestFocus();
        //取消
        alertCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        //确定
        alertConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sign=etAlbumName.getText().toString();
                if (G.isEmteny(sign)) {
                   G.showToast(UMassageActivity.this,"签名不能为空");
                    return;
                }
                userSignSubmit(sign);
                alertDialog.dismiss();
            }
        });
    }

    /**
     * 提交用户个性签名
     * @param userSign
     */
    public void userSignSubmit(String userSign){
        Map<String,Object>map=new HashMap<>();
        map.put("tsId",um.getTsId());
        map.put("signature",userSign);
        Type type=new TypeToken<Result<String>>(){}.getType();
        OkHttps.sendPost(type,SETSIGN,map,this);
    }

    private void userAvatarSubmit(List<File>list){
        Map<String,Object>map=new HashMap<>();
        map.put("tsId",um.getTsId());
        Type type=new TypeToken<Result<String>>(){}.getType();
        OkHttps.sendPost(type,AVATAR,map,list,this);
    }

    @Override
    public void onSuccess(String uri, Object date) {
        if(SETSIGN.equals(uri)){
            um.setUserSign(sign);
            tv_sign.setText(um.getUserSign());
            G.showToast(this,"签名修改成功");
        }else if(AVATAR.equals(uri)){
            Result<String> result= (Result<String>) date;
//            um.setHoldImgUrl(result.getData());
            //测试数据
            um.setHoldImgUrl("file://"+path);
            //修改头像成功后，设置当前融云的用户头像
            if (RongIM.getInstance() != null) {
                RongIM.getInstance().setCurrentUserInfo(new UserInfo(um.getTsId(), um.getUserName(), Uri.parse(um.getHoldImgUrl())));
                RongIM.getInstance().setMessageAttachedUserInfo(true);
            }
            ImageCache.imageLoader(um.getHoldImgUrl(),cv_head);
            G.showToast(this,"头像修改成功");
        }
    }

    @Override
    public void onError(String uri, String error) {
        G.showToast(this,error);
    }
}
