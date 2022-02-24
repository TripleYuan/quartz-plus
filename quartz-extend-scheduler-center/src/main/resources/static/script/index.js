const jobList = document.querySelector("#job-list");
const instanceManage = document.querySelector("#instance-manage");
const jobManage = document.querySelector("#job-manage");

jobList.onclick = function () {
    window.location.href = "job.html";
}
instanceManage.onclick = function () {
    window.location.href = "instance-manage.html";
}
jobManage.onclick = function () {
    window.location.href = "job-manage.html";
};