package net.hongzhang.bbhow.speech;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.hongzhang.bbhow.R;

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
public class SpeechAdapter extends RecyclerView.Adapter<SpeechAdapter.ViewHolder> {
    private Context context;
    private List<String> speechList;
    private onItemClickListener itemClickListener = null;
    private ViewHolder holder;

    public SpeechAdapter(Context context, List<String> speechList) {
        this.context = context;
        this.speechList = speechList;
    }

    @Override
    public SpeechAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_speech_list, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(final SpeechAdapter.ViewHolder holder, final int position) {
        holder.tv_content.setText(speechList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener != null) {
                    itemClickListener.OnItemClick(view, position);
                }
            }
        });
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public int getItemCount() {
        return speechList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_content;

        public ViewHolder(View view) {
            super(view);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
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
