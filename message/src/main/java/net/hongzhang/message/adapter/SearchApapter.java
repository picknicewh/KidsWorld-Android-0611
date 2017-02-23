package net.hongzhang.message.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.baselibrary.mode.ContractInfoVo;
import net.hongzhang.baselibrary.widget.CircleImageView;
import net.hongzhang.message.R;

import java.util.List;

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
     private List<ContractInfoVo> friendInforVos;

    public SearchApapter(Context context,List<ContractInfoVo> friendInforVos){
        this.context = context;
        this.friendInforVos = friendInforVos;
    }

    @Override
    public int getCount() {
        return friendInforVos.size();
    }

    @Override
    public Object getItem(int position) {
        return friendInforVos.get(position);
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
        ContractInfoVo contractInfoVo = friendInforVos.get(position);
        ImageCache.imageLoader(contractInfoVo.getImg(),viewHolder.iv_icon);
        viewHolder.iv_name.setText(contractInfoVo.getTsName());
        return view;
    }
    private  class ViewHolder{
        CircleImageView iv_icon;
        TextView iv_name;
    }
}
