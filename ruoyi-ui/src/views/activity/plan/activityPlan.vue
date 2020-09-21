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
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['activity:plan:add']"
        >新增</el-button>
      </el-col>
    </el-row>
    <el-table v-loading="loading" :data="planList" @selection-change="handleSelectionChange">
      <el-table-column label="活动主题" align="center" prop="activityTheme"/>
      <el-table-column label="活动类型" align="center" prop="activityType" :formatter="activityTypeFormat"/>
      <el-table-column label="召开周期" align="center" prop="conveneCycle" :formatter="conveneCycleFormat"/>
      <el-table-column label="周期开始日期" align="center" prop="cycleStartTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.cycleStartTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="周期结束日期" align="center" prop="cycleEndTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.cycleEndTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="计划状态" align="center" prop="status" :formatter="planStatusFormat"/>
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
            v-hasPermi="['activity:plan:edit']"
          >修改
          </el-button>
          <el-button
            v-if="scope.row.status == '1'"
            size="small"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['activity:plan:remove']"
          >删除
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

    <!-- 添加或修改活动计划对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="90%" append-to-body
               @open="getHeight" :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" :style="bodyStyle" label-width="150px">
        <el-card shadow="always" style="margin-bottom: 30px;">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">活动计划</span>
          </div>
          <el-row>
            <el-col :span="24">
              <el-form-item label="活动主题" prop="activityTheme">
                <el-input :disabled="disabled"
                          v-model="form.activityTheme" placeholder="请输入活动主题"/>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="12">
              <el-form-item label="活动类型" prop="activityType">
                <el-select :disabled="disabled"
                           v-model="form.activityType"
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
              <el-form-item label="召开周期" prop="conveneCycle">
                <el-select :disabled="disabled"
                           v-model="form.conveneCycle"
                           @change="conveneCycleChange"
                           style="width: 100%" placeholder="请选择召开周期">
                  <el-option
                    v-for="dict in conveneCycleOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row v-if="weekShow">
            <el-col :span="12">
              <el-form-item label="周期开始日期" prop="cycleStartTime"
                            :rules="[{required: weekRequired,message: `周期开始日期不能为空`,trigger: 'blur'},
                          {validator: checkCycleTime, trigger: 'blur'}]">
                <el-date-picker :disabled="disabled"
                                clearable size="small" style="width: 100%"
                                v-model="form.cycleStartTime"
                                type="date"
                                value-format="yyyy-MM-dd"
                                :picker-options="beforeTimeOption"
                                placeholder="选择周期开始日期">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="周期结束日期" prop="cycleEndTime"
                            :rules="[{required: weekRequired,message: `周期结束日期不能为空`,trigger: 'blur'},
                          {validator: checkCycleTime, trigger: 'blur'}]">
                <el-date-picker :disabled="disabled"
                                clearable size="small" style="width: 100%"
                                v-model="form.cycleEndTime"
                                type="date"
                                value-format="yyyy-MM-dd"
                                :picker-options="beforeTimeOption"
                                placeholder="选择周期结束日期">
                </el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row v-if="yearShow">
            <el-col :span="12">
              <el-form-item label="周期开始" prop="cycleStartYear"
                            :rules="[{required: yearRequired,message: `请选择周期开始年份`,trigger: 'change'},
                          {validator: checkCycleYear, trigger: 'change'}]">
                <el-select :disabled="disabled"
                           v-model="form.cycleStartYear"
                           style="width: 100%" placeholder="请选择周期开始年份">
                  <el-option
                    v-for="dict in cycleYearOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="周期结束" prop="cycleEndYear"
                            :rules="[{required: yearRequired,message: `请选择周期结束年份`,trigger: 'change'},
                          {validator: checkCycleYear, trigger: 'change'}]">
                <el-select :disabled="disabled"
                           v-model="form.cycleEndYear"
                           style="width: 100%" placeholder="请选择周期结束年份">
                  <el-option
                    v-for="dict in cycleYearOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row v-if="monthShow">
            <el-col :span="12">
              <el-form-item label="" prop="cycleStartMonthUnit"
                            :rules="[{required: monthRequired,message: `请选择周期月份`,trigger: 'change'},
                          {validator: checkCycleMonth, trigger: 'change'}]">
                <el-select :disabled="disabled"
                           v-model="form.cycleStartMonthUnit"
                           style="width: 100%">
                  <el-option
                    v-for="dict in cycleYearMonthUnitOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="" prop="cycleEndMonthUnit"
                            :rules="[{required: monthRequired,message: `请请选择周期月份`,trigger: 'blur'},
                          {validator: checkCycleMonth, trigger: 'change'}]">
                <el-select :disabled="disabled"
                           v-model="form.cycleEndMonthUnit"
                           style="width: 100%">
                  <el-option
                    v-for="dict in cycleYearMonthUnitOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row v-if="quarterShow">
            <el-col :span="12">
              <el-form-item label="" prop="cycleStartQuarterUnit"
                            :rules="[{required: quarterRequired,message: `请选择周期开始季度`,trigger: 'blur'},
                          {validator: checkCycleQuarter, trigger: 'change'}]">
                <el-select :disabled="disabled"
                           v-model="form.cycleStartQuarterUnit"
                           style="width: 100%">
                  <el-option
                    v-for="dict in cycleYearQuarterUnitOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="" prop="cycleEndQuarterUnit"
                            :rules="[{required: quarterRequired,message: `请选择周期结束季度`,trigger: 'blur'},
                          {validator: checkCycleQuarter, trigger: 'change'}]">
                <el-select :disabled="disabled"
                           v-model="form.cycleEndQuarterUnit"
                           style="width: 100%">
                  <el-option
                    v-for="dict in cycleYearQuarterUnitOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="24">
              <el-form-item label="活动计划要求" prop="actPlanRequirements">
                <el-input :disabled="disabled"
                          v-model="form.actPlanRequirements" type="textarea" :autosize="{ minRows: 4, maxRows: 4}"
                          placeholder="请输入计划要求"/>
              </el-form-item>
            </el-col>
          </el-row>
        </el-card>


        <el-card shadow="always" style="margin-bottom: 30px;">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">计划参与党组织</span>
            <el-button
              v-if="!disabled && this.activitySources =='3'"
              type="primary"
              icon="el-icon-plus"
              size="mini"
              @click="openOrgTransfer"
              style="float: right;margin-top: -5px"
            >新增
            </el-button>
          </div>

          <el-table v-loading="orgLoading" :data="arrangeList">
            <el-table-column label="党组织名称" align="center" prop="djPartyOrg.partyOrgFullName"/>
            <el-table-column label="党组织类型" align="center" prop="djPartyOrg.orgType"
                             :formatter="orgTypeFormat"/>
            <el-table-column label="建立时间" align="center" prop="djPartyOrg.buildTime" width="180">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.djPartyOrg.buildTime, '{y}-{m}-{d}') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="党组织简介" align="center" prop="djPartyOrg.description"/>
            <el-table-column  v-if="!disabled && this.form.activitySources =='3'" label="操作" align="center" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleArrangeDelete(scope.row)"
                >删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>

        <el-card shadow="always" style="margin-bottom: 30px;">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">活动资料</span>
          </div>
          <el-upload
            action="#"
            list-type="picture-card"
            :file-list="fileList"
            :http-request="uploadFile"
            :class="{hide:disabled}"
            accept="image/*,.doc,.docx,.xls,.xlsx,.pdf,.ppt,.zip,.txt">
            <i slot="default" class="el-icon-plus"></i>
            <div slot="file" slot-scope="{file}" style="display: inline">
              <div style="height: 70%">
              <img v-if="'jpeg,jpg,gif,png'.indexOf(file.name.split('.')[1]) != -1"
                   class="el-upload-list__item-thumbnail"

                   :src="file.url" :alt="file.name"/>
              <img v-else
                   class="el-upload-list__item-thumbnail"
                   :src="defaultFilePicUrl" :alt="file.name"/>
              <span class="el-upload-list__item-actions">
                    <span v-if="'jpeg,jpg,gif,png'.indexOf(file.name.split('.')[1]) != -1"
                          class="el-upload-list__item-preview"
                          @click="handlePictureCardPreview(file)">
                       <i class="el-icon-zoom-in"></i>
                    </span>
                    <span
                      class="el-upload-list__item-delete"
                      @click="handleDownload(file)">
                      <i class="el-icon-download"></i>
                    </span>
                    <span
                      v-if="!disabled"
                      class="el-upload-list__item-delete"
                      @click="handleRemove(file)">
                      <i class="el-icon-delete"></i>
                    </span>
                  </span>
              </div>
              <div style="height: 30%">
                <span class="file-name">{{file.name}}</span>
              </div>
            </div>
          </el-upload>
          <el-dialog :visible.sync="dialogVisible"
                     append-to-body
                     :close-on-click-modal="false">
            <img width="100%" :src="dialogImageUrl" alt="">
          </el-dialog>
        </el-card>

      </el-form>
      <div slot="footer" class="dialog-footer" :style="{textAlign:'center'}">
        <el-button v-show="!disabled  " type="primary" @click="submitForm(1)">保 存</el-button>
        <el-button v-show="!disabled && this.activitySources =='1'" type="primary"
                   @click="submitForm(2)">备 案</el-button>
        <el-button v-show="!disabled&& (this.activitySources =='2'||this.activitySources =='3')" type="primary"
                   @click="submitForm(2)">发 布</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <org-transfer ref="orgTransfer" @callbackOrg="getJoinOrgList"/>

  </div>
</template>

<script>
  import {listPlan, getPlan, delPlan, addPlan, updatePlan, exportPlan} from "@/api/activity/plan";
  import {listFile, upload, delFile} from "@/api/system/file";
  import {getPartyOrgs} from "@/api/party/org";
  import {downLoadZip} from "@/utils/zipdownload";
  import OrgTransfer from "./orgTransfer";
  import {addArrange,listArrange, delArrange} from "@/api/activity/arrange";
  import { getUserProfile } from "@/api/system/user";

  export default {
    name: "Plan",
    components: {OrgTransfer},
    data() {
      return {
        // 遮罩层
        loading: true,
        orgLoading: true,
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 总条数
        total: 0,
        // 活动计划表格数据
        planList: [],
        arrangeList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 活动来源字典
        activitySourcesOptions: [],
        // 党员活动类型字典
        activityTypeOptions: [],
        // 召开周期字典
        conveneCycleOptions: [],
        // 活动计划状态
        planStatusOptions:[],
        // 周期年份字典
        cycleYearOptions: [],
        //当前月份
        currentMonth: undefined,
        // 周期 （月份）字典
        cycleYearMonthUnitOptions: [],
        orgTypeOptions: [],
        // 周期 （季度）字典
        cycleYearQuarterUnitOptions: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          planUuid: undefined,
          activitySources: undefined,
          activityTheme: undefined,
          activityType: undefined,
          conveneCycle: undefined,
          cycleStartTime: undefined,
          cycleEndTime: undefined,
          cycleStartYear: undefined,
          cycleEndYear: undefined,
          cycleStartMonthUnit: undefined,
          cycleEndMonthUnit: undefined,
          cycleStartQuarterUnit: undefined,
          cycleEndQuarterUnit: undefined,
          cycleEndYearUnit: undefined,
          actPlanRequirements: undefined,
          status: undefined,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          activityTheme: [
            {required: true, message: "活动主题不能为空", trigger: "blur"}
          ],
          activityType: [
            {required: true, message: "党员活动类型不能为空", trigger: "blur"}
          ],
          conveneCycle: [
            {required: true, message: "召开周期不能为空", trigger: "blur"}
          ],
          actPlanRequirements: [
            {required: true, message: "活动计划要求不能为空", trigger: "blur"}
          ],
        },
        bodyStyle: {
          overflowY: 'auto',
          height: '',
          marginLeft: '2%',
          paddingRight: '2%',
        },
        dialogImageUrl: '',
        dialogVisible: false,
        disabled: false,
        fileList: [],
        defaultFilePicUrl: undefined,

        weekShow: true,
        weekRequired: true,
        monthShow: false,
        monthRequired: false,
        quarterShow: false,
        quarterRequired: false,
        yearShow: false,
        yearRequired: false,
        beforeTimeOption: {
          disabledDate(date) {
            //return date.getTime() <= Date.now() - 24 * 60 * 60 * 1000;
            return true;
          }
        },
        user: {},
        activitySources:undefined,

      };
    },
    mounted() {
      window.addEventListener('resize', this.getHeight);
    },
    created() {
      this.getUser();
      this.defaultFilePicUrl = require("@/assets/image/file.png");
      this.getList();
      this.getDicts("activity_sources").then(response => {
        this.activitySourcesOptions = response.data;
      });
      this.getDicts("org_type").then(response => {
        this.orgTypeOptions = response.data;
      });
      this.getDicts("activity_type").then(response => {
        this.activityTypeOptions = response.data;
      });
      this.getDicts("convene_cycle").then(response => {
        this.conveneCycleOptions = response.data;
      });
      this.getDicts("activity_plan_status").then(response => {
        this.planStatusOptions = response.data;
      });
      this.setCycleYearOptions();
      this.getDicts("quarter_type").then(response => {
        this.cycleYearQuarterUnitOptions = response.data;
      });
      this.getDicts("month_type").then(response => {
        this.cycleYearMonthUnitOptions = response.data;
      });
      //this.getActivitySourcesByPath();
    },
    methods: {
      orgTypeFormat(row, column) {
        return this.selectDictLabel(this.orgTypeOptions, row.djPartyOrg.orgType);
      },
      openOrgTransfer() {
        this.$refs.orgTransfer.open = true;
        this.$refs.orgTransfer.title = "选择计划参与党组织";
        this.$refs.orgTransfer.planUuid = this.form.planUuid;
        /*if(this.user.djPartyMember){
          this.$refs.orgTransfer.getPartyOrgSelect(this.user.djPartyMember.djPartyOrg.partyOrgId);
        }else{
          this.$refs.orgTransfer.getPartyOrgSelect(1);
        }*/
        this.$refs.orgTransfer.getPartyOrgSelect(1);

      },
      //周期开始季度校验
      checkCycleQuarter(rule, value, callback) {
        if (this.form.cycleStartYearUnit != undefined && this.form.cycleEndYearUnit != undefined) {
          if (Number(this.form.cycleStartYearUnit) > Number(this.form.cycleEndYearUnit)) {
            callback(new Error("周期结束季度应在周期开始季度之后!"));
          } else {
            callback()
          }
        } else {
          callback()
        }
      },
      //周期开始月份校验
      checkCycleMonth(rule, value, callback) {
        if (this.form.cycleStartYearUnit != undefined && this.form.cycleEndYearUnit != undefined) {
          if (Number(this.form.cycleStartYearUnit) > Number(this.form.cycleEndYearUnit)) {
            callback(new Error("周期结束月份应在周期开始月份之后!"));
          } else {
            callback()
          }
        } else {
          callback()
        }
      },
      //周期开始年份校验
      checkCycleYear(rule, value, callback) {
        if (this.form.cycleStartYear != undefined && this.form.cycleEndYear != undefined) {
          if (Number(this.form.cycleStartYear) > Number(this.form.cycleEndYear)) {
            callback(new Error("周期结束年份应在周期开始年份之后!"));
          } else {
            callback()
          }
        } else {
          callback()
        }
      },
      //周期开始日期校验
      checkCycleTime(rule, value, callback) {
        if (this.form.cycleStartTime != undefined && this.form.cycleEndTime != undefined) {
          let startDate = Date.parse(this.form.cycleStartTime);
          let endDate = Date.parse(this.form.cycleEndTime);
          if (startDate > endDate) {
            callback(new Error("周期结束日期应在周期开始日期之后!"));
          } else {
            callback()
          }
        } else {
          callback()
        }
      },
      //选择不同周期
      conveneCycleChange() {
        switch (this.form.conveneCycle) {
          case "2":
            this.weekShow = true;
            this.weekRequired = true;
            this.monthShow = false;
            this.monthRequired = false;
            this.quarterShow = false;
            this.quarterRequired = false;
            this.yearShow = false;
            this.yearRequired = false;
            break;
          case "3":
            this.weekShow = false;
            this.weekRequired = false;
            this.monthShow = true;
            this.monthRequired = true;
            this.quarterShow = false;
            this.quarterRequired = false;
            this.yearShow = true;
            this.yearRequired = true;
            break;
          case "7":
            this.weekShow = false;
            this.weekRequired = false;
            this.monthShow = false;
            this.monthRequired = false;
            this.quarterShow = true;
            this.quarterRequired = true;
            this.yearShow = true;
            this.yearRequired = true;
            break;
          case "8":
            this.weekShow = false;
            this.weekRequired = false;
            this.monthShow = false;
            this.monthRequired = false;
            this.quarterShow = false;
            this.quarterRequired = false;
            this.yearShow = true;
            this.yearRequired = true;
            break;
        }
        this.$refs.form.clearValidate()
      },
      setCycleYearOptions() {
        let year = new Date().getFullYear();
        this.currentMonth = new Date().getMonth();
        for (let i = 0; i < 5; i++) {
          let cycleYearOption = {};
          cycleYearOption.dictValue = Number(year) + Number(i);
          cycleYearOption.dictLabel = Number(year) + Number(i) + "年";
          this.cycleYearOptions.push(cycleYearOption);
        }
      },
      /**附件上传*/
      uploadFile(file) {
        const loading = this.$loading({
          lock: true,
          text: '上传中……',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });
        let formData = new FormData();
        formData.append("uuid", this.form.planUuid);
        formData.append("file", file.file);
        formData.append("fileType", "activityPlan");
        upload(formData).then(response => {
          if (response.code === 200) {
            this.getFileList();
            loading.close();
            this.msgSuccess("上传成功！")
          } else {
            loading.close();
            this.msgError(response.msg);
          }
        }).catch(function (err) {
          loading.close();
        });
      },
      handleRemove(file) {
        //console.log(file);
        delFile(file.uid).then(response => {
          this.msgSuccess(response.msg);
          this.getFileList();
        });
      },
      handlePictureCardPreview(file) {
        this.dialogImageUrl = file.url;
        this.dialogVisible = true;
      },
      handleDownload(file) {
        //console.log(file);
        downLoadZip("/system/file/download/" + file.uid);

      },
      getFileList() {
        this.fileList = [];
        listFile({'uuid': this.form.planUuid}).then(response => {
          let files = response.rows;
          for (let i = 0; i < files.length; i++) {
            let file = {};
            file.name = files[i].fileName;
            file.url = process.env.VUE_APP_BASE_API + files[i].filePath;
            file.uid = files[i].id;
            this.fileList.push(file);
          }
        });
      },
      /** 对话框自适应高度 */
      getHeight() {
        this.bodyStyle.height = window.innerHeight - 281 + 'px';
      },
      /** 查询活动计划列表 */
      getList() {
        this.loading = true;
        this.getActivitySourcesByPath();
        this.queryParams.activitySources = this.activitySources;
        listPlan(this.queryParams).then(response => {
          this.planList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      getJoinOrgList() {
        this.orgLoading = true;
        listArrange({"planUuid": this.form.planUuid}).then(response => {
          this.arrangeList = response.rows;
          this.orgLoading = false;
        });
      },
      /** 删除按钮操作 */
      handleArrangeDelete(row) {
        const ids = row.id || this.ids;
        this.$confirm('是否确认删除该数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delArrange(ids);
        }).then(() => {
          this.getJoinOrgList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
      // 活动来源字典翻译
      activitySourcesFormat(row, column) {
        return this.selectDictLabel(this.activitySourcesOptions, row.activitySources);
      },
      // 党员活动类型字典翻译
      activityTypeFormat(row, column) {
        return this.selectDictLabel(this.activityTypeOptions, row.activityType);
      },
      // 召开周期字典翻译
      conveneCycleFormat(row, column) {
        return this.selectDictLabel(this.conveneCycleOptions, row.conveneCycle);
      },
      planStatusFormat(row, column) {
        if(row.activitySources =='3'){
          return "已发布";
        }else{
          return this.selectDictLabel(this.planStatusOptions, row.status);
        }
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
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
        this.resetForm("form");
        this.show =true ;
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
        this.ids = selection.map(item => item.planId)
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      getActivitySourcesByPath(){
        let path = this.$route.path;
        switch (path) {
          case "/activity/plan/1" :
            this.activitySources = "1";
            break;
          case "/activity/plan/2" :
            this.activitySources = "2";
            break;
          case "/activity/plan/3" :
            this.activitySources = "3";
            break;
          default:
            break;
        }
      },
      /** 新增按钮操作 */
      handleAdd(activitySources) {
        this.reset();
        this.disabled = false;
        this.form.planUuid = this.uuid();
        this.changeByActivitySources();

        this.fileList = [];
        this.arrangeList = [];
        this.open = true;
        this.orgLoading = false;
        this.title = "添加活动计划";
      },
      changeByActivitySources(){
        switch (this.activitySources) {
          case "1" :
            this.addOrgArrange();
            break;
          case "2" :
            this.addOrgArrange();
            break;
          case "3" :
            break;
          default:
            break;
        }
      },
      addOrgArrange(){
        if(this.user.djPartyMember){
          addArrange({"planUuid":this.form.planUuid,
            "partyOrgId":this.user.djPartyMember.partyOrgId,"status":"1"})
            .then(response => {
              if (response.code === 200) {
                this.getJoinOrgList();
              } else {
                this.msgError(response.msg);
              }
            });
        }
      },
      /** 按钮操作 */
      handleSee(row) {
        this.reset();
        this.disabled = true;
        const planId = row.planId || this.ids
        getPlan(planId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "查看活动计划";
        }).then(() => {
          this.conveneCycleChange();
          this.getJoinOrgList();
          this.getFileList();
        });
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        this.disabled = false;
        const planId = row.planId || this.ids
        getPlan(planId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改活动计划";
        }).then(() => {
          this.conveneCycleChange();
          this.getJoinOrgList();
          this.getFileList();
        });
      },
      /** 提交按钮 */
      submitForm: function (status) {
        if (this.arrangeList.length == 0) {
          this.msgInfo("请选择计划参与党组织!");
          return;
        }
        this.form.status = status;
        this.$refs["form"].validate(valid => {
          if (valid) {
            this.form.activitySources = this.activitySources;
            if (this.form.planId != undefined) {
              updatePlan(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addPlan(this.form).then(response => {
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
        const planIds = row.planId || this.ids;
        this.$confirm('是否确认删除活动计划编号为"' + planIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delPlan(planIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有活动计划数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return exportPlan(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function () {
        });
      },
      getUser() {
        getUserProfile().then(response => {
          this.user = response.data;
        });
      }
    }
  };
</script>
