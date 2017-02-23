package net.hongzhang.school.widget;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.baselibrary.widget.BaseConformDialog;
import net.hongzhang.school.R;
import net.hongzhang.school.activity.CourseArrangeActivity;
import net.hongzhang.school.activity.LeaveListActivity;
import net.hongzhang.school.fragment.MedicineFeedListFragment;
import net.hongzhang.school.fragment.MedicineProcessFragment;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者： wh
 * 时间： 2016/9/19
 * 名称：删除课程表 删除请假 操作对话框
 * 版本说明：
 * 附加注释：
 * 主要接口：1.删除课程表
 */
public class DeleteDialog extends BaseConformDialog implements View.OnClickListener, OkHttpListener {
   private Activity context;
    /**
     * 课程id
     */
    private String syllabusId;
    private int flag;
    private int position;
    public DeleteDialog(Activity context, String syllabusId, int flag,int position){
        this.context  = context;
        this.syllabusId = syllabusId;
        this.flag = flag;
        this.position = position;
    }
    public void initView(){
        initView(context,getContent(flag));
    }
    private String getContent(int flag){
        String content=null;
        if (flag ==1){
            content="确定删除这条课程吗？";
        }else if (flag==0){
            content="确定删除这条请假吗？";
        }else if (flag==2){
            content="确定删除这条委托吗？";
        }
        return content;
    }
    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.bt_conform){
            if (flag ==1){
                deleteCourse(syllabusId);
            }else if (flag==0){
                deleteLeaveAsk(syllabusId);
            }else if (flag==2){
                deleteMedicine(syllabusId);
            }
            alertDialog.dismiss();
        }
    }
    @Override
    public void onSuccess(String uri, Object date) {
        Result<String> data = (Result<String>) date;
        if (data!=null){
            String  result = data.getData();
            Toast.makeText(context,result,Toast.LENGTH_SHORT).show();
          if (context instanceof LeaveListActivity){
              LeaveListActivity   activity = (LeaveListActivity) context;
              activity.deleteUpdate(position);
          }else if (context instanceof CourseArrangeActivity){
              CourseArrangeActivity arrangeActivity = (CourseArrangeActivity) context;
              arrangeActivity.updateDelete(position);
          }
          else {
              MedicineFeedListFragment.medicineVos.remove(position);
              MedicineProcessFragment.medicineScheduleVos.remove(position);
              MedicineFeedListFragment.adapter.notifyDataSetChanged();
              MedicineProcessFragment.adapter.notifyDataSetChanged();
          }
        }
        alertDialog.dismiss();
    }
    @Override
    public void onError(String uri, String error) {
        Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
        alertDialog.dismiss();
    }
    /**
     * 删除请假
     * @param  vacationId 请假id
     */
    private void deleteLeaveAsk(String vacationId){
        Map<String,Object> params = new HashMap<>();
        params.put("tsId", UserMessage.getInstance(context).getTsId());
        params.put("vacationId",vacationId);
        Type type = new TypeToken<Result<String>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.SCHOOL_DELETELEAVES,params,this);
    }
    /**
     * 删除课程
     * @param  syllabusId 课程id
     */
    private void deleteCourse(String syllabusId){
        Map<String,Object> params = new HashMap<>();
        params.put("tsId", UserMessage.getInstance(context).getTsId());
        params.put("syllabusId",syllabusId);
        Type type = new TypeToken<Result<String>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.SCHOOL_DETLTESYLLABUSLISTS,params,this);
    }
    /**
     * 删除喂药
     * @param  medicineId 喂药id
     */
    private void deleteMedicine(String medicineId){
        Map<String,Object> params = new HashMap<>();
        params.put("tsId", UserMessage.getInstance(context).getTsId());
        params.put("medicineId",medicineId);
        Type type = new TypeToken<Result<String >>(){}.getType();
        OkHttps.sendPost(type, Apiurl.SCHOOL_MEDICINETDELETE,params,this);
    }
}
