package net.hunme.baselibrary.network;

import android.support.annotation.Nullable;

import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cache.CacheMode;
import com.lzy.okhttputils.callback.AbsCallback;
import com.lzy.okhttputils.model.HttpParams;
import com.lzy.okhttputils.request.PostRequest;

import net.hunme.baselibrary.util.G;

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
public class OkHttps<T> {
    private static OkHttpUtils httpUtils;
    public static PostRequest postRequest;
    private static Class tClass;
    private static boolean isResString=true;
    private static OkHttps okHttps;
    private String uri_host=ServerConfigManager.SERVER_IP;
    private static OkHttpUtils getInstance() {
        if(null==httpUtils){
            OkHttpUtils.getInstance().getOkHttpClient();
            httpUtils =OkHttpUtils.getInstance()
                    .setCertificates("")          // 自签名https的证书，可变参数，可以设置多个
                    .debug("OkHttpUtils")                                              //是否打开调试
                    .setConnectTimeout(OkHttpUtils.DEFAULT_MILLISECONDS)               //全局的连接超时时间
                    .setReadTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS)                  //全局的读取超时时间
                    .setWriteTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS);
        }
        return httpUtils;
    }

    public static OkHttps setClass(){
        isResString=true;
        return init();
    }

    public static OkHttps setClass(Class mclass){
        isResString=false;
        tClass=mclass;
        return init();
    }

    private static OkHttps init() {
        if(okHttps==null){
            okHttps=new OkHttps<>();
        }
        return  okHttps;
    }

    /**
     *  没有缓存的请求
     * @param uri 请求地址
     * @param params 请求参数
     * @param okHttpListener 请求回调对象
     */
    public  void sendPost(final String uri, HttpParams params, final OkHttpListener okHttpListener) {
        if(null==uri||okHttpListener==null) {
            G.log("参数或者访问地址为空");
            return;
        }
        postRequest= OkHttps.getInstance()
                .post(uri_host+uri);
        doInternet(uri,params,okHttpListener);
    }

    /**
     * 带缓存的请求
     * @param uri 请求地址
     * @param params  请求参数
     * @param okHttpListener  请求回调对象
     * @param cacheMode   缓存模式
     * @param cacheKey  缓存名
     */
    public  void sendPost(final String uri,HttpParams params,
                          final OkHttpListener okHttpListener, int cacheMode, String cacheKey) {
        if(null==uri||okHttpListener==null) {
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
        switch (cacheMode){
            case 1:
                mode=CacheMode.DEFAULT;
                break;
            case 2:
                mode=CacheMode.REQUEST_FAILED_READ_CACHE;
                break;
            case 3:
                mode=CacheMode.IF_NONE_CACHE_REQUEST;
                break;
            default:
                mode=CacheMode.FIRST_CACHE_THEN_REQUEST;
                break;
        }

        postRequest= OkHttps.getInstance()
                .post(uri_host+uri)
                .cacheMode(mode)
                .cacheKey(cacheKey);
        doInternet(uri,params,okHttpListener);
    }

    private void doInternet(final String uri, HttpParams params,
                            final OkHttpListener okHttpListener){

        if(isResString)
            postRequest.params(params)
                    .execute(new AbsCallback<String>() {
                        @Override
                        public String parseNetworkResponse(Response response) throws Exception {
                            return response.body().string();
                        }

                        @Override
                        public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                            okHttpListener.onSuccess(uri,s);
                        }

                        @Override
                        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                            super.onError(isFromCache, call, response, e);
                            if(null==e)
                                okHttpListener.onError(uri,response.networkResponse().toString());
                            else
                                okHttpListener.onError(uri,e.toString());
                        }
                    });
        else
            postRequest.params(params)
                    .execute(new JsonCallback<Class>(tClass){

                        @Override
                        public void onResponse(boolean isFromCache, Object o, Request request, @Nullable Response response) {
                            okHttpListener.onSuccess(uri,o);
                        }

                        @Override
                        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                            super.onError(isFromCache, call, response, e);
                            if(null==e)
                                okHttpListener.onError(uri,response.networkResponse().toString());
                            else
                                okHttpListener.onError(uri,e.toString());
                        }
                    });
    }


}
