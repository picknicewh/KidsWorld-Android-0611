/**
 * Created by lenovo2 on 2016/7/27.
 */
angular.module('app.controllers')

    .controller('courseArrCtrl',function($scope, $state, NoteService,WebService,$ionicPopup) {
        var pageNumber = 1,
            pageSize = 10;
        $scope.tsId=getUrlParam("TsId");
        //alert(tsId);
        $scope.$on("$ionicView.beforeEnter", function(){
            getData();
        });
        //$scope.items=[{
        //   title:"本周的课程",state:true,url:"./images/classlist.jpg",creationTime:"2016-01-01"
        //}
        //];

        //删除整个item
        $scope.del=function(index,syllabusId) {

            //删除数组
            var res = WebService.delteList($scope.tsId, syllabusId);
            res.$promise.then(function (response) {
                data = response.data;
                if (data.code == "0") {

                    //$scope.items = data.data;
                    $scope.items.splice(index, 1);
                }

                //if(data.code == "1") {
                //    $ionicPopup.alert({
                //        title: '提示',
                //        template: '无数据！'
                //    });
                //    return;
                //}

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
        //获取数据
        //function getData(){
        //    var res = WebService.getCourse($scope.tsId,pageNumber,pageSize);
        //    res.$promise.then(function(response) {
        //
        //        $scope.items = response.data.SyllabusJson;
        //        if($scope.items.dishesList.length <= 0) {
        //            $ionicPopup.alert({
        //                title: '提示',
        //                template: '无数据！'
        //            });
        //
        //        }
        //
        //    });
        //}
        ////获取数据列表

    });