'use strict';
angular.module('app.controllers')
.controller('AudioPlayCtrl', function($scope, $state, $ionicHistory, WebService, CommonService) {
    $scope.goBack=function(){
        $ionicHistory.goBack();
    };

    $scope.store=function(){
        setTimeout(function(){
            CommonService.showAlert.show("收藏成功");
        },1000);
    }



})
