'use strict';
angular.module('app.controllers')
.controller('ParadiseHomeCtrl', function($scope, $state, $ionicPopup,WebService) {
    //视图渲染前，从服务器取得图片数据
    $scope.$on('$ionicView.beforeEnter', function() {
        var res = WebService.getParadiseBanner();
        return res.$promise.then(function(response) {
            var data=response.data.data;
            var length=data.length;
            if(length<=0) {
                $ionicPopup.alert({
                    title: '提示',
                    template: '无数据！'
                });
                return;
            }
            $scope.banImgs=data;
            bannerAutoPlay(length);
        });
    });

    $scope.goChildClass= function () {
        $state.go('childClass');
    };
    $scope.goChildMusic= function () {
        $state.go('childMusic');
    };
    $scope.goEduInformation= function () {
        $state.go('eduInformation');
    };

    $scope.items_eduInf=[0,1,2,3];
    $scope.items_audio=[0,1,2,3];
    $scope.items_video=[0,1,2,3];

    $scope.showEduInf= function (idx) {
        $state.go('eduInformation_Detail');
    };
    $scope.playAudio= function (idx) {
        $state.go('audioPlay');
    };
    $scope.playVideo= function () {
        $state.go('videoPlay');
    };

    //轮播图自动播放方法，需传入图片数量
    function bannerAutoPlay(length){
        $scope.bannerCur=0;
        var bannerPlay=function(){
            if($scope.bannerCur>=(length-1)){
                $scope.bannerCur=0;
            }else{
                $scope.bannerCur++;
            }
            $scope.$digest();
        }
        var bannnerTimer=setInterval(bannerPlay,3000);
    }
})
