'use strict';
angular.module('app.controllers')
.controller('EduInformationCtrl', function($scope, $state, WebService) {
    //选项卡切换
    $scope.tabCur=0;
    $scope.changeTab=function(index){
        $scope.tabCur=index;
    };

    $scope.doRefresh=function(){
        setTimeout(function () {
            $scope.$broadcast('scroll.refreshComplete');
        },5000)
    };
    $scope.hasMore=true;
    //$scope.i=1;
    $scope.loadMoreData=function(){
        setTimeout(function (){
            $scope.$broadcast('scroll.infiniteScrollComplete');
        },5000);

        //$scope.i++;
        //if($scope.i>=5){
        //    $scope.hasMore=false;
        //}
    };

    $scope.goBack= function () {
        $state.go('paradiseHome');
    };

    $scope.items_inf0=[0,1,2,3,4,5,6];
    $scope.items_inf1=[0,1,2,3,4,5,6];
    $scope.items_inf2=[0,1,2,3,4,5,6];
    $scope.items_inf3=[0,1,2,3,4,5,6];

    $scope.showInfDet= function (idx) {
        $state.go('eduInformation_Detail');
    };



})
