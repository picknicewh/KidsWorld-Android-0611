/**
 * Created by lenovo2 on 2016/7/27.
 */
angular.module('app.controllers')

    .controller('courseArrCtrl',function($scope, $state,$ionicScrollDelegate, NoteService,WebService,$ionicPopup) {
        //页面无内容时，提示无数据
        $scope.pro = false;
        var pageNumber = 1,
            pageSize = 2;
        $scope.tsId=getUrlParam("tsId");
          $scope.type = getUrlParam("type");
//        $scope.tsId = "540813c7aec64327acf0e36ac339d8c0";
//        $scope.type = 0;
        //alert(tsId);
        $scope.$on("$ionicView.beforeEnter", function(){
            $scope.loadState=0;
            //$scope.pro = false;
            //$scope.doMore = true;
            //$scope.noMore = false;
            $scope.items=[];
            getData();
        });

        //获取数据
        function getData() {
            var res = WebService.getCourse($scope.tsId,pageNumber,pageSize);
            $scope.loadState=1;
            res.$promise.then(function(response) {

                //var data = response.data.data;
                var data=response.data.data,
                    length = data.length;
                $scope.loadState=0;
               /* if(!data) {
                    CommonService.showAlert({message:"查询数据失败"});
                    return;
                }*/
                if(length < pageSize) {
                    $scope.loadState=2;
                    if(length<=0){
                        return;
                    }
                }
                $scope.items=$scope.items.concat(data);
                $ionicScrollDelegate.resize();

/*
                if(data.length = 0){
                    if(pageNumber == 1) {
                        $scope.pro = true;
                        $scope.doMore=false;
                        return;
                    }else{
                        $scope.noMore = true;

                    }
                }*/
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
                title: '确定删除吗？'

            });
            confirmPopup.then(function(res) {
                if(res) {
                    //console.log('You are sure');
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