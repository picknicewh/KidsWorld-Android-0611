package net.hunme.message.activity;

import android.os.Bundle;
import android.util.Log;
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
import net.hunme.message.R;
import net.hunme.message.adapter.ContractAdapter;
import net.hunme.message.bean.GroupJson;
import net.hunme.message.bean.GroupMemberBean;
import net.hunme.message.bean.MemberJson;
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
import io.rong.imlib.model.Conversation;

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
    private List<GroupMemberBean> SourceDateList;

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
    private List<GroupMemberBean> groupMemberBeanList;
    /**
     * 标题
     */
    private String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);
        init();
        initList();
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        title = getIntent().getStringExtra("title");
        setCententTitle(title);
        setLiftOnClickClose();
    }
    private void init(){
      //  getfriendinfor(title);
        lv_parent = $(R.id.lv_parent);
        tv_noparent = $(R.id.tv_no_parent);
        tv_title_cat = $(R.id.tv_title_cat);
        tv_dialog_parent =  $(R.id.tv_dialog_parent);
        ll_title = $(R.id.ll_title_parent);
        sb_parent = $(R.id.sb_parent);
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        SourceDateList = new ArrayList<>();
        sb_parent.setTextView(tv_dialog_parent);
        setLiftOnClickClose();
    }
    private void initdata(){
        groupMemberBeanList = new ArrayList<>();
        GroupMemberBean groupMemberBean1 = new GroupMemberBean();
        groupMemberBean1.setName("王小二");
        groupMemberBean1.setUserid("1001");
        groupMemberBeanList.add(groupMemberBean1);
        GroupMemberBean groupMemberBean2 = new GroupMemberBean();
        groupMemberBean2.setName("刘德华");
        groupMemberBean2.setUserid("1002");
        groupMemberBeanList.add(groupMemberBean2);
        GroupMemberBean groupMemberBean3 = new GroupMemberBean();
        groupMemberBean3.setName("吴亦凡");
        groupMemberBean3.setUserid("1003");
        groupMemberBeanList.add(groupMemberBean3);
        GroupMemberBean groupMemberBean4 = new GroupMemberBean();
        groupMemberBean4.setName("吴用");
        groupMemberBean4.setUserid("1001");
        groupMemberBeanList.add(groupMemberBean4);
        GroupMemberBean groupMemberBean5 = new GroupMemberBean();
        groupMemberBean5.setName("周磊");
        groupMemberBean5.setUserid("1005");
        groupMemberBeanList.add(groupMemberBean5);
        GroupMemberBean groupMemberBean6 = new GroupMemberBean();
        groupMemberBean6.setName("鹿晗");
        groupMemberBean6.setUserid("1006");
        groupMemberBeanList.add(groupMemberBean6);
        GroupMemberBean groupMemberBean7 = new GroupMemberBean();
        groupMemberBean7.setName("郑爽");
        groupMemberBean7.setUserid("1007");
        groupMemberBeanList.add(groupMemberBean7);
        GroupMemberBean groupMemberBean8 = new GroupMemberBean();
        groupMemberBean8.setName("大幂幂");
        groupMemberBean8.setUserid("1008");
        groupMemberBeanList.add(groupMemberBean8);
        GroupMemberBean groupMemberBean9 = new GroupMemberBean();
        groupMemberBean9.setName("吴彦祖");
        groupMemberBean9.setUserid("1009");
        groupMemberBeanList.add(groupMemberBean9);
        GroupMemberBean groupMemberBean = new GroupMemberBean();
        groupMemberBean.setName("刘诗诗");
        groupMemberBean.setUserid("1100");
        groupMemberBeanList.add(groupMemberBean);

    }

    /**
     * 获取所有所有好友信息
     */
    private  void getfriendinfor(String title){
        Map<String,Object> params = new HashMap<>();
        params.put("tsId","1001");
        //1=群，2=老师，3=家长
        if (title.equals("教师")){
            params.put("type","2");
        }else {
            params.put("type","3");
        }
        Type type =new TypeToken<Result<GroupJson>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.MESSAGE_GETGTOUP,params,this);
    }

    private void initList() {
        initdata();
        SourceDateList = filledData(groupMemberBeanList);
        Collections.sort(SourceDateList, pinyinComparator);
        adapter = new ContractAdapter(this, SourceDateList);
        //  adapter.isCrateGroup(createGroup);
        lv_parent.setAdapter(adapter);
        //isSelected = adapter.getIsSelected();
        // 设置右侧触摸监听
        sb_parent.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                Log.i("TAVGF",position+"");
                if (position != -1) {
                    lv_parent.setSelection(position);
                }
            }
        });
        lv_parent.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (RongIM.getInstance()!=null){
                    Log.i("TDDAG", SourceDateList.get(position).getUserid());
                    RongIM.getInstance().startConversation(
                            ParentActivity.this, Conversation.ConversationType.PRIVATE,
                           SourceDateList.get(position).getUserid(),SourceDateList.get(position).getName());

                }
            }
        });
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
                    tv_title_cat.setText(SourceDateList.get(
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
     * 为ListView填充数据
     *
     * @param date
     * @return
     */
    private List<GroupMemberBean> filledData(List<GroupMemberBean> date) {
        List<GroupMemberBean> mSortList = new ArrayList<GroupMemberBean>();
        for (int i = 0; i < date.size(); i++) {
            GroupMemberBean sortModel = new GroupMemberBean();
            sortModel.setUserid(date.get(i).getUserid());
            sortModel.setName(date.get(i).getName());
            // 汉字转换成拼音
            String pinyin = characterParser.getSelling(date.get(i).getName());
            String sortString = pinyin.substring(0, 1).toUpperCase();
            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);

        }
        return mSortList;
    }
    @Override
    public Object[] getSections() {
        return null;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return SourceDateList.get(position).getSortLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < SourceDateList.size(); i++) {
            String sortStr = SourceDateList.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }
   private void setFriendList(List<GroupJson> groupJsonList, List<GroupMemberBean> groupMemberList){
       List<MemberJson> memberJsons = groupJsonList.get(0).getMenberList();
       for (int i = 0;i<memberJsons.size();i++){
           MemberJson memberJson = memberJsons.get(0);
           GroupMemberBean groupMemberBean = MemberJsonnTGroupMember(memberJson);
           groupMemberList.add(groupMemberBean);
       }
   }
    private GroupMemberBean MemberJsonnTGroupMember(MemberJson memberJson){
        GroupMemberBean groupMember =null;
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
        List<GroupJson> groupJsonList = (List<GroupJson>) date;
        if (groupJsonList!=null||groupJsonList.size()!=0){
            setFriendList(groupJsonList,SourceDateList);
        }
    }

    @Override
    public void onError(String uri, String error) {
        Log.i("TAG",error);
        Toast.makeText(getApplicationContext(),error,Toast.LENGTH_SHORT).show();
    }
}

