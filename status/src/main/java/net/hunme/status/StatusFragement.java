package net.hunme.status;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.hunme.baselibrary.base.BaseFragement;
import net.hunme.baselibrary.widget.CircleImageView;
import net.hunme.status.widget.ChooseClassPopWindow;
import net.hunme.status.widget.StatusPublishPopWindow;
import net.hunme.user.activity.UserActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： wh
 * 时间： 2016/7/14
 * 名称：动态首页
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class StatusFragement extends BaseFragement implements View.OnClickListener{
    private CircleImageView iv_lift;
    private ImageView iv_right;
    private TextView tv_classname;
    private List<String> classlist ;
    private ChooseClassPopWindow popWindow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status, null);
        initView(view);
        return view;
    }

    private void initView(View view){
        iv_lift=$(view,R.id.iv_left);
        iv_right=$(view,R.id.iv_right);
        tv_classname=$(view,R.id.tv_classname);
        setViewAction();
        classlist = new ArrayList<>();
        classlist.add("一(1)班");
        classlist.add("一(2)班");
        classlist.add("一(3)班");
        popWindow = new ChooseClassPopWindow(this,classlist);
        tv_classname.setOnClickListener(this);

    }
    private void setViewAction(){
        iv_lift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UserActivity.class));
            }
        });
        iv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final StatusPublishPopWindow pubishPopWindow = new StatusPublishPopWindow(getActivity());
                pubishPopWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                pubishPopWindow.getContentView().setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            pubishPopWindow.dismiss();
                        }
                    }
                });
            }
        });
    }
   public void setClassname(String classname){
       tv_classname.setText(classname);
   }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId==R.id.tv_classname){
            popWindow.showAtLocation(view, Gravity.CENTER_HORIZONTAL,0,0);
            popWindow.getContentView().setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        popWindow.dismiss();
                    }
                }
            });
        }
    }
}
