package net.hunme.status;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;

import net.hunme.baselibrary.activity.BaseFragement;
import net.hunme.user.activity.UserActivity;

/**
 * 作者： Administrator
 * 时间： 2016/7/14
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class StatusFragement extends BaseFragement {
    private ImageView iv_lift;
    private ImageView iv_right;
    private Spinner s_title;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status, null);
        initView(view);
        return view;
    }

    private void initView(View view){
        iv_lift=$(view,R.id.iv_left);
        iv_right=$(view,R.id.iv_right);
        s_title=$(view,R.id.s_title);
        setViewAction();
    }

    private void setViewAction(){
        iv_lift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UserActivity.class));
            }
        });
    }

}