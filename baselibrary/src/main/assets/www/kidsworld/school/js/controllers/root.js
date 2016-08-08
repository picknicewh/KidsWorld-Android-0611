'use strict';
angular.module('app.controllers').controller('AppCtrl', function($scope, $state, $location, $rootScope) {

    $scope.open = function(state, params){
        $state.go(state, params);
    };
	
});
