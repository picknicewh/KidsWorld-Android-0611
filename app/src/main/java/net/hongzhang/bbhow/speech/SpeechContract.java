package net.hongzhang.bbhow.speech;

import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;

/**
 * 作者： wanghua
 * 时间： 2017/6/1
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class SpeechContract {
    interface View {
        void showLoadingDialog();

        void stopLoadingDialog();
    }

    interface Presenter {

        void setRecognizerParam(SpeechRecognizer mIat);

        void setSynthesizerParam(SpeechSynthesizer mTts);
    }
}
