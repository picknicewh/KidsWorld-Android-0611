'use strict';
angular.module('app.controllers')
    .controller('NoteListCtrl', function($scope, $state, NoteService) {
        $scope.loaded = false;
        NoteService.all().success(function(response){
            $scope.notes = response;
            $scope.loaded = true;
        });
    })

    .controller('NoteDetailCtrl', function($scope, $location, $stateParams, NoteService) {
        $scope.loaded = false;
        // GET '/customers/{id}'
        NoteService.get($stateParams.id).success(function(response){
            console.log(response);
            $scope.note = response;
            $scope.loaded = true;
        });
    })
