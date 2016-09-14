package net.hunme.message.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.baselibrary.mode.Result;
import net.hunme.baselibrary.network.Apiurl;
import net.hunme.baselibrary.network.OkHttpListener;
import net.hunme.baselibrary.network.OkHttps;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.message.R;
import net.hunme.message.adapter.ContractAdapter;
import net.hunme.message.bean.GroupInfoVo;
import net.hunme.message.bean.GroupMemberBean;
import net.hunme.message.bean.ContractInfoVo;
import net.hunme.message.util.CharacterParser;
import net.hunme.message.util.PinyinComparator;
import net.hunme.message.widget.SideBar;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;

/**
 * 作者： wh
 * 时间： 2016/7/15
 * 名称：通讯首页--老师+家长(联系人列表显示)
 * 版本说明：
 * 附加注释：通过传递不同的联系人列表分别显示老师，家长两个页面
 * 主要接口：
 */
public class ParentActivity extends BaseActivity implements SectionIndexer,OkHttpListener {

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
    private SideBar sb_parent;
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

    public static List<String> usernames;
    /**
     * 适配器
     */
    private ContractAdapter adapter;
    /**
     * 用户id
     */
    private List<String> userids;

    /**
     * 标题
     */
    private String title;
    /**
     * 类型
     */
    private int type;
    /**
     * 是否选中对话框的item
     */
    private HashMap<Integer, Boolean> isSelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);
        init();
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        title = getIntent().getStringExtra("title");
        setCententTitle(title);
        setLiftOnClickClose();
        setSubTitle("确定");
    }
    private void init(){
        type = getIntent().getIntExtra("type",0);
        getfriendinfor(type);
        lv_parent = $(R.id.lv_parent);
        tv_noparent = $(R.id.tv_no_parent);
        tv_title_cat = $(R.id.tv_title_cat);
        tv_dialog_parent =  $(R.id.tv_dialog_parent);
        ll_title = $(R.id.ll_title_parent);
        sb_parent = $(R.id.sb_parent);
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        groupMemberBeanList = new ArrayList<>();
        sb_parent.setTextView(tv_dialog_parent);
        setLiftOnClickClose();
    }
    /**
     * 获取所有所有好友信息
     * @param  mtype 类型
     */
    private  void getfriendinfor(int mtype){
        String dbname;
        Map<String,Object> params = new HashMap<>();
        params.put("tsId",UserMessage.getInstance(this).getTsId());
        //1=群，2=老师，3=家长
        params.put("type",mtype);
        dbname  = "contract_teacher";
        Type type =new TypeToken<Result<List<GroupInfoVo>>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.MESSAGE_GETGTOUP,params,this,2,dbname);
    }
    /**
     * 显示列表
     */
    private void initList() {
        Collections.sort(groupMemberBeanList, pinyinComparator);
        adapter = new ContractAdapter(this, groupMemberBeanList,type);
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
                    tv_title_cat.setText(groupMemberBeanList.get(
                            getPositionForSection(section)).getSortLetters());
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
                        Intent intent  = new Intent(ParentActivity.this,PersonDetailActivity.class);
                        intent.putExtra("targetId",uid);
                        intent.putExtra("title",name);
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
        return groupMemberBeanList.get(position).getSortLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
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
     * @param  groupJsonList 数据列表
     */
    private void setFriendList(List<GroupInfoVo> groupJsonList){
        List<ContractInfoVo> memberJsons = groupJsonList.get(0).getMenberList();
        if(null==memberJsons){
            return;
        }
        for (int i = 0;i<memberJsons.size();i++){
            ContractInfoVo memberJson = memberJsons.get(i);
            GroupMemberBean groupMemberBean = MemberJsonnTGroupMember(memberJson);
            groupMemberBeanList.add(groupMemberBean);
        }
    }
    /**
     * 将MemberJson转换成GroupMemberBean
     * @param  memberJson 实体类
     */
    private GroupMemberBean MemberJsonnTGroupMember(ContractInfoVo memberJson){
        GroupMemberBean groupMember =new GroupMemberBean();
        groupMember.setName(memberJson.getTsName());
        groupMember.setUserid(memberJson.getTsId());
        groupMember.setImg(memberJson.getImg());
        // 汉字转换成拼音
        String pinyin = characterParser.getSelling(memberJson.getTsName());
        String sortString = pinyin.substring(0, 1).toUpperCase();
        // 正则表达式，判断首字母是否是英文字母
        if (sortString.matches("[A-Z]")) {
            groupMember.setSortLetters(sortString.toUpperCase());
        } else {
            groupMember.setSortLetters("#");
        }
        return groupMember;
    }
    @Override
    public void onSuccess(String uri, Object date) {
        Result<List<GroupInfoVo>> data = (Result<List<GroupInfoVo>>) date;
        if (data!=null){
            List<GroupInfoVo>  groupJsonList = data.getData();
            if (groupJsonList!=null||groupJsonList.size()!=0){
                setFriendList(groupJsonList);
                initList();
            }
        }
    }

    @Override
    public void onError(String uri, String error) {
        Toast.makeText(getApplicationContext(),error,Toast.LENGTH_SHORT).show();
    }
}

