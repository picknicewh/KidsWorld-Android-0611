package net.hongzhang.discovery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.hongzhang.baselibrary.image.GlideUtils;
import net.hongzhang.discovery.R;
import net.hongzhang.discovery.modle.ConsultInfoVo;

import java.util.List;

/**
 * ================================================
 * 作    者：wh
 * 时    间：2016/7/28
 * 描    述：
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class MainConsultAdapter extends BaseAdapter {
    private Context context;
    private List<ConsultInfoVo> consultInfoVos;
    public MainConsultAdapter(Context context,  List<ConsultInfoVo> consultInfoVos) {
        this.context = context;
        this.consultInfoVos = consultInfoVos;
    }

    @Override
    public int getCount() {
        return consultInfoVos.size();
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
            view = LayoutInflater.from(context).inflate(R.layout.item_main_consult, null);
            new ViewHolder(view);
        }
        holder = (ViewHolder) view.getTag();
        ConsultInfoVo consultInfoVo = consultInfoVos.get(i);
        GlideUtils.loadImageView(context,consultInfoVo.getImageUrl(),holder.iv_image);
        holder.tv_title.setText(consultInfoVo.getResourceName());
        return view;
    }
    class ViewHolder {
        public ImageView iv_image;
        private TextView tv_title;

        public ViewHolder(View view) {
            iv_image = (ImageView) view.findViewById(R.id.iv_image);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            view.setTag(this);
        }
    }
}