//package net.hunme.school.activity;
//
//import android.app.Activity;
//import android.content.Context;
//import android.net.wifi.WifiManager;
//import android.os.Bundle;
//import android.os.SystemClock;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//import android.view.View;
//
//import com.hik.mcrsdk.rtsp.LiveInfo;
//import com.hik.mcrsdk.rtsp.RtspClient;
//import com.hik.mcrsdk.rtsp.RtspClientCallback;
//import com.hikvision.vmsnetsdk.CameraInfo;
//import com.hikvision.vmsnetsdk.CameraInfoEx;
//import com.hikvision.vmsnetsdk.ControlUnitInfo;
//import com.hikvision.vmsnetsdk.RealPlayURL;
//import com.hikvision.vmsnetsdk.RegionInfo;
//import com.hikvision.vmsnetsdk.ServInfo;
//import com.hikvision.vmsnetsdk.VMSNetSDK;
//import com.hikvision.vmsnetsdk.netLayer.msp.deviceInfo.DeviceInfo;
//
//import net.hunme.baselibrary.util.G;
//import net.hunme.school.R;
//
//import org.MediaPlayer.PlayM4.Player;
//import org.MediaPlayer.PlayM4.PlayerCallBack;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
//
///**
// * 作者： Administrator
// * 时间： 2016/7/15
// * 名称：开放课堂
// * 版本说明：
// * 附加注释：
// * 主要接口：
// */
//public class OpenClassActivity extends Activity implements PlayerCallBack.PlayerDisplayCB, RtspClientCallback {
//    /** 登录返回的数据 */
//    private ServInfo servInfo;
//    private RealPlayURL realPlayURL;
//    private List<ControlUnitInfo> controlUnitList;
//    private List<CameraInfo> cameraList;
//    private List<RegionInfo> regionList;
//    private DeviceInfo deviceInfo;
//    private CameraInfoEx cameraInfoEx;
//    /**
//     * 通过VMSNetSDK返回的预览地址对象
//     */
//    private RealPlayURL mRealPlayURL;
//    /** RTSP sdk句柄 */
//    private RtspClient mRtspHandle = null;
//
//    private JCVideoPlayerStandard jcVideoPlayerStandard;
//    private SurfaceView surface_move;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_openclass);
//        surface_move= (SurfaceView) findViewById(R.id.surface_move);
//        initDate();
//        loginService("http://221.12.173.38","test","test.1234",getMacAddr(),servInfo);
//    }
//
//    private void initDate(){
//        servInfo=new ServInfo();
//        deviceInfo =new DeviceInfo();
//        cameraInfoEx=new CameraInfoEx();
//        realPlayURL=new RealPlayURL();
//        mRealPlayURL = new RealPlayURL();
//        mPlayerHandler = Player.getInstance();
//        mRtspHandle = RtspClient.getInstance();
//        mRtspHandler= RtspClient.getInstance();
//        controlUnitList= new ArrayList<>();
//        cameraList=new ArrayList<>();
//        regionList=new ArrayList<>();
//    }
//
//    private void playMove(String playURL){
////        if(G.isEmteny(playURL)){
////            playURL="http://pics.dadashi.cn/flash/xuequ/mobile/S1A110201.mp4";
////        }
////        jcVideoPlayerStandard.setUp(playURL, JCVideoPlayerStandard.SCREEN_LAYOUT_LIST,"");
//    }
//    /**
//     *  登录设备
//     * @param servAddr 服务器地址
//     * @param userName 登录名
//     * @param password 密码
//     * @param macAddress mac地址
//     * @param servInfo 返回数据
//     */
//    private void loginService(final String servAddr, final String userName, final String password, final String macAddress, final ServInfo servInfo){
//        // 新线程进行登录操作
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                // 登录请求
//                boolean ret = VMSNetSDK.getInstance().login(servAddr, userName, password, macAddress, servInfo);
//                if (ret) {
//                    G.log("登录成功！");
//                    getControlUnitList(servAddr,servInfo.getSessionID(),"0",100,1,controlUnitList);
//                } else {
//                    G.log("登录失败：code==="+VMSNetSDK.getInstance().getLastErrorCode()+"desc==="+VMSNetSDK.getInstance().getLastErrorDesc());
//                }
//            }
//        }).start();
//    }
//
//    /**
//     * 获取控制中心列表
//     * @param servAddr
//     * @param sessionID
//     * @param controlUnitID 父控制中心的id，首次获取数据传0即可
//     * @param numPerPage
//     * @param curPage
//     * @param controlUnitList
//     */
//    private void getControlUnitList(final String servAddr, final String sessionID, final String controlUnitID,
//                                    final int numPerPage, final int curPage, final List<ControlUnitInfo> controlUnitList){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                boolean res=VMSNetSDK.getInstance().getControlUnitList(servAddr,sessionID,controlUnitID,numPerPage,curPage,controlUnitList);
//                if(res){
//                    G.log("获取控制中心列表成功！"+controlUnitList.size());
//                    boolean ress =VMSNetSDK.getInstance().getControlUnitList(servAddr,sessionID,controlUnitList.get(0).getControlUnitID(),numPerPage,curPage,controlUnitList);
//                    if(ress){
//                        getCameraListFromCtrlUnit(servAddr,sessionID,controlUnitList.get(0).getControlUnitID(),100,1,cameraList);
//                    }
//                }else{
//                    G.log("获取控制中心列表失败！code==="+VMSNetSDK.getInstance().getLastErrorCode()+"desc==="+VMSNetSDK.getInstance().getLastErrorDesc());
//                }
//            }
//        }).start();
//    }
//
//    /**
//     * 从控制中心获取监控点列表
//     * @param servAddr
//     * @param sessionID
//     * @param controlUnitID 由控制中心传
//     * @param numPerPage
//     * @param curPage
//     * @param cameraList
//     */
//    private void getCameraListFromCtrlUnit(final String servAddr, final String sessionID, final String controlUnitID,
//                                           final int numPerPage, final int curPage, final List<CameraInfo> cameraList){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                boolean res=VMSNetSDK.getInstance().getCameraListFromCtrlUnit(servAddr,sessionID,controlUnitID,numPerPage,curPage,cameraList);
//                if(res){
//                    G.log("从控制中心获取监控点列表成功！"+cameraList.size());
//                    //DeviceID  == id
////                    G.log("cameraList.get(0).getDeviceID()--------------"+cameraList.get(0).getDeviceID());
////                    getDeviceInfo(servAddr,sessionID,cameraList.get(0).getDeviceID(),deviceInfo);
//
//                    getCameraDetailInfo(servAddr,sessionID,cameraList.get(0).getId());
//                }else{
//                    G.log("从控制中心获取监控点列表失败！code==="+VMSNetSDK.getInstance().getLastErrorCode()+"desc==="+VMSNetSDK.getInstance().getLastErrorDesc());
//                }
//            }
//        }).start();
//    }
//
//    /**
//     * 获取设备信息
//     * @param servAddr
//     * @param sessionID
//     * @param deviceID 当前监控点的设备deviceid，camerainfo中获得
//     * @param deviceInfo
//     */
//    private void getDeviceInfo(final String servAddr, final String sessionID, final String deviceID, final DeviceInfo deviceInfo){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                boolean res=VMSNetSDK.getInstance().getDeviceInfo(servAddr,sessionID,deviceID,deviceInfo);
//                if(res){
//                    G.log("获取设备信息成功=="+deviceInfo.getDeviceName());
//                }else {
//                    G.log("获取设备信息失败：code==="+VMSNetSDK.getInstance().getLastErrorCode()+"desc==="+VMSNetSDK.getInstance().getLastErrorDesc());
//                }
//            }
//        }).start();
//    }
//
//    /**
//     * 获取监控点详情方法
//     *
//     * @param servAddr 服务器地址
//     * @param sessionID 会话ID
//     */
//    private void getCameraDetailInfo(final String servAddr, final String sessionID, final String mCameraID){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                boolean res= VMSNetSDK.getInstance().getCameraInfoEx(servAddr,sessionID,mCameraID,cameraInfoEx);
//                if(res){
//                    getDeviceInfo(servAddr,sessionID,cameraInfoEx.getDeviceId(), deviceInfo);
//                    getPlayUrl(-1);
//                    G.log("获取监控点详情成功：==="+cameraInfoEx.getDeviceNetId());
//                }else{
//                    G.log("获取监控点详情失败：code==="+VMSNetSDK.getInstance().getLastErrorCode()+"desc==="+VMSNetSDK.getInstance().getLastErrorDesc());
//                }
//            }
//        }).start();
//    }
//    private String url="";
//    private void getPlayUrl(final int streamType){
////        String url="";
//        if (mRealPlayURL == null) {
//            return ;
//        }
//
//        String mToken =VMSNetSDK.getInstance().getPlayToken(servInfo.getSessionID());
//        LiveInfo liveInfo = new LiveInfo();
//        liveInfo.setMagIp(servInfo.getMagServer().getMagStreamSerAddr());
//        liveInfo.setMagPort(servInfo.getMagServer().getMagStreamSerPort());
//        liveInfo.setCameraIndexCode(cameraInfoEx.getId());
//        liveInfo.setToken(mToken);
//        // 转码不区分主子码流
//        liveInfo.setStreamType(streamType);
//        liveInfo.setMcuNetID(servInfo.getAppNetId());
//        liveInfo.setDeviceNetID(cameraInfoEx.getDeviceNetId());
//        liveInfo.setiPriority(servInfo.getUserAuthority());
//        liveInfo.setCascadeFlag(cameraInfoEx.getCascadeFlag());
//
////        if (cameraInfoEx.getCascadeFlag() == LiveInfo.CASCADE_TYPE_YES) {
////            deviceInfo.setLoginName("test");
////            deviceInfo.setLoginPsw("12345");
////        }
//        if (servInfo.isInternet()) {
//            liveInfo.setIsInternet(LiveInfo.NETWORK_TYPE_INTERNET);
//            // 获取不转码地址
//            liveInfo.setbTranscode(false);
//            mRealPlayURL.setUrl1(mRtspHandle.generateLiveUrl(liveInfo));
//
//            // 获取转码地址
//            // 使用默认转码参数cif 128 15 h264 ps
//            liveInfo.setbTranscode(true);
//            mRealPlayURL.setUrl2(mRtspHandle.generateLiveUrl(liveInfo));
//        } else {
//            liveInfo.setIsInternet(LiveInfo.NETWORK_TYPE_LOCAL);
//            liveInfo.setbTranscode(false);
//            // 内网不转码
//            mRtspHandle.generateLiveUrl(liveInfo);
//            mRealPlayURL.setUrl1(mRtspHandle.generateLiveUrl(liveInfo));
//            mRealPlayURL.setUrl2("");
//        }
//
//        G.log("url1:" + mRealPlayURL.getUrl1());
//        G.log("url2:" + mRealPlayURL.getUrl2());
//
//        url = mRealPlayURL.getUrl1();
//        if (streamType == 2 && mRealPlayURL.getUrl2() != null && mRealPlayURL.getUrl2().length() > 0) {
//            url = mRealPlayURL.getUrl2();
//        }
//        G.log("url====="+url);
////        url="rtsp://zhu.hunme.net:556/realplay://12340001001310000001:SUB:TCP?cnid=1&pnid=1&token=ST-120-3JlvUE9nwXaGPmAQ3Eve-cas&auth=50&redirect=1&transcode=0";
//        paly(url,deviceInfo.getLoginName(),deviceInfo.getLoginPsw());
//    }
//    /**
//     * 创建播放库句柄对象
//     */
//    private Player mPlayerHandler;
//    /**
//     * 创建RTSP取流库句柄对象
//     */
//    private RtspClient mRtspHandler;
//    /**
//     * 播放库播放端口
//     */
//    private int mPlayerPort = -1;
//    private int mRtspEngineIndex=RtspClient.RTSPCLIENT_INVALIDATE_ENGINEID;;
//    private void paly(String mUrl,String mDeviceUserName,String mDevicePassword){
//        mRtspEngineIndex = mRtspHandle.createRtspClientEngine(this, RtspClient.RTPRTSP_TRANSMODE);
//        mRtspHandle.startRtspProc(mRtspEngineIndex, mUrl, mDeviceUserName, mDevicePassword);
//    }
//
//    @Override
//    public void onDisplay(int i, byte[] bytes, int i1, int i2, int i3, int i4, int i5, int i6) {
//
//    }
//
//    @Override
//    public void onDataCallBack(int i, int i1, byte[] bytes, int i2, int i3, int i4, int i5) {
//        if(i1==RtspClient.DATATYPE_HEADER){
//            mPlayerPort = mPlayerHandler.getPort();
//            boolean ret=mPlayerHandler.setStreamOpenMode(mPlayerPort, Player.STREAM_REALTIME);
//            if(!ret){
//                int tempErrorCode = mPlayerHandler.getLastError(mPlayerPort);
//                mPlayerHandler.freePort(mPlayerPort);
//                mPlayerPort = -1;
//                G.log("startPlayer(): Player setStreamOpenMode failed! errorCord is P" + tempErrorCode);
//                return;
//            }
//            ret= mPlayerHandler.openStream(mPlayerPort, bytes, i2, 2 * 1024 * 1024);
//            if(!ret){
//                G.log("startPlayer() mPlayerHandle.openStream failed!" + "Port: " + mPlayerPort
//                        + "ErrorCode is P " + mPlayerHandler.getLastError(mPlayerPort));
//                return;
//            }
//
//            ret= mPlayerHandler.setDisplayCB(mPlayerPort, this);
//            if(!ret){
//                G.log("startPlayer() mPlayerHandle.setDisplayCB() failed errorCode is P"
//                        + mPlayerHandler.getLastError(mPlayerPort));
//                return;
//            }
//
//            SurfaceHolder surfaceHolder = surface_move.getHolder();
//            ret =  mPlayerHandler.play(mPlayerPort, surfaceHolder);
//            if(!ret){
//                G.log("startPlayer() mPlayerHandle.play failed!" + "Port: " + mPlayerPort + "PlayView Surface: "
//                        + surfaceHolder + "errorCode is P" + mPlayerHandler.getLastError(mPlayerPort));
//            }
//        }else{
//            processStreamData(bytes, i2);
//        }
//    }
//
//    @Override
//    public void onMessageCallBack(int i, int i1, int i2, int i3, int i4) {
//
//    }
//
////    private void processRecordData(int dataType, byte[] dataBuffer, int dataLength) {
////        if (null == dataBuffer || dataLength == 0) {
////            return;
////        }
////        if (mIsRecord) {
////            if (RtspClient.DATATYPE_HEADER == dataType) {
////                mStreamHeadDataBuffer = ByteBuffer.allocate(dataLength);
////                for (int i = 0; i < dataLength; i++) {
////                    mStreamHeadDataBuffer.put(dataBuffer[i]);
////                }
////            } else if (RtspClient.DATATYPE_STREAM == dataType) {
////                writeStreamData(dataBuffer, dataLength);
////            }
////        } else {
////            if (-1 != mTransState) {
////                mTransState = -1;
////            }
////        }
////    }
//    /**
//     * 向播放库塞数据
//     *
//     * @param data
//     * @param len void
//     * @since V1.0
//     */
//    private void processStreamData(byte[] data, int len) {
//        if (null == data || 0 == len) {
//           G.log("processStreamData() Stream data is null or len is 0");
//            return;
//        }
//        if (null != mPlayerHandler) {
//            boolean ret = mPlayerHandler.inputData(mPlayerPort, data, len);
//            if (!ret) {
//                SystemClock.sleep(10);
//            }
//        }
//    }
//
//    /**
//     * 停止RTSP
//     *
//     * @since V1.0
//     */
//    private void stopRtsp() {
//        if (null != mRtspHandler) {
//            if (RtspClient.RTSPCLIENT_INVALIDATE_ENGINEID != mRtspEngineIndex) {
//                mRtspHandler.stopRtspProc(mRtspEngineIndex);
//                mRtspHandler.releaseRtspClientEngineer(mRtspEngineIndex);
//                mRtspEngineIndex = RtspClient.RTSPCLIENT_INVALIDATE_ENGINEID;
//            }
//        }
//    }
//
//    /**
//     * 关闭播放库 void
//     *
//     * @since V1.0
//     */
//    private void closePlayer() {
//        if (null != mPlayerHandler) {
//            if (-1 != mPlayerPort) {
//                boolean ret = mPlayerHandler.stop(mPlayerPort);
//                if (!ret) {
//                   G.log(
//                            "closePlayer(): Player stop  failed!  errorCode is P"
//                                    + mPlayerHandler.getLastError(mPlayerPort));
//                }
//
//                ret = mPlayerHandler.closeStream(mPlayerPort);
//                if (!ret) {
//                    G.log( "closePlayer(): Player closeStream  failed!");
//                }
//
//                ret = mPlayerHandler.freePort(mPlayerPort);
//                if (!ret) {
//                    G.log( "closePlayer(): Player freePort  failed!");
//                }
//                mPlayerPort = -1;
//            }
//        }
//    }
//    private boolean isPlay=true;
//    public void stopPlay(View view){
//        if(isPlay){
//            stopRtsp();
//            closePlayer();
//            isPlay=false;
//        }else {
//            isPlay=true;
//            paly(url,deviceInfo.getLoginName(),deviceInfo.getLoginPsw());
//        }
//    }
//    /**
//     * 获取登录设备mac地址
//     *
//     * @return mac地址
//     */
//    protected String getMacAddr() {
//        WifiManager wm = (WifiManager)getSystemService(Context.WIFI_SERVICE);
//        String mac = wm.getConnectionInfo().getMacAddress();
//        return mac == null ? "" : mac;
//    }
//}
