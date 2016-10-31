/**
 * Created by yhy on 2016/10/11.
 */
angular.module('app.controllers')

    .controller('medicineListTCtrl',function($scope, $state,$ionicPopup,WebService) {

        //����Ԥ��
        //��url��ȡ����
        //var tsId = getUrlParam("tsId");
        //store.set(tsId,tsId);//��tsId���뻺��
        //var tsId = "b6d039a39af64d7295e0f55352ae417a";//����
        //var tsId = "25c874b12c0043ef87dcdba85db64f51";//����
        //store.set(tsId,tsId);//����
        //var tsIdLogin = "b6d039a39af64d7295e0f55352ae417a";//����
        $scope.item1 = [];
        $scope.item2 = [];

        //���ذ�ť
        $scope.goBack=function(){
            if(platFlag == 0){//android�ն�
                //alert("0");
                change_tb.back();
            }else if(platFlag == 1){//ios�ն�
                //alert("1");
                OCdynamicId.iosUsercomment("userBack");//����ԭ��ҳ��
            }else{
                return;
            }
        };


        $scope.$on("$ionicView.beforeEnter",function(){

            //��ȡ����
            getData();

        });


        //��ȡ����
        function getData() {
            var data1 = "",
                data2 = "";
            var res = WebService.getMedicineT(tsIdLogin);
            res.$promise.then(function (response) {

                data1=response.data.data[1];//διҩ�б�
                data2 = response.data.data[2];
                var length1 = data1.length,
                    length2 = data2.length;

                if(response.data.code == 0) {

                    if (length1 > 0) {//��û����һҳ
                        $scope.item1 = data1;
                    }

                    if (length2 > 0) {//��û����һҳ
                        $scope.item2 = data2;
                    }

                }
            });
        }

        //���ιҩ����ת��ιҩ����ҳ
        $scope.toDetails = function(medicineId,status){

            $state.go("medicineTask",{medicineId:medicineId,status:status});
        };

    });
