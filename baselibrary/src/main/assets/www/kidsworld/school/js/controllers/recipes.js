/**
 * Created by lenovo2 on 2016/7/26.
 */
angular.module('app.controllers')
    .controller('recipesCtrl', function($scope, $state, NoteService, WebService) {
        //标题加载部分
        $scope.$on('$ionicView.beforeEnter', function() {
            var  plat = testPlat();
            if(plat==1){
                window.location.href='tooc://changenavigationtitle:?食谱';

            }else if(plat==0){

                //change_ngb.setNavigationbar("食谱",null);
            }else{

            }

            var res = WebService.getCookbook();
            res.$promise.then(function(response) {
                var data=response.data.data;
                var length=data.length;
                if(length<=0) {
                    $ionicPopup.alert({
                        title: '提示',
                        template: '无数据！'
                    });
                    return;
                }
                $scope.banImgs=data;

            });


            //

            //window.location.href='#/checkOnclass';
        });
        //$scope.$on('$ionicView.beforeEnter', function() {
        //    window.location.href='tooc://changenavigationtitle:?食谱';
        //    window.location.href= change_ng.setNavigationBar("食谱",null);
        //    //window.location.href='#/checkOnclass';
        //});
        //进入下一级
        $scope.goSearch= function () {
            //window.location.href='tooc://changenavigationtitle:?选择学生';
            $state.go('recipesDet');
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