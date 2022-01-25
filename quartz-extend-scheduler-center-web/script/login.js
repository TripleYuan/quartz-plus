const loginLabel = document.querySelector("#submit");
loginLabel.addEventListener("click", login);

document.querySelector("#cancel-login").addEventListener("click", function () {
    window.location.href = "unauthorized.html";
})
document.querySelector("#forget-pwd").addEventListener("click", function () {
    alert("请联系管理员");
})

// 登录
function login() {
    let username = document.querySelector("#uname").value;
    let password = document.querySelector("#psw").value;

    let xhr = new XMLHttpRequest();
    let url = baseUrl + '/api/login';
    xhr.open("post", url);
    xhr.withCredentials = true;
    let body = {
        'username': username,
        'password': md5(password)
    }
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send(JSON.stringify(body));
    xhr.onload = function () {
        let result = JSON.parse(xhr.responseText);
        let status = result.status;
        if (status === 0) {
            window.location.href = "job.html";
        } else if (status === 2) {
            alert(result.message);
        } else {
            handleError(result);
        }
    }
}