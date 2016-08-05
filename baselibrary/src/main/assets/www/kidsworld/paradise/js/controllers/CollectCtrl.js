'use strict';
angular.module('app.controllers')
.controller('CollectCtrl', function($scope, $state, WebService,$ionicScrollDelegate) {

    //视图渲染前，从服务器取得图片数据
    $scope.$on('$ionicView.beforeEnter', function() {
        //隐藏头部导航栏
        if (platFlag == 1) {
            window.location.href = 'tooc://changenavigationtitle:?null&我的收藏*null';
        } else if (platFlag == 0) {
            change_tb.setToolBar("collect");
        }

        $scope.init();
    });
    //选项卡切换
    $scope.tabCur=0;
    $scope.changeTab=function(index){
        $scope.tabCur=index;
    };

    $scope.playVideo= function ( item ) {
        //$state.go('videoPlay',{ themeid:'aa',resourceid:'bb' });
        $state.go('videoPlay',{ themeid:item.themeid,resourceid:item.id });
    };
    $scope.playAudio= function ( item ) {
        //$state.go('audioPlay',{ themeid:'aa',resourceid:'bb' });
        $state.go('audioPlay',{ themeid:item.themeid,resourceid:item.id });
    };
    $scope.showInfDet= function ( item ) {
        //$state.go('eduInformation_Detail',{ resourceid:'aa' });
        $state.go('eduInformation_Detail',{ resourceid:item.resourceid });
    };

    //搜索操作
    $scope.init=function(){
        $scope.pageNumber0=1;
        $scope.pageNumber1=1;
        $scope.pageNumber2=1;
        $scope.items_audio=[];
        $scope.items_video=[];
        $scope.items_inf=[];
        $scope.searchCollect0();
        $scope.searchCollect1();
        $scope.searchCollect2();
    }
    //根据关键字，向后台发送搜索请求--幼儿故事
    $scope.loadState0=0;
    $scope.searchCollect0=function(){
        var res = WebService.getMyAttentionResourceList(tsIdLogin,1,10,$scope.pageNumber0);
        $scope.loadState0=1;
        res.$promise.then(function(response) {
            var data=response.data.data;
            var length=data.length;
            $scope.loadState0=0;
            if(length<10) {
                $scope.loadState0=2;
                if(length<=0){
                    return;
                }
            }
            $scope.items_audio=$scope.items_audio.concat(data);
            $ionicScrollDelegate.resize();
        });
    };
    $scope.loadMoreData0=function(){
        $scope.pageNumber0=$scope.pageNumber0+1;
        $scope.searchCollect0();
    }

    //根据关键字，向后台发送搜索请求--幼儿课堂
    $scope.loadState1=0;
    $scope.searchCollect1=function(){
        var res = WebService.getMyAttentionResourceList(tsIdLogin,0,10,$scope.pageNumber1);
        $scope.loadState1=1;
        res.$promise.then(function(response) {
            var data=response.data.data;
            var length=data.length;
            $scope.loadState1=0;
            if(length<10) {
                $scope.loadState1=2;
                if(length<=0){
                    return;
                }
            }
            $scope.items_video=$scope.items_video.concat(data);
            $ionicScrollDelegate.resize();
        });
    };
    $scope.loadMoreData1=function(){
        $scope.pageNumber1=$scope.pageNumber1+1;
        $scope.searchCollect1();
    }

    //根据关键字，向后台发送搜索请求--教育资讯
    $scope.loadState2=0;
    $scope.searchCollect2=function(){
        var res = WebService.getMyAttentionResourceList(tsIdLogin,2,10,$scope.pageNumber2);
        $scope.loadState2=1;
        res.$promise.then(function(response) {
            var data=response.data.data;
            var length=data.length;
            $scope.loadState2=0;
            if(length<10) {
                $scope.loadState2=2;
                if(length<=0){
                    return;
                }
            }
            $scope.items_inf=$scope.items_inf.concat(data);
            $ionicScrollDelegate.resize();
        });
    };
    $scope.loadMoreData2=function(){
        $scope.pageNumber2=$scope.pageNumber2+1;
        $scope.searchCollect2();
    }



})
