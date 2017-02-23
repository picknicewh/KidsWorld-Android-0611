package net.hongzhang.status.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.hongzhang.status.R;

import java.util.List;

/**
 * 作者： Administrator
 * 时间： 2016/7/19
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class ChooseClassAdapter extends BaseAdapter {
    private Context context;
    private List<String> classnamelist;
    public  ChooseClassAdapter(Context context,List<String> classnamelist){
        this.context = context;
        this.classnamelist = classnamelist;
    }

    @Override
    public int getCount() {
        return classnamelist.size();
    }

    @Override
    public Object getItem(int position) {
        return classnamelist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view==null){
            view = LayoutInflater.from(context).inflate(R.layout.item_chooseclass,null);
            viewHolder = new ViewHolder();
            viewHolder.tv_ca_class = (TextView)view.findViewById(R.id.tv_ca_class);
            viewHolder.v_line=view.findViewById(R.id.v_line);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if(position==classnamelist.size()-1){
            viewHolder.v_line.setVisibility(View.GONE);
        }else{
            viewHolder.v_line.setVisibility(View.VISIBLE);
        }
        viewHolder.tv_ca_class.setText(classnamelist.get(position));
        return view;
    }
    private  class ViewHolder{
        TextView tv_ca_class;
        View v_line;
    }
}
