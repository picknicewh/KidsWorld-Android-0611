package net.hongzhang.school.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import net.hongzhang.school.R;
import net.hongzhang.school.bean.MulDateVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者： wanghua
 * 时间： 2017/3/31
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class MouthDateAdapter extends RecyclerView.Adapter<MouthDateAdapter.ViewHolder> {
    /**
     * 当月日期的颜色
     */
    private final static int CURRENT_MOUTH_COLOR = Color.parseColor("#494949");
    /**
     * 其他月的颜色
     */
    private final static int OTHER_MOUTH_COLOR = Color.parseColor("#a0a0a0");
    /**
     * 其他月的其他月的颜色
     */
    private final static int OTHER_MOUTH_OTHER_COLOR = Color.parseColor("#e1e1e1");
    private Context context;
    private List<MulDateVo> dateList;
    private MouthDateAdapter.onItemClickListener itemClickListener = null;
    public MouthDateAdapter(Context context, List<MulDateVo> dateList) {
        this.context =context;
        this.dateList = dateList;
        initData();
    }
    @Override
    public MouthDateAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                context).inflate(R.layout.item_date, parent, false);
        MouthDateAdapter.ViewHolder holder = new MouthDateAdapter.ViewHolder(view);
        return holder;

    }
    public static Map<Integer,Boolean>  map = new HashMap<>();
    private void initData(){
        for (int i=0;i<dateList.size();i++){
            if (dateList.get(i).getSign()==2){
                map.put(i,true);
            }else {
                map.put(i,false);
            }

        }
    }
    private boolean isclick=true;
    private int count;
    @Override
    public void onBindViewHolder(final MouthDateAdapter.ViewHolder holder, final int position) {
        MulDateVo dateVo = dateList.get(position);
        final int sign = dateVo.getSign();
        holder.tv_week.setText(dateVo.getDate());
        switch (sign){
            case -2://上个月的其他月（比当前月份小的月）
                holder.tv_week.setBackgroundColor(Color.WHITE);
                holder.tv_week.setTextColor(OTHER_MOUTH_OTHER_COLOR);
                holder.tv_week.setEnabled(false);
                break;
            case -1://当前月中的上个月（当前月）
                holder.tv_week.setBackgroundColor(Color.WHITE);
                holder.tv_week.setTextColor(OTHER_MOUTH_COLOR);
                holder.tv_week.setEnabled(false);
                break;
            case 0://当前月中的上个月（当前月）
                holder.tv_week.setBackgroundColor(Color.WHITE);
                holder.tv_week.setTextColor(CURRENT_MOUTH_COLOR);
                holder.tv_week.setEnabled(true);
                break;
            case 1://下个月（比当前月大的月份）
                holder.tv_week.setBackgroundColor(Color.WHITE);
                holder.tv_week.setTextColor(OTHER_MOUTH_COLOR);
                holder.tv_week.setEnabled(true);
                break;
            case 2://当天
                holder.tv_week.setBackgroundResource(R.drawable.circle_green_bg);
                holder.tv_week.setTextColor(Color.WHITE);
                holder.tv_week.setEnabled(true);
                break;
        }

        holder.tv_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getCount(map)<5 ||getCount(map)==5&&map.get(position) ){
                    if (map.get(position)){
                        if (sign!=1){
                            holder.tv_week.setBackgroundColor(Color.WHITE);
                            holder.tv_week.setTextColor(CURRENT_MOUTH_COLOR);
                        }else {
                            holder.tv_week.setBackgroundColor(Color.WHITE);
                            holder.tv_week.setTextColor(OTHER_MOUTH_COLOR);
                        }
                        map.put(position,false);
                    }else {
                        holder.tv_week.setBackgroundResource(R.drawable.circle_green_bg);
                        holder.tv_week.setTextColor(Color.WHITE);
                        map.put(position,true);
                    }
                }else{

                    Toast.makeText(context,"最多只能选择5个日期哦！",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private int getCount(Map<Integer,Boolean>  map){
        int count=0;
        for (int i = 0 ; i<map.size();i++){
            if (map.get(i)){
                count++;
            }
        }
        return count;
    }
    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return dateList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_week;

        public ViewHolder(View view) {
            super(view);
            tv_week = (TextView) view.findViewById(R.id.tv_week);
            view.setTag(this);
        }
    }

    public void setOnItemClickListener(MouthDateAdapter.onItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface onItemClickListener {
        void OnItemClick(View view, int position);
    }
}