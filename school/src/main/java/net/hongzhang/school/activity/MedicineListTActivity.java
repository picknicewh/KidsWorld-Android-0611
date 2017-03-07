package net.hongzhang.school.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.baselibrary.widget.NoScrollListView;
import net.hongzhang.school.R;
import net.hongzhang.school.adapter.MedicineTListAdapter;
import net.hongzhang.school.bean.MedicineTVos;
import net.hongzhang.school.bean.MedicineVo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者： wh
 * 时间： 2016/10/31
 * 名称：老师喂药界面
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class MedicineListTActivity extends BaseActivity implements OkHttpListener, AdapterView.OnItemClickListener {
    private static final int FINISH = 1;
    private static final int UNFINISH = 0;
    /**
     * 需要喂药的列表
     */
    private NoScrollListView lv_need;
    /**
     * 已经完成喂药
     */
    private NoScrollListView lv_finish;
    /**
     * 完成喂药适配器
     */
    private MedicineTListAdapter finishAdpter;
    /**
     * 需要喂药适配器
     */
    private MedicineTListAdapter needAdpter;
    /**
     * 完成列表数据
     */
    private List<MedicineVo> finishmedicineVos;
    /**
     * 需要喂药数据
     */
    private List<MedicineVo> needmedicineVos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_t_list);
        initView();
    }

    private void initView() {
        lv_finish = $(R.id.lv_finish);
        lv_need = $(R.id.lv_need);
        finishmedicineVos = new ArrayList<>();
        needmedicineVos = new ArrayList<>();
        getMedicineList();
        lv_finish.setOnItemClickListener(this);
        lv_need.setOnItemClickListener(this);

    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setCententTitle("今日喂药");
        setLiftOnClickClose();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (finishmedicineVos != null && needmedicineVos != null) {
            getMedicineList();
        }
    }

    private void getMedicineList() {
        Map<String, Object> params = new HashMap<>();
        params.put("tsId", UserMessage.getInstance(this).getTsId());
        Type type = new TypeToken<Result<MedicineTVos>>() {
        }.getType();
        OkHttps.sendPost(type, Apiurl.SCHOOL_MEDICINETLIST, params, this);
    }

    @Override
    public void onSuccess(String uri, Object date) {
        if (Apiurl.SCHOOL_MEDICINETLIST.equals(uri)) {
            if (date != null) {
                Result<MedicineTVos> data = (Result<MedicineTVos>) date;
                MedicineTVos medicineTVos = data.getData();
                needmedicineVos = medicineTVos.getNotMedicine();
                needAdpter = new MedicineTListAdapter(this, needmedicineVos, UNFINISH);
                lv_need.setAdapter(needAdpter);
                finishmedicineVos = medicineTVos.getFinishMedicine();
                finishAdpter = new MedicineTListAdapter(this, finishmedicineVos, FINISH);
                lv_finish.setAdapter(finishAdpter);
            }
        }
    }

    @Override
    public void onError(String uri, String error) {
        Toast.makeText(this, uri, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        int viewId = adapterView.getId();
        if (viewId == R.id.lv_need) {
            getDetail(needmedicineVos.get(i), UNFINISH);
        } else if (viewId == R.id.lv_finish) {
            getDetail(finishmedicineVos.get(i), FINISH);
        }
    }

    private void getDetail(MedicineVo medicineVo, int status) {
        Intent intent = new Intent(this, MedicineTaskTActivity.class);
        intent.putExtra("medicineId", medicineVo.getMedicine_id());
        intent.putExtra("tsId", medicineVo.getTs_id());
        intent.putExtra("isFeed", status);
        startActivity(intent);
    }
}
