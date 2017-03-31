'use strict';
angular.module('app.controllers',['ionic','app'])
.controller('EduInformationCtrl', function($scope, $state, $stateParams, $ionicHistory,$ionicScrollDelegate,CommonService) {
    console.log(1)
    //获取状态参数，资源id
    $scope.resourceid="5dbf2d05968441f99d6331fb33a63ae8";
    //视图view进入前操作
    $scope.$on('$ionicView.beforeEnter', function() {
        //与原生端顶部导航交互，改变导航栏内容
        if (platFlag == 1) {
            window.location.href = 'tooc://changenavigationtitle:?null&null*null';
            //友盟统计--页面进入
            MobclickAgent.onPageBegin('EduInformation_Detail');
        } else if (platFlag == 0) {
            change_tb.setToolBar("consultDetail");
            change_tb.sendBroadcast(false);
            //友盟统计--页面进入
            MobclickAgent.onPageBegin('EduInformation_Detail');
        }
        //获取状态参数，资源id
        $scope.resourceid="5dbf2d05968441f99d6331fb33a63ae8";
        //初始化
        $scope.init();
    });

    //view离开操作
    $scope.$on('$ionicView.beforeLeave', function() {
        //友盟统计--退出页面
        if(platFlag==1){
            MobclickAgent.onPageEnd('EduInformation_Detail');
        }else if(platFlag==0){
            MobclickAgent.onPageEnd('EduInformation_Detail');
        }

        //页面退出时，向服务端提交浏览记录进度信息
        var resPlayHisProgress = WebService.savePlayTheRecordingProgress( tsIdLogin,$scope.resourceid,10);
        resPlayHisProgress.$promise.then(function(response) {
            var data=response.data;
        });
    });
    //初始化方法
    $scope.init= function (WebService) {
        $scope.resourceid="5dbf2d05968441f99d6331fb33a63ae8";
        //根据资源id，获得本资源的内容
        var res = WebService.getResourceDetail( tsIdLogin,$scope.resourceid );
        console.log(2)
        res.$promise.then(function(response) {
            var data=response.data.data;
            if(!data) {
                CommonService.showAlert.show('无资讯数据！');
                return;
            }

            $scope.displayInfName=data.resourceName;        //资讯标题
            $scope.displayInfCnt=data.content;      //资讯内容
            $scope.displaySource=data.author;       //资源来源
            $scope.displayCreatetime=data.createTime;       //资源创建时间

            //1已收藏 2未收藏
            if(data.isFavorites==1){
                $scope.storeActive=true;
            }else{
                $scope.storeActive=false;
            }

            //添加播放记录到服务器
            var resPlayHis = WebService.savePlayTheRecordingBegin( tsIdLogin,$scope.resourceid );
            resPlayHis.$promise.then(function(response) {
                var data=response.data;
                console.log(data.data);
            });
        });

        //调服务端统计次数的接口--弃用接口
        //var resPv = WebService.updateResourcePv( $scope.resourceid );
        //resPv.$promise.then(function(response) {
        //    var data=response.data;
        //});

        //获取用户信息，显示在评论人处
        $scope.currentUser={ };
        $scope.getTsInfo();

        //查询资源评论列表
        $scope.items_comment=[];
        $scope.pageNumber=1;
        $scope.loadState=0;
        $scope.loadDataByPageNumber($scope.pageNumber);

    };

    //me
    $scope.init();

    //返回按钮
    $scope.goBack=function(){
        $ionicHistory.goBack();
    };

    //收藏按钮
    $scope.store=function(){
        //1收藏 2取消收藏
        if($scope.storeActive){
            var cancel=2;
        }else{
            var cancel=1;
        }
        //提交收藏请求到服务端
        var res = WebService.saveResourceFavorites(tsIdLogin,$scope.resourceid,cancel);
        res.$promise.then(function (response) {
            var data = response.data;
            if (data.code==0) {
                if($scope.storeActive){
                    $scope.storeActive=false;
                    data.isFavorites=2;
                    CommonService.showAlert.show('取消收藏'+data.data);
                }else{
                    $scope.storeActive=true;
                    data.isFavorites=1;
                    CommonService.showAlert.show('收藏'+data.data);
                }
            }else{
                CommonService.showAlert.show(data.data);
            }
        });
    };

    //用户评论视频、资讯
    $scope.subComment=function(commentWord){
        var res = WebService.subComment($scope.resourceid,tsIdLogin,commentWord);
        res.$promise.then(function (response) {
            var data = response.data;
            if (data.code==0) {
                CommonService.showAlert.show(data.data);
                //提交评论成功后，本地并将评论添加到第一条
                var tempComment={
                    "tsImgurl":$scope.currentUser.img,
                    "tsType":$scope.currentUser.tsType,
                    "tsName":$scope.currentUser.ts_name,
                    "date":"刚刚",
                    "content":commentWord
                };
                $scope.items_comment.unshift(tempComment);
                $scope.commentWord=null;
                $(".inputBox input").val("");
            }else{
                CommonService.showAlert.show(data.data);
            }
        });
    };

    //从服务端获取用户信息，显示在评论人处
    $scope.getTsInfo=function(){
        var res = WebService.getTsInfo(tsIdLogin);
        res.$promise.then(function (response) {
            var data = response.data;
            if (data.code==0) {
                $scope.currentUser=data.data;
            }else{
                CommonService.showAlert.show(data.data);
            }
        });
    };

    //分页加载更多评论
    $scope.loadMoreData=function(){
        $scope.pageNumber=$scope.pageNumber+1;
        $scope.loadDataByPageNumber($scope.pageNumber);
    };
    //根据页面查询评论内容
    $scope.loadDataByPageNumber=function(pageNumber){
        var res = WebService.getCommentList( $scope.resourceid,20,pageNumber);
        $scope.loadState=1;
        res.$promise.then(function(response) {
            var data=response.data.data;
            var length=data.length;
            $scope.loadState=0;
            if(length<20) {
                $scope.loadState=2;
                if(length<=0){
                    return;
                }
            }
            $scope.items_comment=$scope.items_comment.concat(data);
            $ionicScrollDelegate.resize();
//          $scope.$broadcast('scroll.infiniteScrollComplete');
        });
    };

    //安卓端虚拟键盘弹出后，盖住输入框，所以让其向上滚动
    $scope.scrollToUp=function(){
        $scope.positionOrigin = $('.InfDetContent.eduInf>.scroll').css('transform');
        var hight=$('.InfDetContent.eduInf .InfDetContent').height()+160;
        var toUp = 'translate3d( 0px,-'+hight+'px,0 ) scale( 1 )';
        $('.InfDetContent.eduInf>.scroll').css('transform',toUp);
    };
    $scope.scrollBack=function(){
        $('.InfDetContent.eduInf>.scroll').css('transform',$scope.positionOrigin);
    };

});
