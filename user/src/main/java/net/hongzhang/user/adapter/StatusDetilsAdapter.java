package net.hongzhang.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.widget.CircleImageView;
import net.hongzhang.user.R;
import net.hongzhang.user.mode.StatusDetilsVo;

import java.util.List;


/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/9/9
 * 描    述：动态详情回复列表适配器
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class StatusDetilsAdapter extends BaseAdapter {
    private Context context;
    private List<StatusDetilsVo.DynamidRewListBean> listBeen;
    public StatusDetilsAdapter(Context context, List<StatusDetilsVo.DynamidRewListBean> listBeen) {
        this.context = context;
        this.listBeen=listBeen;
    }

    @Override
    public int getCount() {
        return listBeen.size();
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
            view= LayoutInflater.from(context).inflate(R.layout.item_comment_list,viewGroup,false);
            new ViewHold(view);
        }
        hold= (ViewHold) view.getTag();
        StatusDetilsVo.DynamidRewListBean rewListBean=listBeen.get(i);
        //测试数据
        ImageCache.imageLoader(rewListBean.getTsImgurl(),hold.cv_head);
        hold.tv_name.setText(rewListBean.getTsName());
        hold.tv_id.setBackgroundResource(R.drawable.user_teach_selecter);
        if(!G.isEmteny(rewListBean.getRew_ts_id())){
            hold.tv_reply_person.setVisibility(View.VISIBLE);
            hold.tv_reply_person.setText(rewListBean.getRewTsName());
            hold.ll_answer.setVisibility(View.VISIBLE);
        }else{
            hold.ll_answer.setVisibility(View.GONE);
            hold.tv_reply_person.setVisibility(View.GONE);
        }
        String date=rewListBean.getDate();
        if(date.contains("小时")){
            int hour=Integer.parseInt(date.substring(0,date.indexOf("小")));
            if(hour>10){
                hold.tv_time.setText(rewListBean.getCreateDate().substring(5,16));
            }else{
                hold.tv_time.setText(rewListBean.getDate());
            }
        }else{
            hold.tv_time.setText(rewListBean.getDate());
        }
        hold.tv_content.setText(rewListBean.getRew_content());
        if("1".equals(rewListBean.getTsType())){
            hold.tv_id.setText("学");
            hold.tv_id.setBackgroundResource(R.drawable.user_study_selecter);
        }else{
            hold.tv_id.setText("师");
            hold.tv_id.setBackgroundResource(R.drawable.user_teach_selecter);
        }
        return view;
    }

    class ViewHold{
        CircleImageView cv_head ;//回复人头像
        TextView tv_name; //回复人姓名
        TextView tv_id; //回复人身份
        TextView tv_reply_person; //被回复人姓名
        TextView tv_time; //回复时间
        TextView tv_content; //回复内容
        LinearLayout ll_answer;
        public ViewHold(View view) {
            cv_head= (CircleImageView) view.findViewById(R.id.cv_head);
            tv_name= (TextView) view.findViewById(R.id.tv_name);
            tv_id= (TextView) view.findViewById(R.id.tv_id);
            tv_reply_person= (TextView) view.findViewById(R.id.tv_reply_person);
            tv_time= (TextView) view.findViewById(R.id.tv_time);
            tv_content= (TextView) view.findViewById(R.id.tv_content);
            ll_answer= (LinearLayout) view.findViewById(R.id.ll_answer);
            view.setTag(this);
        }
    }
}
