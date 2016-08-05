'use strict';
angular.module('app.controllers')
.controller('ChildMusicCtrl', function($scope, $state, WebService ,$ionicScrollDelegate,$ionicPopup) {

    //视图渲染前，从服务器取得图片数据
    $scope.$on('$ionicView.beforeEnter', function() {
        //隐藏头部导航栏
        if (platFlag == 1) {
            window.location.href = 'tooc://changenavigationtitle:?null&幼儿听听*search_Audio';
        } else if (platFlag == 0) {
            change_tb.setToolBar("mediaplay");
        }
        //初始化
        $scope.init();
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
    //搜索按钮
    $scope.goSearch_Audio= function () {
        $state.go('search_Audio');
    };

    //初始化
    $scope.init=function(){
        //初始化精选加载第一页
        $scope.items_audio=[];
        $scope.pageNumber=1;
        $scope.loadState=0;
        $scope.loadDataByPageNumber($scope.pageNumber);
        //初始化全部加载分类信息
        $scope.loadClassify();
    }
    //精选-控制方法
    $scope.playAudio= function ( item ) {
        //$state.go('audioPlay',{ themeid:'aa',resourceid:null });
        $state.go('audioPlay',{ themeid:item.id,resourceid:null });
    };
    //精选-加载更多
    $scope.loadMoreData=function(){
        $scope.pageNumber=$scope.pageNumber+1;
        $scope.loadDataByPageNumber($scope.pageNumber);
    }
    //根据页码查询推荐内容
    $scope.loadDataByPageNumber=function(pageNumber){
        var res = WebService.getContent( tsIdLogin,1,6,pageNumber);
        $scope.loadState=1;
        res.$promise.then(function(response) {
            var data=response.data.data;
            var length=data.length;
            $scope.loadState=0;
            if(length<6) {
                $scope.loadState=2;
                if(length<=0){
                    return;
                }
            }
            $scope.items_audio=$scope.items_audio.concat(data);
            $ionicScrollDelegate.resize();
        });
    }

    //全部-控制方法
    $scope.goAudioSort= function ( item ) {
        //$state.go('childMusic_Sort',{ typeid:"aa" });
        $state.go('childMusic_Sort',{ typeid:item.id,typename:item.name });
    };
    //获取幼儿听听下子分类
    $scope.loadClassify=function(){
        var res = WebService.getClassify( tsIdLogin,1 );
        res.$promise.then(function(response) {
            var data=response.data.data;
            var length=data.length;
            if(length<=0) {
                $ionicPopup.alert({
                    title: '提示',
                    template: '无分类数据！'
                });
                return;
            }
            $scope.items_audioSort=data;
            $ionicScrollDelegate.resize();
        });
    }

})
