/**
 * Created by yhy on 2016/10/11.
 */
angular.module('app.controllers')

    .controller('medicineTaskCtrl',function($scope, $state,$ionicPopup,$stateParams,$ionicHistory,WebService,CommonService) {

        //var tsIdLogin = "cea03c849fd542df8f69174f79072108";//测试
        $scope.$on('$ionicView.beforeEnter',function(){

            $scope.medicineId = $stateParams["medicineId"];//获取页面传参
            $scope.status = $stateParams["status"];//0未喂药 1已喂药 2家长进入（无头像和按钮）
            $scope.item = [];
            getData($scope.medicineId);
            //alert($scope.medicineId)


        });

        //点击返回按钮，跳转到喂药列表页
        $scope.goBack = function(){

            $ionicHistory.goBack();
            //$state.go("medicineListT");
        };

        //点击头像，到原生聊天页面
        $scope.toPerson = function(childId) {

            //var childId = "25c874b12c0043ef87dcdba85db64f51";//测试
            if(platFlag == 0){//android终端
                change_tb.goDetailActivity(childId);
            }else if(platFlag == 1){//ios终端
                OCdynamicId.iosUsercomment(childId);
            }else{
                return;
            }
        };


        //获取数据
        function getData(medId) {
            var data = "";

            var res = WebService.getMedicineTaskDetails(medId);
            res.$promise.then(function (response) {

                data = response.data.data;//喂药详情

                if(response.data.code == 0) {
                    //对medicineStatusList字段进行处理
                    var temp = "";
                    var timeList = data.medicineStatusList;
                    if(timeList && timeList.length !== 0){
                        if(timeList.length == 1){
                            temp = timeList[i].create_time.slice(0,10)
                        }else{
                            for(var i in timeList){
                                if(i == timeList.length - 1){
                                    temp += timeList[i].create_time.slice(0,10);
                                }else {
                                    temp += timeList[i].create_time.slice(0,10) + ";   ";
                                }

                            }
                        }

                        data.medicineStatusList = temp;

                    }

                    //将获取的数据赋值给页面变量
                        $scope.item = data;


                }
            });

        }

        //完成喂药
        //$scope.finishTask = function(event){
        $scope.finishTask = function(){
            var res = WebService.finishedMedicine(tsIdLogin,$scope.medicineId);
            res.$promise.then(function (response) {
                if(response && response.data.code == 0){
                    //提交委托成功
                    //$scope.status == 1;
                    //$(event.target).parent().addClass('ng-hide').next().removeClass('ng-hide');
                   /* CommonService.showAlert.show("提交成功");
                    $ionicHistory.goBack();*/
                    CommonService.showAlert1.show("提交成功");
                    setTimeout(function(){
                        $ionicHistory.goBack();
                    },2000);


                } else {
                    CommonService.showAlert.show("提交失败");
                }
            })
        };




    });