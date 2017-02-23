package net.hongzhang.baselibrary.util.ewcode;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultPointCallback;
import com.google.zxing.common.HybridBinarizer;

import java.util.Hashtable;

/**
 * 作者： wh
 * 时间： 2016/11/4
 * 名称：扫描二维码解析
 * 版本说明：
 * 附加注释：由于解析二维码需要很长的时间所以，通过一个异步线程解析二维码图片
 * 主要接口：
 */
public class DecodeThread  extends AsyncTask<Void ,Void ,Result> {
    private DecodeListener listener;
     private LuminanceSource luminanceSource;
     private Bitmap mbitmap;
    private boolean isStop = false;
    public DecodeThread(DecodeListener listener, LuminanceSource luminanceSource){
        this.listener = listener;
        this.luminanceSource = luminanceSource;
    }

    @Override
    protected Result doInBackground(Void... params) {

        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(luminanceSource));
        Hashtable<DecodeHintType, Object> hints = new Hashtable<>(3);
        hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(DecodeHintType.NEED_RESULT_POINT_CALLBACK, listener);
        MultiFormatReader multiFormatReader = new MultiFormatReader();
        multiFormatReader.setHints(hints);
        Result rawResult = null;
        try {
            rawResult = multiFormatReader.decodeWithState(bitmap);
            mbitmap = luminanceSource.renderCroppedGreyScaleBitmap();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }finally {
            multiFormatReader.reset();
        }
        return rawResult;
    }

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        if (listener !=null  && !isStop){
            if (result!=null){
                listener.onDecodeSuccess(luminanceSource,result,mbitmap);
            }else {
                listener.onDecodeFailed(luminanceSource);
            }
        }
    }
    public void cancel() {
        isStop = true;
        cancel(true);
    }
    public  interface  DecodeListener  extends ResultPointCallback {
        void onDecodeSuccess(LuminanceSource luminanceSource, Result result, Bitmap bitmap);
        void onDecodeFailed(LuminanceSource luminanceSource);
    }
}
