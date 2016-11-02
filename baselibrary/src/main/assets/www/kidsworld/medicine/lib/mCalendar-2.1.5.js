/*
version：v2.1.5
update:2016-01-25
*/
(function(wd){
	var doc=document;
	var tap='ontouchstart' in wd?'touchstart':'click';
	var touchstart='ontouchstart' in wd?'touchstart':'mousedown';
	var touchmove='ontouchmove' in wd?'touchmove':'mousemove';
	var touchend='ontouchend' in wd?'touchend':'mouseup';
	var requestAnimationFrame = wd.requestAnimationFrame || wd.webkitRequestAnimationFrame;
	var cancelAnimationFrame = wd.cancelAnimationFrame || wd.webkitCancelAnimationFrame;
	var transform='transform' in doc.createElement('div').style?'transform':'webkitTransform';
	wd.mCalendar=function(options){
		var this_=this;
		if(options){
			if(options.setDate){
				this_.getDate=options.setDate;
				var year=options.setDate.substr(0,4),
				month=parseInt(options.setDate.substr(5,2))-1,
				date=options.setDate.substr(8,2);
				month=month<10?'0'+month:month;
				this_.Date=new Date(year,month,date);
				this_.today={
					'day':this_.Date.getDay(),
					'date':this_.Date.getDate(),
					'month':this_.Date.getMonth(),
					'year':this_.Date.getFullYear()
				};
			};
			if(options.toBind){

				options.toBind.addEventListener(touchstart,function(){
				//options.toBind.addEventListener(propertychange,function(){
					this_.open();
					arguments[0].preventDefault();
				});
			};
			if(options.multiple){
				this_.multiple=true;
			};
			if(options.callback&&typeof(options.callback)=="function"){
				this_.hasCallback=true;
				this_.callback=options.callback;
			};
		};
		this_.handlerStart=Date.now();
		this_.currentIndex=this_.preIndex=1;
		this_.startX=0;
		this_.endX=0;
		this_.layout();
		this_.countbox=this_.byClass(this_.calendar,'countbox');
		this_.tables=this_.byTag(this_.dategroup,'table');
		this_.yearArrows_l=this_.byClass(this_.countbox[0],'l-arrows')[0];
		this_.yearArrows_r=this_.byClass(this_.countbox[0],'r-arrows')[0];
		this_.dateArrows_l=this_.byClass(this_.countbox[1],'l-arrows')[0];
		this_.dateArrows_r=this_.byClass(this_.countbox[1],'r-arrows')[0];
		this_.yearNum=this_.byClass(this_.calendar,'count-number')[0];
		this_.monthNum=this_.byClass(this_.calendar,'count-number')[1];
		this_.cancelBtn=this_.byClass(this_.calendar,'cancel-btn')[0];
		this_.finishBtn=this_.byClass(this_.calendar,'finish-btn')[0];
		this_.currentDate={
			'date':this_.today.date,
			'day':this_.today.day,
			'month':this_.today.month,
			'year':this_.today.year
		};
		this_.outputDate='';
		this_.init();	
	};
	wd.mCalendar.prototype.byClass=function(parentEl,className){
		return parentEl.getElementsByClassName(className);
	};
	wd.mCalendar.prototype.byTag=function(parentEl,tagName){
		return parentEl.getElementsByTagName(tagName);
	};
	wd.mCalendar.prototype.htmlStrJson={
		'header':'<div class="countbox">'+
					'<span class="l-arrows">&lt;</span>'+
					'<span class="count-number">2000年</span>'+
					'<span class="r-arrows">&gt;</span>'+
				'</div>'+
				'<div class="countbox">'+
					'<span class="l-arrows">&lt;</span>'+
					'<span class="count-number">00月</span>'+
					'<span class="r-arrows">&gt;</span>'+
				'</div>'+
				'<div class="btn-item">'+
					'<span class="btn cancel-btn">取消</span>'+
					'<span class="btn finish-btn">完成</span>'+
				'</div>',
		'dayHeader':'<thead>'+'<tr>'+
						'<th>日</th>'+
						'<th>一</th>'+
						'<th>二</th>'+
						'<th>三</th>'+
						'<th>四</th>'+
						'<th>五</th>'+
						'<th>六</th>'+
					'</tr>'+'</thead>'
	};
	//options:appendToEl,className
	wd.mCalendar.prototype.createElement=function(tagName,options){
		var newEl=document.createElement(tagName);
		if(options.className){
			this.addClass(newEl,options.className);
		};
		options.appendToEl.appendChild(newEl);
		return newEl;
	};
	wd.mCalendar.prototype.hasClass=function(el,className){
	    var className=className||'';
	    if(className.replace(/\s/g, '').length==0){return false;};
	    return new RegExp(' '+className+' ').test(' '+el.className+' ');
	};
	wd.mCalendar.prototype.addClass=function(el,className){
	    if(!wd.mCalendar.prototype.hasClass(el,className)){
	        el.className+=' '+className;
	    }
	};
	wd.mCalendar.prototype.removeClass=function(el,className){
	    if(wd.mCalendar.prototype.hasClass(el,className)){
	        var newClass=' '+el.className.replace(/[\t\r\n]/g,'')+' ';
	        while(newClass.indexOf(' '+className+' ')>= 0){
	            newClass=newClass.replace(' '+className+' ',' ');
	        }
	        el.className=newClass.replace(/^\s+|\s+$/g,'');
	    }
	};
	wd.mCalendar.prototype.close=function(this_){
		var this_=this_||this;
		this_.addClass(this_.calendar,'calendar-hide');
	};
	wd.mCalendar.prototype.open=function(this_){
		var this_=this_||this;
		this_.removeClass(this_.calendar,'calendar-hide');
	};
	wd.mCalendar.prototype.dates=[];
	wd.mCalendar.prototype.months=[];
	wd.mCalendar.prototype.Date=new Date();
	wd.mCalendar.prototype.today={
		'day':wd.mCalendar.prototype.Date.getDay(),
		'date':wd.mCalendar.prototype.Date.getDate(),
		'month':wd.mCalendar.prototype.Date.getMonth(),
		'year':wd.mCalendar.prototype.Date.getFullYear()
	};

	//布局
	wd.mCalendar.prototype.tablelength=3;
	wd.mCalendar.prototype.rows=6;
	wd.mCalendar.prototype.cols=7;
	wd.mCalendar.prototype.tdslengh=42;
	wd.mCalendar.prototype.layout=function(){
		var calendar=this.createElement('div',{
			'appendToEl':document.body,
			'className':'calendar'
		});
		var mask=this.createElement('div',{
			'appendToEl':calendar,
			'className':'mask'
		});
		var container=this.createElement('div',{
			'appendToEl':calendar,
			'className':'container'
		});
		var header=this.createElement('div',{
			'appendToEl':container,
			'className':'calendar-header'
		});
		var body=this.createElement('div',{
			'appendToEl':container,
			'className':'calendar-body'
		});
		var weekbox=this.createElement('div',{
			'appendToEl':body,
			'className':'week-header'
		});
		var weekHeader=this.createElement('table',{
			'appendToEl':weekbox
		});
		var dategroup=this.createElement('div',{
			'appendToEl':body,
			'className':'date-group'
		});
		header.innerHTML=this.htmlStrJson.header;
		weekHeader.innerHTML=this.htmlStrJson.dayHeader;

		this.calendar=calendar;
		this.dategroup=dategroup;
		this.addClass(this.calendar,'calendar-hide');
		//创建日历表
		var width=this.width=this.calendar.offsetWidth;
		for(var i=0;i<this.tablelength;i++){
			var daysItem=this.createElement('table',{
				'appendToEl':dategroup
			});
			var tbody=this.createElement('tbody',{
				'appendToEl':daysItem
			});
			for(var r=0;r<this.rows;r++){
				var tr=this.createElement('tr',{
					'appendToEl':tbody
				});
				for(var c=0;c<this.cols;c++){
					var td=this.createElement('td',{
						'appendToEl':tr
					});
				};
			};
			this.translateX(daysItem,-width+width*i);
		};
	};
	//切换日期: 参数dir 左右方向值 左：1 右：-1  switchType：切换年或月
	wd.mCalendar.prototype.switchDate=function(dir,switchType,duration){
		var duration=duration||250;
		var this_=this;
		if(Date.now()-this_.handlerStart<duration){
			return;
		};
		this_.endX+=this_.width*dir;
		this_.currentDate[switchType]+=dir*-1;
		if(this_.currentDate.month<0){
			this_.currentDate.year-=1;
			this_.currentDate.month=11;
		}else if(this_.currentDate.month>11){
			this_.currentDate.year+=1;
			this_.currentDate.month=0;
		};
		this_.currentIndex+=dir*-1;
		if(this_.currentIndex>this_.tablelength-1){
			this_.currentIndex=0;
		} else if(this_.currentIndex<0){
			this_.currentIndex=this_.tablelength-1;
		};
		this_.tables[this_.preIndex].removeAttribute('aria-show');
		this_.tables[this_.currentIndex].setAttribute('aria-show','true');
		if(dir===-1){
			var nextIndex=this_.currentIndex+1>this_.tablelength-1?0:this_.currentIndex+1;
		}else if(dir===1){
			var nextIndex=this_.currentIndex-1<0?this_.tablelength-1:this_.currentIndex-1;
		};
		this_.translateX(this_.tables[this_.preIndex],-this_.endX+this_.width*dir);
		this_.translateX(this_.tables[nextIndex],-this_.endX+this_.width*dir*-1);
		this_.translateX(this_.tables[this_.currentIndex],-this_.endX);
		if(switchType==='year'){
			this_.drawDate(this_.tables[this_.preIndex],this_.currentDate.year-dir*-1,this_.currentDate.month);
			this_.drawDate(this_.tables[nextIndex],this_.currentDate.year+dir*-1,this_.currentDate.month);
		}else if(switchType==='month'){
			this_.drawDate(this_.tables[this_.preIndex],this_.currentDate.year,this_.currentDate.month-dir*-1);
			this_.drawDate(this_.tables[nextIndex],this_.currentDate.year,this_.currentDate.month+dir*-1);
		};
		this_.drawDate(this_.tables[this_.currentIndex],this_.currentDate.year,this_.currentDate.month);
		this_.scrollX({
			'endX':this_.endX,
			'duration':duration,
			'callback':function(){
				this_.startX=this_.endX;
			}
		});
		this_.update();
		this_.preIndex=this_.currentIndex;
		this_.handlerStart=Date.now();
	};

	wd.mCalendar.prototype.select=function(){
		var this_=this;
		this_.bindselect=function(){
			var e=arguments[0];
			var thisEle=e.target.parentElement;
			if(thisEle&&thisEle.nodeName.toLowerCase()=='td'){
				var date=thisEle.getAttribute('aria-date');
				if(this_.multiple){
					if(this_.hasClass(thisEle,'active')&&this_.selectedDates[date]){
						this_.removeClass(thisEle,'active');
						this_.selectedDates[date]=null;
					}else{
						this_.addClass(thisEle,'active');
						this_.selectedDates[date]=date;
					};
				}else{
					for(var i=0;i<this.tdsAllLen;i++){
						this_.removeClass(this_.alltds[i],'active');
					};
					this_.addClass(thisEle,'active');
					this_.selectedDates['single']=date;
				};
			};
		};
	};
	wd.mCalendar.prototype.translateX=function(el,x){
		el.style[transform]="translate3d("+x+"px,0,0)";
	};

	wd.mCalendar.prototype.scrollX=function(options){
		var this_=this;
		requestAnimationFrame?cancelAnimationFrame(this_.timer):clearTimeout(this_.timer);
		var moveX=0,
		startX=this_.startX,
		endX=options.endX,
		duration=options.duration||250,
		stepTime=0,
		startTime=Date.now();
		function ani(){
			if(stepTime/duration>=1){
				requestAnimationFrame?cancelAnimationFrame(this_.timer):clearTimeout(this_.timer);
				this_.timer=null;
				if(options.callback){
					(options.callback)();
				};
				return;
			};
			stepTime=Math.min(duration,(Date.now()-startTime));
			moveX=startX+(endX-startX)/duration*stepTime;
			this_.startX=moveX;
			this_.translateX(this_.dategroup,moveX);
			this_.timer=requestAnimationFrame?requestAnimationFrame(ani):setTimeout(ani,0);
		};
		ani();
	};

	wd.mCalendar.prototype.swipeX=function(){
		var this_=this;
		var touchstartX=touchmoveX=moveendX=gapX=0;
		var dir=0;
		function move(){
			var touch=arguments[0].touches?arguments[0].touches[0]:arguments[0];
	        touchmoveX=touch.pageX;
	        gapX=touchmoveX-touchstartX;
	        if(touchmoveX>moveendX){
	        	//swipe-right
				dir=1;	
	        }else{
	        	//swipe-left
	        	dir=-1;
	        };
	        moveendX&&(this_.startX+=(touchmoveX-moveendX));
	        this_.translateX(this_.dategroup,this_.startX);
	        moveendX=touch.pageX;
	        arguments[0].preventDefault();
		};
		this_.dategroup.addEventListener(touchstart,function(){
	        var touch=arguments[0].touches?arguments[0].touches[0]:arguments[0];
	        touchstartX=touch.pageX;
	        this_.calendar.addEventListener(touchmove,move,false);
	        arguments[0].preventDefault();       
		},false);
		this_.calendar.addEventListener(touchend,function(){
			if(Math.abs(gapX)>10){
				this_.switchDate(dir,'month');
	    	}else{
	    		if(Math.abs(gapX)==0){
		    		this_.bindselect(arguments[0]);
		    	};
		    	if(Math.abs(gapX)>0&&!this_.hasClass(arguments[0].target,'l-arrows')&&!this_.hasClass(arguments[0].target,'r-arrows')){
		    		this_.scrollX({
		    			'endX':this_.endX,
		    			'duration':200,
		    			'callback':function(){
		    				this_.startX=this_.endX;
		    			}
		    		});
		    	};
	    	};
	        this_.calendar.removeEventListener(touchmove,move);
	        touchstartX=touchmoveX=moveendX=gapX=0;
		});
	};

	wd.mCalendar.prototype.drawDate=function(el,year,month){
		//绘制
		var tbody=this.byTag(el,'tbody')[0];
		var year=year;
		var month=month;
		if(month<0){
			year-=1;
			month=11;
		}else if(month>11){
			year+=1;
			month=0;
		};
		var startDate=1;//当月第一天日期
		var endDate=new Date(year,month+1,0).getDate();//当月最后一天日期
		var firstDay=new Date(year,month,1).getDay();//当月第一天是周几
		var preDate=new Date(year,month,0).getDate()-firstDay+1;//上个月补全开始日期
		var nextDate=1;//下个月补全开始日期

		var innerstr="";
		var tdstrs=[];
		for(var i=0;i<this.tdslengh;i++){
			if(i<firstDay){
				tdstrs.push(
							"<td class='mendDay' aria-date="+(month-1<0?year-1:year)+"-"+this.months[month-1<0?11:month-1]+"-"+this.dates[preDate-1]+">"+
								"<div>"+preDate+"</div>"+
							"</td>"
						);
				preDate++;
			}else if(startDate>endDate){
				tdstrs.push(
							"<td class='mendDay' aria-date="+(month+1>11?year+1:year)+"-"+this.months[month+1>11?0:month+1]+"-"+this.dates[nextDate-1]+">"+
								"<div>"+nextDate+"</div>"+
							"</td>"
						);
				nextDate++;
			}else{
				tdstrs.push(
							"<td aria-date="+year+"-"+this.months[month]+"-"+this.dates[startDate-1]+">"+
								"<div>"+startDate+"</div>"+
							"</td>"
						);
				startDate++;
			};
		};
		for(var r=0;r<this.rows;r++){
			var coltds="";
			for(var d=r*this.cols;d<r*this.cols+this.cols;d++){
				coltds+=tdstrs[d];
			};
			innerstr+="<tr>"+coltds+"</tr>";
		};
		tbody.innerHTML=innerstr;
	};
	wd.mCalendar.prototype.update=function(){
		this.yearNum.innerHTML=this.currentDate.year+'年';
		this.monthNum.innerHTML=String(this.currentDate.month+1).length==1?'0'+(this.currentDate.month+1)+'月':this.currentDate.month+1+'月';
		for(var t=0;t<this.tdsAllLen;t++){
			for(var s in this.selectedDates){
				if(this.alltds[t].getAttribute('aria-date')==this.selectedDates[s]){
					this.addClass(this.alltds[t],'active');
				};
			};
		};
	};
	wd.mCalendar.prototype.init=function(){
		var this_=this;
		for(var i=1;i<=31;i++){
			if(i<10){
				i='0'+i;
			};
			i=String(i);
			wd.mCalendar.prototype.dates.push(i);
		};
		for(var i=1;i<=12;i++){
			if(i<10){
				i='0'+i;
			};
			i=String(i);
			wd.mCalendar.prototype.months.push(i);
		};
		this_.alltds=this_.byTag(this_.calendar,'td');
		this_.tdsAllLen=this_.alltds.length;
		//默认选中今天日期
		this_.ariaToday=(this_.today.year+'-'+this_.months[this_.today.month]+'-'+this_.dates[this.today.date-1]);
		this_.selectedDates={};
		if(!this_.multiple){
			this_.selectedDates['single']=this_.ariaToday;
		}else{
			this_.selectedDates[this_.ariaToday]=this_.ariaToday;
		};
		this_.translateX(this_.dategroup,this_.startX);
		this_.swipeX();
		this_.dategroup.style.height=this_.byTag(this_.dategroup,'tbody')[0].offsetHeight+"px";


		for(var i=0;i<this_.tablelength;i++){
			this_.drawDate(this_.tables[i],this_.currentDate.year,this_.currentDate.month-1+i);
		};
		this_.tables[1].setAttribute('aria-show','true');
		this_.update();

		//切换月份
		this_.dateArrows_l.addEventListener(tap,function(){
			this_.switchDate(1,'month');
		},false);
		this_.dateArrows_r.addEventListener(tap,function(){
			this_.switchDate(-1,'month');	
		},false);
		//切换年份
		this_.yearArrows_l.addEventListener(tap,function(){
			this_.switchDate(1,'year');
		},false);
		this_.yearArrows_r.addEventListener(tap,function(){
			this_.switchDate(-1,'year');
		},false);

		this_.select();

		this_.cancelBtn.addEventListener('click',function(){
		//this_.cancelBtn.addEventListener('tap',function(){
			this_.close(this_);
		},false);
		this_.finishBtn.addEventListener('click',function(){
		//this_.finishBtn.addEventListener('tap',function(){
			this_.close(this_);
			var outputDate=[];
			for(var i in this_.selectedDates){
				if(this_.selectedDates[i]){
					outputDate.push(this_.selectedDates[i]);
				};
			};
			this_.outputDate=outputDate.length==1?outputDate:outputDate.sort().join(";   ");
			//this_.outputDate = outputDate;
			if(this_.hasCallback){
				(this_.callback)();
			};
			//测试
			//alert(2);
		},false);
	};
})(window);







