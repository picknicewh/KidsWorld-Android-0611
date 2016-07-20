'use strict';
angular.module('app.controllers')
.controller('ChildClass_SortCtrl', function($scope, $state, WebService) {
    $scope.goBack= function () {
        $state.go('childClass');
    };

    $scope.items_video=[0,1,2,3,4,5,6];
    $scope.playVideo= function (idx) {
        $state.go('videoPlay');
    };

})
