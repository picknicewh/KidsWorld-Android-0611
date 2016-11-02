/**
 * Created by yhy on 2016/10/11.
 */
angular.module('app.controllers')

    .controller('medicineEntrustCtrl',function($scope, $state,$ionicPopup,WebService,CommonService) {

           //var tsIdLogin = "a1eda6bc285d462698b869564e4592f1";//测试
        $scope.$on('$ionicView.beforeEnter',function(){
            //$scope.tsId = store.get("tsId"); //获取缓存里的tsId;
            //$scope.tsId = "a1eda6bc285d462698b869564e4592f1";//测试
            //alert(new Date());
            $scope.param ={};

            //友盟统计
            MobclickAgent.onPageBegin('medicineEntrust');

             //alert(tsIdLogin);
            //$scope.param.selectedDate = new Date();

        });

        $scope.$on('$ionicView.beforeLeave',function(){
            //友盟统计
            MobclickAgent.onPageEnd('medicineEntrust');
        });

        $scope.$on('$ionicView.afterEnter',function(){

            //初始化日历
            /*var outputDate = [];
            var today = new Date();
            var tempDay = {
                'day':today.getDate(),
                'month':today.getMonth(),
                'year':today.getFullYear()
            };

            var dayNow = new Date(tempDay.year,tempDay.month,tempDay.day);*/

            var input1=document.getElementById('input1');
            var calendar1=new mCalendar({
                //可选参数
                //'setDate':'2016-12-12', //注* 日期格式：2015-01-01
                'multiple':true,//多选
                //必填参数
                'toBind':input1,//绑定触发日历的元素
                'callback':function(){ //确定选择执行回调

                    /*for(var t in this.outputDate){
                        //字符串分割，取出年夜日，生成new Date(yyyy-mm-dd);
                        var temp = new Date(this.outputDate[t]);
                        console.log(temp);
                       if(temp >= dayNow){
                           outputDate.push(this.outputDate[t]);
                       }
                    }
                    this.outputDate = outputDate.length == 1?outputDate:outputDate.sort().join(";   ");*/


                    //input1.setAttribute('value',this.outputDate);
                    input1.value = this.outputDate;
                    $scope.param.selectedDate = this.outputDate;
                    //alert(1);
                    //entrust_form.selectedDate.$error={};
                    //alert(entrust_form.selectedDate.$valid);

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

            }



            //测试
            /*$scope.alertEvent = function(){
                //alert(entrust_form.$dirty);
                alert($scope.entrust_form.selectedDate);
                //alert($scope.param.selectedDateShow);
            }*/
            /*calendar1.then(function(){
                alert(2);
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
           var medicineTime = $('.radio_meal.on').children('input').val();
           var selectedDate = $scope.param.selectedDate;
           if(selectedDate.length <= 0){
               CommonService.showAlert1.show('日期不能为空');
               return;
           }

           selectedDate = selectedDate.length==1?selectedDate.join():selectedDate.replace(/\s/ig,'');

           var data = {
               //createTime:$scope.param.selectedDate,
               createTime:selectedDate,
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

        //修复bug,textarea被虚拟键盘挡住（ios可行，安卓不行）
        /*$('textarea').on('tap', function () {
            var target = this;
            setTimeout(function(){
                target.scrollIntoViewIfNeeded();
                //console.log('scrollIntoViewIfNeeded');
            },500);
        });*/

        //安卓端虚拟键盘弹出后，盖住输入框，所以让其向上滚动（都行，但安卓的点击完成按钮，输入框下不来）
        $scope.scrollToUp=function(){
            /*$scope.positionOrigin = $('.InfDetContent.eduInf>.scroll').css('transform');
            var hight=$('.InfDetContent.eduInf .InfDetContent').height()+60;
            var toUp = 'translate3d( 0px,-'+hight+'px,0 ) scale( 1 )';
            $('.InfDetContent.eduInf>.scroll').css('transform',toUp);*/

            $scope.positionOrigin = $('.hasBottomButton>.scroll').css('transform');
            //var hight=$('.hasBottomButton').height()+60;
            var toUp = 'translate3d( 0px,-300px,0 ) scale( 1 )';//60为虚拟键盘的高度
            $('.hasBottomButton>.scroll').css('transform',toUp);
            //$('e.target').preventDefault();
        };

        $scope.scrollBack=function(){
            //$('.InfDetContent.eduInf>.scroll').css('transform',$scope.positionOrigin);
            $('.hasBottomButton>.scroll').css('transform',$scope.positionOrigin);

        };

        //resize事件监测不到
        /*$(window).resize(function(){
            $('.hasBottomButton>.scroll').css('transform',$scope.positionOrigin);
        });*/

        //方案4,也需要失去焦点事件
        /*var timer, windowInnerHeight;
        function eventCheck(e) {
            if (e) { //blur,focus事件触发的
                //$('#dv').html('android键盘' + (e.type == 'focus' ? '弹出' : '隐藏') + '--通过' + e.type + '事件');
                if (e.type == 'focus') {//如果是点击事件启动计时器监控是否点击了键盘上的隐藏键盘按钮，没有点击这个按钮的事件可用，keydown中也获取不到keyCode值
                    setTimeout(function () {//由于键盘弹出是有动画效果的，要获取完全弹出的窗口高度，使用了计时器
                        windowInnerHeight = window.innerHeight;//获取弹出android软键盘后的窗口高度
                        timer = setInterval(function () { eventCheck() }, 100);
                    }, 500);
                } else clearInterval(timer);

            } else { //计时器执行的，需要判断窗口可视高度，如果改变说明android键盘隐藏了
                if (window.innerHeight > windowInnerHeight) {
                    alert(windowInnerHeight);
                    $('.hasBottomButton>.scroll').css('transform',$scope.positionOrigin);
                    clearInterval(timer);
                    //$('#dv').html('android键盘隐藏--通过点击键盘隐藏按钮');
                }
            }
        }
        $('#big-textarea').focus(eventCheck).blur(eventCheck);*/




    });