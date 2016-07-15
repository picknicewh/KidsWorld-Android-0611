package net.hunme.school;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.hunme.baselibrary.activity.BaseFragement;
import net.hunme.baselibrary.widget.NavigationBar;

import butterknife.ButterKnife;

/**
 * 作者： Administrator
 * 时间： 2016/7/14
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class SchoolFragement extends BaseFragement {
    private NavigationBar navigationBar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_school, null);
        ButterKnife.bind(this, view);
        init(view);
        return view;
    }
    private  void init(View v){
        navigationBar = $(v,R.id.nb_school);
        navigationBar.setTitle("学校");

    }
}
