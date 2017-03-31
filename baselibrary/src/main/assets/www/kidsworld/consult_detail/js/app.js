'use strict';
angular.module('app', ['ui.router','ngAnimate','ionic'])

//	.constant("BASE_URL", "http://192.168.1.200:8080/KidsWorld")
//  .constant("BASE_URL", "http://zhu.hunme.net:8082/KidsWorld") //测试服
//  .constant("BASE_URL", "http://eduslb.openhunme.com/KidsWorld")
    .constant("BASE_URL", " http://www.hongzhang.net.cn/KidsWorld") //正式服
//  .constant("BASE_URL","http://zhu.hunme.net:12345/KidsWorld")		

    .run(function($rootScope) {
        $rootScope.$on('$stateChangeStart', function (event, next) {
        });
        function onDeviceReady() {
        }
        document.addEventListener('deviceready', onDeviceReady, false);
    })
    //过滤器
    .filter('numberTo4', function() {
	    return function(input) {
	        if(!input){
	            return 0;
	        }else{
	            if (input>=1000) {
	                var newNum=(input/10000).toFixed(1);
	                return newNum+'万';
	            }else{
	                return input;
	            }
	        }
	    }
	})
    .filter('timeToMinute', function() {
        return function(inputs) {
            if(inputs){
                return inputs.substring(5,16);
            }else{
                return "";
            }
        }
    })

	.factory('CommonService', function() {
		return {
			// 2秒自动消失提示框
			showAlert: {
				cfg: {
					id: ".kBubble",
					speed: 2000
				},
				show: function (str) {
					var oBubble = $(this.cfg.id);
					if (!oBubble.length) {

						oBubble = $('<div class="kBubble" />').appendTo($("body"));
						oBubble.html(str).fadeIn();
					}
					setTimeout("$('.kBubble').fadeOut('slow',function(){$('.kBubble').remove();})", this.cfg.speed);
				}
			}
		}
	})
    //通用后台数据交换服务，包含get和post方法
    .factory('AjaxService', function($http, $httpParamSerializerJQLike, $q, $ionicLoading, CommonService,BASE_URL,$rootScope) {
	    return {
	        ajax: function(options) {
	            var url = options.url;
	            if(options.useBaseUrl){
	                url = BASE_URL + options.url;
	            }
	            var httpOption = {method: options.type || "post", url: url};
	            if (httpOption.method == "post") {
	                httpOption.data = options.data || {};
	            } else {
	                var pString = $httpParamSerializerJQLike(options.data || {});
	                httpOption.url += pString ? "?" + pString : "";
	            }
	            var deferred = $q.defer();

	            if(httpOption.method == "post"){    //post方法用jQuery的post方法
	                $.ajax({
	                    type: 'post',
	                    url : httpOption.url,
	                    data: httpOption.data,
	                    dataType: 'json',
	                    success: function(data){
	                        if(data.code==-9){
	                            CommonService.showAlert.show("系统繁忙");
	                            return;
	                        }
	                        deferred.resolve({
	                            data : data
	                        });
	                    },
	                    error: function(data){
	                        CommonService.showAlert.show("网络异常");
	                        $rootScope.$broadcast('ajaxError');
	                        deferred.reject(data);
	                    }
	                });

	            }else{  //get方法用angularjs的get方法
	                $http(httpOption).success(
	                    function(data){
	                        if(data.code==-9){
	                            CommonService.showAlert.show("系统繁忙");
	                            return;
	                        }
	                        deferred.resolve({
	                            data : data
	                        });
	                    }
	                ).error(
	                    function(data){
	                        CommonService.showAlert.show("网络异常");
	                        $rootScope.$broadcast('ajaxError');
	                        deferred.reject(data);
	                    }
	                );
	            }

	            return {
	                $promise: deferred.promise
	            };
	        }
	    };
	})
    //接口
    .factory('WebService', function(AjaxService) {
	    return {
	        //获取合集
	        getContentInfo:function (tsId,themeid) {
	            var options={};
	            options.type = "post";
	            options.useBaseUrl = true;
	            options.url = "/rakuen/getContentInfo.do";
	            options.data={
	                tsId:tsId,
	                themeid:themeid
	            };
	            return AjaxService.ajax(options);
	        },

	        //点赞资源（取消点赞）
	        subPraise:function (tsId,resourceid,cancel) {    //cancel 1=点赞 2=取消
	            var options={};
	            options.type = "post";
	            options.useBaseUrl = true;
	            options.url = "/rakuen/subPraise.do";
	            options.data={
	                tsId:tsId,
	                resourceid:resourceid,
	                cancel:cancel
	            };
	            return AjaxService.ajax(options);
	        },

	        //获取资源详细信息
	        getResourceDetail:function (tsId,resourceid) {
	            var options={};
	            options.type = "post";
	            options.useBaseUrl = true;
	            options.url = "/rakuen/getDetails.do";
	            options.data={
	                tsId:tsId,
	                resourceId:resourceid,
	                account_id:accountIdLogin
	            };
	            return AjaxService.ajax(options);
	        },

	        //用户评论视频、资讯
	        subComment:function (resourceid,tsId,content) {
	            var options={};
	            options.type = "post";
	            options.useBaseUrl = true;
	            options.url = "/rakuen/subComment";
	            options.data={
	                resourceid:resourceid,
	                tsId:tsId,
	                content:content
	            };
	            return AjaxService.ajax(options);
	        },
	        //查询评论列表
	        getCommentList:function (resourceid,pageSize,pageNumber) {
	            var options={};
	            options.type = "post";
	            options.useBaseUrl = true;
	            options.url = "/rakuen/getCommentList.do";
	            options.data={
	                resourceid:resourceid,
	                pageSize:pageSize,
	                pageNumber:pageNumber
	            };
	            return AjaxService.ajax(options);
	        },

	       //新--查询评论列表
	         getTsInfo:function (tsId) {
	         var options={};
	         options.type = "post";
	         options.useBaseUrl = true;
	         options.url = "/message/getTS.do";
	         options.data={
	         tsId:tsId
	         };
	         return AjaxService.ajax(options);
	         },
	        //新-获取首页轮播图
	        newGetBanner:function (tsId) {
	            var options={};
	            options.type = "post";
	            options.useBaseUrl = true;
	            options.url = "/rakuen/newGetBanner.do";
	            options.data={
	                tsId:tsId
	            };
	            return AjaxService.ajax(options);
	        },
	        //新-猜你喜欢
	        guessYouLike:function (tsId,type,pageNumber,pageSize) {
	            var options={};
	            options.type = "post";
	            options.useBaseUrl = true;
	            //options.url = "/rakuen/guessYouLike.do";
	            options.url = "/rakuen/getRecommendAblum.do";
	            options.data={
	                tsId:tsId,
	                type:type,
	                pageNumber:pageNumber,
	                pageSize:pageSize
	            };
	            return AjaxService.ajax(options);
	        },
	        //新-获取资讯        type:1猜你喜欢，2熱搜,3最新上架,4根据theme获取资讯
	        getInformation:function (tsId,type,pageNumber,pageSize,themeId) {
	            var options={};
	            options.type = "post";
	            options.useBaseUrl = true;
	            options.url = "/rakuen/getInformation.do";
	            options.data={
	                tsId:tsId,
	                type:type,
	                pageNumber:pageNumber,
	                pageSize:pageSize,
	                themeId:themeId,
	                account_id:accountIdLogin
	            };
	            return AjaxService.ajax(options);
	        },
	        //新-提交资源开始播放记录
	        savePlayTheRecordingBegin:function (tsId,resourceid) {
	            var options={};
	            options.type = "post";
	            options.useBaseUrl = true;
	            options.url = "/rakuen/savePlayTheRecording.do";
	            options.data={
	                tsId:tsId,
	                resourceid:resourceid,
	                type:1
	            };
	            return AjaxService.ajax(options);
	        },
	        //新-提交资源播放记录进度
	        savePlayTheRecordingProgress:function (tsId,resourceid,broadcastPace) {
	            var options={};
	            options.type = "post";
	            options.useBaseUrl = true;
	            options.url = "/rakuen/savePlayTheRecording.do";
	            options.data={
	                tsId:tsId,
	                resourceid:resourceid,
	                type:2,
	                broadcastPace:broadcastPace
	            };
	            return AjaxService.ajax(options);
	        },
	        //新-获取该分类下子分类
	        getThemeList:function(tsId,type) {
	            var options={};
	            options.type = "post";
	            options.useBaseUrl = true;
	            options.url = "/rakuen/getThemeList.do";
	            options.data={
	                tsId:tsId,
	                type:type,
	                pageNumber:1,
	                pageSize:100
	            };
	            return AjaxService.ajax(options);
	        },
	        //新-根据主题ID获取对应专辑
	        getAlbumByTheme:function (tsId,themeId,pageNumber,pageSize) {
	            var options={};
	            options.type = "post";
	            options.useBaseUrl = true;
	            options.url = "/rakuen/getAlbumByTheme.do";
	            options.data={
	                tsId:tsId,
	                themeId:themeId,
	                pageNumber:pageNumber,
	                pageSize:pageSize
	            };
	            return AjaxService.ajax(options);
	        },
	        //新-资源搜索
	        resourceSearch:function (tsId,tag,type,pageSize,pageNumber) {
	            var options={};
	            options.type = "post";
	            options.useBaseUrl = true;
	            options.url = "/rakuen/resourceSearch.do";
	            options.data={
	                tsId:tsId,
	                tag:tag,
	                type:type,
	                pageSize:pageSize,
	                pageNumber:pageNumber,
	                account_id:accountIdLogin
	            };
	            return AjaxService.ajax(options);
	        },
	        //新--收藏资源（按专辑id）
	        saveResourceFavorites:function (tsId,albumId,type) {
	            var options={};
	            options.type = "post";
	            options.useBaseUrl = true;
	            options.url = "/rakuen/saveResourceFavorites.do";
	            options.data={
	                tsId:tsId,
	                albumId:albumId,
	                type:type       //1.收藏 2.取消收藏
	            };
	            return AjaxService.ajax(options);
	        },
	        //新--根据专辑ID获取资源（按专辑id）
	        getCompilationsAllResource:function(tsId,albumId) {
	            var options={};
	            options.type = "post";
	            options.useBaseUrl = true;
	            options.url = "/rakuen/getCompilationsAllResource.do";
	            options.data={
	                tsId:tsId,
	                albumId:albumId,
	                pageSize:1000,
	                pageNumber:1,
	                account_id:accountIdLogin
	            };
	            return AjaxService.ajax(options);
	        }
	    };
	})


	.controller('EduInformationCtrl', function($scope, $state, $stateParams, $ionicHistory,$ionicScrollDelegate,CommonService,WebService) {
		$scope.resourceid="f197c43f1f164afcb1f1789b2e677ed6";
		//视图view进入前操作
		$scope.$on('$ionicView.beforeEnter', function() {
			//与原生端顶部导航交互，改变导航栏内容
			if (platFlag == 1) {
				window.location.href = 'tooc://changenavigationtitle:?null&null*null';
				//友盟统计--页面进入
				MobclickAgent.onPageBegin('index');
			} else if (platFlag == 0) {
				change_tb.setToolBar("consultDetail");
				change_tb.sendBroadcast(false);
				//友盟统计--页面进入
				MobclickAgent.onPageBegin('index');
			}
			//获取状态参数，资源id
			$scope.resourceid=getUrlParam('resourceid');
			//初始化
			$scope.init();
		});

		//view离开操作
		$scope.$on('$ionicView.beforeLeave', function() {
			//友盟统计--退出页面
			if(platFlag==1){
				MobclickAgent.onPageEnd('index');
			}else if(platFlag==0){
				MobclickAgent.onPageEnd('index');
			}

			//页面退出时，向服务端提交浏览记录进度信息
			var resPlayHisProgress = WebService.savePlayTheRecordingProgress( tsIdLogin,$scope.resourceid,10);
			resPlayHisProgress.$promise.then(function(response) {
				var data=response.data;
			});
		});
		//初始化方法
		$scope.init= function () {
			//根据资源id，获得本资源的内容
			var res = WebService.getResourceDetail( tsIdLogin,$scope.resourceid );
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

			//获取用户信息，显示在评论人处
			$scope.currentUser={ };
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
			$scope.getTsInfo();

			//查询资源评论列表
			$scope.items_comment=[];
			$scope.pageNumber=1;
			$scope.loadState=0;
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
				});
			};
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

		//分页加载更多评论
		$scope.loadMoreData=function(){
			$scope.pageNumber=$scope.pageNumber+1;
			$scope.loadDataByPageNumber($scope.pageNumber);
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
