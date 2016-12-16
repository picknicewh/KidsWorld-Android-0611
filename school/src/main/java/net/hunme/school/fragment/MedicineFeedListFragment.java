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
import net.hunme.school.adapter.MedicineSListAdapter;
import net.hunme.school.bean.MedicineSVos;
import net.hunme.school.bean.MedicineVo;
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
 * 主要接口：获取家长端喂药列表
 */
public class MedicineFeedListFragment extends BaseFragement implements OkHttpListener {
    /**
     * 一页条数
     */
    private static  final  int pageSize= 10;
    /**
     * 显示列表
     */
    private ListView lv_feed_list;
    /**
     * 页码
     */
    private int pageNumber=1;
    /**
     * 喂药列表数据
     */
    public static List<MedicineVo> medicineVos;
    /**
     * 喂药适配器
     */
    public  static    MedicineSListAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed_list_s,null);
        initView(view);
        return view;
    }
   private void initView(View view){
       lv_feed_list = $(view,R.id.lv_feed_list);
       medicineVos = new ArrayList<>();
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
    public void onResume() {
        super.onResume();
        if (medicineVos.size()>0){
            getFeedList();
        }
    }
    @Override
    public void onSuccess(String uri, Object date) {
      if (Apiurl.SCHOOL_MEDICINESLIST.equals(uri)){
          Result<MedicineSVos> data = (Result<MedicineSVos>) date;
          if (data!=null){
              MedicineSVos medicineSVos = data.getData();
              medicineVos  = medicineSVos.getMedicineList();
              adapter = new MedicineSListAdapter(getActivity(),medicineVos);
              lv_feed_list.setAdapter(adapter);
          }
      }
    }

    @Override
    public void onError(String uri, String error) {
        Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
    }
}
