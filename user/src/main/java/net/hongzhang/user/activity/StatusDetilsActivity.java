package net.hongzhang.user.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.widget.MyAlertDialog;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.baselibrary.widget.CircleImageView;
import net.hongzhang.baselibrary.widget.LoadingDialog;
import net.hongzhang.baselibrary.widget.NoScrollListView;
import net.hongzhang.user.R;
import net.hongzhang.user.adapter.StatusDetilsAdapter;
import net.hongzhang.user.mode.StatusDetilsVo;
import net.hongzhang.user.util.PictrueUtils;
import net.hongzhang.user.util.StatusDetilsContract;
import net.hongzhang.user.util.StatusDetilsPresenter;
import net.hongzhang.user.widget.NineGridPictureLayout;
import net.hongzhang.user.widget.StatusCommentPopWindow;

import java.util.List;

public class StatusDetilsActivity extends BaseActivity implements StatusDetilsContract.View {
    /**
     * 头像
     */
    private CircleImageView cv_head;
    /**
     * 动态人姓名
     */
    private TextView tv_name;
    /**
     * 动态人身份
     */
    private TextView tv_id;
    /**
     * 动态内容
     */
    private TextView tv_content;
    /**
     * 动态时间
     */
    private TextView tv_time;
    /**
     * 点赞
     */
    private ImageView iv_praise;
    /**
     * 评论
     */
    private ImageView iv_comment;
    /**
     * 点赞页
     */
    private LinearLayout ll_praise;
    /**
     * 点赞列表
     */
    private TextView tv_praise_person;
    /**
     * 评论列表
     */
    private NoScrollListView lv_commmet;
    /**
     * 图片
     */
    private NineGridPictureLayout nine_picture;
    private RelativeLayout rl_picture;
    private StatusDetilsContract.Presenter presenter;
    private String tsId;
    private String dynamicId;
    private boolean isAgree;
    private LoadingDialog dialog;
    private ScrollView scrollView;
    private TextView tv_praise_num;
    private TextView tv_comment_num;
    private TextView tv_delete_content;
    private TextView tv_delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_detils);
        findId();
        onClickComment();
        onClickPraise();
        tsId= UserMessage.getInstance(this).getTsId();
        dynamicId=getIntent().getStringExtra("dynamicId");
        presenter= new StatusDetilsPresenter(this,tsId,dynamicId,this);
    }
    private void findId(){
        lv_commmet= (NoScrollListView) findViewById(R.id.lv_commment);
        cv_head= (CircleImageView) findViewById(R.id.cv_head);
        tv_name= (TextView) findViewById(R.id.tv_name);
        tv_id= (TextView) findViewById(R.id.tv_id);
        tv_content= (TextView) findViewById(R.id.tv_content);
        tv_time= (TextView) findViewById(R.id.tv_time);
        iv_praise= (ImageView) findViewById(R.id.iv_praise);
        iv_comment= (ImageView) findViewById(R.id.iv_comment);
        ll_praise= (LinearLayout) findViewById(R.id.ll_praise);
        tv_praise_person= (TextView) findViewById(R.id.tv_praise_person);
        rl_picture= (RelativeLayout) findViewById(R.id.rl_picture);
        scrollView= (ScrollView) findViewById(R.id.scrollView);
        tv_praise_num= (TextView) findViewById(R.id.tv_praise_num);
        tv_comment_num= (TextView) findViewById(R.id.tv_comment_num);
        tv_delete_content = (TextView)findViewById(R.id.tv_delete_content);
        tv_delete = (TextView)findViewById(R.id.tv_delete);
        tv_delete.setVisibility(View.GONE);
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setCententTitle("动态详情");
        setLiftOnClickClose();
    }

    @Override
    public void setHeadImageView(String url) {
        ImageCache.imageLoader(url,cv_head);
    }

    @Override
    public void setName(String name) {
        tv_name.setText(name);
    }

    @Override
    public void setId(String id) {
        if("1".equals(id)){
            tv_id.setText("学");
            tv_id.setBackgroundResource(R.drawable.user_study_selecter);
        }else{
            tv_id.setText("师");
            tv_id.setBackgroundResource(R.drawable.user_teach_selecter);
        }
    }
    @Override
    public void setContent(String content) {
        tv_content.setText(content);
    }

    @Override
    public void setTime(String time) {
    /*    if (time.length()>10){
            tv_time.setText(time.substring(0,10));
        }else {

        }*/
        tv_time.setText(time);

    }

    private void onClickPraise() {
        iv_praise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String agreeType;
                if(isAgree){
                    agreeType="2";
                } else{
                    agreeType="1";
                }

                presenter.personPraise(tsId,dynamicId,agreeType);
            }
        });
    }

    private void onClickComment() {
        iv_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopWindow(view,null);
            }
        });
        tv_comment_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopWindow(view,null);
            }
        });
    }

    @Override
    public void setlinPraiseVis(boolean isVis) {
        if(isVis)
            ll_praise.setVisibility(View.VISIBLE);
        else
            ll_praise.setVisibility(View.GONE);
    }

    @Override
    public void setPraisePerson(String person) {
        tv_praise_person.setText(person);
    }

    @Override
    public void setCommentList(final List<StatusDetilsVo.DynamidRewListBean> listBean) {
        StatusDetilsAdapter adapter =new StatusDetilsAdapter(this,listBean);
        lv_commmet.setAdapter(adapter);
        lv_commmet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {
                final StatusDetilsVo.DynamidRewListBean bean =listBean.get(i);
                if(UserMessage.getInstance(StatusDetilsActivity.this).getTsId().equals(bean.getTs_id())){
                    View coupons_view = LayoutInflater.from(StatusDetilsActivity.this).inflate(R.layout.alertdialog_message, null);
                    final AlertDialog alertDialog = MyAlertDialog.getDialog(coupons_view, StatusDetilsActivity.this,1);
                    Button pop_notrigst = (Button) coupons_view.findViewById(R.id.pop_notrigst);
                    Button pop_mastrigst = (Button) coupons_view.findViewById(R.id.pop_mastrigst);
                    TextView pop_title = (TextView) coupons_view.findViewById(R.id.tv_poptitle);
                    pop_mastrigst.setText("确认");
                    pop_title.setText("删除此条评论？");
                    pop_mastrigst.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            presenter.deleteComment(tsId,bean.getRew_id());
                            alertDialog.dismiss();
                            alertDialog.cancel();
                        }
                    });
                    pop_notrigst.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                            alertDialog.cancel();
                        }
                    });
                }else {
                    showPopWindow(view,bean);
                    int[] location = new int[2] ;
                    view.getLocationOnScreen(location);
//                    lv_commmet.smoothScrollBy(location[1],100);
                    scrollView.smoothScrollBy(0,location[1]-view.getHeight());
                }
            }
        });
    }

    @Override
    public void setImagePrasise(boolean isAgree) {
        this.isAgree = isAgree;
        if(isAgree)
            iv_praise.setImageResource(R.mipmap.ic_heat_on);
        else
            iv_praise.setImageResource(R.mipmap.ic_heat_off);
    }

    @Override
    public void showPopWindow(View view, final StatusDetilsVo.DynamidRewListBean bean) {
        final StatusCommentPopWindow popWindow = new StatusCommentPopWindow(this,R.layout.pop_comment_status);
        popWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        final View conentView= popWindow.getView();
        final EditText td_commnet= (EditText) conentView.findViewById(R.id.et_comment);
        TextView b_comment= (TextView) conentView.findViewById(R.id.b_comment);
        final String rewtype;
        final String rewtsId;
        if(null==bean){
            rewtype ="1";
            rewtsId="";
            td_commnet.setHint("说点什么吧...");
        } else{
            rewtype ="2";
            rewtsId=bean.getTs_id();
            td_commnet.setHint("回复"+bean.getTsName()+"：");
        }
        td_commnet.setFocusable(true);
        b_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content=td_commnet.getText().toString().trim();
                if(G.isEmteny(content)){
                    popWindow.dismiss();
                    return;
                }
                presenter.personComment(tsId,dynamicId,content,rewtsId, rewtype);

                popWindow.dismiss();
            }
        });
    }

    @Override
    public void setPictures(List<String> imageUrl) {
        new PictrueUtils().setPictrueLoad(this,imageUrl,rl_picture);
    }

    @Override
    public void setCommentVis(boolean isVis) {
        if(isVis)
            tv_content.setVisibility(View.VISIBLE);
        else
            tv_content.setVisibility(View.GONE);
    }

    @Override
    public void setImageVis(boolean isVis) {
        if(isVis)
            rl_picture.setVisibility(View.VISIBLE);
        else
            rl_picture.setVisibility(View.GONE);
    }

    @Override
    public void setPiaiseNum(int piaiseNum) {
        if(piaiseNum>0)
            tv_praise_num.setText(String.valueOf(piaiseNum));
        else
            tv_praise_num.setText("赞");
    }

    @Override
    public void setCommentNum(int commentNum) {
        if(commentNum>0)
            tv_comment_num.setText(String.valueOf(commentNum));
        else
            tv_comment_num.setText("评论");
    }

    @Override
    public void setDeleteView(boolean isDelete) {
        if (isDelete){
            tv_delete_content.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        }else {
            tv_delete_content.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showLoadingDialog() {
        if(dialog==null)
            dialog=new LoadingDialog(this,R.style.LoadingDialogTheme);
        dialog.show();
        dialog.setCancelable(true);
        dialog.setLoadingText("数据加载中...");
    }

    @Override
    public void stopLoadingDialog() {
        dialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dialog.cancel();
    }
}
