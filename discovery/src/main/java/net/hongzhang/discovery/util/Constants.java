package net.hongzhang.discovery.util;

public class Constants {
	public static String MUSIC_SERVICE = "com.service.MusicService";
	//本地歌曲listview点击
	public static String ACTION_STOP = "com.music.stop";
	//暂停音乐
	public static String ACTION_PAUSE = "com.music.pause";
	//播放音乐
	public static String ACTION_PLAY = "com.music.play";
	//下一曲
	public static String ACTION_NEXT = "com.music.next";
	//下一曲
	public static String ACTION_UPDATE = "com.music.update";
	//上一曲
	public static String ACTION_PRV = "com.music.prv";
	public static final String ACTION_PROCRESSBAR = "com.music.procress";
	public static final String ACTION_POSITION = "com.music.position";
	//播放模式
	public static final String MUSIC_NEXT = "com.music.next2";
	//当前时间
	public static final String MUSIC_CURRENT = "com.music.currentTime";
	//播放的动画
	public static final String MUSIC_ANIAMATION = "com.music.animation";
	public static final String MUSIC_UPDATE = "com.music.update";
	public static final String ACTION_CALLING = "com.music.calling";
	public static final String ACTION_NOTIFICATION = "android.intent.action.music.ACTION.NOTIFICATION ";
	public static final String CLEAN_NOTIFICATION = "android.intent.action.music.ACTION.NOTIFICATION ";
	public static final String ACTION_CHANGETEXT = "android.intent.action.music.ACTION.CHANGETEXT";
	// 播放状态
	public static final int PLAYING = 0;// 定义该怎么对音乐操作的常量,如播放是1
	public static final int PROGRESS_CHANGE = 3;// 进度条改变事件设为4
	public static final int NEXT = 4;// 暂停事件是2
	public static final int LAST = 5;// 暂停事件是2
	public static final int CLOSE = 6;// 暂停事件是2
	public static final String LOCKSCREEN_SERVIVE_NAME = "android.intent.action.lockScreen";
	public static boolean isPlay = false;
	public static PlayMode  playMode = PlayMode.LOOP;
	public static int position;


	public static int TYPE_MUSIC = 1;
	public static int TYPE_VEDIO = 2;

}
