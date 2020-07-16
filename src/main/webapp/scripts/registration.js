$(document).on("keyup", ":password", function () {
    let input = $("#password").val();
    let replay = $("#cor_password");
    if (input !== replay.val()) {
        $(replay).attr("style", "background-color: red");
        $("#submit").attr("disabled", "disabled");
        $(".error").html("Passwords do not match");
    } else {
        $("#submit").removeAttr("disabled");
        $(replay).removeAttr("style");
        $(".error").html("");
    }
});