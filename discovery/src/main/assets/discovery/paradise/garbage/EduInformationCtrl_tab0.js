'use strict';
angular.module('app.controllers')
.controller('EduInformationCtrl_tab0', function($scope, $state, WebService) {


    $scope.doRefresh=function(){
        setTimeout(function () {
            $scope.$broadcast('scroll.refreshComplete');
        },5000)
    };
    $scope.hasMore=true;
    //$scope.i0=1;
    $scope.loadMoreData=function(){
        setTimeout(function (){
            $scope.$broadcast('scroll.infiniteScrollComplete');
        },5000);

        //$scope.i0++;
        //if($scope.i0>=5){
        //    $scope.hasMore0=false;
        //}
    };


})
