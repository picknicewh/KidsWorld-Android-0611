package net.hunme.user.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.baselibrary.mode.SystemInformVo;
import net.hunme.baselibrary.util.SystemInfomDb;
import net.hunme.baselibrary.util.SystemInfomDbHelp;
import net.hunme.user.R;
import net.hunme.user.adapter.SystemInfoAdapter;
import net.hunme.user.util.MyAlertDialog;
import net.hunme.user.util.PermissionUtils;

import java.util.ArrayList;
import java.util.List;

public class SystemInfoActivity extends BaseActivity {
    private ListView lv_systeminfo;
   // private List<MessageVo>messageList;
    private SystemInfoAdapter adapter;
//    private final String SYSTEMESSAGE="/appUser/systemMessages.do";
    /**
     * 系统消息数据创建类
     */
    private SystemInfomDb infoDb;
    /**
     * 写数据库
     */
    private SQLiteDatabase wdb;
    /**
     * 数据库--修改类
     */
    private SystemInfomDbHelp dbHelp;
    /**
     * 系统消息列表数据
     */
    private List<SystemInformVo> systemInformVoList;
    private final String[] PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE, //读写权限
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_info);
        initView();
        initdata();
    }

    @Override
    protected void setToolBar(){
        setCententTitle("系统消息");
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
    }
    /**
     * 初始化数据库数据
     */
    private void initdata(){
        PermissionUtils.getPermission(this,PERMISSIONS);
        infoDb = new SystemInfomDb(this);
        dbHelp = SystemInfomDbHelp.getinstance();
        //测试数据
        wdb = infoDb.getWritableDatabase();
       // insert();//插入数据
        systemInformVoList =new ArrayList<>();
        systemInformVoList = dbHelp.getSystemInformVo(infoDb.getReadableDatabase());
        adapter=new SystemInfoAdapter(systemInformVoList,this);
        lv_systeminfo.setAdapter(adapter);
        lv_systeminfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dbHelp.update(wdb,systemInformVoList.get(i).getId(),0);
                adapter.notifyDataSetChanged();
                Intent intent = new Intent(SystemInfoActivity.this,InfoDetailsActivity.class);
                intent.putExtra("content",systemInformVoList.get(i).getContent());
                intent.putExtra("title",systemInformVoList.get(i).getTitle());
                intent.putExtra("date",systemInformVoList.get(i).getTime());
                startActivity(intent);
                setDosGone();
            }
        });
        lv_systeminfo.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                showAlertDialog(systemInformVoList.get(position),wdb);
                setDosGone();
                return true;
            }
        });
    }
    private void setDosGone(){
        Intent myintent = new Intent("net.hunme.user.activity.ShowSysDosReceiver");
        myintent.putExtra("isVisible",false);
        sendBroadcast(myintent);
    }
    /**
     * 删除提示框
     */
    private void showAlertDialog(final SystemInformVo systemInformVo, final  SQLiteDatabase database) {
        View coupons_view = LayoutInflater.from(this).inflate(R.layout.alertdialog_message, null);
        final AlertDialog alertDialog = MyAlertDialog.getDialog(coupons_view, this, 1);
        Button pop_notrigst = (Button) coupons_view.findViewById(R.id.pop_notrigst);
        Button pop_mastrigst = (Button) coupons_view.findViewById(R.id.pop_mastrigst);
        TextView pop_title = (TextView) coupons_view.findViewById(R.id.tv_poptitle);
        pop_title.setText("确认删除此系统消息？");
        pop_notrigst.setText("取消");
        pop_mastrigst.setText("删除");
        pop_mastrigst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dbHelp.deleteById(database,systemInformVo.getId());
                initdata();
                alertDialog.dismiss();
            }
        });
        pop_notrigst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        systemInformVoList.clear();
        systemInformVoList.addAll(dbHelp.getSystemInformVo(infoDb.getReadableDatabase()));
        adapter.notifyDataSetChanged();
    }

    /**
     * 模拟插入数据
     */
    private  void  insert(){
        if (dbHelp.isEmpty(infoDb.getReadableDatabase())) {
            dbHelp.insert(wdb, "标题一", "这是第一条通知的内容哦！", "10:38", 1);
            dbHelp.insert(wdb, "标题二", "这是第二条通知的内容哦！", "20:50", 1);
            dbHelp.insert(wdb, "标题三", "这是第三条通知的内容哦！", "昨天", 1);
            dbHelp.insert(wdb, "标题四", "这是第四条通知的内容哦！", "星期三", 1);
            dbHelp.insert(wdb, "标题五", "这是第五条通知的内容哦！", "7/24", 1);
            dbHelp.insert(wdb, "标题六", "这是第六条通知的内容哦！", "7/25", 1);
        }
//        }else {
//            dbHelp.delete(db);
//        }

    }
    private void initView(){
        lv_systeminfo=$(R.id.lv_systeminfo);
      //  messageList=new ArrayList<>();
//        adapter=new SystemInfoAdapter(systemInformVoList,this);
//        lv_systeminfo.setAdapter(adapter);
     //   testDate();
//        getMessageDate(UserMessage.getInstance(this).getTsId());
       // infoDb=new SystemInfoDb(this);//创建数据库
    }

//    private void getMessageDate(String tsId){
//        Map<String,Object>map=new HashMap<>();
//        map.put("tsId",tsId);
//        Type type=new TypeToken<Result<List<MessageVo>>>(){}.getType();
//        OkHttps.sendPost(type,SYSTEMESSAGE,map,this);
//    }
//
//    @Override
//    public void onSuccess(String uri, Object date) {
//        if(SYSTEMESSAGE.equals(uri)){
//            Result<List<SystemInformVo>>result= (Result<List<SystemInformVo>>) date;
//            if(result.isSuccess()){
//                systemInformVoList=result.getData();
//                adapter.notifyDataSetChanged();
//            }else {
//                G.showToast(this,"消息获取失败，请稍后再试！");
//            }
//        }
//    }
//
//    @Override
//    public void onError(String uri, String error) {
//        G.showToast(this,"消息获取失败，请检查网络再试！");
//    }

  /*  private void testDate(){
        for (int i=0;i<100;i++){
            MessageVo vo=new MessageVo();
            vo.setContent(i*10+"");
            vo.setDate(i+"");
            messageList.add(vo);
        }
        adapter.notifyDataSetChanged();
    }
*/
}
