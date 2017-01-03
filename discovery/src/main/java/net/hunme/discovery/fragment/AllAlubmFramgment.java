package net.hunme.discovery.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import net.hunme.baselibrary.base.BaseFragement;
import net.hunme.baselibrary.widget.NoScrollListView;
import net.hunme.discovery.R;

/**
 * Created by wanghua on 2016/12/19.
 */
public class AllAlubmFramgment extends BaseFragement {
    /**
     * 专辑列表
     */
   private NoScrollListView noScrollListView;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_album,null);
        noScrollListView = $(view,R.id.lv_album);
        return view;
    }
}
