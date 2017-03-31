package net.hongzhang.discovery.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import net.hongzhang.baselibrary.base.BaseFragement;
import net.hongzhang.discovery.R;
import net.hongzhang.discovery.adapter.AllAlbumListAdapter;
import net.hongzhang.discovery.modle.ThemeVo;
import net.hongzhang.discovery.presenter.ThemeListContract;
import net.hongzhang.discovery.presenter.ThemeListPresenter;

import java.util.List;

/**
 * Created by wanghua on 2016/12/19.
 */
public class ThemeListFragment extends BaseFragement implements ThemeListContract.View {
    /**
     * 专辑列表
     */
    private ListView listView;
    private TextView tv_nodata;
    /**
     * 数据处理
     */
    private ThemeListPresenter present;
    /**
     * 类型
     */
    private int type;
    /**
     * 页码数
     */
    private int pageNumber = 1;
    /**
     * 一页显示数据条数
     */
    private final static int pageSize = 10;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_album, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        listView = $(view, R.id.lv_album);
        tv_nodata = $(view, R.id.tv_nodata);
        present = new ThemeListPresenter(getActivity(), this);
        Bundle bundle = getArguments();
        type = bundle.getInt("type");
        present.getThemeList(type, pageSize, pageNumber);
    }

    @Override
    public void setThemeList(final List<ThemeVo> themeVos) {
        if (themeVos.size() != 0) {
            tv_nodata.setVisibility(View.GONE);
            AllAlbumListAdapter allAlbumAdapter = new AllAlbumListAdapter(getActivity(), themeVos);
            listView.setAdapter(allAlbumAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String themeId = themeVos.get(i).getThemeId();
                    String themeName = themeVos.get(i).getThemeName();
                    present.starThemeVoListActivity(themeId, themeName, type);
                }
            });
        } else {
            tv_nodata.setVisibility(View.VISIBLE);
        }

    }
}
