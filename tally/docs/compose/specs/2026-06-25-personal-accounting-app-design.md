# [S1] Problem

## [S1.1] Background
个人记账需求：用户需要一个简单的个人记账Android应用，用于记录日常收入和支出，管理自定义分类，并查看分类支出统计。

## [S1.2] Requirements
- 核心功能：基础记账（收入支出记录）
- 分类管理：完全自定义分类
- 统计功能：分类支出统计
- 数据存储：仅本地SQLite存储
- 技术栈：Kotlin + Jetpack Compose + SQLite直接操作 + Material Design 2
- 架构：参考Expenso的MVVM架构

## [S1.3] Success Criteria
- 用户可以方便地记录收入和支出
- 用户可以自定义管理分类
- 用户可以查看分类支出统计
- 应用运行稳定，数据安全存储在本地

# [S2] Solution Overview

## [S2.1] Architecture
采用MVVM（Model-View-ViewModel）架构，分为三层：
1. **数据层**：SQLite数据库操作，直接使用SQLiteOpenHelper
2. **领域层**：业务逻辑处理，数据转换
3. **表现层**：Jetpack Compose UI和ViewModel

## [S2.2] Key Components
1. **数据库组件**：
   - DatabaseHelper：SQLite数据库管理
   - TransactionDao：交易记录数据访问
   - CategoryDao：分类数据访问

2. **业务逻辑组件**：
   - TransactionRepository：交易记录业务逻辑
   - CategoryRepository：分类业务逻辑
   - StatisticsService：统计计算服务

3. **UI组件**：
   - 主界面：交易列表展示
   - 添加/编辑交易界面：记录收入和支出
   - 分类管理界面：自定义分类管理
   - 统计界面：分类支出统计图表

## [S2.3] Data Flow
1. 用户操作UI界面
2. ViewModel处理用户交互，调用Repository
3. Repository执行业务逻辑，调用DAO
4. DAO操作SQLite数据库
5. 数据变化通过StateFlow通知UI更新

# [S3] Database Design

## [S3.1] Tables
1. **transactions表**：
   - id：主键，自增
   - amount：金额（Decimal）
   - type：类型（收入/支出，枚举）
   - category_id：分类ID（外键）
   - date：日期（Date）
   - note：备注（String，可选）
   - created_at：创建时间（DateTime）
   - updated_at：更新时间（DateTime）

2. **categories表**：
   - id：主键，自增
   - name：分类名称（String）
   - icon：图标（String，可选）
   - color：颜色（String，可选）
   - type：类型（收入/支出，枚举）
   - is_default：是否默认分类（Boolean）
   - created_at：创建时间（DateTime）

3. **accounts表**（可选，用于多账户）：
   - id：主键，自增
   - name：账户名称（String）
   - balance：余额（Decimal）
   - icon：图标（String，可选）
   - color：颜色（String，可选）
   - created_at：创建时间（DateTime）

## [S3.2] Relationships
- transactions.category_id -> categories.id
- 可选：transactions.account_id -> accounts.id

# [S4] UI Design

## [S4.1] Main Screen
- 交易列表：按日期分组显示交易记录
- 顶部：总览信息（本月收入、支出、结余）
- 底部导航：主页、统计、设置
- 浮动按钮：添加新交易

## [S4.2] Add/Edit Transaction Screen
- 金额输入：数字键盘
- 类型选择：收入/支出切换
- 分类选择：网格展示所有分类
- 日期选择：日期选择器
- 备注输入：文本输入框
- 保存按钮

## [S4.3] Category Management Screen
- 分类列表：显示所有自定义分类
- 添加分类：名称、图标、颜色选择
- 编辑分类：修改分类信息
- 删除分类：删除自定义分类（默认分类不可删除）

## [S4.4] Statistics Screen
- 分类支出统计：饼图或柱状图展示各分类支出占比
- 时间范围选择：本月、上月、自定义范围
- 详细数据列表：显示各分类具体金额

# [S5] Implementation Plan

## [S5.1] Phase 1: Project Setup
- 创建Android项目
- 配置依赖（Jetpack Compose、Material Design 2）
- 设置项目结构

## [S5.2] Phase 2: Database Layer
- 实现SQLiteOpenHelper
- 创建数据库表
- 实现DAO层

## [S5.3] Phase 3: Business Logic
- 实现Repository层
- 实现统计计算服务
- 实现数据验证逻辑

## [S5.4] Phase 4: UI Layer
- 实现主界面
- 实现添加/编辑交易界面
- 实现分类管理界面
- 实现统计界面

## [S5.5] Phase 5: Testing & Polish
- 单元测试
- 集成测试
- UI测试
- 性能优化

# [S6] Technical Details

## [S6.1] Dependencies
- Kotlin
- Jetpack Compose
- Material Design 2
- SQLite（Android内置）
- StateFlow/SharedFlow
- ViewModel
- Navigation Compose

## [S6.2] Package Structure
```
com.example.personalaccounting
├── data/
│   ├── db/
│   │   ├── DatabaseHelper
│   │   ├── TransactionDao
│   │   ├── CategoryDao
│   │   └── AccountDao
│   └── repository/
│       ├── TransactionRepository
│       ├── CategoryRepository
│       └── AccountRepository
├── domain/
│   ├── model/
│   │   ├── Transaction
│   │   ├── Category
│   │   └── Account
│   └── service/
│       └── StatisticsService
├── ui/
│   ├── screens/
│   │   ├── home/
│   │   ├── addedit/
│   │   ├── category/
│   │   └── statistics/
│   ├── components/
│   └── navigation/
└── utils/
    └── Extensions
```

## [S6.3] Testing Strategy
- 单元测试：Repository、Service层
- 集成测试：数据库操作
- UI测试：Compose组件测试

# [S7] Risks & Mitigations

## [S7.1] Technical Risks
1. **SQLite性能问题**：大量数据时可能变慢
   - 缓解：添加索引，优化查询
   
2. **数据一致性问题**：并发操作可能导致数据不一致
   - 缓解：使用事务，确保数据一致性

## [S7.2] Scope Risks
1. **功能蔓延**：需求不断增加导致项目复杂
   - 缓解：严格遵循最小可行产品原则

2. **时间估算不足**：低估开发时间
   - 缓解：分阶段开发，优先实现核心功能

# [S8] Future Enhancements

## [S8.1] Potential Features
1. 多账户管理
2. 数据导出/导入
3. 云同步
4. 预算管理
5. 重复交易
6. 标签系统

## [S8.2] Technical Improvements
1. 迁移到Room数据库
2. 添加依赖注入（Hilt）
3. 实现离线优先架构
4. 添加单元测试覆盖率

# [S9] Conclusion

本设计文档详细描述了基于Expenso架构的个人记账Android应用设计方案。采用MVVM架构，使用Kotlin + Jetpack Compose + SQLite直接操作 + Material Design 2技术栈，实现基础记账、自定义分类管理和分类支出统计功能。

设计方案已通过用户批准，可以进入实现阶段。