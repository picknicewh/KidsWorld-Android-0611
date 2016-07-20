'use strict';
angular.module('app.controllers')
.controller('ChildMusicCtrl', function($scope, $state, WebService) {

    $scope.tabCur=0;

    $scope.changeTab=function(index){
        $scope.tabCur=index;
        $scope.$digest();
    };

    $scope.goBack= function () {
        $state.go('paradiseHome');
    };

    $scope.items_audio=[0,1,2,3,4,5,6];
    $scope.playAudio= function (idx) {
        $state.go('audioPlay');
    };

    $scope.items_audioSort=[0,1,2,3,4,5,6];
    $scope.goAudioSort= function (idx) {
        $state.go('childMusic_Sort');
    };

})
