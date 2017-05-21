function deleteBlog(idBlog){
    $.ajax({
        url : '/deleteblog',
        type: 'POST',
        data : ({
            "idBlog": idBlog
        }),
        success: function (id) {
            $('li').each(function(){
                if($(this).attr("id") == id){
                    $(this).remove();
                }
            });
        }
    });
}

function deletePage(idPage){
    $.ajax({
        url : '/deletepage',
        type: 'POST',
        data : ({
            "idPage": idPage
        }),
        success: function (pageId) {
            $("li[id='"+pageId+"']").remove();
        }
    });
}

function loadContent(html) {
    $("#content").append(html);
}

function like(idPage) {
    $.ajax({
        url : '/like',
        type: 'POST',
        data : ({
            "idPage": idPage
        }),
        success: function () {
            $.ajax({
                url : '/likes',
                type: 'POST',
                data : ({
                    "idPage": idPage
                }),
                success: function (rate) {
                    $("#rating").text(rate);
                }
            });
        }
    });
}

function sendComment(idPage) {
    var value = $("#commentArea").val();
    $.ajax({
        url : '/sendComment',
        type: 'POST',
        data : ({
            "idPage": idPage,
            "value": value
        }),
        success: function (info) {
            $("#countComments").text(Number($("#countComments").text().match(/[0-9]*/g)[0])+1 + 'comments');
            $(".comments-list").prepend('<div class="media">'+
                '<p class="pull-right"><small>'+info['createdAt']+'</small></p>'+
                '<div class="media-body">'+
                '<h4 class="media-heading user_name">'+info['name']+'</h4>'+
                '<p>'+value+'</p>'+
                '</div>'+
                '</div>')
        }
    });
}


$( function() {
    var dialog, form;
    dialog = $("#dialog-form").dialog({
        autoOpen: false,
        height: 250,
        width: 350,
        modal: true,
        buttons: {
            "Submit": subscribe,
            Cancel: function () {
                dialog.dialog("close");
            }
        },
        close: function () {
            form[0].reset();
        }
    });
    function subscribe() {
        var username = $("#username").text();
        var notification_feed = $("#notification-feed").is(":checked");
        var notification_email = $("#notification-email").is(":checked");
        $.ajax({
            url : '/subscribe',
            type: 'POST',
            data : ({
                "username": username,
                "notification_feed": notification_feed,
                "notification_email": notification_email
            }),
            success: function (pageId) {
                $("#subscribe").removeClass("glyphicon-star-empty");
                $("#subscribe").addClass("glyphicon-star");
            }
        });
        dialog.dialog("close");
    }
    form = dialog.find("form").on("submit", function (event) {
        event.preventDefault();
        subscribe();
        dialog.dialog("close");
    });
    $("#subscribe").button().on("click", function () {
        dialog.dialog("open");
    });
})



