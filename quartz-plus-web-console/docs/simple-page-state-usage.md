# 页面状态管理 - 简便方案

## 🚀 新的简便方案

使用 `PageStateWrapper` 组件，页面组件只需要很少的配置就能实现状态管理！

## ✨ 主要优势

- **代码量减少 80%** - 从原来的 50+ 行代码减少到 10 行以内
- **配置化** - 只需要配置需要保存的字段，无需写保存/恢复逻辑
- **自动处理** - 自动保存、恢复、过期检查，无需手动管理
- **零侵入** - 不影响原有业务逻辑，只是包装了一层

## 📝 使用方法

### 1. 引入组件

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
      // 状态配置 - 只需要配置需要保存的字段
      stateConfig: {
        queryForm: 'queryForm',        // 查询条件
        pageData: 'pageData',          // 分页信息
        tableData: 'tableData',        // 表格数据
        total: 'total',                // 总记录数
        selectedRows: 'selectedRows'   // 选中的行
      },
      isDataLoaded: false,            // 必须：标记数据是否已加载
      // ... 其他数据
    }
  },
  methods: {
    // 状态恢复后的回调（可选）
    onStateRestored() {
      // 状态已恢复，可以做一些额外处理
    },
    
    // 需要加载数据的回调（可选）
    onNeedLoadData() {
      // 没有保存的状态，需要重新请求数据
      this.loadData()
    }
  }
}
</script>
```

### 2. 配置说明

#### stateConfig 格式
```javascript
stateConfig: {
  // key: 保存时的字段名
  // value: 组件中的字段路径（支持嵌套路径）
  queryForm: 'queryForm',           // 直接字段
  userInfo: 'user.info',            // 嵌套字段
  tableData: 'tableData',           // 数组字段
  settings: 'config.settings'       // 深层嵌套
}
```

#### 支持的字段类型
- 基本类型：String, Number, Boolean
- 对象：Object, Array
- 嵌套对象：支持任意深度的嵌套

### 3. 完整示例

```vue
<template>
  <PageStateWrapper 
    page-name="UserManage"
    :state-config="stateConfig"
    @state-restored="onStateRestored"
    @need-load-data="onNeedLoadData"
  >
    <div class="user-manage">
      <!-- 查询表单 -->
      <el-form :model="queryForm" :inline="true">
        <el-form-item>
          <el-input v-model="queryForm.username" placeholder="用户名"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button @click="search">查询</el-button>
        </el-form-item>
      </el-form>
      
      <!-- 数据表格 -->
      <el-table :data="tableData">
        <!-- 表格列 -->
      </el-table>
      
      <!-- 分页 -->
      <el-pagination 
        :current-page="pageData.pageNo"
        :page-size="pageData.pageSize"
        :total="total"
        @current-change="handlePageChange"
      />
    </div>
  </PageStateWrapper>
</template>

<script>
import PageStateWrapper from '../components/PageStateWrapper.vue'

export default {
  name: 'UserManage',
  components: { PageStateWrapper },
  
  data() {
    return {
      // 状态配置 - 只需要这一行配置！
      stateConfig: {
        queryForm: 'queryForm',
        pageData: 'pageData',
        tableData: 'tableData',
        total: 'total',
        selectedRows: 'selectedRows'
      },
      
      // 业务数据
      queryForm: { username: '', status: '' },
      pageData: { pageNo: 1, pageSize: 10 },
      tableData: [],
      total: 0,
      selectedRows: [],
      isDataLoaded: false
    }
  },
  
  methods: {
    search() {
      this.loadData()
    },
    
    loadData() {
      // 你的数据加载逻辑
      // 成功后设置 this.isDataLoaded = true
    },
    
    handlePageChange(page) {
      this.pageData.pageNo = page
      this.loadData()
    },
    
    // 可选的回调方法
    onStateRestored() {
      console.log('状态已恢复')
    },
    
    onNeedLoadData() {
      this.loadData()
    }
  }
}
</script>
```

## 🔧 高级配置

### 自定义过期时间
```vue
<PageStateWrapper 
  page-name="YourPage"
  :state-config="stateConfig"
  :expire-minutes="60"  <!-- 1小时后过期 -->
>
```

### 禁用状态管理
```vue
<PageStateWrapper 
  page-name="YourPage"
  :state-config="stateConfig"
  :enabled="false"  <!-- 禁用状态管理 -->
>
```

### 支持复杂字段路径
```javascript
stateConfig: {
  // 支持任意深度的嵌套路径
  userProfile: 'user.profile.basic',
  permissions: 'auth.permissions.list',
  tableConfig: 'table.config.columns'
}
```

## 📊 对比效果

| 方案 | 代码行数 | 配置复杂度 | 维护成本 |
|------|----------|------------|----------|
| 原始方案 | 50+ 行 | 高 | 高 |
| **新方案** | **10 行** | **低** | **低** |

## 🎯 适用场景

- ✅ 列表页面（查询条件、分页、数据）
- ✅ 表单页面（表单数据、验证状态）
- ✅ 配置页面（用户设置、偏好）
- ✅ 任何需要保持状态的页面

## 🚨 注意事项

1. **必须添加 `isDataLoaded` 标记**
2. **字段路径必须正确**，建议使用简单路径
3. **只保存必要的状态**，避免内存占用过大
4. **状态有过期时间**，默认30分钟

现在为任何页面添加状态管理，只需要几行配置即可！🎉
