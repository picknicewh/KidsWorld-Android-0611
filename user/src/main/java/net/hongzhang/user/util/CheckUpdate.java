package net.hongzhang.user.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.FileCallback;

import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.MD5Utils;
import net.hongzhang.baselibrary.widget.MyAlertDialog;
import net.hongzhang.baselibrary.util.NetWorkUitls;
import net.hongzhang.baselibrary.util.PackageUtils;
import net.hongzhang.user.R;
import net.hongzhang.user.mode.CheckUpadteVo;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/26
 * 描    述：版本更新
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class CheckUpdate implements OkHttpListener {
	/**
	 * 下载地址
	 */
	private String downLoadURL;
	/**
	 * 下载保存路径
	 */
	private String mSavePath;
	/**
	 * 当前上下文
	 */
	private Context context;
	/**
	 * 更新类型
	 */
	private int updateType;
	/**
	 * 新版本号
	 */
	private String version;
	/**
	 * app名字
	 */
    private final String APPNAME="幼儿天地";
	/**
	 * 当前activity对象
	 */
	private Activity activity;
	/**
	 * 下载进度条
	 */
	private ProgressBar pb_dowmload;
	/**
	 * 下载提示
	 */
	private TextView tv_progress_percent;
	/**
	 * 提示框取消
	 */
	private Button b_cancel;
	/**
	 * 提示框确认
	 */
	private Button b_confirm;
	/**
	 * 提示框
	 */
	private AlertDialog dialog;
	private boolean isPrompt;
	public CheckUpdate(Activity activity,boolean isPrompt) {
		this.context = activity;;
		this.activity=activity;
		this.isPrompt=isPrompt;
		// 判断SD卡是否存在，并且是否具有读写权限
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			// 获得存储卡的路径
			mSavePath = Environment.getExternalStorageDirectory().toString() + "/ChatFile";
		}

		if(NetWorkUitls.isNetworkAvailable(context)){
			checkUpdate();
		}else if(updateType==0) {
			G.showToast(context, "无网络，无法更新！");
		}
	}

    public void checkUpdate() {
        Map<String, Object> map = new HashMap<>();
        map.put("version", String.valueOf(PackageUtils.getVersionCode(context)));
        map.put("system","2");
        Type type= new TypeToken<Result<CheckUpadteVo>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.UPDATESYSTEM,map,this);
    }

    @Override
    public void onSuccess(String uri, Object date) {
        if (uri.equals(Apiurl.UPDATESYSTEM)){
            Result<CheckUpadteVo> result= (Result<CheckUpadteVo>) date;
            if(!G.isEmteny(result.getData().getUpdateurl())){
                downLoadURL = result.getData().getUpdateurl();
                version = result.getData().getVersions();
				showDownLoadDialog();
            }else{
				if(isPrompt) G.showToast(context, "您已经是最新版本");
			}
        }
    }

    @Override
    public void onError(String uri, String error) {
		if(isPrompt) G.showToast(context, "您已经是最新版本");
    }

    private void downLoadAPK(){
        OkHttpUtils.get(downLoadURL).execute(new FileCallback(mSavePath,APPNAME+ MD5Utils.encode(version) + ".apk.temp") {
            @Override
            public void onResponse(boolean isFromCache, File file, Request request, @Nullable Response response) {
                File apkfile = new File(mSavePath, APPNAME + MD5Utils.encode(version) + ".apk.temp");
                apkfile.renameTo(new File(mSavePath, APPNAME + MD5Utils.encode(version) + ".apk"));
				installApk();//前往安装
				G.showToast(context,"下载完成！");
				b_confirm.setEnabled(true);
				bttonCancel(false);
				dialog.dismiss();
            }

            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                G.showToast(context,"更新失败，请稍后更新！");
				bttonCancel(false);
            }

			@Override
			public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
				super.downloadProgress(currentSize, totalSize, progress, networkSpeed);
				setDownLodaProgress(currentSize,totalSize,progress,networkSpeed);
			}
		});
    }

	private void showDownLoadDialog(){
		View view = LayoutInflater.from(context).inflate(R.layout.alertdialog_checkupdate,null);
		dialog= MyAlertDialog.getDialog(view,activity,1);
		dialog.setCancelable(false);
		final TextView tv_message= (TextView) view.findViewById(R.id.tv_update_message); //更新提示
		final RelativeLayout rl_dowmload= (RelativeLayout) view.findViewById(R.id.rl_dowmload); //下载页面
		pb_dowmload= (ProgressBar) view.findViewById(R.id.pb_download);  //下载进度条
		tv_progress_percent= (TextView) view.findViewById(R.id.tv_progress_percent); //下载进度提示
		b_cancel = (Button) view.findViewById(R.id.b_cancel); //取消
		b_confirm = (Button) view.findViewById(R.id.b_confirm); //确定
		b_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.dismiss();
			}
		});
		b_confirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				tv_message.setVisibility(View.GONE);
				rl_dowmload.setVisibility(View.VISIBLE);
				downLoadAPK();
				bttonCancel(true);
			}
		});
	}

	/**
	 * 在下载中 取消button 的点击事件
	 * @param isCancel
     */
	private void bttonCancel(boolean isCancel){
		if(isCancel){
			b_confirm.setEnabled(false);
		}else {
			b_confirm.setEnabled(true);
		}
	}
	/**
	 * 下载进度信息
	 * @param currentSize 当前下载的大小
	 * @param totalSize 需要下载的大小
	 * @param progress 进度大小
	 * @param networkSpeed 下载网速，单位秒
     */
	private void setDownLodaProgress(long currentSize,long totalSize,float progress,long networkSpeed){
		pb_dowmload.setMax((int) totalSize);
		pb_dowmload.setProgress((int) currentSize);//(int)(networkSpeed/1000)+"KB/s"
		tv_progress_percent.setText((int)(progress*100)+"%--"+CacheHelp.getFormatSize(networkSpeed));
	}

	/**
	 * 安装APK文件
	 */
	private boolean installApk() {
		File apkfile = new File(mSavePath, APPNAME + MD5Utils.encode(version) + ".apk");
		if (!apkfile.exists()) {
			return false;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		context.startActivity(i);
		return true;
	}

	public void Testdate(String visonCode,String downLoadURL){
		this.downLoadURL = downLoadURL;
		this.version = visonCode;
		showDownLoadDialog();
	}
}
