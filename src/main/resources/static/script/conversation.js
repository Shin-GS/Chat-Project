let stompClient = null;
let currentConversationId = null;
let currentSubscription = null; // Track active subscription
let reconnectTimer = null;      // Optional: backoff timer

// Unsubscribe previous topic (keep connection for reuse)
function unsubscribeCurrent() {
    try {
        if (currentSubscription) {
            currentSubscription.unsubscribe(); // Stop receiving from previous room
            currentSubscription = null;
        }
    } catch (e) {
        console.warn(e);
    }
}

// Fully disconnect (use when panel disappears or leaving the page)
function disconnectAll() {
    unsubscribeCurrent();
    try {
        if (stompClient && stompClient.connected) {
            stompClient.disconnect(() => console.log("STOMP disconnected"));
        }

    } catch (e) {
        console.warn(e);
    }

    stompClient = null;
    currentConversationId = null;
    if (reconnectTimer) {
        clearTimeout(reconnectTimer);
        reconnectTimer = null;
    }
}

// Ensure STOMP connection (create if missing), then run callback
function ensureConnected(cb) {
    if (stompClient && stompClient.connected) {
        cb();
        return;
    }

    const socket = new SockJS("/ws-stomp", null, {transports: ["websocket"], withCredentials: true});
    stompClient = Stomp.over(socket);
    stompClient.debug = null;          // Silence logs
    stompClient.heartbeat.outgoing = 10000; // Send heartbeat every 10s
    stompClient.heartbeat.incoming = 10000; // Expect heartbeat every 10s

    // Optional: basic backoff reconnect on close
    socket.onclose = () => {
        console.warn("SockJS closed");
        if (reconnectTimer) return;
        reconnectTimer = setTimeout(() => {
            reconnectTimer = null;
            if (currentConversationId) {
                // Try to re-subscribe to current conversation
                subscribeConversation(currentConversationId);
            }
        }, 2000); // basic backoff; tune as needed
    };

    stompClient.connect(
        {},
        () => cb(),
        (err) => {
            console.error("STOMP error:", err);
            // Optional: schedule reconnect attempt
            if (!reconnectTimer) {
                reconnectTimer = setTimeout(() => {
                    reconnectTimer = null;
                    if (currentConversationId) {
                        subscribeConversation(currentConversationId);
                    }
                }, 2000);
            }
        }
    );
}

// Subscribe to a conversation (no buttons, purely automatic)
function subscribeConversation(conversationId) {
    // Skip if already subscribed to the same room
    if (stompClient && stompClient.connected && currentConversationId === conversationId && currentSubscription) {
        return;
    }

    // Unsubscribe previous room first
    unsubscribeCurrent();

    // Capture the intended conversation for this subscription (guard against races)
    const desiredConversationId = conversationId;
    currentConversationId = conversationId;

    ensureConnected(() => {
        // Guard: if user switched room before connection finished, bail out
        if (currentConversationId !== desiredConversationId) return;

        const messageDest = `/user/sub/conversations/${desiredConversationId}`;
        currentSubscription = stompClient.subscribe(messageDest, (frame) => {
            // Guard: drop late frames from a stale subscription
            if (currentConversationId !== desiredConversationId) return;

            const container = document.getElementById('conversation-message-list');
            if (!container) return; // Panel may have been swapped out

            container.insertAdjacentHTML('beforeend', frame.body);
            container.scrollTop = container.scrollHeight;
        });
        console.log("Subscribed:", messageDest);
    });
}

// Send message (existing implementation; unchanged)
function sendMessage() {
    const input = document.getElementById('conversation-input');
    const text = input?.value?.trim();

    if (!text || !stompClient || !stompClient.connected || !currentConversationId) {
        return;
    }

    stompClient.send("/pub/conversations/message", {}, JSON.stringify({
        conversationId: currentConversationId,
        message: text
    }));

    input.value = '';
}
