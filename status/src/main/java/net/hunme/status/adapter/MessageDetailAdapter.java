package net.hunme.status.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.hunme.baselibrary.util.G;
import net.hunme.status.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private List<Map<String,Object>>  statusVoList;
    private Context context;
    public MessageDetailAdapter(Context context) {
        this.context =context;
       statusVoList = getMessageDetailList();
    }
    @Override
    public int getCount() {
        return statusVoList.size();
    }

    @Override
    public Object getItem(int i) {
        return statusVoList.get(i);
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
        Map<String,Object> statusVo  = statusVoList.get(i);
        String name = String.valueOf(statusVo.get("name"));
        String content= String.valueOf(statusVo.get("content"));
        String time = String.valueOf(statusVo.get("time"));
        viewHold.tv_name .setText(name);
        if (!G.isEmteny(content)){
            viewHold.tv_content.setVisibility(View.VISIBLE);
            viewHold.iv_praise.setVisibility(View.GONE);
            viewHold.tv_content.setText(content);
        }else {
            viewHold.tv_content.setVisibility(View.GONE);
            viewHold.iv_picture.setVisibility(View.GONE);
            viewHold.iv_praise.setVisibility(View.VISIBLE);
        }
        viewHold.tv_time.setText(time);
       return view;
    }
    class ViewHold{
        ImageView iv_head ;//头像
        TextView tv_name; //姓名
        TextView tv_time; //时间
        TextView tv_content; //评论内容内容
        ImageView iv_praise;//点赞
        ImageView iv_picture;//评论朋友圈的图片
        public ViewHold(View view) {
            iv_head= (ImageView) view.findViewById(R.id.iv_head);
            tv_name= (TextView) view.findViewById(R.id.tv_name);
            tv_time= (TextView) view.findViewById(R.id.tv_time);
            tv_content= (TextView) view.findViewById(R.id.tv_content);
            iv_praise = (ImageView) view.findViewById(R.id.iv_praise);
            iv_picture = (ImageView) view.findViewById(R.id.iv_picture);
            view.setTag(this);
        }
    }

    private  List<Map<String,Object>>  getMessageDetailList(){
        String[] times = new String[]{"刚刚","09:55","15:56","昨天 21：21","10月9日 20:13","2015年12月12日 12:45"};
        String[] names = new String[]{"王桦","杜尧天","王小二","周玲玲","杨海燕","胡世豪"};
        String[] contents = new String[]{"哈哈不错哦","我们而我的你就是的呢年底开始你想卡死你你看上下课了收纳","进入失败失败你还上班呢的保时捷的空间你快点才能看到市场","","想好好学吧",""};
        List<Map<String,Object>> mapList = new ArrayList<>();
        for (int i = 0;i<times.length;i++){
            Map<String,Object> map = new HashMap<>();
            map.put("time",times[i]);
            map.put("name",names[i]);
            map.put("content",contents[i]);
            mapList.add(map);
        }
        return mapList;
    }

}
