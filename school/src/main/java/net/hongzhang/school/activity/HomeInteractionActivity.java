package net.hongzhang.school.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.school.R;

import butterknife.ButterKnife;

public class HomeInteractionActivity extends BaseActivity {

    private RecyclerView rvEducation;
    private RecyclerView rvConservation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_interaction);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void setToolBar() {
        setLiftOnClickClose();
        setCententTitle("家园互动");
    }

    private void initView() {
        rvConservation = (RecyclerView) findViewById(R.id.rv_conservation);
        rvEducation = (RecyclerView) findViewById(R.id.rv_education);
        rvConservation.setPreserveFocusAfterLayout(true);
        rvEducation.setPreserveFocusAfterLayout(true);
    }
}
