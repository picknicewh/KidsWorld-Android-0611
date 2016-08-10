/**
 * Created by lenovo2 on 2016/7/27.
 */
angular.module('app.controllers')

    .controller('courseArrCtrl',function($scope, $state, NoteService,WebService,$ionicPopup) {
        //页面无内容时，提示无数据
        $scope.pro = false;
        var pageNumber = 1,
            pageSize = 10;
        $scope.tsId=getUrlParam("TsId");
        //alert(tsId);
        $scope.$on("$ionicView.beforeEnter", function(){
            getData();
        });
        //删除整个item
        $scope.del=function(index,syllabusId) {
        //删除数组
            var res = WebService.delteList($scope.tsId, syllabusId);
            res.$promise.then(function (response) {
                data = response.data;
                if (data.code == "0") {
                    $scope.items.splice(index, 1);
                }
            });
        }
        //获取数据
        function getData() {
            var res = WebService.getCourse($scope.tsId,pageNumber,pageSize);
            res.$promise.then(function(response) {
                data = response.data;
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
    });