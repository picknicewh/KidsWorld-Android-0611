/**
 * Created by JIANBO on 2016/8/1.
 */
'use strict';
angular.module('app.filters', []);

angular.module('app.filters')
.filter('numberTo4', function() {
    return function(input) {
        if (input>=1000) {
            var newNum=(input/10000).toFixed(1);
            return newNum+'万';
        }else{
            return input;
        }
    }
});
angular.module('app.filters')
.filter('dataToMinute', function() {
    return function(input) {
        return input.substring(0,16);
    }
});
angular.module('app.filters')
.filter('dataToday',function(){
        return function(input){
            return input.slice(0,11);
        }
    });
angular.module('app.filters')
    .filter('dataTime',function(){
        return function(input){
            return input.slice(0,10);
        }
    });