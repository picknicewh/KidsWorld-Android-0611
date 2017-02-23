package net.hongzhang.school.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.baselibrary.widget.CircleImageView;
import net.hongzhang.school.R;
import net.hongzhang.school.util.MedicineTDetail;
import net.hongzhang.school.util.MedicineTDetailPresenter;

public class MedicineTaskTActivity extends BaseActivity implements MedicineTDetail.View,View.OnClickListener{
    /**
     * 显示类
     */
    private MedicineTDetailPresenter presenter;
    /**
     * 喂药的id
     */
    private String medicineId;
    /**
     * 委托喂药的人的tsId
     */
    private String tsId;
    /**
     * 是否已经喂药
     */
    private int isFeed;
    /**
     * 头像
     */
    private CircleImageView iv_image;
    /**
     * 委托喂药的姓名
     */
    private TextView tv_Tsname;
    /**
     * 药物的名称
     */
    private TextView tv_medicine_name;
    /**
     * 喂药的日期
     */
    private TextView tv_medicine_date;
    /**
     * 喂药的日期
     */
    private TextView tv_medicine_dos;
    /**
     * 喂药的备注
     */
    private TextView tv_remark;
    /**
     *喂药按钮
     */
     private Button btn_feed;

    /**
     *饭前
     */
    private RadioButton rb_before;
    /**
     *饭后
     */
    private RadioButton rb_after;
    private LinearLayout ll_anchor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_task_t);
        initView();
        presenter = new MedicineTDetailPresenter(this,this,medicineId,tsId,isFeed);
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setCententTitle("喂药");
        setLiftOnClickClose();
    }
    private void initView(){
        iv_image =  $(R.id.civ_image);
        tv_Tsname  = $(R.id.tv_tsName);
        tv_medicine_date=$(R.id.tv_alldate);
        tv_medicine_name = $(R.id.tv_medicine_name);
        tv_medicine_dos = $(R.id.tv_medicine_dosage);
        tv_remark = $(R.id.tv_remark);
        btn_feed = $(R.id.btn_finish);
        rb_after = $(R.id.rb_after);
        rb_before = $(R.id.rb_before);
        ll_anchor =  $(R.id.ll_anchor);
        Intent intent = getIntent();
        medicineId = intent.getStringExtra("medicineId");
        tsId = intent.getStringExtra("tsId");
        isFeed = intent.getIntExtra("isFeed",-1);
        if (isFeed==-1){
            btn_feed.setVisibility(View.GONE);
            ll_anchor.setVisibility(View.GONE);
        }
    }
    @Override
    public void setHeadImageView(String url) {
      if (url!=null){
          ImageCache.imageLoader(url,iv_image);
      }
    }

    @Override
    public void setTsName(String name) {
      if (name!=null){
          tv_Tsname.setText(name);
      }
    }

    @Override
    public void setFeedDate(String date) {
    if (date!=null){
        tv_medicine_date.setText(date);
    }
    }

    @Override
    public void setMedicineName(String medicineName) {
       if (medicineName!=null){
           tv_medicine_name.setText(medicineName);
       }
    }
    @Override
    public void setMedicineDosage(String medicineDosage) {
      if (medicineDosage!=null){
          tv_medicine_dos.setText(medicineDosage);
      }
    }

    @Override
    public void setLaunchTime(int LaunchTime) {
       if (LaunchTime==1){
           rb_before.setChecked(true);
       }else {

           rb_after.setChecked(true);
       }
    }
    @Override
    public void setRemark(String remark) {
     if (remark!=null){
         tv_remark.setText(remark);
     }
    }

    @Override
    public void setIsFeed(int feed) {
     if (feed==1){
         btn_feed.setText("已喂药");
         btn_feed.setBackgroundColor(getResources().getColor(R.color.noread_grey));
         btn_feed.setClickable(false);
     }else {
         btn_feed.setText("完成" );
         btn_feed.setBackgroundColor(getResources().getColor(R.color.main_green));
         btn_feed.setClickable(true);
         btn_feed.setOnClickListener(this);
     }
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.btn_finish){
            presenter.finishFeedMedicine(UserMessage.getInstance(this).getTsId(),medicineId);
        }
    }

}
