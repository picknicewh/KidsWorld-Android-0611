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


    .config(function($stateProvider, $urlRouterProvider) {
        $stateProvider
            .state('paradiseHome', {
                url: '/paradiseHome',
                templateUrl: 'views/paradiseHome.html',
                controller: 'ParadiseHomeCtrl'
            })
            .state('search', {
                url: '/search',
                templateUrl: 'views/search.html',
                controller: 'SearchCtrl'
            })
            .state('search_Audio', {
                url: '/search_Audio',
                templateUrl: 'views/search_Audio.html',
                controller: 'Search_AudioCtrl'
            })
            .state('search_Video', {
                url: '/search_Video',
                templateUrl: 'views/search_Video.html',
                controller: 'Search_VideoCtrl'
            })
            .state('search_Inf', {
                url: '/search_Inf',
                templateUrl: 'views/search_Inf.html',
                controller: 'Search_InfCtrl'
            })
            .state('collect', {
                url: '/collect',
                templateUrl: 'views/collect.html',
                controller: 'CollectCtrl'
            })
            .state('history', {
                url: '/history',
                templateUrl: 'views/history.html',
                controller: 'HistoryCtrl'
            })
            .state('childClass', {
                url: '/childClass',
                templateUrl: 'views/childClass.html',
                controller: 'ChildClassCtrl'
            })
            .state('childClass_Sort', {
                url: '/childClass_Sort?typeid&typename',
                templateUrl: 'views/childClass_Sort.html',
                controller: 'ChildClass_SortCtrl'
            })
            .state('childMusic', {
                url: '/childMusic',
                templateUrl: 'views/childMusic.html',
                controller: 'ChildMusicCtrl'
            })
            .state('childMusic_Sort', {
                url: '/childMusic_Sort?typeid&typename',
                templateUrl: 'views/childMusic_Sort.html',
                controller: 'ChildMusic_SortCtrl'
            })
            .state('eduInformation', {
                url: '/eduInformation',
                templateUrl: 'views/eduInformation.html',
                controller: 'EduInformationCtrl'
            })
            .state('videoPlay', {
                url: '/videoPlay?themeid&resourceid',
                templateUrl: 'views/videoPlay.html',
                controller: 'VideoPlayCtrl'
            })
            .state('audioPlay', {
                url: '/audioPlay?themeid&resourceid',
                templateUrl: 'views/audioPlay.html',
                controller: 'AudioPlayCtrl'
            })
            .state('eduInformation_Detail', {
                url: '/eduInformation_Detail?resourceid',
                templateUrl: 'views/eduInformation_Detail.html',
                controller: 'EduInformation_DetailCtrl'
            });

            $urlRouterProvider.otherwise('paradiseHome');
    });
