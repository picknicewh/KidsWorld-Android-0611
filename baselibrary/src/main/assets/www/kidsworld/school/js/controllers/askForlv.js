/**
 * Created by lenovo2 on 2016/7/29.
 */
angular.module('app.controllers')

    .controller('askForlvCtrl',function($scope, $state,$ionicScrollDelegate,$ionicPopup,$parse, NoteService,WebService,$timeout,$http) {
        //页面无内容时，提示无数据
        //$scope.pro = false;
        $scope.loadState = 0;
        var pageNumber = 1,
            pageSize = 15;
        $scope.tsId=getUrlParam("tsId");
//        $scope.tsId = "12345678901";
        // 视图显示之前需要完成的任务
        $scope.$on('$ionicView.beforeEnter', function() {
            //$scope.pro = false;
            //$scope.noMore = false;
            //$scope.doMore = true;
            $scope.items=[];
            getData();
        });

        //获取数据列表
        function getData() {
            //alert('弹出');
            var res = WebService.getLeaveList($scope.tsId,pageNumber,pageSize);
            $scope.loadState = 1;
            res.$promise.then(function(response) {
                var data = response.data.data;
                var length = data.length;
                $scope.loadState=0;
                if(response.data.code == 0){

                    if(length < pageSize) {
                        $scope.loadState=2;
                        if(length<=0){
                            return;
                        }
                    }
                    $scope.items=$scope.items.concat(data);
                    $ionicScrollDelegate.resize();
                }else{
                    $scope.loadState=2;
                }


               /* if(length=0){

                    if(pageIndex == 1) {
                        $scope.pro = true;
                    }

                }else{
                    $scope.items=$scope.items.concat(data);

                }
                $scope.domore=false;
                $scope.noMore = true;*/

            });
        }

        //上拉加载
        $scope.pullUp = function(){
            pageNumber++;
            getData();

        }

    });