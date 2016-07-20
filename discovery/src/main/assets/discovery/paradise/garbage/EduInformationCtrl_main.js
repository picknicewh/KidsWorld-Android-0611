'use strict';
angular.module('app.controllers')
.controller('EduInformationCtrl_main', function($scope, $state, WebService) {
    //选项卡切换
    $scope.tabCur=0;
    $scope.changeTab=function(index){
        $scope.tabCur=index;
        if(index==0){
            $state.go("eduInformation_main.tab0");
        }else if(index==1){
            $state.go("eduInformation_main.tab1");
        }else if(index==2){
            $state.go("eduInformation_main.tab2");
        }else if(index==3){
            $state.go("eduInformation_main.tab3");
        }
    };
})
