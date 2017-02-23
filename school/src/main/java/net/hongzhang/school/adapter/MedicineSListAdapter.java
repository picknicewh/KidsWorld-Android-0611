package net.hongzhang.school.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.hongzhang.school.R;
import net.hongzhang.school.bean.MedicineVo;
import net.hongzhang.school.widget.DeleteDialog;

import java.util.List;

/**
 * ================================================
 * 作    者：wh
 * 时    间：2016/9/19
 * 描    述：学生喂药列表--适配器
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class MedicineSListAdapter extends BaseAdapter {
    private Activity context;
    private List<MedicineVo> medicineVos;
    public MedicineSListAdapter(Activity context, List<MedicineVo> medicineVos) {
        this.context = context;
        this.medicineVos=medicineVos;
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
            view= LayoutInflater.from(context).inflate(R.layout.item_medicine_list_s,null);
            new ViewHold(view);
        }
        viewHold = (ViewHold) view.getTag();
        final MedicineVo medicineVo = medicineVos.get(i);
        viewHold.tv_medicine_date.setText(medicineVo.getCreate_time().substring(0,11));
        viewHold.tv_medicine_dosage.setText(medicineVo.getMedicine_dosage());
        viewHold.tv_medicine_name.setText(medicineVo.getMedicine_name());
        viewHold.tv_medicine_remark.setText(medicineVo.getMedicine_doc());
        viewHold.tv_medicine_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteDialog deleteDialog = new DeleteDialog(context,medicineVo.getMedicine_id(),2,i);
                deleteDialog.initView();

            }
        });
        return view;
    }

    class ViewHold{
         TextView tv_medicine_date;//用餐时间
         TextView tv_medicine_name;//药物的姓名
         TextView tv_medicine_dosage;//药物的用量
         TextView tv_medicine_remark;//备注
         TextView tv_medicine_delete;//删除
        public ViewHold(View view) {
            tv_medicine_date= (TextView) view.findViewById(R.id.tv_medicine_date);
            tv_medicine_delete= (TextView) view.findViewById(R.id.tv_medicine_delete);
            tv_medicine_name= (TextView) view.findViewById(R.id.tv_medicine_name);
            tv_medicine_dosage= (TextView) view.findViewById(R.id.tv_medicine_dosage);
            tv_medicine_remark = (TextView) view.findViewById(R.id.tv_medicine_remark);
            view.setTag(this);
        }
     }
}
