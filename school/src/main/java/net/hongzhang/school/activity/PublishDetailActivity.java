package net.hongzhang.school.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.SpeechUtil;
import net.hongzhang.school.R;
import net.hongzhang.school.bean.PublishVo;
import net.hongzhang.school.util.PublishPhotoUtil;

import java.util.ArrayList;

public class PublishDetailActivity extends BaseActivity implements View.OnClickListener {
    private ImageView lv_holad;
    private TextView tv_title;
    private TextView tv_date;
    private TextView tv_content;
    private ImageView iv_image;
    private TextView tv_ptitle;
   private FloatingActionButton fac_btn;
    /**
     * 语音合成对象
     */
    private SpeechSynthesizer mTts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_detail);
        initView();
        initDate();
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        setCententTitle("通知详情");
    }

    private void initView() {
        lv_holad = (ImageView) findViewById(R.id.lv_holad);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_content = (TextView) findViewById(R.id.tv_content);
        iv_image = (ImageView) findViewById(R.id.iv_image);
        tv_ptitle = (TextView) findViewById(R.id.tv_ptitle);
        fac_btn = (FloatingActionButton)findViewById(R.id.fac_btn);
        fac_btn.setOnClickListener(this);
    }
    private void initDate() {
        final PublishVo vo = (PublishVo) getIntent().getSerializableExtra("publish");
        if (G.isEmteny(vo.getImgUrl())) {
            lv_holad.setImageResource(R.mipmap.ic_headmaster);//校长的头像
        } else {
            ImageCache.imageLoade(vo.getImgUrl(),lv_holad);
        }
        if (G.isEmteny(vo.getTsName())) {
            tv_title.setText("校长");
        } else {
            tv_title.setText(vo.getTsName());
        }
        if (vo.getMessageUrl() != null && vo.getMessageUrl().size() > 0) {
            setParam(this,vo.getMessageUrl().get(0), iv_image);
            iv_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PublishPhotoUtil.imageBrowernet(0, (ArrayList<String>) vo.getMessageUrl(), PublishDetailActivity.this);
                }
            });
        }
        if (!G.isEmteny(vo.getTitle())) {
            tv_ptitle.setText(vo.getTitle());
            tv_ptitle.setVisibility(View.VISIBLE);
        } else {
            tv_ptitle.setVisibility(View.GONE);
        }
        tv_date.setText(vo.getDateTime().substring(0, 11));
        tv_content.setText(vo.getMessage());

        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(this, mTtsInitListener);
        SpeechUtil.setSynthesizerParam(mTts);
    }   /**
     * 初始化合成语音监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
                @Override
                public void onInit(int code) {
                  //  Log.d(TAG, "InitListener init() code = " + code);
                    if (code != ErrorCode.SUCCESS) {
                        G.showToast(PublishDetailActivity.this,"初始化失败,错误码："+code);
                    } else {
                        // 初始化成功，之后可以调用startSpeaking方法
                        // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                        // 正确的做法是将onCreate中的startSpeaking调用移至这里
                    }
                }
            };
    /**
     * 设置图片布局参数,根据不同长度的图片不同的布局
     * @param url      图片的url
     * @param iv_image 图片控件
     */
    public static void setParam(final Context context, String url, final ImageView iv_image) {
        Glide.with(context).load(url).asBitmap().fitCenter().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                if (bitmap != null) {
                    int imageWidth = bitmap.getWidth();
                    int imageHeight = bitmap.getHeight();
                    LinearLayout.LayoutParams lp;
                    int viewWidth = G.dp2px(context, 200);
                    int viewHeight = G.dp2px(context, 200);
                    if (imageHeight > imageWidth) {
                        lp = new LinearLayout.LayoutParams(viewWidth, viewHeight*3/2);
                        iv_image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    } else if (imageHeight == imageWidth) {
                        lp = new LinearLayout.LayoutParams(viewWidth, viewWidth);
                        iv_image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    } else {
                        lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, viewHeight);
                        iv_image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }
                    iv_image.setLayoutParams(lp);
                    iv_image.setImageBitmap(bitmap);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.fac_btn){
            int code = mTts.startSpeaking(tv_content.getText().toString(), mTtsListener);
//			/**
//			 * 只保存音频不进行播放接口,调用此接口请注释startSpeaking接口
//			 * text:要合成的文本，uri:需要保存的音频全路径，listener:回调接口
//			*/
//			String path = Environment.getExternalStorageDirectory()+"/tts.pcm";
//			int code = mTts.synthesizeToUri(text, path, mTtsListener);
            if (code != ErrorCode.SUCCESS && code != ErrorCode.ERROR_COMPONENT_NOT_INSTALLED)
                G.showToast(this,"语音合成失败,错误码: " + code);
        }
    }
    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
           // G.showToast(PublishDetailActivity.this,"开始播放");
        }

        @Override
        public void onSpeakPaused() {
       //     G.showToast(PublishDetailActivity.this,"暂停播放");
        }

        @Override
        public void onSpeakResumed() {
          //  G.showToast(PublishDetailActivity.this,"继续播放");
        }
        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
            // 合成进度
        }
        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
        }
        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
              //  G.showToast(PublishDetailActivity.this,"播放完成");
            } else if (error != null) {
                G.showToast(PublishDetailActivity.this,error.getPlainDescription(true));
            }
        }
        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if( null != mTts ){
            mTts.stopSpeaking();
            // 退出时释放连接
            mTts.destroy();
        }
    }
}


