'use strict';
angular.module('app.controllers')
.controller('ChildMusic_SortCtrl', function($scope, $state, WebService) {
    $scope.goBack= function () {
        $state.go('childMusic');
    };

    $scope.items_audio=[0,1,2,3,4,5,6];
    $scope.playAudio= function (idx) {
        $state.go('audioPlay');
    };
})
