/**
 * Created by lenovo2 on 2016/7/29.
 */
angular.module('app.controllers')

    .controller('askForlvCtrl',function($scope, $state,$ionicPopup,$parse, NoteService,WebService,$timeout,$http) {
        //页面无内容时，提示无数据
        $scope.pro = false;
        var pageNumber = 1,
            pageSize = 15;
            $scope.tsId=getUrlParam("TsId");
        // 视图显示之前需要完成的任务
        $scope.$on('$ionicView.beforeEnter', function() {
            getData();
        });
        //获取数据列表
        function getData() {
            var res = WebService.getLeaveList($scope.tsId,pageNumber,pageSize);
            res.$promise.then(function(response) {
                data = response.data;
                var length = data.data.length;
                if(data.code == "0"){
                    $scope.items = data.data;
                    pageNumber++;
                    //页面无内容时，提示无数据
                    if(length=0){
                        $scope.pro = true;
                    }
                }
                if(data.code == "1") {
                    $ionicPopup.alert({
                        title: '提示',
                        template: '无数据！'
                    });
                    //页面无内容时，提示无数据
                        $scope.pro = true;
                    return;
                }
            });
        }
        $scope.doRefresh=function(){
                var res = WebService.getLeaveList($scope.tsId,pageNumber,pageSize);
                res.$promise.then(function(response) {
                    $scope.$broadcast('scroll.refreshComplete');
                    var data=response.data.data;
                    var length=data.length;
                    if(length<=0) {
                        $ionicPopup.alert({
                            title: '提示',
                            template: '无数据！'
                        });
                        return;
                    }
                });
        }
    });