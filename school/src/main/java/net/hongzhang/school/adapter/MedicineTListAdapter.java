package net.hongzhang.school.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.school.R;
import net.hongzhang.school.activity.MedicineTaskTActivity;
import net.hongzhang.school.bean.MedicineVo;

import java.util.List;

/**
 * ================================================
 * 作    者：wh
 * 时    间：2016/9/19
 * 描    述：请假列表--适配器
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class MedicineTListAdapter extends BaseAdapter {
    private Context context;
    private List<MedicineVo> medicineVos;
    private int status;
    public MedicineTListAdapter(Context context, List<MedicineVo> medicineVos,int status) {
        this.context = context;
        this.medicineVos = medicineVos;
        this.status =status;
    }

    @Override
    public int getCount() {
        return medicineVos.size();
    }

    @Override
    public Object getItem(int i) {
        return medicineVos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHold viewHold;
        if(null==view){
            view= LayoutInflater.from(context).inflate(R.layout.item_medicine_t_list,null);
            new ViewHold(view);
        }
        viewHold = (ViewHold) view.getTag();
        final MedicineVo medicineVo = medicineVos.get(i);
        ImageCache.imageLoader(medicineVo.getImgUrl(),viewHold.civ_image);
        viewHold.tv_tsName.setText(medicineVo.getTs_name());
        if (status==0){
            viewHold.tv_medicine.setBackgroundColor(context.getResources().getColor(R.color.main_green));
            viewHold.tv_medicine.setText("喂药");

        }else {
            viewHold.tv_medicine.setBackgroundColor(context.getResources().getColor(R.color.noread_grey));
            viewHold.tv_medicine.setText("已喂药");
        }
        viewHold.tv_medicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDetail(medicineVo);
            }
        });
        if (medicineVo.getMeal_before_or_after()==1){
            viewHold.tv_medicine_time.setBackgroundResource(R.drawable.launch_before_bg);
            viewHold.tv_medicine_time.setText("饭前");
        }else {
            viewHold.tv_medicine_time.setBackgroundResource(R.drawable.launch_after_bg);
            viewHold.tv_medicine_time.setText("饭后");
        }
        viewHold.tv_medicine_dosage.setText(medicineVo.getMedicine_dosage());
        viewHold.tv_medicine_name.setText(medicineVo.getMedicine_name());
        viewHold.tv_remark.setText(medicineVo.getMedicine_doc());
        return view;
    }


    class ViewHold{
         ImageView civ_image;//头像
         TextView tv_tsName;//名字
         TextView tv_medicine_time;//用餐时间
         TextView tv_medicine;//喂药按钮
         TextView tv_medicine_name;//药物的姓名
         TextView tv_medicine_dosage;//药物的用量
         TextView tv_remark;//备注
        public ViewHold(View view) {
           civ_image = (ImageView)view.findViewById(R.id.civ_image);
            tv_tsName = (TextView) view.findViewById(R.id.tv_tsName);
            tv_medicine= (TextView) view.findViewById(R.id.tv_medicine);
            tv_medicine_time= (TextView) view.findViewById(R.id.tv_medicine_time);
            tv_medicine_name= (TextView) view.findViewById(R.id.tv_medicine_name);
            tv_medicine_dosage= (TextView) view.findViewById(R.id.tv_medicine_dosage);
            tv_remark = (TextView) view.findViewById(R.id.tv_remark);
            view.setTag(this);
        }
     }
    private void getDetail(MedicineVo medicineVo){
        Intent intent = new Intent(context, MedicineTaskTActivity.class);
        intent.putExtra("medicineId",medicineVo.getMedicine_id());
        intent.putExtra("tsId",medicineVo.getTs_id());
        intent.putExtra("isFeed",status);
        context.startActivity(intent);
    }
}
