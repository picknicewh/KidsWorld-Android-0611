package net.hongzhang.message.bean;

/**
 * 作者： wh
 * 时间： 2016/7/14
 * 名称：联系人类
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class GroupMemberBean {
	/**
	 * 联系人的姓名
	 */
	private String name;
	/**
	 * 联系人的姓名拼音的首字母
	 */
	private String sortLetters;
	/**
	 * 联系人的userid
	 */
	private String userid;

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}
	/**
	 * 头像地址
	 */
	private String img;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

}
