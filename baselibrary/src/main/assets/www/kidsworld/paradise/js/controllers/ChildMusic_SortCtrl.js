'use strict';
angular.module('app.controllers')
.controller('ChildMusic_SortCtrl', function($scope, $state, $stateParams, WebService, $ionicScrollDelegate) {

    //视图渲染前，从服务器取得图片数据
    $scope.$on('$ionicView.beforeEnter', function() {

        $scope.typeid=$stateParams["typeid"];
        $scope.typename=$stateParams["typename"];
        //隐藏头部导航栏
        if (platFlag == 1) {
            window.location.href = 'tooc://changenavigationtitle:?null&某类幼儿听听*null';
        } else if (platFlag == 0) {
            change_tb.setClassCauseTitle($scope.typename);
        }

        //初始化
        $scope.init();
    });
    //返回按钮
    $scope.goBack= function () {
        $state.go('childMusic');
    };
    //初始化
    $scope.init=function(){
        //初始化精选加载第一页
        $scope.items_audio=[];
        $scope.pageNumber=1;
        $scope.loadState=0;
        $scope.loadDataByPageNumber($scope.pageNumber);
    }
    //控制
    $scope.playAudio= function ( item ) {
        //$state.go('audioPlay',{ themeid:'aa',resourceid:null });
        $state.go('audioPlay',{ themeid:item.id,resourceid:null });
    };
        //控制-加载更多
    $scope.loadMoreData=function(){
        $scope.pageNumber=$scope.pageNumber+1;
        $scope.loadDataByPageNumber($scope.pageNumber);
    }
    //根据页面查询推荐内容
    $scope.loadDataByPageNumber=function(pageNumber){
        var res = WebService.getContent( tsIdLogin,1,6,pageNumber,$scope.typeid);
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
})
