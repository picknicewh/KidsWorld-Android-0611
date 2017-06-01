package net.hongzhang.school.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.hongzhang.baselibrary.util.G;
import net.hongzhang.school.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 作者：wh
 * 时间：2017/5/5
 * 描述：
 * 版本：
 */

public class MyDimensionalityAdapter extends RecyclerView.Adapter<MyDimensionalityAdapter.ViewHolder> {
    private Map<String, String> list;
    private Context context;
    public static Map<Integer, Boolean> isSelects;
    public MyDimensionalityAdapter(Context context, Map<String, String> list) {
        this.context = context;
        this.list = list;
        initAllSelect();
    }
    /**
     * 获取map所有的值
     */
    private List<String> getValueList() {
        List<String> values = new ArrayList<>();
        Set<Map.Entry<String, String>> entries = list.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            values.add(entry.getValue());
            G.log("-----xx---" + entry.getValue());
        }
        return values;
    }
    /**
     * 获取map所有的值
     */
    private List<String> getKeyList() {
        List<String> values = new ArrayList<>();
        Set<Map.Entry<String, String>> entries = list.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            values.add(entry.getKey());
            G.log("-----xx---" + entry.getValue());
        }
        return values;
    }
    private void initAllSelect() {
        isSelects = new HashMap<>();
        for (int i = 0; i < list.keySet().size(); i++) {
            isSelects.put(i, false);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_activity_type, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        List<String> values = getValueList();
        values.set(0, " "+values.get(0) + "    ");
        holder.tv_text.setText(values.get(position));
        if (isSelects.get(position)) {
            holder.tv_text.setBackgroundResource(R.drawable.frame_green);
            holder.tv_text.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else {
            holder.tv_text.setBackgroundResource(R.drawable.frame_white);
            holder.tv_text.setTextColor(ContextCompat.getColor(context, R.color.theme_text));
        }
        holder.tv_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSelects.get(position)) {
                    isSelects.put(position, false);
                } else {
                    isSelects.put(position, true);
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.keySet().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_text;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_text = (TextView) itemView.findViewById(R.id.tv_text);
            itemView.setTag(this);
        }
    }

    /**
     * 获取选择的维度
     */
    public String getSelected() {
        StringBuffer buffer = new StringBuffer();
        List<String> values = getKeyList();
        for (int i = 0; i < list.keySet().size(); i++) {
            G.log("---xx----------"+isSelects.get(i));
            if (isSelects.get(i)) {
                buffer.append(values.get(i)).append(",");
            }
        }
        if (buffer.length() != 0) {
            buffer.deleteCharAt(buffer.length()-1);
        }
        return buffer.toString();
    }
}
