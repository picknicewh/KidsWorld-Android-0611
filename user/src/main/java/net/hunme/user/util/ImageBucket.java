package net.hunme.user.util;

import net.hunme.user.mode.ImageItemVo;

import java.util.List;
/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/18
 * 描    述：用户本地单个相册信息实体类
 * 版    本：1.0
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class ImageBucket {
	public int count = 0; //图片数量
	public String bucketName;  //相册名字
	public List<ImageItemVo> imageList; //改相册所有相片数量
}
