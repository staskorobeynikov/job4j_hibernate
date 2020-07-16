function logIn() {
    if (validate()) {
        checkAccount();
    }
}

function checkAccount() {
    $.ajax({
        type: 'POST',
        url: './login',
        data: {login : $('#login').val(), password : $('#password').val()},
        dataType: 'json',
        success: function ($data) {
            if ($data['answer'] !== undefined) {
                alert($data['answer']);
            }
            if ($data['user'] !== undefined) {
                setTimeout(function () {
                    window.location.href = "/buyer/adverts.html";
                }, 1000);
            }
        }
    });
}

function validate() {
    let result = true;
    let answer = '';
    let elements = [$('#login'), $('#password')];
    for (let i = 0; i < elements.length; i++) {
        if (elements[i].val() === '') {
            answer += "Please, " + elements[i].attr("placeholder").toLowerCase() + "\n";
            result = false;
        }
    }
    if (!result) {
        alert(answer);
    }
    return result;
}