'use strict';
angular.module('app.directives').directive('touchableIcon', function() {
  return {
    restrict: 'A',
    transclude: false,
    scope: false,
    link: function(scope, element, attrs){
        element.on('touchstart', function(){
            $(this).addClass('pressed-icon');
        }).on('touchend', function(){
            $(this).removeClass('pressed-icon');
        });
    }
  }
});