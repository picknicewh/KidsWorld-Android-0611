package net.hunme.kidsworld.base;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.hunme.kidsworld.R;
import net.hunme.kidsworld.widget.NavigationBar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者： Administrator
 * 时间： 2016/7/14
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class MessageFragement extends Fragment {
    @Bind(R.id.nb_message)
    NavigationBar nbMessage;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, null);
        ButterKnife.bind(this, view);
        nbMessage.setTitle("消息");

        return view;

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
