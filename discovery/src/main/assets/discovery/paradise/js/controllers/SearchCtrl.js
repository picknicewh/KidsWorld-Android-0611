'use strict';
angular.module('app.controllers')
.controller('SearchCtrl', function($scope, $state, WebService) {

    $scope.hasSearch=0;

    $scope.search=function(){
        $scope.hasSearch=1;
    };

    $scope.quickSearch=function(){
        $scope.hasSearch=1;
    };

    //选项卡切换
    $scope.tabCur=0;
    $scope.changeTab=function(index){
        $scope.tabCur=index;
    };

    $scope.goBack= function () {
        $state.go('paradiseHome');
    };

    $scope.items_video=[0,1,2,3,4,5,6];
    $scope.items_audio=[0,1,2,3,4,5,6];
    $scope.items_inf=[0,1,2,3,4,5,6];

    $scope.showInfDet= function (idx) {
        $state.go('eduInformation_Detail');
    };

    $scope.playAudio= function (idx) {
        $state.go('audioPlay');
    };

    $scope.playVideo= function (idx) {
        $state.go('videoPlay');
    };
})
