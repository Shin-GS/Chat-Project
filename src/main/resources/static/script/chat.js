let stompClient = null;

function connect() {
    const socket = new SockJS('/ws');  // Spring WebSocket endpoint
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/sub/chat', function (message) {
            showMessage(JSON.parse(message.body));
        });
    });
}

function sendMessage() {
    const input = document.getElementById('chat-input');
    const message = {
        to: "roomA", // or get dynamically
        message: input.value
    };
    stompClient.send("/chat/message/user1", {}, JSON.stringify(message));
    input.value = '';
}

function showMessage(msg) {
    const container = document.getElementById('chat-messages');
    const div = document.createElement('div');
    div.textContent = `[${msg.from}] ${msg.message}`;
    container.appendChild(div);
}

connect();
