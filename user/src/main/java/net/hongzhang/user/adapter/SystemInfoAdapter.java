package net.hongzhang.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.hongzhang.baselibrary.mode.SystemInformVo;
import net.hongzhang.user.R;

import java.util.List;

/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/26
 * 描    述：
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class SystemInfoAdapter extends BaseAdapter {
    private Context context;
    private List<SystemInformVo> systemInformVoList;

    public SystemInfoAdapter(List<SystemInformVo> systemInformVoList, Context context) {
        this.systemInformVoList = systemInformVoList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return systemInformVoList.size();
    }

    @Override
    public Object getItem(int i) {
        return systemInformVoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHold hold;
        if(view==null){
            view= LayoutInflater.from(context).inflate(R.layout.item_system_info,null);
            new ViewHold(view);
        }
        hold= (ViewHold) view.getTag();

        SystemInformVo vo=systemInformVoList.get(i);
        hold.tv_date.setText(vo.getTime().substring(0,10));
        hold.tv_content.setText(vo.getContent());
        hold.tv_title.setText(vo.getTitle());
         int flag = vo.getFlag();
        if(flag==1){
            hold.tv_date.setTextColor(context.getResources().getColor(R.color.theme_text));
            hold.tv_content.setTextColor(context.getResources().getColor(R.color.theme_text));
            hold.tv_title.setTextColor(context.getResources().getColor(R.color.theme_text));
        }else {
            hold.tv_date.setTextColor(context.getResources().getColor(R.color.default_grey));
            hold.tv_content.setTextColor(context.getResources().getColor(R.color.default_grey));
            hold.tv_title.setTextColor(context.getResources().getColor(R.color.default_grey));
        }
        return view;
    }

    class ViewHold{
        TextView tv_title;
        TextView tv_date;
        TextView tv_content;

        public ViewHold(View view) {
            tv_date= (TextView) view.findViewById(R.id.tv_date);
            tv_content= (TextView) view.findViewById(R.id.tv_content);
            tv_title = (TextView)view.findViewById(R.id.tv_stitle) ;
            view.setTag(this);
        }
    }
}
