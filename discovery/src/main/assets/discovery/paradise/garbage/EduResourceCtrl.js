'use strict';
angular.module('app.controllers')
.controller('EduResourceCtrl', function($scope, $state, WebService) {

    $scope.tabCur=0;

    $scope.changeTab=function(index){
        $scope.tabCur=index;
        $scope.$digest();
    };

})
