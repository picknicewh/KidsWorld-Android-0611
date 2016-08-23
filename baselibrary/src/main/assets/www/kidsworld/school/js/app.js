'use strict';
angular.module('app', ['ui.router','ionic','ionic-datepicker','onezone-datepicker','app.filters','app.services', 'app.controllers', 'app.constants', 'app.directives'])

    .run(function($rootScope, $state) {
        $rootScope.$on('$stateChangeStart', function (event, next) {
        });
        //phonegap deviceready function. init globals and local storage items here
        function onDeviceReady() {
        }
        document.addEventListener('deviceready', onDeviceReady, false);
    })

    .config(function($stateProvider, $urlRouterProvider) {
        $stateProvider
            .state('foodList',{
                url:'/foodList',
                templateUrl:'views/home/foodList.html',
                controller:'foodListCtrl'
            })
          /*  .state('foodListDet',{
                url:'/foodListDet',
                templateUrl:'views/home/foodListDet.html',
                controller:'foodListDetCtrl'
            })*/
            .state('askForlv',{
                url:'/askForlv',
                templateUrl:'views/home/askForlv.html',
                controller:'askForlvCtrl'
            })
            .state('courseArr',{
                url:'/courseArr',
                templateUrl:'views/home/courseArr.html',
                controller:'courseArrCtrl'
            })


            //$urlRouterProvider.otherwise('home');
    });

