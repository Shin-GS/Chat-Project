let stompClient = null;
let myUserId = null; // 내 userId
let chatTargetId = null; // 현재 대화 중인 상대 userId

function connectWebSocket(myId, friendId) {
    if (stompClient !== null) {
        stompClient.disconnect(() => {
            console.log("Disconnected from previous chat");
        });
    }

    myUserId = myId;
    chatTargetId = friendId;

    const socket = new SockJS('/ws');
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
    if (!text || !stompClient || !chatTargetId) {
        return;
    }

    const message = {
        message: text,
        userId: chatTargetId
    };

    stompClient.send("/chat/message", {}, JSON.stringify(message));
    input.value = '';
}
