/*
 * Created by yanghy on 2016-7-11.
 *
 * */
//var url = "http://192.168.1.108:8080/KidsWorld";
//var url = "http://zhu.hunme.net:8080/KidsWorld";
var url = "http://eduslb.openhunme.com/KidsWorld";


//页面参数
var imgIndex = 2,
    tsId = getQueryString("tsId"),
    groupId = getQueryString("groupId"),
    groupType = getQueryString("groupType"),
    firstTime = getQueryString("clickTime"),//第一次请求的时间
    dynamicId = null,
    myName = getQueryString("myName");
    
/* var imgIndex = 2,
    tsId = "afa41d59d3f4400ca1558d43b6d29991",
    groupId = "eed2ce7de25b44f2a550d96b1f2b5295",
    groupType = 1,
    firstTime = "2016-08-17 16:35:00",//第一次请求的时间
    dynamicId = null,
    myName = "周龙龙";*/
   		
	//点赞事件
	$('.mui-scroll').on('tap', '#more', function(event) {

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
	 
    });
     
      
    
	$('.mui-scroll').on('tap', '#popup', function(event) {
	var $this = $(this);

	var likecnt = $this.parent().data('likecnt');
	var islike = $this.parent().data('islike');
	var dynamicId = $this.parent().data('dynamicid');
	var dataz = {//点赞传参
        	"tsId": tsId,
        	"dynamicId":dynamicId,
        	"cancel":1
       };
    var dataq = {//取消赞传参
	        "tsId": tsId,
        	"dynamicId":dynamicId,
        	"cancel":2
        };

	if(islike == 1 && likecnt == 1) {
		$this.parent().siblings('div#names').children('span[value=' + myName + ']').remove();

		$this.data('show', 0).hide();
		$this.parent().siblings('div#names').hide();
		$this.children('span#thumb').text('赞');
		$this.parent().data('islike', 2);
		$this.parent().data('likecnt', likecnt - 1);
		//取消赞
        $.post(url + '/dynamic/subPraise.do', dataq, 
        function(response){ 
//      	if(response.data )        	
       });
	} else if(islike == 2 && likecnt == 0) {


		$this.parent().data('islike', 1);
		$this.parent().data('likecnt', likecnt + 1);
		$this.children('span#thumb').text('取消');
		$this.parent().siblings('div#names').append('<span class="name" value=' + myName + '>' + myName + '</span>');
		$this.parent().siblings('div#names').show();
		$this.data('show', 0).hide();
		
		//点赞	
        $.post(url + '/dynamics/subPraise.do', dataz,
        function(response){ 
//      	if(response.data )        	
       });
	} else if(islike == 1) {
        $this.parent().siblings('div#names').children('span[value=' + myName + ']').remove();
		$this.data('show', 0).hide();
		$this.children('span#thumb').text('赞');
		$this.parent().data('islike',2);
		$this.parent().data('likecnt', likecnt - 1);
		
		//取消赞
        $.post(url + '/dynamics/subPraise.do', dataq,
        function(response){ 
//      	if(response.data )        	
       });
	} else {	
		$this.parent().data('islike', 1);
		$this.parent().data('likecnt', likecnt + 1);
		$this.children('span#thumb').text('取消');
		$this.parent().siblings('div#names').append('<span class="name" value=' + myName + '>' + myName + '</span>');
		$this.data('show', 0).hide();
		//点赞
        $.post(url + '/dynamics/subPraise.do', dataz,
        function(response){         	
       });
	}
});

        //图片预览
    mui.previewImage();
  
	

//点赞事件
var imgindex = 2,
    pageflag = true,
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
//		 mui.toast('下拉刷新');//测试
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
				if(response.data.length == 0 && pageIndex == 1){
					mui.toast("暂无数据");
				}else{
					var resultData = response.data,
						dataLen = resultData.length;
					if(dataLen != pageSize){
						pageflag = false;
					}

					dynamicId = resultData[0].dynamicId;
//      	alert(dynamicId);
//      	pageCount = response.pageCount;//初次请求，返回总页数

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
	
	var tmpHtml = "",names = [];
			  	
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
    tmpHtml += '<div class="app-control" data-islike=' + arr.isAgree + ' data-likecnt=' + arr.list.length;
    tmpHtml += ' data-dynamicid=' + arr.dynamicId + '><span class="app-time">';
	//时间
	//tmpHtml += '<span class="app-time">' + (new Date(arr.createTime)).Format("yyyy-MM-dd") + '</span>';
	tmpHtml += '<span class="app-time">' + arr.date + '</span>';
    tmpHtml += '</span><span class="mui-pull-right" id="more"><img src="../images/more.png"  style="width:20px;"/></span>';
	tmpHtml += '<a  class="item dy_like_btn_v3 popup" id="popup" href="javascript:;" data-show="0" style="display: none;">';
	tmpHtml += '<img src="../images/heart.png"/><span id="thumb">' +((arr.isAgree == 2)?"赞":"取消")+'</span></a></div>';
	
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



