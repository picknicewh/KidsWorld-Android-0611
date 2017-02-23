package net.hongzhang.user.util;

import android.content.Context;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.user.mode.StatusDetilsVo;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/9/9
 * 描    述：
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class StatusDetilsPresenter implements StatusDetilsContract.Presenter, OkHttpListener {
    private StatusDetilsContract.View view;
    private Context context;
    private String tsId;
    private String dynamicId;
    public StatusDetilsPresenter(StatusDetilsContract.View view,String tsId,String dynamicId,Context context) {
        this.view = view;
        this.context=context;
        this.tsId=tsId;
        this.dynamicId=dynamicId;
        view.showLoadingDialog();
        getStatusDetils(tsId,dynamicId);
    }

    @Override
    public void getStatusDetils(String tsId, String dynamicId) {
        Map<String,Object>map=new HashMap<>();
        map.put("tsId",tsId);
        map.put("dynamicId",dynamicId);
        Type type=new TypeToken<Result<StatusDetilsVo>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.STATUSDETILS,map,this);
    }
    /**
     * 点赞
     */
    @Override
    public void personPraise(String tsId,String dynamicId,String cancel) {
        Map<String,Object>map=new HashMap<>();
        map.put("tsId",tsId);
        map.put("dynamicId",dynamicId);
        map.put("cancel",cancel);
        Type type=new TypeToken<Result<String>>(){}.getType();
        OkHttps.sendPost(type,Apiurl.SUBPRAISE,map,this);
        view.showLoadingDialog();
    }
    /**
     * 评论
     */
    @Override
    public void personComment(String tsId,String dynamicId,String content,String rewtsId,String rewtype) {
        if(G.isEmteny(content)){
            return;
        }
        Map<String,Object>map=new HashMap<>();
        map.put("tsId",tsId);
        map.put("dynamicId",dynamicId);
        map.put("content",content);
        if(!G.isEmteny(rewtsId)){
            map.put("rewtsId",rewtsId);
        }
        map.put("rewtype",rewtype);
        Type type=new TypeToken<Result<String>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.SUBCOMMENT,map,this);
        view.showLoadingDialog();
    }
    /**
     *  删除评论
     */
    @Override
    public void deleteComment(String tsId, String rewId) {
        Map<String,Object>map=new HashMap<>();
        map.put("tsId",tsId);
        map.put("rewId",rewId);
        Type type=new TypeToken<Result<String>>(){}.getType();
        OkHttps.sendPost(type,Apiurl.DELETERCOMMENTD,map,this);
    }

    @Override
    public void onSuccess(String uri, Object date) {
        if (date!=null){
            if(Apiurl.STATUSDETILS.equals(uri)){
                view.stopLoadingDialog();
                view.setDeleteView(false);
                Result<StatusDetilsVo> result= (Result<StatusDetilsVo>) date;
                StatusDetilsVo detilsVo= result.getData();
                if (detilsVo!=null){
                    String praisePerson = "";
                    view.setId(detilsVo.getTsType());
                    view.setHeadImageView(detilsVo.getImg());
                    view.setName(detilsVo.getTsName());
                    view.setContent(detilsVo.getText());
                    view.setTime(detilsVo.getDate());
                    boolean ishasPraise=null!=detilsVo.getList()&&detilsVo.getList().size()>0;
                    view.setlinPraiseVis(ishasPraise);
                    if(ishasPraise){
                        for (String s:detilsVo.getList()){
                            praisePerson=G.isEmteny(praisePerson)?praisePerson+s:praisePerson+"、"+s;
                        }
                        view.setPraisePerson(praisePerson);
                        view.setPiaiseNum(detilsVo.getList().size());
                    }
                    if(detilsVo.getDynamidRewList()!=null){
                        view.setCommentNum(detilsVo.getDynamidRewList().size());
                        view.setCommentList(detilsVo.getDynamidRewList());
                    }
                    view.setImagePrasise(detilsVo.getIsAgree()==1);
                    view.setCommentVis(!G.isEmteny(detilsVo.getText()));
                    if(null!=detilsVo.getImgUrl()&&detilsVo.getImgUrl().size()>0){
                        view.setPictures(detilsVo.getImgUrl());
                        view.setImageVis(true);
                    }else {
                        view.setImageVis(false);
                    }
                    view.setDeleteView(false);
                }else {
                    view.setDeleteView(true);
                    view.stopLoadingDialog();
                }

            } else{
                G.KisTyep.isUpdateComment=true;  //通知statusFragement 当前动态发生了改变  需要刷新数据
                view.stopLoadingDialog();
                Result<String> result= (Result<String>) date;
                G.showToast(context,result.getData());
                getStatusDetils(tsId,dynamicId);
            }
        }
    }
    @Override
    public void onError(String uri, String error) {
        view.stopLoadingDialog();
        G.showToast(context,error);
    }
}
