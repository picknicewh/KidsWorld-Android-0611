package net.hunme.login;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.hunme.baselibrary.BaseLibrary;
import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.baselibrary.network.OkHttpListener;
import net.hunme.baselibrary.util.G;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.login.mode.CharacterSeleteVo;
import net.hunme.login.util.UserAction;

import java.lang.reflect.Type;
import java.util.List;

public class UserChooseActivity extends BaseActivity implements OkHttpListener {
    private ListView lv_user_choose;
    private UserChooseAdapter adapter;
    private UserMessage um;
    private List<CharacterSeleteVo> seleteList;
    public static int flag = 0;
    private CharacterSeleteVo data;
    private boolean isGoBack; //判断按返回键是返回还是保存用户信息跳入主页面  如果是用户设置进入的话 直接返回 如果是登录进来的话 默认选择跳入主页面
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_choose);
        initView();
    }

    private void initView(){
        um=UserMessage.getInstance(this);
        //拿到用户信息再次解析
        Type type =new TypeToken<List<CharacterSeleteVo>>(){}.getType();
        seleteList= new Gson().fromJson(um.getUserMessageJsonCache(),type);
        lv_user_choose=$(R.id.lv_user_choose);
        if (seleteList.size()!=0){
            adapter=new UserChooseAdapter(this,seleteList);
            lv_user_choose.setAdapter(adapter);
            lv_user_choose.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    adapter.setSelectPosition(i);
                    data=seleteList.get(i);
                    LoginActivity.selectUserSubmit(data.getTsId(),UserChooseActivity.this);
                }
            });
        }
        isGoBack=getIntent().getBooleanExtra("type",false);
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        setCententTitle("选择账号");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!isGoBack&&G.isEmteny(um.getUserName())){
            // 如果用户没有点击选择默认选择第一个身份
            data=seleteList.get(0);
            LoginActivity.selectUserSubmit(data.getTsId(),this);
        }
    }


    @Override
    public void onSuccess(String uri, Object date) {
        String sex;
        if(data.getSex()==1){
            sex="男";
        }else{
            sex="女";
        }
        //通过用户选择身份保存用户信息
        UserAction.saveUserMessage(UserChooseActivity.this,data.getName(),
                data.getImg(),data.getClassName(),data.getSchoolName(),
                data.getRyId(),data.getTsId(),data.getType(),sex,data.getSignature());
        //与融云进行连接
        String image = data.getImg();
        if (image==null){
            image = "http://rongcloud-web.qiniudn.com/docs_demo_rongcloud_logo.png";
        }
        BaseLibrary.connect(data.getRyId(),UserChooseActivity.this,data.getName(),image);
        G.KisTyep.isChooseId=true;
        flag=1;
        UserAction.goMainActivity(this);
        finish();
        G.log("用户选择身份成功----");
    }

    @Override
    public void onError(String uri, String error) {
        G.showToast(this,"身份选择失败，请重新选择");
    }
}
