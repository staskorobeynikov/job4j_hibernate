$(document).ready(function () {
    showUsersAdverts();
});

function showUsersAdverts() {
    $.ajax({
        type: 'POST',
        url: './adverts',
        datatype: 'json',
        success: function (data) {
            fillTable(data);
        }
    });
}

function fillTable(data) {
    let adverts = data;
    for (let i = 0; i < adverts.length; i++) {
        let id = adverts[i].id;
        let carAdd = adverts[i]['car'].mark.name + '\n' + adverts[i]['car'].model.name;
        let price = adverts[i]['price'];
        let photo = adverts[i]['imageName'];
        let status = adverts[i]['status'];
        if (status === false) {
            status = "В продаже";
        } else {
            status = "Продано";
        }
        $('#adverts tr:last').after(
            '<tr><td id="car">' + carAdd + '</td>'
            + '<td id="price">' + price + '</td>'
            + '<td id="photo"><img src="./download?name=' + photo + '" width="250px" height="200px"></td>'
            + '<td id="status">' + status + "</br></br>" + '<button type="button" class="btn btn-dark" id="' + id + '">Change status</button></td></tr>'
        );
    }
}

$(document).on('click', ':button', function () {
    let id = $(this).attr('id');
    if (id !== 'add') {
        $.ajax({
            type: 'POST',
            url: './status',
            data: {id : id, status : true},
            datatype: 'json',
            success: function (data) {
                location.reload();
            }
        });
    }
});