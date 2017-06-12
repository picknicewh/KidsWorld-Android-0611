package net.hongzhang.school.adapter;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.hongzhang.baselibrary.util.G;
import net.hongzhang.school.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：Restring
 * 时间：2017/4/20
 * 描述：
 * 版本：
 */

public class MedicineDateAdapter extends RecyclerView.Adapter<MedicineDateAdapter.ViewHolder> {
    private List<String> itemList;
    private onItemClickListener itemClickListener = null;
    private Activity context;
    private Map<Integer, Boolean> selectList;

    public MedicineDateAdapter(Activity context, List<String> itemList) {
        this.itemList = itemList;
        this.context = context;
        initData(0);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_medicine_date, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String text = itemList.get(position);
        if (text.contains("-")) {
            holder.tv_date.setText(getDateText(text));
        } else {
            holder.tv_date.setText(text);
        }

        if (selectList.size() > 0) {
            if (selectList.get(position)) {
                holder.tv_date.setTextColor(ContextCompat.getColor(context, R.color.main_green));
                holder.v_line.setVisibility(View.VISIBLE);
            } else {
                holder.tv_date.setTextColor(ContextCompat.getColor(context, R.color.theme_text));
                holder.v_line.setVisibility(View.GONE);
            }
        } else {
            if (position == 0) {
                holder.v_line.setVisibility(View.VISIBLE);
            } else {
                holder.v_line.setVisibility(View.GONE);
            }

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initData(position);
                notifyDataSetChanged();
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(view, position);
                }
            }
        });
    }

    private String getDateText(String text) {
        String mouth = text.substring(5, 7) + "月";
        String day = text.substring(8, 10) + "日";
        return mouth + day;
    }

    private void initData(int position) {
        selectList = new HashMap<>();
        for (int i = 0; i < itemList.size(); i++) {
            if (i == position) {
                selectList.put(i, true);
            } else {
                selectList.put(i, false);
            }
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_date;
        View v_line;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            v_line = itemView.findViewById(R.id.v_line);
            setLayoutParam(tv_date, v_line);
            itemView.setTag(this);
        }
    }

    private void setLayoutParam(TextView textView, View view) {
        G.initDisplaySize(context);
        int width = (G.size.W) / 4;
        int margin = G.dp2px(context, 10);
        LinearLayout.LayoutParams lpTextView = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        lpTextView.gravity = Gravity.CENTER;
        textView.setGravity(Gravity.CENTER);
        lpTextView.setMargins(0, margin, 0, margin);
        LinearLayout.LayoutParams lpView = new LinearLayout.LayoutParams(width, G.dp2px(context, 2));
        lpView.gravity = Gravity.CENTER;
        textView.setLayoutParams(lpTextView);
        view.setLayoutParams(lpView);
        view.setVisibility(View.GONE);
    }

    public void setOnItemClickListener(onItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface onItemClickListener {
        void onItemClick(View view, int position);
    }
}
