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
