//通用后台数据交换服务，包含get和post方法
angular.module('app.services').factory('AjaxService', function($http, $httpParamSerializerJQLike, $q, $ionicLoading, CommonService,BASE_URL) {
    return {
        ajax: function(options) {
            var url = BASE_URL + options.url;
            var httpOption = {method: options.type || "post", url: url};
            if (httpOption.method == "post") {
                httpOption.data = options.data || {};
            } else {
                var pString = $httpParamSerializerJQLike(options.data || {});
                httpOption.url += pString ? "?" + pString : "";
            }
            $ionicLoading.show({
                template: '加载中...'
            });
            var deferred = $q.defer();
            $http(httpOption).success(
                function(data, status, header, config){
                    $ionicLoading.hide();
                    //$ionicPopup.alert({
                    //    title: '提示',
                    //    template: '请求成功！'
                    //});
                    deferred.resolve({
                        data : data
                    });
                }
            ).error(
                function(data, status, header, config){
                    $ionicLoading.hide();
                    //$ionicPopup.alert({
                    //    title: '提示',
                    //    template: '请求失败！'
                    //});

                    CommonService.showAlert.show("请求失败");
                    deferred.reject(data);
                }
            );
            return {
                $promise: deferred.promise
            };
        },
    };
});

