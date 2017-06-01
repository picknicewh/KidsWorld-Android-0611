package net.hongzhang.school.presenter;

import net.hongzhang.school.bean.DimensionalityTagVo;
import net.hongzhang.school.bean.SelectTagVo;

import java.util.List;
import java.util.Map;

/**
 * 作者： wanghua
 * 时间： 2017/5/9
 * 名称：教师评分
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class CommentTaskTContract {
    public interface View {
        void showLoadingDialog();

        void stopLoadingDialog();

        void setDimensionalityTags(List<DimensionalityTagVo> dimensionalityTags,List<List<String>> childTagList, List<SelectTagVo> selectTagVos);

        void setTags(String tags);

        //  void setSelectTags(List<DimensionalityTagVo.ChildTagVo> childTagVos);
       void setChildTagList(List<List<String>> childTagList, List<SelectTagVo> selectTagVos,int groupPosition);
    }

    public interface Presenter {
        void getDimensionalityTags(String activityId, String activityWorksId);

        void addActivityTagName(String tagName, String parentId, String activityWorksId);

        void submitScore(String tags, String activityWorksId, int score, String content);

        void setTagMapList(List<Map<Integer, Boolean>> mapList,int groupPosition);


        void initMapList(List<DimensionalityTagVo> dimensionalityTagVos);

        void initTag(List<DimensionalityTagVo> dimensionalityTagVos);

    }
}
