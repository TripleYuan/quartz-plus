import Vue from 'vue'
import App from './App.vue'
import {
  Row, Col, Button, Container, Header, Main, Aside, Menu, Submenu, MenuItem,
  MenuItemGroup, Dropdown, DropdownMenu, DropdownItem, Card, Table, TableColumn,
  Breadcrumb, BreadcrumbItem, Tag, Dialog, Form, FormItem, Input, Select, Option,
  DatePicker, MessageBox, Message, Pagination, Tooltip, Tree
} from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import router from './router'
import store from './store'
import Cookies from 'js-cookie'

Vue.use(Row);
Vue.use(Col);
Vue.use(Button);
Vue.use(Container);
Vue.use(Header);
Vue.use(Main);
Vue.use(Aside);
Vue.use(Menu);
Vue.use(Submenu);
Vue.use(MenuItem);
Vue.use(MenuItemGroup);
Vue.use(Dropdown);
Vue.use(DropdownMenu);
Vue.use(DropdownItem);
Vue.use(Card);
Vue.use(Table);
Vue.use(TableColumn);
Vue.use(Breadcrumb);
Vue.use(BreadcrumbItem);
Vue.use(Tag);
Vue.use(Dialog);
Vue.use(Form);
Vue.use(FormItem);
Vue.use(Input);
Vue.use(Select);
Vue.use(Option);
Vue.use(DatePicker)
Vue.use(Pagination)
Vue.use(Tooltip)
Vue.use(Tree)
Vue.prototype.$confirm = MessageBox.confirm
Vue.prototype.$message = Message
Vue.config.productionTip = false

// 添加全局导航守卫
router.beforeEach((to, from, next) => {
  const token = Cookies.get('token')
  if (!token && to.name !== 'login') {
    // 未登录，跳转至登录页面
    next('/login')
  } else if (token && to.name === 'login') {
    next('/home')
  } else {
    next()
  }
})

new Vue({
  router,
  store,
  render: h => h(App),
  created() {
    store.commit('initMenu', router)
  }
}).$mount('#app')
