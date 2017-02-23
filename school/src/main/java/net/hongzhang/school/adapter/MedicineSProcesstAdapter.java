package net.hongzhang.school.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.school.R;
import net.hongzhang.school.bean.MedicineSchedule;
import net.hongzhang.school.bean.MedicineVo;
import net.hongzhang.school.bean.Schedule;

import java.util.List;

/**
 * ================================================
 * 作    者：wh
 * 时    间：2016/11/1
 * 描    述：列表--适配器
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class MedicineSProcesstAdapter extends BaseAdapter {
    private Context context;

    private List<MedicineSchedule> medicineScheduleVos;
    public MedicineSProcesstAdapter(Context context, List<MedicineSchedule>medicineScheduleVos) {
        this.context = context;
        this.medicineScheduleVos = medicineScheduleVos;

    }

    @Override
    public int getCount() {
        return medicineScheduleVos.size();
    }

    @Override
    public Object getItem(int i) {
        return medicineScheduleVos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHold viewHold;
        if(null==view){
            view= LayoutInflater.from(context).inflate(R.layout.item_medicine_t_process,null);
            new ViewHold(view);
        }
        viewHold = (ViewHold) view.getTag();
        MedicineSchedule medicineSchedule =medicineScheduleVos.get(i);
        MedicineVo medicineVo = medicineSchedule.getMedicine();
        Schedule schedule = medicineSchedule.getSchedule();
        Log.i("nnnnnnnnnnnnnn",medicineVo.getMedicine_name());
        ImageCache.imageLoader(medicineVo.getImgUrl(),viewHold.civ_publish_image);
        viewHold.tv_medicine_dosage.setText(medicineVo.getMedicine_dosage());
        viewHold.tv_medicine_name.setText(medicineVo.getMedicine_name());
        viewHold.tv_medicine_remark.setText(medicineVo.getMedicine_doc());
        ImageCache.imageLoader(UserMessage.getInstance(context).getHoldImgUrl(),viewHold.civ_publish_image);
        ImageCache.imageLoader(schedule.getKnowUrl(),viewHold.civ_known_image);
        ImageCache.imageLoader(schedule.getFinishUrl(),viewHold.civ_feed_image);
        viewHold.v_publish_dos.setBackgroundResource(R.drawable.circle_process);
        if (schedule.getFinishId()==null){
            viewHold.tv_feed_message .setTextColor(context.getResources().getColor(R.color.line_gray));
            viewHold.v_feed_dos.setBackgroundResource(R.drawable.circle_process_next);
        }else {
            viewHold.tv_feed_message .setTextColor(context.getResources().getColor(R.color.main_green));
            viewHold.v_feed_dos.setBackgroundResource(R.drawable.circle_process);
        }
        if (schedule.getKnowId()==null){
            viewHold.tv_known_message .setTextColor(context.getResources().getColor(R.color.line_gray));
            viewHold.v_known_dos.setBackgroundResource(R.drawable.circle_process_next);
        }else {
            viewHold.tv_known_message .setTextColor(context.getResources().getColor(R.color.main_green));
            viewHold.v_known_dos.setBackgroundResource(R.drawable.circle_process);
        }
        viewHold.tv_publish_message .setTextColor(context.getResources().getColor(R.color.main_green));
        viewHold.tv_medicine_date.setText(medicineVo.getCreate_time().substring(0,11));
        return view;
    }
    class ViewHold{
         ImageView civ_publish_image;//发布人的头像
         ImageView civ_known_image;//知晓人的头像
         ImageView civ_feed_image;//喂药人的头像
         View v_publish_dos;//发布人的小圈圈
         View v_known_dos;//知晓人的小圈圈
         View v_feed_dos;//喂药人的小圈圈
         TextView tv_publish_message;//发布人的信息
         TextView tv_known_message;//知晓人的信息
         TextView tv_feed_message;//喂药人的信息
         TextView tv_medicine_date;//发布日期
         TextView tv_medicine_name;//药物的姓名
         TextView tv_medicine_dosage;//药物的用量
         TextView tv_medicine_remark;//备注
        public ViewHold(View view) {
            civ_publish_image = (ImageView)view.findViewById(R.id.civ_publish_image);
            civ_known_image = (ImageView)view.findViewById(R.id.civ_known_image);
            civ_feed_image = (ImageView)view.findViewById(R.id.civ_feed_image);
            v_publish_dos = view.findViewById(R.id.v_publish_dos);
            v_known_dos = view.findViewById(R.id.v_known_dos);
            v_feed_dos = view.findViewById(R.id.v_feed_dos);
            tv_publish_message  =  (TextView) view.findViewById(R.id.tv_publish_message);
            tv_known_message  =  (TextView) view.findViewById(R.id.tv_known_message);
            tv_feed_message  =  (TextView) view.findViewById(R.id.tv_feed_message);
            tv_medicine_date= (TextView) view.findViewById(R.id.tv_medicine_date);
            tv_medicine_name= (TextView) view.findViewById(R.id.tv_medicine_name);
            tv_medicine_dosage= (TextView) view.findViewById(R.id.tv_medicine_dosage);
            tv_medicine_remark = (TextView) view.findViewById(R.id.tv_medicine_remark);
            view.setTag(this);
        }
     }
}
