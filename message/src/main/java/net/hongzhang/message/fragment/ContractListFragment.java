package net.hongzhang.message.fragment;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import net.hongzhang.baselibrary.base.BaseFragement;
import net.hongzhang.baselibrary.contract.ContractsDb;
import net.hongzhang.baselibrary.contract.ContractsDbHelper;
import net.hongzhang.baselibrary.mode.ContractInfoVo;
import net.hongzhang.message.R;
import net.hongzhang.message.activity.PersonDetailActivity;
import net.hongzhang.message.adapter.ContractAdapter;
import net.hongzhang.message.bean.GroupMemberBean;
import net.hongzhang.message.util.CharacterParser;
import net.hongzhang.message.util.PinyinComparator;
import net.hongzhang.message.widget.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.rong.imkit.RongIM;

/**
 * 作者： wh
 * 时间： 2016/7/15
 * 名称：通讯首页--老师+家长(联系人列表显示)
 * 版本说明：
 * 附加注释：通过传递不同的联系人列表分别显示老师，家长两个页面
 * 主要接口：
 */
public class ContractListFragment extends BaseFragement implements SectionIndexer{

    /**
     * 教师列表view
     */
    private ListView lv_parent;
    /**
     * 没有教师显示
     */
    private TextView tv_noparent;
    /**
     * 显示的标题
     */
    private LinearLayout ll_title;
    /**
     * 显示大写字母
     */
    private TextView tv_title_cat;
    /**
     * 字母对话框
     */
    private TextView tv_dialog_parent;
    /**
     * 字母列表控件
     */
    public  SideBar sb_parent;
    /**
     * 上次第一个可见元素，用于滚动时记录标识。
     */
    private int lastFirstVisibleItem = -1;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private List<GroupMemberBean> groupMemberBeanList;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;
    /**
     * 适配器
     */
    private ContractAdapter adapter;
    /**
     * 数据库
     */
     private SQLiteDatabase database;
    private ContractsDbHelper helper;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_contractlist,null);
        init(view);
        return view;
    }

    /**
     * 初始化数据
     */
    private void init(View view){
        lv_parent = $(view,R.id.lv_parent);
        tv_noparent = $(view,R.id.tv_no_parent);
        tv_title_cat = $(view,R.id.tv_title_cat);
        tv_dialog_parent =  $(view,R.id.tv_dialog_parent);
        ll_title = $(view,R.id.ll_title_parent);
        sb_parent = $(view,R.id.sb_parent);
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        sb_parent.setTextView(tv_dialog_parent);
        groupMemberBeanList = new ArrayList<>();
        database = new ContractsDb(getActivity()).getWritableDatabase();
        helper = ContractsDbHelper.getinstance();
        List<ContractInfoVo> contractInfoVoa=  helper.getFriendInformVos(database);
        setFriendList(contractInfoVoa);
        initList();
        //  getfriendinfor()
    }
  /*  *//**
     * 获取所有所有好友信息
     *//*
    private  void getfriendinfor(){
        String dbname;
        Map<String,Object> params = new HashMap<>();
        params.put("tsId",UserMessage.getInstance(getActivity()).getTsId());
        //1=群，2=老师，3=家长
        params.put("type",0);
        dbname  = "contract_teacher";
        Type type =new TypeToken<Result<List<GroupInfoVo>>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.MESSAGE_GETGTOUP,params,this,2,dbname);
    }*/
    /**
     * 显示列表
     */
    private void initList() {
        Collections.sort(groupMemberBeanList, pinyinComparator);
        adapter = new ContractAdapter(getActivity(), groupMemberBeanList);
        lv_parent.setAdapter(adapter);
        // 设置右侧触摸监听
        sb_parent.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    lv_parent.setSelection(position);
                }
            }
        });
        itemclick();
        itemScroll();
    }
    /**
     * 列表滑动事件
     */
    private void itemScroll(){
        lv_parent.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                int section = getSectionForPosition(firstVisibleItem);
                int nextSection = getSectionForPosition(firstVisibleItem + 1);
                int nextSecPosition = getPositionForSection(+nextSection);
                if (firstVisibleItem != lastFirstVisibleItem) {
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) ll_title
                            .getLayoutParams();
                    params.topMargin = 0;
                    ll_title.setLayoutParams(params);
                    if (groupMemberBeanList.size()>0&&groupMemberBeanList!=null){
                        tv_title_cat.setText(groupMemberBeanList.get(
                                getPositionForSection(section)).getSortLetters());
                    }
                }
                if (nextSecPosition == firstVisibleItem + 1) {
                    View childView = view.getChildAt(0);
                    if (childView != null) {
                        int titleHeight = ll_title.getHeight();
                        int bottom = childView.getBottom();
                        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) ll_title
                                .getLayoutParams();
                        if (bottom < titleHeight) {
                            float pushedDistance = bottom - titleHeight;
                            params.topMargin = (int) pushedDistance;
                            ll_title.setLayoutParams(params);
                        } else {
                            if (params.topMargin != 0) {
                                params.topMargin = 0;
                                ll_title.setLayoutParams(params);
                            }
                        }
                    }
                }
                lastFirstVisibleItem = firstVisibleItem;
            }
        });
    }
    /**
     * 列表点击事件
     */
    private  void itemclick(){
        lv_parent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    GroupMemberBean bean = groupMemberBeanList.get(position);
                    final String uid  = bean.getUserid();
                    final  String name = bean.getName();
                    if (RongIM.getInstance()!=null){
                        Intent intent  = new Intent(getActivity(),PersonDetailActivity.class);
                        intent.putExtra("targetId",uid);
                        //intent.putExtra("title",name);
                        startActivity(intent);
                    }
                }
        });

    }
    @Override
    public Object[] getSections() {
        return null;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        if (groupMemberBeanList!=null&&groupMemberBeanList.size()>0){
            return groupMemberBeanList.get(position).getSortLetters().charAt(0);
        }
        return 0;
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     * @param  section 位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < groupMemberBeanList.size(); i++) {
            String sortStr = groupMemberBeanList.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }
    /**
     * 设置好友列表
     * @param  ContractInfoVos 数据列表
     */
    private void setFriendList(List<ContractInfoVo> ContractInfoVos){
        if(null==ContractInfoVos){
            return;
        }
        for (int i = 0;i<ContractInfoVos.size();i++){
            ContractInfoVo memberJson = ContractInfoVos.get(i);
            GroupMemberBean groupMemberBean = MemberJsonnTGroupMember(memberJson);
            groupMemberBeanList.add(groupMemberBean);
        }
    }
    /**
     * 将MemberJson转换成GroupMemberBean
     * @param  contractInfoVo 实体类
     */
    private GroupMemberBean MemberJsonnTGroupMember(ContractInfoVo contractInfoVo){
        GroupMemberBean groupMember =new GroupMemberBean();
        groupMember.setName(contractInfoVo.getTsName());
        groupMember.setUserid(contractInfoVo.getTsId());
        groupMember.setImg(contractInfoVo.getImg());
        // 汉字转换成拼音
        String pinyin = characterParser.getSelling(contractInfoVo.getTsName());
        String sortString = pinyin.substring(0, 1).toUpperCase();
        // 正则表达式，判断首字母是否是英文字母
        if (sortString.matches("[A-Z]")) {
            groupMember.setSortLetters(sortString.toUpperCase());
        } else {
            groupMember.setSortLetters("#");
        }
        return groupMember;
    }
  /*  @Override
    public void onSuccess(String uri, Object date) {
        Result<List<GroupInfoVo>> data = (Result<List<GroupInfoVo>>) date;
        if (data!=null){
            List<GroupInfoVo>  groupJsonList = data.getData();
            if (groupJsonList!=null||groupJsonList.size()!=0){
                setFriendList(groupJsonList.get(0).getMenberList());
                initList();
            }
        }
    }

    @Override
    public void onError(String uri, String error) {
        Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
    }*/
}

