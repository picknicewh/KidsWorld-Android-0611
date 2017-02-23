package net.hongzhang.baselibrary.network;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cache.CacheMode;
import com.lzy.okhttputils.callback.AbsCallback;
import com.lzy.okhttputils.model.HttpParams;
import com.lzy.okhttputils.request.PostRequest;

import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.util.G;

import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * ================================================
 * 作    者： ZLL
 * 时    间： 2016/7/8
 * 描    述：
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class OkHttps {
    private static OkHttpUtils httpUtils;
    public static PostRequest postRequest;
    private static boolean isSuccess; //服务端返回状态
    private static String uri_host = ServerConfigManager.SERVER_IP;
    private static final String ERRORPROMPT = "与服务器连接异常，请检查网络后重试！";
    private static boolean isSendError;

    private static OkHttpUtils getInstance() {
        if (null == httpUtils) {
            httpUtils = OkHttpUtils.getInstance()
                    .setCertificates()          // 自签名https的证书，可变参数，可以设置多个
                    .debug("OkHttpUtils")//是否打开调试
                    .setConnectTimeout(OkHttpUtils.DEFAULT_MILLISECONDS)               //全局的连接超时时间
                    .setReadTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS)                  //全局的读取超时时间
                    .setWriteTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS);
        }
        return httpUtils;
    }

    /**
     * 没有缓存的请求
     *
     * @param uri            请求地址
     * @param map            请求参数
     * @param okHttpListener 请求回调对象
     */
    public static void sendPost(Type type, final String uri, Map<String, Object> map, final OkHttpListener okHttpListener) {
        if (null == uri || okHttpListener == null) {
            G.log("参数或者访问地址为空");
            return;
        }
        //进行网络请求
        postRequest = getInstance()
                .post(uri_host + uri);
        doInternet(type, uri, getParams(map), okHttpListener);
    }
    /**
     * 上传文件
     *
     * @param type
     * @param uri
     * @param map
     * @param filelist
     * @param okHttpListener
     */
    public static void sendPost(Type type, final String uri, Map<String, Object> map, List<File> filelist, final OkHttpListener okHttpListener) {
        if (null == uri || okHttpListener == null) {
            G.log("参数或者访问地址为空");
            return;
        }
        //进行网络请求
        postRequest = getInstance()
                .post(uri_host + uri);
        // 分批次上传文件给服务器   一起打包发送给服务器的话 服务器接受到最后一个文件
        for (int i = 0; i < filelist.size(); i++) {
            postRequest.params(i + "", filelist.get(i));
        }
        doInternet(type, uri, getParams(map), okHttpListener);
    }

    /**
     * 带缓存的请求
     *
     * @param uri            请求地址
     * @param map            请求参数
     * @param okHttpListener 请求回调对象
     * @param cacheMode      缓存模式
     * @param cacheKey       缓存名
     */
    public static void sendPost(Type type, final String uri, Map<String, Object> map,
                                final OkHttpListener okHttpListener, int cacheMode, String cacheKey) {
        if (null == uri || okHttpListener == null) {
            G.log("参数或者访问地址为空");
            return;
        }
//        DEFAULT: 按照HTTP协议的默认缓存规则，例如有304响应头时缓存
//        REQUEST_FAILED_READ_CACHE：先请求网络，如果请求网络失败，则读取缓存，如果读取缓存失败，本次请求失败。
//                                   该缓存模式的使用，会根据实际情况，导致onResponse,onError,onAfter三个方法调用不只一次，
//                                   具体请在三个方法返回的参数中进行判断。
//        IF_NONE_CACHE_REQUEST：如果缓存不存在才请求网络，否则使用缓存。
//        FIRST_CACHE_THEN_REQUEST：先使用缓存，不管是否存在，仍然请求网络，如果网络顺利，会导致onResponse方法执行两次，第一次isFromCache为true，第二次isFromCache为false。
//                                 使用时根据实际情况，对onResponse,onError,onAfter三个方法进行具体判断。
        CacheMode mode;
        switch (cacheMode) {
            case 1:
                mode = CacheMode.DEFAULT;
                break;
            case 2:
                mode = CacheMode.REQUEST_FAILED_READ_CACHE;
                break;
            case 3:
                mode = CacheMode.IF_NONE_CACHE_REQUEST;
                break;
            default:
                mode = CacheMode.FIRST_CACHE_THEN_REQUEST;
                break;
        }
        postRequest = getInstance()
                .post(uri_host + uri)
                .cacheMode(mode)
                .cacheKey(cacheKey);
        doInternet(type, uri, getParams(map), okHttpListener);
    }

    /**
     * 网络请求回调
     * @param type
     * @param uri
     * @param params
     * @param okHttpListener
     */
    private static void doInternet(final Type type, final String uri, HttpParams params, final OkHttpListener okHttpListener) {
        postRequest.params(params)
                .execute(new AbsCallback<Object>() {
                    @Override
                    public Object parseNetworkResponse(Response response) throws Exception {
                        String value = response.body().string();
                        JSONObject jsonObject = new JSONObject(value);
                        isSuccess = "0".equals(jsonObject.getString("code"));
                        isSendError = false; //初始化默认发送过错误
                        //是否成功访问（服务端是不是正常返回值 如果不是正常返回一般date都是String类型 直接去解析 否则 按照所传的格式解析）
                        if (isSuccess)
                            return new Gson().fromJson(value, type);
                        else
                            return new Gson().fromJson(value, new TypeToken<Result<String>>() {
                            }.getType());
                    }

                    @Override
                    public void onResponse(boolean isFromCache, Object o, Request request, @Nullable Response response) {
//                            Result result= (Result) o;
//                            Map<String,Object>map=new HashMap<>();
//                            map.put("data",result.getData());
                        //验签
//                            boolean isSuccess=EncryptUtil.verify(map,result.getMsec(),result.getSign());
//                            if(isSuccess)

                        //服务端正常返回或者是缓存的 请求成功
                        if (isSuccess || isFromCache)
                            okHttpListener.onSuccess(uri, o);
                        else if (!isSendError) //否则请求失败  但是必须是之前没有发送错误信息  不能重复发送 会报错
                            try {
                                okHttpListener.onError(uri, ((Result<String>) o).getData());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

//                            else
//                               okHttpListener.onError(uri,"非法访问");
                    }

                    @Override
                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                        super.onError(isFromCache, call, response, e);
                        okHttpListener.onError(uri, ERRORPROMPT);
                        isSendError = true; //请求失败 并且告诉onResponse已经发送过无需发送
                    }

                    @Override
                    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        super.upProgress(currentSize, totalSize, progress, networkSpeed);
                    }
                });
    }

    /**
     * 组合访问参数
     * @param map 参数map对象
     * @return httpParams 对象
     */
    private static HttpParams getParams(Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        //将参数加签
//        String msec = String.valueOf(System.currentTimeMillis());
//        String sign = EncryptUtil.getSign(map, msec);

        //将map转化成httpParams
        HttpParams params = new HttpParams();
        Set<Map.Entry<String, Object>> set = map.entrySet();
        Iterator<Map.Entry<String, Object>> iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            params.put(entry.getKey(), entry.getValue().toString());
            G.log(entry.getKey() + "------请求参数-------" + entry.getValue());
        }
//        params.put("msec",msec);
//        params.put("sign",sign);
//        G.log("-----sign---------"+sign);
//        G.log("-----msec---------"+msec);
        return params;
    }
}
