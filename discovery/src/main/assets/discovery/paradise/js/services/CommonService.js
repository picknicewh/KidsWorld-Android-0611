//2秒自动消失提示框
angular.module('app.services').factory('CommonService', function() {
    return {
        showAlert: {
            cfg: {
                id: ".kBubble",
                speed: 3000
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
        }
    };
});

