<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="活动主题" prop="activityTheme">
        <el-input
          v-model="queryParams.activityTheme"
          placeholder="请输入活动主题"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>


    <el-table v-loading="loading" :data="detailedList" @selection-change="handleSelectionChange">
      <el-table-column label="活动名称" align="center" prop="djActivityPlan.activityTheme"/>
      <el-table-column label="活动类型" align="center" prop="djActivityPlan.activityType"
                       :formatter="activityTypeFormat"/>
      <el-table-column label="活动党组织" align="center" prop="djPartyOrg.partyOrgFullName"/>
      <el-table-column label="负责人" align="center" prop="djPartyMember.memberName"/>
      <el-table-column label="计划开始时间" align="center" prop="planStartTime" >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.planStartTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="计划截止时间" align="center" prop="planEndTime" >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.planEndTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="活动状态" align="center" prop="status"
                       :formatter="detailedStatusFormat"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
         <!-- <el-button
            size="small"
            type="text"
            icon="el-icon-search"
            @click="handleSee(scope.row)"
          >查看
          </el-button>-->
          <el-button
            size="small"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['activity:detailed:edit']"
          >活动管理
          </el-button>
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

    <!-- 添加或修改活动详情对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="80%" append-to-body
               @open="getHeight" :close-on-click-modal="false">
      <div :style="bodyStyle">
        <el-card shadow="always" style="margin-bottom: 30px;">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">活动进度</span>
          </div>
          <el-steps  :active="Number(form.status)-1" finish-status="success">
            <el-step title="完善活动信息"></el-step>
            <el-step title="启动活动"></el-step>
            <el-step title="开始活动"></el-step>
            <el-step title="结束活动"></el-step>
            <el-step title="活动归档"></el-step>
          </el-steps>
        </el-card>
        <el-card shadow="always" style="margin-bottom: 30px;">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">活动信息</span>
          </div>
          <el-form ref="form" :model="form" :rules="rules"  label-width="150px">

            <el-row>
              <el-col :span="24">
                <el-form-item label="活动名称">
                  <el-input :disabled="true"
                            v-model="djActivityPlan.activityTheme" placeholder="请输入活动名称"/>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="活动类型">
                  <el-select :disabled="true"
                             v-model="djActivityPlan.activityType"
                             style="width: 100%"
                             placeholder="请选择党员活动类型">
                    <el-option
                      v-for="dict in activityTypeOptions"
                      :key="dict.dictValue"
                      :label="dict.dictLabel"
                      :value="dict.dictValue"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="负责人" prop="partyMemberName">
                  <el-input :disabled="disabled" v-model="form.partyMemberName" placeholder="请输入负责人">
                    <el-button :disabled="disabled" slot="append" icon="el-icon-search"
                               @click="openMemberChoose"></el-button>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="计划开始日期" prop="planStartTime">
                  <el-date-picker :disabled="true"
                                  clearable size="small"
                                  style="width: 100%"
                                  v-model="form.planStartTime"
                                  type="date"
                                  value-format="yyyy-MM-dd"
                                  placeholder="选择计划召开时间">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="计划截止日期" prop="planEndTime">
                  <el-date-picker :disabled="true"
                                  clearable size="small"
                                  style="width: 100%"
                                  v-model="form.planEndTime"
                                  type="date"
                                  value-format="yyyy-MM-dd"
                                  placeholder="选择计划结束时间">
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>


            <el-row>
              <el-col :span="12">
                <el-form-item label="活动党组织" >
                  <el-input :disabled="true" v-model="djPartyOrg.partyOrgFullName" placeholder="请输入活动地点"/>
                </el-form-item>
              </el-col>
              <el-col :span="12">
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

            <el-row>
              <el-col :span="24">
                <el-form-item label="活动内容" prop="activityContent">
                  <el-input :disabled="disabled" v-model="form.activityContent" type="textarea" :autosize="{ minRows: 3, maxRows: 6}"
                            placeholder="请输入内容"/>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="活动计划召开时间" prop="activityPlanStartTime">
                  <el-date-picker :disabled="disabled"
                                  clearable size="small"
                                  style="width: 100%"
                                  v-model="form.activityPlanStartTime"
                                  type="date"
                                  value-format="yyyy-MM-dd"
                                  placeholder="选择活动计划召开时间">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="活动计划结束时间" prop="activityPlanEndTime">
                  <el-date-picker :disabled="disabled"
                                  clearable size="small"
                                  style="width: 100%"
                                  v-model="form.activityPlanEndTime"
                                  type="date"
                                  value-format="yyyy-MM-dd"
                                  placeholder="选择活动计划结束时间">
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="活动实际开始时间" prop="actualStartTime">
                  <el-date-picker :disabled="disabled"
                                  clearable size="small"
                                  style="width: 100%"
                                  v-model="form.actualStartTime"
                                  type="date"
                                  value-format="yyyy-MM-dd"
                                  placeholder="选择活动实际开始时间">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="活动实际结束时间" prop="actualEndTime">
                  <el-date-picker :disabled="disabled"
                                  clearable size="small"
                                  style="width: 100%"
                                  v-model="form.actualEndTime"
                                  type="date"
                                  value-format="yyyy-MM-dd"
                                  placeholder="选择活动实际结束时间">
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="记录人" prop="recorder">
                  <el-input :disabled="disabled" v-model="form.recorder" placeholder="请输入记录人"/>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="到会指导人员" prop="mentors">
                  <el-input :disabled="disabled" v-model="form.mentors" placeholder="请输入到会指导人员"/>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="主持人" prop="presenter">
                  <el-input :disabled="disabled" v-model="form.presenter" placeholder="请输入主持人"/>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="主讲人及身份" prop="speaker">
                  <el-input :disabled="disabled"v-model="form.speaker" placeholder="请输入主讲人及身份"/>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-card>

        <el-card shadow="always" style="margin-bottom: 30px;">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">活动参与情况</span>
          </div>
          <el-tabs v-model="activeTab">
            <el-tab-pane label="活动参与人" name="1">

            </el-tab-pane>
            <el-tab-pane label="签到情况" name="2">

            </el-tab-pane>
            <el-tab-pane label="建言献策" name="3">

            </el-tab-pane>
            <el-tab-pane label="心得体会" name="4">

            </el-tab-pane>
            <el-tab-pane label="请假记录" name="5">

            </el-tab-pane>

          </el-tabs>
        </el-card>


        <el-card shadow="always" style="margin-bottom: 30px;">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">活动纪要</span>
          </div>

        </el-card>

        <el-card shadow="always" style="margin-bottom: 30px;">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">活动决议</span>
          </div>


        </el-card>

        <el-card shadow="always" style="margin-bottom: 30px;">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">活动图片</span>
          </div>


        </el-card>

        <el-card shadow="always" style="margin-bottom: 30px;">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">活动资料</span>
          </div>


        </el-card>
      </div>


      <div slot="footer" class="dialog-footer" :style="{textAlign:'center'}">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <party-member ref="partyMember" @callbackMember="setMember"/>

  </div>
</template>

<script>
  import {
    listDetailed,
    listDetailedByParam,
    getDetailed,
    delDetailed,
    addDetailed,
    updateDetailed,
    exportDetailed
  } from "@/api/activity/detailed";
  import partyMember from "../../party/org/partyMemberChoose";
  import addressMap from "../../party/org/addressMap";

  export default {
    name: "Detailed",
    components: {
      partyMember, addressMap
    },
    data() {
      return {
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
        // 活动详情表格数据
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
          cycle: undefined,
          partyMemberId: undefined,
          venue: undefined,
          activityContent: undefined,
          planStartTime: undefined,
          planEndTime: undefined,
          activityPlanStartTime: undefined,
          activityPlanEndTime: undefined,
          actualStartTime: undefined,
          actualEndTime: undefined,
          recorder: undefined,
          mentors: undefined,
          presenter: undefined,
          speaker: undefined,
          allowComment: undefined,
          activityVoteId: undefined,
          activityExamId: undefined,
          status: undefined,
        },
        // 表单参数
        form: {},
        djActivityPlan: {},
        djPartyOrg: {},
        // 表单校验
        rules: {
          partyMemberName: [
            {required: true, message: "负责人不能为空", trigger: "blur"}
          ],
          venue: [
            {required: true, message: "活动地点不能为空", trigger: "blur"}
          ],
          activityPlanStartTime: [
            {required: true, message: "活动计划召开时间不能为空", trigger: "blur"}
          ],
          activityPlanEndTime: [
            {required: true, message: "活动计划结束时间不能为空", trigger: "blur"}
          ],
          /*activityContent: [
            {required: true, message: "活动内容不能为空", trigger: "blur"}
          ],
          actualStartTime: [
            {required: true, message: "活动实际开始时间不能为空", trigger: "blur"}
          ],
          actualEndTime: [
            {required: true, message: "活动实际结束时间不能为空", trigger: "blur"}
          ],
          recorder: [
            {required: true, message: "记录人不能为空", trigger: "blur"}
          ],
          mentors: [
            {required: true, message: "到会指导人员不能为空", trigger: "blur"}
          ],
          presenter: [
            {required: true, message: "主持人不能为空", trigger: "blur"}
          ],
          speaker: [
            {required: true, message: "主讲人及身份不能为空", trigger: "blur"}
          ],*/
        },
        bodyStyle: {
          overflowY: 'auto',
          height: '',
          marginLeft: '2%',
          paddingRight: '2%',
        },
        // 党员活动类型字典
        activityTypeOptions: [],
        //活动详情状态
        arrangeStatusOptions: [],
        activeTab: "1",
        disabled:false,
        mapOpen: false,
      };
    },
    mounted() {
      window.addEventListener('resize', this.getHeight);
    },
    created() {
      this.getList();
      this.getDicts("activity_type").then(response => {
        this.activityTypeOptions = response.data;
      });
      this.getDicts("activity_detailed_status").then(response => {
        this.detailedStatusOptions = response.data;
      });
    },
    methods: {
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
        this.$refs.partyMember.queryParams.partyOrgId = this.form.partyOrgId;
      },
      detailedStatusFormat(row, column) {
        return this.selectDictLabel(this.detailedStatusOptions, row.status);
      },
      // 党员活动类型字典翻译
      activityTypeFormat(row, column) {
        return this.selectDictLabel(this.activityTypeOptions, row.djActivityPlan.activityType);
      },
      /** 对话框自适应高度 */
      getHeight() {
        this.bodyStyle.height = window.innerHeight - 281 + 'px';
      },
      /** 查询活动详情列表 */
      getList() {
        this.loading = true;
        listDetailedByParam(this.queryParams).then(response => {
          this.detailedList = response.rows;
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
          activityId: undefined,
          planUuid: undefined,
          cycle: undefined,
          partyMemberId: undefined,
          partyMemberName: undefined,
          venue: undefined,
          activityContent: undefined,
          planStartTime: undefined,
          planEndTime: undefined,
          activityPlanStartTime: undefined,
          activityPlanEndTime: undefined,
          actualStartTime: undefined,
          actualEndTime: undefined,
          recorder: undefined,
          mentors: undefined,
          presenter: undefined,
          speaker: undefined,
          allowComment: undefined,
          activityVoteId: undefined,
          activityExamId: undefined,
          status: "0",
          delFlag: undefined,
          createBy: undefined,
          createTime: undefined,
          updateBy: undefined,
          updateTime: undefined
        };

        this.djActivityPlan = {
          planId: undefined,
          planUuid: undefined,
          activitySources: undefined,
          activityTheme: undefined,
          activityType: undefined,
          conveneCycle: '2',
          cycleStartTime: undefined,
          cycleEndTime: undefined,
          cycleStartYear: undefined,
          cycleStartYearUnit: undefined,
          cycleEndYear: undefined,
          cycleEndYearUnit: undefined,
          actPlanRequirements: undefined,
          planPartyOrgIds: undefined,
          status: "0",
          delFlag: undefined,
          createBy: undefined,
          createTime: undefined,
          updateBy: undefined,
          updateTime: undefined
        };

        this.djPartyOrg = {
          partyOrgId: undefined,
          partyOrgUuid:undefined,
          parentId: undefined,
          ancestors: undefined,
          partyOrgName: undefined,
          partyOrgFullName: undefined,
          orgType: undefined,
          partyOrgType: undefined,
          buildTime: undefined,
          leader: undefined,
          phone: undefined,
          email: undefined,
          address: undefined,
          description: undefined,
          orderNum: undefined,
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
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.activityId)
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.mapOpen = false;
        this.title = "添加活动详情";
      },

      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        this.mapOpen = false;
        const activityId = row.activityId || this.ids
        getDetailed(activityId).then(response => {
          this.form = response.data;
          this.form.partyMemberName = response.data.djPartyMember.memberName
          this.djActivityPlan = response.data.djActivityPlan;
          this.djPartyOrg = response.data.djPartyOrg;
          this.open = true;
          this.title = "活动管理";
        });
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.activityId != undefined) {
              updateDetailed(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addDetailed(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("新增成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            }
          }
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const activityIds = row.activityId || this.ids;
        this.$confirm('是否确认删除活动详情编号为"' + activityIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delDetailed(activityIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有活动详情数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return exportDetailed(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function () {
        });
      }
    }
  };
</script>
