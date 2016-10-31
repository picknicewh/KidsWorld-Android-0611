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
import net.hunme.baselibrary.widget.MyAlertDialog;
import net.hunme.school.R;
import net.hunme.school.activity.CourseArrangeActivity;
import net.hunme.school.activity.LeaveListActivity;

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
public class DeleteDialog implements View.OnClickListener, OkHttpListener {
   private Activity context;

    /**
     * 确定
     */
    private Button bt_conform;
    /**
     * 取消
     */
    private Button bt_cancel;
    /**
     * 对话框
     */
    private AlertDialog alertDialog;
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

    /**
     * 初始化view
     */
    public void  initView(){
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_delete_course, null);
        alertDialog = MyAlertDialog.getDialog(contentView, context,1);
        bt_conform = (Button) contentView.findViewById(R.id.bt_conform);
         bt_cancel = (Button) contentView.findViewById(R.id.bt_cancel);
        TextView content = (TextView) contentView.findViewById(R.id.tv_message);
        if (flag ==1){
            content.setText("确定删除这条课程吗？");
        }else {
            content.setText("确定删除这条请假吗？");
        }
        bt_cancel.setOnClickListener(this);
         bt_conform.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.bt_conform){
            if (flag ==1){
                deleteCourse(syllabusId);
            }else {
                deleteLeaveAsk(syllabusId);
            }
            alertDialog.dismiss();
        }else if (view.getId()==R.id.bt_cancel){
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
              activity.leaveVos.remove(position);
              activity.adapter.notifyDataSetChanged();
          }else if (context instanceof CourseArrangeActivity){
              CourseArrangeActivity arrangeActivity = (CourseArrangeActivity) context;
              arrangeActivity.syllabusVoList.remove(position);
              arrangeActivity.adapter.notifyDataSetChanged();
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
}
