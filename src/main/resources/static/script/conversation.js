let stompClient = null;
let myUserId = null;
let targetConversationId = null;

function connectWebSocket(myId, conversationId) {
    if (stompClient !== null) {
        stompClient.disconnect(() => {
            console.log("Disconnected from previous chat");
        });
    }

    myUserId = myId;
    targetConversationId = conversationId;

    const socket = new SockJS("/ws-stomp", null, {transports: ["websocket"], withCredentials: true});
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        // console.log('Connected: ' + frame);

        stompClient.subscribe(`/sub/conversations/${myUserId}`, function (message) {
            const renderedHtml = message.body;
            const container = document.getElementById('chat-message-list');
            container.insertAdjacentHTML('beforeend', renderedHtml);
            container.scrollTop = container.scrollHeight;
        });
    });
}

function sendMessage() {
    const input = document.getElementById('chat-input');
    const text = input.value?.trim();

    if (!text || !stompClient || !stompClient.connected || !targetConversationId) {
        console.warn("Unable to send message. Missing input or connection.");
        return;
    }

    const message = {
        message: text,
        conversationId: targetConversationId
    };

    // console.log("Sending message:", message);
    stompClient.send("/pub/conversations/message", {}, JSON.stringify(message));
    input.value = '';
}
