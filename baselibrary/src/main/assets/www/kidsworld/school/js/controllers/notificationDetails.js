/**
 * Created by lenovo2 on 2016/7/26.
 */
angular.module('app.controllers')
    .controller('notificationDetailsCtrl', function($scope, $state, NoteService) {

        //标题加载部分
        $scope.$on('$ionicView.beforeEnter', function() {
            var  plat = testPlat();
            if(plat==1){
                window.location.href='tooc://changenavigationtitle:?通知详情';

            }else if(plat==0){

                change_ngb.setNavigationbar("通知详情",null);
            }else{

            }
            //

            //window.location.href='#/checkOnclass';
        });
        //$scope.$on('$ionicView.beforeEnter', function() {
        //    window.location.href='tooc://changenavigationtitle:?通知详情';
        //    window.location.href= change_ng.setNavigationBar("通知详情",null);
        //    //window.location.href='#/checkOnclass';
        //});
        //返回到上一级
        $scope.goBack= function () {
            $state.go('noticeRead');
            window.history.back='#/noticeRead';
            clearInterval($scope.bannnerTimer);
        }
    })
//var u = navigator.userAgent, app = navigator.appVersion;
//function ismobile(test) {
//    if (/AppleWebKit.*Mobile/i.test(navigator.userAgent) || (/MIDP|SymbianOS|NOKIA|SAMSUNG|LG|NEC|TCL|Alcatel|BIRD|DBTEL|Dopod|PHILIPS|HAIER|LENOVO|MOT-|Nokia|SonyEricsson|SIE-|Amoi|ZTE/.test(navigator.userAgent))) {
//        if (window.location.href.indexOf("?mobile") < 0) {
//            try {
//                if (/iPhone|mac|iPod|iPad/i.test(navigator.userAgent)) {
//                    return '0';
//                } else {
//                    return '1';
//                }
//            } catch (e) {
//            }
//        }
//    } else if (u.indexOf('iPad') > -1) {
//        return '0';
//    } else {
//        return '1';
//    }
//}