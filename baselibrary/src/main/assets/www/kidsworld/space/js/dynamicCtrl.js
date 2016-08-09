/*
 * Created by yanghy on 2016-7-11.
 *
 * */
//var url = "http://192.168.1.108:8080/KidsWorld";
var url = "http://zhu.hunme.net:8080/KidsWorld";



//页面参数
var imgIndex = 2,   
    tsId = getQueryString("tsId"),
    groupId = getQueryString("groupId"),
    groupType = getQueryString("groupType"),
    firstTime = getQueryString("clickTime"),//第一次请求的时间
//  refreshTime = null,
    dynamicId = null,
    myName = getQueryString("myName");
    
/* var imgIndex = 2,   
    urlNow = window.location.href,
    tsId = "81c5dc8725044e629cf524a3222cd818",
    groupId = "298f1648653840fdaa6c396830025af5",
    groupType = 1,
    firstTime = "2016-08-08 11:35:00",//第一次请求的时间
    dynamicId = null,
    myName = "周龙龙";*/

    

	
	//点击事件
	$('.mui-scroll').on('tap', '#more', function(event) {

	var $this = $(this);
	var drop = $this.next().data('show');

	if(drop == 0) {
		$this.next().data('show', 1).show();
	} else {
		$this.next().data('show', 0).hide();
	}
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

//		$this.parent().siblings('div#names').css('display', 'none');
		$this.parent().data('islike', 1);
		$this.parent().data('likecnt', likecnt + 1);
		$this.children('span#thumb').text('取消');
//		$this.parent().siblings('div#names').css('display', 'block');
		$this.parent().siblings('div#names').append('<span class="name" value=' + myName + '>' + myName + '</span>');
		$this.parent().siblings('div#names').show();
		$this.data('show', 0).hide();
		
		//点赞	
        $.post(url + '/dynamic/subPraise.do', dataz, 
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
        $.post(url + '/dynamic/subPraise.do', dataq, 
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
        $.post(url + '/dynamic/subPraise.do', dataz, 
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
//	refreshTime = new Date();
	setTimeout(function() {
        
//		 mui.toast('下拉刷新');//测试
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

         
        $.post(url + '/dynamic/getDynamic.do', data, 
        function(response){ 

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
//     pageNewIndex++;

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
        
         $.post(url + '/dynamic/getDynamic.do', data, 
        function(response){ 
        	if(response.code == "0" && response.data != null){
        	var resultData = response.data;
        	if(resultData.length != pageSize){
        		pageflag = false;
        	}
        	
        	dynamicId = resultData[0].dynamicId;
//      	alert(dynamicId);
//      	pageCount = response.pageCount;//初次请求，返回总页数
        	
        	for(var i = 0; i < resultData.length; i++) {
				var li = document.createElement('div');
				li.className = 'app-mainBox';
				li.innerHTML = generateHtml(resultData[i]);
			
				table.appendChild(li);
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
	    	var imgHtml = "";
	    	var imgArr = arr.imgUrl;

            if(imgArr.length == 1){
            	imgHtml += '<li><img src="' + imgArr[1] + '" class="oneImg" data-preview-src="' + imgArr[1].replace(/\/s/,"") + '" data-preview-group="' + imgIndex + '"/></li>';
            }else {
		    	for(var j = 0; j < imgArr.length; j++){
		    		imgHtml += '<li><img src="' + imgArr[j] + '" data-preview-src="' + imgArr[j].replace(/\/s/,"") + '" data-preview-group="' + imgIndex + '"/></li>';
		    	}
	    	}

	        tmpHtml += '<ul class="image2">' + imgHtml + '</ul>';
	    	imgIndex++;		
	    }


    tmpHtml += '</div>';
    tmpHtml += '<div class="app-control" data-islike=' + arr.isAgree + ' data-likecnt=' + arr.list.length;
    tmpHtml += ' data-dynamicid=' + arr.dynamicId + '>';
	tmpHtml += '<span class="app-time">' + (new Date(arr.createTime)).Format("yyyy-MM-dd") + '</span>';
	tmpHtml += '<img src="../images/more.png" class="mui-pull-right" id="more" style="width:20px;"/>';
	tmpHtml += '<a  class="item dy_like_btn_v3 popup" id="popup" href="javascript:;" data-show="0" style="display: none;">';
	tmpHtml += '<img src="../images/heart.png"/><span id="thumb">' +((arr.isAgree == 2)?"赞":"取消")+'</span></a></div>';
	
	if(arr.list.length != 0){
		 names = arr.list;
		tmpHtml += '<div class="names" id="names"><span class="top"></span><img src="../images/lightHeart.png" width="20px" height="18px">';
		
		for(var n = 0; n < names.length; n++){
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
        return decodeURI(r[2]);
    }
    return null;
}
//调用getQueryString
//alert(getQueryString("text"));

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

