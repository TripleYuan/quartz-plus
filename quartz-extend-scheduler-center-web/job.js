const jobTbody = document.querySelector(".job-tbody");

loadJobTable();

function loadJobTable() {
    jobTbody.innerHTML = '';

    let url = baseUrl + '/api/job/list';
    let request = new XMLHttpRequest();
    try {
        request.open("get", url);
        request.send();
        request.onload = function () {
            let result = JSON.parse(request.responseText);
            let status = result.status;
            if (status === 0) {
                let data = result.data;
                for (let i = 0; i < data.length; i++) {
                    let model = data[i];
                    let tr = document.createElement('tr');

                    let schedNameTd = document.createElement("td");
                    schedNameTd.textContent = model.schedName;
                    tr.appendChild(schedNameTd);

                    let jobNameTd = document.createElement("td");
                    jobNameTd.textContent = model.jobName;
                    tr.appendChild(jobNameTd);

                    let jobDescTd = document.createElement("td");
                    jobDescTd.textContent = model.jobDesc;
                    tr.appendChild(jobDescTd);

                    let prevFireTimeTd = document.createElement("td");
                    prevFireTimeTd.textContent = model.prevFireTime;
                    tr.appendChild(prevFireTimeTd);

                    let nextFireTime = document.createElement("td");
                    nextFireTime.textContent = model.nextFireTime;
                    tr.appendChild(nextFireTime);

                    let triggerStateTd = document.createElement("td");
                    triggerStateTd.textContent = model.triggerState;
                    tr.appendChild(triggerStateTd);

                    let updateTimeTd = document.createElement("td");
                    updateTimeTd.textContent = model.updateTime;
                    tr.appendChild(updateTimeTd);

                    // button
                    let operationTd = document.createElement("td");
                    let triggerJobBtn = document.createElement("button");
                    let refreshBtn = document.createElement("button");
                    let delBtn = document.createElement("button");
                    triggerJobBtn.textContent = '执行job';
                    triggerJobBtn.onclick = function () {
                        let url = baseUrl + '/api/job/trigger';
                        let request = new XMLHttpRequest();
                        try {
                            request.open("post", url);
                            let body = {
                                'schedName': model.schedName,
                                'jobName': model.jobName,
                                'jobGroup': model.jobGroup
                            }
                            request.setRequestHeader('Content-Type', 'application/json');
                            request.send(JSON.stringify(body));
                            request.onload = function () {
                                let result = JSON.parse(request.responseText);
                                let status = result.status;
                                let success = result.data;
                                if (status === 0 && success) {
                                    alert('执行任务成功');
                                } else {
                                    alert('执行任务失败：' + result.message);
                                }
                            }
                        } catch (error) {
                            console.log(error);
                        }
                    };
                    refreshBtn.textContent = '刷新job';
                    refreshBtn.onclick = function () {
                        let url = baseUrl + '/api/job/refresh';
                        let request = new XMLHttpRequest();
                        try {
                            request.open("post", url);
                            let body = {
                                'schedName': model.schedName,
                                'triggerName': model.triggerName,
                                'triggerGroup': model.triggerGroup
                            }
                            request.setRequestHeader('Content-Type', 'application/json');
                            request.send(JSON.stringify(body));
                            request.onload = function () {
                                let result = JSON.parse(request.responseText);
                                let status = result.status;
                                if (status === 0) {
                                    let model = result.data;
                                    schedNameTd.textContent = model.schedName;
                                    jobNameTd.textContent = model.jobName;
                                    jobDescTd.textContent = model.jobDesc;
                                    prevFireTimeTd.textContent = model.prevFireTime;
                                    nextFireTime.textContent = model.nextFireTime;
                                    triggerStateTd.textContent = model.triggerState;
                                    updateTimeTd.textContent = model.updateTime;
                                } else {
                                    alert('刷新job失败：' + result.message);
                                }
                            }
                        } catch (error) {
                            console.log(error);
                        }
                    };
                    delBtn.textContent = '删除';
                    delBtn.onclick = function () {
                        let url = baseUrl + "/api/job/delete"
                        let request = new XMLHttpRequest();
                        try {
                            request.open("delete", url);
                            let body = {
                                'schedName': model.schedName,
                                'triggerName': model.triggerName,
                                'triggerGroup': model.triggerGroup
                            }
                            request.setRequestHeader('Content-Type', 'application/json');
                            request.send(JSON.stringify(body));
                            request.onload = function () {
                                let result = JSON.parse(request.responseText);
                                let status = result.status;
                                if (status === 0 && result.data) {
                                    tr.remove();
                                } else {
                                    alert('删除数据失败：' + result.message);
                                }
                            }
                        } catch (error) {
                            console.log(error);
                        }
                    };

                    operationTd.appendChild(triggerJobBtn);
                    operationTd.appendChild(refreshBtn);
                    operationTd.appendChild(delBtn);
                    tr.appendChild(operationTd);

                    jobTbody.appendChild(tr);
                }
            } else {
                alert('获取job列表失败：' + result.message);
            }
        }
    } catch (error) {
        console.log(error);
    }
}