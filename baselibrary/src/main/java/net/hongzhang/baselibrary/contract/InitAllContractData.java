package net.hongzhang.baselibrary.contract;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import net.hongzhang.baselibrary.mode.ContractInfoVo;
import net.hongzhang.baselibrary.mode.GroupInforVo;
import net.hongzhang.baselibrary.util.G;

import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;

/**
 * 作者： wh
 * 时间： 2016/8/31
 * 名称：初始化所有联系人和群组信息
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class InitAllContractData {
    private SQLiteDatabase db;
    private SQLiteDatabase db2;
    private GroupsDbHelper groupsDbHelper;
    private ContractsDbHelper friendsDbHelper;
    public InitAllContractData(Context context){
        ContractsDb friendsDb = new ContractsDb(context);
        GroupDb groupDb = new GroupDb(context);
        db  = friendsDb.getWritableDatabase();
        db2 = groupDb.getWritableDatabase();
        groupsDbHelper = GroupsDbHelper.getinstance();
        friendsDbHelper = ContractsDbHelper.getinstance();
        //init();
    }
    public void  init(){
        List<ContractInfoVo> contractInfoVos = friendsDbHelper.getFriendInformVos(db);
        for (int i = 0 ; i<contractInfoVos.size();i++){
            ContractInfoVo contractInfoVo = contractInfoVos.get(i);
            final String image = contractInfoVo.getImg();
            final String userName = contractInfoVo.getTsName();
            final String ryid  = contractInfoVo.getRyId();
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
            if (!G.isEmteny(classId) && !G.isEmteny(groupName)){
                RongIM.getInstance().refreshGroupInfoCache(new Group(classId,groupName, Uri.parse("")));
            }
        }
    }
}
