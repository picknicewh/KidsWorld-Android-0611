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
