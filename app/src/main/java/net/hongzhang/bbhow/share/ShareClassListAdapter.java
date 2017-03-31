package net.hongzhang.bbhow.share;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.hongzhang.bbhow.R;
import net.hongzhang.status.mode.DynamicVo;

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
public class ShareClassListAdapter extends RecyclerView.Adapter<ShareClassListAdapter.ViewHolder> {
    private Context context;
    private List<DynamicVo> dynamicVos;
    private onItemClickListener itemClickListener = null;
    private ViewHolder holder;

    public ShareClassListAdapter(Context context, List<DynamicVo> dynamicVos) {
        this.context = context;
        this.dynamicVos = dynamicVos;
    }

    @Override
    public ShareClassListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_share_classlist, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(final ShareClassListAdapter.ViewHolder holder, final int position) {
        DynamicVo dynamicVo = dynamicVos.get(position);
        holder.rb_class.setText(dynamicVo.getGroupName());
        holder.ll_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener != null) {
                    itemClickListener.OnItemClick(view, position);
                }
            }
        });
        this.holder = holder;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public int getItemCount() {
        return dynamicVos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView rb_class;
        private LinearLayout ll_room;

        public ViewHolder(View view) {
            super(view);
            rb_class = (TextView) view.findViewById(R.id.rb_class);
            ll_room = (LinearLayout) view.findViewById(R.id.ll_room);
            view.setTag(this);
        }
    }
    public void setOnItemClickListener(onItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    public interface onItemClickListener {
        void OnItemClick(View view, int position);
    }
}
