//2秒自动消失提示框
angular.module('app.services').factory('CommonService', function() {
    return {
        showAlert: {
            cfg: {
                id: ".kBubble",
                speed: 5000
            },
            show: function (str) {
                var oBubble = $(this.cfg.id);
                if (!oBubble.length) {

                    oBubble = $('<div class="kBubble" />').appendTo($("body"));
                    oBubble.html(str).fadeIn();
                }
                setTimeout(this.hide(), this.cfg.speed);
            },
            hide: function () {
                $(this.cfg.id).fadeOut("slow", function () {
                    $(this).remove();
                });
            }
        },
        //播放记录服务
        playHisService:{
            //增加音乐播放记录
            addAudioHis: function (item) {
                var history=store.get('audioHistory');
                if(history){
                    var length=history.length;
                    if(length<10){
                        history.unshift(item);
                    }else if(length>=10){
                        history.splice(length-1,1);
                        history.unshift(item);
                    }
                }else{
                    history=[item];
                }
                store.set('audioHistory',history);
            },
            //获取音乐播放记录
            getAudioHis:function(){
                return store.get('audioHistory');
            },
            //增加视频播放记录
            addVideoHis: function (item) {
                var history=store.get('videoHistory');
                if(history){
                    var length=history.length;
                    if(length<10){
                        history.unshift(item);
                    }else if(length>=10){
                        history.splice(length-1,1);
                        history.unshift(item);
                    }
                }else{
                    history=[item];
                }
                store.set('videoHistory',history);
            },
            //获取视频播放记录
            getVideoHis:function(){
                return store.get('videoHistory');
            },
            //增加资讯阅读记录
            addInfHis: function (item) {
                var history=store.get('infHistory');
                if(history){
                    var length=history.length;
                    if(length<10){
                        history.unshift(item);
                    }else if(length>=10){
                        history.splice(length-1,1);
                        history.unshift(item);
                    }
                }else{
                    history=[item];
                }
                store.set('infHistory',history);
            },
            //获得资讯阅读记录
            getInfHis:function(){
                return store.get('infHistory');
            }
        },

        //搜索记录服务
        searchHisService:{
            //增加全部搜索记录
            addGeneralHis: function (item) {
                var history=store.get('generalSearchHistory');
                if(history){
                    var length=history.length;
                    if(length<10){
                        history.unshift({"searchHis":item});
                    }else if(length>=10){
                        history.splice(length-1,1);
                        history.unshift({"searchHis":item});
                    }
                }else{
                    history=[{"searchHis":item}];
                }
                store.set('generalSearchHistory',history);
            },
            //获取全部搜索记录
            getGeneralHis:function(){
                return store.get('generalSearchHistory');
            },
            //增加音乐搜索记录
            addAudioHis: function (item) {
                var history=store.get('audioSearchHistory');
                if(history){
                    var length=history.length;
                    if(length<10){
                        history.unshift({"searchHis":item});
                    }else if(length>=10){
                        history.splice(length-1,1);
                        history.unshift({"searchHis":item});
                    }
                }else{
                    history=[{"searchHis":item}];
                }
                store.set('audioSearchHistory',history);
            },
            //获取音乐搜索记录
            getAudioHis:function(){
                return store.get('audioSearchHistory');
            },
            //增加视频搜索记录
            addVideoHis: function (item) {
                var history=store.get('videoSearchHistory');
                if(history){
                    var length=history.length;
                    if(length<10){
                        history.unshift({"searchHis":item});
                    }else if(length>=10){
                        history.splice(length-1,1);
                        history.unshift({"searchHis":item});
                    }
                }else{
                    history=[{"searchHis":item}];
                }
                store.set('videoSearchHistory',history);
            },
            //获取视频搜索记录
            getVideoHis:function(){
                return store.get('videoSearchHistory');
            },
            //增加资讯搜索记录
            addInfHis: function (item) {
                var history=store.get('infSearchHistory');
                if(history){
                    var length=history.length;
                    if(length<10){
                        history.unshift({"searchHis":item});
                    }else if(length>=10){
                        history.splice(length-1,1);
                        history.unshift({"searchHis":item});
                    }
                }else{
                    history=[{"searchHis":item}];
                }
                store.set('infSearchHistory',history);
            },
            //获得资讯搜索记录
            getInfHis:function(){
                return store.get('infSearchHistory');
            }
        }
    };
});

