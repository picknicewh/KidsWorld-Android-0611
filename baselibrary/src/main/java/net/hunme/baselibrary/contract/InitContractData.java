package net.hunme.baselibrary.contract;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import net.hunme.baselibrary.mode.FriendInforVo;
import net.hunme.baselibrary.mode.GroupInforVo;

import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;

/**
 * 作者： Administrator
 * 时间： 2016/8/31
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class InitContractData {
    private SQLiteDatabase db;
    private SQLiteDatabase db2;
    private GroupsDbHelper groupsDbHelper;
    private FriendsDbHelper friendsDbHelper;
    public InitContractData(Context context){
        FriendsDb friendsDb = new FriendsDb(context);
        GroupDb groupDb = new GroupDb(context);
        db  = friendsDb.getWritableDatabase();
        db2 = groupDb.getWritableDatabase();
        groupsDbHelper = GroupsDbHelper.getinstance();
        friendsDbHelper = FriendsDbHelper.getinstance();
        init();
    }
    public void  init(){
        List<FriendInforVo> friendInforVos = friendsDbHelper.getFriendInformVos(db);
        for (int i = 0 ; i<friendInforVos.size();i++){
            FriendInforVo friendInforVo = friendInforVos.get(i);
            final String image = friendInforVo.getImgurl();
            final String userName = friendInforVo.getName();
            final String ryid  = friendInforVo.getRyid();
            RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
                @Override
                public UserInfo getUserInfo(String userId) {
                    UserInfo userInfo = new UserInfo(ryid, userName, Uri.parse(image));
                    return userInfo;
                }
            }, true);
            if (image!=null && ryid!=null && userName!=null){
                RongIM.getInstance().refreshUserInfoCache(new UserInfo(ryid, userName, Uri.parse(image)));
            }
        }
        List<GroupInforVo> groupInforVos = groupsDbHelper.getGroupInformVos(db2);
        for (int i = 0 ; i <groupInforVos.size();i++){
            GroupInforVo groupInforVo = groupInforVos.get(i);
            final String classId = groupInforVo.getClassId();
            final  String groupName= groupInforVo.getGroupName();
            RongIM.setGroupInfoProvider(new RongIM.GroupInfoProvider() {
                @Override
                public Group getGroupInfo(String s) {
                    Group group = new Group(classId,groupName, Uri.parse(""));
                    return group;

                }
            },true);
            RongIM.getInstance().refreshGroupInfoCache(new Group(classId,groupName, Uri.parse("")));
        }
    }
}
