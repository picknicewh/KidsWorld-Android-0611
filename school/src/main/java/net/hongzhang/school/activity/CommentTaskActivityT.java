package net.hongzhang.school.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.widget.LoadingDialog;
import net.hongzhang.school.R;
import net.hongzhang.school.adapter.TaskCommentTAdapter;
import net.hongzhang.school.bean.DimensionalityTagVo;
import net.hongzhang.school.bean.SelectTagVo;
import net.hongzhang.school.presenter.CommentTaskTContract;
import net.hongzhang.school.presenter.CommentTaskTPresenter;
import net.hongzhang.school.widget.StarProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * 老师活动评分
 * 20170508
 */
public class CommentTaskActivityT extends Activity implements View.OnClickListener, CommentTaskTContract.View {
    /**
     * 加载中对话框
     */
    public LoadingDialog dialog;
    /**
     * 点评的星级
     */
    private StarProgressBar ratingBar;
    /**
     * 点评内容
     */
    private EditText etcomment;
    /**
     * 提交点评
     */
    private TextView tvConfrom;
    /**
     * 数据处理
     */
    private CommentTaskTPresenter presenter;
    /**
     * 活动id
     */
    private String activityId;
    /**
     * 作品id
     */
    private String activityWorksId;
    /**
     * 上传标签
     */
    private String tags;
    /**
     * 分数
     */
    private int score;
    /**
     * view
     */
    private ImageView iv_left;
    private List<DimensionalityTagVo> dimensionalityTagVoList;
    private List<List<String>> childTagList;
    private TaskCommentTAdapter adapter;
    private RecyclerView rv_tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_comment);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        iv_left = (ImageView) findViewById(R.id.iv_left);
        ratingBar = (StarProgressBar) findViewById(R.id.ratingBar);
        etcomment = (EditText) findViewById(R.id.et_comment);
        tvConfrom = (TextView) findViewById(R.id.tv_confrom);

        rv_tag = (RecyclerView) findViewById(R.id.rv_tag);
        tvConfrom.setOnClickListener(this);
        iv_left.setOnClickListener(this);
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        activityId = intent.getStringExtra("activityId");
        activityWorksId = intent.getStringExtra("activityWorksId");
        dimensionalityTagVoList = new ArrayList<>();
        childTagList = new ArrayList<>();
        selectTagVos = new ArrayList<>();
        presenter = new CommentTaskTPresenter(CommentTaskActivityT.this, this);
        adapter = new TaskCommentTAdapter(this, dimensionalityTagVoList, childTagList, selectTagVos, activityWorksId, presenter);
        rv_tag.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv_tag.setAdapter(adapter);
        presenter.getDimensionalityTags(activityId, activityWorksId);
        ratingBar.setOnProgressChangeListener(new StarProgressBar.OnProgressChangeListener() {
            @Override
            public void onProgress(int progress) {
                score = progress;
            }
        });
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.tv_confrom) {
            String content = etcomment.getText().toString().trim();
            if (G.isAllSpace(content) || G.isEmteny(content)) {
                G.showToast(this, "评语不能为空哦！");
                return;
            }
            presenter.submitScore(tags, activityWorksId, score, content);
        } else if (viewId == R.id.iv_left) {
            finish();
        }
    }

    @Override
    public void showLoadingDialog() {
        if (dialog == null)
            dialog = new LoadingDialog(this, R.style.LoadingDialogTheme);
        dialog.show();
        dialog.setCancelable(true);
        dialog.setLoadingText("数据加载中...");
    }

    @Override
    public void stopLoadingDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void setDimensionalityTags(List<DimensionalityTagVo> dimensionalityTags, List<List<String>> childTagList, List<SelectTagVo> selectTagVos) {
        presenter.initMapList(dimensionalityTags);
        presenter.initTag(dimensionalityTags);
        this.childTagList.addAll(childTagList);
        this.selectTagVos.addAll(selectTagVos);
        dimensionalityTagVoList.addAll(dimensionalityTags);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setTags(String tags) {
        this.tags = tags;
    }

    private List<SelectTagVo> selectTagVos;

    @Override
    public void setChildTagList(List<List<String>> childTagList, List<SelectTagVo> selectTagVos,int groupPosition) {
        G.log(childTagList.size() + "---------size");
        this.childTagList.clear();
        this.selectTagVos.clear();
        this.childTagList.addAll(childTagList);
        this.selectTagVos.addAll(selectTagVos);
        adapter.setSelectList(childTagList,groupPosition);
    }
}
