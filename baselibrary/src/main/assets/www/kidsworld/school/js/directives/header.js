'use strict';
angular.module('app.directives').directive('templateHeader', function() {
  return {
    restrict: 'EA',
    templateUrl: 'views/headers/root.html',
    transclude: true,
    scope: false,
    controller: ['$scope', '$state', function($scope, $state) {
      $scope.getState = function(){
        if (!$state.current.templateUrl){
          return 'views/headers/notes/notes.list.html';
        }
      	return 'views/headers/'+ $state.current.templateUrl.substring(6);
      }
    }]
  }
});