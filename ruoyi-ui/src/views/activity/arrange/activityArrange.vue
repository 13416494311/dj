<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="100px">
      <el-form-item label="活动主题" prop="activityTheme">
        <el-input
          v-model="queryParams.activityTheme"
          placeholder="请输入活动主题"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="活动党组织" prop="partyOrgId">
        <select-tree :value="queryParams.partyOrgId"
                     :options="partyOrgOptions"
                     vModel="partyOrgId"
                     @selected="setVModelValue"
                     placeholder="请选择党组织"
        />
      </el-form-item>

      <el-form-item label="活动状态" prop="status">
        <el-select v-model="queryParams.status"
                   clearable
                   placeholder="请选择活动状态">
          <el-option
            v-for="dict in arrangeStatusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          ></el-option>
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!--<el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['activity:arrange:add']"
        >新增
        </el-button>
      </el-col>
    </el-row>-->

    <el-table v-loading="loading" :data="arrangeList" @selection-change="handleSelectionChange">
      <el-table-column label="活动主题" align="center" prop="djActivityPlan.activityTheme"/>
      <el-table-column label="活动类型" align="center" prop="djActivityPlan.activityType"
                       :formatter="activityTypeFormat"/>
      <el-table-column label="召开周期" align="center" prop="djActivityPlan.conveneCycle"
                       :formatter="conveneCycleFormat"/>
      <el-table-column label="周期开始日期" align="center" prop="djActivityPlan.cycleStartTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.djActivityPlan.cycleStartTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="周期结束日期" align="center" prop="djActivityPlan.cycleEndTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.djActivityPlan.cycleEndTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="负责人" align="center" prop="djPartyMember.memberName"/>
      <el-table-column label="活动党组织" align="center" prop="djPartyOrg.partyOrgFullName"/>
      <el-table-column label="活动状态" align="center" prop="status"
                       :formatter="arrangeStatusFormat"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="small"
            type="text"
            icon="el-icon-search"
            @click="handleSee(scope.row)"
          >查看
          </el-button>
          <el-button
            v-if="scope.row.status == '1'"
            size="small"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['activity:arrange:edit']"
          >活动安排
          </el-button>
          <!--<el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['activity:arrange:remove']"
          >删除
          </el-button>-->
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改活动安排对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="90%" append-to-body
               @open="getHeight" :close-on-click-modal="false">

      <div :style="bodyStyle">
        <el-row :gutter="20">
          <el-col :span="6" :xs="24">
            <el-card shadow="always" style="margin-bottom: 30px;">
              <div slot="header" style="height: 25px">
                <span style="font-weight: bold;font-size: 16px">活动计划</span>
              </div>
              <div class="plan-theme"> {{activityPlan.activityTheme}}</div>
              <el-collapse v-model="activeNames">
                <el-collapse-item title="基本信息" name="1">
                  <div>
                    <span class="plan-title">活动类型:</span>
                    <span class="plan-content">{{activityPlan.activityTypeText}}</span>
                  </div>
                  <div>
                    <span class="plan-title">召开周期:</span>
                    <span class="plan-content">{{activityPlan.conveneCycleText}}</span>
                  </div>
                  <div>
                    <span class="plan-title">周期开始:</span>
                    <span class="plan-content">{{activityPlan.cycleStart}}</span>
                  </div>
                  <div>
                    <span class="plan-title">周期结束:</span>
                    <span class="plan-content">{{activityPlan.cycleEnd}}</span>
                  </div>
                </el-collapse-item>
                <el-collapse-item title="计划要求" name="2">
                  <div class="plan-content">{{activityPlan.actPlanRequirements}}</div>
                </el-collapse-item>
                <el-collapse-item title="活动资料" name="3">
                  <div v-for="(item, index) in fileList" :key="index">
                    <el-row>
                      <el-button
                        size="small"
                        type="text"
                        icon="el-icon-paperclip"
                        @click="handleDownload(item)"
                        style="float:left;"
                      >{{item.fileName}}
                      </el-button>
                      <br/>
                    </el-row>
                  </div>
                </el-collapse-item>

              </el-collapse>
            </el-card>
          </el-col>
          <el-col :span="18" :xs="24">
            <el-form ref="form" :model="form" :rules="rules" label-width="150px">
              <el-card shadow="always" style="margin-bottom: 30px;">
                <div slot="header" style="height: 25px">
                  <span style="font-weight: bold;font-size: 16px">活动安排</span>
                </div>
                <el-row>
                  <el-col :span="24">
                    <el-form-item label="负责人" prop="partyMemberId">
                      <el-input :disabled="true" v-model="form.partyMemberName" placeholder="请选择负责人">
                        <el-button :disabled="disabled" slot="append" icon="el-icon-search"
                                   @click="openMemberChoose"></el-button>
                      </el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="24">
                    <el-form-item label="活动地点" prop="venue">
                      <el-input :disabled="disabled" v-model="form.venue" placeholder="请输入活动地点">
                        <el-button :disabled="disabled" slot="append" icon="el-icon-map-location"
                                   @click="openMap"></el-button>
                      </el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="24">
                    <el-form-item label="" prop="description" v-if="mapOpen" style="height: 500px">
                      <address-map ref="addressMap" @callbackAddress="setVenue"></address-map>
                    </el-form-item>
                  </el-col>
                </el-row>
              </el-card>

              <el-card shadow="always" style="margin-bottom: 30px;">
                <div slot="header" style="height: 25px">
                  <span style="font-weight: bold;font-size: 16px">活动参与人</span>
                  <el-button
                    v-if="!disabled"
                    type="primary"
                    icon="el-icon-plus"
                    size="mini"
                    @click="openOrgMemberTransfer('1')"
                    style="float: right;margin-top: -5px"
                  >新增
                  </el-button>
                </div>
                <el-table v-loading="loading" :data="memberList" @selection-change="handleSelectionChange">
                  <el-table-column label="姓名" align="center" prop="djPartyMember.memberName" />
                  <el-table-column label="所属党组织" align="center" prop="djPartyMember.djPartyOrg.partyOrgFullName" />
                  <el-table-column label="联系方式" align="center" prop="djPartyMember.mobile" />
                  <el-table-column  v-if="!disabled" label="操作" align="center" class-name="small-padding fixed-width">
                    <template slot-scope="scope">
                      <el-button
                        size="mini"
                        type="text"
                        icon="el-icon-delete"
                        @click="handleMemberDelete(scope.row)"
                      >删除</el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </el-card>

              <el-card shadow="always" style="margin-bottom: 30px;">
                <div slot="header" style="height: 25px">
                  <span style="font-weight: bold;font-size: 16px">活动列表</span>
                </div>
                <el-table v-loading="activityLoading" :data="detailedList" @selection-change="handleSelectionChange">
                  <el-table-column label="计划起始时间" align="center" prop="planStartTime" width="180">
                    <template slot-scope="scope">
                      <span>{{ parseTime(scope.row.planStartTime, '{y}-{m}-{d}') }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column label="计划截止时间" align="center" prop="planEndTime" width="180">
                    <template slot-scope="scope">
                      <span>{{ parseTime(scope.row.planEndTime, '{y}-{m}-{d}') }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column label="活动名称" align="center" prop="djActivityPlan.activityTheme"/>
                  <!--<el-table-column label="周期" align="center" prop="cycle"/>-->
                  <el-table-column label="活动来源" align="center" prop="djActivityPlan.activitySources"
                                   :formatter="activitySourcesFormat"/>
                </el-table>
              </el-card>
            </el-form>
          </el-col>
        </el-row>
      </div>
      <div slot="footer" class="dialog-footer" :style="{textAlign:'center'}">
        <el-button v-show="!disabled" type="primary" @click="submitForm(1)">保 存</el-button>
        <el-button v-show="!disabled" type="primary" @click="submitForm(2)">发 布</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <party-member ref="partyMember" @callbackMember="setMember"/>
    <member-transfer ref="memberTransfer" @callback="getJoinMemberList"/>
  </div>
</template>

<style>
  .plan-theme {
    font-size: 14px;
    font-weight: bold;
    margin-bottom: 5px
  }

  .plan-title {
    color: #606266;
  }

  .plan-content {
    color: #000000;
  }
</style>

<script>
  import {listArrange, listArrangeByParam,getArrange, delArrange, addArrange, updateArrange, exportArrange} from "@/api/activity/arrange";
  import {downLoadZip} from "@/utils/zipdownload";
  import {listFile} from "@/api/system/file";
  import {delMember,listMember } from "@/api/activity/member";
  import {listDetailed} from "@/api/activity/detailed";
  import partyMember from "../../party/org/partyMemberChoose";
  import addressMap from "../../party/org/addressMap";
  import memberTransfer from "./memberTransfer";
  import { partyOrgTreeselect, getPartyOrg } from "@/api/party/org";
  import selectTree from '../../components/selectTree';

  export default {
    name: "Arrange",
    components: {
      partyMember, addressMap,memberTransfer,selectTree
    },
    data() {
      return {
        activityLoading:true,
        memberLoading:true,
        // 遮罩层
        loading: true,
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 总条数
        total: 0,
        // 活动安排表格数据
        arrangeList: [],
        // 活动参与人表格数据
        memberList: [],
        // 活动列表
        detailedList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          planUuid: undefined,
          partyOrgId: undefined,
          partyMemberId: undefined,
          venue: undefined,
          status: undefined,
          searchValue: undefined,
        },
        partyOrgOptions: [],
        // 表单参数
        form: {},
        // 表单校验
        rules: {

          partyMemberId: [
            {required: true, message: "负责人不能为空", trigger: "blur"}
          ],
          venue: [
            {required: true, message: "活动地点不能为空", trigger: "blur"}
          ],
        },
        bodyStyle: {
          overflowY: 'auto',
          height: '',
          marginLeft: '2%',
          paddingRight: '2%',
        },
        //活动安排状态
        arrangeStatusOptions: [],
        // 党员活动类型字典
        activityTypeOptions: [],
        // 活动来源字典
        activitySourcesOptions: [],
        // 召开周期字典
        conveneCycleOptions: [],
        activeNames: ['1', '2', '3'],
        activityPlan: {
          activityTheme: undefined,
          activityTypeText: undefined,
          conveneCycleText: undefined,
          cycleStart: undefined,
          cycleEnd: undefined,
          actPlanRequirements: undefined,
        },
        fileList: [],
        disabled: false,
        mapOpen: false,
      };
    },
    mounted() {
      window.addEventListener('resize', this.getHeight);
    },
    created() {
      this.getList();
      this.getPartyOrgTreeSelect();
      this.getDicts("activity_type").then(response => {
        this.activityTypeOptions = response.data;
      });
      this.getDicts("convene_cycle").then(response => {
        this.conveneCycleOptions = response.data;
      });
      this.getDicts("activity_arrange_status").then(response => {
        this.arrangeStatusOptions = response.data;
      });
      this.getDicts("activity_sources").then(response => {
        this.activitySourcesOptions = response.data;
      });
    },
    methods: {
      getPartyOrgTreeSelect() {
        partyOrgTreeselect().then(response => {
          this.partyOrgOptions = this.treeInitData(response.data);
        });
      },
      openOrgMemberTransfer(type) {
        this.$refs.memberTransfer.open = true;
        this.$refs.memberTransfer.title = "选择活动参与人";
        this.$refs.memberTransfer.planUuid = this.form.planUuid;
        this.$refs.memberTransfer.partyOrgId = this.form.partyOrgId;
        this.$refs.memberTransfer.type = type;
        this.$refs.memberTransfer.getPartyMemberSelect();
      },
      /**返回地址*/
      setVenue(val) {
        this.form.venue = '';
        this.form.venue = val;
        this.mapOpen = false;
      },
      /**显示地图 获取地址**/
      openMap() {
        this.mapOpen = !this.mapOpen
        this.$forceUpdate();
      },
      setMember(member) {
        this.form.partyMemberId = member.memberId;
        this.form.partyMemberName = member.memberName;
        this.$forceUpdate();
      },
      openMemberChoose() {
        this.$refs.partyMember.open = true;
        this.$refs.partyMember.title = "选择负责人";
        //this.$refs.partyMember.queryParams.partyOrgId = this.form.partyOrgId;
      },
      // 活动来源字典翻译
      activitySourcesFormat(row, column) {
        return this.selectDictLabel(this.activitySourcesOptions, row.djActivityPlan.activitySources);
      },
      arrangeStatusFormat(row, column) {
        return this.selectDictLabel(this.arrangeStatusOptions, row.status);
      },
      // 党员活动类型字典翻译
      activityTypeFormat(row, column) {
        return this.selectDictLabel(this.activityTypeOptions, row.djActivityPlan.activityType);
      },
      // 召开周期字典翻译
      conveneCycleFormat(row, column) {
        return this.selectDictLabel(this.conveneCycleOptions, row.djActivityPlan.conveneCycle);
      },
      /** 对话框自适应高度 */
      getHeight() {
        this.bodyStyle.height = window.innerHeight - 281 + 'px';
      },
      getDetailedList(){
        this.detailedList =[];
        this.activityLoading = true;
        listDetailed({"planUuid":this.form.planUuid,"partyOrgId":this.form.partyOrgId}).then(response => {
          this.detailedList = response.rows;
          this.activityLoading = false;
        });
      },
      getJoinMemberList(){
        this.memberList =[];
          this.memberLoading = true;
        listMember({"planUuid":this.form.planUuid,"partyOrgId":this.form.partyOrgId,"type":"1"}).then(response => {
          this.memberList = response.rows;
          this.memberLoading = false;
        });
      },
      /** 查询活动安排列表 */
      getList() {
        this.arrangeList=[];
        this.loading = true;
        this.queryParams.delFlag='0';
        listArrangeByParam(this.queryParams).then(response => {
          this.arrangeList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.mapOpen = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          id: undefined,
          planUuid: undefined,
          partyOrgId: undefined,
          partyMemberId: undefined,
          partyMemberName: undefined,
          venue: undefined,
          status: "0",
          delFlag: undefined,
          createBy: undefined,
          createTime: undefined,
          updateBy: undefined,
          updateTime: undefined
        };
        this.resetForm("form");
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      //下拉树选择后设置值
      setVModelValue(vModel,val){
        if(val!=null){
          this.queryParams[vModel] =  val;
        }else{
          this.queryParams[vModel] =  undefined;
        }
        this.handleQuery();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.id)
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.mapOpen = false;
        this.title = "添加活动安排";
      },
      /** 修改按钮操作 */
      handleSee(row) {
        this.reset();
        this.mapOpen = false;
        this.disabled = true;
        const id = row.id || this.ids
        getArrange(id).then(response => {
          this.form = response.data;
          if(response.data.djPartyMember != undefined){
            this.form.partyMemberName = response.data.djPartyMember.memberName
          }
          this.setActivityPlan(response.data.djActivityPlan);
          this.open = true;
          this.title = "活动安排";
        }).then(() => {
          this.getFileList();
          this.getJoinMemberList();
          this.getDetailedList();
        });
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        this.mapOpen = false;
        this.disabled = false;
        const id = row.id || this.ids
        getArrange(id).then(response => {
          this.form = response.data;
          if(response.data.djPartyMember != undefined){
            this.form.partyMemberName = response.data.djPartyMember.memberName
          }else{
            this.form.partyMemberName = this.form.djPartyOrg.leaderMember.memberName;
            this.form.partyMemberId = this.form.djPartyOrg.leader;
          }
          this.setActivityPlan(response.data.djActivityPlan);
          this.open = true;
          this.title = "活动安排";
        }).then(() => {

          this.getFileList();
          this.getJoinMemberList();
          this.getDetailedList();
        });
      },
      setActivityPlan(activityPlan) {
        this.activityPlan = activityPlan;
        this.activityPlan.activityTypeText =
          this.selectDictLabel(this.activityTypeOptions, activityPlan.activityType);
        this.activityPlan.conveneCycleText =
          this.selectDictLabel(this.conveneCycleOptions, activityPlan.conveneCycle);
        switch (activityPlan.conveneCycle) {
          case "2":
            this.activityPlan.cycleStart = activityPlan.cycleStartTime;
            this.activityPlan.cycleEnd = activityPlan.cycleEndTime;
            break;
          case "3":
            this.activityPlan.cycleStart =
              activityPlan.cycleStartYear + "年  " + activityPlan.cycleStartMonthUnit + "月";
            this.activityPlan.cycleEnd =
              activityPlan.cycleEndYear + "年  " + activityPlan.cycleEndMonthUnit + "月";
            break;
          case "7":
            this.activityPlan.cycleStart =
              activityPlan.cycleStartYear + "年  " + activityPlan.cycleStartQuarterUnit + "季";
            this.activityPlan.cycleEnd =
              activityPlan.cycleEndYear + "年  " + activityPlan.cycleEndQuarterUnit + "季";
            break;
          case "8":
            this.activityPlan.cycleStart = activityPlan.cycleStartYear + "年";
            this.activityPlan.cycleEnd = activityPlan.cycleEndYear + "年";
            break;
          default :
            break;
        }
      },
      getFileList() {
        this.fileList = [];
        listFile({'uuid': this.form.planUuid}).then(response => {
          this.fileList = response.rows;
        });
      },
      /** 提交按钮 */
      submitForm: function (status) {
        if (this.memberList.length == 0) {
          this.msgInfo("请选择活动参与人!");
          return;
        }
        this.form.status = status;
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.id != undefined) {
              updateArrange(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addArrange(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("新增成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            }
          } else {
            setTimeout(() => {
              var isError = document.getElementsByClassName("is-error");
              isError[0].querySelector('input').focus();
            }, 100);
            return false;
          }
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const ids = row.id || this.ids;
        this.$confirm('是否确认删除活动安排编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delArrange(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有活动安排数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return exportArrange(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function () {
        });
      },
      handleDownload(file) {
        downLoadZip("/system/file/download/" + file.id);
      },
      /** 删除按钮操作 */
      handleMemberDelete(row) {
        const memberIds = row.memberId || this.ids;
        this.$confirm('是否确认删除该参与人?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delMember(memberIds);
        }).then(() => {
          this.getJoinMemberList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
      },
    }
  };
</script>
