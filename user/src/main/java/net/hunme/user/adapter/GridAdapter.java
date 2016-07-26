package net.hunme.user.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import net.hunme.user.R;
import net.hunme.user.util.Bimp;
import net.hunme.user.util.FileUtils;

import java.io.IOException;

/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/18
 * 描    述：用户选取图片之后显示略缩图的适配器
 * 版    本：1.0
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class GridAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private int selectedPosition = -1;
    private boolean shape;
    private Context context;

    public boolean isShape() {
        return shape;
    }

    public void setShape(boolean shape) {
        this.shape = shape;
    }

    public GridAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context=context;
    }

    public void update() {
        loading();
    }

    public int getCount() {
        if(Bimp.bmp.size() == 9){
            return 9;
        }
        return (Bimp.bmp.size() + 1);
    }

    public Object getItem(int arg0) {
        return null;
    }

    public long getItemId(int arg0) {
        return 0;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final int coord = position;
        ViewHolder holder = null;
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.item_published_grida,
                    parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView
                    .findViewById(R.id.item_grida_image);
            holder.clv_delete= (ImageView) convertView.findViewById(R.id.clv_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == Bimp.bmp.size()) {
            holder.image.setImageBitmap(BitmapFactory.decodeResource(
                    context.getResources(), R.mipmap.ic_unfocused));
            holder.clv_delete.setVisibility(View.GONE);
            if (position == 9) {
                holder.image.setVisibility(View.GONE);
            }
        } else {
            holder.clv_delete.setVisibility(View.VISIBLE);
            holder.image.setImageBitmap(Bimp.bmp.get(position));
        }

        holder.clv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bimp.bmp.remove(position);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    public class ViewHolder {
        public ImageView image;
        public ImageView clv_delete;
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    notifyDataSetChanged();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public void loading() {
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    if (Bimp.max == Bimp.tempSelectBitmap.size()) {
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                        break;
                    } else {
                        try {
                            String path = Bimp.tempSelectBitmap.get(Bimp.max);
                            System.out.println(path);
                            Bitmap bm = null;
                            bm = Bimp.revitionImageSize(path);
                            Bimp.bmp.add(bm);
                            String newStr = path.substring(
                                    path.lastIndexOf("/") + 1,
                                    path.lastIndexOf("."));
                            FileUtils.saveBitmap(bm, "" + newStr);
                            Bimp.max += 1;
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }
}
