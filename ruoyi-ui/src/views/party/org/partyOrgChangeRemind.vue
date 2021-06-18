<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="100px">
      <el-form-item label="党组织名称" prop="partyOrgName">
        <el-input
          v-model="queryParams.partyOrgName"
          placeholder="请输入党组织名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button
          class="filter-item"
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
        >搜索
        </el-button>
        <el-button
          icon="el-icon-refresh"
          size="mini"
          @click="resetQuery"
        >重置
        </el-button>
      </el-form-item>
    </el-form>

    <el-table
      v-loading="loading"
      :data="partyOrgList"
      row-key="partyOrgId"
      default-expand-all
      :stripe="true"
      :border="true"
      :row-class-name="tableRowClassName"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column label="序号" align="center" type="index"/>
      <el-table-column label="党组织名称" align="left" prop="partyOrgName" width="300"/>
      <el-table-column label="组织类型" align="center" prop="orgType" :formatter="orgTypeFormat"/>
      <el-table-column label="党组织负责人" align="center" prop="partyOrgPost" :formatter="partyPositionFormat"/>
      <el-table-column label="建立时间" align="center" prop="buildTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.buildTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="上次换届时间" align="center" prop="preChangeTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.preChangeTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="下次换届时间" align="center" prop="nextChangeTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.nextChangeTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="距下次换届天数" align="center" :formatter="leftDaysFormat"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="small"
            type="text"
            icon="el-icon-search"
            @click="handleSee(scope.row)"
          >提醒
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  import {listPartyOrg, getPartyOrg, delPartyOrg, addPartyOrg,
    updatePartyOrg, exportPartyOrg,partyOrgTreeselect} from "@/api/party/org";
  import Treeselect from "@riophae/vue-treeselect";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";

  export default {
    name: "PartyOrgChnageRemind",
    components: { Treeselect},
    data() {
      return {
        // 遮罩层
        loading: true,
        // 党组织架构表格数据
        partyOrgList: [],
        // 组织类型字典
        orgTypeOptions: [],
        // 党组织类别字典
        partyOrgTypeOptions: [],
        // 党组织状态字典
        statusOptions: [],
        // 查询参数
        queryParams: {
          parentId: undefined,
          ancestors: undefined,
          partyOrgName: undefined,
          type: undefined,
          buildTime: undefined,
          leader: undefined,
          phone: undefined,
          email: undefined,
          description: undefined,
          orderNum: undefined,
          status: undefined,
        },

      };
    },
    mounted() {

    },
    watch:{
    },
    created() {
      this.getList();
      this.getDicts("org_type").then(response => {
        this.orgTypeOptions = response.data;
      });
      this.getDicts("party_org_type").then(response => {
        this.partyOrgTypeOptions = response.data;
      });
      this.getDicts("sys_normal_disable").then(response => {
        this.statusOptions = response.data;
      });
    },
    methods: {
      leftDaysFormat(row, column){
        if(row.nextChangeTime){
          let days = this.getDaysBetween(this.getNowFormatDate(),row.nextChangeTime);
          return days.toFixed(0) + ' 天';
        }else{
          return '';
        }
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      },
      /** 查询党组织架构列表 */
      getList() {
        this.loading = true;
        listPartyOrg(this.queryParams).then(response => {
          this.partyOrgList = this.handleTree(response.data, "partyOrgId", "parentId");
          this.loading = false;
        });
      },

      // 组织类型字典翻译
      orgTypeFormat(row, column) {
        return this.selectDictLabel(this.orgTypeOptions, row.orgType);
      },
      partyPositionFormat(row, column) {
        var result = "";
        for(let i in row.partyPositionMemberList){
          if(row.partyPositionMemberList[i].partyPositionType =='1'){
            result = row.partyPositionMemberList[i].memberName
          }
        }
        if(result ==""){
          for(let i in row.partyPositionMemberList){
            if(row.partyPositionMemberList[i].partyPositionType =='2'){
              result += row.partyPositionMemberList[i].memberName+" "
            }
          }
        }
        return result;
      },
      // 党组织类别字典翻译
      partyOrgTypeFormat(row, column) {
        return this.selectDictLabel(this.partyOrgTypeOptions, row.partyOrgType);
      },
      // 党组织状态字典翻译
      statusFormat(row, column) {
        return this.selectDictLabel(this.statusOptions, row.status);
      },

      /** 查看按钮操作 */
      handleSee(row) {
        this.reset();
        this.mapOpen = false;
        this.disabled = true;
        this.getTreeselect();
        if (row != undefined) {
          this.form.parentId = row.partyOrgId;
        }
        getPartyOrg(row.partyOrgId).then(response => {
          this.form = response.data;
          if(response.data.leaderMember != undefined){
            this.form.leaderName = response.data.leaderMember
          }
          if(response.data.regionCode != undefined){
            this.form.regionCode = response.data.regionCode.split("-");
          }
          this.open = true;
          this.title = "查看党组织架构";
        });
      },
      /**行颜色*/
      tableRowClassName({row, rowIndex}) {
        if(row.nextChangeTime){
          let days = this.getDaysBetween(this.getNowFormatDate(),row.nextChangeTime).toFixed(0);
          if(days<90){
            return 'danger-row';
          }else if(days>=90 && days<180){
            return 'warn-row';
          }
        }
        return '';
      },
    }
  };
</script>
