let baseUrl = 'http://localhost:62000';

const job = document.querySelector("#job");
const instance = document.querySelector("#instance");

job.onclick = function () {
    window.location.href = "job.html";
}

instance.onclick = function () {
    window.location.href = "instance.html";
}