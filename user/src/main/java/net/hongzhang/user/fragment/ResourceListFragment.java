package net.hongzhang.user.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.hongzhang.baselibrary.base.BaseFragement;
import net.hongzhang.baselibrary.mode.ResourceVo;
import net.hongzhang.baselibrary.widget.NoScrollGirdView;
import net.hongzhang.user.R;
import net.hongzhang.user.adapter.ResourceAdapter;
import net.hongzhang.user.mode.CompilationsVo;
import net.hongzhang.user.util.MyResourcePresent;
import net.hongzhang.user.util.ResourceContract;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： wh
 * 时间： 2016/11/25
 * 名称：我的收藏和园所记录的我的听听和幼儿课堂页面
 * 版本说明：
 * 附加注释：通过传递不同的source值来判断是哪个位置，再根据传递的不同type值来决定是幼儿课堂还是幼儿听听
 * 主要接口：
 */
public class ResourceListFragment extends BaseFragement implements ResourceContract.View, View.OnClickListener {
    /**
     * 资源列表显示控件
     */
    private NoScrollGirdView gv_list;
    /**
     * 没有资源
     */
    private TextView tv_nodata;
    /**
     * 分页加载，加载更多
     */
    private LinearLayout ll_load_more;
    /**
     * 显示是否有数据
     */
    private TextView tv_load_more;
    /**
     * 显示数据控件
     */
    private ImageView iv_load_more;
    /**
     * 数据资源列表
     */
    private List<ResourceVo> resourceManagerVoList;
    /**
     * 专辑资源列表
     */
    private List<CompilationsVo> compilationsVoList;
    /**
     * 资源适配器
     */
    private ResourceAdapter adapter;
    /**
     * 页面数
     */
    private int pageNumber = 1;
    /**
     * source=0收藏 source = 1记录
     */
    private int source;
    /**
     * 数据处理类
     */
    private MyResourcePresent myResourcePresent;
    /**
     * 一页资源条数
     */
    private int pageSize = 10;
    /**
     * 资源类型 1 视频 2 音乐
     */
    private int type;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resource_list, null);
        initView(view);
        return view;
    }

    /**
     * 初始化控件
     *
     * @param view
     */
    private void initView(View view) {
        gv_list = $(view, R.id.gv_resource_list);
        tv_nodata = $(view, R.id.tv_nodata);
        ll_load_more = $(view, R.id.ll_load_more);
        tv_load_more = $(view, R.id.tv_load_more);
        iv_load_more = $(view, R.id.iv_load_more);
        ll_load_more.setOnClickListener(this);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Bundle bundle = getArguments();
        source = bundle.getInt("source");
        type = bundle.getInt("type");
        myResourcePresent = new MyResourcePresent(getContext(), this, source, type);
        if (source == 0) {
            compilationsVoList = new ArrayList<>();
        } else if (source == 1) {
            resourceManagerVoList = new ArrayList<>();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ll_load_more) {
            pageNumber++;
            if (source == 0) {
                myResourcePresent.getCollectResourceList(pageNumber, pageSize, type);
            } else if (source == 1) {
                myResourcePresent.getPlayRecordList(pageNumber, pageSize, type);
            }
        }
    }

    /**
     * 显示加载dialog
     */
    @Override
    public void showLoadingDialog() {
        super.showLoadingDialog();

    }

    /**
     * 停止加载dialog
     */
    @Override
    public void stopLoadingDialog() {
        super.stopLoadingDialog();
    }

    /**
     * 设置根据资源列表
     *
     * @param resourceVoList  数据资源列表
     * @param compilationsVos 专辑资源列表
     */
    @Override
    public void setResourceVoList(final List<ResourceVo> resourceVoList, final List<CompilationsVo> compilationsVos) {
        this.resourceManagerVoList = resourceVoList;
        this.compilationsVoList = compilationsVos;
        if (source == 0) {
            adapter = new ResourceAdapter(getActivity(), compilationsVos, resourceVoList);
        } else if (source == 1) {
            adapter = new ResourceAdapter(getActivity(), compilationsVos, resourceVoList);
        }
        gv_list.setAdapter(adapter);
        gv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (source == 0) {
                    CompilationsVo compilationsVo = compilationsVos.get(i);
                    String themeId = compilationsVo.getAlbumId();
                    if (type == 1) {
                        myResourcePresent.starVedioActivity(themeId);
                    } else {

                        myResourcePresent.startMusicActivity(themeId, null);
                    }
                } else if (source == 1) {
                    ResourceVo resourceVo = resourceVoList.get(i);
                    if (type == 1) {
                        myResourcePresent.starVedioActivity(resourceVo.getAlbumId());
                    } else {
                        myResourcePresent.startMusicActivity(resourceVo.getAlbumId(), resourceVo.getResourceId());
                    }

                }
            }
        });
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 根据每次加载资源列表的数据做出相应的处理
     *
     * @param size 每次加载更多，获取数据的条数
     *             size==0 说明上一页数据是10条，本页数据为0条，那么，加载更多的话，加载数据前一页乘以页面数据
     *             size<10 说明上一页数据是10条 ，本页数据不满10条那么本页是最后一页，显示相应得加载更多得数据形式
     */
    @Override
    public void setResourceSize(int size) {
        if (size == 0) {
            if (source == 0) {
                if (compilationsVoList.size() > 0) {
                    tv_nodata.setVisibility(View.GONE);
                } else {
                    tv_nodata.setVisibility(View.VISIBLE);
                    tv_nodata.setText("你还没有收藏哦，快去收藏吧！");
                }
            } else {
                if (resourceManagerVoList.size() > 0) {
                    tv_nodata.setVisibility(View.GONE);
                } else {
                    tv_nodata.setVisibility(View.VISIBLE);
                    tv_nodata.setText("你还没有足迹哦，快去留下足迹吧！");
                }
            }
            lastPage();
        } else {
            if (size < 10) {
                lastPage();
            }
            tv_nodata.setVisibility(View.GONE);
        }
    }

    private void lastPage() {
        ll_load_more.setVisibility(View.VISIBLE);
        tv_load_more.setText("没有更多数据了");
        iv_load_more.setVisibility(View.GONE);
        ll_load_more.setClickable(false);
    }
}
