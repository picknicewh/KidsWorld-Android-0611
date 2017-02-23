package net.hongzhang.message.util;


import net.hongzhang.message.bean.GroupMemberBean;

import java.util.Comparator;

/**
 * 作者： wh
 * 时间： 2016/7/14
 * 名称：根据拼音首字母-比较联系人排序
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class PinyinComparator implements Comparator<GroupMemberBean> {

	public int compare(GroupMemberBean o1, GroupMemberBean o2) {
		if (o1.getSortLetters().equals("@")
				|| o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}
