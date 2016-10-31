//2秒自动消失提示框
angular.module('app.services').factory('CommonService', function() {
    return {
        //2秒消失提示框
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

        /*//播放记录服务
        playHisService:{
            //增加音乐播放记录
            addAudioHis: function (item) {
                addPlayHisItemByKey('audioHistory',item);
            },
            //获取音乐播放记录
            getAudioHis:function(){
                return store.get('audioHistory');
            },
            //增加视频播放记录
            addVideoHis: function (item) {
                addPlayHisItemByKey('videoHistory',item);
            },
            //获取视频播放记录
            getVideoHis:function(){
                return store.get('videoHistory');
            },
            //增加资讯阅读记录
            addInfHis: function (item) {
                addPlayHisItemByKey('infHistory',item);
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
                addSearchHisItemByKey('generalSearchHistory',item);
            },
            //获取全部搜索记录
            getGeneralHis:function(){
                return store.get('generalSearchHistory');
            },
            //增加音乐搜索记录
            addAudioHis: function (item) {
                addSearchHisItemByKey('audioSearchHistory',item);
            },
            //获取音乐搜索记录
            getAudioHis:function(){
                return store.get('audioSearchHistory');
            },
            //增加视频搜索记录
            addVideoHis: function (item) {
                addSearchHisItemByKey('videoSearchHistory',item);
            },
            //获取视频搜索记录
            getVideoHis:function(){
                return store.get('videoSearchHistory');
            },
            //增加资讯搜索记录
            addInfHis: function (item) {
                addSearchHisItemByKey('infSearchHistory',item);
            },
            //获得资讯搜索记录
            getInfHis:function(){
                return store.get('infSearchHistory');
            }
        }
    };

    function addSearchHisItemByKey(key,item){
        var history=store.get(key);
        if(history){
            $.each(history, function(ind,el){
                if(el.searchHis==item){
                    history.splice(ind,1);
                    return false;
                }
            });
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
        store.set(key,history);
    }

    function addPlayHisItemByKey(key,item){
        var history=store.get(key);
        if(history){
            $.each(history, function(ind,el){
                if(el.id==item.id){
                    history.splice(ind,1);
                    return false;
                }
            });
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
        store.set(key,history);*/
    }
});

