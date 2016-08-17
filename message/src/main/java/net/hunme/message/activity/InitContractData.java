package net.hunme.message.activity;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hunme.baselibrary.mode.Result;
import net.hunme.baselibrary.network.Apiurl;
import net.hunme.baselibrary.network.OkHttpListener;
import net.hunme.baselibrary.network.OkHttps;
import net.hunme.baselibrary.util.G;
import net.hunme.message.bean.GroupJson;
import net.hunme.message.bean.MemberJson;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * 作者： Administrator
 * 时间： 2016/8/16
 * 名称：初始化联系人头像
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class InitContractData implements OkHttpListener {

    private Context context;
    public InitContractData(Context context){
        this.context = context;
     //   getGroupList(tsid);
    }

    /**
     * 获取所有班级信息
     * @param tsid
     *  type获取列表类型 0 所有联系人 1 所有群信息 2 所有老师3 所有家长
     */
    public  void getContractList(String tsid){
        if(G.isEmteny(tsid)){
            //用户没登录或者退出账号 打开App无需去请求
            return;
        }
        Map<String,Object> params = new HashMap<>();
        params.put("tsId", tsid);
        //1=群，2=老师，3=家长
        params.put("type",0);
        Type type =new TypeToken<Result<List<GroupJson>>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.MESSAGE_GETGTOUP,params,this);
        Log.i("TGGG","ryid.................."+tsid);
    }

    @Override
    public void onSuccess(String uri, Object date) {
        Result<List<GroupJson>> data = (Result<List<GroupJson>>) date;
        if (data!=null) {
            List<GroupJson> groupJsonList = data.getData();
            if (RongIM.getInstance()!=null){
                List<MemberJson> memberJsons = groupJsonList.get(0).getMenberList();
                for (int i = 0 ;i<memberJsons.size();i++){
                   MemberJson memberJson = memberJsons.get(i);
                    final String image = memberJson.getImg();
                    final String userName = memberJson.getTsName();
                    final String ryid  = memberJson.getRyId();
                    RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {

                        @Override
                        public UserInfo getUserInfo(String userId) {
                                UserInfo userInfo = new UserInfo(ryid, userName, Uri.parse(image));
                                return userInfo;
                        }

                    }, true);

                    RongIM.getInstance().refreshUserInfoCache(new UserInfo(ryid, userName, Uri.parse(image)));
                }
            }
        }
    }

    @Override
    public void onError(String uri, String error) {
        Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
    }
}
