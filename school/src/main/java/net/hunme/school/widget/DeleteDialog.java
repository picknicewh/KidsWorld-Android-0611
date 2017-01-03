package net.hunme.school.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hunme.baselibrary.mode.Result;
import net.hunme.baselibrary.network.Apiurl;
import net.hunme.baselibrary.network.OkHttpListener;
import net.hunme.baselibrary.network.OkHttps;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.baselibrary.widget.BaseConformDialog;
import net.hunme.baselibrary.widget.MyAlertDialog;
import net.hunme.school.R;
import net.hunme.school.activity.CourseArrangeActivity;
import net.hunme.school.activity.LeaveListActivity;
import net.hunme.school.fragment.MedicineFeedListFragment;
import net.hunme.user.activity.MyDynamicActivity;

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
            //  activity.leaveVos.remove(position);
             // activity.adapter.notifyDataSetChanged();
              activity.deleteUpdate(position);
          }else if (context instanceof CourseArrangeActivity){
              CourseArrangeActivity arrangeActivity = (CourseArrangeActivity) context;
             // arrangeActivity.syllabusVoList.remove(position);
            //  arrangeActivity.adapter.notifyDataSetChanged();
              arrangeActivity.updateDelete(position);
          }
          else {
              MedicineFeedListFragment.medicineVos.remove(position);
              MedicineFeedListFragment.adapter.notifyDataSetChanged();

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
    private void deleteMedicine(String medicineId){
        Map<String,Object> params = new HashMap<>();
        params.put("tsId", UserMessage.getInstance(context).getTsId());
        params.put("medicineId",medicineId);
        Type type = new TypeToken<Result<String >>(){}.getType();
        OkHttps.sendPost(type, Apiurl.SCHOOL_MEDICINETDELETE,params,this);
    }

}
