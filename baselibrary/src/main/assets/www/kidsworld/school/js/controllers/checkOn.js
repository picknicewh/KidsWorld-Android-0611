/**
 * Created by lenovo2 on 2016/7/26.
 */
angular.module('app.controllers')
    .controller('checkOnCtrl', function($scope, $state, NoteService) {
        //标题加载部分
        $scope.$on('$ionicView.beforeEnter', function() {
            var  plat = testPlat();
            if(plat==1){
                window.location.href='tooc://changenavigationtitle:?考勤';
            }else if(plat==0){
                change_ngb.setNavigationbar("考勤",null);
            }else{

            }
            //

            //window.location.href='#/checkOnclass';
        });
        //后退按钮
        $scope.goBack= function () {
            window.location.href="#/checkOnperson";
            //window.location.href='tooc://changenavigationtitle:?选择学生';
            $state.go('checkOnperson');
            clearInterval($scope.bannnerTimer);
        }
    })