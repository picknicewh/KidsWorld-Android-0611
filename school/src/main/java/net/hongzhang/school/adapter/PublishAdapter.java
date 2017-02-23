package net.hongzhang.school.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.school.R;
import net.hongzhang.school.bean.PublishVo;

import java.util.List;

/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/8/1
 * 描    述：
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class PublishAdapter extends BaseAdapter {
    private Context context;
    private List<PublishVo> publishList;

    public PublishAdapter(Context context, List<PublishVo> publishList) {
        this.context = context;
        this.publishList = publishList;
    }

    @Override
    public int getCount() {
        return publishList.size();
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
        ViewHold viewHold;
        if(null==view){
            view= LayoutInflater.from(context).inflate(R.layout.item_publish,null);
            new ViewHold(view);
        }
        PublishVo vo=publishList.get(i);
        viewHold= (ViewHold) view.getTag();
        if (G.isEmteny(vo.getTsName())){
            viewHold.tv_title.setText("校长");
        }else {
            viewHold.tv_title.setText(vo.getTsName());
        }
        viewHold.tv_content.setText(vo.getMessage());
        viewHold.tv_date.setText(vo.getDateTime().substring(0,10));
        if (G.isEmteny(vo.getImgUrl())){
            viewHold.lv_holad.setImageResource(R.mipmap.ic_headmaster);//校长的头像
        }else {
            ImageCache.imageLoader(vo.getImgUrl(),viewHold.lv_holad);
        }

        if (!G.isEmteny(vo.getTitle())){
            viewHold.tv_ptitle.setText(vo.getTitle());
        }
        viewHold.setTextColor(vo.isRead());
        return view;
    }

     class ViewHold{
         ImageView lv_holad;
         TextView tv_title;
         TextView tv_date;
         TextView tv_content;
         TextView tv_ptitle;
        public ViewHold(View view) {
            lv_holad = (ImageView) view.findViewById(R.id.lv_holad);
            tv_title= (TextView) view.findViewById(R.id.tv_title);
            tv_ptitle= (TextView) view.findViewById(R.id.tv_ptitle);
            tv_date= (TextView) view.findViewById(R.id.tv_date);
            tv_content= (TextView) view.findViewById(R.id.tv_content);
            view.setTag(this);
        }

         public void setTextColor(boolean isRead) {
             if (!isRead) {
                 tv_content.setTextColor(context.getResources().getColor(R.color.sc_));
                 tv_title.setTextColor(context.getResources().getColor(R.color.main_green));
                 tv_ptitle.setTextColor(Color.BLACK);
             } else {
                 tv_content.setTextColor(Color.parseColor("#a9a9a9"));
                 tv_title.setTextColor(Color.parseColor("#a9a9a9"));
                 tv_ptitle.setTextColor(Color.parseColor("#a9a9a9"));
             }
         }
     }
}
