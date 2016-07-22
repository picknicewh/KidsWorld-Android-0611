package net.hunme.user.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import net.hunme.user.activity.AlbumActivity;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.concurrent.Executors;

/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/18
 * 描    述：图片缓存类
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class BitmapCache extends Activity {

	public Handler h = new Handler();
	public final String TAG = getClass().getSimpleName();
	private HashMap<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();

	public void put(String path, Bitmap bmp) {
		if (!TextUtils.isEmpty(path) && bmp != null) {
			imageCache.put(path, new SoftReference<Bitmap>(bmp));
		}
	}

	/**
	 *  将图片转化成略缩图 并显示图片
	 * @param iv 显示图片空间 imageView
	 * @param thumbPath 小图路径
	 * @param sourcePath 原图路径
     * @param callback 返回对象
     */
	public void displayBmp(final ImageView iv, final String thumbPath,
						   final String sourcePath, final ImageCallback callback) {
		if (TextUtils.isEmpty(thumbPath) && TextUtils.isEmpty(sourcePath)) {
			Log.e(TAG, "no paths pass in");
			return;
		}

		final String path; //图片路径
		final boolean isThumbPath; //是否做过处理（也可以认为是否第一次加载这张图）
		if (!TextUtils.isEmpty(thumbPath)) {
			path = thumbPath;
			isThumbPath = true;
		} else if (!TextUtils.isEmpty(sourcePath)) {
			path = sourcePath;
			isThumbPath = false;
		} else {
			// iv.setImageBitmap(null);
			return;
		}

		if (imageCache.containsKey(path)) {
			SoftReference<Bitmap> reference = imageCache.get(path);
			Bitmap bmp = reference.get();
			if (bmp != null) {
				if (callback != null) {
					callback.imageLoad(iv, bmp, sourcePath);
				}
				iv.setImageBitmap(bmp);
				Log.d(TAG, "hit cache");
				return;
			}
		}
//		iv.setImageBitmap(null);
		Executors.newFixedThreadPool(5).execute(new Runnable() {
			Bitmap thumb;
			@Override
			public void run() {
				try {
					if (isThumbPath) {
						thumb = BitmapFactory.decodeFile(thumbPath);
						if (thumb == null) {
							thumb = Bimp.revitionImageSize(sourcePath);
						}
					} else {
						thumb = Bimp.revitionImageSize(sourcePath);
					}
				} catch (Exception e) {

				}
				if (thumb == null) {
					thumb = AlbumActivity.bimap;
				}
				put(path, thumb);
				if (callback != null) {
					h.post(new Runnable() {
						@Override
						public void run() {
							callback.imageLoad(iv, thumb, sourcePath);
						}
					});
				}
			}
		});

	}

//	public Bitmap revitionImageSize(String path) throws IOException {
//		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
//				new File(path)));
//		BitmapFactory.Options options = new BitmapFactory.Options();
//		options.inJustDecodeBounds = true;
//		BitmapFactory.decodeStream(in, null, options);
//		in.close();
//		int i = 0;
//		Bitmap bitmap = null;
//		while (true) {
//			if ((options.outWidth >> i <= 256)
//					&& (options.outHeight >> i <= 256)) {
//				in = new BufferedInputStream(
//						new FileInputStream(new File(path)));
//				options.inSampleSize = (int) Math.pow(2.0D, i);
//				options.inJustDecodeBounds = false;
//				bitmap = BitmapFactory.decodeStream(in, null, options);
//				break;
//			}
//			i += 1;
//		}
//		return bitmap;
//	}

	public interface ImageCallback {
		 void imageLoad(ImageView imageView, Bitmap bitmap, Object... params);
	}
}
