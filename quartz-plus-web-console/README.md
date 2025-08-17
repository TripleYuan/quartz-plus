# Quartz Plus Web Console

 Quartz 任务调度管理平台

## 功能特性

- 🚀 任务管理：创建、编辑、删除定时任务
- 📊 实例管理：管理多个 Quartz 实例
- 📝 执行记录：查看任务执行历史
- 👥 用户权限：用户和角色管理
- 📋 操作日志：系统操作审计
- 💾 **页面状态保持**：切换页面时自动保存和恢复状态

## 页面状态管理功能

### ✨ 主要特性

- **自动状态保存**: 在页面离开时自动保存状态
- **智能状态恢复**: 在页面重新进入时自动恢复之前的状态
- **状态过期管理**: 状态保存30分钟后自动过期，避免数据过时
- **极简配置**: 页面组件只需要几行配置即可使用

### 🚀 快速使用

为任何页面添加状态管理，只需要：

```vue
<template>
  <PageStateWrapper 
    page-name="YourPageName"
    :state-config="stateConfig"
    @state-restored="onStateRestored"
    @need-load-data="onNeedLoadData"
  >
    <!-- 你的页面内容 -->
  </PageStateWrapper>
</template>

<script>
import PageStateWrapper from '../components/PageStateWrapper.vue'

export default {
  components: { PageStateWrapper },
  data() {
    return {
      // 状态配置 - 只需要这一行配置！
      stateConfig: {
        queryForm: 'queryForm',
        pageData: 'pageData',
        tableData: 'tableData',
        total: 'total'
      },
      isDataLoaded: false  // 必须添加这个标记
    }
  }
}
</script>
```

### 📚 详细文档

查看 [页面状态管理使用说明](docs/simple-page-state-usage.md) 了解更多用法。

## 技术栈

- **前端**: Vue.js 2.x + Element UI
- **状态管理**: Vuex
- **路由**: Vue Router
- **构建工具**: Vue CLI

## 开发

```bash
# 安装依赖
npm install

# 启动开发服务器
npm run serve

# 构建生产版本
npm run build
```

## 项目结构

```
src/
├── api/              # API 接口
├── components/       # 公共组件
│   └── PageStateWrapper.vue  # 页面状态管理组件
├── store/            # Vuex 状态管理
│   └── pageState.js  # 页面状态管理模块
├── views/            # 页面组件
└── mixins/           # 混入
```

## 许可证

MIT License