$(document).ready(function () {
    showAll();
});

function showAll() {
    $.ajax({
        type: 'POST',
        url: './cars',
        dataType: 'json',
        success: function ($data) {
            addRows($data)
        }
    });
}

function addRows(data) {
    let adverts = data;
    for (let i = 0; i < adverts.length; i++) {
        let id = adverts[i].id;
        let carAdd = adverts[i]['car'].mark.name + '\n' + adverts[i]['car'].model.name;
        let price = adverts[i]['price'];
        let seller = adverts[i]['owner'].name + '\n' + adverts[i]['owner'].phone;
        let photo = adverts[i]['imageName'];
        let dateAdd = adverts[i]['createdDate'];
        let status = adverts[i]['status'];
        if (status === false) {
            status = "В продаже";
        } else {
            status = "Продано";
        }
        $('#allAdverts tr:last').after(
            '<tr class="rows"><td id="car">' + carAdd + "</br></br>"
            + '<button id="' + id + '" type="button" class="btn btn-primary" data-toggle="modal" data-target="#carInfo">'
            + 'Details</button></td>'
            + '<td id="price">' + price + '</td>'
            + '<td id="seller">' + seller + '</td>'
            + '<td id="photo"><img src="./download?name=' + photo + '" width="200px" height="150px"></td>'
            + '<td id="date_add">' + dateAdd + '</td>'
            + '<td id="status">' + status + '</td></tr>'
        );
    }
}

$(document).on('click', ':button', function () {
    let id = $(this).attr('id');
    if (id !== 'add') {
        $.ajax({
            type: 'POST',
            url: './info',
            data: {id : id},
            dataType: 'json',
            success: function (data) {
                addInfo(data);
            }
        });
    }
});

function addInfo(data) {
    let car = data;
    document.getElementById("infoLabel").innerHTML = car.mark.name + "\n" + car.model.name;
    let result = "<tr><td>Mile age</td><td>" + car.mileAge + " km</td></tr>";
    result += "<tr><td>Date created</td><td>" + car.created + "</td></tr>";
    result += "<tr><td>Gear box</td><td>" + car.transmission.gearBox + "</td></tr>";
    result += "<tr><td>Gear type</td><td>" + car.transmission.gearType + "</td></tr>";
    result += "<tr><td>Car body type</td><td>" + car.carBody.type + "</td></tr>";
    result += "<tr><td>Car body color</td><td>" + car.carBody.color + "</td></tr>";
    result += "<tr><td>Count door</td><td>" + car.carBody.countDoor + "</td></tr>";
    result += "<tr><td>Engine volume</td><td>" + car.engine.volume + " l</td></tr>";
    result += "<tr><td>Engine power</td><td>" + car.engine.power + " hp</td></tr>";
    result += "<tr><td>Engine type</td><td>" + car.engine.type + "</td></tr>";
    document.getElementById("info").innerHTML = result;
}

function enter() {
    window.location.href = "/buyer/login.html";
}

$(document).on('change', ':checkbox', function () {
    let id = $(this).attr("id");
    console.log(id);
    if (id !== "fPhoto") {
        deleteRows();
        let check = $(this).prop("checked");
        if (check) {
            $.ajax({
                type: 'POST',
                url: './lastday',
                dataType: 'json',
                success: function ($data) {
                    addRows($data);
                }
            });
        } else {
            location.reload();
        }
    } else {
        deleteRows();
        let check = $(this).prop("checked");
        if (check) {
            $.ajax({
                type: 'POST',
                url: './withphoto',
                dataType: 'json',
                success: function ($data) {
                    addRows($data);
                }
            });
        } else {
            location.reload();
        }
    }
});

function deleteRows() {
    let rows = document.getElementsByClassName("rows");
    console.log(rows.length);
    for (let i = 0; i < rows.length; i++) {
        rows[i].innerHTML = "";
    }
}

function showAdvertsMark() {
    let selectModel = $("#mark").val();
    deleteRows();
    $.ajax({
        type: 'POST',
        url: './marks',
        data: {mark : selectModel},
        dataType: 'json',
        success: function ($data) {
            addRows($data);
        }
    });
}