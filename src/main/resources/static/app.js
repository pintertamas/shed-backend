const url = 'https://localhost:8443';
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
            url: url + "/game/create?creator=" + username + "&numberOfDecks=1&numberOfCards=3",
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
    stompClient.connect({}, function () {
        setConnected(true);
        console.log(gameId);
        stompClient.subscribe('/topic/' + gameId, function (response) {
            handleGameplay(response);
        });
    });
}

function connectToRandom() {
    let username = document.getElementById("name").value;
    if (username == null || username === '') {
        alert("Please enter login");
    } else {
        $.ajax({
            url: url + "/game/connect/random",
            type: 'POST',
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({
                "username": username
            }),
            success: function (data) {
                gameId = data.gameId;
                connect(gameId);
                alert("Congrats you're in this lobby: " + data.gameId);
            },
            error: function (error) {
                console.log(error);
            }
        })
    }
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function handleGameplay(data) {
    console.log("connect response: " + data);
    console.log("action: " + JSON.stringify(data));
}

function action() {
    let message = document.getElementById("message").value;
    stompClient.send("/topic/" + gameId, {},
        JSON.stringify({
                'message': message,
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
    $("#connect-random").click(function () {
        connectToRandom();
    });
    $("#disconnect").click(function () {
        disconnect();
    });
    $("#send").click(function () {
        action();
    });
});