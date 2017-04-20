package net.hongzhang.school.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.school.R;
import net.hongzhang.school.adapter.MyHomeInteractionAdapter;

import butterknife.ButterKnife;

public class HomeInteractionActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView rvEducation;
    private RecyclerView rvConservation;
    private TextView tvRelease;
    private TextView tvViewState;
    private TextView tvEductionTitle;
    private TextView tvConservationTitle;
    private MyHomeInteractionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_interaction);
        ButterKnife.bind(this);
        initView();
        initViewLister();
    }

    @Override
    protected void setToolBar() {
        setLiftOnClickClose();
        setCententTitle("家园互动");
    }

    private void initView() {
        rvConservation = (RecyclerView) findViewById(R.id.rv_conservation);
        rvEducation = (RecyclerView) findViewById(R.id.rv_education);
        tvRelease = (TextView) findViewById(R.id.tv_release);
        tvViewState= (TextView) findViewById(R.id.tv_view_state);
        tvEductionTitle = (TextView) findViewById(R.id.tv_eduction_title);
        tvConservationTitle = (TextView) findViewById(R.id.tv_conservation_title);
        rvConservation.setNestedScrollingEnabled(false);
        rvEducation.setNestedScrollingEnabled(false);
        rvConservation.setLayoutManager(new LinearLayoutManager(this));
        rvEducation.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyHomeInteractionAdapter(true);
        rvEducation.setAdapter(adapter);
        rvConservation.setAdapter(adapter);
    }

    private void initViewLister() {
        tvRelease.setOnClickListener(this);
        tvEductionTitle.setOnClickListener(this);
        tvConservationTitle.setOnClickListener(this);
        tvViewState.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_release) {
            startActivity(new Intent(this, ReleaseActivity.class));
        } else if (view.getId() == R.id.tv_eduction_title) {
            rvEducation.setVisibility
                    (rvEducation.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        } else if (view.getId() == R.id.tv_conservation_title) {
            rvConservation.setVisibility
                    (rvConservation.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        }else if(view.getId()==R.id.tv_view_state){
            startActivity(new Intent(this, RankingListActivity.class));
        }
    }
}
