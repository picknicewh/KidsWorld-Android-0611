package net.hongzhang.status.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.baselibrary.widget.CircleImageView;
import net.hongzhang.status.R;
import net.hongzhang.status.StatusFragement;
import net.hongzhang.status.mode.StatusVo;
import net.hongzhang.status.util.PictrueUtils;
import net.hongzhang.status.widget.DeleteStatusDialog;
import net.hongzhang.user.activity.StatusDetilsActivity;

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
    /**
     * 点赞集合
     */
    private  Map<Integer,Integer> praises;
    /**
     * 点赞数量集合
     */
    private  Map<Integer,Integer> praiseNums;
    /**
     * 点赞名字集合
     */
    private  Map<Integer,String>praisesNames;
    public StatusAdapter(StatusFragement statusFragement, List<StatusVo> statusVoList) {
        this.statusFragement = statusFragement;
        this.statusVoList = statusVoList;
        context = statusFragement.getActivity();
        praises = new HashMap<>();
        praiseNums = new HashMap<>();
        praisesNames = new HashMap<>();
    }
    /**
     * 设置相对于的数据
     */
    public  void setData(List<StatusVo> statusVoList){
        if (statusVoList!=null){
            for (int i = 0 ; i<statusVoList.size();i++){
                StatusVo statusVo = statusVoList.get(i);
                praises.put(i,statusVo.getIsAgree());
                int size=statusVo.getList().size();
                praiseNums.put(i,size);
                praisesNames.put(i,getPesronname(statusVo.getList()));
            }
        }
    }
    private static String getPesronname(List<String> names){
        String praisePerson = "";
        if (names.size()>0){
            for (String s:names){
                praisePerson= G.isEmteny(praisePerson)?praisePerson+s:praisePerson+"、"+s;
            }
            return praisePerson;
        }
        return "";
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
         ViewHold viewHold = null;
        if(view==null){
            view= LayoutInflater.from(context).inflate(R.layout.layout_status_head,viewGroup,false);
            new ViewHold(view);
        }
        viewHold= (ViewHold) view.getTag();
        final StatusVo statusVo = statusVoList.get(i);
        ImageCache.imageLoader(statusVo.getImg(),viewHold.cv_head);
        viewHold.tv_name.setText(statusVo.getTsName());
        if (statusVo.getDate().length()>10){
            viewHold.tv_time.setText(statusVo.getDate().substring(0,10));
        }else {
            viewHold.tv_time.setText(statusVo.getDate());
        }
        //显示内容
        if (!G.isEmteny(statusVo.getText())){
            viewHold.tv_content.setText(statusVo.getText());
            viewHold.tv_content.setVisibility(View.VISIBLE);
        }else {
            viewHold.tv_content.setVisibility(View.GONE);
        }
        //显示点赞和横条
        if (praiseNums.get(i)>0){
            viewHold.ll_praise.setVisibility(View.VISIBLE);
            viewHold.tv_praise_num.setText(String.valueOf(praiseNums.get(i)));
            viewHold.tv_praise_person.setText(praisesNames.get(i));
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
        }else if (statusVo.getTsType()==2||statusVo.getTsType()==0){
            viewHold.tv_id.setText("师");
            viewHold.tv_id.setBackgroundResource(R.drawable.user_teach_selecter);
        }
        Log.i("sssss",statusVo.getTsType()+"====================");
        //是否点赞
         if(praises.get(i)==1){
             viewHold.iv_praise.setImageResource(R.mipmap.ic_heat_on);
         }else if (praises.get(i)==2){
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
                String  praisePerson = praisesNames.get(i);
                String cancle="";
                if (praises.get(i)==1){//点赞,取消赞
                    cancle = "2";
                    praises.put(i,2);
                    String[] plist = praisePerson.split("、");
                    if (plist[0].equals(username)){
                        if (plist.length==1){
                            praisesNames.put(i,praisePerson.replace(username,""));
                        }else {
                            praisesNames.put(i,praisePerson.replace(username+"、",""));
                        }
                    }else {
                        if (!praisePerson.contains("、")){
                            praisesNames.put(i,praisePerson.replace(username,""));
                        }else{
                            praisesNames.put(i,praisePerson.replace("、"+username,""));
                        }
                    }
                    praiseNums.put(i,praiseNums.get(i)-1);
                }else if (praises.get(i)==2){//没点赞，点赞
                    cancle = "1";
                    praises.put(i,1);
                    praisesNames.put(i,G.isEmteny(praisePerson)?praisePerson+username:praisePerson+"、"+username);
                    praiseNums.put(i,praiseNums.get(i)+1);
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
                statusFragement.setScrollPosition(getScrollPosition(statusVo));
                context.startActivity(intent);
            }
        });
        //评论点击事件
        viewHold.tv_comment_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StatusDetilsActivity.class);
                intent.putExtra("dynamicId",statusVo.getDynamicId());
                statusFragement.setScrollPosition(getScrollPosition(statusVo));
                context.startActivity(intent);
            }
        });
        if (UserMessage.getInstance(context).getTsId().equals(statusVo.getTsId())){
            viewHold.tv_delete.setVisibility(View.VISIBLE);
        }else {
            viewHold.tv_delete.setVisibility(View.GONE);
        }
        viewHold.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteStatusDialog dialog = new DeleteStatusDialog(statusFragement,statusVo.getDynamicId(),i);
                dialog.initView();
            }
        });
        return view;
    }
    private String username = UserMessage.getInstance(context).getUserName();
    /**
     * 点赞
     * @param tsId
     * @param  dynamicId 动态id
     *  @param cancel 点赞或取消赞
     */
    public void personPraise(String tsId,String dynamicId,String cancel) {
        Map<String,Object> map=new HashMap<>();
        map.put("tsId",tsId);
        map.put("dynamicId",dynamicId);
        map.put("cancel",cancel);
        Type type=new TypeToken<Result<String>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.SUBPRAISE,map,this);
    }
    /**
     * 获取当前点击修改状态在数据的位置
     * @param statusVo
     */
    private int  getScrollPosition(StatusVo statusVo){
        for (int i = 0;i<statusFragement.statusVoList.size();i++){
            if (statusFragement.statusVoList.get(i).getDynamicId().equals(statusVo.getDynamicId())){
                return  i;
            }
        }
       return 1;
    }
    @Override
    public void onSuccess(String uri, Object date) {
        if (uri.equals(Apiurl.SUBPRAISE)){
            String result = ((Result<String>)date).getData();
            if (result.contains("成功")){
                Toast.makeText(context,result,Toast.LENGTH_SHORT).show();
                StatusAdapter.this.notifyDataSetChanged();
            }
        }
    }
    @Override
    public void onError(String uri, String error) {
        Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
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
        TextView tv_delete;//删除
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
            tv_delete = (TextView) view.findViewById(R.id.tv_delete);
            iv_comment = (ImageView)view.findViewById(R.id.iv_comment);
            rl_picture = (RelativeLayout) view.findViewById(R.id.rl_picture);
            view.setTag(this);
        }
    }
}
