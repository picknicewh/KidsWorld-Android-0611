package net.hongzhang.school.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.hongzhang.school.R;
import net.hongzhang.school.activity.OfficeReadActivity;

import java.util.List;

/**
 * 作者： wanghua
 * 时间： 2017/4/19
 * 名称：活动图片详情附件
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */

public class AttacthmentAdapter extends RecyclerView.Adapter<AttacthmentAdapter.ViewHolder> {
    private Context context;
    private int[] images = new int[]{ R.mipmap.ic_word,R.mipmap.ic_ppt, R.mipmap.ic_pdf, R.mipmap.ic_excel};
    private List<String> fileList;
    private onItemClickListener itemClickListener;

    public AttacthmentAdapter(Context context, List<String> fileList) {
        this.context = context;
        this.fileList = fileList;
    }

    @Override
    public AttacthmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                context).inflate(R.layout.item_attachment, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.iv_image.setImageResource(images[position]);
        holder.iv_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OfficeReadActivity.class);
                //  intent.putExtra("path",fileList.get(position));
                context.startActivity(intent);
            }
        });
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
        return images.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_image;
        public TextView tv_file_name;
        public TextView tv_file_size;
        public ViewHolder(View view) {
            super(view);
            iv_image = (ImageView) view.findViewById(R.id.iv_image);
            tv_file_name = (TextView) view.findViewById(R.id.tv_file_name);
            tv_file_size = (TextView) view.findViewById(R.id.tv_file_size);
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
