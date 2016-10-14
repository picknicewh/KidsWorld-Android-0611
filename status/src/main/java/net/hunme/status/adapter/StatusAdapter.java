package net.hunme.status.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import net.hunme.baselibrary.image.ImageCache;
import net.hunme.baselibrary.mode.Result;
import net.hunme.baselibrary.network.Apiurl;
import net.hunme.baselibrary.network.OkHttpListener;
import net.hunme.baselibrary.network.OkHttps;
import net.hunme.baselibrary.util.G;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.baselibrary.widget.CircleImageView;
import net.hunme.status.R;
import net.hunme.status.StatusFragement;
import net.hunme.status.mode.StatusVo;
import net.hunme.status.util.PictrueUtils;
import net.hunme.user.activity.StatusDetilsActivity;

import java.lang.reflect.Type;
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
public class StatusAdapter extends BaseAdapter implements  OkHttpListener {
    private StatusFragement statusFragement;
    private List<StatusVo> statusVoList;
    private Context context;

    public StatusAdapter(StatusFragement statusFragement, List<StatusVo> statusVoList) {
        this.statusFragement = statusFragement;
        this.statusVoList = statusVoList;
        context = statusFragement.getActivity();

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
        String praisePerson = "";
        if(view==null){
            view= LayoutInflater.from(context).inflate(R.layout.layout_status_head,viewGroup,false);
            new ViewHold(view);
        }
        viewHold= (ViewHold) view.getTag();
        final StatusVo statusVo = statusVoList.get(i);
        ImageCache.imageLoader(statusVo.getImg(),viewHold.cv_head);
        viewHold.tv_name.setText(statusVo.getTsName());
        viewHold.tv_time.setText(statusVo.getDate());
        //显示内容
        if (!G.isEmteny(statusVo.getText())){
            viewHold.tv_content.setText(statusVo.getText());
            viewHold.tv_content.setVisibility(View.VISIBLE);
        }else {
            viewHold.tv_content.setVisibility(View.GONE);
        }

        //显示点赞和横条
        if (statusVo.getList()!=null&& statusVo.getList().size()>0){
            viewHold.ll_praise.setVisibility(View.VISIBLE);
            for (String s:statusVo.getList()){
                praisePerson= G.isEmteny(praisePerson)?praisePerson+s:praisePerson+"、"+s;
                viewHold.tv_praise_num.setText(String.valueOf(statusVo.getList().size()));
                viewHold.tv_praise_person.setText(praisePerson);
            }
        }else {
            viewHold.ll_praise.setVisibility(View.GONE);
            viewHold.tv_praise_num.setText("赞");
        }
        //显示评论
        if (statusVo.getRewCount()>0){
            viewHold.tv_comment_num.setText(String.valueOf(statusVo.getRewCount()));
        }else {
            viewHold.tv_comment_num.setText("评论");
        }
        //显示的身份
        if (statusVo.getTsType()==1){
            viewHold.tv_id.setText("学");
            viewHold.tv_id.setBackgroundResource(R.drawable.user_study_selecter);
        }else if (statusVo.getTsType()==0){
            viewHold.tv_id.setText("师");
            viewHold.tv_id.setBackgroundResource(R.drawable.user_teach_selecter);
        }
        //是否点赞
        if (statusVo.getIsAgree()==1){
            viewHold.iv_praise.setImageResource(R.mipmap.ic_heat_on);
        }else if (statusVo.getIsAgree()==2){
            viewHold.iv_praise.setImageResource(R.mipmap.ic_heat_off);
        }

        //加载图片
        if (statusVo.getImgUrl()!=null&&statusVo.getImgUrl().size()>0){
            viewHold.rl_picture.setVisibility(View.VISIBLE);

            new PictrueUtils().setPictrueLoad(context,statusVo.getImgUrl(),viewHold.rl_picture);
        }else {
            viewHold.rl_picture.setVisibility(View.GONE);
        }//点赞按钮事件
        viewHold.iv_praise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String  cancle =null ;
                if (statusVo.getIsAgree()==1){
                    cancle="2";
                }else if (statusVo.getIsAgree()==2){
                    cancle = "1";
                }
                personPraise(UserMessage.getInstance(context).getTsId(),statusVo.getDynamicId(), cancle);
            }
        });
        //评论点击事件
        viewHold.iv_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StatusDetilsActivity.class);
                intent.putExtra("dynamicId",statusVo.getDynamicId());
                context.startActivity(intent);
            }
        });
        //评论点击事件
        viewHold.tv_comment_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StatusDetilsActivity.class);
                intent.putExtra("dynamicId",statusVo.getDynamicId());
                context.startActivity(intent);
            }
        });
        return view;
    }
    /**
     * 点赞
     */
    public void personPraise(String tsId,String dynamicId,String cancel) {
        Map<String,Object> map=new HashMap<>();
        map.put("tsId",tsId);
        map.put("dynamicId",dynamicId);
        map.put("cancel",cancel);
        Type type=new TypeToken<Result<String>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.SUBPRAISE,map,this);
    }
    @Override
    public void onSuccess(String uri, Object date) {
        if (uri.equals(Apiurl.SUBPRAISE)){
            String result = ((Result<String>)date).getData();
            if (result.contains("成功")){
                statusFragement.getDynamicList(statusFragement.getGroupId(),statusFragement.getGroupType(),1);
                notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onError(String uri, String error) {

    }
    class ViewHold{
        CircleImageView cv_head ;//头像
        TextView tv_name; //姓名
        TextView tv_id; //身份
        TextView tv_time; //时间
        TextView tv_content; //内容
        TextView tv_praise_num;//点赞的数量
        TextView tv_comment_num;//评论数量
        ImageView iv_comment;//评论
        TextView tv_praise_person;//点赞的人姓名
        ImageView iv_praise;//是否已经点赞
        LinearLayout ll_praise;//点赞横条
        RelativeLayout rl_picture;
        public ViewHold(View view) {
            cv_head= (CircleImageView) view.findViewById(R.id.cv_head);
            tv_name= (TextView) view.findViewById(R.id.tv_name);
            tv_id= (TextView) view.findViewById(R.id.tv_id);
            tv_time= (TextView) view.findViewById(R.id.tv_time);
            tv_content= (TextView) view.findViewById(R.id.tv_content);
            ll_praise= (LinearLayout) view.findViewById(R.id.ll_praise);
            tv_praise_num = (TextView)view.findViewById(R.id.tv_praise_num);
            tv_comment_num = (TextView)view.findViewById(R.id.tv_comment_num);
            tv_praise_person = (TextView)view.findViewById(R.id.tv_praise_person);
            iv_praise = (ImageView) view.findViewById(R.id.iv_praise);
            iv_comment = (ImageView)view.findViewById(R.id.iv_comment);
            rl_picture = (RelativeLayout) view.findViewById(R.id.rl_picture);
            view.setTag(this);
        }
    }
}
