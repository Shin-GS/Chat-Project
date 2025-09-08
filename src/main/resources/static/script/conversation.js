let stompClient = null;
let currentConversationId = null;

// Separate maps: keep global vs. per-conversation subs apart
const globalSubscriptions = new Map();      // e.g. key: 'ui'
const conversationSubscriptions = new Map(); // keys: 'messages', 'system'

let reconnectTimer = null; // backoff timer

// --- Helpers ---------------------------------------------------------------
// Unsubscribe ONLY per-conversation subscriptions (keep global alive)
function unsubscribeConversationSubs() {
    try {
        for (const [, sub] of conversationSubscriptions) {
            try {
                sub?.unsubscribe?.();
            } catch (e) {
                console.warn(e);
            }
        }
    } catch (e) {
        console.warn(e);
    } finally {
        conversationSubscriptions.clear();
    }
}

// Unsubscribe ALL (global + conversation) and disconnect socket
function disconnectAll() {
    // 1) per-conversation
    unsubscribeConversationSubs();

    // 2) global
    try {
        for (const [, sub] of globalSubscriptions) {
            try {
                sub?.unsubscribe?.();
            } catch (e) {
                console.warn(e);
            }
        }
    } catch (e) {
        console.warn(e);
    } finally {
        globalSubscriptions.clear();
    }

    // 3) socket
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
        // Make sure global subscription exists even if connection was created elsewhere
        ensureGlobalSubscribed();
        cb();
        return;
    }

    // If a client exists but is not connected yet, wait briefly until it connects
    if (stompClient && !stompClient.connected) {
        const wait = setInterval(() => {
            if (stompClient && stompClient.connected) {
                clearInterval(wait);
                ensureGlobalSubscribed();
                cb();
            }
        }, 50);
        // Safety: stop waiting after a short time
        setTimeout(() => clearInterval(wait), 3000);
        return;
    }

    const socket = new SockJS("/ws-stomp", null, {transports: ["websocket"], withCredentials: true});
    stompClient = Stomp.over(socket);
    stompClient.debug = null;               // Silence logs
    stompClient.heartbeat.outgoing = 10000; // Send heartbeat every 10s
    stompClient.heartbeat.incoming = 10000; // Expect heartbeat every 10s

    // Basic backoff reconnect on close
    socket.onclose = () => {
        console.warn("SockJS closed");
        scheduleReconnect();
    };

    stompClient.connect(
        {},
        () => {
            // On connect: restore global and current conversation subs
            ensureGlobalSubscribed();
            if (currentConversationId) {
                // Re-subscribe current conversation if any
                actuallySubscribeConversation(currentConversationId);
            }
            cb();
        },
        (err) => {
            console.error("STOMP error:", err);
            scheduleReconnect();
        }
    );
}

function scheduleReconnect() {
    if (reconnectTimer) return;
    reconnectTimer = setTimeout(() => {
        reconnectTimer = null;
        if (stompClient && stompClient.connected) return; // already back
        // Attempt to reconnect; restoring subs happens in onConnect
        ensureConnected(() => {
        });
    }, 2000);
}

// --- Global (always-on) subscription --------------------------------------
// Subscribe to user-scoped UI channel once and keep it alive
function ensureGlobalSubscribed() {
    if (!stompClient || !stompClient.connected) return;
    if (globalSubscriptions.has('ui')) return; // already subscribed

    const uiDest = "/user/queue/ui";
    const uiHandler = (frame) => {
        // Headers carry refresh ids; dispatch namespaced events to body
        refreshUI(frame);
    };
    const sub = stompClient.subscribe(uiDest, uiHandler);
    globalSubscriptions.set('ui', sub);
    console.log("Subscribed (global):", uiDest);
}

// --- Conversation subscription (created on panel mount) --------------------
function subscribeConversation(conversationId) {
    // If already on this room and have subs, do nothing
    if (
        stompClient &&
        stompClient.connected &&
        currentConversationId === conversationId &&
        conversationSubscriptions.size > 0
    ) {
        return;
    }

    // Unsubscribe previous conversation subs (keep global!)
    unsubscribeConversationSubs();

    // Capture the intended conversation for this subscription (guard against races)
    currentConversationId = conversationId;

    ensureConnected(() => {
        actuallySubscribeConversation(conversationId);
    });
}

function actuallySubscribeConversation(conversationId) {
    // Guard: connection can drop before here; ensure connected and correct room
    if (!stompClient || !stompClient.connected) return;
    if (currentConversationId !== conversationId) return;

    const desiredConversationId = conversationId;

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
    conversationSubscriptions.set('messages', msgSub);
    console.log("Subscribed (conv):", messageDest);

    // Subscribe: system messages
    const systemMessageDest = `/user/sub/conversations/${desiredConversationId}/system`;
    const sysSub = stompClient.subscribe(systemMessageDest, mountHandler);
    conversationSubscriptions.set('system', sysSub);
    console.log("Subscribed (conv):", systemMessageDest);
}

// --- UI refresh dispatcher -------------------------------------------------
function refreshUI(frame) {
    const refreshIdsHeader = frame.headers['user-ui-refresh-ids'] || '';
    const refreshIds = Array.from(new Set(
        refreshIdsHeader.split(',').map(s => s.trim()).filter(Boolean)
    ));

    // Fire namespaced events on <body>; HTMX listeners use "refresh:{id} from:body"
    refreshIds.forEach(id => {
        const refreshElement = document.getElementById(id);
        if (refreshElement) {
            htmx.trigger(document.body, 'refresh:' + id);
        }
    });
}

// --- Send message (unchanged) ---------------------------------------------
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
