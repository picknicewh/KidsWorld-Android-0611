'use strict';
angular.module('app', ['ui.router','ngAnimate','ionic','app.services', 'app.controllers', 'app.constants', 'app.directives','app.filters'])

    .run(function($rootScope, $state) {
        $rootScope.$on('$stateChangeStart', function (event, next) {
        });
        //phonegap deviceready function. init globals and local storage items here
        function onDeviceReady() {
        }
        document.addEventListener('deviceready', onDeviceReady, false);
    })


    .config(function($stateProvider, $urlRouterProvider, $ionicConfigProvider) {

        //$ionicConfigProvider.platform.ios.tabs.position('bottom');
        //$ionicConfigProvider.platform.android.tabs.position('bottom');
        //$ionicConfigProvider.scrolling.jsScrolling(true);
        //$ionicConfigProvider.views.transition("none");
        //$ionicConfigProvider.views.forwardCache(false);
        $ionicConfigProvider.views.maxCache(1);
        //路由配置
        $stateProvider
            .state('medicineEntrust',{
                url:'/medicineEntrust',
                templateUrl:'views/medicineEntrust.html',
                controller:'medicineEntrustCtrl'
            })
            .state('medicineListP',{
                url:'/medicineListP',
                templateUrl:'views/medicineListP.html',
                controller:'medicineListPCtrl'
            })
            .state('medicineListT',{
                url:'/medicineListT',
                templateUrl:'views/medicineListT.html',
                controller:'medicineListTCtrl'
            })
            .state('medicineTask',{
                url:'/medicineTask?medicineId&status',
                templateUrl:'views/medicineTask.html',
                controller:'medicineTaskCtrl'
            });
            //设置默认路由
            //$urlRouterProvider.otherwise('paradiseHome');
    });
