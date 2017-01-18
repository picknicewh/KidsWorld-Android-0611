package net.hunme.school.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hunme.baselibrary.mode.Result;
import net.hunme.baselibrary.network.Apiurl;
import net.hunme.baselibrary.network.OkHttpListener;
import net.hunme.baselibrary.network.OkHttps;
import net.hunme.baselibrary.util.G;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.baselibrary.widget.LoadingDialog;
import net.hunme.school.R;
import net.hunme.school.widget.DatePopWindow;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class MedicineEntrustActivity extends Activity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, OkHttpListener, TextWatcher {
    private ImageView iv_left;
    /**
     * 委托喂药日期
     */
    private static EditText et_alldate;
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
     * beforeOrAfte r1=餐前 2=餐后
     */
    private int beforeOrAfter = 2;
    private RadioGroup rg_meal;
    /**
     * 日期弹窗
     */
    private DatePopWindow datePopWindow;
    /**
     * 加载
     */
    public LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_entrust);
        initview();
    }

    public static EditText getEditText() {
        return et_alldate;
    }

    private void entrustMedicine(int beforeOrAfter) {
        String name = et_medicine_name.getText().toString();
        String dosage = et_medicine_dosage.getText().toString();
        String remark = et_medicine_remark.getText().toString();
        String date = et_alldate.getText().toString();
        if (G.isEmteny(name) || G.isEmteny(dosage) || G.isEmteny(date)) {
            Toast.makeText(this, "必填信息不能为空哦", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("createTime", date);
        params.put("medicineName", name);
        params.put("medicineDosage", dosage);
        params.put("medicineDoc", remark);
        params.put("mealBeforeOrAfter", beforeOrAfter);
        params.put("tsId", UserMessage.getInstance(this).getTsId());
        Type type = new TypeToken<Result<String>>() {
        }.getType();
        OkHttps.sendPost(type, Apiurl.SCHOOL_MEDICINEPUBLISH, params, this);
        showLoadingDialog();
    }

    private void initview() {
        et_alldate = (EditText) findViewById(R.id.et_alldate);
        et_medicine_name = (EditText) findViewById(R.id.et_medicine_name);
        et_medicine_dosage = (EditText) findViewById(R.id.et_medicine_dosage);
        et_medicine_remark = (EditText) findViewById(R.id.et_medicine_remark);
        btn_medicine_submit = (Button) findViewById(R.id.btn_conform);
        iv_left = (ImageView) findViewById(R.id.iv_left);
        et_alldate.setInputType(InputType.TYPE_DATETIME_VARIATION_NORMAL);
        rg_meal = (RadioGroup) findViewById(R.id.rg_meal);
        rg_meal.setOnCheckedChangeListener(this);
        btn_medicine_submit.setOnClickListener(this);
        iv_left.setOnClickListener(this);
        et_alldate.setOnClickListener(this);
        et_medicine_name.addTextChangedListener(this);
        et_medicine_dosage.addTextChangedListener(this);
        et_medicine_remark.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.btn_conform) {
            entrustMedicine(beforeOrAfter);
        } else if (viewId == R.id.iv_left) {
            finish();
        } else if (viewId == R.id.et_alldate) {
            datePopWindow = new DatePopWindow(this);
            datePopWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        if (checkedId == R.id.rb_before) {
            beforeOrAfter = 1;
        } else if (checkedId == R.id.rb_after) {
            beforeOrAfter = 2;
        }
    }

    @Override
    public void onSuccess(String uri, Object date) {
        stopLoadingDialog();
        Result<String> data = (Result<String>) date;
        String result = data.getData();
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onError(String uri, String error) {
        stopLoadingDialog();
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String name = et_medicine_name.getText().toString();
        String dosage = et_medicine_dosage.getText().toString();
        String date = et_alldate.getText().toString();
        if (!G.isEmteny(name) && !G.isEmteny(dosage)  && !G.isEmteny(date)) {
            btn_medicine_submit.setClickable(true);
            btn_medicine_submit.setBackgroundResource(R.drawable.finish_medicine_bg);
            Log.i("ssssss", "=====================全部都有数据");

        } else {
            btn_medicine_submit.setClickable(false);
            btn_medicine_submit.setBackgroundResource(R.drawable.trust_medicine_bg);
            Log.i("ssssss", "=====================部分或者全部没有数据");
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (datePopWindow != null) {
            datePopWindow.unregisteReceiver();
        }
    }

    public void showLoadingDialog() {
        if (dialog == null)
            dialog = new LoadingDialog(this, net.hunme.baselibrary.R.style.LoadingDialogTheme);
        dialog.show();
        dialog.setCancelable(true);
        dialog.setLoadingText("数据加载中...");
    }

    public void stopLoadingDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
