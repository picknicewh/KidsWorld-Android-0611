package net.hongzhang.school.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.hongzhang.school.R;

/**
 * ================================================
 * 作    者：wh
 * 时    间：2016/9/19
 * 描    述：学生上传活动列表--适配器
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class PublishListAdapter extends BaseAdapter {
    private Context context;

    public PublishListAdapter(Context context) {
        this.context = context;

    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHold viewHold;
        if (null == view) {
            view = LayoutInflater.from(context).inflate(R.layout.item_publish_list, null);
            new ViewHold(view);
        }
        viewHold = (ViewHold) view.getTag();
        return view;
    }

    class ViewHold {
        TextView tv_publish_time;
        TextView tv_publish_count;

        public ViewHold(View view) {
            tv_publish_time = (TextView) view.findViewById(R.id.tv_publish_time);
            tv_publish_count = (TextView) view.findViewById(R.id.tv_publish_count);
            view.setTag(this);
        }
    }
}
