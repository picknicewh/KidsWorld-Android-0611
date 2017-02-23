package net.hongzhang.message.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.hongzhang.baselibrary.mode.GroupInfoVo;
import net.hongzhang.message.R;

import java.util.List;

/**
 * 作者： Administrator
 * 时间： 2016/7/19
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class ClassAdapter extends BaseAdapter {
    private Context context;
    private List<GroupInfoVo> classnamelist;
    public ClassAdapter(Context context, List<GroupInfoVo> classnamelist){
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
            view = LayoutInflater.from(context).inflate(R.layout.item_class,null);
            viewHolder = new ViewHolder();
            viewHolder.tv_class = (TextView)view.findViewById(R.id.tv_class);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tv_class.setText(classnamelist.get(position).getGroupName());
        return view;
    }
    private  class ViewHolder{
        TextView tv_class;
    }
}
