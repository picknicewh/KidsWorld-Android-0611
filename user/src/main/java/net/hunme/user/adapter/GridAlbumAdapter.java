package net.hunme.user.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.pizidea.imagepicker.ImgLoader;
import com.pizidea.imagepicker.UilImgLoader;
import com.pizidea.imagepicker.bean.ImageItem;

import net.hunme.baselibrary.util.G;
import net.hunme.baselibrary.widget.CircleImageView;
import net.hunme.user.R;

import java.util.List;



/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/25
 * 描    述：
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class GridAlbumAdapter extends BaseAdapter {
    private List<ImageItem> imageItems;
    private Context context;
    private ImgLoader presenter;
    public GridAlbumAdapter(List<ImageItem> imageItems, Context context) {
        this.imageItems = imageItems;
        this.context = context;
        presenter = new UilImgLoader();
    }

    @Override
    public int getCount() {
        if(imageItems.size()==9){
            return  9;
        }
        return (imageItems.size()+1);
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
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_published_grida, null);
            new ViewHolder(convertView);
        }
        holder = (ViewHolder) convertView.getTag();

        if (position == imageItems.size()) {
            holder.image.setImageBitmap(BitmapFactory.decodeResource(
                    context.getResources(), R.mipmap.ic_unfocused));
            holder.clv_delete.setVisibility(View.GONE);
            if (position == 9) {
                holder.image.setVisibility(View.GONE);
            }
        } else {
            holder.clv_delete.setVisibility(View.VISIBLE);
            getBitmapData(holder.image,imageItems.get(position).path);
        }

        holder.clv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageItems.remove(position);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    public class ViewHolder {
        public ImageView image;
        public CircleImageView clv_delete;

        public ViewHolder(View view) {
            image = (ImageView) view.findViewById(R.id.item_grida_image);
            clv_delete= (CircleImageView) view.findViewById(R.id.clv_delete);
            view.setTag(this);
        }
    }

    private void getBitmapData(ImageView imageView ,String imagePath){
        presenter.onPresentImage(imageView,imagePath,G.px2dp(context,800));
    }

}
