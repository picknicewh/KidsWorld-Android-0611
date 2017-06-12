package net.hongzhang.school.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.mode.ClassVo;
import net.hongzhang.baselibrary.util.DateUtil;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.school.R;
import net.hongzhang.school.adapter.MedicineDateAdapter;
import net.hongzhang.school.adapter.RecipesListAdapter;
import net.hongzhang.school.bean.RecipesVo;
import net.hongzhang.school.presenter.RecipesListContract;
import net.hongzhang.school.presenter.RecipesListPresenter;

import java.util.ArrayList;
import java.util.List;

public class RecipesListActivity extends BaseActivity implements RecipesListContract.View, View.OnClickListener {
    /**
     * 日期
     */
    private RecyclerView rv_class;
    /**
     * 食谱列表
     */
    private RecyclerView rv_food;
    private RecipesListAdapter adapter;
    private RecipesListPresenter presenter;
    private List<RecipesVo.CBInfo> cbInfoList;
    private List<String> classVoList;
    private UserMessage userMessage;
    private MedicineDateAdapter dateAdapter;
    private String date;
    private TextView tv_nodata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_list);
        initView();
    }

    @Override
    protected void setToolBar() {
        date = DateUtil.DATE_MOUTH.format(System.currentTimeMillis());
        setCententTitle(date);
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        userMessage = new UserMessage(this);
        if (userMessage.getType().equals("3")) {
            //  setSubTitle("发布食谱");
        }
        setSubTitle("发布食谱");
        setSubTitleOnClickListener(this);
    }

    private void initView() {

        rv_class = $(R.id.rv_class);
        rv_food = $(R.id.rv_food);
        tv_nodata = $(R.id.tv_nodata);
        initData();
    }

    private void initData() {

        cbInfoList = new ArrayList<>();
        classVoList = new ArrayList<>();
        adapter = new RecipesListAdapter(this, cbInfoList);
        dateAdapter = new MedicineDateAdapter(this, classVoList);
        rv_class.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rv_class.setAdapter(dateAdapter);
        rv_food.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv_food.setAdapter(adapter);
        presenter = new RecipesListPresenter(RecipesListActivity.this, this);
        presenter.getClassList(userMessage.getTsId());


    }

    @Override
    public void setClassList(List<ClassVo> classVos) {
        classVoList.clear();
        for (int i = 0; i < classVos.size(); i++) {
            ClassVo classVo = classVos.get(i);
            classVoList.add(classVo.getGroupName());
        }
        if (dateAdapter != null) {
            dateAdapter.notifyDataSetChanged();
        }
        if (classVoList.size() > 0) {
            classId = classVos.get(0).getGroupId();
            presenter.getRecipesList(classId);
        }
        dateAdapter.setOnItemClickListener(new MedicineDateAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                classId = classVoList.get(position);
                presenter.getRecipesList(classId);
            }
        });
    }

    private String classId;

    @Override
    public void setRecipesVoList(List<RecipesVo> recipesVos) {
        cbInfoList.clear();
        for (int i = 0; i < recipesVos.size(); i++) {
            if (classId.equals(recipesVos.get(i).getClass_id())) {
                cbInfoList.addAll(recipesVos.get(i).getCb_info_list());
            }
        }
        tv_nodata.setVisibility(cbInfoList.size() == 0 ? View.VISIBLE : View.GONE);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_subtitle) {
            Intent intent = new Intent(this, SubmitRecipesActivity.class);
            intent.putExtra("classId", classId);
            intent.putExtra("date", date);
            startActivity(intent);
        }
    }
}
