/**
 * Created by lenovo2 on 2016/7/29.
 */
angular.module('app.controllers')

.controller('openClassCtrl',function($scope, $state, NoteService) {

        $scope.items=[{
            name:"摄像头1号"
        },{
            name:"摄像头2号"
        },{
            name:"摄像头3号"
        },{
            name:"摄像头4号"
        },{
            name:"摄像头5号"
        },{
            name:"摄像头6号"
        }
        ]
})