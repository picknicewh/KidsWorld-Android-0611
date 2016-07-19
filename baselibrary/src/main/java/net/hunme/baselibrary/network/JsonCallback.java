package net.hunme.baselibrary.network;

import com.google.gson.Gson;
import com.lzy.okhttputils.callback.AbsCallback;

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
public abstract class JsonCallback<T> extends AbsCallback<T> {
    private Class<T> mclass;

    public JsonCallback(Class<T> mclass) {
        this.mclass = mclass;
    }

    @Override
    public T parseNetworkResponse(Response response) throws Exception {

        return new Gson().fromJson(response.body().string(),mclass);
    }

}
