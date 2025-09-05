let stompClient = null;
let currentConversationId = null;
let currentSubscriptions = new Map(); // Track multiple subs by key
let reconnectTimer = null;            // Optional: backoff timer

// Unsubscribe ALL active subs for the current room
function unsubscribeCurrent() {
    try {
        for (const [, sub] of currentSubscriptions) {
            try {
                sub?.unsubscribe?.(); // Stop receiving from previous room
            } catch (e) {
                console.warn(e);
            }
        }
    } catch (e) {
        console.warn(e);
    } finally {
        currentSubscriptions.clear();
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

    // If a client exists but is not connected yet, wait briefly until it connects
    if (stompClient && !stompClient.connected) {
        const wait = setInterval(() => {
            if (stompClient && stompClient.connected) {
                clearInterval(wait);
                cb();
            }
        }, 50);
        // Safety: stop waiting after a short time
        setTimeout(() => clearInterval(wait), 3000);
        return;
    }

    const socket = new SockJS("/ws-stomp", null, { transports: ["websocket"], withCredentials: true });
    stompClient = Stomp.over(socket);
    stompClient.debug = null;               // Silence logs
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

// Subscribe to a conversation (idempotent per room)
function subscribeConversation(conversationId) {
    // Skip if already subscribed to the same room (has any active subs)
    if (
        stompClient &&
        stompClient.connected &&
        currentConversationId === conversationId &&
        currentSubscriptions.size > 0
    ) {
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

        const mountHandler = (frame) => {
            // Guard: drop late frames from a stale subscription
            if (currentConversationId !== desiredConversationId) return;

            const container = document.getElementById('conversation-message-list');
            if (!container) { // Panel may have been swapped out
                return;
            }

            container.insertAdjacentHTML('beforeend', frame.body);
            container.scrollTop = container.scrollHeight;

            refreshUI(frame);
        };

        // Subscribe: user messages
        const messageDest = `/user/sub/conversations/${desiredConversationId}`;
        const msgSub = stompClient.subscribe(messageDest, mountHandler);
        currentSubscriptions.set('messages', msgSub);
        console.log("Subscribed:", messageDest);

        // Subscribe: system messages
        const systemMessageDest = `/user/sub/conversations/${desiredConversationId}/system`;
        const sysSub = stompClient.subscribe(systemMessageDest, mountHandler);
        currentSubscriptions.set('system', sysSub);
        console.log("Subscribed:", systemMessageDest);
    });
}

// Refresh Ui
function refreshUI(frame) {
    const refreshIdsHeader = frame.headers['user-ui-refresh-ids'] || '';
    const refreshIds = Array.from(new Set(
        refreshIdsHeader.split(',').map(s => s.trim()).filter(Boolean)
    ));

    refreshIds.forEach(id => {
        const refreshElement = document.getElementById(id);
        if (refreshElement) {
            htmx.trigger(refreshElement, 'refresh');
        }
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
