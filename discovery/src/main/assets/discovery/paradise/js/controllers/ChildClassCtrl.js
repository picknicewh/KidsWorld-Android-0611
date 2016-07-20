'use strict';
angular.module('app.controllers')
.controller('ChildClassCtrl', function($scope, $state, WebService) {

    $scope.tabCur=0;

    $scope.changeTab=function(index){
        $scope.tabCur=index;
        $scope.$digest();
    };

    $scope.goBack= function () {
        $state.go('paradiseHome');
    };

    $scope.items_video=[0,1,2,3,4,5,6];
    $scope.playVideo= function (idx) {
        $state.go('videoPlay');
    };

    $scope.items_videoSort=[0,1,2,3,4,5,6];
    $scope.goVideoSort= function (idx) {
        $state.go('childClass_Sort');
    };


})
