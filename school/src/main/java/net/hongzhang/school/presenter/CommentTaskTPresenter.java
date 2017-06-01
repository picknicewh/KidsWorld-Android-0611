package net.hongzhang.school.presenter;

import android.app.Activity;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.DetaiCodeUtil;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.school.bean.DimensionalityTagVo;
import net.hongzhang.school.bean.SelectTagVo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 作者： wanghua
 * 时间： 2017/5/9
 * 名称：称：教师评分
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class CommentTaskTPresenter implements CommentTaskTContract.Presenter, OkHttpListener {
    private Activity context;

    private CommentTaskTContract.View view;
    public static List<Map<Integer, Boolean>> mapList;
    public  static List<SelectTagVo>  selectTagVoList;
    public CommentTaskTPresenter(Activity context, CommentTaskTContract.View view) {
        this.context = context;
        this.view = view;
        mapList = new ArrayList<>();
        selectTagVoList  = new ArrayList<>();
    }

    @Override
    public void getDimensionalityTags(String activityId, String activityWorksId) {
        Map<String, Object> params = new HashMap<>();
        params.put("activityId", activityId);
        params.put("activityWorksId", activityWorksId);
        Type type = new TypeToken<Result<List<DimensionalityTagVo>>>() {
        }.getType();
        OkHttps.sendPost(type, Apiurl.SCHOOL_GETDIMESIONALITY, params, this);
        view.showLoadingDialog();
    }

    @Override
    public void addActivityTagName(String tagName, String parentId, String activityWorksId) {
        Map<String, Object> params = new HashMap<>();
        params.put("parentId", parentId);
        params.put("tagName", tagName);
        params.put("activityWorksId", activityWorksId);
        Type type = new TypeToken<Result<String>>() {
        }.getType();
        OkHttps.sendPost(type, Apiurl.SCHOOL_ADDACTIVETAG, params, this);
        view.showLoadingDialog();
    }

    @Override
    public void submitScore(String tags, String activityWorksId, int score, String content) {
        Map<String, Object> params = new HashMap<>();
        params.put("tags", tags);
        params.put("score", score);
        params.put("content", content);
        params.put("activityWorksId", activityWorksId);
        Type type = new TypeToken<Result<String>>() {
        }.getType();
        OkHttps.sendPost(type, Apiurl.SCHOOL_COMMITSCORE, params, this);
        view.showLoadingDialog();
    }
    /**
     * 设置tags
     */
    @Override
    public void setTagMapList(List<Map<Integer, Boolean>> mapList,int groupPosition) {
        List<String> tagList = new ArrayList<>();
        List<List<String>> selectChildTagVo = new ArrayList<>();
        for (int i = 0; i < mapList.size(); i++) {
            Map<Integer, Boolean> map = mapList.get(i);
            Set<Map.Entry<Integer, Boolean>> entries = map.entrySet();
            List<String> childTagList = new ArrayList<>();
            for (Map.Entry<Integer, Boolean> entry : entries) {
                if (entry.getValue()) {
                    DimensionalityTagVo.ChildTagVo childTagVo = dimensionalityTagVos.get(i).getTags().get(entry.getKey() - 1);
                    childTagList.add(childTagVo.getTagName());
                    tagList.add(childTagVo.getTagId());

                }
            }
            selectChildTagVo.add(childTagList);
        }
        view.setChildTagList(selectChildTagVo,selectTagVoList,groupPosition);
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < tagList.size(); i++) {
            buffer.append(tagList.get(i)).append(",");
        }
        if (buffer.length() > 0) {
            buffer.deleteCharAt(buffer.lastIndexOf(","));
            String tags = buffer.toString();
            view.setTags(tags);
        }
    }
    private SelectTagVo getSelectTagVo(int groupPosition,int position){
        SelectTagVo   selectTagVo =new SelectTagVo();
        selectTagVo.setChildPosition(position);
        selectTagVo.setGroupPosition(groupPosition);
        return selectTagVo;
    }
    @Override
    public void initMapList(List<DimensionalityTagVo> dimensionalityTagVos) {
        selectTagVoList.add(getSelectTagVo(0,1));
        selectTagVoList.add(getSelectTagVo(1,1));
        mapList = new ArrayList<>();
        for (int i = 0; i < dimensionalityTagVos.size(); i++) {
            DimensionalityTagVo tagVo = dimensionalityTagVos.get(i);
            Map<Integer, Boolean> map = new HashMap<>();
            for (int j = 0; j < tagVo.getTags().size() + 1; j++) {
                if (j == 1) {
                    map.put(j, true);
                } else {
                    map.put(j, false);
                }
            }
            mapList.add(map);
        }
    }

    private  List<List<String>> initChildSelectTag(List<DimensionalityTagVo> dimensionalityTagVos) {
        List<List<String>>  childTagVoList = new ArrayList<>();
        if (dimensionalityTagVos != null && dimensionalityTagVos.size() > 0) {
            for (int i = 0; i < dimensionalityTagVos.size(); i++) {
                List<String>childTagVos = new ArrayList<>();
                List<DimensionalityTagVo.ChildTagVo> childTagVo = dimensionalityTagVos.get(i).getTags();
                childTagVos.add(childTagVo.get(0).getTagName());
                childTagVoList.add(childTagVos);
            }
        }
        return childTagVoList;
    }

    @Override
    public void initTag(List<DimensionalityTagVo> dimensionalityTagVos) {
        String tags;
        if (dimensionalityTagVos != null && dimensionalityTagVos.size() > 0) {
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < dimensionalityTagVos.size(); i++) {
                DimensionalityTagVo.ChildTagVo childTagVo = dimensionalityTagVos.get(i).getTags().get(0);
                buffer.append(childTagVo.getTagId()).append(",");
            }
            if (buffer.length() > 0) {
                buffer.deleteCharAt(buffer.lastIndexOf(","));
                tags = buffer.toString();
                view.setTags(tags);
            }
        }
    }
    private List<DimensionalityTagVo> dimensionalityTagVos;
    @Override
    public void onSuccess(String uri, Object date) {
        view.stopLoadingDialog();
        if (uri.equals(Apiurl.SCHOOL_GETDIMESIONALITY)) {
            Result<List<DimensionalityTagVo>> result = (Result<List<DimensionalityTagVo>>) date;
            dimensionalityTagVos = result.getData();
            List<List<String>> childSelectTag = initChildSelectTag(dimensionalityTagVos);
            view.setDimensionalityTags(dimensionalityTagVos, childSelectTag,selectTagVoList);
        } else if (uri.equals(Apiurl.SCHOOL_ADDACTIVETAG)) {
            Result<String> result = (Result<String>) date;
            G.showToast(context,"添加成功");
          //  G.showToast(context, result.getData());
        } else if (uri.equals(Apiurl.SCHOOL_COMMITSCORE)) {
            Result<String> result = (Result<String>) date;
            G.showToast(context, result.getData());
            context.finish();
        }
    }

    @Override
    public void onError(String uri, Result result) {
        view.stopLoadingDialog();
        DetaiCodeUtil.errorDetail(result, context);
    }
}
