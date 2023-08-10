import Vue from 'vue'
import Vuex from 'vuex'
import tab from './tab'
import menu from './menu'

Vue.use(Vuex)

// 创建vuex实例
const store = new Vuex.Store({
    modules: {
        tab,
        menu
    }
})

export default store