'use strict';
angular.module('app.controllers')
.controller('SearchCtrl', function($scope, $state, $ionicPopup,$ionicScrollDelegate,WebService,CommonService) {

    $scope.$on('$ionicView.beforeEnter', function() {
        //隐藏头部导航栏
        if (platFlag == 1) {
            window.location.href='tooc://changenavigationtitle:?null&搜索*null';
        } else if (platFlag == 0) {
            change_tb.setToolBar("search");
        }
        $scope.showHisBox=1;
        $scope.showResultBox=0;
        $scope.inputFocusing=0;
        $scope.reset();

        $scope.getSearchHis();
    });
    //输入框获得焦点
    $scope.inputFocus=function(){
        $scope.getSearchHis();
        $scope.inputFocusing=1;
    }
    //输入框失去焦点
    $scope.inputBlur=function(){
        $scope.inputFocusing=0;
    }
    //返回按钮
    $scope.goBack= function () {
        $state.go('paradiseHome');
    };
    //获得搜索历史
    $scope.getSearchHis=function(){
        //var res = WebService.getSearchHis();
        //res.$promise.then(function(response) {
        //    var data=response.data.data;
        //    $scope.items_searchHis=data;
        //});
        $scope.items_searchHis=CommonService.searchHisService.getGeneralHis();
    };
    //清空按钮
    $scope.reset= function () {
        $scope.keyWord="";
        //$('.filter').focus();
    };
    //选项卡切换
    $scope.tabCur=4;
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
        $state.go('eduInformation_Detail',{ resourceid:'aa' });
        //$state.go('eduInformation_Detail',{ resourceid:item.resourceid });
    };

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
        CommonService.searchHisService.addGeneralHis(key);
        //$scope.keyForSearch="话";
        $scope.doSearch();
    };
    //快速搜索
    $scope.quickSearch=function(key){
        $scope.keyForSearch=key;
        //$scope.keyForSearch="话";
        $scope.doSearch();
    };
    //点击搜索结果区，隐藏搜索记录
    $scope.hideHisBox= function () {
        $scope.showHisBox=0;
    };
    //搜索操作
    $scope.doSearch=function(){
        $scope.tabCur=0;
        $scope.pageNumber0=1;
        $scope.pageNumber1=1;
        $scope.pageNumber2=1;
        $scope.items_audio=[];
        $scope.items_video=[];
        $scope.items_inf=[];
        $scope.searchByKey0();
        $scope.searchByKey1();
        $scope.searchByKey2();
    }
    //根据关键字，向后台发送搜索请求0
    $scope.loadState0=0;
    $scope.searchByKey0=function(){
        var res = WebService.getResourceListByName($scope.keyForSearch,1,6,$scope.pageNumber0);
        $scope.showResultBox=1;
        $scope.loadState0=1;
        res.$promise.then(function(response) {
            var data=response.data.data;
            var length=data.length;
            $scope.loadState0=0;
            if(length<6) {
                $scope.loadState0=2;
                if(length<=0){
                    return;
                }
            }
            $scope.items_audio=$scope.items_audio.concat(data);
            $ionicScrollDelegate.resize();
            $scope.showHisBox=0;
        });
    };
    $scope.loadMoreData0=function(){
        $scope.pageNumber0=$scope.pageNumber0+1;
        $scope.searchByKey0();
    }

    //根据关键字，向后台发送搜索请求1
    $scope.loadState1=0;
    $scope.searchByKey1=function(){
        var res = WebService.getResourceListByName($scope.keyForSearch,0,6,$scope.pageNumber1);
        $scope.showResultBox=1;
        $scope.loadState1=1;
        res.$promise.then(function(response) {
            var data=response.data.data;
            var length=data.length;
            $scope.loadState1=0;
            if(length<6) {
                $scope.loadState1=2;
                if(length<=0){
                    return;
                }
            }
            $scope.items_video=$scope.items_video.concat(data);
            $ionicScrollDelegate.resize();
            $scope.showHisBox=0;
        });
    };
    $scope.loadMoreData1=function(){
        $scope.pageNumber1=$scope.pageNumber1+1;
        $scope.searchByKey1();
    }

    //根据关键字，向后台发送搜索请求2
    $scope.loadState2=0;
    $scope.searchByKey2=function(){
        var res = WebService.getResourceListByName($scope.keyForSearch,2,6,$scope.pageNumber2);
        $scope.showResultBox=1;
        $scope.loadState2=1;
        res.$promise.then(function(response) {
            var data=response.data.data;
            var length=data.length;
            $scope.loadState2=0;
            if(length<6) {
                $scope.loadState2=2;
                if(length<=0){
                    return;
                }
            }
            $scope.items_inf=$scope.items_inf.concat(data);
            $ionicScrollDelegate.resize();
            $scope.showHisBox=0;
        });
    };
    $scope.loadMoreData2=function(){
        $scope.pageNumber2=$scope.pageNumber2+1;
        $scope.searchByKey2();
    }


})
