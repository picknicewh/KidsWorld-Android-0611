package net.hongzhang.discovery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.hongzhang.discovery.R;
import net.hongzhang.discovery.modle.SearchKeyVo;

import java.util.List;

/**
 * ================================================
 * 作    者：wh
 * 时    间：2016/11/28
 * 描    述：
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class SearchHistoryAdapter extends BaseAdapter {
    private Context context;
    private List<SearchKeyVo> searchKeyVos;

    public SearchHistoryAdapter(Context context, List<SearchKeyVo> searchKeyVos) {
        this.context = context;
        this.searchKeyVos = searchKeyVos;
    }

    @Override
    public int getCount() {
        return searchKeyVos.size();
    }

    @Override
    public Object getItem(int i) {
        return searchKeyVos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_search_history, null);
            new ViewHolder(view);
        }
        holder = (ViewHolder) view.getTag();
        SearchKeyVo searchKeyVo = searchKeyVos.get(position);
        holder.tv_search_key.setText(searchKeyVo.getKey());
        return view;
    }


    class ViewHolder {
        private TextView tv_search_key;
        public ViewHolder(View view) {
            tv_search_key = (TextView) view.findViewById(R.id.tv_search_key);
            view.setTag(this);
        }
    }
}
