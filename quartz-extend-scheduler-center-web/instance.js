const instanceTbody = document.querySelector(".instance-tbody");

loadInstanceTable();

function loadInstanceTable() {
    instanceTbody.innerHTML = '';
    let request = new XMLHttpRequest();
    let url = baseUrl + '/api/instance/list';
    try {
        request.open('get', url);
        request.send();
        request.onload = function () {
            let result = JSON.parse(request.responseText);
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

                    let operationTd = document.createElement('td');
                    let delBtn = document.createElement('button');
                    delBtn.textContent = '删除';
                    delBtn.onclick = function () {
                        let url = baseUrl + '/api/instance/delete';
                        request.open('delete', url);
                        request.setRequestHeader('Content-Type', 'application/json');
                        let body = {
                            'schedName': model.schedName,
                            'instanceHost': model.instanceHost,
                            'instancePort': model.instancePort
                        };
                        request.send(JSON.stringify(body));
                        request.onload = function (event) {
                            let result = JSON.parse(request.responseText);
                            if (result.status === 0 && result.data) {
                                tr.remove();
                            } else {
                                alert("删除实例失败：" + result.message);
                            }
                        };
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