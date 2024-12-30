document.addEventListener("DOMContentLoaded", function () {
    var socket = new SockJS('/ws');
    var stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/updates', function (update) {
            showNotification(update.body);
        });
    });

    function showNotification(message) {
        var notification = document.createElement('div');
        notification.className = 'notification';
        notification.innerText = message;
        document.body.appendChild(notification);

        setTimeout(function () {
            notification.remove();
        }, 5000);
    }
});