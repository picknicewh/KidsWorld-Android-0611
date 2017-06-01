package net.hongzhang.school.util;

import net.hongzhang.baselibrary.util.G;
import net.hongzhang.school.R;
import net.hongzhang.school.bean.RankingInfoVo;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： wanghua
 * 时间： 2017/5/15
 * 名称：排行榜排列工具类
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class SortUtil {
    public static int[] rankImage = new int[]{R.mipmap.ic_top, R.mipmap.ic_second, R.mipmap.ic_third, R.mipmap.ic_no_place};

    public static List<Integer> deleteSame(List<Integer> list) {
        List<Integer> mList = new ArrayList<>();
        for (int j = 0; j < list.size(); j++) {
            if (!mList.contains(list.get(j))) {
                mList.add(list.get(j));
            }
        }
        return mList;
    }

    public static List<Integer> getTopThree(List<Integer> list) {
        List<Integer> mList = new ArrayList<>();
        List<Integer> sortList = deleteSame(list);
        for (int i = 0; i < sortList.size(); i++) {
            if (i < 3) {
                mList.add(sortList.get(i));
            }
        }
        return mList;
    }

    public static List<List<RankingInfoVo>> getList(List<RankingInfoVo> list) {
        List<List<RankingInfoVo>> infoList = new ArrayList<>();
        if (list.size()>0){
            int temp = list.get(0).getScore();//按大到小排序列表，拿到最大的那个，依次排序
            List<RankingInfoVo> infoVoList = new ArrayList<>();
            List<RankingInfoVo> otherInfoVoList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                int score = list.get(i).getScore();
                if (infoList.size() <= 3) {
                    if (temp > score) {
                        infoList.add(infoVoList);//把之前相等的添加到数据中
                        temp = score;//重新赋值
                        infoVoList = new ArrayList<>();
                        infoVoList.add(list.get(i));
                    } else if (temp == score) {
                        infoVoList.add(list.get(i));
                    }
                }else {
                    otherInfoVoList.add(list.get(i));
                }
                if (list.size()==i+1){//如果同分数的前三名有且只有一个
                    infoList.add(infoVoList);
                }
                if (otherInfoVoList.size()>0){
                    infoList.add(otherInfoVoList);
                }
            }
        }
        G.log("----------x-xx-x" + infoList.size());
        return infoList;
    }
}
