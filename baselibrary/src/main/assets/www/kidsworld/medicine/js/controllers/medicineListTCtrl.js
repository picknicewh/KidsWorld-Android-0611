/**
 * Created by yhy on 2016/10/11.
 */
angular.module('app.controllers')

    .controller('medicineListTCtrl',function($scope, $state,$ionicPopup,WebService) {

        //参数预设
        //从url获取参数
        //var tsId = getUrlParam("tsId");
        //store.set(tsId,tsId);//将tsId存入缓存
        //var tsId = "b6d039a39af64d7295e0f55352ae417a";//测试
        //var tsId = "25c874b12c0043ef87dcdba85db64f51";//测试
        //store.set(tsId,tsId);//测试
        //var tsIdLogin = "b6d039a39af64d7295e0f55352ae417a";//测试
        $scope.item1 = [];
        $scope.item2 = [];

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
        };


        $scope.$on("$ionicView.beforeEnter",function(){

            //获取数据
            getData();

        });


        //获取数据
        function getData() {
            var data1 = "",
                data2 = "";
            var res = WebService.getMedicineT(tsIdLogin);
            res.$promise.then(function (response) {

                data1=response.data.data[1];//未喂药列表
                data2 = response.data.data[2];
                var length1 = data1.length,
                    length2 = data2.length;

                if(response.data.code == 0) {

                    if (length1 > 0) {//即没有下一页
                        $scope.item1 = data1;
                    }

                    if (length2 > 0) {//即没有下一页
                        $scope.item2 = data2;
                    }

                }
            });
        }

        //点击喂药，跳转到喂药详情页
        $scope.toDetails = function(medicineId,status){

            $state.go("medicineTask",{medicineId:medicineId,status:status});
        };

    });
