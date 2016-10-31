/**
 * Created by yhy on 2016/10/11.
 */
angular.module('app.controllers')

    .controller('medicineListPCtrl',function($scope, $state,$ionicScrollDelegate,$ionicPopup,WebService) {

        //参数预设
        //从url获取参数
        //var tsId = getUrlParam("tsId");
        //store.set(tsId,tsId);//将tsId存入缓存
        //var tsId = "a1eda6bc285d462698b869564e4592f1";//测试
        var pageSize = 10;
        var pageNumber = 1;


        $scope.$on("$ionicView.beforeEnter",function(){
            $scope.item1 = [];
            $scope.item2 = [];
            $scope.loadState=0;
            $scope.pro = false;
            //获取数据
            getData(pageNumber);

        });

        //选项卡切换
        $scope.tabCur=0;
        $scope.changeTab=function(index){
            $scope.tabCur=index;
        };

        //滑动切换tab页
        $scope.onSwipeTo= function(index) {
            $scope.tabCur=index;
        };

        //获取数据
        function getData(pageNumber) {

            var res = WebService.getMedicineP(tsIdLogin, pageNumber, pageSize);
            $scope.loadState=1;

            res.$promise.then(function (response) {

               /* if(response && response.data.code == 0){
                    $scope.item1 = response.data.data.medicineList; //喂药列表
                    $scope.item2 = response.data.data.medicineScheduleJson; //今日喂药流程反馈
                }*/
                $scope.loadState = 0;
                if(response && response.data.code == 0) {
                    var data1 = response.data.data.medicineList,//喂药列表
                        data2 = response.data.data.medicineScheduleJson,//今日喂药流程反馈
                        length1 = data1.length,
                        length2 = data2.length;

                    /*if (length1 < pageSize) {//即没有下一页
                        if (length1 <= 0) {
                            $scope.loadState = 2;
                            return;
                        }else{
                            $scope.item1 = $scope.item1.concat(data1);
                        }
                    }*/


                    //今日进度页
                    if (length2 > 0) {
                        $scope.pro = false;
                        $scope.item2 = $scope.item2.concat(data2);
                    } else {
                        $scope.pro = true;
                    }


                    if(length1 < pageSize) {
                        $scope.loadState=2;
                        if(length1<=0){
                            return;
                        }
                    }

                    $scope.item1 = $scope.item1.concat(data1);

                    $ionicScrollDelegate.resize();

                }else{
                    $scope.loadState = 2;
                    $scope.pro = true;
                }

            });
        }


        //加载更多委托信息
        $scope.loadMoreData=function(){
            pageNumber++;
            getData(pageNumber);
        };

        //点击喂药，跳转到喂药详情页
        $scope.toDetails = function(medicineId,status){

            $state.go("medicineTask",{medicineId:medicineId,status:status});
        };


        //弹窗confirm提示框
        $scope.showConfirm = function(index,medicineId) {
            var confirmPopup = $ionicPopup.confirm({
                title: '确认删除吗',
                cancelText: '取消', // String (default: 'Cancel'). The text of the Cancel button.
                okText: '确定', // String (default: 'OK'). The text of the OK button.

            });
            confirmPopup.then(function(res) {
                if(res) {
                    del(index,medicineId);
                } else {
                    console.log('You are not sure');
                }
            });
        };


            //执行删除委托操作
            function del(index,medicineId) {

                var res = WebService.delEntrust(tsIdLogin,medicineId);
                res.$promise.then(function(response) {
                    if(response && response.data.code == 0) {
                        //alert("删除成功");
                        $scope.item1.splice(index, 1);
                    }else {
                        //提示删除失败，成功则不提示
                        CommonService.showAlert("操作失败");
                    }

                });
            }


        //返回按钮
        $scope.goBack=function(){

            if(platFlag == 0){//android终端
                //alert("0");
                change_tb.back();
            }else if(platFlag == 1){//ios终端
                //alert("1");
                OCdynamicId.iosUsercomment("userBack");//返回原生页面
            }else{
                return;
            }
           /* alert(Hunmeios);
            alert(Hunmeios.toSource());
            alert(Hunmeios.UserClick);*/
            //alert(OCdynamicId.iosUsercomment);
            //OCdynamicId.iosUsercomment("fjeiwfjdfhj888");
            //Hunmeios.UserClick("userBack");//返回原生页面
        };

       //委托按钮--到委托填写页面
        $scope.toEntrust = function(){
            $state.go('medicineEntrust');
        }

    });
