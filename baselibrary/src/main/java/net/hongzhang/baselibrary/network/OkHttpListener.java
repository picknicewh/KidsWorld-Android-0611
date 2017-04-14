package net.hongzhang.baselibrary.network;

import net.hongzhang.baselibrary.mode.Result;

/**
 * HTTP 监听器
 * @author Shurrik
 * 2016年4月28日17:30:14
 */
public interface OkHttpListener<T> {
	void onSuccess(String uri, T date);
	void onError(String uri, Result<String> result);
}
