/**
 * Created by lenovo2 on 2016/8/3.
 */
angular.module('app.controllers')
    .controller('foodListDetCtrl', function($scope, $state, NoteService) {
        //标题加载部分
        $scope.$on('$ionicView.beforeEnter', function() {
            var  plat = testPlat();
            if(plat==1){
                window.location.href='tooc://changenavigationtitle:?早餐';
            }else if(plat==0){
                change_ngb.setNavigationbar("早餐",null);
            }else{
            }
        });
        $scope.goBack= function () {
            $state.go('recipes');
            //window.location.href='#/home';
            window.history.back='#/recipes';
            clearInterval($scope.bannnerTimer);
        }
    })