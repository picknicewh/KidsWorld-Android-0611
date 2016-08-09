'use strict';
angular.module('app.controllers')
.controller('EduInformation_DetailCtrl', function($scope, $state, $stateParams, $ionicHistory, WebService, CommonService) {

    //视图渲染前，从服务器取得图片数据
    $scope.$on('$ionicView.beforeEnter', function() {
        //隐藏头部导航栏
        if (platFlag == 1) {
            window.location.href = 'tooc://changenavigationtitle:?null&教育资讯*null';
        } else if (platFlag == 0) {
            change_tb.setToolBar("consultDetail");
        }
        $scope.resourceid=$stateParams["resourceid"];
        $scope.init();
    });

    $scope.init= function () {
        //var name=store.get('displayInfName');
        //var content=store.get('displayInfCnt');
        //
        //$scope.displayInfName=name;
        //$scope.displayInfCnt=content;
        //$scope.storeActive=false;

        var res = WebService.getResourceDetail( tsIdLogin,$scope.resourceid );
        res.$promise.then(function(response) {
            var data=response.data.data;
            if(!data) {
                $ionicPopup.alert({
                    title: '提示',
                    template: '无资源数据！'
                });
                return;
            }
            $scope.displayInfName=data.name;
            $scope.displayInfCnt=data.content;

            if(data.attentionid){
                $scope.storeActive=true;
            }else{
                $scope.storeActive=false;
            }
            //$scope.storeActive=data.attentionid;
            CommonService.playHisService.addInfHis(data);
        });

    }

    //返回按钮
    $scope.goBack=function(){
        $ionicHistory.goBack();
    };
    //收藏按钮
    $scope.store=function(){
        //$scope.resourceid=1;
        if($scope.storeActive){
            var cancel=2;
        }else{
            var cancel=1;
        }
        var res = WebService.subAttention(tsIdLogin,$scope.resourceid,cancel);
        res.$promise.then(function (response) {
            var data = response.data;
            if (data.code==0) {
                CommonService.showAlert.show(data.data);
                if($scope.storeActive){
                    $scope.storeActive=false;
                    data.attentionid=false;
                }else{
                    $scope.storeActive=true;
                    data.attentionid=true;
                }
            }else{
                CommonService.showAlert.show(data.data);
            }
        });

    };
});
