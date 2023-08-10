export default {
    state: {
        tabsList: [
            {
                path: '/',
                name: 'home',
                label: '首页',
                icon: 's-home',
                url: 'Home/Home'
            }
        ]
    },
    mutations: {
        addTab(state, payload) {
            if (payload.name !== 'home') {
                const index = state.tabsList.findIndex(item => item.name === payload.name)
                if (index === -1) {
                    state.tabsList.push(payload)
                }
            }
        },
        removeTab(state, payload) {
            // console.log(payload, 'removeTab')
            const index = state.tabsList.findIndex(item => item.name === payload.name)
            state.tabsList.splice(index, 1)
        },
    }
}