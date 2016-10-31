//通用后台数据交换服务，包含get和post方法
angular.module('app.services').factory('WebService', function(AjaxService) {
    return {
        ////获取首页轮播图-本机测试
        //getParadiseBanner: function() {
        //    var options={};
        //    options.type = "get";
        //    options.useBaseUrl = false;
        //    options.url = "jsonTemp/paradiseBanner.json";
        //    return AjaxService.ajax(options);
        //},
        ////获取播放列表-本机测试
        //getAudioPlayList: function() {
        //    var options={};
        //    options.type = "get";
        //    options.useBaseUrl = false;
        //    options.url = "jsonTemp/paradiseAudio.json";
        //    return AjaxService.ajax(options);
        //},
        ////获取播放列表-本机测试
        //getSearchHis: function() {
        //    var options={};
        //    options.type = "get";
        //    options.useBaseUrl = false;
        //    options.url = "jsonTemp/paradiseSearchHistory.json";
        //    return AjaxService.ajax(options);
        //},

        //获取家长喂药列表
        getMedicineP: function(tsId,pageNumber,pageSize){
            var options = {};
            options.type = "post";
            options.useBaseUrl = true;
            options.url = "/school/patriarchFeedingMedicine.do";
            options.data = {
                tsId:tsId,
                pageNumber:pageNumber,
                pageSize:pageSize
            };
            return AjaxService.ajax(options);
        },

        //家长删除委托
        delEntrust: function(tsId,medicineId){
            var options = {};
            options.type = "post";
            options.useBaseUrl = true;
            options.url = "/school/deleteMedicine.do";
            options.data = {
                tsId:tsId,
                medicineId:medicineId
            };
            return AjaxService.ajax(options);
        },

        //教师喂药列表
        getMedicineT: function(tsId) {
            var options = {};
            options.type = "post";
            options.useBaseUrl = true;
            options.url = "/school/teacherFeedingMedicine.do";
            options.data = {
                tsId:tsId
            };
            return AjaxService.ajax(options);
        },

        //老师获取喂药任务详情
        getMedicineTaskDetails: function(medicineId) {
            var options = {};
            options.type = "post";
            options.useBaseUrl = true;
            options.url = "/school/medicineDetails.do";
            options.data = {
                medicineId:medicineId
            };
            return AjaxService.ajax(options);
        },

        //老师完成喂药
        finishedMedicine: function(tsId,medicineId){
            var options = {};
            options.type = "post";
            options.useBaseUrl = true;
            options.url = "/school/finishMedicine.do";
            options.data = {
                tsId: tsId,
                medicineId:medicineId
            };
            return AjaxService.ajax(options);
        },

        //提交喂药委托
        submitEnturst:function(data){
            var options = {};
            options.type = "post";
            options.useBaseUrl = true;
            options.url = "/school/publishMedicineCommissioned.do";
            options.data = data;
            return AjaxService.ajax(options);
        },

        //获取首页轮播图
        getHomeBanner:function (tsId) {
            var options={};
            options.type = "post";
            options.useBaseUrl = true;
            options.url = "/rakuen/getBanner.do";
            options.data={
                tsId:tsId
            };
            return AjaxService.ajax(options);
        },
        //获取推荐
        getRecommenda:function (tsId,size,type) {
            var options={};
            options.type = "post";
            options.useBaseUrl = true;
            options.url = "/rakuen/getRecommenda.do";
            options.data={
                tsId:tsId,
                size:size,
                type:type
            };
            return AjaxService.ajax(options);
        },
        //获取首页全部推荐
        getIndexAllRecommenda:function (tsId,size) {
            var options={};
            options.type = "post";
            options.useBaseUrl = true;
            options.url = "/rakuen/getIndexAllRecommenda.do";
            options.data={
                tsId:tsId,
                size:size
            };
            return AjaxService.ajax(options);
        },
        //获取对应内容
        getContent:function (tsId,type,pageSize,pageNumber,typeid) {
            var options={};
            options.type = "post";
            options.useBaseUrl = true;
            options.url = "/rakuen/getContent.do";
            options.data={
                tsId:tsId,
                type:type,
                pageSize:pageSize,
                pageNumber:pageNumber,
                typeid:typeid
            };
            return AjaxService.ajax(options);
        },
        //获取该分类下子分类
        getClassify:function (tsId,type) {
            var options={};
            options.type = "post";
            options.useBaseUrl = true;
            options.url = "/rakuen/getClassify.do";
            options.data={
                tsId:tsId,
                type:type
            };
            return AjaxService.ajax(options);
        },
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
        //收藏资源（取消收藏）
        subAttention:function (tsId,resourceid,cancel) {    //cancel 1=收藏 2=取消
            var options={};
            options.type = "post";
            options.useBaseUrl = true;
            options.url = "/rakuen/subAttention.do";
            options.data={
                tsId:tsId,
                resourceid:resourceid,
                cancel:cancel
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
        //根据名称模糊搜索资源列表
        getResourceListByName:function (name,type,pageSize,pageNumber) {
            var options={};
            options.type = "post";
            options.useBaseUrl = true;
            options.url = "/rakuen/getResourceListByName.do";
            options.data={
                name:name,
                type:type,
                pageSize:pageSize,
                pageNumber:pageNumber
            };
            return AjaxService.ajax(options);
        },
        //我的收藏列表
        getMyAttentionResourceList:function (tsId,type,pageSize,pageNumber) {
            var options={};
            options.type = "post";
            options.useBaseUrl = true;
            options.url = "/rakuen/getMyAttentionResourceList.do";
            options.data={
                tsId:tsId,
                type:type,
                pageSize:pageSize,
                pageNumber:pageNumber
            };
            return AjaxService.ajax(options);
        },
        //获取资源详细信息
        getResourceDetail:function (tsId,resourceid) {
            var options={};
            options.type = "post";
            options.useBaseUrl = true;
            options.url = "/rakuen/getResourceDetail.do";
            options.data={
                tsId:tsId,
                resourceid:resourceid
            };
            return AjaxService.ajax(options);
        },
        //获取资源详细信息
        updateResourcePv:function (resourceid) {
            var options={};
            options.type = "post";
            options.useBaseUrl = true;
            options.url = "/rakuen/updateResourcePv";
            options.data={
                resourceid:resourceid
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
            options.url = "/rakuen/getCommentList";
            options.data={
                resourceid:resourceid,
                pageSize:pageSize,
                pageNumber:pageNumber
            };
            return AjaxService.ajax(options);
        },
        //查询评论列表
        getTsInfo:function (tsId) {
            var options={};
            options.type = "post";
            options.useBaseUrl = true;
            options.url = "/rakuen/getTsInfo";
            options.data={
                tsId:tsId
            };
            return AjaxService.ajax(options);
        }
    };
});

