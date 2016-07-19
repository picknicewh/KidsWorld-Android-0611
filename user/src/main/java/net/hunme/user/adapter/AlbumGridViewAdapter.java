package net.hunme.user.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.hunme.user.R;
import net.hunme.user.mode.ImageItemVo;
import net.hunme.user.util.Bimp;
import net.hunme.user.util.BitmapCache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/18
 * 描    述：显示本地相册所有图片的适配器
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class AlbumGridViewAdapter extends BaseAdapter{
    private List<ImageItemVo>dateList;
    private Context context;
    private BitmapCache cache;
    private DisplayMetrics dm;
    private int selectTotal = 0;
    public static Map<String, String> map;

    public AlbumGridViewAdapter(List<ImageItemVo> dateList,Context context) {
        this.dateList = dateList;
        this.context = context;
        cache=new BitmapCache();
        dm = new DisplayMetrics();
        map = new HashMap<>();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
    }

    @Override
    public int getCount() {
        return dateList.size();
    }

    @Override
    public Object getItem(int i) {
        return dateList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHold viewHold;
        if(null==view){
            view= LayoutInflater.from(context).inflate(R.layout.item_image_grid,null);
            new ViewHold(view);
        }
        viewHold= (ViewHold) view.getTag();
        final ImageItemVo itemVo=dateList.get(i);
        viewHold.ivImage.setTag(itemVo.imagePath);
        cache.displayBmp(viewHold.ivImage, itemVo.thumbnailPath, itemVo.imagePath,
                callback);
        if(itemVo.isSelected){
            viewHold.ivSelect.setImageResource(R.mipmap.ic_pic_select_itme);
            viewHold.tvImageBg.setBackgroundResource(R.drawable.item_pic_selecter);
        }else{
//            viewHold.ivSelect.setImageResource(-1);
            viewHold.tvImageBg.setBackgroundColor(0x00000000);
        }

        viewHold.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String path = dateList.get(i).imagePath;
                if ((Bimp.bmp.size() + selectTotal) < 9) {
                    itemVo.isSelected = !itemVo.isSelected;
                    if (itemVo.isSelected) {
                        viewHold.ivSelect.setImageResource(R.mipmap.ic_pic_select_itme);
                        viewHold.tvImageBg.setBackgroundResource(R.drawable.item_pic_selecter);
                        selectTotal++;
                        map.put(path, path);

                    } else if (!itemVo.isSelected) {
//                        viewHold.ivSelect.setImageResource(-1);
                        viewHold.tvImageBg.setBackgroundColor(0x00000000);
                        selectTotal--;
                        map.remove(path);
                    }
                }else if((Bimp.bmp.size() + selectTotal) >= 9){
                    if (itemVo.isSelected == true) {
                        itemVo.isSelected = !itemVo.isSelected;
//                        viewHold.ivSelect.setImageResource(-1);
                        selectTotal--;
                        map.remove(path);
                    } else {
                        Toast.makeText(context, "最多选择9张图片", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        return view;
    }

    class ViewHold{
         TextView tvImageBg;
         ImageView ivImage;
         ImageView ivSelect;

        public ViewHold(View view) {
            tvImageBg= (TextView) view.findViewById(R.id.tv_image_bg);
            ivImage= (ImageView) view.findViewById(R.id.iv_image);
            ivSelect= (ImageView) view.findViewById(R.id.iv_select);
            view.setTag(this);
        }
    }

    BitmapCache.ImageCallback callback = new BitmapCache.ImageCallback() {
        @Override
        public void imageLoad(ImageView imageView, Bitmap bitmap,
                              Object... params) {
            if (imageView != null && bitmap != null) {
                String url = (String) params[0];
                if (url != null && url.equals(imageView.getTag())) {
                    (imageView).setImageBitmap(bitmap);
                }
            }
        }
    };
}
