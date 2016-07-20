//通用后台数据交换服务，包含get和post方法
angular.module('app.services').factory('WebService', function(AjaxService) {
    return {
        getParadiseBanner: function() {
            var options={};
            options.type = "get";
            options.url = "jsonTemp/paradiseBanner.json";
            return AjaxService.ajax(options);
        },
    };
});

