package net.hongzhang.school.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.hongzhang.school.R;

/**
 * 作者： wanghua
 * 时间： 2017/3/31
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class WeekTopAdapter extends RecyclerView.Adapter<WeekTopAdapter.ViewHolder> {
    private Context context;
    private static final String weeks[] = new String[]{"日", "一", "二", "三", "四", "五", "六"};
    public WeekTopAdapter(Context context) {
        this.context = context;
    }
    @Override
    public WeekTopAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                context).inflate(R.layout.item_week_top, parent, false);
        WeekTopAdapter.ViewHolder holder = new WeekTopAdapter.ViewHolder(view);
        return holder;

    }
    @Override
    public void onBindViewHolder(final WeekTopAdapter.ViewHolder holder,  int position) {
        holder.tv_week.setText(weeks[position]);
    }
    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return weeks.length;
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_week;
        public ViewHolder(View view) {
            super(view);
            tv_week = (TextView) view.findViewById(R.id.tv_week);
            view.setTag(this);
        }
    }

}