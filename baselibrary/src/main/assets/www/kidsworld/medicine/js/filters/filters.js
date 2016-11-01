/**
 * Created by JIANBO on 2016/8/1.
 */
'use strict';
angular.module('app.filters', []);

angular.module('app.filters')
.filter('numberTo4', function() {
    return function(input) {
        if(!input){
            return 0;
        }else{
            if (input>=1000) {
                var newNum=(input/10000).toFixed(1);
                return newNum+'万';
            }else{
                return input;
            }
        }
    }
});

angular.module('app.filters')
    .filter('timeToMinute', function() {
        return function(input) {
            if(input){
                return input.substring(5,16);
            }else{
                return "";
            }
        }
    });

angular.module('app.filters')
    .filter('dataTime',function(){
        return function(input){
            return input.slice(0,10);
        }
    });