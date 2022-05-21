const schedNameList = document.querySelector("#schedNames");
const instanceTbody = document.querySelector(".instance-tbody");

loadSchedNameList();
loadInstanceTable();
schedNameList.addEventListener("click", loadInstanceTable);

function loadSchedNameList() {
    let xhr = new XMLHttpRequest();
    let url = baseUrl + '/api/job/sched-names';
    try {
        xhr.open("get", url);
        xhr.withCredentials = true;
        xhr.send();
        xhr.onload = function () {
            let result = JSON.parse(xhr.responseText);
            let status = result.status;
            if (status === 0) {
                let data = result.data;
                for (let i = 0; i < data.length; i++) {
                    let optionEle = document.createElement("option");
                    optionEle.textContent = data[i];
                    schedNameList.appendChild(optionEle);
                }
            } else {
                alert('获取quartz实例名称列表失败：' + result.message);
            }
        }
    } catch (error) {
        console.log(error);
    }
}

function loadInstanceTable() {
    instanceTbody.innerHTML = '';
    let xhr = new XMLHttpRequest();
    let url = baseUrl + '/api/instance/list';
    try {
        let index = schedNameList.selectedIndex;
        if (index > 0) {
            let params = "schedName=" + schedNameList.options[index].value;
            url = url + "?" + params
        }
        xhr.open('get', url);
        xhr.withCredentials = true;
        xhr.send();
        xhr.onload = function () {
            let result = JSON.parse(xhr.responseText);
            if (result.status === 0) {
                let data = result.data;
                for (let i = 0; i < data.length; i++) {
                    let model = data[i];

                    let tr = document.createElement('tr');

                    let schedNameTd = document.createElement('td');
                    schedNameTd.textContent = model.schedName;
                    tr.appendChild(schedNameTd);

                    let hostTd = document.createElement('td');
                    hostTd.textContent = model.instanceHost;
                    tr.appendChild(hostTd);

                    let ipTd = document.createElement('td');
                    ipTd.textContent = model.instancePort;
                    tr.appendChild(ipTd);

                    // 添加删除按钮
                    let operationTd = document.createElement('td');
                    let delBtn = document.createElement('button');
                    delBtn.textContent = '删除';
                    delBtn.onclick = function () {
                        deleteInstance(model, tr);
                    };
                    operationTd.appendChild(delBtn);
                    tr.appendChild(operationTd);

                    instanceTbody.appendChild(tr);
                }
            } else {
                alert('获取实例列表失败：' + result.message);
            }
        };
    } catch (error) {
        console.log(error);
    }
}

// 删除实例
function deleteInstance(model, tr) {
    let url = baseUrl + '/api/instance/delete';
    let xhr = new XMLHttpRequest();
    xhr.open('delete', url);
    xhr.withCredentials = true;
    xhr.setRequestHeader('Content-Type', 'application/json');
    let body = {
        'schedName': model.schedName,
        'instanceHost': model.instanceHost,
        'instancePort': model.instancePort
    };
    xhr.send(JSON.stringify(body));
    xhr.onload = function (event) {
        let result = JSON.parse(xhr.responseText);
        if (result.status === 0 && result.data) {
            tr.remove();
        } else {
            alert("删除实例失败：" + result.message);
        }
    };
}