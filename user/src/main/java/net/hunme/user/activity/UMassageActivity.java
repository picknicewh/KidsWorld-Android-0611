package net.hunme.user.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.pizidea.imagepicker.AndroidImagePicker;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.user.R;
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
public class UMassageActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout rl_userMessage;
    private ImageView iv_into;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_massage);
        initView();
    }
    private void initView(){
        rl_userMessage=$(R.id.rl_userMessage);
        iv_into=$(R.id.iv_into);
        rl_userMessage.setOnClickListener(this);
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
            AndroidImagePicker.getInstance().pickAndCrop(UMassageActivity.this, true, 150, new AndroidImagePicker.OnImageCropCompleteListener() {
                @Override
                public void onImageCropComplete(Bitmap bmp, float ratio) {
//                    Log.i(TAG,"=====onImageCropComplete (get bitmap="+bmp.toString());
//                    ivCrop.setVisibility(View.VISIBLE);
                    iv_into.setImageBitmap(bmp);
                }
            });
        }
    }
}
