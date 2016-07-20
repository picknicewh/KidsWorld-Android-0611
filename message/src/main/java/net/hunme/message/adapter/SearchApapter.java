package net.hunme.message.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.hunme.baselibrary.widget.CircleImageView;
import net.hunme.message.R;

/**
 * 作者： Administrator
 * 时间： 2016/7/20
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class SearchApapter extends BaseAdapter {
    private Context context;
  //  private List<Map<String,Object>> mlist;
    String[] name = new String[]{"王小二","刘德华","吴亦凡","吴用","周磊","鹿晗","郑爽","大幂幂","吴彦祖","刘诗诗"};
    public SearchApapter(Context context){
        this.context = context;
     //   this.mlist = mlist;
    }

    @Override
    public int getCount() {
        return name.length;
    }

    @Override
    public Object getItem(int position) {
        return name[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view==null){
            view = LayoutInflater.from(context).inflate(R.layout.item_search,null);
            viewHolder = new ViewHolder();
            viewHolder.iv_icon = (CircleImageView) view.findViewById(R.id.iv_contract_icon);
            viewHolder.iv_name = (TextView)view.findViewById(R.id.tv_contract_name);
            view.setTag(viewHolder);
        }else {
           viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.iv_icon.setImageResource(R.mipmap.person);
        viewHolder.iv_name.setText(name[position]);
        return view;
    }
    private  class ViewHolder{
        CircleImageView iv_icon;
        TextView iv_name;
    }
}
