package net.hongzhang.school.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.school.R;
import net.hongzhang.school.adapter.MedicineDateAdapter;
import net.hongzhang.school.adapter.MedicineListAdapter;
import net.hongzhang.school.bean.MyMedicineVo;
import net.hongzhang.school.presenter.MedicineListContract;
import net.hongzhang.school.presenter.MedicineListPresenter;

import java.util.ArrayList;
import java.util.List;

public class MedicineListActivity extends BaseActivity implements MedicineListContract.View, View.OnClickListener {
    private MedicineListPresenter presenter;
    private UserMessage userMessage;
    private List<String> dateList;
    private RecyclerView rv_date;
    private RecyclerView rv_medicine;
    private MedicineDateAdapter adapter;
    private MedicineListAdapter medicinedListAdapter;
    private List<MyMedicineVo> medicineVoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicined_list_s);
        initView();
    }

    private void initView() {
        rv_date = $(R.id.rv_date);
        rv_medicine = $(R.id.rv_medicine);
        dateList = new ArrayList<>();
        medicineVoList = new ArrayList<>();
        presenter = new MedicineListPresenter(MedicineListActivity.this, this);
        presenter.getMedicineDate(userMessage.getTsId());
        medicinedListAdapter = new MedicineListAdapter(medicineVoList);

        rv_medicine.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv_medicine.setAdapter(medicinedListAdapter);

        adapter = new MedicineDateAdapter(this, dateList);
        rv_date.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rv_date.setAdapter(adapter);
        adapter.setOnItemClickListener(new MedicineDateAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                presenter.getMedicine(userMessage.getTsId(), dateList.get(position));
            }
        });
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        userMessage = new UserMessage(this);
        setCententTitle("喂药列表");
        if (userMessage.getType().equals("1")) {
            setSubTitle("发布喂药");
        }
        setSubTitleOnClickListener(this);
    }
    @Override
    public void setMedicineDate(final List<String> medicineDates) {
        dateList.clear();
        this.dateList.addAll(medicineDates);
        if (dateList.size()>0){
           presenter.getMedicine(userMessage.getTsId(),dateList.get(0));
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    public void setMedicine(List<MyMedicineVo> medicineVos) {
        medicineVoList.clear();
        this.medicineVoList.addAll(medicineVos);
        if (medicinedListAdapter != null) {
            medicinedListAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_subtitle) {
            Intent intent = new Intent(this, SubmitMedicineActivity.class);
            startActivity(intent);
        }
    }
}
