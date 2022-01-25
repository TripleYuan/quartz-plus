let baseUrl = 'http://localhost:62000';

// let pathname = window.location.pathname;
// let protocol = window.location.protocol;
// if (pathname.startsWith('/revert/quartzweb')) {
//     if (protocol === 'https:') {
//         baseUrl = 'https://prod/quartzJobScheduler';
//     } else {
//         baseUrl = 'http://prod/quartzJobScheduler';
//     }
//
//     const h1 = document.querySelector('h1');
//     h1.textContent = '生产环境';
// } else if (pathname.startsWith('/revert/testquartzweb')) {
//     if (protocol === 'https:') {
//         baseUrl = 'https://test/testQuartzJobScheduler';
//     } else {
//         baseUrl = 'http://test/testQuartzJobScheduler';
//     }
//     h1.textContent = '测试环境';
// }

// 错误处理
function handleError(result) {
    let status = result.status;
    let message = result.message;
    console.log(status, ", ", message);
    if (status === 401) {
        // 登录
        window.location.href = "login.html";
    } else {
        alert('error：' + message);
    }
}