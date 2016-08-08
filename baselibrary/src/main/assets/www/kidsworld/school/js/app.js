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
            .state('home', {
                url: '/home',
                templateUrl: 'views/home/home.html',
                controller: 'AppCtrl'
            })
            .state('openClass',{
                url:'/openClass',
                templateUrl:'views/home/openClass.html',
                controller:'openClassCtrl'
            })
            .state('noticeRead',{
                url:'/noticeRead',
                templateUrl:'views/home/noticeRead.html',
                controller:'noticeReadCtrl'
            })
            .state('notificationDetails',{
                url:'/notificationDetails',
                templateUrl:'views/home/notificationDetails.html',
                controller:'notificationDetailsCtrl'
            })
            .state('foodList',{
                url:'/foodList',
                templateUrl:'views/home/foodList.html',
                controller:'foodListCtrl'
            })
            .state('foodListDet',{
                url:'/foodListDet',
                templateUrl:'views/home/foodListDet.html',
                controller:'foodListDetCtrl'
            })
            //.state('recipes',{
            //    url:'/recipes',
            //    templateUrl:'views/home/recipes.html',
            //    controller:'recipesCtrl'
            //})
            //.state('recipesDet',{
            //    url:'/recipesDet',
            //    templateUrl:'views/home/recipesDet.html',
            //    controller:'recipesDetCtrl'
            //})
            .state('checkOn',{
                url:'/checkOn',
                templateUrl:'views/home/checkOn.html',
                controller:'checkOnCtrl'
            })
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
            .state('checkOnclass',{
                url:'/checkOnclass',
                templateUrl:'views/home/checkOnclass.html',
                controller: 'checkOnclassCtrl'
            })
            .state('checkOnperson',{
                url:'/checkOnperson',
                templateUrl:'views/home/checkOnperson.html',
                controller: 'checkOnpersonCtrl'
            })

            $urlRouterProvider.otherwise('home');
    });

