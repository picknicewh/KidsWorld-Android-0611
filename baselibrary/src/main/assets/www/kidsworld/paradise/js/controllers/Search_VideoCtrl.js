'use strict';
angular.module('app.controllers')
.controller('Search_VideoCtrl', function($scope, $state,$ionicPopup, $ionicScrollDelegate, WebService,CommonService) {

    //视图渲染前，从服务器取得图片数据
    $scope.$on('$ionicView.beforeEnter', function() {
        //隐藏头部导航栏
        if (platFlag == 1) {
            window.location.href = 'tooc://changenavigationtitle:?null&搜索课程*null';
        } else if (platFlag == 0) {
            change_tb.setToolBar("search_cause");
        }

        $scope.init();
    });
    //返回按钮
    $scope.goBack= function () {
        $state.go('childClass');
    };
    //初始化
    $scope.init=function(){
        $scope.showHisBox=1;
        $scope.showResultBox=0;
        $scope.inputFocusing=0;
        $scope.loadState=0;
        $scope.reset();

        $scope.getSearchHis();
    };
    //输入框获得焦点
    $scope.inputFocus=function(){
        $scope.getSearchHis();
        $scope.inputFocusing=1;
    }
    //输入框失去焦点
    $scope.inputBlur=function(){
        $scope.inputFocusing=0;
    }
    //获得搜索历史
    $scope.getSearchHis=function(){
        //获取轮播图
        //var res = WebService.getSearchHis();
        //res.$promise.then(function(response) {
        //    var data=response.data.data;
        //    $scope.items_searchHis=data;
        //});
        $scope.items_searchHis=CommonService.searchHisService.getVideoHis();
    }
    //清空按钮
    $scope.reset= function () {
        $scope.keyWord="";
    }
    //搜索按钮
    $scope.search=function(){
        //var key=$('.filter')[0].value;
        var key=$scope.keyWord;
        //if(!key){
        //    $ionicPopup.alert({
        //        title: '提示',
        //        template: '未输入关键字'
        //    });
        //    return;
        //}

        $scope.keyForSearch=key;
        CommonService.searchHisService.addVideoHis(key);
        //$scope.keyForSearch="话";
        $scope.pageNumber=1;
        $scope.items_video=[];
        $scope.searchByKey();
    };
    //快速搜索
    $scope.quickSearch=function(key){
        $scope.keyForSearch=key;
        //$scope.keyForSearch="话";
        $scope.items_video=[];
        $scope.pageNumber=1;
        $scope.searchByKey();
    };
    //点击搜索结果区，隐藏搜索记录
    $scope.hideHisBox= function () {
        $scope.showHisBox=0;
    };
    //根据关键字，向后台发送搜索请求
    $scope.searchByKey=function(){
        var res = WebService.getResourceListByName($scope.keyForSearch,0,6,$scope.pageNumber);
        $scope.showResultBox=1;
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
            $scope.items_video=$scope.items_video.concat(data);
            $ionicScrollDelegate.resize();
            $scope.showHisBox=0;
        });
    };
    //加载更多
    $scope.loadMoreData=function(){
        $scope.pageNumber=$scope.pageNumber+1;
        $scope.searchByKey();
    }
    //搜索结果控制
    $scope.playVideo= function ( item ) {
        //$state.go('videoPlay',{ themeid:'aa',resourceid:'bb' });
        //$state.go('videoPlay',{ themeid:item.themeid,resourceid:item.resourceid });
        $state.go('videoPlay',{ themeid:item.themeid,resourceid:item.id });
    };
});
