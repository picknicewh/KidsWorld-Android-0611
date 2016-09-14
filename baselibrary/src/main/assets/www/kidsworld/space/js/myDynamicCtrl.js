/*
 * Created by yanghy on 2016-7-11.
 *
 * */
//var url = "http://192.168.1.200:8080/KidsWorld";
var url = "http://zhu.hunme.net:8080/KidsWorld";
//var url = "http://eduslb.openhunme.com/KidsWorld";



var imgIndex = 2,
    pageflag = true,
    pageIndex = 1,
    //tsId = "34f7752ddb384677b05a185e2cedebd7",
    //tsId = "",
    tsId = getQueryString("tsId"),
    pageSize = 15;


//判断平台
var platFlag=testPlat();
function testPlat(){
	var u = navigator.userAgent;
	if(u.indexOf('Android') > -1 || u.indexOf('Adr') > -1){ //android终端
		return 0;
	}else if(!!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/)){ //ios终端
		return 1;
	}else{
		return 2;
	}
};

mui.init({
	pullRefresh: {
		container: '#pullrefresh',
		up: {
			contentrefresh: '正在加载...',
			callback: pullupRefresh
		}
	}
});

//图片预览
mui.previewImage();

var count = 0;

function generateHtml(arr) {
	var tmpHtml = "",
		counts = arr.list.length,
	    createTime = new Date(arr.createTime);
	tmpHtml += '<div class="section-left">';
    tmpHtml += '<span class="time" id="time">' + createTime.getDate() + '</span>';
    tmpHtml += '<span class="time2">' + getMonthZ(createTime.getMonth()) + '</span></div>';
	tmpHtml += '<div class="section-right"><div class="app-content content-top">';
	tmpHtml += '<div class="text">' + arr.text +'</div>';
	//如果有图片
	     if(arr.dynamicType ==1 && arr.imgUrl.length != 0){
	    	var imgHtml = "",
	    	     imgArr = arr.imgUrl,
	    	     imgLen = imgArr.length;

            if(imgLen == 1){
            	imgHtml += '<li><img src="' + imgArr[0] + '" class="oneImg" data-preview-src="' + imgArr[0].replace(/\/s/,"") + '" data-preview-group="' + imgIndex + '"/></li>';
            }else {
		    	for(var j = 0; j < imgLen; j++){
		    		imgHtml += '<li><img src="' + imgArr[j] + '" data-preview-src="' + imgArr[j].replace(/\/s/,"") + '" data-preview-group="' + imgIndex + '"/></li>';
		    	}
	    	}

	        tmpHtml += '<ul class="image2">' + imgHtml + '</ul>';
	    	imgIndex++;		
	    }
	
	tmpHtml += '</div><div class="app-control" data-islike=' + arr.isAgree + ' data-likecnt=' + counts;
	tmpHtml += ' data-dynamicid=' + arr.dynamicId + '>';

	tmpHtml += '<span class="app-time title-green" id="del">删除</span>';

	//评论
	tmpHtml += ' <span class="mui-pull-right comment"><img src="../images/comment.jpg" style="width:16px;">';
	if(arr.rewCount != 0){
		//tmpHtml += ' <span class="mui-pull-right comment"><img src="../images/comment.jpg" style="width:16px;">';
		tmpHtml += '<span class="counts">'+ arr.rewCount +'</span></span>';
	}else{
		//tmpHtml += ' <span class="mui-pull-right comment"><img src="../images/comment.jpg" style="width:16px;"><span class="counts">评论</span></span>';
		tmpHtml += '<span class="counts">评论</span></span>';
	}

	//点赞
	tmpHtml += '<span id="like" class="mui-pull-right like">';
	if(arr.isAgree == 1){
		tmpHtml += '<img src="../images/heart-filled.jpg" style="width:16px;">';
		/*}else if(arr.isisAgree == 2){
		 tmpHtml += '<span id="like" class="mui-pull-right comment"><img src="../images/heart.png" style="width:16px;">*/
	}else{
		tmpHtml += '<img src="../images/heart.png" style="width:16px;">';
	}

	if(counts != 0){
		tmpHtml += '<span class="counts">'+ counts +'</span></span></div>';
	}else{
		tmpHtml += '<span class="counts">赞</span></span></div>';
	}
//	tmpHtml += '<div class="mui-pull-right names" style="display: inline-block;" hidden><img src="../images/heart.png"/><span class="padding-left">3</span></div>';
	tmpHtml += '</div>';

	return tmpHtml;
}

/**
 * 上拉加载具体业务实现
 */
function pullupRefresh() {
	setTimeout(function() {
		mui('#pullrefresh').pullRefresh().endPullupToRefresh((!pageflag));
		var table = document.body.querySelector('.wrap-list');
		var cells = document.body.querySelectorAll('.app-mainBox');
		//请求数据
		var data = {
        	"tsId": tsId,
         	"pageNumber": pageIndex,
        	"pageSize": pageSize               	
        };
        
		$.post(url + '/dynamics/myDynamic.do', data,
        function(response){
			if(response.code == "0"){
				if(response.data.length == 0) {

					if(pageIndex == 1) {
						mui.toast("暂无数据");
					}

				}else{

					var resultData = response.data;
					if(resultData.length != pageSize){
						pageflag = false;
					}
					for(var i = 0; i < resultData.length; i++) {
						var li = document.createElement('div');
						li.className = 'app-mainBox';
						li.innerHTML = generateHtml(resultData[i]);

						table.appendChild(li);
					}

			}
        	//if(response.code == "0" && response.data != null){
				/*if(response.data == null || response.data.length == 0 ){
                     mui.toast("暂无数据");
				}else{
					var resultData = response.data;
					if(resultData.length != pageSize){
						pageflag = false;
					}
					for(var i = 0; i < resultData.length; i++) {
						var li = document.createElement('div');
						li.className = 'app-mainBox';
						li.innerHTML = generateHtml(resultData[i]);

						table.appendChild(li);
					}

				}*/

        	}
        });
        pageIndex++;//页码加一
      
	}, 1500);
}

//删除消息及确认消息弹窗
//document.getElementById("del").addEventListener('tap', function() {
	$('.mui-scroll').on('tap','#del', function(event) {
	            var $this = $(this),
	                dynamicId = $this.parent().data('dynamicid'),
	                wrap = $this.parent().parent().parent(),
				    btnArray = ['取消', '删除'];
				mui.confirm('','确定删除吗？',btnArray, function(e) {
					if (e.index == 1) {
					
						//数据库操作
						var datas = {
							"tsId":tsId,
							"dynamicId":dynamicId
						}
						$.post(url + '/dynamics/deleteDynamic.do',datas,
						  function(response){
						  	 if(response.code == 0){
//						  	 	alert("删除成功");
						  	 	wrap.remove();
						  	 	//根据不同的设备，调用不同的原生方法，来重新加载页面。
						  	 	/*var u = navigator.userAgent;
					            if(u.indexOf('Android') > -1 || u.indexOf('Adr') > -1){ //android终端
							            change_tob.noticeChange();
							    }else if(!!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/)){ //ios终端
							            refreshTrend();
							    }*/
								 if(platFlag == 0){//android终端
									 change_tob.noticeChange();
								 }else if(platFlag == 1){//ios终端
									 //alert("IOS");
									 refreshTrend();
								 }else{
									 return;
								 }

						  	 	
						  	 }
						  });
								
					} else {
					}
				})
			});


//点击评论跳转到消息详情
$('.mui-scroll').on('tap','span.comment',function(event) {

	var $this = $(this);
	var dynamicIds = $this.parent().data('dynamicid');

	if(platFlag == 0){//android终端
		//alert("安卓");
		showDos.startStatusDetils(dynamicIds);

	}else if(platFlag == 1){//ios终端
        //alert("IOS");
		OCdynamicId.iosUsercomment(dynamicIds);
	}else{
		return;
	}
	//event.stopPropagation(); //阻止事件向上冒泡
});

//点赞
$('.mui-scroll').on('tap', '#like', function(event) {
	var $this = $(this);

	var likecnt = $this.parent().data('likecnt');
	var islike = $this.parent().data('islike');//2未点赞，1已点赞
	var dynamicIds = $this.parent().data('dynamicid');
	var dataz = {//点赞传参
		"tsId": tsId,
		"dynamicId":dynamicIds,
		"cancel":1
	};
	var dataq = {//取消赞传参
		"tsId": tsId,
		"dynamicId":dynamicIds,
		"cancel":2
	};

	if(islike == 1 && likecnt == 1) {
		//如果只有一个赞，且我点过赞，点击即取消赞
		//图标变为空心
		//点赞个数为0，隐藏
		//点赞姓名列表变化

		//$this.parent().siblings('div#names').children('span[value=' + myName + ']').remove();

		//$this.data('show', 0).hide();
		//$this.parent().siblings('div#names').hide();
		//$this.children('span#thumb').text('赞');
		$this.children('img').attr("src","../images/heart.png");
		$this.children('span.counts').text("赞");
		$this.parent().data('islike', 2);
		$this.parent().data('likecnt', 0);
		//取消赞
		$.post(url + '/dynamic/subPraise.do', dataq,
			function(response){
//      	if(response.data )
			});
	} else if(islike == 2 && likecnt == 0) {
		//如果点赞个数为0，且我位点赞，点击即点赞
		// 图标变为实心
		// 点赞个数为1，显示
		//点赞姓名列表变化

		$this.parent().data('islike', 1);
		$this.parent().data('likecnt', 1);
		//$this.children('span#thumb').text('取消');
		$this.children('img').attr("src","../images/heart-filled.jpg");
		$this.children('span.counts').text("1");
		//$this.parent().siblings('div#names').append('<span class="name" value=' + myName + '>' + myName + '</span>');
		//$this.parent().siblings('div#names').show();
		//$this.data('show', 0).hide();

		//点赞
		$.post(url + '/dynamics/subPraise.do', dataz,
			function(response){
//      	if(response.data )
			});
	} else if(islike == 1) {
		//一般情况，取消赞
		// 图标变为空心
		//点赞姓名列表变化

		//likecnt -= likecnt;
		//$this.parent().siblings('div#names').children('span[value=' + myName + ']').remove();
		//$this.data('show', 0).hide();
		//$this.children('span#thumb').text('赞');
		$this.children('img').attr("src","../images/heart.png");
		$this.children('span.counts').text(likecnt - 1);
		$this.parent().data('islike',2);
		$this.parent().data('likecnt', likecnt - 1);

		//取消赞
		$.post(url + '/dynamics/subPraise.do', dataq,
			function(response){
//      	if(response.data )
			});
	} else {
		//一般情况，点赞
		// 图标变为实心
		//点赞姓名列表变化
		//likecnt += likecnt;
		$this.parent().data('islike', 1);
		$this.parent().data('likecnt', likecnt + 1);
		//$this.children('span#thumb').text('取消');
		$this.children('img').attr("src","../images/heart-filled.jpg");
		$this.children('span.counts').text(likecnt + 1);
		//$this.parent().siblings('div#names').append('<span class="name" value=' + myName + '>' + myName + '</span>');
		//$this.data('show', 0).hide();
		//点赞
		$.post(url + '/dynamics/subPraise.do', dataz,
			function(response){
			});
	}
});

if(mui.os.plus) {
	mui.plusReady(function() {
		setTimeout(function() {
			mui('#pullrefresh').pullRefresh().pullupLoading();
		}, 1000);

	});
} else {
	mui.ready(function() {
		mui('#pullrefresh').pullRefresh().pullupLoading();
	});
}


//适配
 (function (doc, win) {
    var docEl = doc.documentElement || doc.body,
        resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
        recalc = function () {
            var clientWidth = docEl.clientWidth;
            if (!clientWidth) return;
            docEl.style.fontSize = 20 * (clientWidth / 320) + 'px';
            docEl.style.display = "block";
        };
    docEl.style.display = "none";
    if (!doc.addEventListener) return;
    win.addEventListener(resizeEvt, recalc, false);
    doc.addEventListener('DOMContentLoaded', recalc, false);
})(document, window);

//获取天，2016-07-29 13:33:04-----13 七月
/*var getDay = function (date){
	var day = ((date.split(" "))[0].split("-"))[2];
	
	return day;
}*/
//获取月份
var getMonthZ = function (month){
//	var month = ((date.split(" "))[0].split("-"))[1];
	var monthStr = "";
	switch (month + 1){
		case 1:
		    monthStr = "一月";
			break;
		case 2:
		    monthStr = "二月";
			break;
		case 3:
		    monthStr = "三月";
			break;
		case 4:
		    monthStr = "四月";
			break;
		case 5:
		    monthStr = "五月";
			break;
		case 6:
		    monthStr = "六月";
			break;
		case 7:
		    monthStr = "七月";
			break;
		case 8:
		    monthStr = "八月";
			break;
		case 9:
		    monthStr = "九月";
			break;
		case 10:
		    monthStr = "十月";
			break;
		case 11:
		    monthStr = "十一月";
			break;
		case 12:
		    monthStr = "十二月";
			break;
		default:
			break;
	}
	return monthStr;
}
//获取url参数
function getQueryString(name) {
    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
    var r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return encodeURIComponent(r[2]);
    }
    return null;
}

