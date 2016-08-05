/*
 * Created by yanghy on 2016-7-11.
 *
 * */
var url = "http://zhu.hunme.net:8080/KidsWorld";
//图片预览
mui.previewImage();	

var imgIndex = 2,
    pageflag = true,
    pageIndex = 1,
    tsId = getQueryString("tsId"),
    pageSize = 15;

mui.init({
	pullRefresh: {
		container: '#pullrefresh',
		up: {
			contentrefresh: '正在加载...',
			callback: pullupRefresh
		}
	}
});

//删除消息
$('.mui-scroll').on('tap','#del', function(event) {
	var $this = $(this);
	var dynamicId = $this.data('id');
	var wrap = $this.parent().parent().parent();
	//alert 弹窗确认
	
	//数据库操作
	var datas = {
		"tsId": tsId,
		"dynamicId":dynamicId
	}
	$.post(url + '/dynamic/deleteDynamic.do',datas,
	  function(response){
	  	 if(response.code == 0){
//	  	 	alert("删除成功");
	  	 }
	  });
	wrap.remove();	
});


var count = 0;

function generateHtml(arr) {
	var tmpHtml = "";
	tmpHtml += '<div class="section-left">';
    tmpHtml += '<span class="time" id="time">' + getDay(arr.createTime) + '</span>';
    tmpHtml += '<span class="time2">' + getMonth(arr.createTime) + '</span></div>';
	tmpHtml += '<div class="section-right"><div class="app-content content-top">';
	tmpHtml += '<div class="text">' + arr.text +'</div>';
	//如果有图片

	    if(arr.imgUrl !== null && arr.imgUrl.length != 0){
	    	var imgHtml = "";
	    	var imgArr = arr.imgUrl;
	    	for(var j = 0; j < imgArr.length; j++){
	    		imgHtml += '<li><img src="' + imgArr[j] + '" data-preview-src="' + imgArr[j].replace(/\/s/,"") + '" data-preview-group="' + imgIndex + '"/></li>';
	    	}

	        tmpHtml += '<ul class="image2">' + imgHtml + '</ul>';
	    	imgIndex++;		
	    }
	
	tmpHtml += '</div><div class="app-control">';
	tmpHtml += '<span class="app-time title-green" id="del" data-id=' + arr.dynamicId + '>删除</span>';
	if(arr.list.length > 0){
		tmpHtml += '<div class="mui-pull-right heart-gray" style="display: inline-block;"><img src="../images/heart.png"/><span class="padding-left label-font">' + arr.list.length + '</span></div>';
	}
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
        
         $.post(url + '/dynamic/myDynamic.do', data, 
        function(response){ 
        	if(response.code == "0" && response.data != null){
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
        });
        pageIndex++;//页码加一
      
	}, 1500);
}

//确认消息弹窗
	$('.mui-scroll').on('tap','#del', function(event) {
	      
				var btnArray = ['取消', '删除'];
				mui.confirm('','确定删除吗？',btnArray, function(e) {
					if (e.index == 1) {
						var $this = $(this);
						var dynamicId = $this.data('id');
						var wrap = $this.parent().parent().parent();
						//数据库操作
						var datas = {
							"tsId":"123456",
							"dynamicId":dynamicId
						}
						$.post(url + '/dynamic/deleteDynamic.do',datas,
						  function(response){
						  	 if(response.code == 0){
//						  	 	alert("删除成功");
						  	 	wrap.remove();
						  	 }
						  });
								
					} else {
					}
				})
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
var getDay = function (date){
	var day = ((date.split(" "))[0].split("-"))[2];
	
	return day;
}
//获取月份
var getMonth = function (date){
	var month = ((date.split(" "))[0].split("-"))[1];
	var monthStr = "";
	switch (month){
		case "01":
		    monthStr = "一月";
			break;
		case "02":
		    monthStr = "二月";
			break;
		case "03":
		    monthStr = "三月";
			break;
		case "04":
		    monthStr = "四月";
			break;
		case "05":
		    monthStr = "五月";
			break;
		case "06":
		    monthStr = "六月";
			break;
		case "07":
		    monthStr = "七月";
			break;
		case "08":
		    monthStr = "八月";
			break;
		case "09":
		    monthStr = "九月";
			break;
		case "10":
		    monthStr = "十月";
			break;
		case "11":
		    monthStr = "十一月";
			break;
		case "12":
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
        return unescape(r[2]);
    }
    return null;
}

