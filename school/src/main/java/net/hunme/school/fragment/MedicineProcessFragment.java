package net.hunme.school.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hunme.baselibrary.base.BaseFragement;
import net.hunme.baselibrary.mode.Result;
import net.hunme.baselibrary.network.Apiurl;
import net.hunme.baselibrary.network.OkHttpListener;
import net.hunme.baselibrary.network.OkHttps;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.school.R;
import net.hunme.school.adapter.MedicineSProcesstAdapter;
import net.hunme.school.bean.MedicineSVos;
import net.hunme.school.bean.MedicineSchedule;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 作者： wh
 * 时间： 2016/10/10
 * 名称：学生端喂药列表类
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class MedicineProcessFragment extends BaseFragement implements OkHttpListener {
    /**
     * 一页条数
     */
    private static  final  int pageSize= 10;
    /**
     * 页码
     */
    private int pageNumber=1;
   private ListView lv_feed_process;
    /**
     *今日喂药流程反馈
     */
    private   List<MedicineSchedule> medicineScheduleVos;
    /**
     * 喂药适配器
     */
    private MedicineSProcesstAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed_process_s,null);
        initView(view);
        return view;
    }
    private void initView(View view){
        lv_feed_process = $(view,R.id.lv_feed_procress);
        medicineScheduleVos = new ArrayList<>();
        getFeedList();
    }
    private void getFeedList(){
        Map<String,Object> map=new HashMap<>();
        map.put("tsId", UserMessage.getInstance(getActivity()).getTsId());
        map.put("pageNumber",pageNumber);
        map.put("pageSize",pageSize);
        Type type=new TypeToken<Result<MedicineSVos>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.SCHOOL_MEDICINESLIST,map,this);
    }

    @Override
    public void onSuccess(String uri, Object date) {
        if (Apiurl.SCHOOL_MEDICINESLIST.equals(uri)){
            Result<MedicineSVos> data = (Result<MedicineSVos>) date;
            if (data!=null){
                MedicineSVos medicineSVos = data.getData();
                medicineScheduleVos=medicineSVos.getMedicineScheduleJson();
                adapter = new MedicineSProcesstAdapter(getActivity(),medicineScheduleVos);
                lv_feed_process.setAdapter(adapter);
            }
        }
    }

    @Override
    public void onError(String uri, String error) {
        Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
    }
}
