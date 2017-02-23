package net.hongzhang.status.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.status.R;
import net.hongzhang.status.mode.StatusInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * ================================================
 * 作    者：wh
 * 时    间：2016/9/9
 * 描    述：动态列表适配器
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class MessageDetailAdapter extends BaseAdapter  {
    private List<StatusInfo> statusInfos;
    private Context context;
    public MessageDetailAdapter(Context context,List<StatusInfo> statusInfos) {
        this.context =context;
        this.statusInfos =statusInfos;
    }
    @Override
    public int getCount() {
        return statusInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return statusInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHold viewHold = null;
        if(view==null){
            view= LayoutInflater.from(context).inflate(R.layout.item_message_detail,viewGroup,false);
            new ViewHold(view);
        }
        viewHold= (ViewHold) view.getTag();
         StatusInfo statusInfo = statusInfos.get(i);
        String name =statusInfo.getInput_name();
        String content= statusInfo.getInput_content();
        String time = statusInfo.getCreate_time();
        String headImage = statusInfo.getInput_url();
        String imageUrlContent  = statusInfo.getDynamic_content();
        viewHold.tv_name .setText(name);
        if (!content.equals("#点赞#")){
            viewHold.tv_content.setVisibility(View.VISIBLE);
            viewHold.iv_praise.setVisibility(View.GONE);
            viewHold.tv_content.setText(content);
        }else {
            viewHold.tv_content.setVisibility(View.GONE);
            viewHold.iv_picture.setVisibility(View.GONE);
            viewHold.iv_praise.setVisibility(View.VISIBLE);
        }
        viewHold.tv_time.setText(getFormateDate(time));
        ImageCache.imageLoader(headImage,viewHold.iv_head);
        if (statusInfo.getMessage_type().equals("1")){
            viewHold.iv_picture.setVisibility(View.VISIBLE);
            viewHold.tv_dynamic_content.setVisibility(View.GONE);
            ImageCache.imageLoader(imageUrlContent,viewHold.iv_picture);
        }else {
            viewHold.tv_dynamic_content.setVisibility(View.VISIBLE);
            viewHold.tv_dynamic_content.setText(imageUrlContent);
            viewHold.iv_picture.setVisibility(View.GONE);
        }
       return view;
    }

    class ViewHold{
        ImageView iv_head ;//头像
        TextView tv_name; //姓名
        TextView tv_time; //时间
        TextView tv_content; //评论内容内容
        ImageView iv_praise;//点赞
        ImageView iv_picture;//评论朋友圈的图片
        TextView tv_dynamic_content;
        public ViewHold(View view) {
            iv_head= (ImageView) view.findViewById(R.id.iv_head);
            tv_name= (TextView) view.findViewById(R.id.tv_name);
            tv_time= (TextView) view.findViewById(R.id.tv_time);
            tv_content= (TextView) view.findViewById(R.id.tv_content);
            iv_praise = (ImageView) view.findViewById(R.id.iv_praise);
            iv_picture = (ImageView) view.findViewById(R.id.iv_picture);
            tv_dynamic_content =  (TextView) view.findViewById(R.id.tv_dynamic_content);
            view.setTag(this);
        }
    }
    private String getFormateDate(String timeDate){
        long time = Long.parseLong(timeDate);
        Date date = new Date(time);
        SimpleDateFormat format  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return  format.format(date);
    }
}
