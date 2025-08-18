let stompClient = null;
let myUserId = null;
let targetConversationId = null;

function connectWebSocket(myId, conversationId) {
    if (stompClient !== null) {
        stompClient.disconnect(() => {
            console.log("Disconnected from previous conversation");
        });
    }

    myUserId = myId;
    targetConversationId = conversationId;

    const socket = new SockJS("/ws-stomp", null, {transports: ["websocket"], withCredentials: true});
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        // console.log('Connected: ' + frame);

        stompClient.subscribe(`/user/sub/conversations/${targetConversationId}`, function (message) {
            const renderedHtml = message.body;
            const container = document.getElementById('conversation-message-list');
            container.insertAdjacentHTML('beforeend', renderedHtml);
            container.scrollTop = container.scrollHeight;
        });
    });
}

function sendMessage() {
    const input = document.getElementById('conversation-input');
    const text = input.value?.trim();

    if (!text || !stompClient || !stompClient.connected || !targetConversationId) {
        console.warn("Unable to send message. Missing input or connection.");
        return;
    }

    const message = {
        conversationId: targetConversationId,
        message: text
    };

    // console.log("Sending message:", message);
    stompClient.send("/pub/conversations/message", {}, JSON.stringify(message));
    input.value = '';
}
