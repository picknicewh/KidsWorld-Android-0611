'use strict';
angular.module('app.controllers')
.controller('AudioPlayCtrl', function($scope, $state, $stateParams, $ionicHistory,$ionicPopup, WebService, CommonService) {

    //初始化设定播放状态
    $scope.currentIndex = 0;    //当前播放序列号：第1首
    $scope.listLength = 0;      //播放列表曲目数量：0
    $scope.songPlaying = 0;     //当前播放状态：未播放
    $scope.showPlaylist = 0;    //播放列表显示状态：隐藏
    $scope.circleMode = 0;      //当前播放模式：顺序播放
    $scope.items_audio = [];    //播放列表项目状态：空数组
    $scope.loadingTextShow = 0; //获取数据信息：隐藏
    $scope.canPlay = 0;         //歌曲可播放状态：不可播放0

    //点击歌曲列表，根据序号，播放相应歌曲
    $scope.playSongByIndex = function(index){
        //$scope.currentIndex=index;
        //var audioSrc=$scope.items_audio[index].url;
        //myAudio.src=audioSrc;
        //$scope.audioNameCurrent=$scope.items_audio[index].name;
        //myAudio.load();
        //$scope.showPlaylist=0;

        //接后台
        $scope.currentIndex=index;
        var audioSrc=$scope.items_audio[index].resourceUrl;
        myAudio.src=audioSrc;
        $scope.audioNameCurrent=$scope.items_audio[index].name;
        $scope.audioImageCurrent=$scope.items_audio[index].imgUrl;
        $scope.audioLrcCurrent=$scope.items_audio[index].content;
        //myAudio.play();
        myAudio.load();
        //$scope.songPlaying=1;
        $scope.showPlaylist=0;
        CommonService.playHisService.addAudioHis($scope.items_audio[index]);
    };

    //视图渲染前，从服务器取得图片数据
    $scope.$on('$ionicView.beforeEnter', function() {
        //修改头部导航栏
        if (platFlag == 1) {
            window.location.href = 'tooc://changenavigationtitle:?null&音乐播放*null';
        } else if (platFlag == 0) {
            change_tb.setToolBar("mediaplaying");
        }

        $scope.themeid=$stateParams["themeid"];
        //$scope.themeid=109;
        $scope.resourceidFrom=$stateParams["resourceid"];

        ////获取曲目信息
        //var res = WebService.getAudioPlayList();
        //res.$promise.then(function (response) {
        //    var data = response.data.data;
        //    var length = data.length;
        //    if (length <= 0) {
        //        $ionicPopup.alert({
        //            title: '提示',
        //            template: '无数据！'
        //        });
        //        return;
        //    }
        //    //将曲目信息传给播放列表
        //    $scope.items_audio = data;
        //    $scope.listLength = length;
        //    //初始化播放第一首歌
        //    $scope.playFirstSong();
        //});

        //获取曲目信息--接后台
        var res = WebService.getContentInfo(tsIdLogin,$scope.themeid);
        res.$promise.then(function (response) {
            var data = response.data.data;
            var length = data.length;
            if (length <= 0) {
                $ionicPopup.alert({
                    title: '提示',
                    template: '无曲目数据！'
                });
                return;
            }
            //将曲目信息传给播放列表
            $scope.items_audio = data;
            $scope.listLength = length;
            //初始化播放第一首歌
            if($scope.resourceidFrom){
                $.each($scope.items_audio, function(ind,el){
                    if(el.id==$scope.resourceidFrom){
                        var index=ind;
                        $scope.playFirstSong(index);
                    }
                });
            }else{
                $scope.playFirstSong(0);
            }
        });
    });
    //初始化，播放第一首歌曲
    $scope.playFirstSong=function(index){

        //var audioSrc=$scope.items_audio[0].url;
        //myAudio.src=audioSrc;
        //$scope.audioNameCurrent=$scope.items_audio[0].name;
        //$scope.audioImageCurrent="images/temp/audioImage.png";
        //$scope.audioLrcCurrent="<p>知了整个夏天</p><p>知了整个夏天</p><p>知了整个夏天</p><p>知了整个夏天</p><p>知了整个夏天</p><p>知了整个夏天</p><p>知了整个夏天</p> <p>知了整个夏天</p> <p>知了整个夏天</p> <p>知了整个夏天</p> <p>知了整个夏天</p> <p>知了整个夏天</p> <p>知了整个夏天</p> <p>知了整个夏天</p> <p>知了整个夏天</p> <p>知了整个夏天</p> <p>知了整个夏天</p> <p>知了整个夏天</p> <p>知了整个夏天</p> <p>知了整个夏天</p> <p>知了整个夏天</p>";
        //
        //myAudio.play();

        //接后台
        var audioSrc=$scope.items_audio[index].resourceUrl;
        myAudio.src=audioSrc;
        $scope.audioNameCurrent=$scope.items_audio[index].name;
        $scope.audioImageCurrent=$scope.items_audio[index].imgUrl;
        $scope.audioLrcCurrent=$scope.items_audio[index].content;
        myAudio.play();
        CommonService.playHisService.addAudioHis($scope.items_audio[index]);

    };

    //获取audio对象
    var myAudio=document.getElementById("audioPlayerHide");
    //判断歌曲是否可以播放
    myAudio.addEventListener('canplay',function(){
        $scope.loadingTextShow = 0;
        $scope.canPlay = 1;
        $scope.songPlaying=1;
        $scope.$digest();
        myAudio.play();
    },false);
    //监听歌曲是否缓冲
    myAudio.addEventListener('loadstart',function(){
        $scope.loadingTextShow = 1;
        $scope.canPlay = 0;
        $scope.songPlaying=0;
        $scope.$digest();
    },false);
    //监听视频是否缓冲
    myAudio.addEventListener('error',function(){
        $scope.loadingTextShow = 0;
        $scope.$digest();
        $ionicPopup.alert({
            title: '提示',
            template: '音乐加载失败！'
        });
    },false);

    //上一首按钮
    $scope.playPreviousSong = function(){
        if($scope.circleMode==0||$scope.circleMode==1){     //单曲循环和顺序播放模式，上一首为序号前一首
            if($scope.currentIndex>0){
                $scope.playSongByIndex($scope.currentIndex-1);
            }else{
                $scope.playSongByIndex($scope.listLength-1);
            }
        }else{      //随机播放模式
            var indexTemp=Math.floor($scope.currentIndex-($scope.listLength*Math.random()));
            if(indexTemp<0){
                $scope.playSongByIndex(($scope.listLength-1)+indexTemp);
            }else{
                $scope.playSongByIndex(indexTemp);
            }
        }
    };
    //下一首按钮
    $scope.playNextSong = function(){
        if($scope.circleMode==0||$scope.circleMode==1){     //顺序播放和单曲循环模式，下一首为序号后一首
            if($scope.currentIndex<($scope.listLength-1)){
                $scope.playSongByIndex($scope.currentIndex+1);
            }else{
                $scope.playSongByIndex(0);
            }
        }else{      //随机播放模式
            var indexTemp=Math.floor($scope.currentIndex+($scope.listLength*Math.random()));
            if(indexTemp>($scope.listLength-1)){
                $scope.playSongByIndex(indexTemp-($scope.listLength-1));
            }else{
                $scope.playSongByIndex(indexTemp);
            }
        }
    };

    //当前曲目播放停止时,根据播放模式，播放下一首
    myAudio.addEventListener("ended", function(){
        $scope.songPlaying=0;
        $scope.$digest();
        //当前歌曲播放结束时，0顺序播放下一首，1单曲循环，2，随机播放下一首
        if($scope.circleMode==0){
            if($scope.currentIndex<($scope.listLength-1)){
                $scope.playSongByIndex($scope.currentIndex+1);
            }else{
                $scope.playSongByIndex(0);
            }
        }else if($scope.circleMode==1){
            $scope.playSongByIndex($scope.currentIndex);
        }else{
            var indexTemp=Math.floor($scope.currentIndex+($scope.listLength*Math.random()));
            if(indexTemp>($scope.listLength-1)){
                $scope.playSongByIndex(indexTemp-($scope.listLength-1));
            }else{
                $scope.playSongByIndex(indexTemp);
            }
        }
    });

    //$scope.store=function(){
    //    setTimeout(function(){
    //        CommonService.showAlert.show("收藏成功");
    //    },1000);
    //}

    //播放暂停按钮
    $scope.playOrpause=function(){
        if($scope.songPlaying==0){
            //myAudio.load();
            myAudio.play();
            $scope.songPlaying=1;
            //if($scope.canPlay==1){
            //    $scope.songPlaying=1;
            //}
        }else{
            myAudio.pause();
            $scope.songPlaying=0;
        }
    };

    //循环模式
    $scope.changeCircleMode=function(){
        if($scope.circleMode==0){
            $scope.circleMode=1;
        }else if($scope.circleMode==1){
            $scope.circleMode=2;
        }else{
            $scope.circleMode=0;
        }
    };

    //歌词全屏显示收缩
    $scope.lrcFull=0;
    $scope.showSrcTxt="查看歌词";
    $scope.toggleLrc=function(){
        if($scope.lrcFull==0){
            $scope.lrcFull=1;
            $scope.showSrcTxt="返回";
        }else{
            $scope.lrcFull=0;
            $scope.showSrcTxt="查看歌词";
        }
    };

    //点击列表按钮和关闭按钮时，显示隐藏播放列表
    $scope.togglePlaylist=function(){
        if($scope.showPlaylist==0){
            $scope.showPlaylist=1;
        }else{
            $scope.showPlaylist=0;
        }
    };

    //后退键按钮
    $scope.goBack=function(){
        //$ionicHistory.goBack();
        window.history.back();
    };
    //收藏按钮
    //$scope.storeActive=false;
    $scope.store=function( index ){

        //var resourceidS=item.id;
        ////var resourceidS=1;
        //if(item.attentionid){
        //    var cancel=2;
        //}else{
        //    var cancel=1;
        //}
        //var res = WebService.subAttention(tsIdLogin,resourceidS,cancel);
        //res.$promise.then(function (response) {
        //    var data = response.data;
        //    if (data.code==0) {
        //        CommonService.showAlert.show(data.data);
        //        if(item_audio.storeActive){
        //            item.attentionid=false;
        //        }else{
        //            item.attentionid=true;
        //        }
        //    }else{
        //        CommonService.showAlert.show(data.data);
        //    }
        //});

        var resourceidS=$scope.items_audio[index].id;
        //var resourceidS=1;
        if($scope.items_audio[index].attentionid){
            var cancel=2;
        }else{
            var cancel=1;
        }
        var res = WebService.subAttention(tsIdLogin,resourceidS,cancel);
        res.$promise.then(function (response) {
            var data = response.data;
            if (data.code==0) {
                CommonService.showAlert.show(data.data);
                if($scope.items_audio[index].attentionid){
                    $scope.items_audio[index].attentionid=null;
                }else{
                    $scope.items_audio[index].attentionid=true;
                }
            }else{
                CommonService.showAlert.show(data.data);
            }
        });



    };

});
