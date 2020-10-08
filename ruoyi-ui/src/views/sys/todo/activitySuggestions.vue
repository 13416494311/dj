<template>
  <div style="padding: 30px">


    <el-card shadow="always" style="margin-bottom: 30px;">
      <div slot="header" style="height: 25px">
        <span style="font-weight: bold;font-size: 16px">活动信息</span>
      </div>
      <el-form ref="form" :model="form" label-width="150px">

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
              <el-input :disabled="true" v-model="form.partyMemberName"
                        placeholder="请输入负责人">
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
            <el-form-item label="活动党组织">
              <el-input :disabled="true" v-model="djPartyOrg.partyOrgFullName" placeholder="请输入活动党组织"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="活动地点" prop="venue">
              <el-input :disabled="true" v-model="form.venue" placeholder="请输入活动地点">
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="活动内容" prop="activityContent">
              <el-input :disabled="true " v-model="form.activityContent"
                        type="textarea"
                        :autosize="{ minRows: 3, maxRows: 6}"
                        placeholder="请输入内容"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="活动计划召开时间" prop="activityPlanStartTime">
              <el-date-picker :disabled="true "
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
              <el-date-picker :disabled="true "
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
              <el-date-picker :disabled="true"
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
              <el-date-picker :disabled="true"
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
              <el-input :disabled="true " v-model="form.recorder"
                        placeholder="请输入记录人"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="到会指导人员" prop="mentors">
              <el-input :disabled="true " v-model="form.mentors"
                        placeholder="请输入到会指导人员"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="主持人" prop="presenter">
              <el-input :disabled="true " v-model="form.presenter"
                        placeholder="请输入主持人"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="主讲人及身份" prop="speaker">
              <el-input :disabled="true " v-model="form.speaker"
                        placeholder="请输入主讲人及身份"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="活动资料">
              <el-upload
                action="#"
                list-type="picture-card"
                :file-list="planFileList"
                :http-request="uploadFileNull"
                :class="{hide:true}"
                accept="image/*,.doc,.docx,.xls,.xlsx,.pdf,.ppt,.zip,.txt">
                <i slot="default" class="el-icon-plus"></i>
                <div slot="file" slot-scope="{file}" style="display: inline">
                  <div style="height: 70%">
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
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <el-card shadow="always" style="margin-bottom: 30px;">
      <div slot="header" style="height: 25px">
        <span style="font-weight: bold;font-size: 16px">建言献策</span>
      </div>
      <el-form ref="activitySuggestions" :model="activitySuggestions" :rules="rules" label-width="150px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="标题" prop="title">
              <el-input v-model="activitySuggestions.title" placeholder="请输入标题"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="建言献策内容" prop="content">
              <el-input v-model="activitySuggestions.content" type="textarea" :autosize="{ minRows: 4, maxRows: 4}"
                        placeholder="请输入内容"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="附件" prop="content">
              <el-upload
                action="#"
                :http-request="uploadFile"
                :on-remove="handleRemove"
                :on-preview="handleDownload"
                multiple
                :file-list="fileList">
                <el-button size="small" type="primary">上传</el-button>
              </el-upload>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <div v-if="todoStatus=='0'" :style="{textAlign:'center'}">
      <el-button type="primary" @click="submitForm('1')">提 交</el-button>
      <el-button @click="submitTodo('1')">完 成</el-button>
    </div>
    <div v-if="todoStatus=='1'" :style="{textAlign:'center'}">
      <el-button @click="cancel">关 闭</el-button>
    </div>


  </div>


</template>


<script>
  import {
    listDetailed,
    listDetailedByParam,
    getDetailed,
    getDetailedByUuid,
    delDetailed,
    addDetailed,
    updateDetailed,
    exportDetailed
  } from "@/api/activity/detailed";
  import {listFile, upload, delFile} from "@/api/system/file";
  import {downLoadZip} from "@/utils/zipdownload";
  import {listTodo, getTodo, delTodo, addTodo, updateTodo, exportTodo} from "@/api/sys/todo";
  import {
    listSuggestions,
    getSuggestions,
    delSuggestions,
    addSuggestions,
    updateSuggestions,
    exportSuggestions
  } from "@/api/activity/suggestions";

  export default {
    data() {
      return {
        todoStatus: undefined,
        todoId: undefined,
        detailedUuid: undefined,
        partyMemberId: undefined,
        activityTypeOptions: [],
        planFileList: [],
        // 表单参数
        form: {},
        djActivityPlan: {},
        djPartyOrg: {},
        activitySuggestions: {},
        // 表单校验
        rules: {
          title: [
            {required: true, message: "标题不能为空", trigger: "blur"}
          ],
          content: [
            {required: true, message: "内容不能为空", trigger: "blur"}
          ],
        },
        dialogVisible: false,
        dialogImageUrl: '',
        defaultFilePicUrl: undefined,
        content: undefined,
        fileList: [],
      }
    },
    created() {
      this.defaultFilePicUrl = require("@/assets/image/file.png");
      this.init();
      this.getDicts("activity_type").then(response => {
        this.activityTypeOptions = response.data;
      });
    },
    methods: {
      init() {
        this.todoId = this.$route.query.todoId;
        getTodo(this.todoId).then(response => {
          if(response.data ==undefined){
            this.$store.dispatch('tagsView/delView', this.$route)
            this.$router.go(-1)
          }
          this.todoStatus = response.data.status
          let params = eval('(' + response.data.urlParams + ')');
          this.detailedUuid = params.detailedUuid;
          this.partyMemberId = params.partyMemberId;
        }).then(() => {
          this.reset();
          this.activitySuggestions.suggestionsUuid = this.uuid()
          const detailedUuid = this.detailedUuid;
          getDetailedByUuid(detailedUuid).then(response => {
            this.form = response.data;
            this.form.partyMemberName = response.data.djPartyMember.memberName
            this.djActivityPlan = response.data.djActivityPlan;
            this.djPartyOrg = response.data.djPartyOrg;
          }).then(() => {
            this.getPlanFileList();
          });
        });
      },

      reset() {
        this.content = undefined;
        this.form = {
          detailedId: undefined,
          detailedUuid: undefined,
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
          partyOrgUuid: undefined,
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
        this.planFileList = [];

        this.activitySuggestions = {
          suggestionsId: undefined,
          suggestionsUuid: undefined,
          detailedUuid: undefined,
          title: undefined,
          content: undefined,
          partyMemberId: undefined,
          delFlag: undefined,
          createBy: undefined,
          createTime: undefined,
          updateBy: undefined,
          updateTime: undefined
        };

        this.resetForm("activitySuggestions");
        this.resetForm("form");
      },
      uploadFileNull(file) {

      },
      handlePictureCardPreview(file) {
        this.dialogImageUrl = file.url;
        this.dialogVisible = true;
      },
      handleDownload(file) {
        //console.log(file);
        downLoadZip("/system/file/download/" + file.uid);

      },
      getPlanFileList() {
        this.planFileList = [];
        listFile({'uuid': this.form.planUuid}).then(response => {
          let files = response.rows;
          for (let i = 0; i < files.length; i++) {
            let file = {};
            file.name = files[i].fileName;
            file.url = process.env.VUE_APP_BASE_API + files[i].filePath;
            file.uid = files[i].id;
            this.planFileList.push(file);
          }
        });
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
        formData.append("uuid", this.activitySuggestions.suggestionsUuid);
        formData.append("file", file.file);
        formData.append("fileType", "activitySuggestions");
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
      getFileList() {
        this.fileList = [];
        listFile({'uuid': this.activitySuggestions.suggestionsUuid}).then(response => {
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
      submitForm: function (status) {
        this.$refs["activitySuggestions"].validate(valid => {
          if (valid) {
            this.activitySuggestions.detailedUuid = this.form.detailedUuid;
            this.activitySuggestions.partyMemberId = this.partyMemberId;
            if (this.activitySuggestions.suggestionsId != undefined) {
              updateSuggestions(this.activitySuggestions).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("提交成功");
                  this.submitTodo(status)
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addSuggestions(this.activitySuggestions).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("提交成功");
                  this.submitTodo(status)
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
      submitTodo(status) {
        updateTodo({"todoId": this.todoId, "status": status}).then(response => {
          if (response.code === 200) {
            this.$store.dispatch('tagsView/delView', this.$route)
            this.$router.go(-1)
          } else {
            this.msgError(response.msg);
          }
        });
      },
      cancel(){
        this.$store.dispatch('tagsView/delView', this.$route)
        this.$router.go(-1)
      }
    },
  }
</script>


