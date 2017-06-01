package net.hongzhang.baselibrary.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.FileCallback;

import net.hongzhang.baselibrary.BaseLibrary;
import net.hongzhang.baselibrary.R;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.UpdateUtil;

import java.io.File;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 作者： wanghua
 * 时间： 2017/4/12
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class UpdateConformDialog extends AlertDialog implements View.OnClickListener{
    private Context context;
    private Button btn_confrom;
    private Button btn_cancel;
    private ForceUpdateDialog  dialog ;
    private String savePath;
    private String saveName;

    public UpdateConformDialog(Context context) {
        super(context);
        this.context =context;
        savePath = getSavePath();
        saveName = UpdateUtil.getSaveName(context)+".temp";

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_update_conform);
        setCanceledOnTouchOutside(false);
        btn_confrom = (Button) findViewById(R.id.btn_conform);
        btn_cancel  = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);
        btn_confrom.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId==R.id.btn_cancel){
            BaseLibrary.exit();
        }else if (viewId==R.id.btn_conform){
            dialog = new ForceUpdateDialog(context);
            downLoadApk("http://101.69.121.36/imtt.dd.qq.com/16891/A57EB927AA21AB73D521BE20DD0BF61B.apk?mkey=58edeb529e1b8689&f=1a07&c=0&fsname=com.tencent.mm_6.5.7_1041.apk&csr=1bbd&p=.apk",savePath,saveName);

        }
        dismiss();
    }
    public void downLoadApk(String downLoadUrl, final String savePath, final String saveName){
        OkHttpUtils.get(downLoadUrl).execute(new FileCallback(savePath,saveName) {
            @Override
            public void onResponse(boolean isFromCache, File file, Request request, @Nullable Response response) {
                G.showToast(context,"下载成功");
                File apkFile = new File(savePath, saveName);
                apkFile.renameTo(new File(savePath, saveName.replace(".temp","")));
                UpdateUtil.installApk(context,savePath);
                try {
                    dialog.dismiss();
                }catch (Exception e){
                    e.printStackTrace();//用户强制要退出应用程序
                }
            }
            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                G.showToast(context,"下载失败");
                try {
                    dialog.dismiss();
                }catch (Exception ex){
                    ex.printStackTrace();//用户强制要退出应用程序
                }
            }
            @Override
            public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                try {
                    dialog.show();
                    dialog.setMessageData(progress,(float) currentSize/1024/1024,(float) totalSize/1024/1024);
                }catch (Exception ex){
                    ex.printStackTrace();//用户强制要退出应用程序
                }

            }
        });
    }
    private String getSavePath(){
        String mSavePath ="";
        // 判断SD卡是否存在，并且是否具有读写权限
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // 获得存储卡的路径
            mSavePath = Environment.getExternalStorageDirectory().toString() + "/apk";
            File file = new File(mSavePath);
            if (!file.exists()){
                file.mkdirs();
            }
        }
        return mSavePath;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            return false;
        }
        return super.onKeyDown(keyCode, event);

    }
}
