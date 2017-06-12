package net.hongzhang.school.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.hongzhang.school.R;
import net.hongzhang.school.bean.MyMedicineVo;

import java.util.List;

/**
 * 作者：Restring
 * 时间：2017/4/20
 * 描述：
 * 版本：
 */

public class MedicineListAdapter extends RecyclerView.Adapter<MedicineListAdapter.ViewHolder> {

    public List<MyMedicineVo> medicineVoList;

    public MedicineListAdapter(List<MyMedicineVo> medicineVoList) {
        this.medicineVoList = medicineVoList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_medicined_list_s, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MyMedicineVo medicineVo = medicineVoList.get(position);
        holder.tv_time.setText(medicineVo.getMEDICINE_TIME());
        holder.tv_remark.setText(medicineVo.getREMARK());
        holder.tv_medicine.setText(medicineVo.getDRUG_NAME());
        holder.tv_sick.setText(medicineVo.getSICKEN_NAME());
        holder.tv_medicine_num.setText(medicineVo.getMEDICINE_NUMBER());

    }
    @Override
    public int getItemCount() {
        return medicineVoList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_time;
        TextView tv_remark;
        TextView tv_medicine;
        TextView tv_medicine_num;
        TextView tv_sick;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_sick = (TextView) itemView.findViewById(R.id.tv_sick);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_remark = (TextView) itemView.findViewById(R.id.tv_remark);
            tv_medicine_num = (TextView) itemView.findViewById(R.id.tv_medicine_num);
            tv_medicine = (TextView) itemView.findViewById(R.id.tv_medicine);
            itemView.setTag(this);
        }
    }
}
