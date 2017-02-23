package net.hongzhang.school.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import net.hongzhang.baselibrary.base.BaseFragement;
import net.hongzhang.school.R;
import net.hongzhang.school.activity.MedicineTaskTActivity;
import net.hongzhang.school.adapter.MedicineSListAdapter;
import net.hongzhang.school.bean.MedicineVo;

import java.util.ArrayList;
import java.util.List;


/**
 * 作者： wh
 * 时间： 2016/10/10
 * 名称：学生端喂药列表类
 * 版本说明：
 * 附加注释：
 * 主要接口：获取家长端喂药列表
 */
public class MedicineFeedListFragment extends BaseFragement {
    /**
     * 显示列表
     */
    private ListView lv_feed_list;
    /**
     * 喂药列表数据
     */
    public static List<MedicineVo> medicineVos;
    /**
     * 喂药适配器
     */
    public static MedicineSListAdapter adapter;
    /**
     * 没有数据
     */
    private TextView tv_nodata;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed_list_s, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        lv_feed_list = $(view, R.id.lv_feed_list);
        tv_nodata = $(view, R.id.tv_nodata);
        medicineVos = new ArrayList<>();
        setListView();
    }

    private void setListView() {
        adapter = new MedicineSListAdapter(getActivity(), medicineVos);
        lv_feed_list.setAdapter(adapter);
        lv_feed_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), MedicineTaskTActivity.class);
                 MedicineVo medicineVo = medicineVos.get(i);
                intent.putExtra("medicineId",medicineVo.getMedicine_id());
                intent.putExtra("tsId",medicineVo.getTs_id());
                intent.putExtra("isFeed",-1);
                startActivity(intent);
            }
        });
    }

    public void setMedicineVo(List<MedicineVo> medicineList) {
        if (medicineVos != null) {
            medicineVos.clear();
            medicineVos.addAll(medicineList);
            adapter.notifyDataSetChanged();
            if (medicineVos.size() == 0) {
                tv_nodata.setVisibility(View.VISIBLE);
            } else {
                tv_nodata.setVisibility(View.GONE);
            }
            Log.i("nnnnnnnn", "=============3333333=================" + medicineVos.size());
        }
    }

}
