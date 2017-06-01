package net.hongzhang.bbhow.speech;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.bbhow.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SpeechActivity extends BaseActivity {
    @Bind(R.id.iv_speech)
    ImageView ivSpeech;
    /**
     * 会话列表
     */
    @Bind(R.id.rv_speech)
    RecyclerView rvSpeech;
    private String TAG = SpeechActivity.class.getSimpleName();
    @Bind(R.id.tv_content)
    TextView tvContent;
    /**
     * 语音听写对象
     */
    private SpeechRecognizer mIat;
    /**
     * 语音听写UI
     */
    private RecognizerDialog mIatDialog;
    /**
     * 用HashMap存储听写结果
     */
    private HashMap<String, String> mIatResults;
    /**
     * 引擎类型
     */
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    private SpeechAdapter adapter;
    private List<String> itemList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speek);
        ButterKnife.bind(this);
        intData();
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setCententTitle("语音听写");
        setLiftOnClickClose();
    }

    private void intData() {
        mIatResults = new LinkedHashMap<>();
        itemList = new ArrayList<>();
        adapter = new SpeechAdapter(this,itemList);

        rvSpeech.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        rvSpeech.setAdapter(adapter);
        mIat = SpeechRecognizer.createRecognizer(this, mInitListener);

    }

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败，错误码：" + code);
            }
        }
    };

    private void showTip(String content) {
        G.showToast(SpeechActivity.this, content);
    }


    /**
     * 参数设置
     *
     * @paramm Iat 语音解析类
     */
    public void setParam(SpeechRecognizer mIat) {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);
        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
        String lag = "mandarin";
        if (lag.equals("en_us")) {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
        } else {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, lag);
        }
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");
        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "1500");
        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "1");
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mIat) {
            // 退出时释放连接
            mIat.cancel();
            mIat.destroy();
        }
    }

    /**
     * 函数调用返回值
     */
    int ret = 0;
    private int position=-1;
    @OnClick(R.id.iv_speech)
    public void onViewClicked() {
        if (null == mIat) {
            // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
            this.showTip("创建对象失败，请确认 libmsc.so 放置正确，且有调用 createUtility 进行初始化");
            return;
        }
        setParam(mIat);
        // 不显示听写对话框
        ret = mIat.startListening(mRecognizerListener);
        if (ret != ErrorCode.SUCCESS) {
            showTip("听写失败,错误码：" + ret);
        } else {
            showTip("请开始说话…");
        }
    }
     private String speechContent="";
    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            showTip("开始说话");
           // tvContent.setText("");
            if (!G.isEmteny(speechContent)){
                itemList.add(speechContent);
                speechContent="";
                position++;
            }else {
                if (position==-1){
                    itemList.add(speechContent);
                }
            }
        //    adapter.notifyDataSetChanged();
        }
        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
            showTip(error.getPlainDescription(true));
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            showTip("结束说话");
            ivSpeech.setImageResource(R.mipmap.ic_speech);
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.d(TAG, results.getResultString());
            StringBuffer buffer = new StringBuffer();
            buffer.append(speechContent);
            SpeechVos speechVos = new Gson().fromJson(results.getResultString(), SpeechVos.class);
            for (int i = 0; i < speechVos.getWs().size(); i++) {
                SpeechVos.WsBean words = speechVos.getWs().get(i);
                buffer.append(words.getCw().get(0).getW());
                itemList.set(position,buffer.toString());
                rvSpeech.scrollToPosition(position);
                adapter.notifyDataSetChanged();
            }
            speechContent = buffer.toString();
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            ivSpeech.setImageResource(R.mipmap.ic_speeching);
            //   showTip("当前正在说话，音量大小：" + volume);
            Log.d(TAG, "返回音频数据：" + data.length);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下
            //
            //
            //
            // 代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

}
