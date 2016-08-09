'use strict';
angular.module('app.controllers')
.controller('EduInformationCtrl', function($scope, $state, WebService) {
        //视图渲染前，从服务器取得图片数据
        $scope.$on('$ionicView.beforeEnter', function() {
            //隐藏头部导航栏
            if (platFlag == 1) {
                window.location.href = 'tooc://changenavigationtitle:?null&教育资讯*search_Inf';
            } else if (platFlag == 0) {
                change_tb.setToolBar("consult");
            }
            $scope.$broadcast('getInEduInformation');
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
        $scope.goSearch_Inf= function () {
            $state.go('search_Inf');
        };
    })

    .controller('InfSort0Ctrl', function($scope, $state, WebService ,$ionicScrollDelegate) {
        //视图渲染前，从服务器取得图片数据
        $scope.$on('getInEduInformation', function() {
            //初始化
            $scope.init();
        });
        //初始化，加载第一页数据
        $scope.init=function(){
            $scope.items_inf=[];
            $scope.pageNumber=1;
            $scope.loadState=0;
            $scope.loadDataByPageNumber($scope.pageNumber);
        }
        //下拉刷新
        $scope.doRefresh=function(){
            $scope.init();
        };
        //控制
        $scope.showInfDet= function ( item ) {
            //$state.go('eduInformation_Detail',{ resourceid:'aa' });
            $state.go('eduInformation_Detail',{ resourceid:item.id });
        };
        //控制-加载更多
        $scope.loadMoreData=function(){
            $scope.pageNumber=$scope.pageNumber+1;
            $scope.loadDataByPageNumber($scope.pageNumber);
        };
        //根据页码查询推荐内容
        $scope.loadDataByPageNumber=function(pageNumber){
            var res = WebService.getContent( tsIdLogin,2,6,pageNumber,201);
            $scope.loadState=1;
            res.$promise.then(function(response) {
                if(pageNumber=1){
                    $scope.$broadcast('scroll.refreshComplete');
                }
                var data=response.data.data;
                var length=data.length;
                $scope.loadState=0;
                if(length<6) {
                    $scope.loadState=2;
                    if(length<=0){
                        return;
                    }
                }
                $scope.items_inf=$scope.items_inf.concat(data);
                $ionicScrollDelegate.resize();
            });
        }
    })

    .controller('InfSort1Ctrl', function($scope, $state, WebService ,$ionicScrollDelegate) {
        //视图渲染前，从服务器取得图片数据
        $scope.$on('getInEduInformation', function() {
            //初始化
            $scope.init();
        });
        //初始化，加载第一页数据
        $scope.init=function(){
            $scope.items_inf=[];
            $scope.pageNumber=1;
            $scope.loadState=0;
            $scope.loadDataByPageNumber($scope.pageNumber);
        }
        //下拉刷新
        $scope.doRefresh=function(){
            $scope.init();
        };
        //控制
        $scope.showInfDet= function ( item ) {
            //$state.go('eduInformation_Detail',{ resourceid:'aa' });
            $state.go('eduInformation_Detail',{ resourceid:item.id });
        };
        //控制-加载更多
        $scope.loadMoreData=function(){
            $scope.pageNumber=$scope.pageNumber+1;
            $scope.loadDataByPageNumber($scope.pageNumber);
        };
        //根据页码查询推荐内容
        $scope.loadDataByPageNumber=function(pageNumber){
            var res = WebService.getContent( tsIdLogin,2,6,pageNumber,202);
            $scope.loadState=1;
            res.$promise.then(function(response) {
                if(pageNumber=1){
                    $scope.$broadcast('scroll.refreshComplete');
                }
                var data=response.data.data;
                var length=data.length;
                $scope.loadState=0;
                if(length<6) {
                    $scope.loadState=2;
                    if(length<=0){
                        return;
                    }
                }
                $scope.items_inf=$scope.items_inf.concat(data);
                $ionicScrollDelegate.resize();
            });
        }
    })

    .controller('InfSort2Ctrl', function($scope, $state, WebService, $ionicScrollDelegate) {
        //视图渲染前，从服务器取得图片数据
        $scope.$on('getInEduInformation', function() {
            //初始化
            $scope.init();
        });
        //初始化，加载第一页数据
        $scope.init=function(){
            $scope.items_inf=[];
            $scope.pageNumber=1;
            $scope.loadState=0;
            $scope.loadDataByPageNumber($scope.pageNumber);
        }
        //下拉刷新
        $scope.doRefresh=function(){
            $scope.init();
        };
        //控制
        $scope.showInfDet= function ( item ) {
            //$state.go('eduInformation_Detail',{ resourceid:'aa' });
            $state.go('eduInformation_Detail',{ resourceid:item.id });
        };
        //控制-加载更多
        $scope.loadMoreData=function(){
            $scope.pageNumber=$scope.pageNumber+1;
            $scope.loadDataByPageNumber($scope.pageNumber);
        };
        //根据页码查询推荐内容
        $scope.loadDataByPageNumber=function(pageNumber){
            var res = WebService.getContent( tsIdLogin,2,6,pageNumber,203);
            $scope.loadState=1;
            res.$promise.then(function(response) {
                if(pageNumber=1){
                    $scope.$broadcast('scroll.refreshComplete');
                }
                var data=response.data.data;
                var length=data.length;
                $scope.loadState=0;
                if(length<6) {
                    $scope.loadState=2;
                    if(length<=0){
                        return;
                    }
                }
                $scope.items_inf=$scope.items_inf.concat(data);
                $ionicScrollDelegate.resize();
            });
        }
    })

    .controller('InfSort3Ctrl', function($scope, $state, WebService, $ionicScrollDelegate) {
        //视图渲染前，从服务器取得图片数据
        $scope.$on('getInEduInformation', function() {
            //初始化
            $scope.init();
        });
        //初始化，加载第一页数据
        $scope.init=function(){
            $scope.items_inf=[];
            $scope.pageNumber=1;
            $scope.loadState=0;
            $scope.loadDataByPageNumber($scope.pageNumber);
        }
        //下拉刷新
        $scope.doRefresh=function(){
            $scope.init();
        };
        //控制
        $scope.showInfDet= function ( item ) {
            //$state.go('eduInformation_Detail',{ resourceid:'aa' });
            $state.go('eduInformation_Detail',{ resourceid:item.id });
        };
        //控制-加载更多
        $scope.loadMoreData=function(){
            $scope.pageNumber=$scope.pageNumber+1;
            $scope.loadDataByPageNumber($scope.pageNumber);
        };
        //根据页码查询推荐内容
        $scope.loadDataByPageNumber=function(pageNumber){
            var res = WebService.getContent( tsIdLogin,2,6,pageNumber,204);
            $scope.loadState=1;
            res.$promise.then(function(response) {
                if(pageNumber=1){
                    $scope.$broadcast('scroll.refreshComplete');
                }
                var data=response.data.data;
                var length=data.length;
                $scope.loadState=0;
                if(length<6) {
                    $scope.loadState=2;
                    if(length<=0){
                        return;
                    }
                }
                $scope.items_inf=$scope.items_inf.concat(data);
                $ionicScrollDelegate.resize();
            });
        }
    });
