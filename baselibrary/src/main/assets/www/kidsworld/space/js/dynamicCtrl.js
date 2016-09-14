/*
 * Created by yanghy on 2016-7-11.
 *
 * */
//var url = "http://192.168.1.108:8080/KidsWorld";
var url = "http://zhu.hunme.net:8080/KidsWorld";
//var url = "http://eduslb.openhunme.com/KidsWorld";


//页面参数
var imgIndex = 2,
    tsId = getQueryString("tsId"),
    groupId = getQueryString("groupId"),
    groupType = getQueryString("groupType"),
    firstTime = getQueryString("clickTime"),//第一次请求的时间
    dynamicId = null,
    myName = getQueryString("myName");

	 //正式服务器上的账号
    /*tsId = "fc6eb09ef20c453faa1be09859158796",
    groupId = "1dc191ae0cd54761937ed3a50ea9e9b7",
    groupType = 1,*/

	 //测试服务器上
//var tsId = "43a43a92c69a4330948bb2cd8b7a5501";
//var groupId = "";
//var groupType = 1;
//var firstTime = "2016-9-10 13:38:01";//第一次请求的时间
//var myName = "周龙龙";


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

	//点赞事件
	/*$('.mui-scroll').on('tap', '#more', function(event) {

	var $this = $(this);
	var drop = $this.next().data('show');

	if(drop == 0) {
		//其它的点赞弹窗消失
		$('[id=popup]').data('show', 0).hide();
		$this.next().data('show', 1).show();
	} else {
		$this.next().data('show', 0).hide();
	}
	
	//点击其它地方，让所有的#popup消失
	$(document).one("tap", function() { //对document绑定一个影藏Div方法
			$('[id=popup]').data('show', 0).hide();
		});
	 event.stopPropagation(); //阻止事件向上冒泡
	 
    });*/

     //评论跳转到消息详情
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
		event.stopPropagation(); //阻止事件向上冒泡
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

		$this.parent().siblings('div#names').children('span[value=' + myName + ']').remove();

		//$this.data('show', 0).hide();
		$this.parent().siblings('div#names').hide();
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
		$this.parent().siblings('div#names').append('<span class="name" value=' + myName + '>' + myName + '</span>');
		$this.parent().siblings('div#names').show();
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
        $this.parent().siblings('div#names').children('span[value=' + myName + ']').remove();
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
		$this.parent().siblings('div#names').append('<span class="name" value=' + myName + '>' + myName + '</span>');
		//$this.data('show', 0).hide();
		//点赞
        $.post(url + '/dynamics/subPraise.do', dataz,
        function(response){         	
       });
	}
});

        //图片预览
    mui.previewImage();
  
	

//点赞事件
var pageflag = true,
    pageIndex = 1,
    pageSize = 15;
    
  

mui.init({
	pullRefresh: {
		container: '#pullrefresh',
		down: {
			callback: pulldownRefresh
		},
		up: {
			contentrefresh: '正在加载...',
			callback: pullupRefresh
		}
	}
});
//日期格式转换

    Date.prototype.Format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
	}

// Date.Format("yyyy-MM-dd hh:mm:ss");//调用



 //加载新的数据

function loadNew() {
//function pulldownRefresh() {
	setTimeout(function() {

		 //mui.toast('下拉刷');//测试
		//选择DOM
		var table = document.body.querySelector('.mui-table-view');
		var cells = document.body.querySelectorAll('.app-mainBox');
		//请求数据
//		alert(firstTime);
		var data = {
			"tsId": tsId,
			"groupId": groupId,
			"groupType": groupType,
			"pageNumber": 1,
			"pageSize":100,
			"dynamicId": dynamicId,
			"type": 2
		};


		$.post(url + '/dynamics/getDynamic.do', data,
			function(response){

				//根据不同设备，调用不同的消除红点方法。
				/*var u = navigator.userAgent;
				if(u.indexOf('Android') > -1 || u.indexOf('Adr') > -1){ //android终端
					showDos.setStatus();
				}else if(!!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/)){ //ios终端
					tooc();
				}*/
                alert(response);
				if(platFlag == 0){//android终端
//					alert("安卓");
					showDos.setStatus();
				}else if(platFlag == 1){//ios终端
					//alert("IOS");
					tooc();
				}else{
					return;
				}
//                 alert(response);
				if(response.code == 0 && response.data.length > 0){
//      		firstTime = refreshTime.Format("yyyy-MM-dd hh:mm:ss");

					var resultData = response.data;

					dynamicId = resultData[0].dynamicId;
//      		alert(dynamicId);

					for(var i = resultData.length - 1; i > -1; i--) {
						var li = document.createElement('div');
						li.className = 'app-mainBox';
						li.innerHTML = generateHtml(resultData[i]);

						//下拉刷新，新纪录插到最前面
						table.insertBefore(li, table.firstChild);
					}
				}
			});
		//加载完数据后
		mui('#pullrefresh').pullRefresh().endPulldownToRefresh(); //refresh completed
	}, 1500);
}

/**
 * 下拉刷新具体业务实现
 */
function pulldownRefresh() {

	firstTime = (new Date()).Format("yyyy-MM-dd hh:mm:ss");
	//alert(refreshTime);
	pageIndex = 1;

	var u = navigator.userAgent;
	if(u.indexOf('Android') > -1 || u.indexOf('Adr') > -1){ //android终端
		showDos.setStatus();
	}else if(!!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/)){ //ios终端
		tooc();
	}

	setTimeout(function() {
		//选择DOM
		var table = document.body.querySelector('.mui-table-view');
		var cells = document.body.querySelectorAll('.app-mainBox');

		//请求数据
		var data = {
        	"tsId": tsId,
        	"groupId": groupId,
        	"groupType": groupType,
        	"pageNumber": pageIndex,
        	"pageSize":pageSize,
        	"createTime": firstTime,
        	"type": 1
       };

         
        $.post(url + '/dynamics/getDynamic.do', data,
        function(response){ 
        	//根据不同设备，调用不同的消除红点方法。

		        
        	if(response.code == 0 && response.data.length > 0){
				table.innerHTML = "";
        		var resultData = response.data,
				    dataLen = resultData.length;
        		dynamicId = resultData[0].dynamicId;
//      		alert(dynamicId);

        		//修改的部分
				for(var i = 0; i < dataLen; i++) {
					var li = document.createElement('div');
					li.className = 'app-mainBox';
					li.innerHTML = generateHtml(resultData[i]);

					table.appendChild(li);
				}

        	}
       });
        pageIndex = 2;

		//加载完数据后
		mui('#pullrefresh').pullRefresh().endPulldownToRefresh(); //refresh completed
	}, 1500);
}


/**
 * 上拉加载具体业务实现
 */

function pullupRefresh() {
	setTimeout(function() {
		mui('#pullrefresh').pullRefresh().endPullupToRefresh((!pageflag));
		var table = document.body.querySelector('.mui-table-view');
		var cells = document.body.querySelectorAll('.app-mainBox');
		//请求数据
//		alert(firstTime);
		var data = {
        	"tsId": tsId,
        	"groupId": groupId,
        	"groupType": groupType,
        	"pageNumber": pageIndex,
        	"pageSize": pageSize,
        	"createTime": firstTime,
        	"type": 1
        	
      };  

         $.post(url + '/dynamics/getDynamic.do', data,
        function(response){ 
        	if(response.code == "0"){
				if(response.data.length == 0) {

					if(pageIndex == 1) {
						mui.toast("暂无数据");
					}

				}else{

					var resultData = response.data,
						dataLen = resultData.length;
					//为加载新数据的参数做准备
					dynamicId = resultData[0].dynamicId;
					if(dataLen != pageSize){
						pageflag = false;
					}

					for(var i = 0; i < dataLen; i++) {
						var li = document.createElement('div');
						li.className = 'app-mainBox';

						li.innerHTML = generateHtml(resultData[i]);

						table.appendChild(li);

					}
					////时间转换
					//$('time.timeago').timeago();
				}

        	
        	}
        });
        pageIndex++;//页码加一

      
	}, 1500);
}


function generateHtml(arr) {//此处arr不是数组
	
	var tmpHtml = "",
		names = [],
	    counts = arr.list.length;
			  	
	tmpHtml += '<div class="app-leftIn"><div class="app-photo">';
	//角色判断
	if(arr.tsType == 1){
		tmpHtml += '<img src=' + arr.img + '><span class="mui-badge mui-badge-danger app-badge">学</span>';
	}else{
		tmpHtml += '<img src=' + arr.img + '><span class="mui-badge mui-badge-danger app-badge back-green">师</span>';
	}

	tmpHtml += '</div></div><div class="app-rigthIn"><div class="app-name">' + arr.tsName + '</div><div class="app-content">';
	tmpHtml += '<div class="text">' + arr.text + '</div>';
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


    tmpHtml += '</div>';
    tmpHtml += '<div class="app-control" data-islike=' + arr.isAgree + ' data-likecnt=' + counts;
    tmpHtml += ' data-dynamicid=' + arr.dynamicId + '><span class="app-time">';
	//时间
	//tmpHtml += '<span class="app-time">' + (new Date(arr.createTime)).Format("yyyy-MM-dd") + '</span>';
	tmpHtml += '<span class="app-time">' + arr.date + '</span></span>';

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

    /*tmpHtml += '</span><span class="mui-pull-right" id="more"><img src="../images/more.png"  style="width:20px;"/></span>';
	tmpHtml += '<a  class="item dy_like_btn_v3 popup" id="popup" href="javascript:;" data-show="0" style="display: none;">';
	tmpHtml += '<img src="../images/heart.png"/><span id="thumb">' +((arr.isAgree == 2)?"赞":"取消")+'</span></a></div>';*/
	
	if(arr.list.length != 0){
		 names = arr.list;
		tmpHtml += '<div class="names" id="names"><span class="top"></span><img src="../images/lightHeart.png" width="20px" height="18px">';
		
		for(var n = 0, namesLen = names.length; n < namesLen; n++){
			tmpHtml += '<span class="name" value=' + names[n] + '>' + names[n] + '</span>';
		}
	}else{
		tmpHtml += '<div class="names" id="names" hidden><span class="top"></span><img src="../images/lightHeart.png" width="20px" height="18px">';
	}
	tmpHtml += '</div></div></div>';

	return tmpHtml;
}

//获取url参数
function getQueryString(name) {
    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
    var r = window.location.search.substr(1).match(reg);
    if (r != null) {
		//return unescape(r[2]);
        //return decodeURI(r[2]);
		return decodeURIComponent(r[2]);

    }
    return null;
}
//调用getQueryString
//alert(getQueryString("text"));

//时间格式
function ISODateString(d) {
	function pad(n){
		return n<10 ? '0'+n : n
	}
	return d.getUTCFullYear()+'-'
		+ pad(d.getUTCMonth()+1)+'-'
		+ pad(d.getUTCDate())+'T'
		+ pad(d.getUTCHours())+':'
		+ pad(d.getUTCMinutes())+':'
		+ pad(d.getUTCSeconds())+'Z'
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

//设备准备好
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



