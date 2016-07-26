package net.hunme.baselibrary.network;

import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cache.CacheMode;
import com.lzy.okhttputils.callback.AbsCallback;
import com.lzy.okhttputils.model.HttpParams;
import com.lzy.okhttputils.request.PostRequest;

import net.hunme.baselibrary.mode.Result;
import net.hunme.baselibrary.util.EncryptUtil;
import net.hunme.baselibrary.util.G;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
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
public class OkHttps<T> {
    private static OkHttpUtils httpUtils;
    public static PostRequest postRequest;
    //ServerConfigManager.SERVER_IP;
    private static String uri_host=ServerConfigManager.SERVER_IP;
    private static OkHttpUtils getInstance() {
        if(null==httpUtils){
            OkHttpUtils.getInstance().getOkHttpClient();
            httpUtils =OkHttpUtils.getInstance()
//                    .setCertificates("")          // 自签名https的证书，可变参数，可以设置多个
                    .debug("OkHttpUtils")                                              //是否打开调试
                    .setConnectTimeout(OkHttpUtils.DEFAULT_MILLISECONDS)               //全局的连接超时时间
                    .setReadTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS)                  //全局的读取超时时间
                    .setWriteTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS);
        }
        return httpUtils;
    }


//    public static OkHttps getIntence() {
////        OkHttps.mClass = mClass;
//        if(okHttps==null){
//            okHttps= new OkHttps();
//        }
//        return okHttps;
//    }

    /**
     *  没有缓存的请求
     * @param uri 请求地址
     * @param map 请求参数
     * @param okHttpListener 请求回调对象
     */
    public static void sendPost(Type type, final String uri, Map<String, Object> map, final OkHttpListener okHttpListener) {
        if(null==uri||okHttpListener==null) {
            G.log("参数或者访问地址为空");
            return;
        }

        //进行网络请求
        postRequest= getInstance()
                .post(ServerConfigManager.SERVER_IP+uri);
        doInternet(type,uri,getParams(map),okHttpListener);
    }

    /**
     * 带缓存的请求
     * @param uri 请求地址
     * @param map  请求参数
     * @param okHttpListener  请求回调对象
     * @param cacheMode   缓存模式
     * @param cacheKey  缓存名
     */
    public static void sendPost(final String uri, Map<String,Object> map,
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

        postRequest=getInstance()
                .post(uri_host+uri)
                .cacheMode(mode)
                .cacheKey(cacheKey);
//        doInternet(uri,getParams(map),okHttpListener);
    }

    private static void doInternet(final Type type, final String uri, HttpParams params, final OkHttpListener okHttpListener){
            postRequest.params(params)
                    .execute(new AbsCallback<Object>(){
                        @Override
                        public Object parseNetworkResponse(Response response) throws Exception {
                            String value="{\"code\":\"123\",\"data\":{},\"msec\":\"133\",\"sign\":\"455\"}";
                            Log.i("TAGFFF",response.body().string());
                            return new Gson().fromJson(response.body().toString(),type);
                        }

                        @Override
                        public void onResponse(boolean isFromCache, Object o, Request request, @Nullable Response response) {
                            Result result= (Result) o;
                            Map<String,Object>map=new HashMap<>();
                            map.put("data",result.getData());
                            //验签
                            boolean isSuccess=EncryptUtil.verify(map,result.getMsec(),result.getSign());
//                            if(isSuccess)
                                okHttpListener.onSuccess(uri,result);
//                            else
//                                okHttpListener.onError(uri,"非法访问");
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

    /**
     *  组合访问参数
     * @param map 参数map对象
     * @return httpParams 对象
     */
    private static HttpParams getParams(Map<String,Object> map){
        if(map==null){
            return  null;
        }
        //将参数加签
        String msec = String.valueOf(System.currentTimeMillis());
        String sign = EncryptUtil.getSign(map, msec);

        //将map转化成httpParams
        HttpParams params=new HttpParams();
        Set<Map.Entry<String, Object>> set = map.entrySet();
        Iterator<Map.Entry<String, Object>> iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            params.put(entry.getKey(), entry.getValue().toString());
        }
        params.put("msec",msec);
        params.put("sign",sign);
        G.log("-----sign---------"+sign);
        G.log("-----msec---------"+msec);
        return  params;
    }


}
