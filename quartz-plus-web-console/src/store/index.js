import Vue from 'vue'
import Vuex from 'vuex'
import tab from './tab'
import menu from './menu'
import pageState from './pageState'

Vue.use(Vuex)

// 创建vuex实例
const store = new Vuex.Store({
    modules: {
        tab,
        menu,
        pageState
    }
})

export default store