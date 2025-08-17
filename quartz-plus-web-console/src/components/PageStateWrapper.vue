<template>
  <div class="page-state-wrapper">
    <slot></slot>
  </div>
</template>

<script>
import { mapActions, mapGetters } from 'vuex'

export default {
  name: 'PageStateWrapper',
  props: {
    // 页面名称，用于标识状态
    pageName: {
      type: String,
      required: true
    },
    // 需要保存的状态字段配置
    stateConfig: {
      type: Object,
      required: true,
      // 格式: { queryForm: 'queryForm', tableData: 'tableData', ... }
      // key: 保存时的字段名, value: 组件中的字段路径
    },
    // 是否启用状态管理
    enabled: {
      type: Boolean,
      default: true
    },
    // 状态过期时间（分钟）
    expireMinutes: {
      type: Number,
      default: 360
    },
    // 事件配置，支持多种配置方式
    events: {
      type: [String, Object, Boolean],
      default: false,
      validator: function (value) {
        if (typeof value === 'boolean') return true
        if (typeof value === 'string') return ['restore', 'load', 'both'].includes(value)
        if (typeof value === 'object') {
          return Object.keys(value).every(key => ['restore', 'load'].includes(key))
        }
        return false
      }
    }
  },

  computed: {
    ...mapGetters('pageState', ['getPageState']),
    
    // 解析事件配置
    eventConfig() {
      if (this.events === false) {
        return { restore: false, load: false }
      }
      
      if (this.events === true) {
        return { restore: true, load: true }
      }
      
      if (typeof this.events === 'string') {
        switch (this.events) {
          case 'restore':
            return { restore: true, load: false }
          case 'load':
            return { restore: false, load: true }
          case 'both':
            return { restore: true, load: true }
          default:
            return { restore: false, load: false }
        }
      }
      
      if (typeof this.events === 'object') {
        return {
          restore: !!this.events.restore,
          load: !!this.events.load
        }
      }
      
      return { restore: false, load: false }
    }
  },

  methods: {
    ...mapActions('pageState', ['savePageState']),

    // 自动保存状态
    autoSaveState() {
      if (!this.enabled) return

      const pageState = {}
      let hasData = false

      // 根据配置自动收集状态
      Object.entries(this.stateConfig).forEach(([key, path]) => {
        const value = this.getNestedValue(this.$parent, path)
        if (value !== undefined && value !== null) {
          pageState[key] = this.deepClone(value)
          hasData = true
        }
      })

      // 如果有数据且有isDataLoaded标记，则保存
      if (hasData && this.$parent.isDataLoaded) {
        this.savePageState({
          pageName: this.pageName,
          pageState: {
            ...pageState,
            timestamp: Date.now()
          }
        })
      }
    },

    // 自动恢复状态
    autoRestoreState() {
      if (!this.enabled) return false

      const savedState = this.getPageState(this.pageName)
      if (!savedState) return false

      // 检查是否过期
      if (this.expireMinutes > 0) {
        const expireTime = this.expireMinutes * 60 * 1000
        if (Date.now() - savedState.timestamp > expireTime) {
          return false
        }
      }

      // 根据配置自动恢复状态
      Object.entries(this.stateConfig).forEach(([key, path]) => {
        if (savedState[key] !== undefined) {
          this.setNestedValue(this.$parent, path, this.deepClone(savedState[key]))
        }
      })

      // 设置数据已加载标记
      if (this.$parent.isDataLoaded !== undefined) {
        this.$parent.isDataLoaded = true
      }

      return true
    },

    // 获取嵌套对象的值
    getNestedValue(obj, path) {
      return path.split('.').reduce((current, key) => {
        return current && current[key] !== undefined ? current[key] : undefined
      }, obj)
    },

    // 设置嵌套对象的值
    setNestedValue(obj, path, value) {
      const keys = path.split('.')
      const lastKey = keys.pop()
      const target = keys.reduce((current, key) => {
        if (!current[key]) {
          current[key] = {}
        }
        return current[key]
      }, obj)
      target[lastKey] = value
    },

    // 深拷贝
    deepClone(obj) {
      if (obj === null || typeof obj !== 'object') return obj
      if (obj instanceof Date) return new Date(obj.getTime())
      if (obj instanceof Array) return obj.map(item => this.deepClone(item))
      if (typeof obj === 'object') {
        const cloned = {}
        for (const key in obj) {
          if (Object.prototype.hasOwnProperty.call(obj, key)) {
            cloned[key] = this.deepClone(obj[key])
          }
        }
        return cloned
      }
    }
  },

  mounted() {
    // 自动恢复状态
    if (this.autoRestoreState()) {
      // 触发恢复事件（如果启用且父组件监听了该事件）
      if (this.eventConfig.restore) {
        this.$emit('state-restored')
      }
    } else {
      // 触发需要加载数据事件（如果启用且父组件监听了该事件）
      if (this.eventConfig.load) {
        this.$emit('need-load-data')
      }
    }
  },

  beforeDestroy() {
    // 自动保存状态
    this.autoSaveState()
  },

  beforeRouteLeave(to, from, next) {
    // 路由离开时保存状态
    this.autoSaveState()
    next()
  }
}
</script>

<style scoped>
.page-state-wrapper {
  height: 100%;
  width: 100%;
}
</style>
