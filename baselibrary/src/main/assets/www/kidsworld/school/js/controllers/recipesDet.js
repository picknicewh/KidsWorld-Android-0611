/**
 * Created by lenovo2 on 2016/7/26.
 */
angular.module('app.controllers')
    .controller('recipesDetCtrl', function($scope, $state, NoteService) {
        //标题加载部分
        $scope.$on('$ionicView.beforeEnter', function() {
            var  plat = testPlat();
            if(plat==1){
                window.location.href='tooc://changenavigationtitle:?食谱详情';

            }else if(plat==0){

                change_ngb.setNavigationbar("午餐",null);
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