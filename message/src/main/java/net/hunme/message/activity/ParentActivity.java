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

import net.hunme.baselibrary.activity.BaseActivity;
import net.hunme.message.R;
import net.hunme.message.adapter.ContractAdapter;
import net.hunme.message.bean.GroupMemberBean;
import net.hunme.message.util.CharacterParser;
import net.hunme.message.util.PinyinComparator;
import net.hunme.message.widget.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

public class ParentActivity extends BaseActivity implements SectionIndexer {

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

    public static String[] userList;
    /**
     * 适配器
     */
   private ContractAdapter adapter;
    /**
     * 用户id
     */
    private List<String> userids;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);
        init();
        initList();
    }
    private void init(){
        setLiftImage(R.mipmap.ic_arrow_lift);
        setCententTitle(getIntent().getStringExtra("title"));
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
        setLiftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void initdata(){
        userList = new String[10];
        userids = new ArrayList<>();
        userList[0] = "王小二";
        userList[1] = "吴用";
        userList[2] = "周磊";
        userList[3] = "鹿晗";
        userList[4] = "刘涛";
        userList[5] = "郑爽";
        userList[6] = "刘德华";
        userList[7] = "大幂幂";
        userList[8] = "吴彦祖";
        userList[9] = "刘诗诗";
        for (int  i = 1;i <10;i++){
            userids.add("100"+i);
        }
        userids.add("1100");

    }
    private void initList() {
        initdata();
        SourceDateList = filledData(userList);
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
                    RongIM.getInstance().startConversation(
                            ParentActivity.this, Conversation.ConversationType.PRIVATE,
                           userids.get(position),SourceDateList.get(position).getName());
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
    private List<GroupMemberBean> filledData(String[] date) {
        List<GroupMemberBean> mSortList = new ArrayList<GroupMemberBean>();
        for (int i = 0; i < date.length; i++) {
            GroupMemberBean sortModel = new GroupMemberBean();
            sortModel.setName(date[i]);
            // 汉字转换成拼音
            String pinyin = characterParser.getSelling(date[i]);
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
}

