  //通用后台数据交换服务，包含get和post方法
angular.module('app.services').factory('WebService', function(AjaxService) {
    return {
        //获取首页轮播图-本机测试
        /*getParadiseBanner: function() {
            var options={};
            options.type = "get";
            options.useBaseUrl = false;
            options.url = "jsonTemp/paradiseBanner.json";
            return AjaxService.ajax(options);
        },*/
       //获取食谱列表
        getFoodList: function(tsId,date) {
            var options = {};
            options.type = "post";
            options.useBaseUrl = true;
            options.url = "/school/getCookbook.do";
            options.data = {
                "tsId":tsId,
                "date":date
            };
            return AjaxService.ajax(options);
        },
        //获取课程表
        getCourse: function(tsId,pageNumber,pageSize) {
            var options = {};
            options.type = "post";
            options.useBaseUrl = true;
            options.url = "/school/getSyllabusList.do";
            options.data = {
                "tsId":tsId,
                "pageNumber":pageNumber,
                "pageSize":pageSize
            };
            return AjaxService.ajax(options);
        },
        //删除课程表
        delteList:function(tsId,syllabusId){
            var options = {};
            options.type = "post";
            options.useBaseUrl = true;
            options.url = "/school/deleteSyllabus.do";
            options.data = {
                "tsId" : tsId,
                "syllabusId" :syllabusId
            };
            return AjaxService.ajax(options);
        },
        //获取请假列表
        getLeaveList: function(tsId,pageNumber,pageSize) {
            var options = {};
            options.type = "post";
            options.useBaseUrl = true;
            options.url = "/school/getLeave.do";
            options.data = {
                "tsId":tsId,
                "pageNumber":pageNumber,
                "pageSize":pageSize
            };
            return AjaxService.ajax(options);
        }
    };
});

