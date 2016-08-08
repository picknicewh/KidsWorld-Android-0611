/**
 * Created by yanghy on 2016/7/30.
 */
angular.module('app.controllers')
    .controller('foodListCtrl', function($scope,$state,NoteService,WebService){
        //日期格式转换
        Date.prototype.Format = function (fmt) {
            var o = {
                "M+": this.getMonth() + 1, //月份
                "d+": this.getDate(), //日
                "h+": this.getHours(), //小时
                "m+": this.getMinutes(), //分
                "s+": this.getSeconds(), //秒
                "q+": Math.floor((this.getMonth() + 3) / 3), //季度
                "S": this.getMilliseconds() //毫秒
            };
            if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
            for (var k in o)
                if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
            return fmt;
        };
        //变量声明
        var tsId=getUrlParam("TsId");
        tsId = "81c5dc8725044e629cf524a3222cd818";

        //日历初始化
        var currentDate = new Date();
        var date = new Date(currentDate.getFullYear(), currentDate.getMonth(), currentDate.getDate());
        $scope.date = date;

        $scope.myFunction=function(date){
            //alert(date);
            var dateString = date.Format("yyyy-MM-dd");
            getFoodList(tsId,dateString);
        };

        $scope.onezoneDatepicker = {
            date: date,
            mondayFirst: false,
            months: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
            daysOfTheWeek: ["日", "一", "二", "三", "四", "五", "六"],
            startDate: new Date(1989, 1, 26),
            endDate: new Date(2024, 1, 26),
            disablePastDays: false,
            disableSwipe: false,
            disableWeekend: false,
            //disableDates: [new Date(date.getFullYear(), date.getMonth(), 15), new Date(date.getFullYear(), date.getMonth(), 16), new Date(date.getFullYear(), date.getMonth(), 17)],
            showDatepicker: false,
            showTodayButton: true,
            calendarMode: false,
            hideCancelButton: false,
            hideSetButton: true,
            callback: $scope.myFunction
        };
        $scope.showDatepicker = function () {
            $scope.onezoneDatepicker.showDatepicker = true;
        };
        $scope.goSearch= function () {
            $state.go('foodListDet');
            //clearInterval($scope.bannnerTimer);
        };

        function getFoodList(tsId,dateKey){
            var res = WebService.getFoodList(tsId,dateKey);
            res.$promise.then(function(response) {
                $scope.items = response.data.data.dishesList;
                if($scope.items.length <= 0) {
                    $ionicPopup.alert({
                        title: '提示',
                        template: '无数据！'
                    });
                    return;
                }
            });
        };


    });