function submitFrom() {
    var content = [];
    $(".content").each(function (index) {
        if ($(this).children("textarea").val()) {
            contentT = $(this).children("textarea").val();
            numContainer = $(this).parent($('.droppable')).attr("id");
            $.ajax({
                url: '/savecontent',
                type: 'POST',
                data: ({
                    "content": contentT,
                    "type": 1,
                    "numContainer": numContainer,
                    "position": index
                }),
                success: function () {

                }
            });
        }
        if ($(this).children("iframe").attr('src')) {
            var $temp1 = $(this).parent($(".content")).parent($("div#iframe"));
            content.push($(this).children("iframe").attr('src'));
            content.push("2");
            content.push($temp1.parent($('.droppable')).attr("id"));
            content.push(index);
        }
        if ($(this).children(".navbar-right").attr('style')) {
            content.push("0");
            content.push("3");
            content.push($(this).parent($('.droppable')).attr("id"));
            content.push(index);
        }
        if ($(this).children(".comment").attr('style')) {
            var tempor = $(this).parent($(".content")).parent($(".modal-content")).parent($(".modal-dialog"));
            content.push("0");
            content.push("4");
            content.push(tempor.parent($('.droppable')).attr("id"));
            content.push(index);
        }
        if ($(this).find(".el")) {
            number = index;
            var $temp = $(this).parent($(".content")).parent($(".modal-content")).parent($(".modal-dialog"));
            $textarea = $(this).find(".el");
            $.each($textarea, function () {
                content.push($(this).val());
                content.push("5");
                content.push($temp.parent($('.droppable')).attr("id"));
                content.push(number);
            });
        }
        if ($(this).children(".images").attr('src')) {
            var temp1 = $(this).parent($(".content")).parent($('.droppable')).attr("id");
            var tempcontent = $(this).children(".images").attr('src');
            tempcontent = tempcontent.replace(",", "#");
            tempcontent = tempcontent.replace(",", "#");
            content.push(tempcontent);
            content.push("6");
            content.push(temp1);
            content.push(index);
        }

    });
    $("[name=content]").val(content);
}

function savePage() {
    // s = $("#dropableContainer");
    // $("#submitForm").append("<div class='form-group'><input type='hidden' value='"+s.html()+"' name='originalValue'/></div>");
    // content = [];
    // contentToReplace = [];
    // $(".content").each(function (index) {
    //     if ($(this).children("textarea")[0]) {
    //         if ($(this).children("textarea").val()) {
    //             contentT = $(this).children("textarea").val();
    //             content.push(contentT);
    //             contentToReplace.push($(this)[0]);
    //             numContainer = $(this).parent($('.droppable')).attr("id");
    //         } else {
    //             content.push('');
    //             contentToReplace.push($(this)[0]);
    //             numContainer = $(this).parent($('.droppable')).attr("id")
    //         }
    //     }
    //     if (tinyMCE.get($($(".content")[0]).children("textarea").attr("id"))) {
    //         if (tinyMCE.get($($(".content")[0]).children("textarea").attr("id")).getContent()) {
    //             contentT = tinyMCE.get($($(".content")[0]).children("textarea").attr("id")).getContent();
    //             content.push(contentT);
    //             contentToReplace.push($(this)[0]);
    //             numContainer = $(this).parent($('.droppable')).attr("id");
    //         } else {
    //             content.push('');
    //             contentToReplace.push($(this)[0]);
    //             numContainer = $(this).parent($('.droppable')).attr("id")
    //         }
    //     }
    // });
    // contentToReplace.forEach(function(item, i, arr) {
    //     item.innerHTML = content[i];
    // });
    // $(".jumbotron").addClass("containerContent");
    // $(".jumbotron").removeAttr("style");
    // $(".jumbotron").removeClass("jumbotron droppable ui-sortable");
    // $(".content").removeAttr("style");
    // $(".content").removeClass("draggable-text ui-draggable ui-draggable-handle content");
    // if ($("#3")) {
    //     $("#3").addClass("centered-div");
    // }
    //
    // s = $("#dropableContainer");
    // // document.body.innerHTML="";
    // // document.body.innerHTML=s.html();
    var content = tinyMCE.editors[0].getContent();
    $("#submitForm").append("<div class='form-group'><input type='hidden' value='"+content+"' name='value'/></div>");
    var tags = $(".bootstrap-tagsinput").tagsinput('items')[1];
    $("#submitForm").append("<div class='form-group'><input type='hidden' value='"+tags+"' name='tages'/></div>");
    $("#submitForm").submit();


}