<template>
  <div class="app-container">

    <el-form ref="form" :model="form" :rules="rules"  label-width="150px">
      <el-card shadow="always" style="margin-bottom: 30px;">
        <div slot="header" style="height: 25px">
          <span style="font-weight: bold;font-size: 16px">基本信息</span>
        </div>

        <el-row>
          <el-col :span="12">
            <el-form-item  label="党员姓名" prop="memberName">
              <el-input :disabled="disabled" v-model="form.memberName" placeholder="请选择负责人">
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
              <el-input :disabled="disabled" v-model="form.helpRecord" type="textarea" :autosize="{ minRows: 10, maxRows: 11}"
                        placeholder="请输入内容"/>
            </el-form-item>
          </el-col>

        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item  label="附件">
              <el-upload
                action="#"
                list-type="picture-card"
                :file-list="fileList"
                multiple
                :http-request="uploadFile"
                :class="{hide:disabled}"
                class="upload"
                accept="image/*,.doc,.docx,.xls,.xlsx,.pdf,.ppt,.zip,.txt">
                <i slot="default" class="el-icon-plus"></i>
                <div slot="file" slot-scope="{file}" style="display: inline">
                  <div class="upload-file">
                    <img v-if="'jpeg,jpg,gif,png,JPEG,JPG,GIF,PNG'.indexOf(file.name.split('.')[1]) != -1"
                         class="el-upload-list__item-thumbnail"

                         :src="file.url" :alt="file.name"/>
                    <img v-else
                         class="el-upload-list__item-thumbnail"
                         :src="defaultFilePicUrl" :alt="file.name"/>
                    <span class="el-upload-list__item-actions">
                    <span v-if="'jpeg,jpg,gif,png,JPEG,JPG,GIF,PNG'.indexOf(file.name.split('.')[1]) != -1"
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
                  <div class="upload-text">
                    <el-tooltip class="item" effect="dark" :content="setFileNameTip(file)" placement="top">
                      <span>{{file.name}}</span>
                    </el-tooltip>
                  </div>
                </div>
              </el-upload>
              <el-image-viewer
                v-if="dialogVisible"
                :on-close="closeViewer"
                :url-list="srcList"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

      <el-card shadow="always" style="margin-bottom: 30px;">
        <div slot="header" style="height: 25px">
          <span style="font-weight: bold;font-size: 16px">审批记录</span>
        </div>
        <el-table v-loading="loading" :data="logList">
          <el-table-column label="操作名称" align="center" prop="stepName"/>
          <el-table-column label="操作人" align="center" prop="sysUser.nickName"/>
          <el-table-column label="操作" align="center" prop="operResult"/>
          <el-table-column label="操作时间" align="center" prop="operTime" width="180">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.operTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="备注" align="center" prop="operReason"/>
        </el-table>

      </el-card>

    </el-form>



    <div slot="footer" class="dialog-footer" :style="{textAlign:'center'}">
      <el-button v-if="todoStatus=='0'" type="primary" @click="auditResultOpen">审 核</el-button>
      <el-button @click="cancel">关 闭</el-button>
    </div>

    <audit-result ref="auditResult" @ok="submitAudit"/>
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
  import {listFile, upload, delFile} from "@/api/system/file";
  import {downLoadZip} from "@/utils/zipdownload";
  import ElImageViewer from 'element-ui/packages/image/src/image-viewer'

  import ChooseAuditUser from "../../audit/chooseAuditUser";
  import {listLog} from "@/api/sys/log";
  import {listTodo, getTodo, delTodo, addTodo, updateTodo, exportTodo} from "@/api/sys/todo";
  import auditResult from "../../audit/auditResult";

  export default {
    name: "parttyExemplary",
    components: {
      partyMember, addressMap, selectTree, ElImageViewer,ChooseAuditUser,auditResult
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

        disabled: false,
        logList: [],
        todoId: undefined,
        todoStatus: undefined,

      };
    },
    mounted() {
      window.addEventListener('resize', this.getHeight);
    },
    created() {
      this.init();
      this.defaultFilePicUrl = require("@/assets/image/file.png");
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
          this.exemplaryUuid = params.exemplaryUuid;
          this.sysLogId = params.sysLogId;

        }).then(() => {
          this.reset();
          this.disabled = true;
          let partyExemplary = {};
          partyExemplary.exemplaryUuid = this.exemplaryUuid
          listExemplary(partyExemplary).then(response => {
            if(response.rows && response.rows.length>0){
              this.form = response.rows[0];
              this.form.partyOrgName = this.form.djPartyOrg.partyOrgName;
              this.form.memberName = this.form.djPartyMember.memberName;
            }
          }).then(()=>{
            this.getLogList();
            this.getFileList();
          });
        });
      },
      getLogList() {
        this.loading = true;
        listLog({"uuid": this.form.exemplaryUuid}).then(response => {
          this.logList = response.rows;
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
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.disabled = false;
        this.open = true;
        this.title = "添加先锋模范";
        this.form.exemplaryUuid = this.uuid();
        this.getFileList();
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
        });
      },
      handleSubmit(form){
        this.form.params = {}
        this.form.params.auditUserId = form.auditUserId;
        this.form.params.operReason = form.reason;
        this.submitForm(2);
      },




      // 取消按钮
      cancel() {
        this.$store.dispatch('tagsView/delView', this.$route)
        this.$router.go(-1)
      },
      // 表单重置
      reset() {
        this.form = {
          changeId: undefined,
          changeUuid: undefined,
          partyOrgId: undefined,
          changeTime: undefined,
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
      submitAudit(form) {
        this.form.params = {}
        this.form.params.state = form.state;
        this.form.params.reason = form.reason;
        this.form.params.sysLogId = this.sysLogId;
        this.form.auditState = form.state;
        updateExemplary(this.form).then(response => {
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


      uploadFile(file) {
        const loading = this.$loading({
          lock: true,
          text: '上传中……',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });
        let formData = new FormData();
        formData.append("uuid", this.form.exemplaryUuid);
        formData.append("file", file.file);
        formData.append("fileType", "exemplary");
        upload(formData).then(response => {
          if (response.code === 200) {
            let file = {};
            file.name = response.data.fileName;
            file.url = process.env.VUE_APP_BASE_API + response.data.filePath;
            file.uid = response.data.id;
            this.fileList.push(file);
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

      setFileNameTip(file) {
        return file.name;
      },
      handlePictureCardPreview(file) {
        this.dialogVisible = true;
        this.srcList.push(file.url)

      },
      closeViewer() {
        this.dialogVisible = false;
        this.srcList = [];
      },
      handleDownload(file) {
        //console.log(file);
        downLoadZip("/system/file/download/" + file.uid);

      },
      handleRemove(file) {
        //console.log(file);
        this.$confirm('是否确认删除该附件?', "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "info"
        }).then(() => {
          delFile(file.uid).then(response => {
            this.msgSuccess(response.msg);
            this.getFileList();
          });
        }).catch(function () {
        });
      },
      getFileList() {
        this.fileList = [];
        listFile({'uuid': this.form.exemplaryUuid}).then(response => {
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






    }
  };
</script>
