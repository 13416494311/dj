<template>
  <el-row :gutter="40" class="panel-group">
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper icon-tree">
          <svg-icon icon-class="tree" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            党组织数
          </div>
          <count-to :start-val="0" :end-val="orgCount" :duration="2600" class="card-panel-num" />
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper icon-peoples">
          <svg-icon icon-class="peoples" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            党员人数
          </div>
          <count-to :start-val="0" :end-val="memberCount" :duration="3000" class="card-panel-num" />
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper icon-date">
          <svg-icon icon-class="date" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            活动计划召开数
          </div>
          <count-to :start-val="0" :end-val="planActivityCount" :duration="3200" class="card-panel-num" />
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper icon-star">
          <svg-icon icon-class="star" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            活动实际召开数
          </div>
          <count-to :start-val="0" :end-val="actualActivityCount" :duration="3600" class="card-panel-num" />
        </div>
      </div>
    </el-col>
  </el-row>
</template>

<script>
import CountTo from 'vue-count-to'
import { getOrgCount } from "@/api/party/org";
import { getMemberCount } from "@/api/party/member";
import { getActivityCount } from "@/api/activity/detailed";

export default {
  components: {
    CountTo
  },
  data() {
    return {
      orgCount: undefined,
      memberCount: undefined,
      planActivityCount: undefined,
      actualActivityCount: undefined,
    }
  },
  created(){
    this.getOrgCount();
    this.getMemberCount();
    this.getPlanActivityCount();
    this.getActualActivityCount();
  },
  methods: {
    getOrgCount(){
      getOrgCount().then((response)=>{
        this.orgCount = response.data;
      })
    },
    getMemberCount(){
      getMemberCount().then((response)=>{
        this.memberCount = response.data;
      })
    },
    getPlanActivityCount(){
      getActivityCount({}).then((response)=>{
        this.planActivityCount = response.data;
      })
    },
    getActualActivityCount(){
      getActivityCount({"status":5}).then((response)=>{
        this.actualActivityCount = response.data;
      })
    },
  }
}
</script>

<style lang="scss" scoped>
.panel-group {
  margin-top: 18px;

  .card-panel-col {
    margin-bottom: 20px;
  }

  .card-panel {
    height: 108px;
    cursor: pointer;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #666;
    background: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
    border-color: rgba(0, 0, 0, .05);

    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
      }

      .icon-tree {
        background: #40c9c6;
      }

      .icon-peoples {
        background: #36a3f7;
      }

      .icon-date {
        background: #34bfa3;
      }

      .icon-star {
        background: #f4516c
      }
    }

    .icon-tree {
      color: #40c9c6;
    }

    .icon-peoples {
      color: #36a3f7;
    }

    .icon-date {
      color: #34bfa3
    }

    .icon-star {
      color: #f4516c;
    }

    .card-panel-icon-wrapper {
      float: left;
      margin: 14px 0 0 14px;
      padding: 16px;
      transition: all 0.38s ease-out;
      border-radius: 6px;
    }

    .card-panel-icon {
      float: left;
      font-size: 48px;
    }

    .card-panel-description {
      float: right;
      font-weight: bold;
      margin: 26px;
      margin-left: 0px;

      .card-panel-text {
        line-height: 18px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 16px;
        margin-bottom: 12px;
      }

      .card-panel-num {
        font-size: 20px;
      }
    }
  }
}

@media (max-width:550px) {
  .card-panel-description {
    display: none;
  }

  .card-panel-icon-wrapper {
    float: none !important;
    width: 100%;
    height: 100%;
    margin: 0 !important;

    .svg-icon {
      display: block;
      margin: 14px auto !important;
      float: none !important;
    }
  }
}
</style>
