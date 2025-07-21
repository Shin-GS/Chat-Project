let accessToken = localStorage.getItem("accessToken");
let refreshToken = localStorage.getItem("refreshToken");

window.handleLoginSuccess = (event) => {
    if (event.detail.xhr.status === 200) {
        accessToken = event.detail.xhr.getResponseHeader("Authorization");
        refreshToken = event.detail.xhr.getResponseHeader("Authorization-Refresh");
        if (accessToken && refreshToken) {
            localStorage.setItem("accessToken", accessToken);
            localStorage.setItem("refreshToken", refreshToken);
            document.getElementById("modal-container").innerHTML = "";
            // window.location.reload();
        }
    }
};

function registerHtmxTokenHeaders() {
    document.body.addEventListener("htmx:configRequest", (event) => {
        if (accessToken) {
            event.detail.headers['Authorization'] = accessToken;
        }
        if (refreshToken) {
            event.detail.headers['Authorization-Refresh'] = refreshToken;
        }
    });

    document.body.addEventListener("htmx:afterOnLoad", (event) => {
        const xhr = event.detail.xhr;
        if (xhr.getResponseHeader("HX-Token-Invalid") === "true") {
            localStorage.removeItem("accessToken");
            localStorage.removeItem("refreshToken");
            accessToken = null;
            refreshToken = null;
            // window.location.reload();
        }
    });
}

if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", registerHtmxTokenHeaders);
} else {
    registerHtmxTokenHeaders();
}
