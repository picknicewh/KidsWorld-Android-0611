package net.hongzhang.school.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import net.hongzhang.baselibrary.base.BaseFragement;
import net.hongzhang.school.R;
import net.hongzhang.school.adapter.MedicineSProcesstAdapter;
import net.hongzhang.school.bean.MedicineSchedule;

import java.util.ArrayList;
import java.util.List;


/**
 * 作者： wh
 * 时间： 2016/10/10
 * 名称：学生端喂药列表类
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class MedicineProcessFragment extends BaseFragement{
   private ListView lv_feed_process;
    /**
     *今日喂药流程反馈
     */
    public  static List<MedicineSchedule> medicineScheduleVos;
    /**
     * 喂药适配器
     */
    public  static MedicineSProcesstAdapter adapter;
    /**
     * 没有数据
     */
    private TextView tv_nodata;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed_process_s,null);
        initView(view);
        return view;
    }
    private void initView(View view){
        lv_feed_process = $(view,R.id.lv_feed_procress);
        tv_nodata = $(view,R.id.tv_nodata);
        medicineScheduleVos = new ArrayList<>();
        setListView();
    }
    private void setListView(){
        adapter = new MedicineSProcesstAdapter(getActivity(),medicineScheduleVos);
        lv_feed_process.setAdapter(adapter);
    }
   public void setMedicineScheduleVos( List<MedicineSchedule> medicineScheduleList){
       if (medicineScheduleVos != null) {
           medicineScheduleVos.clear();
           medicineScheduleVos.addAll(medicineScheduleList);
           adapter.notifyDataSetChanged();
           if (medicineScheduleVos.size()==0){
               tv_nodata.setVisibility(View.VISIBLE);
           }else {
               tv_nodata.setVisibility(View.GONE);
           }
           Log.i("nnnnnnnn", "=============3333333=================" + medicineScheduleVos.size());
       }
   }

}
