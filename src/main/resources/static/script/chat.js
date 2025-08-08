let stompClient = null;
let myUserId = null;
let chatTargetId = null;

function connectWebSocket(myId, friendId) {
    if (stompClient !== null) {
        stompClient.disconnect(() => {
            console.log("Disconnected from previous chat");
        });
    }

    myUserId = myId;
    chatTargetId = friendId;

    const socket = new SockJS("/ws-stomp", null, {transports: ["websocket"], withCredentials: true});
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);

        stompClient.subscribe(`/sub/chat/${myUserId}`, function (message) {
            const chatMessage = JSON.parse(message.body);
            const container = document.getElementById('chat-message-list');
            const div = document.createElement('div');
            div.textContent = `[${chatMessage.from}] ${chatMessage.message}`;
            container.appendChild(div);
        });
    });
}

function sendMessage() {
    const input = document.getElementById('chat-input');
    const text = input.value?.trim();

    if (!text || !stompClient || !stompClient.connected || !chatTargetId) {
        console.warn("Unable to send message. Missing input or connection.");
        return;
    }

    const message = {
        message: text,
        userId: chatTargetId
    };

    console.log("Sending message:", message);
    stompClient.send("/pub/chat/message", {}, JSON.stringify(message)); // must include "/pub"
    input.value = '';
}
