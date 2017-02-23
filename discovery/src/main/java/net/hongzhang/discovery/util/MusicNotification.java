package net.hongzhang.discovery.util;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.RemoteViews;

import net.hongzhang.baselibrary.mode.ResourceVo;
import net.hongzhang.discovery.R;

/**
 * 作者： wh
 * 时间： 2016/11/11
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class MusicNotification {
    private RemoteViews mContentViewBig, mContentViewSmall;
    private Context context;
    private ResourceVo resourceVo;
    private  boolean isPlaying;
    private Bitmap bitmap;
    public MusicNotification(Context context , ResourceVo resourceVo, boolean isPlaying, Bitmap bitmap){
        this.context  = context;
        this.resourceVo= resourceVo;
        this.isPlaying = isPlaying;
        this.bitmap = bitmap;
    }
    public RemoteViews getSmallContentView() {
        if (mContentViewSmall == null) {
            mContentViewSmall = new RemoteViews(context.getPackageName(), R.layout.remote_view_music_player_small);
            setUpRemoteView(mContentViewSmall);
        }
        updateRemoteViews(mContentViewSmall);
        return mContentViewSmall;
    }
    public RemoteViews getBigContentView() {
        if (mContentViewBig == null) {
            mContentViewBig = new RemoteViews(context.getPackageName(), R.layout.remote_view_music_player);
            setUpRemoteView(mContentViewBig);
        }
        updateRemoteViews(mContentViewBig);
        return mContentViewBig;
    }
    private void setUpRemoteView(RemoteViews remoteView) {

        remoteView.setImageViewResource(R.id.image_view_close, R.drawable.ic_remote_view_close);
        remoteView.setImageViewResource(R.id.image_view_play_last, R.drawable.ic_remote_view_play_last);
        remoteView.setImageViewResource(R.id.image_view_play_next, R.drawable.ic_remote_view_play_next);
        remoteView.setOnClickPendingIntent(R.id.button_close, getPendingIntent(Constants.CLOSE));
        remoteView.setOnClickPendingIntent(R.id.button_play_last, getPendingIntent(Constants.LAST));
        remoteView.setOnClickPendingIntent(R.id.button_play_next, getPendingIntent(Constants.NEXT));
        remoteView.setOnClickPendingIntent(R.id.button_play_toggle, getPendingIntent(Constants.PLAYING));
    }
    private void updateRemoteViews(RemoteViews remoteView) {
        remoteView.setTextViewText(R.id.text_view_name,resourceVo.getResourceName());
        remoteView.setTextViewText(R.id.text_view_artist, resourceVo.getCreateTime());
        remoteView.setImageViewResource(R.id.image_view_play_toggle, isPlaying
                ? R.drawable.ic_remote_view_pause : R.drawable.ic_remote_view_play);
        if (bitmap == null) {
            remoteView.setImageViewResource(R.id.image_view_album, R.mipmap.ic_album);
        } else {
           remoteView.setImageViewBitmap(R.id.image_view_album, bitmap);
        }
   }
    // PendingIntent
      private PendingIntent getPendingIntent(int op) {
          Intent intent = new Intent();
          intent.setAction(Constants.ACTION_NOTIFICATION);
          intent.putExtra("op",op);
         return  PendingIntent.getBroadcast(context,op,intent,PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
