/**
 * Created by yhy on 2016/10/11.
 */
angular.module('app.controllers')

    .controller('medicineEntrustCtrl',function($scope, $state,$ionicPopup,WebService,CommonService) {


        $scope.$on('$ionicView.beforeEnter',function(){
            //$scope.tsId = store.get("tsId"); //获取缓存里的tsId;
            //$scope.tsId = "a1eda6bc285d462698b869564e4592f1";//测试
            //alert(new Date());
            $scope.param ={};
            //alert(tsIdLogin);
            //$scope.param.selectedDate = new Date();

        });


        $scope.$on('$ionicView.afterEnter',function(){

            //初始化日历
            var input1=document.getElementById('input1');
            var calendar1=new mCalendar({
                //可选参数
                //'setDate':'2016-12-12', //注* 日期格式：2015-01-01
                'multiple':true,//多选
                //必填参数
                'toBind':input1,//绑定触发日历的元素
                'callback':function(){ //确定选择执行回调
                    input1.setAttribute('value',this.outputDate);
                    input1.value = this.outputDate;
                    $scope.param.selectedDate = this.outputDate.replace(/\s/ig,'');
                    //输出日期 this.outputDate
                }
            });

            $scope.toggleRadio = function(event){
                //alert(2);
                $(event.target).addClass("on").siblings().removeClass("on");
               /* var medicineTime = $('.radio_meal.on').children('input').val();
                alert(medicineTime);*/
            }
            $scope.toggleInput = function(event){
                $(event.target).parent().addClass("on").siblings().removeClass("on");
                /*var medicineTime = $('.radio_meal.on').children('input').val();
                alert(medicineTime);*/
            }
           /* $(".radio_meal").on("tap",function(){
                alert(2);
                $(this).addClass("on").siblings().removeClass("on");
            });*/

        });


        //var input1=document.getElementById('input1');
        //alert(3);

        //提交数据
        /*
        * param createTime喂药日期，medicineName药物名称，medicineDosage药物用量，medicineDoc备注，mealBeforeOrAfter餐前餐后，
        * tsId家长ID
        * */
       $scope.submitData = function(){
           //获取单选按钮的值
           //var medicineTime = $('input:radio[name="meal"]:checked').val();
           //var medicineTime = $('input:radio[name="meal"].on').val();
           var medicineTime = $('.radio_meal.on').children('input').val();

           var data = {
               createTime:$scope.param.selectedDate,
               medicineName:$scope.param.medicineName,
               medicineDosage:$scope.param.medicineCounts,
               medicineDoc:$scope.param.remark,
               mealBeforeOrAfter:medicineTime,
               //tsId:$scope.tsId
               tsId:tsIdLogin
           };
           var res = WebService.submitEnturst(data);
           res.$promise.then(function(response) {
               if(response && response.data.code == 0){
                   //CommonService.showAlert.show('提交成功');
                   $state.go('medicineListP');
               }else {
                   CommonService.showAlert.show('提交失败！请重新提交');
               }
           });

       };

        //返回按钮
        $scope.goBack=function(){
            $state.go('medicineListP');
        };

        //修复bug,textarea被虚拟键盘挡住
        $('textarea').on('tap', function () {
            var target = this;
            setTimeout(function(){
                target.scrollIntoViewIfNeeded();
                //console.log('scrollIntoViewIfNeeded');
            },400);
        });


    });