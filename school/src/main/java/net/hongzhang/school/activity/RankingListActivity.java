package net.hongzhang.school.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.school.R;
import net.hongzhang.school.adapter.MyRankListItemAdapter;
import net.hongzhang.school.bean.RankingInfoVo;
import net.hongzhang.school.presenter.RankingListContract;
import net.hongzhang.school.presenter.RankingListPresenter;

import java.util.List;

/**
 * 排行榜
 */
public class RankingListActivity extends BaseActivity implements RankingListContract.View {
    private RecyclerView rvRankList;
    private String activityId;
    private RankingListPresenter presenter;

    private MyRankListItemAdapter myRankListItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_list);
        initData();
    }
    @Override
    protected void setToolBar() {
        setLiftOnClickClose();
        setCententTitle("排行榜");
    }
    private void initData() {
        Intent intent = getIntent();
        activityId = intent.getStringExtra("activityId");
        rvRankList = (RecyclerView) findViewById(R.id.rv_rank_list);
        presenter = new RankingListPresenter(RankingListActivity.this, this);
        presenter.getRankingList(activityId);

    }
    @Override
    public void setRankingList(List<RankingInfoVo> rankingInfoVo) {
        G.log("a-xxxxxxxxxxxxx" + rankingInfoVo.size());
        rvRankList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        myRankListItemAdapter = new MyRankListItemAdapter(this, rankingInfoVo);
        rvRankList.setAdapter(myRankListItemAdapter);
     /*   if (UserMessage.getInstance(this).getType().equals("1")) {

        } else {
            adapter = new RankingListTAdapter(this, rankingInfoVo);
            rvRankList.setAdapter(adapter);
        }*/
    }
}
