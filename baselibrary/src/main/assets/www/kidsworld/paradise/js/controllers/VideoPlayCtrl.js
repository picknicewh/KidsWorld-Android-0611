'use strict';
angular.module('app.controllers')
.controller('VideoPlayCtrl', function($scope, $state, $stateParams, $ionicHistory, $ionicPopup,WebService, CommonService) {

    var myVideo=document.getElementById("videoPlayerMy");

    //$scope.currentIndex = 0;
    //$scope.current={ };
    //$scope.current.title = "小熊好棒";
    //$scope.current.count = "34万";
    //$scope.current.score = 7.2;
    //$scope.current.beriefe = "简介：小熊很可爱，小熊很好吃，小熊很可爱,小熊很可爱，小熊很好吃，小熊很可爱,小熊很可爱，小熊很好吃，小熊很可爱,小熊很可爱，小熊很好吃，小熊很可爱,小熊很可爱，小熊很好吃，小熊很可爱,小熊很可爱，小熊很好吃，小熊很可爱,小熊很可爱，小熊很好吃，小熊很可爱,小熊很可爱，小熊很好吃，小熊很可爱,小熊很可爱，小熊很好吃，小熊很可爱,小熊很可爱，小熊很好吃，小熊很可爱,小熊很可爱，小熊很好吃，小熊很可爱,小熊很可爱，小熊很好吃，小熊很可爱.";

    //视图渲染前，从服务器取得图片数据
    $scope.$on('$ionicView.beforeEnter', function() {
        //隐藏头部导航栏
        if (platFlag == 1) {
            window.location.href = 'tooc://changenavigationtitle:?null&null*null';
        } else if (platFlag == 0) {
            change_tb.setToolBar("video");
        }

        $scope.themeid=$stateParams["themeid"];
        //$scope.themeid=1;
        $scope.resourceidFrom=$stateParams["resourceid"];

        $scope.init();
    });
    //返回按钮
    $scope.goBack=function(){
        //$ionicHistory.goBack();
        window.history.back();
    };
    ////--接后台
    $scope.init=function(){
        //获取选集信息，并播放第一集
        $scope.getSetByThemeId($scope.themeid,false);
        //$scope.items_play=[0,1,2,3,4,5,6,7,8,9,10,11,12,13,14];
        //resizeSetVideoBox();
        //获取为你推荐
        $scope.current={ };
        $scope.items_video=[];
        $scope.getRecomForyou();

        //默认正在提示不显示
        $scope.loadingTextShow=0;
    };
    //获取选集信息，并播放相应集
    $scope.getSetByThemeId=function(themeid,fromRecom){
        //获取选集信息
        var res = WebService.getContentInfo(tsIdLogin,themeid);
        res.$promise.then(function (response) {
            var data = response.data.data;
            var length = data.length;
            if (length <= 0) {
                $ionicPopup.alert({
                    title: '提示',
                    template: '无选集数据！'
                });
                return;
            }
            $scope.items_play = data;
            resizeSetVideoBox();
            //初始化播放
            if(fromRecom){  //从为你推荐点入的，播放第一集
                $scope.playVideoByIndex(0);
            } else{     //从别的页面跳入的，如果有resouceid则播放相应视频，如果没有resourceid则播放第一集
                if($scope.resourceidFrom){
                    $.each($scope.items_play, function(ind,el){
                        if(el.id==$scope.resourceidFrom){
                            var index=ind;
                            $scope.playVideoByIndex(index);
                        }
                    });
                }else{
                    $scope.playVideoByIndex(0);
                }
            }
        });
    };

    //判断视频是否可以播放
    myVideo.addEventListener('canplay',function(){
        $scope.loadingTextShow = 0;
        $scope.$digest();
    },false);
    //监听视频是否缓冲
    myVideo.addEventListener('loadstart',function(){
        $scope.loadingTextShow = 1;
        $scope.$digest();
    },false);
    //监听视频是否加载出错
    myVideo.addEventListener('error',function(){
        $scope.loadingTextShow = 0;
        $scope.$digest();
        $ionicPopup.alert({
            title: '提示',
            template: '视频加载失败！'
        });
    },false);
    //根据序号播放视频
    $scope.playVideoByIndex=function(index){
        //myVideo.src="http://www.w3school.com.cn/i/movie.mp4";
        //myVideo.load();
        //$scope.currentIndex = index;

        //接后台
        myVideo.src=$scope.items_play[index].resourceUrl;
        myVideo.load();
        $scope.currentIndex = index;
        $scope.current.title = $scope.items_play[index].name;
        $scope.current.count = $scope.items_play[index].pv;
        //$scope.current.resourceid=$scope.items_play[index].resourceid;
        $scope.current.resourceid=$scope.items_play[index].id;  //与后台沟通后，resourceid即为id
        //$scope.current.score = items_play[index].score;
        $scope.current.beriefe =$scope.items_play[index].description;
        CommonService.playHisService.addVideoHis($scope.items_play[index]);
        //收藏
        if($scope.items_play[index].attentionid){
            $scope.current.storeActive=true;
        }else{
            $scope.current.storeActive=false;
        }
        //点赞
        if($scope.items_play[index].praiseid){
            $scope.current.praiseActive=true;
        }else{
            $scope.current.praiseActive=false;
        }

    };
    //展开收起视频简介
    $scope.togglePinch=function(){
        $('.videoCont').toggleClass('pinch');
    }
    //重设选集滚动条宽度
    function resizeSetVideoBox(){
        var length=$scope.items_play.length;
        var width=117*length;
        $('.SetVideoBox').width(width);
    }
    //为你推荐控制
    $scope.getRecomForyou=function(){
        var res = WebService.getRecommenda(tsIdLogin,8,0);
        res.$promise.then(function(response) {
            var data=response.data.data;
            var length=data.length;
            if(length<=0) {
                $ionicPopup.alert({
                    title: '提示',
                    template: '无为你推荐数据！'
                });
                return;
            }
            $scope.items_video=data;
        });
    };
    //点击为你推荐中的选集，则切换选集内容，并播放第一集
    $scope.changePlayByThemeid= function ( item ) {
        $scope.getSetByThemeId( item.themeid,true );
    };
    //收藏按钮
    //$scope.current.storeActive=false;
    $scope.store=function(){
        //$scope.current.resourceid=1;
        if($scope.current.storeActive){
            var cancel=2;   //2取消收藏
        }else{
            var cancel=1;   //1收藏资源
        }
        var res = WebService.subAttention(tsIdLogin,$scope.current.resourceid,cancel);
        res.$promise.then(function (response) {
            var data = response.data;
            if (data.code==0) {
                CommonService.showAlert.show(data.data);
                if($scope.current.storeActive){
                    $scope.current.storeActive=false;
                    $scope.items_play[$scope.currentIndex].attentionid=false;
                }else{
                    $scope.current.storeActive=true;
                    $scope.items_play[$scope.currentIndex].attentionid=true;
                }
            }else{
                CommonService.showAlert.show(data.data);
            }
        });

    };
    //点赞按钮
    //$scope.current.praiseActive=false;
    $scope.praise=function(){
        //$scope.current.resourceid=1;
        if($scope.current.praiseActive){
            var cancel=2;   //2取消点赞
        }else{
            var cancel=1;   //1点赞资源
        }
        var res = WebService.subPraise(tsIdLogin,$scope.current.resourceid,cancel);
        res.$promise.then(function (response) {
            var data = response.data;
            if (data.code==0) {
                CommonService.showAlert.show(data.data);
                if($scope.current.praiseActive){
                    $scope.current.praiseActive=false;
                    $scope.items_play[$scope.currentIndex].praiseid=false;
                }else{
                    $scope.current.praiseActive=true;
                    $scope.items_play[$scope.currentIndex].praiseid=true;
                }
            }else{
                CommonService.showAlert.show(data.data);
            }
        });
    };


})
