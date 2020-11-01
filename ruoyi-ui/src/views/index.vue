<template>
  <div class="dashboard-editor-container" style="padding-top: 10px;">
    <panel-group />
    <el-row :gutter="20">
      <el-col :span="12" :xs="24">
        <el-card shadow="always" style="margin-bottom: 20px; height: 330px;overflow-y:hidden ">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">待办事项</span>
            <router-link to="/sysTodo/index">
              <el-button
                type="text"
                icon="el-icon-plus"
                size="small"
                @click=""
                style="float: right;margin-top: -5px"
              >更多
              </el-button>
            </router-link>
          </div>
          <todo-index ref="todoIndex"></todo-index>
        </el-card>
      </el-col>
      <el-col :span="12" :xs="24">
        <el-card shadow="always" style="margin-bottom: 20px;height: 330px;overflow-y:hidden">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">通知公告</span>
            <router-link to="/office/notice/1">
              <el-button
                type="text"
                icon="el-icon-plus"
                size="small"
                @click=""
                style="float: right;margin-top: -5px"
              >更多
              </el-button>
            </router-link>
          </div>
          <notice-table noticeType="1"></notice-table>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="12" :xs="24">
        <el-card shadow="always" style="margin-bottom: 20px;height: 280px;overflow-y:hidden">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">制度办法</span>
            <router-link to="/office/notice/2">
              <el-button
                type="text"
                icon="el-icon-plus"
                size="small"
                @click=""
                style="float: right;margin-top: -5px"
              >更多
              </el-button>
            </router-link>
          </div>
          <notice-table noticeType="2"></notice-table>
        </el-card>
      </el-col>
      <el-col :span="12" :xs="24">
        <el-card shadow="always" style="margin-bottom: 20px;height: 280px;overflow-y:hidden">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">党建风采</span>
            <router-link to="/office/notice/3">
              <el-button
                type="text"
                icon="el-icon-plus"
                size="small"
                @click=""
                style="float: right;margin-top: -5px"
              >更多
              </el-button>
            </router-link>
          </div>
          <notice-table noticeType="3"></notice-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import PanelGroup from './dashboard/PanelGroup'
import TodoIndex from './sys/todoIndex/todoIndex'
import NoticeTable from './system/notice/noticeTable'

export default {
  name: 'Index',
  components: {
    PanelGroup,
    TodoIndex,
    NoticeTable,
  },
  data() {
    return {
    }
  },
  created(){

  },
  methods: {


  }
}
</script>

<style lang="scss" scoped>
  .chart-title{
    line-height: 18px;
    color: rgba(0,0,0,0.45);
    font-size: 16px;
    margin-bottom: 12px;
    font-weight: bold;
  }
.dashboard-editor-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
  position: relative;

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
}

@media (max-width:1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}
</style>
