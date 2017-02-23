package net.hongzhang.baselibrary.contract;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.mode.ContractInfoVo;
import net.hongzhang.baselibrary.mode.GroupInfoVo;
import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.G;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * 作者： wh
 * 时间： 2016/8/16
 * 名称：：获取所有联系人信息，并保存到数据库
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class GetContractData implements OkHttpListener {
    private SQLiteDatabase db;
    private ContractsDbHelper friendsDbHelper;
    private Context context;
    public GetContractData(Context context){
        this.context = context;
        ContractsDb friendsDb = new ContractsDb(context);
        db  = friendsDb.getWritableDatabase();
        friendsDbHelper = ContractsDbHelper.getinstance();
        if (!friendsDbHelper.isEmpty(db)){
            friendsDbHelper.delete(db);
        }

    }
    /**
     * 获取所有班级信息
     * @param tsid
     *  type获取列表类型 0=所有联系人 1=所有群信息 2=所有老师3=所有家长
     */
    public  void getContractList(String tsid){
        if(G.isEmteny(tsid)){
            //用户没登录或者退出账号 打开App无需去请求
            return;
        }
        Map<String,Object> params = new HashMap<>();
        params.put("tsId", tsid);
        //1=群，2=老师，3=家长
        params.put("type",4);
        Type type =new TypeToken<Result<List<GroupInfoVo>>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.MESSAGE_GETGTOUP,params,this);
    }

    @Override
    public void onSuccess(String uri, Object date) {
        Result<List<GroupInfoVo>> data = (Result<List<GroupInfoVo>>) date;
        if (data!=null) {
            List<GroupInfoVo> groupJsonList = data.getData();
            if (RongIM.getInstance()!=null){
                List<ContractInfoVo> memberJsons = groupJsonList.get(0).getMenberList();
                for (int i = 0 ;i<memberJsons.size();i++){
                    ContractInfoVo memberJson = memberJsons.get(i);
                    final String image = memberJson.getImg();
                    final String userName = memberJson.getTsName();
                    final String ryid  = memberJson.getRyId();
                    final  String tsid = memberJson.getTsId();
                    friendsDbHelper.insert(db,userName,ryid,tsid,image,0);
                     RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
                    @Override
                    public UserInfo getUserInfo(String userId) {
                         UserInfo userInfo = new UserInfo(ryid, userName, Uri.parse(image));
                        return userInfo;
                    }
                  }, true);
                  if (!G.isEmteny(image) && !G.isEmteny(ryid) && !G.isEmteny(userName)){
                     RongIM.getInstance().refreshUserInfoCache(new UserInfo(ryid, userName, Uri.parse(image)));
                  }
             }
            }
        }
    }
    @Override
    public void onError(String uri, String error) {
        Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
    }
}
