package net.hongzhang.user.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.pizidea.imagepicker.AndroidImagePicker;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.contract.ContractsDb;
import net.hongzhang.baselibrary.contract.ContractsDbHelper;
import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.PermissionsChecker;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.baselibrary.widget.CircleImageView;
import net.hongzhang.baselibrary.widget.MyAlertDialog;
import net.hongzhang.user.R;
import net.hongzhang.user.util.BitmapCache;
import net.hongzhang.user.widget.SignDialog;

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
    public static TextView tv_sign;
    private TextView tv_classname;
    private TextView tv_schoolname;
    private UserMessage um;
    private String path;//选择头像保存地址
    private String mpath; //头像绝对地址
    private String sign; //用户个性签名
    private boolean isVisit;
    // 访问相册所需的全部权限
    private final String[] PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE, //读写权限
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    /**
     * 标记位，一定要等待修改头像成功后才能返回
     */
    private int flag=1;
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
   public  static  TextView getTv_sign(){
       return tv_sign;
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
        setLiftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag==1){
                    finish();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int viewID=v.getId();
        if(viewID==R.id.rl_userMessage){
            PermissionsChecker checker=PermissionsChecker.getInstance(this);
            if(checker.lacksPermissions(PERMISSIONS)){
                checker.getPerMissions(PERMISSIONS);
                return;
            }
            AndroidImagePicker.getInstance().pickAndCrop(UMassageActivity.this, true, 200, new AndroidImagePicker.OnImageCropCompleteListener() {
                @Override
                public void onImageCropComplete(Bitmap bmp, float ratio) {
//                    Log.i(TAG,"=====onImageCropComplete (get bitmap="+bmp.toString());
//                    ivCrop.setVisibility(View.VISIBLE);
                    mpath=path+ new Date().getTime()+".jpg";
                    BitmapCache.compressBiamp(bmp,mpath,100);//压缩图片到该路径 path
                    List<File> files=new ArrayList<>();
                    files.add(new File(mpath));//从该路径拿到图片
                    userAvatarSubmit(files);

                }
            });
        }else if(viewID==R.id.ll_sex){
//            showSexDialog();
        }else if(viewID==R.id.ll_sign){
         //   showSignDialog();
            SignDialog dialog = new SignDialog(this);
            dialog.initView();
        }
    }

    /**
     * 性别选择框
     */
    public void showSexDialog(){
        View view= LayoutInflater.from(this).inflate(R.layout.alertdialog_select_sex,null);
        final AlertDialog alertDialog= MyAlertDialog.getDialog(view,this,1);
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

    private  void  userAvatarSubmit(List<File>list){
        if(isVisit)return;
        isVisit=true;
        Map<String,Object>map=new HashMap<>();
        map.put("tsId",um.getTsId());
        Type type=new TypeToken<Result<String>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.AVATAR,map,list,this);
        flag = 0;
        G.log("=============");
    }

    @Override
    public void onSuccess(String uri, Object date) {
      if(Apiurl.AVATAR.equals(uri)){
            isVisit=false;
            Result<String> result= (Result<String>) date;
            //测试数据
            um.setHoldImgUrl(result.getData());
            //修改头像成功后，设置当前融云的用户头像
            if (RongIM.getInstance() != null) {
                RongIM.getInstance().setCurrentUserInfo(new UserInfo(um.getTsId(), um.getUserName(), Uri.parse(result.getData())));
                RongIM.getInstance().setMessageAttachedUserInfo(true);
            }
            ImageCache.imageLoader(um.getHoldImgUrl(),cv_head);
           //修改头像后，修改数据库中的联系人的头像
            SQLiteDatabase db= new ContractsDb(this).getWritableDatabase();
            ContractsDbHelper helper = ContractsDbHelper.getinstance();
            helper.updateImageUrl(db,um.getTsId(),result.getData());
            G.KisTyep.isUpadteHold=true;
            G.showToast(this,"头像修改成功");
            flag = 1;
        }
    }

    @Override
    public void onError(String uri, String error) {
        G.showToast(this,error);
        flag = 0;
    }
}
