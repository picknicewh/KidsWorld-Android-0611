package net.hongzhang.school.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;

import net.hongzhang.baselibrary.mode.MapVo;
import net.hongzhang.baselibrary.util.DateUtil;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.school.R;
import net.hongzhang.school.adapter.SubmitDateAdapter;
import net.hongzhang.school.presenter.SubmitMedicineContract;
import net.hongzhang.school.presenter.SubmitMedicinePresenter;
import net.hongzhang.school.widget.AddTypeDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： wanghua
 * 时间： 2017/6/6
 * 名称：家长提交喂药
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class SubmitMedicineActivity extends Activity implements View.OnClickListener, SubmitMedicineContract.View {
    private RelativeLayout rl_sick_type;
    private TextView tv_sick_type;
    private String sickType;
    private RelativeLayout rl_medicine;
    /**
     * 药品控件
     */
    private TextView tv_medicine;
    /**
     * 药品名称
     */
    private String drugType;
    /**
     * 添加日期按钮
     */
    private ImageView iv_date;
    /**
     * 日期列表
     */
    private RecyclerView rv_date;
    /**
     * 日期列表数据
     */
    private List<String> dates;
    private RelativeLayout rl_days;
    /**
     * 编辑药品天数
     */
    private EditText et_days;
    /**
     * 药品天数
     */
    private String days;
    private RelativeLayout rl_number;
    /**
     * 药品数量
     */
    private EditText et_number;
    private String number;
    private LinearLayout ll_message;
    /**
     * 备注
     */
    private EditText et_message;
    /**
     * 病名
     */
    private TextView tv_addSickType;
    /**
     * 药品名
     */
    private TextView tv_addMedicineType;
    /**
     * 信息类
     */
    private String message;
    /**
     * 数据处理类
     */
    private SubmitMedicinePresenter presenter;
    /**
     * 所有的病名列表
     */
    private List<MapVo> sickenType;
    /**
     * 所有药名的列表
     */
    private List<MapVo> drigType;
    private UserMessage userMessage;
    /**
     * 提交日期适配器
     */
    private SubmitDateAdapter adapter;
    /**
     * 添加药品名称
     */
    private AddTypeDialog drigTypeDialog;
    /**
     * 添加病名
     */
    private AddTypeDialog sickenTypeDialog;
    private ImageView iv_left;
    private TextView tv_subtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_medicine);
        initView();
    }

    private void initView() {
        iv_left = (ImageView) findViewById(R.id.iv_left);
        tv_subtitle = (TextView) findViewById(R.id.tv_subtitle);
        rl_sick_type = (RelativeLayout) findViewById(R.id.rl_sick_type);
        tv_sick_type = (TextView) findViewById(R.id.tv_sick_type);
        rl_medicine = (RelativeLayout) findViewById(R.id.rl_medicine);
        tv_medicine = (TextView) findViewById(R.id.tv_medicine);
        tv_addSickType = (TextView) findViewById(R.id.tv_addSick_type);
        tv_addMedicineType = (TextView) findViewById(R.id.tv_addMedicine_type);
        iv_date = (ImageView) findViewById(R.id.iv_date);
        rv_date = (RecyclerView) findViewById(R.id.rv_date);
        rl_days = (RelativeLayout) findViewById(R.id.rl_days);
        et_days = (EditText) findViewById(R.id.et_days);
        rl_number = (RelativeLayout) findViewById(R.id.rl_number);
        et_number = (EditText) findViewById(R.id.et_number);
        ll_message = (LinearLayout) findViewById(R.id.ll_message);
        et_message = (EditText) findViewById(R.id.et_message);
        tv_sick_type.setOnClickListener(this);
        tv_addMedicineType.setOnClickListener(this);
        tv_addSickType.setOnClickListener(this);
        rl_number.setOnClickListener(this);
        rl_days.setOnClickListener(this);
        ll_message.setOnClickListener(this);
        rl_medicine.setOnClickListener(this);
        iv_date.setOnClickListener(this);
        rl_sick_type.setOnClickListener(this);
        iv_left.setOnClickListener(this);
        tv_subtitle.setOnClickListener(this);
        initData();
    }

    private void initData() {
        userMessage = new UserMessage(this);
        dates = new ArrayList<>();
        adapter = new SubmitDateAdapter(dates);
        presenter = new SubmitMedicinePresenter(SubmitMedicineActivity.this, this);
        sickenTypeDialog = new AddTypeDialog(this, "请输入患病类型", presenter);
        drigTypeDialog = new AddTypeDialog(this, "请输入药品类型", presenter);
        rv_date.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv_date.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.rl_sick_type || viewId == R.id.tv_sick_type) {
            presenter.optionsPickerView(this, "请选择患病类型", new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    MapVo mapVo = sickenType.get(options1);
                    tv_sick_type.setText(mapVo.getValue());
                    sickType = mapVo.getKey();
                }
            }, sickenType).show();
        } else if (viewId == R.id.rl_medicine || viewId == R.id.tv_medicine) {
            presenter.optionsPickerView(this, "请选择药品类型", new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    MapVo mapVo = drigType.get(options1);
                    tv_medicine.setText(mapVo.getValue());
                    drugType = mapVo.getKey();
                }
            }, drigType).show();
        } else if (viewId == R.id.iv_date) {
            if (dates.size() < 4) {
                presenter.getoptionsPickerTime(this, new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        String date = DateUtil.getDayList().get(options1) + ":" + DateUtil.getWholeTime().get(options2);
                        dates.add(date);
                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        }
                    }
                }).show();
            }else {
                G.showToast(this,"一天最多选择三个时间段哦！");
            }
        } else if (viewId == R.id.tv_subtitle) {
            number = et_number.getText().toString();
            days = et_days.getText().toString();
            message = et_message.getText().toString();
            presenter.submitMedicine(userMessage.getTsId(), sickType, drugType, dates, number, message, days);
        } else if (viewId == R.id.tv_addMedicine_type) {
            drigTypeDialog.initView();
        } else if (viewId == R.id.tv_addSick_type) {
            sickenTypeDialog.initView();
        } else if (viewId == R.id.iv_left) {
            finish();
        }
    }

    @Override
    public void setMedicineText(String text, String drugType) {
        tv_medicine.setText(text);
        this.drugType = drugType;
    }

    @Override
    public void setSickText(String text, String sick_type) {
        tv_sick_type.setText(text);
        this.sickType = sick_type;
    }

    @Override
    public void showLoadingDialog() {

    }

    @Override
    public void stopLoadingDialog() {

    }

    @Override
    public void setSickenType(List<MapVo> sickenType) {
        this.sickenType = sickenType;
    }

    @Override
    public void setDrigType(List<MapVo> drigType) {
        this.drigType = drigType;
    }

}
