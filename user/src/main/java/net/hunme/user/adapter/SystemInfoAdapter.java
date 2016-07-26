package net.hunme.user.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.hunme.user.R;
import net.hunme.user.activity.SystemInfoActivity;
import net.hunme.user.mode.MessageVo;
import net.hunme.user.util.SystemInfoDbHelp;

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
    private List<MessageVo>messageList;

    public SystemInfoAdapter(List<MessageVo> messageList, Context context) {
        this.messageList = messageList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHold hold;
        if(view==null){
            view= LayoutInflater.from(context).inflate(R.layout.item_system_info,null);
            new ViewHold(view);
        }
        hold= (ViewHold) view.getTag();
        MessageVo vo=messageList.get(i);
        hold.tv_date.setText(vo.getDate());
        hold.tv_content.setText(vo.getContent());
        SQLiteDatabase db=SystemInfoActivity.infoDb.getReadableDatabase();
        boolean type = SystemInfoDbHelp.select(db, vo.getContent()+vo.getDate());
        if(!type){
            hold.tv_date.setTextColor(Color.BLACK);
            hold.tv_content.setTextColor(Color.BLACK);
        }
        return view;
    }

    class ViewHold{
        TextView tv_date;
        TextView tv_content;

        public ViewHold(View view) {
            tv_date= (TextView) view.findViewById(R.id.tv_date);
            tv_content= (TextView) view.findViewById(R.id.tv_content);
            view.setTag(this);
        }
    }
}
