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
            $scope.doMore = true;
            $scope.getData();
        });
        //获取数据列表
        $scope.getData = function(){
        //}
        //function getData() {
            //alert('弹出');
            var res = WebService.getLeaveList($scope.tsId,pageNumber,pageSize);
            res.$promise.then(function(response) {
                data = response.data;
                var length = data.data.length;
                if(data.code == "0"){
                    $scope.items = data.data;
                    //页面无内容时，提示无数据
                    if(length=0){
                        $scope.pro = true;
                        $scope.doMore = false;
                    }
                }
                if(data.code == "1") {
                    //$ionicPopup.alert({
                    //    title: '提示',
                    //    template: '无数据！'
                    //});
                    //页面无内容时，提示无数据
                        $scope.pro = true;
                    return;
                }
            });
        }
        function pullUp(){
            getData();
            pageNumber++;
        }
        //$scope.hasmore=true;
        //var run = false;//模拟线程锁机制  防止多次请求 含义：是否正在请求。请注意，此处并非加入到了就绪队列，而是直接跳过不执行
        //console.log($scope.hasmore+"是否加载更多");
        //var obj = {pageNumber:1,pageSize:2};
        //var result = chushihua(obj,1);
        //
        ////上拉加载
        //$scope.loadMore = function(){
        //    var old = $scope.project;
        //    if(old!=undefined){
        //        var result = chushihua(obj,3);
        //    }
        //    $scope.$broadcast('scroll.infiniteScrollComplete');
        //};
        //function chushihua(obj_data,state) {
        //    if (!run) {
        //        run = true;
        //        $http({
        //            method: 'POST',
        //            url: '/school/getLeave.do',
        //            data: WebService.getLeaveList($scope.tsId, pageNumber, pageSize),
        //            headers: {'Content-Type': 'application/json;charset=utf-8'},
        //            dataType: 'JSON'
        //        }).success(function (data, status) {
        //            run = false;
        //            if(state==3){
        //                $scope.project = $scope.project.concat(data.result);
        //                if(data.result==null || data.result.length==0){
        //                    console.log("结束");
        //                    $scope.hasmore=false;
        //                }else{
        //                    obj.current += obj.count;
        //                }
        //            }else{
        //                $scope.project = data.result;
        //            }
        //        }).error(function(data,status){
        //
        //        })
        //
        //    }
        //}
    });