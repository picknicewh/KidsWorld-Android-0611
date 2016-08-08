/**
 * Created by lenovo2 on 2016/7/26.
 */
angular.module('app.controllers')
    .controller('checkOnpersonCtrl', function($scope, $state, NoteService) {
        //$scope.loaded = false;
        //NoteService.all().success(function(response){
        //    $scope.notes = response;
        //    $scope.loaded = true;
        //});
        //标题加载部分
        $scope.$on('$ionicView.beforeEnter', function() {
            var  plat = testPlat();
            if(plat==1){
                window.location.href='tooc://changenavigationtitle:?选择学生';
            }else if(plat==0){
                change_ngb.setNavigationbar("选择学生",null);
            }else{

            }
        });
        $scope.goSearch= function () {
            $state.go('checkOn');
            clearInterval($scope.bannnerTimer);
        };
        $scope.goBack= function () {
            window.location.href='#/checkOnclass';
            $state.go('checkOnclass');
            clearInterval($scope.bannnerTimer);
        }
        //班级人员
        $scope.items=[{
            id:"周助",url:"./images/sign-right.png"
        },{
            id:"王小二",url:"./images/sign-right.png"
        },{
            id:"王小三",url:"./images/sign-right.png"
        },{
            id:"王小四",url:"./images/sign-right.png"
        },{
            id:"王小五",url:"./images/sign-right.png"
        }
        ]
    })


