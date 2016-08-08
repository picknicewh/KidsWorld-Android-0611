/**
 * Created by lenovo2 on 2016/7/29.
 */
angular.module('app.controllers')

    .controller('askForlvCtrl',function($scope, $state,$ionicPopup,$parse, NoteService,WebService) {
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
                if(data.code == "0"){
                    $scope.items = data.data;
                    pageNumber++;
                }
                if(data.code == "1") {
                    $ionicPopup.alert({
                        title: '提示',
                        template: '无数据！'
                    });
                    return;
                }
            });
        }
    });