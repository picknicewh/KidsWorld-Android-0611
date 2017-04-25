package net.hongzhang.school.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.school.R;
import net.hongzhang.school.adapter.MyRankListItemAdapter;

/**
 * 排行榜
 */
public class RankingListActivity extends BaseActivity {
    private RecyclerView rvRankList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_list);
        rvRankList= (RecyclerView) findViewById(R.id.rv_rank_list);
        rvRankList.setLayoutManager(new LinearLayoutManager(this));
        rvRankList.setAdapter(new MyRankListItemAdapter());
    }

    @Override
    protected void setToolBar() {
        setLiftOnClickClose();
        setCententTitle("排行榜");
    }
}
