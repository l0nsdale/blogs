function resendConfirmationMessage() {
    username = $("#username").val();
    $.ajax({
        url : '/resendConfirmation',
        type: 'POST',
        data : ({
            "username": username
        }),
        success: function () {
            $("#accountNotEnabled").find('*').remove();
            $("#accountNotEnabled").append("<p>Message send</p>");
        }
    });

}