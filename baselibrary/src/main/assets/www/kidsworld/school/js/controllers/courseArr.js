/**
 * Created by lenovo2 on 2016/7/27.
 */
angular.module('app.controllers')

    .controller('courseArrCtrl',function($scope, $state,$ionicScrollDelegate, NoteService,WebService,$ionicPopup) {
        //页面无内容时，提示无数据
        $scope.pro = false;
        var pageNumber = 1,
            pageSize = 2;
        $scope.reg =new RegExp(/\/s/,'');
        $scope.imgIndex = 2;
    /*    $scope.tsId=getUrlParam("tsId");
        $scope.type = getUrlParam("type");*/

        //测试
        $scope.tsId = "09e05e5c31f244d3b51e00e5973ab874";
        //$scope.type = 0;
        $scope.type = 2;
        //alert(tsId);

        $scope.$on("$ionicView.beforeEnter", function(){
            $scope.loadState = 0;
            //$scope.pro = false;
            //$scope.doMore = true;
            //$scope.noMore = false;
            $scope.items = [];
            getData();
            mui.previewImage();
        });

        //$scope.$on("")

        //获取数据
        function getData() {
            var res = WebService.getCourse($scope.tsId,pageNumber,pageSize);
            $scope.loadState=1;
            res.$promise.then(function(response) {

                //var data = response.data.data;
                var data=response.data.data,
                    length = data.length;
                $scope.loadState=0;

                if(length < pageSize) {
                    $scope.loadState=2;
                    if(length<=0){
                        return;
                    }
                }
                $scope.items=$scope.items.concat(data);
                $ionicScrollDelegate.resize();


            });
        }



        //上拉加载
        $scope.pullUp = function(){
            pageNumber++;
            getData();
        }

        //删除
        $scope.showConfirm = function(index,syllabusId) {
            var confirmPopup = $ionicPopup.confirm({
                title: '确定删除吗？',
                buttons: [
                    { text: '取消' },
                    {
                        text: '<b>确认</b>',
                        type: 'button-positive'}]

            });
            confirmPopup.then(function(res) {
                if(res) {
                    del(index,syllabusId);
                } else {
                    console.log('You are not sure');
                }
            });
        };

        function del(index,syllabusId) {
            //删除数组
            var res = WebService.delteList($scope.tsId, syllabusId);
            res.$promise.then(function (response) {
                data = response.data;
                if (data.code == "0") {
                    $scope.items.splice(index, 1);
                }
            });
        }

    });