/**
 * Created by lenovo2 on 2016/7/26.
 */

angular.module('app.controllers')
    .controller('noticeReadCtrl',function($scope, $state, NoteService) {
        //$scope.loaded = false;
        //NoteService.all().success(function(response){
        //    $scope.notes = response;
        //    $scope.loaded = true;
        //});
        //标题加载部分
        $scope.$on('$ionicView.beforeEnter', function() {
            var  plat = testPlat();
            if(plat==1){
                window.location.href='tooc://changenavigationtitle:?通知';

            }else if(plat==0){

                //change_ngb.setNavigationbar("通知",null);
            }else{

            }
            //

            //window.location.href='#/checkOnclass';
        });
        //$scope.$on('$ionicView.beforeEnter', function() {
        //    window.location.href='tooc://changenavigationtitle:?通知';
        //    window.location.href= change_ng.setNavigationBar("通知",null);
        //    //window.location.href='#/checkOnclass';
        //});
        //进入下一级
        $scope.goSearch= function () {
            //window.location.href='tooc://changenavigationtitle:?选择学生';
            $state.go('notificationDetails');
            clearInterval($scope.bannnerTimer);
            //goSearch_Origin();
            //$state.go('search');
        };
        //返回到上一级
        $scope.goBack= function () {
            $state.go('home');
            //window.location.href='#/home';
            window.history.back='#/home';
            clearInterval($scope.bannnerTimer);

        }
    })