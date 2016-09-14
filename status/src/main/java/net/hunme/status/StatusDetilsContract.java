package net.hunme.status;

import net.hunme.status.mode.StatusDetilsVo;

import java.util.List;

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
public interface StatusDetilsContract {
    interface View {
        void setHeadImageView(String url);
        void setName(String name);
        void setId(String id);
        void setContent(String content);
        void setTime(String time);
        void setlinPraiseVis(boolean isVis);
        void setPraisePerson(String person);
        void setCommentList(List<StatusDetilsVo.DynamidRewListBean> list);
        void setImagePrasise(boolean isAgree);
        void showPopWindow(android.view.View view,StatusDetilsVo.DynamidRewListBean bean);
        void setPictures(List<String> imageUrl);
        void setPiaiseNum(int piaiseNum);
        void setCommentNum(int commentNum);
        void showLoadingDialog();
        void stopLoadingDialog();
    }

    interface Presenter{
        void getStatusDetils(String tsId,String dynamicId);
        void personPraise(String tsId,String dynamicId,String cancel);
        void personComment(String tsId,String dynamicId,String content,String rewtsId,String rewtype);
        void deleteComment(String tsId,String rewId);
    }

}