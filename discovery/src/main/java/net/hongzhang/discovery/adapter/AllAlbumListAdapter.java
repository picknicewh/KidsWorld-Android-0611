package net.hongzhang.discovery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.discovery.R;
import net.hongzhang.discovery.modle.ThemeVo;

import java.util.List;

/**
 * Created by wanghua on 2016/12/21.
 */
public class AllAlbumListAdapter extends BaseAdapter {
    private Context context;
    private List<ThemeVo> themeVos;

    public AllAlbumListAdapter(Context context, List<ThemeVo> themeVos) {
        this.context = context;
        this.themeVos = themeVos;
    }

    @Override
    public int getCount() {
        return themeVos.size();
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
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_all_album, null);
            new ViewHolder(view);
        }
        holder = (ViewHolder) view.getTag();
        ThemeVo themeVo = themeVos.get(i);
        ImageCache.imageLoader(themeVo.getImageUrl(), holder.iv_album_image);
        return view;
    }

    class ViewHolder {
        private ImageView iv_album_image;

        public ViewHolder(View view) {
            iv_album_image = (ImageView) view.findViewById(R.id.iv_album_image);
            view.setTag(this);
        }
    }
}
