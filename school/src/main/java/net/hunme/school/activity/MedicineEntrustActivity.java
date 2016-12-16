package net.hunme.school.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.baselibrary.mode.Result;
import net.hunme.baselibrary.network.Apiurl;
import net.hunme.baselibrary.network.OkHttpListener;
import net.hunme.baselibrary.network.OkHttps;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.school.R;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class MedicineEntrustActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, OkHttpListener, TextWatcher {
    /**
     * 委托喂药日期
     */
    private EditText et_alldate;
    /**
     * 喂药的名称
     */
    private EditText et_medicine_name;
    /**
     * 喂药量
     */
    private EditText et_medicine_dosage;
    /**
     * 喂药说明
     */
    private EditText et_medicine_remark;
    /**
     * 提交喂药
     */
    private Button btn_medicine_submit;
    /**
     * 饭前喂药
     */
    private RadioButton rb_before_launch;
    /**
     * 饭后喂药
     */
    private RadioButton rb_after_launch;
    /**
     * beforeOrAfte r1=餐前 2=餐后
     */
    private int beforeOrAfter=2;
    private RadioGroup rg_meal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_entrust);
        initview();
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        setCententTitle("委托喂药");
    }
    private void entrustMedicine(int beforeOrAfter){
        String name = et_medicine_name.getText().toString();
        String dosage = et_medicine_dosage.getText().toString();
        String remark = et_medicine_remark.getText().toString();
        String date =et_alldate.getText().toString();
        if (!check()){
            Toast.makeText(this,"必填信息不能为空哦",Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String,Object> params = new HashMap<>();
        params.put("createTime",date);
        params.put("medicineName",name);
        params.put("medicineDosage",dosage);
        params.put("medicineDoc",remark);
        params.put("mealBeforeOrAfter",beforeOrAfter);
        params.put("tsId", UserMessage.getInstance(this).getTsId());
        Type type = new TypeToken<Result<String>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.SCHOOL_MEDICINEPUBLISH,params,this);
        showLoadingDialog();
    }
    private void initview(){
        et_alldate = $(R.id.et_alldate);
        et_medicine_name = $(R.id.et_medicine_name);
        et_medicine_dosage = $(R.id.et_medicine_dosage);
        et_medicine_remark = $(R.id.et_medicine_remark);
        btn_medicine_submit = $(R.id.btn_conform);
        rb_before_launch = $(R.id.rb_before);
        rb_after_launch = $(R.id.rb_after);
        rg_meal=  $(R.id.rg_meal);
        rg_meal.setOnCheckedChangeListener(this);
        btn_medicine_submit.setOnClickListener(this);
        et_alldate.addTextChangedListener(this);
        et_medicine_name.addTextChangedListener(this);
        et_medicine_dosage.addTextChangedListener(this);
        et_medicine_remark.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId==R.id.btn_conform){
          entrustMedicine(beforeOrAfter);
        }
    }
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        if (checkedId==R.id.rb_before){
         //   rb_before_launch.setChecked(true);
            beforeOrAfter = 1;
        }else if (checkedId==R.id.rb_after){
           beforeOrAfter  =2;
        }
    }


    private boolean check(){
        String name = et_medicine_name.getText().toString();
        String dosage = et_medicine_dosage.getText().toString();
        String remark = et_medicine_remark.getText().toString();
        String date =et_alldate.getText().toString();
        if (name==null||dosage==null||remark==null ||date ==null){
            btn_medicine_submit.setClickable(false);
            btn_medicine_submit.setBackgroundResource(R.drawable.trust_medicine_bg);
            return false;
        }else {
            btn_medicine_submit.setClickable(true);
            btn_medicine_submit.setBackgroundResource(R.drawable.finish_medicine_bg);
            return true;
        }
    }

    @Override
    public void onSuccess(String uri, Object date) {
        stopLoadingDialog();
        Result<String> data = (Result<String>) date;
        String result = data.getData();
        Toast.makeText(this,result,Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onError(String uri, String error) {
        stopLoadingDialog();
        Toast.makeText(this,error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        check();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
