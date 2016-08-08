/**
 * Created by lenovo2 on 2016/7/26.
 */
angular.module('app.controllers')
    .controller('checkOnclassCtrl',function($scope, $state, NoteService) {
        //$scope.loaded = false;
        //NoteService.all().success(function(response){
        //    $scope.notes = response;
        //    $scope.loaded = true;
        //});
        //标题加载部分
        $scope.$on('$ionicView.beforeEnter', function() {
            var  plat = testPlat();
            if(plat==1){
                window.location.href='tooc://changenavigationtitle:?选择班级';

            }else if(plat==0){

                 change_ngb.setNavigationbar("选择班级",null);
            }else{

            }
            //

            //window.location.href='#/checkOnclass';
        });
        //进入下一级
        $scope.goSearch= function () {
            $state.go('checkOnperson');
            clearInterval($scope.bannnerTimer);
        };
        //返回到上一级
        $scope.goBack= function () {
            $state.go('home');
            window.history.back='#/home';
            clearInterval($scope.bannnerTimer);
        }
        //班级
        $scope.items=[{
            name:"小小班(01)班",url:"./images/sign-right.png",id:"周助"
        },{
            name:"小小班(02)班",url:"./images/sign-right.png"
        },{
            name:"小班(01)班",url:"./images/sign-right.png"
        },{
            name:"小班(02)班",url:"./images/sign-right.png"
        }
        ]
    })