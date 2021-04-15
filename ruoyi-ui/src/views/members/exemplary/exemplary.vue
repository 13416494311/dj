<template>
  <div class="app-container">
    <el-form v-if="!see":model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="党员姓名" prop="memberName">
        <el-input
          v-model="queryParams.memberName"
          placeholder="请输入党员姓名"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="党组织" prop="partyOrgId">
        <select-tree :value="queryParams.partyOrgId"
                     :options="partyOrgOptions"
                     vModel="partyOrgId"
                     @selected="setVModelValue"
                     placeholder="请选择党组织"
        />
      </el-form-item>
      <el-form-item label="模范事项" prop="exemplaryTheme">
        <el-input
          v-model="queryParams.exemplaryTheme"
          placeholder="请输入模范事项"
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

    <el-row v-if="!see":gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['members:exemplary:add']"
        >新增
        </el-button>
      </el-col>
      <!--      <el-col :span="1.5">-->
      <!--        <el-button-->
      <!--          type="success"-->
      <!--          icon="el-icon-edit"-->
      <!--          size="mini"-->
      <!--          :disabled="single"-->
      <!--          @click="handleUpdate"-->
      <!--          v-hasPermi="['members:exemplary:edit']"-->
      <!--        >修改-->
      <!--        </el-button>-->
      <!--      </el-col>-->
      <!--      <el-col :span="1.5">-->
      <!--        <el-button-->
      <!--          type="danger"-->
      <!--          icon="el-icon-delete"-->
      <!--          size="mini"-->
      <!--          :disabled="multiple"-->
      <!--          @click="handleDelete"-->
      <!--          v-hasPermi="['members:exemplary:remove']"-->
      <!--        >删除-->
      <!--        </el-button>-->
      <!--      </el-col>-->
      <!--      <el-col :span="1.5">-->
      <!--        <el-button-->
      <!--          type="warning"-->
      <!--          icon="el-icon-download"-->
      <!--          size="mini"-->
      <!--          @click="handleExport"-->
      <!--          v-hasPermi="['members:exemplary:export']"-->
      <!--        >导出-->
      <!--        </el-button>-->
      <!--      </el-col>-->
    </el-row>

    <el-table v-loading="loading" :data="exemplaryList"
              :stripe="true" :border="true"  @selection-change="handleSelectionChange">
      <el-table-column label="序号" align="center" type="index"/>
      <el-table-column v-if="!see"label="党员姓名" align="center" prop="djPartyMember.memberName" />
      <el-table-column v-if="!see"label="党组织名称" align="center" prop="djPartyOrg.partyOrgFullName"  />
      <el-table-column label="记录日期" align="center" prop="recordTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.recordTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="模范事项" align="center" prop="exemplaryTheme"/>
      <el-table-column v-if="see" label="模范描述" align="center" prop="helpRecord"/>
      <el-table-column v-if="!see" label="状态" align="center" prop="auditState" :formatter="auditStateFormat"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">

        <template slot-scope="scope">
          <el-button
            size="small"
            type="text"
            icon="el-icon-view"
            @click="handleSee(scope.row)"
          >查看
          </el-button>
          <el-button
            size="small"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-if="!see && user.userId == scope.row.createBy  && (scope.row.auditState =='1'|| scope.row.auditState =='4')"
            v-hasPermi="['members:exemplary:edit']"
          >修改
          </el-button>
          <el-button
            size="small"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-if="!see && user.userId == scope.row.createBy  && scope.row.auditState =='1'"
            v-hasPermi="['members:exemplary:remove']"
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

    <!-- 添加或修改先锋模范对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="80%" append-to-body
               @open="getHeight" :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" :style="bodyStyle" label-width="100px">

        <el-card shadow="always" style="margin-bottom: 30px;">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">基本信息</span>
          </div>

          <el-row>
            <el-col :span="12">
              <el-form-item  label="党员姓名" prop="memberName">
                <el-input :disabled="true" v-model="form.memberName" placeholder="请选择负责人">
                  <el-button  :disabled="disabled" slot="append" icon="el-icon-search"
                              @click="openMemberChoose" ></el-button>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="党组织" prop="partyOrgId">
                <el-input  v-model="form.partyOrgName" placeholder="" :disabled="true" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="12">
              <el-form-item label="模范事项" prop="exemplaryTheme">
                <el-input :disabled="disabled" v-model="form.exemplaryTheme" placeholder="请输入模范事项"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="记录日期" prop="recordTime">
                <el-date-picker :disabled="disabled" clearable size="small"
                                style="width: 100%"
                                v-model="form.recordTime"
                                type="date"
                                value-format="yyyy-MM-dd"
                                placeholder="选择记录日期">
                </el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>


          <el-row>
            <el-col :span="24">
              <el-form-item label="模范描述" prop="helpRecord">
                <el-input :disabled="disabled" v-model="form.helpRecord" type="textarea" :autosize="{ minRows: 10, maxRows: 10}"
                          placeholder="请输入内容"/>
              </el-form-item>
            </el-col>

          </el-row>

            <el-row>
              <el-col :span="24">
                <el-form-item  label="附件">
                  <upload-all-file ref="uploadAllFile" :disabled="disabled"/>
                </el-form-item>
              </el-col>
            </el-row>
        </el-card>

      </el-form>

      <div slot="footer" class="dialog-footer" :style="{textAlign:'center'}">
        <el-button v-show="!disabled" type="primary" @click="submitForm(1)">保 存</el-button>
        <el-button v-show="!disabled" type="primary" @click="chooseAuditUser">提 交</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <party-member ref="partyMember" @callbackMember="setMember"/>
    <choose-audit-user ref="chooseAuditUser"  @ok="handleSubmit" />
  </div>
</template>

<script>
  import {
    listExemplary,
    getExemplary,
    delExemplary,
    addExemplary,
    updateExemplary,
    exportExemplary
  } from "@/api/members/exemplary";

  import partyMember from "../../party/org/partyMemberChoose";
  import addressMap from "../../party/org/addressMap";
  import {partyOrgTreeselect, getPartyOrg} from "@/api/party/org";
  import selectTree from '../../components/selectTree';
  import {getUserProfile} from "@/api/system/user";
  import {getPartyMember} from "../../../api/party/member";
  import {listFile, upload, delFile} from "@/api/system/file";
  import {downLoadZip} from "@/utils/zipdownload";
  import ElImageViewer from 'element-ui/packages/image/src/image-viewer'
  import ChooseAuditUser from "../../audit/chooseAuditUser";
  import { listLog } from "@/api/sys/log";
  import UploadAllFile from "../../upload/uploadAllFile";

  export default {
    name: "Exemplary",
    components: {
      UploadAllFile,
      partyMember, addressMap, selectTree, ElImageViewer,ChooseAuditUser
    },
    props: {
      see: {
        type: Boolean,
        default: () => {
          return false
        }
      },
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
        // 先锋模范表格数据
        exemplaryList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          exemplaryUuid: undefined,
          memberId: undefined,
          memberName: undefined,
          partyOrgId: undefined,
          partyOrgName: undefined,
          recordTime: undefined,
          exemplaryTheme: undefined,
          helpRecord: undefined,
          auditState: undefined,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          memberName: [
            {required: true, message: "名字不能为空", trigger: "blur"}
          ],
          recordTime: [
            {required: true, message: "记录时间不能为空", trigger: "blur"}
          ],
          exemplaryTheme: [
            {required: true, message: "模范事项不能为空", trigger: "blur"}
          ],
          helpRecord: [
            {required: true, message: "模范描述不能为空", trigger: "blur"}
          ],
        },
        bodyStyle: {
          overflowY: 'auto',
          height: '',
          marginLeft: '2%',
          paddingRight: '2%',
        },
        //组织架构
        partyOrgOptions: [],
        fileList: [],
        defaultFilePicUrl: undefined,
        srcList: [],
        dialogImageUrl: '',
        dialogVisible: false,
        //uuid
        exemplaryUuid: undefined,
        //审批状态
        auditStateOptions: [],
        user: {},
        disabled: false,

      };
    },
    mounted() {
      window.addEventListener('resize', this.getHeight);
    },
    created() {
      this.getUser();
      this.getList();
      //组织架构树
      this.getPartyOrgTreeSelect();
      //审批状态
      this.getDicts("audit_state").then(response => {
        this.auditStateOptions = response.data;
      });
    },
    methods: {
      init(memberId){
        this.queryParams.memberId = memberId
        this.getList();
      },
      getUser() {
        getUserProfile().then(response => {
          this.user = response.data;
        });
      },
      /** 对话框自适应高度 */
      getHeight() {
        this.bodyStyle.height = window.innerHeight - 281 + 'px';
      },
      /** 查询先锋模范列表 */
      getList() {
        this.loading = true;
        listExemplary(this.queryParams).then(response => {
          this.exemplaryList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          exemplaryId: undefined,
          exemplaryUuid: undefined,
          memberId: undefined,
          memberName: undefined,
          partyOrgId: undefined,
          partyOrgName: undefined,
          recordTime: undefined,
          exemplaryTheme: undefined,
          helpRecord: undefined,
          auditState: undefined,
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
        this.ids = selection.map(item => item.exemplaryId)
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      showHandleUpdate(row){
        let showFlag = false;
        if(this.user && this.user.userId == row.createBy){
          showFlag = true;
        }
        return showFlag ;
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.disabled = false;
        this.open = true;
        this.title = "添加先锋模范";
        this.form.exemplaryUuid = this.uuid();
        this.$nextTick(function () {
          this.$refs.uploadAllFile.init(this.form.exemplaryUuid, 'exemplary') ;
        })
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const exemplaryId = row.exemplaryId || this.ids
        this.disabled = false;
        getExemplary(exemplaryId).then(response => {
          this.form = response.data;
          this.form.partyOrgName = this.form.djPartyOrg.partyOrgName;
          this.form.memberName = this.form.djPartyMember.memberName;
          this.open = true;
          this.title = "修改先锋模范";
        }).then(()=>{
          this.$refs.uploadAllFile.init(this.form.exemplaryUuid, 'exemplary') ;
        });
      },
      /** 查看按钮操作 */
      handleSee(row) {
        this.reset();
        const exemplaryId = row.exemplaryId || this.ids
        this.disabled = true;
        getExemplary(exemplaryId).then(response => {
          this.form = response.data;
          this.form.partyOrgName = this.form.djPartyOrg.partyOrgName;
          this.form.memberName = this.form.djPartyMember.memberName;
          this.open = true;
          this.title = "查看先锋模范";
        }).then(()=>{
          this.$refs.uploadAllFile.init(this.form.exemplaryUuid, 'exemplary') ;
        });
      },
      handleSubmit(form){
        this.form.params = {}
        this.form.params.auditUserId = form.auditUserId;
        this.form.params.operReason = form.reason;
        this.submitForm(2);
      },
      /** 提交按钮 */
      submitForm: function (status) {
        this.$refs["form"].validate(valid => {
          if (valid) {
            this.form.auditState = status;
            if (this.form.exemplaryId != undefined) {
              updateExemplary(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("操作成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addExemplary(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("操作成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            }
          }else{
            setTimeout(() => {
              var isError = document.getElementsByClassName("is-error");
              isError[0].querySelector('input').focus();
            }, 100);
            return false;
          }
        });
      },
      chooseAuditUser(){
        this.$refs.chooseAuditUser.init(10)
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const exemplaryIds = row.exemplaryId || this.ids;
        this.$confirm('是否确认删除先锋模范编号为"' + exemplaryIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delExemplary(exemplaryIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有先锋模范数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return exportExemplary(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function () {
        });
      },
      //下拉树选择后设置值
      setVModelValue(vModel, val) {
        if (val != null) {
          this.queryParams[vModel] = val;
        } else {
          this.queryParams[vModel] = undefined;
        }
        this.handleQuery();
      },
      //获取组织架构树
      getPartyOrgTreeSelect() {
        partyOrgTreeselect().then(response => {
          this.partyOrgOptions = this.treeInitData(response.data);
        });
      },
      //选择党员
      setMember(member) {
        this.form.memberId = member.memberId;
        this.form.memberName = member.memberName;
        getPartyMember(member.memberId).then(response => {
          let partyMember = response.data;
          this.form.partyOrgName = partyMember.djPartyOrg.partyOrgName;
          this.form.partyOrgId = partyMember.partyOrgId;
          this.$forceUpdate();
        });
      },
      openMemberChoose() {
        this.$refs.partyMember.open = true;
        this.$refs.partyMember.title = "选择党员";
        this.$refs.partyMember.queryParams.memberType ="all";
      },
      //审批状态
      auditStateFormat(row, column) {
        return this.selectDictLabel(this.auditStateOptions, row.auditState);
      },



    }
  };
</script>
