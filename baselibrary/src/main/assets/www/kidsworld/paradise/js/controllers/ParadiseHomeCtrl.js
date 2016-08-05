'use strict';
angular.module('app.controllers')
.controller('ParadiseHomeCtrl', function($scope, $state, $ionicPopup, WebService,CommonService) {
    //视图渲染前操作
    $scope.$on('$ionicView.beforeEnter', function() {
        //隐藏头部导航栏
        if(platFlag==1){
            window.location.href='tooc://changenavigationtitle:?history&乐园*search';
        }else if(platFlag==0){
            change_tb.setToolBar("home");
        }
        //初始化加载页面
        $scope.init();
    });
    //初始化方法
    $scope.init=function(){

        ////获取轮播图
        //var res = WebService.getParadiseBanner();
        //res.$promise.then(function(response) {
        //    var data=response.data.data;
        //    var length=data.length;
        //    if(length<=0) {
        //        $ionicPopup.alert({
        //            title: '提示',
        //            template: '无数据！'
        //        });
        //        return;
        //    }
        //    $scope.banImgs=data;
        //    bannerAutoPlay(length);
        //    $scope.banImgsLength=length;
        //});
        //$scope.items_audio=[0,1,2,3];
        //$scope.items_video=[0,1,2,3];
        //$scope.items_eduInf=[0,1,2,3];

        $scope.banImgs=[];
        $scope.items_audio=[];
        $scope.items_video=[];
        $scope.items_eduInf=[];
        $scope.doRefresh();
    }
    //页面下拉刷新，重新加载页面资源方法
    $scope.doRefresh=function(){
        //页面刷新需2个ajax请求，刷新前将ajaxCount重置为0
        $scope.ajaxCount=0;

        //获取首页轮播图
        var res = WebService.getHomeBanner( tsIdLogin );
        res.$promise.then(function(response) {
            //页面刷新需2个ajax请求，当2个ajax都返回的时候将刷新图标隐藏
            $scope.ajaxCount=$scope.ajaxCount+1;
            if($scope.ajaxCount=2){
                $scope.$broadcast('scroll.refreshComplete');
            }
            var data=response.data.data;
            var length=data.length;
            if(length<=0) {
                //$ionicPopup.alert({
                //    title: '提示',
                //    template: '无banner数据！'
                //});
                CommonService.showAlert.show('无banner数据！');
                return;
            }
            $scope.banImgs=data;
            bannerAutoPlay(length);
            $scope.banImgsLength=length;
        });

        //获取首页全部推荐
        var res1 = WebService.getIndexAllRecommenda( tsIdLogin,4 );
        res1.$promise.then(function(response) {

            //页面刷新需2个ajax请求，当2个ajax都返回的时候将刷新图标隐藏
            $scope.ajaxCount=$scope.ajaxCount+1;
            if($scope.ajaxCount=2){
                $scope.$broadcast('scroll.refreshComplete');
            }
            //所有推荐数据
            var data=response.data.data;
            var length=data.length;
            if(length<=0) {
                //$ionicPopup.alert({
                //    title: '提示',
                //    template: '无推荐数据！'
                //});
                CommonService.showAlert.show('无推荐数据！');
                return;
            }
            //推荐故事数据
            var data_0=data[0].contentJsonList;
            $scope.items_audio=data[0].contentJsonList;
            var length_0=data_0.length;
            if(length_0<=0) {
                //$ionicPopup.alert({
                //    title: '提示',
                //    template: '无推荐故事数据！'
                //});
                CommonService.showAlert.show('无推荐故事数据！');
            }
            //推荐课堂数据
            var data_1=data[1].contentJsonList;
            $scope.items_video=data[1].contentJsonList;
            var length_1=data_1.length;
            if(length_1<=0) {
                //$ionicPopup.alert({
                //    title: '提示',
                //    template: '无推荐课堂数据！'
                //});
                CommonService.showAlert.show('无推荐课堂数据！');
            }
            //推荐资讯数据
            var data_2=data[2].contentJsonList;
            $scope.items_eduInf=data[2].contentJsonList;
            var length_2=data_2.length;
            if(length_2<=0) {
                //$ionicPopup.alert({
                //    title: '提示',
                //    template: '无推荐资讯数据！'
                //});
                CommonService.showAlert.show('无推荐资讯数据！');
            }
        });
    };
    //请求失败时,广播刷新完成时间
    $scope.$on('ajaxError',function(){
        $scope.$broadcast('scroll.refreshComplete');
    });

    //搜索按钮
    $scope.goSearch= function () {
        clearInterval($scope.bannnerTimer);
        $state.go('search');
    };
    //历史记录按钮
    $scope.goHistory= function () {
        clearInterval($scope.bannnerTimer);
        $state.go('history');
    };
    //跳转各板块方法
    $scope.goChildClass= function () {
        //testios();
        clearInterval($scope.bannnerTimer);
        $state.go('childClass');
    };
    $scope.goChildMusic= function () {
        clearInterval($scope.bannnerTimer);
        $state.go('childMusic');
    };
    $scope.goEduInformation= function () {
        clearInterval($scope.bannnerTimer);
        $state.go('eduInformation');
    };

    //点击资源，直接跳到各播放页面
    $scope.showEduInf= function ( item ) {
        clearInterval($scope.bannnerTimer);
        //$state.go('eduInformation_Detail',{ resourceid:5 });
        $state.go('eduInformation_Detail',{ resourceid:item.resourceid });
    };
    $scope.playAudio= function ( item ) {
        clearInterval($scope.bannnerTimer);
        //$state.go('audioPlay',{ themeid:'aa',resourceid:null });
        $state.go('audioPlay',{ themeid:item.id,resourceid:null });
    };
    $scope.playVideo= function ( item ) {
        clearInterval($scope.bannnerTimer);
        //$state.go('videoPlay',{ themeid:'aa',resourceid:null });
        $state.go('videoPlay',{ themeid:item.id,resourceid:null });
    };

    //轮播图自动播放方法，需传入图片数量
    function bannerAutoPlay(length){
        $scope.bannnerTimer=null;
        $scope.bannerCur=0;
        var bannerPlay=function(){
            if($scope.bannerCur>=(length-1)){
                $scope.bannerCur=0;
            }else{
                $scope.bannerCur++;
            }
            $scope.$digest();
        };
        $scope.bannnerTimer=setInterval(bannerPlay,3000);

    }
    //左滑
    $scope.onSwipeLeft= function () {
        if($scope.bannerCur>=($scope.banImgsLength-1)){
            $scope.bannerCur=0;
        }else{
            $scope.bannerCur++;
        }
        //$scope.$digest();
    };
    //右滑
    $scope.onSwipeRight = function () {
        if($scope.bannerCur<=0){
            $scope.bannerCur=$scope.banImgsLength-1;
        }else{
            $scope.bannerCur--;
        }
        //$scope.$digest();
    }


})
