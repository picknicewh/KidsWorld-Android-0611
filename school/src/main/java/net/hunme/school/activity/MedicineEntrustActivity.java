package net.hunme.school.activity;

import android.os.Bundle;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.school.R;

public class MedicineEntrustActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_entrust);
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        setCententTitle("委托喂药");
    }
}
