const url = 'http://localhost:8080';
let stompClient = null;
let gameId;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    } else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function createGame() {
    let username = document.getElementById("name").value;
    if (username == null || username === '') {
        alert("Please enter name");
    } else {
        $.ajax({
            url: url + "/game/create",
            type: 'POST',
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({
                "creator": username,
                "numberOfDecks": 1,
                "numberOfCards": 3,
            }),
            success: function (data) {
                gameId = data.gameId;
                alert("Game created with this gameId: " + data.gameId);
            },
            error: function (error) {
                console.log(error);
            }
        })
    }
}

function connect(id = null) {
    if (id != null) gameId = id;
    const socket = new SockJS(url + '/shed');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log(gameId);
        stompClient.subscribe('/topic/action/' + gameId, function (message) {
            showGreeting(JSON.parse(message.body).gameId);
            console.log("subscribed to /topic/action/" + gameId);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

function sendName() {
    stompClient.send("/topic/action/" + gameId, {},
        JSON.stringify({
                'gameId': gameId,
            }
        )
    );
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#connect").click(function () {
        let gameId = document.getElementById("game-id").value;
        if (gameId == null || gameId === '') {
            createGame();
            connect();
        } else {
            connect(gameId);
        }
    });
    $("#disconnect").click(function () {
        disconnect();
    });
    $("#send").click(function () {
        sendName();
    });
});