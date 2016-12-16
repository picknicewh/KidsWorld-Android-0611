package net.hunme.user.activity;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.reflect.TypeToken;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.baselibrary.mode.Result;
import net.hunme.baselibrary.network.Apiurl;
import net.hunme.baselibrary.network.OkHttpListener;
import net.hunme.baselibrary.network.OkHttps;
import net.hunme.baselibrary.util.G;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.user.R;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class AdviceActivity extends BaseActivity implements View.OnClickListener, OkHttpListener {
    private EditText et_advice;
    private Button b_submit;
    private UserMessage um;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice);
        initView();
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        setCententTitle("意见反馈");
    }

    private void initView(){
        et_advice=$(R.id.et_advice);
        b_submit=$(R.id.b_submit);
        b_submit.setOnClickListener(this);
        et_advice.setFilters(new InputFilter[]{new InputFilter.LengthFilter(200)});
    }
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.b_submit){
            b_submit.setEnabled(false);
            String advice=et_advice.getText().toString().trim();
            if(G.isEmteny(advice)){
                G.showToast(this,"反馈内容不能为空！");
                return;
            }
            um=UserMessage.getInstance(this);
            Map<String,Object>map=new HashMap<>();
            map.put("tsId",um.getTsId());
            map.put("feedback",advice);
            map.put("contact",um.getLoginName());
            Type type=new TypeToken<Result<String>>(){}.getType();
            OkHttps.sendPost(type, Apiurl.FEEDBACK,map,this);
        }
    }

    @Override
    public void onSuccess(String uri, Object date) {
        if(Apiurl.FEEDBACK.equals(uri)){
            G.showToast(this,"数据提交成功，感谢亲的反馈！");
            finish();
        }
    }

    @Override
    public void onError(String uri, String error) {
        G.showToast(this,error);
    }
}
