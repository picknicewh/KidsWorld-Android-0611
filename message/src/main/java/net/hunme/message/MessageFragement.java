package net.hunme.message;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 作者： Administrator
 * 时间： 2016/7/14
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class MessageFragement extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, null);
        return view;
    }

}
