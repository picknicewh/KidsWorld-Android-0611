package net.hongzhang.user.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.pizidea.imagepicker.ImgLoader;
import com.pizidea.imagepicker.UilImgLoader;

import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.user.R;

import java.util.ArrayList;
import java.util.List;

import main.picturesee.util.ImagePagerActivity;

/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/28
 * 描    述：
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class AlbumDetailsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String>imagePath;
    private ImgLoader presenter;
    public AlbumDetailsAdapter(Context context, ArrayList<String> imagePath) {
        this.context = context;
        this.imagePath = imagePath;
        presenter = new UilImgLoader();
    }

    @Override
    public int getCount() {
        return imagePath.size();
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
        if(view==null){
            view= LayoutInflater.from(context).inflate(R.layout.item_album_details,null);
            new ViewHolder(view);
        }
        holder= (ViewHolder) view.getTag();
        ImageCache.imageLoader(imagePath.get(i),holder.image);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageBrower(i,getimagepath(imagePath));
            }
        });
//        getBitmapData(holder.image,imagePath.get(i));
        return view;
    }
    /**
     * 把缩略图转换成为原图
     */
    private ArrayList<String> getimagepath(List<String> itemList){
        ArrayList<String> imagepath = new ArrayList<>();
        for (int i = 0 ; i<itemList.size();i++){
            String pathurl = itemList.get(i);
            int index = itemList.get(i).lastIndexOf("/");
            String path = pathurl.substring(0,index-2)+pathurl.substring(index,pathurl.length());
            imagepath.add(path);
        }
        return imagepath;

    }
    /**
     * 浏览照片
     * @param  position
     * @param  urls2
     */
    protected void imageBrower(int position, ArrayList<String> urls2) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        intent.putExtra("source","net");
        context.startActivity(intent);
    }
    class ViewHolder {
        public ImageView image;
        public ViewHolder(View view) {
            image = (ImageView) view.findViewById(R.id.iv_image);
            view.setTag(this);
        }
    }

    private void getBitmapData(ImageView imageView ,String imagePath){
        presenter.onPresentImage(imageView,imagePath, G.px2dp(context,800));
    }
}
