# PageStateWrapper 组件使用说明

## 概述

`PageStateWrapper` 是一个用于页面状态保存与恢复的 Vue 组件，可以自动保存和恢复页面的查询条件、分页信息、表格数据等状态。

## 🚀 快速使用

| 使用场景 | 配置方式 | 代码示例 |
|---------|---------|----------|
| **简单页面**（推荐） | 默认配置 | `<PageStateWrapper page-name="YourPage" :state-config="stateConfig">` |
| **需要状态恢复事件** | `events="restore"` | `<PageStateWrapper events="restore" @state-restored="onRestored">` |
| **需要加载数据事件** | `events="load"` | `<PageStateWrapper events="load" @need-load-data="onLoadData">` |
| **需要所有事件** | `events="both"` | `<PageStateWrapper events="both" @state-restored="onRestored" @need-load-data="onLoadData">` |

### 简单页面（默认配置，推荐）
```vue
<PageStateWrapper page-name="YourPage" :state-config="stateConfig">
  <!-- 页面内容 -->
</PageStateWrapper>
```

### 复杂页面（需要事件处理）
```vue
<PageStateWrapper
    page-name="YourPage"
    :state-config="stateConfig"
    events="both"
    @state-restored="onStateRestored"
    @need-load-data="onNeedLoadData">
  <!-- 页面内容 -->
</PageStateWrapper>
```

## 基本用法

### 1. 导入组件

```vue
import PageStateWrapper from '../components/PageStateWrapper.vue'

export default {
components: {
PageStateWrapper
}
}
```

### 2. 配置状态保存

```javascript
data() {
    return {
        stateConfig: {
            // 查询表单状态
            queryForm: 'queryForm',
            // 分页数据
            pageData: 'pageData',
            // 表格数据
            tableData: 'tableData',
            // 总数
            total: 'total'
        },
        queryForm: { appid: '', startTime: '', endTime: '' },
        pageData: { pageNo: 1, pageSize: 10 },
        tableData: [],
        total: 0
    }
}
```

### 3. 在模板中使用

```vue
<template>
  <PageStateWrapper
      page-name="YourPageName"
      :state-config="stateConfig">

    <!-- 你的页面内容 -->
    <div class="your-page">
      <!-- 页面内容 -->
    </div>

  </PageStateWrapper>
</template>
```

## 配置选项

### 必需属性

- `page-name`: 页面名称，用于标识状态
- `state-config`: 需要保存的状态字段配置

### 可选属性

- `enabled`: 是否启用状态管理，默认为 `true`
- `expire-minutes`: 状态过期时间（分钟），默认为 360 分钟
- `events`: 事件配置，支持多种配置方式（默认为 `false`）

#### events 配置详解

**重要说明：**
- **字符串值**：`events="restore"` 或 `:events="'restore'"` 都可以，效果相同
- **布尔值**：必须使用 `:events="false"` 或 `:events="true"`，不能省略冒号
- **对象值**：必须使用 `:events="{ restore: true, load: false }"`

**为什么布尔值不能省略冒号？**
- `events="false"` 传递的是字符串 `"false"`，在布尔上下文中会被转换为 `true`
- `:events="false"` 传递的是布尔值 `false`，这才是我们想要的结果

```vue
<!-- 1. 禁用所有事件（默认） -->
<PageStateWrapper :events="false">

  <!-- 2. 启用所有事件 -->
  <PageStateWrapper :events="true">

    <!-- 3. 只启用状态恢复事件 -->
    <PageStateWrapper events="restore">
      <!-- 或 -->
      <PageStateWrapper :events="'restore'">

        <!-- 4. 只启用需要加载数据事件 -->
        <PageStateWrapper events="load">
          <!-- 或 -->
          <PageStateWrapper :events="'load'">

            <!-- 5. 精确控制每个事件 -->
            <PageStateWrapper :events="{ restore: true, load: false }">
```

## 事件处理

### 方式 1: 完全禁用事件（推荐用于简单页面）

```vue
<PageStateWrapper
    page-name="SimplePage"
    :state-config="stateConfig">

  <!-- 页面内容 -->

</PageStateWrapper>
```

**优点：**
- 无需实现任何事件处理方法
- 代码更简洁
- 自动处理状态保存和恢复

**适用场景：**
- 只需要保存查询条件和分页信息
- 不需要在状态恢复后执行特殊逻辑
- 页面逻辑相对简单

### 方式 2: 启用事件处理（适用于复杂页面）

```vue
<template>
  <PageStateWrapper
      page-name="ComplexPage"
      :state-config="stateConfig"
      events="both"
      @state-restored="onStateRestored"
      @need-load-data="onNeedLoadData">

    <!-- 页面内容 -->

  </PageStateWrapper>
</template>

<script>
  export default {
    methods: {
      onStateRestored() {
        // 状态恢复后的处理逻辑
        console.log('状态已恢复')
      },

      onNeedLoadData() {
        // 需要重新加载数据的处理逻辑
        this.loadData()
      }
    }
  }
</script>
```

**适用场景：**
- 需要在状态恢复后执行特殊逻辑
- 需要根据状态恢复情况决定是否重新加载数据
- 页面有复杂的初始化逻辑

## 实际使用示例

### 示例 1: AppAlertRecord.vue（简单页面）

```vue
<template>
  <PageStateWrapper
      page-name="AppAlertRecord"
      :state-config="stateConfig">

    <!-- 页面内容 -->

  </PageStateWrapper>
</template>

<script>
  export default {
    data() {
      return {
        stateConfig: {
          ciApp: 'ciApp',
          queryForm: 'queryForm',
          pageData: 'pageData',
          tableData: 'tableData',
          total: 'total'
        }
      }
    }
  }
</script>
```

### 示例 2: OperationLog.vue（简单页面）

```vue
<template>
  <PageStateWrapper
      page-name="OperationLog"
      :state-config="stateConfig">

    <!-- 页面内容 -->

  </PageStateWrapper>
</template>

<script>
  export default {
    data() {
      return {
        stateConfig: {
          queryForm: 'queryForm',
          pageData: 'pageData',
          tableData: 'tableData',
          total: 'total'
        }
      }
    }
  }
</script>
```

### 示例 3: 需要事件处理的复杂页面

```vue
<template>
  <PageStateWrapper
      page-name="ComplexPage"
      :state-config="stateConfig"
      events="both"
      @state-restored="onStateRestored"
      @need-load-data="onNeedLoadData">

    <!-- 页面内容 -->

  </PageStateWrapper>
</template>

<script>
  export default {
    data() {
      return {
        stateConfig: {
          queryForm: 'queryForm',
          pageData: 'pageData',
          tableData: 'tableData',
          total: 'total'
        }
      }
    },
    methods: {
      onStateRestored() {
        // 状态恢复后的处理逻辑
        this.getUsernames()
      },

      onNeedLoadData() {
        // 需要重新加载数据的处理逻辑
        this.getUsernames()
        this.getList()
      }
    }
  }
</script>
```

## 注意事项

1. **状态字段路径**: `state-config` 中的值必须是组件中实际存在的字段路径
2. **数据加载标记**: 组件会自动设置 `isDataLoaded` 标记，确保状态保存的准确性
3. **状态过期**: 默认状态保存 6 小时，可以通过 `expire-minutes` 调整
4. **自动保存**: 状态会在页面销毁和路由离开时自动保存
5. **事件可选**: 两个事件都是可选的，可以根据页面需求选择启用或禁用

## 最佳实践

1. **简单页面**: 使用默认配置，无需添加任何事件相关属性
2. **复杂页面**: 使用 `events="both"` 启用所有事件，或使用 `events="restore"` 等精确控制
3. **状态配置**: 只保存必要的状态字段，避免保存过大的数据
4. **性能考虑**: 对于大数据量的表格，考虑只保存分页信息，不保存完整数据
