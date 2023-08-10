import axios from 'axios'
import router from '../router'
import Cookies from 'js-cookie';

const http = axios.create({
  baseURL: 'http://localhost:62000/api',
  timeout: 3000,
  // headers: {'X-Custom-Header': 'foobar'}
})

// 添加请求拦截器
http.interceptors.request.use(function (config) {
  // 在发送请求之前做些什么
  return config;
}, function (error) {
  // 对请求错误做些什么
  return Promise.reject(error);
});

// 添加响应拦截器
http.interceptors.response.use(function (response) {
  // 2xx 范围内的状态码都会触发该函数。
  const { data } = response
  // console.log(router, 'router')
  // console.log(router.app.$route.path, 'router.app.$route.path')
  if (data.status === 401) {
    Cookies.remove('token')
    router.push('/login').catch(err => { })
  }
  return response;
}, function (error) {
  // 超出 2xx 范围的状态码都会触发该函数。
  // 对响应错误做点什么
  return Promise.reject(error);
});

export default http