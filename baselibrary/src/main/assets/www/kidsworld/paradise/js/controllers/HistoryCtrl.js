'use strict';
angular.module('app.controllers')
.controller('HistoryCtrl', function($scope, $state ,$ionicScrollDelegate, WebService, CommonService) {
    //视图渲染前操作
    $scope.$on('$ionicView.beforeEnter', function() {
        //隐藏头部导航栏
        if (platFlag == 1) {
            window.location.href='tooc://changenavigationtitle:?null&播放记录*null';
        } else if (platFlag == 0) {
            change_tb.setToolBar("play_history");
        }
    });
    //选项卡切换
    $scope.tabCur=0;
    $scope.changeTab=function(index){
        $scope.tabCur=index;
    };
    //返回按钮
    $scope.goBack= function () {
        $state.go('paradiseHome');
    };

    $scope.items_video=CommonService.playHisService.getVideoHis();
    if($scope.items_video){
        $scope.hasHis0=1;
    }else{
        $scope.hasHis0=0;
    }

    $scope.items_audio=CommonService.playHisService.getAudioHis();
    if($scope.items_audio){
        $scope.hasHis1=1;
    }else{
        $scope.hasHis1=0;
    }
    $scope.items_inf=CommonService.playHisService.getInfHis();
    if($scope.items_inf){
        $scope.hasHis2=1;
    }else{
        $scope.hasHis2=0;
    }

    $scope.playVideo= function ( item ) {
        //$state.go('videoPlay',{ themeid:'aa',resourceid:'bb' });
        $state.go('videoPlay',{ themeid:item.themeid,resourceid:item.resourceid });
    };
    $scope.playAudio= function ( item ) {
        //$state.go('audioPlay',{ themeid:'aa',resourceid:'bb' });
        $state.go('audioPlay',{ themeid:item.themeid,resourceid:item.resourceid });
    };
    $scope.showInfDet= function ( item ) {
        //$state.go('eduInformation_Detail',{ resourceid:'aa' });
        $state.go('eduInformation_Detail',{ resourceid:item.resourceid });
    };

        //选项卡0操作
    //$scope.items_video=[0,1,2,3,4,5,6,7];
    //$scope.playVideo= function (idx) {
    //    $state.go('videoPlay',{ themeid:"aa" });
    //};
    //    //加载更多
    //$scope.loadState0=0;
    //$scope.count0=0;
    //$scope.loadMoreData0=function(){
    //    //获取轮播图
    //    var res = WebService.getParadiseBanner();
    //    $scope.loadState0=1;
    //    res.$promise.then(function(response) {
    //        var data=response.data.data;
    //        var length=data.length;
    //        if(length<=0) {
    //            $ionicPopup.alert({
    //                title: '提示',
    //                template: '无数据！'
    //            });
    //            return;
    //        }
    //        $scope.items_video.push($scope.count0+8);
    //        $scope.loadState0=0;
    //        $scope.count0=$scope.count0+1;
    //        if($scope.count0>=5){
    //            $scope.loadState0=2;
    //        }
    //        $ionicScrollDelegate.resize();
    //        //$scope.$digest();
    //    });
    //}

    ////选项卡1操作
    //$scope.items_audio=[0,1,2,3,4,5,6,7];
    //$scope.playAudio= function (idx) {
    //    $state.go('audioPlay',{ themeid:"aa" });
    //};
    //    //加载更多
    //$scope.loadState1=0;
    //$scope.count1=0;
    //$scope.loadMoreData1=function(){
    //    //获取轮播图
    //    var res = WebService.getParadiseBanner();
    //    $scope.loadState1=1;
    //    res.$promise.then(function(response) {
    //        var data=response.data.data;
    //        var length=data.length;
    //        if(length<=0) {
    //            $ionicPopup.alert({
    //                title: '提示',
    //                template: '无数据！'
    //            });
    //            return;
    //        }
    //        $scope.items_audio.push($scope.count1+8);
    //        $scope.loadState1=0;
    //        $scope.count1=$scope.count1+1;
    //        if($scope.count1>=6){
    //            $scope.loadState1=2;
    //        }
    //        $ionicScrollDelegate.resize();
    //        //$scope.$digest();
    //    });
    //}

    //选项卡2操作
    //$scope.items_inf=[0,1,2,3,4,5,6,7];
    //$scope.showInfDet= function (idx) {
    //    $state.go('eduInformation_Detail');
    //};
    //    //加载更多
    //$scope.loadState2=0;
    //$scope.count2=0;
    //$scope.loadMoreData2=function(){
    //    //获取轮播图
    //    var res = WebService.getParadiseBanner();
    //    $scope.loadState2=1;
    //    res.$promise.then(function(response) {
    //        var data=response.data.data;
    //        var length=data.length;
    //        if(length<=0) {
    //            $ionicPopup.alert({
    //                title: '提示',
    //                template: '无数据！'
    //            });
    //            return;
    //        }
    //        $scope.items_inf.push($scope.count2+8);
    //        $scope.loadState2=0;
    //        $scope.count2=$scope.count2+1;
    //        if($scope.count2>=6){
    //            $scope.loadState2=2;
    //        }
    //        $ionicScrollDelegate.resize();
    //        //$scope.$digest();
    //    });
    //}

})


