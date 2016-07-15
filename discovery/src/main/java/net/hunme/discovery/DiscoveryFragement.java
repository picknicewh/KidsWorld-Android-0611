package net.hunme.discovery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.hunme.baselibrary.activity.BaseFragement;
import net.hunme.baselibrary.widget.NavigationBar;

/**
 * 作者： Administrator
 * 时间： 2016/7/14
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class DiscoveryFragement extends BaseFragement {
    private NavigationBar navigationBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discovery,null);
        init(view);
        return view;
    }
    private  void init(View v){
        navigationBar = $(v,R.id.nb_descovery);
        navigationBar.setTitle("学校");

    }
}