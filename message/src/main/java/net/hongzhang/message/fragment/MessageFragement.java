package net.hongzhang.message.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import net.hongzhang.baselibrary.base.BaseFragement;
import net.hongzhang.baselibrary.contract.GetContractData;
import net.hongzhang.baselibrary.contract.GetGroupData;
import net.hongzhang.baselibrary.contract.InitAllContractData;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.message.R;
import net.hongzhang.message.activity.ContractMemberActivity;
import net.hongzhang.message.activity.SearchActivity;
import net.hongzhang.message.ronglistener.MyConversationListBehaviorListener;
import net.hongzhang.message.ronglistener.MyReceiveMessageListener;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;

/**
 * 作者： wh
 * 时间： 2016/7/14
 * 名称：通讯首页
 * 版本说明：
 * 附加注释：三个融云的监听事件，点击头像点击
 * 主要接口：
 */
public class MessageFragement extends BaseFragement implements View.OnClickListener{
    /**
     * 消息页面
     */
    private static final int MESSAGE_PAGE = 0;
    /**
     * 联系人页面
     */
    private static final int CONTRACT_PAGE = 1;
    /**
     * 群组页面
     */
    private static final int GROUP_PAGE = 2;
    /**
     * 搜索
     */
    private ImageView iv_search;
    /**
     * 用户信息
     */
    private  UserMessage userMessage;

    /**
     * 指示器viewpager
     */
    private ViewPager viewPager;
    /**
     * 页面列表
     */
    private List<Fragment> fragmentlist;
    /**
     * 消息
     */
    private RadioGroup rg_choose;
    private RadioButton rb_message;
    private View line_message;
    /**
     * 联系人
     */
    private RadioButton rb_contract;
    private View line_contract;
    /**
     * 群组
     */
    private RadioButton rb_group;
    private View line_group;
    /**
     * 当前页面的位置
     */
    private int mPosition;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, null);
        init(view);
        RongIM.setConversationListBehaviorListener(new MyConversationListBehaviorListener(getActivity()));
        RongIM.setOnReceiveMessageListener(new MyReceiveMessageListener(getActivity()));
        return view;
    }
    /**
     * 初始化view
     * @param  v
     */
   public   void init(View v){
       iv_search = $(v,R.id.iv_search);
       viewPager = $(v,R.id.vp_exchange);
       rb_message = $(v,R.id.rb_message);
       rb_contract = $(v,R.id.rb_contract);
       rb_group = $(v,R.id.rb_group);
       line_message = $(v,R.id.line_message);
       line_group = $(v,R.id.line_group);
       line_contract = $(v,R.id.line_contract);
       rg_choose = $(v,R.id.rg_choose);
       setFrametDetails();
       iv_search.setOnClickListener(this);
       rb_group.setOnClickListener(this);
       rb_contract.setOnClickListener(this);
       rb_message.setOnClickListener(this);
    }
    /**
     * 设置页面
     * @param position 位置
     */
    private void setItemPage(int position){
        setLine(position);
        switch (position){
            case 0:
                rb_message.setChecked(true);
                rb_contract.setChecked(false);
                rb_group.setChecked(false);
                break;
            case 1:
                rb_message.setChecked(false);
                rb_contract.setChecked(true);
                rb_group.setChecked(false);
                break;
            case 2:
                rb_message.setChecked(false);
                rb_contract.setChecked(true);
                rb_group.setChecked(true);
                break;
        }
    }
    /**
     * 设置下划线
     * @param position 位置
     */
    private void setLine(int position){
        switch (position){
            case MESSAGE_PAGE:
                line_contract.setBackgroundColor(getResources().getColor(R.color.main_bg));
                line_group.setBackgroundColor(getResources().getColor(R.color.main_bg));
                line_message.setBackgroundColor(getResources().getColor(R.color.main_text_green));
                iv_search.setVisibility(View.GONE);
                mPosition = MESSAGE_PAGE;
                break;
            case CONTRACT_PAGE:
                line_contract.setBackgroundColor(getResources().getColor(R.color.main_text_green));
                line_group.setBackgroundColor(getResources().getColor(R.color.main_bg));
                line_message.setBackgroundColor(getResources().getColor(R.color.main_bg));
                iv_search.setVisibility(View.VISIBLE);
                iv_search.setImageResource(R.mipmap.search);
                mPosition = CONTRACT_PAGE;
                break;
            case GROUP_PAGE:
                line_contract.setBackgroundColor(getResources().getColor(R.color.main_bg));
                line_group.setBackgroundColor(getResources().getColor(R.color.main_text_green));
                line_message.setBackgroundColor(getResources().getColor(R.color.main_bg));
                iv_search.setVisibility(View.VISIBLE);
                iv_search.setImageResource(R.mipmap.ic_add);
                mPosition = GROUP_PAGE;
                break;
        }
        viewPager.setCurrentItem(position);
    }
    /**
     * 滑动界面
     */
    private void setFrametDetails() {
        fragmentlist = new ArrayList<>();
        ConverationListFragment converationListFragment = new ConverationListFragment();
        final ContractListFragment   contractListFragment = new ContractListFragment();
        GroupListFragment   groupListFragment = new GroupListFragment();
        fragmentlist.add(converationListFragment);
        fragmentlist.add(contractListFragment);
        fragmentlist.add(groupListFragment);
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public int getCount() {
                return fragmentlist.size();
            }
            @Override
            public Fragment getItem(int position) {
                return fragmentlist.get(position);
            }
        };
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                contractListFragment.sb_parent.getmTextDialog().setVisibility(View.GONE);
                contractListFragment.sb_parent.setBackgroundDrawable(new ColorDrawable(0x00000000));
            }
            @Override
            public void onPageSelected(int position) {
                setItemPage(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * 初始化界面数据
     */
    public void initData(){
        userMessage = UserMessage.getInstance(getActivity());
        initContract(userMessage);
    }
    /**
     * 初始化联系人
     * @param  userMessage 用户信息类
     */
    private void initContract(UserMessage userMessage){
        GetContractData getContractData = new GetContractData(getActivity());
        getContractData.getContractList(userMessage.getTsId());
        GetGroupData getGroupData = new GetGroupData(getActivity());
        getGroupData.getGroupList(userMessage.getTsId());
        //初始化所有联系人的数据
        InitAllContractData data = new InitAllContractData(getActivity());
        data.init();
    }
    @Override
    public void onResume() {
        super.onResume();
    //    initData();
        RongIM.setConversationListBehaviorListener(new MyConversationListBehaviorListener(getActivity()));
    }
    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId==R.id.rb_message){
          setLine(MESSAGE_PAGE);
        }else if (viewId==R.id.rb_contract){
         setLine(CONTRACT_PAGE);
        }else if (viewId==R.id.rb_group) {
          setLine(GROUP_PAGE);
        } else if (viewId==R.id.iv_search){
            Intent intent = new Intent();
            switch (mPosition){
               case CONTRACT_PAGE:
                   intent.setClass(getActivity(), SearchActivity.class);
                   startActivity(intent);
                   break;
               case GROUP_PAGE:
                   intent.setClass(getActivity(), ContractMemberActivity.class);
                   intent.putExtra("title","创建群聊");
                   intent.putExtra("type",0);
                   startActivity(intent);
                   break;
           }
         }
    }
}
