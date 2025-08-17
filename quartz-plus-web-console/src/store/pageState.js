const state = {
    // 保存各个页面的状态
    pageStates: {}
}

const mutations = {
    // 保存页面状态
    SAVE_PAGE_STATE(state, { pageName, pageState }) {
        state.pageStates[pageName] = {
            ...pageState,
            timestamp: Date.now()
        }
    },
    
    // 清除页面状态
    CLEAR_PAGE_STATE(state, pageName) {
        if (pageName) {
            delete state.pageStates[pageName]
        } else {
            state.pageStates = {}
        }
    }
}

const actions = {
    // 保存页面状态
    savePageState({ commit }, { pageName, pageState }) {
        commit('SAVE_PAGE_STATE', { pageName, pageState })
    },
    
    // 获取页面状态
    getPageState({ state }, pageName) {
        const pageState = state.pageStates[pageName]
        // 如果状态存在且不超过30分钟，则返回状态
        if (pageState && (Date.now() - pageState.timestamp) < 30 * 60 * 1000) {
            return pageState
        }
        return null
    },
    
    // 清除页面状态
    clearPageState({ commit }, pageName) {
        commit('CLEAR_PAGE_STATE', pageName)
    }
}

const getters = {
    // 获取页面状态
    getPageState: (state) => (pageName) => {
        const pageState = state.pageStates[pageName]
        if (pageState && (Date.now() - pageState.timestamp) < 30 * 60 * 1000) {
            return pageState
        }
        return null
    }
}

export default {
    namespaced: true,
    state,
    mutations,
    actions,
    getters
}
