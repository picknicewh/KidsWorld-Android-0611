package net.hongzhang.user.adapter;

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
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.DateUtil;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.user.R;
import net.hongzhang.user.activity.MyDynamicActivity;
import net.hongzhang.user.activity.StatusDetilsActivity;
import net.hongzhang.user.mode.DynamicInfoVo;
import net.hongzhang.user.util.PictrueUtils;
import net.hongzhang.user.util.PublishPhotoUtil;
import net.hongzhang.user.widget.DeleteDynamicDialog;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ================================================
 * 作    者：wh
 * 时    间：2016/10/12
 * 描    述：动态列表适配器
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class MyDynamicAdapter extends BaseAdapter implements  OkHttpListener {
    private MyDynamicActivity myDynamicActivity;
    private   List<DynamicInfoVo> dynamicInfoVos;
    private int pageNumber;
    private Context context;
    /**
     * 点赞集合
     */
    private  Map<Integer,Integer> praises;
    /**
     * 点赞数量集合
     */
    private  Map<Integer,Integer> praiseNums;
    public MyDynamicAdapter(MyDynamicActivity myDynamicActivity,List<DynamicInfoVo> dynamicInfoVos, int pageNumber) {
        this.myDynamicActivity = myDynamicActivity;
        this.dynamicInfoVos = dynamicInfoVos;
        this.pageNumber = pageNumber;
         this.context = myDynamicActivity;
         praiseNums = new HashMap<>();
         praises = new HashMap<>();
    }
    /**
     * 设置相对于的数据
     */
    public  void setData(List<DynamicInfoVo> dynamicInfoVos ){
        if (dynamicInfoVos!=null){
            for (int i = 0 ; i<dynamicInfoVos.size();i++){
                DynamicInfoVo statusVo = dynamicInfoVos.get(i);
                praises.put(i,statusVo.getIsAgree());
                int size=statusVo.getList().size();
                praiseNums.put(i,size);
            }
        }
    }
    @Override
    public int getCount() {
        return dynamicInfoVos.size();
    }

    @Override
    public Object getItem(int i) {
        return dynamicInfoVos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHold viewHold = null;
        if(view==null){
            view= LayoutInflater.from(context).inflate(R.layout.item_mydynamic,viewGroup,false);
            new ViewHold(view);
        }
        viewHold= (ViewHold) view.getTag();
        final DynamicInfoVo dynamicInfoVo = dynamicInfoVos.get(i);
        viewHold.tv_day.setText(DateUtil.getMyDay(dynamicInfoVo.getCreateTime()));
        viewHold.tv_mouth.setText(DateUtil.getMyMouth(dynamicInfoVo.getCreateTime()));
        //显示内容+加载图片
        if (!G.isEmteny(dynamicInfoVo.getText())){
            viewHold.tv_content.setText(dynamicInfoVo.getText());
            viewHold.rl_picture.setVisibility(View.GONE);
            viewHold.tv_content.setVisibility(View.VISIBLE);
            if (dynamicInfoVo.getImgUrl()!=null&&dynamicInfoVo.getImgUrl().size()>0){
                viewHold.iv_picture.setVisibility(View.VISIBLE);
                viewHold.tv_pic_count.setVisibility(View.VISIBLE);
                viewHold.tv_pic_count.setText("共"+dynamicInfoVo.getImgUrl().size()+"张");
                ImageCache.imageLoader(dynamicInfoVo.getImgUrl().get(0), viewHold.iv_picture);
                viewHold.iv_picture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PublishPhotoUtil.imageBrowernet(0,dynamicInfoVo.getImgUrl(),context);
                    }
                });
            }else {
                viewHold.iv_picture.setVisibility(View.GONE);
                viewHold.tv_pic_count.setVisibility(View.GONE);

            }
        }else {
            viewHold.tv_pic_count.setVisibility(View.GONE);
            viewHold.iv_picture.setVisibility(View.GONE);
            viewHold.tv_content.setVisibility(View.GONE);
            if (dynamicInfoVo.getImgUrl()!=null&&dynamicInfoVo.getImgUrl().size()>0){
                viewHold.rl_picture.setVisibility(View.VISIBLE);
                new PictrueUtils().setPictrueLoad(context,dynamicInfoVo.getImgUrl(),viewHold.rl_picture);
            }else {
                viewHold.rl_picture.setVisibility(View.GONE);
            }
        }
        //显示点赞
        if (praiseNums.get(i)>0){
            viewHold.tv_praise_num.setText(String.valueOf(praiseNums.get(i)));
        }else {
            viewHold.tv_praise_num.setText("赞");
        }
        //显示评论
        if (dynamicInfoVo.getRewCount()>0){
            viewHold.tv_comment_num.setText(String.valueOf(dynamicInfoVo.getRewCount()));
        }else {
            viewHold.tv_comment_num.setText("评论");
        }
        //是否点赞
        if (praises.get(i)==1){
            viewHold.iv_praise.setImageResource(R.mipmap.ic_heat_on);
        }else if (praises.get(i)==2){
            viewHold.iv_praise.setImageResource(R.mipmap.ic_heat_off);
        }
      //点赞按钮事件
        viewHold.iv_praise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String  cancle =null ;
                if (praises.get(i)==1){
                    cancle="2";
                    praises.put(i,2);
                    praiseNums.put(i,praiseNums.get(i)-1);
                }else if (praises.get(i)==2){
                    cancle = "1";
                    praises.put(i,1);
                    praiseNums.put(i,praiseNums.get(i)+1);
                }
                personPraise(UserMessage.getInstance(context).getTsId(),dynamicInfoVo.getDynamicId(), cancle);
            }
        });
        viewHold.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteDynamicDialog dialog = new DeleteDynamicDialog(myDynamicActivity,dynamicInfoVo.getDynamicId(),i);
                dialog.initView();
            }
        });
        viewHold.ll_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(context, StatusDetilsActivity.class);
                intent.putExtra("dynamicId",dynamicInfoVo.getDynamicId());
                myDynamicActivity.setScrollPosition(getScrollPosition(dynamicInfoVo));
                context.startActivity(intent);
            }
        });
        return view;
    }
    /**
     * 获取当前点击修改状态在数据的位置
     * @param dynamicInfoVo
     */
    private int  getScrollPosition(DynamicInfoVo dynamicInfoVo){
        for (int i = 0;i<myDynamicActivity.dynamicInfoVoList.size();i++){
            if (myDynamicActivity.dynamicInfoVoList.get(i).getDynamicId().equals(dynamicInfoVo.getDynamicId())){
                return  i;
            }
        }
        return 1;
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
                Toast.makeText(context,result,Toast.LENGTH_SHORT).show();
                MyDynamicAdapter.this.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onError(String uri, String error) {
        Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
    }
    class ViewHold{
        TextView tv_day; //时间
        TextView tv_mouth; //时间
        TextView tv_content; //内容
        TextView tv_praise_num;//点赞的数量
        TextView tv_comment_num;//评论数量
        ImageView iv_comment;//评论
        ImageView iv_praise;//是否已经点赞
        RelativeLayout rl_picture;
        ImageView iv_picture;
        TextView tv_pic_count;
        TextView tv_delete;
        LinearLayout ll_comment;
        public ViewHold(View view) {
            tv_day= (TextView) view.findViewById(R.id.tv_day);
            tv_mouth= (TextView) view.findViewById(R.id.tv_mouth);
            tv_content= (TextView) view.findViewById(R.id.tv_content);
            tv_praise_num = (TextView)view.findViewById(R.id.tv_praise_num);
            tv_comment_num = (TextView)view.findViewById(R.id.tv_comment_num);
            iv_praise = (ImageView) view.findViewById(R.id.iv_praise);
            iv_comment = (ImageView)view.findViewById(R.id.iv_comment);
            rl_picture = (RelativeLayout) view.findViewById(R.id.rl_picture);
            iv_picture = (ImageView) view.findViewById(R.id.iv_picture);
            tv_pic_count= (TextView) view.findViewById(R.id.tv_pic_count);
            tv_delete= (TextView) view.findViewById(R.id.tv_delete);
            ll_comment=(LinearLayout)view.findViewById(R.id.ll_comment);
            view.setTag(this);
        }
    }
}
