<template>
  <div div style="padding: 30px">

    <el-form ref="form" :model="form" :rules="rules" :style="bodyStyle"  label-width="150px">
      <el-card shadow="always" style="margin-bottom: 30px;">
        <div slot="header" style="height: 25px">
          <span style="font-weight: bold;font-size: 16px">基本信息</span>
        </div>
        <el-row>
          <el-col :span="12">
            <el-form-item label="党员姓名" prop="memberId">
              <el-input v-model="form.partyMemberName"
                        placeholder="请输入党员姓名" :disabled="true"  />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="党组织名称" prop="partyOrgId">
              <el-input  v-model="form.partyOrgName"
                         placeholder="请输入党组织名称" :disabled="true" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="内容" prop="suggestionsContent">
              <el-input v-model="form.suggestionsContent" :disabled="seeFlag || !addFlag"
                        type="textarea" :autosize="{ minRows: 6, maxRows: 6}"
                        placeholder="请输入内容" />
            </el-form-item>
          </el-col>

        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="创建日期" prop="recordTime">
              <el-date-picker clearable size="small" style="width: 100%" :disabled="seeFlag || !addFlag"
                              v-model="form.recordTime"
                              type="date"
                              value-format="yyyy-MM-dd"
                              placeholder="选择填写日期">
              </el-date-picker>
            </el-form-item>
          </el-col>

        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="附件" >
              <upload-all-file  ref="uploadAllFile" :disabled="seeFlag || !addFlag" />
            </el-form-item>
          </el-col>

        </el-row>
      </el-card>

      <el-card v-if="!addFlag" shadow="always" style="margin-bottom: 30px;">
        <div slot="header" style="height: 25px">
          <span style="font-weight: bold;font-size: 16px">处理信息</span>
        </div>
        <el-table v-loading="loading" :data="logList">
          <el-table-column label="名称" align="center" prop="stepName"/>
          <el-table-column label="处理人" align="center" prop="sysUser.nickName"/>
          <el-table-column label="处理操作" align="center" prop="operResult"/>
          <el-table-column label="处理时间" align="center" prop="operTime" width="180">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.operTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="处理情况" align="center"  width="600" prop="operReason"/>
        </el-table>
      </el-card>
    </el-form>

    <div :style="{textAlign:'center'}">
      <el-button v-show="todoStatus=='0' " type="primary" @click="auditResultOpen">处 理</el-button>
      <el-button v-show="todoStatus=='0' && form.status == '2'" type="primary" @click="chooseAuditUser">转 办</el-button>
      <el-button @click="cancel">关 闭</el-button>
    </div>


    <audit-result-pass ref="auditResult" @ok="submitAudit"/>

    <choose-audit-user-all ref="chooseAuditUser"  @ok="handleSubmit" />

  </div>
</template>

<script>
  import { listSuggestions, getSuggestions, delSuggestions, addSuggestions, updateSuggestions, exportSuggestions } from "@/api/members/suggestions";
  import {getUserProfile} from "@/api/system/user";
  import UploadAllFile from "../../upload/uploadAllFile";
  import {partyOrgTreeselect, getPartyOrg} from "@/api/party/org";
  import selectTree from '../../components/selectTree';
  import ChooseAuditUser from "../../audit/chooseAuditUser";
  import {listTodo, getTodo, delTodo, addTodo, updateTodo, exportTodo} from "@/api/sys/todo";
  import {listLog} from "@/api/sys/log";
  import AuditResultPass from "../../audit/auditResultPass";
  import ChooseAuditUserAll from "../../audit/chooseAuditUserAll";

  export default {
    name: "MemberSuggestions",
    components: {ChooseAuditUserAll, AuditResultPass, UploadAllFile, selectTree,ChooseAuditUser},
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
        // 党员建议/心愿表格数据
        suggestionsList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 是否已处理 Y 是 N 否字典
        statusOptions: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          suggestionsUuid: undefined,
          memberId: undefined,
          partyOrgId: undefined,
          suggestionsContent: undefined,
          recordTime: undefined,
          handleContent: undefined,
          handleMemberId: undefined,
          handleTime: undefined,
          status: undefined,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          suggestionsContent: [
            { required: true, message: "内容不能为空", trigger: "blur" }
          ],
          recordTime: [
            { required: true, message: "创建日期不能为空", trigger: "blur" }
          ],
          handleContent: [
            { required: true, message: "处理情况不能为空", trigger: "blur" }
          ],
          handleTime: [
            { required: true, message: "处理日期不能为空", trigger: "blur" }
          ],
        },
        bodyStyle:{
          overflowY:'auto',
          height: '',
          marginLeft:'2%' ,
          paddingRight:'2%',
        },
        addFlag:false,
        seeFlag:false,
        //组织架构
        partyOrgOptions: [],
        type:undefined,
        logList: [],
        suggestionsUuid:undefined,
        sysLogId:undefined,
        todoStatus: undefined,
      };
    },
    mounted () {
      window.addEventListener('resize', this.getHeight);
    },
    created() {
      this.init();
      this.getDicts("sys_yes_no").then(response => {
        this.statusOptions = response.data;
      });
    },
    methods: {
      init() {
        this.todoId = this.$route.query.todoId;
        getTodo(this.todoId).then(response => {
          if (response.data == undefined) {
            this.$store.dispatch('tagsView/delView', this.$route)
            this.$router.go(-1)
          }
          this.todoStatus = response.data.status
          let params = eval('(' + response.data.urlParams + ')');
          this.sysLogId = params.sysLogId;
          this.suggestionsUuid = params.suggestionsUuid;

        }).then(() => {
          this.reset();
          this.addFlag = false;
          this.seeFlag = true;
          let suggestions = {};
          suggestions.suggestionsUuid = this.suggestionsUuid
          listSuggestions(suggestions).then(response => {
            if(response.rows && response.rows.length>0){
              this.form = response.rows[0];
              this.form.partyMemberName = this.form.partyMember.memberName
              this.form.partyOrgName = this.form.partyOrg.partyOrgName
              this.getLogList();
              this.$refs.uploadAllFile.init(this.form.suggestionsUuid, 'suggestions') ;
            }

          });


        });
      },

      getLogList() {
        this.loading = true;
        listLog({"uuid": this.form.suggestionsUuid}).then(response => {
          this.logList = response.rows;
          this.loading = false;
        });
      },

      /** 对话框自适应高度 */
      getHeight(){
        this.bodyStyle.height=window.innerHeight-281+'px';
      },


      // 是否已处理 Y 是 N 否字典翻译
      statusFormat(row, column) {
        return this.selectDictLabel(this.statusOptions, row.status);
      },
      // 取消按钮
      cancel() {
        this.$store.dispatch('tagsView/delView', this.$route)
        this.$router.go(-1)
      },
      // 表单重置
      reset() {
        this.form = {
          suggestionsId: undefined,
          suggestionsUuid: undefined,
          memberId: undefined,
          partyOrgId: undefined,
          suggestionsContent: undefined,
          recordTime: undefined,
          handleContent: undefined,
          handleMemberId: undefined,
          handleTime: undefined,
          status: undefined,
          delFlag: undefined,
          createBy: undefined,
          createTime: undefined,
          updateBy: undefined,
          updateTime: undefined
        };
        this.resetForm("form");
      },
      auditResultOpen() {
        this.$refs.auditResult.init();
      },
      chooseAuditUser(){
        this.$refs.chooseAuditUser.init()
      },
      submitAudit(form) {
        this.form.params = {}
        this.form.params.state = form.state;
        this.form.params.reason = form.reason;
        this.form.params.sysLogId = this.sysLogId;
        this.form.status = form.state;
        updateSuggestions(this.form).then(response => {
          if (response.code === 200) {
            this.msgSuccess("审批成功");
          } else {
            this.msgError(response.msg);
          }
        }).then(() => {
          updateTodo({"todoId": this.todoId, "status": "1"}).then(response => {
            if (response.code === 200) {
              this.$store.dispatch('tagsView/delView', this.$route)
              this.$router.go(-1)
            } else {
              this.msgError(response.msg);
            }
          });
        });
      },

      handleSubmit(form){
        this.form.params = {}
        this.form.params.auditMemberId = form.auditMemberId;
        this.form.params.operReason = form.reason;
        this.form.params.sysLogId = this.sysLogId;
        this.submitForm(3);
        updateTodo({"todoId": this.todoId, "status": "1"}).then(response => {
          if (response.code === 200) {
            this.$store.dispatch('tagsView/delView', this.$route)
            this.$router.go(-1)
          } else {
            this.msgError(response.msg);
          }
        });
      },
      /** 提交按钮 */
      submitForm: function (status) {
        this.$refs["form"].validate(valid => {
          if (valid) {
            this.form.status = status;
            if (this.form.suggestionsId != undefined) {
              updateSuggestions(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addSuggestions(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("新增成功");
                  this.open = false;
                } else {
                  this.msgError(response.msg);
                }
              });
            }
          }
        });
      },


    }
  };
</script>
