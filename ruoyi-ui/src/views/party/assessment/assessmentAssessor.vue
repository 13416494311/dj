<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="100px">
      <el-form-item label="党组织名称" prop="partyOrgId">
        <select-tree :value="queryParams.partyOrgId"
                     style="width:100%;"
                     :options="partyOrgOptions"
                     vModel="partyOrgId"
                     @selected="setVModelValue"
                     placeholder="请选择党组织"
        />
      </el-form-item>
      <el-form-item label="考核年份" prop="year">
        <el-date-picker
          style="width:100%;"
          v-model="queryParams.assessmentyear.year"
          type="year"
          format="yyyy"
          value-format="yyyy"
          @change="handleQuery"
          placeholder="选择考核年份">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="考核名称" prop="assessmentName">
        <el-input
          v-model="queryParams.assessmentyear.assessmentName"
          placeholder="请输入考核名称"
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

    <el-table :stripe="true" :border="true" v-loading="loading" :data="assessmentList"
              :header-cell-style="{'text-align':'center'}">
      <el-table-column label="序号" align="center" type="index"/>
      <el-table-column label="党组织" width="300" align="left"
                       prop="djPartyOrg.partyOrgFullName" :formatter="partyOrgFormat"/>
      <el-table-column label="考核年份" width="120" align="center" prop="assessmentyear.year"
                       :formatter="yearFormat"/>
      <el-table-column label="考核名称" align="center" prop="assessmentyear.assessmentName"/>
      <el-table-column label="考核状态" width="120" align="center" prop="orgAssessmentStatus"
                       :formatter="orgAssessmentStatusFormat"/>
      <el-table-column label="操作" align="center" width="180" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="small"
            type="text"
            icon="el-icon-search"
            @click="handleSee(scope.row)"
          >查看
          </el-button>
          <el-button
            v-if="scope.row.orgAssessmentStatus =='2'"
            size="small"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['party:assessment:edit']"
          >党委评分
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

    <!-- 添加或修改党组织考核对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="90%" append-to-body
               @open="getHeight" :close-on-click-modal="false">
      <div :style="bodyStyle">
        <div style="text-align: center;font-size: 24px;padding: 8px;">
          {{assessmentName}}
        </div>
        <div style="padding: 0px 0px 8px 0px;">
          党组织名称：{{partyOrgFullName.substring(partyOrgFullName.indexOf("/") + 1)}}
        </div>
        <el-form ref="form" :model="form" :rules="rules">
          <el-table :stripe="true"
                    :border="true"
                    :summary-method="getTotal"
                    :show-summary="showSum"
                    v-loading="orgLoading" :data="assessmentScoreList"
                    :header-cell-style="{'text-align':'center'}">
            <el-table-column label="序号" align="center" type="index"/>
            <el-table-column label="考核项目" width="150" align="center" prop="item"/>
            <el-table-column label="考核内容" width="150" align="left" prop="content"/>
            <el-table-column label="考核指标" align="left" prop="quota"/>
            <el-table-column label="分值" width="50" align="center" prop="score"
                             :formatter="scoreFormat"/>
            <el-table-column label="评分标准" align="left" prop="criteria"/>
            <el-table-column label="自评分数" width="80" align="center" prop="selfScore" :formatter="selfScoreFormat"/>
            <el-table-column label="党委评分" width="180" align="center" prop="assessorScore">
              <template slot-scope="scope">
                <el-form-item v-if="!scoreDisabled"
                              :prop="createProp(scope.$index)"
                              :rules="[{validator: checkAssessorScore, trigger: 'blur'}]">
                  <el-input-number style="width:150px"
                                   v-model="scope.row['assessorScore']"
                                   size="small"
                                   @change="refreshTotal"
                                   controls-position="right"
                                   :precision="1" :step="0.5"
                                   :min="0"></el-input-number>
                </el-form-item>

                <div v-if="scoreDisabled">
                  {{scope.row['assessorScore']==undefined?'':scope.row['assessorScore'].toFixed(1)+' 分'}}
                </div>
              </template>
            </el-table-column>

            <el-table-column v-if="!scoreDisabled" label="操作" width="70" align="center" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button
                  size="small"
                  type="text"
                  icon="el-icon-edit"
                  @click="handleDetailedUpdate(scope.row)"
                  v-hasPermi="['party:assessmentScore:edit']"
                >保存
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-card shadow="always" style="margin: 30px 0px;">
            <div slot="header" style="height: 25px">
              <span style="font-weight: bold;font-size: 16px">自评资料</span>
            </div>
            <el-upload
              action="#"
              list-type="picture-card"
              :file-list="fileList"
              multiple
              :http-request="uploadFile"
              :class="{hide:true}"
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
                      v-if="!true"
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
          </el-card>
        </el-form>
      </div>


      <div slot="footer" class="dialog-footer" :style="{textAlign:'center'}">
        <el-button v-if="!scoreDisabled" type="primary" @click="submitForAssessor">提 交</el-button>
        <el-button v-if="!scoreDisabled" type="primary" @click="allSave">保 存</el-button>
        <el-button @click="cancel">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    listAssessment,
    getAssessment,
    delAssessment,
    addAssessment,
    updateAssessment,
    exportAssessment
  } from "@/api/party/assessment";
  import {
    listAssessmentScore,
    getAssessmentScore,
    delAssessmentScore,
    addAssessmentScore,
    updateAssessmentScore,
    updateAssessmentScoreList,
    exportAssessmentScore
  } from "@/api/party/assessmentScore";

  import {partyOrgTreeselect, getPartyOrg} from "@/api/party/org";
  import {listFile, upload, delFile} from "@/api/system/file";
  import {downLoadZip} from "@/utils/zipdownload";
  import ElImageViewer from 'element-ui/packages/image/src/image-viewer'
  import UploadAllFile from "../../upload/uploadAllFile";
  import selectTree from '../../components/selectTree';

  export default {
    name: "AssessmentAssessor",
    components: {
      UploadAllFile,
      ElImageViewer,
      selectTree
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
        // 党组织考核表格数据
        assessmentList: [],
        //党支部考核状态
        orgAssessmentStatusOptions: [],
        // 考核评价评分表格数据
        assessmentScoreList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          assessmentUuid: undefined,
          assessmentyearUuid: undefined,
          partyOrgId: undefined,
          assessment_year: undefined,
          assessmentName: undefined,
          assessmentResult: undefined,
          orgAssessmentStatus: undefined,
          assessment_rank: undefined,
          assessmentyear: {
            orgAssessmentStatus: 2,
            year:undefined,
            assessmentName:undefined,
          }
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {},
        bodyStyle: {
          overflowY: 'auto',
          height: '',
          marginLeft: '2%',
          paddingRight: '2%',
        },
        //组织架构
        partyOrgOptions: [],
        fileList: [],
        assessmentScoreListSelections: [],
        disabled: false,
        dialogVisible: false,
        srcList: [],
        defaultFilePicUrl: undefined,
        showSum: true,
        orgLoading: true,
        assessorScoreRequired: false,
        scoreDisabled: false,
        assessmentName:'',
        partyOrgFullName:'',
        assessmentScore:'',
      };
    },
    mounted() {
      window.addEventListener('resize', this.getHeight);
    },
    created() {
      this.getList();
      //组织架构树
      this.getPartyOrgTreeSelect();
      //党组织考核状态
      this.getDicts("org_assessment_status").then(response => {
        this.orgAssessmentStatusOptions = response.data;
      });
      this.defaultFilePicUrl = require("@/assets/image/file.png");
    },
    methods: {
      selfScoreFormat(row, column) {
        return row.selfScore==undefined?'':row.selfScore.toFixed(1) + " 分";
      },
      scoreFormat(row, column) {
        return row.score + " 分";
      },
      createProp(rowIndex) {
        return "assessorScore-" + rowIndex;
      },
      //党委评分验证
      checkAssessorScore(rule, value, callback) {
        let filed = rule.field
        let fileds = filed.split("-");
        let assessorScore = this.assessmentScoreList[fileds[1]].assessorScore;
        let score = this.assessmentScoreList[fileds[1]].score;
        if(assessorScore>score){
          callback(new Error("党委评分不能大于"+score+"分!"));
        }else{
          if (!assessorScore && this.assessorScoreRequired) {
            callback(new Error("党委评分不能为空!"));
          } else {
            callback()
          }
        }

      },
      refreshTotal() {
        this.showSum = false;
        this.$nextTick().then(() => {
          this.showSum = true;
        })
      },
      getTotal(param) {
        const {columns, data} = param;
        const sums = [];
        columns.forEach((column, index) => {
          if (index === 1) {
            sums[index] = '合计';
            return;
          }
          const values = data.map(item => Number(item[column.property]));
          if (column.property === 'assessorScore' ||column.property === 'selfScore') {
            sums[index] = values.reduce((prev, curr) => {
              const value = Number(curr);
              if (!isNaN(value)) {
                return prev + curr;
              } else {
                return prev;
              }
            }, 0);
            if (sums[index] && sums[index] != 0) {
              this.assessmentScore = sums[index].toFixed(1);
              sums[index] = sums[index].toFixed(1) + ' 分';
            } else {
              sums[index] = '';
            }
          }
        });
        return sums;
      },
      /** 查询考核评价评分列表 */
      getAssessmentScoreList() {
        this.orgLoading = true;
        this.assessmentScoreList = []
        listAssessmentScore({"assessmentUuid": this.form.assessmentUuid,type:'1'}).then(response => {
          this.assessmentScoreList = response.rows;
          for (let i in this.assessmentScoreList) {
            if (!this.assessmentScoreList[i].assessorScore) {
              this.assessmentScoreList[i].assessorScore = undefined
            }
          }
          this.orgLoading = false;
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
      yearFormat(row, column) {
        return row.assessmentyear.year + "年";
      },
      partyOrgFormat(row, column) {
        let partyOrgFullName = row.djPartyOrg.partyOrgFullName;
        return partyOrgFullName.substring(partyOrgFullName.indexOf("/") + 1);
      },
      //获取党组织考核状态字典值
      orgAssessmentStatusFormat(row, column) {
        return this.selectDictLabel(this.orgAssessmentStatusOptions, row.orgAssessmentStatus);
      },
      /** 对话框自适应高度 */
      getHeight() {
        this.bodyStyle.height = window.innerHeight - 281 + 'px';
      },
      /** 查询党组织考核列表 */
      getList() {
        this.loading = true;
        listAssessment(this.queryParams).then(response => {
          this.assessmentList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.getList()
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          id: undefined,
          assessmentUuid: undefined,
          assessmentyearUuid: undefined,
          partyOrgId: undefined,
          assessment_year: undefined,
          assessmentName: undefined,
          assessmentResult: undefined,
          orgAssessmentStatus: undefined,
          assessment_rank: undefined,
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
        this.queryParams = {
          pageNum: 1,
          pageSize: 10,
          assessmentUuid: undefined,
          assessmentyearUuid: undefined,
          partyOrgId: undefined,
          assessment_year: undefined,
          assessmentName: undefined,
          assessmentResult: undefined,
          orgAssessmentStatus: 2,
          assessment_rank: undefined,
          assessmentyear: {
            orgAssessmentStatus: 2,
            year: undefined,
            assessmentName: undefined,
          }
        }
        this.handleQuery();
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.id);
        this.single = selection.length != 1;
        this.multiple = !selection.length;
        this.assessmentScoreListSelections = selection;
      },
      // 查看按钮操作
      handleSee(row) {
        this.reset()
        this.scoreDisabled = true
        const id = row.id || this.ids
        getAssessment(id).then(response => {
          this.form = response.data;
          this.assessmentName = response.data.assessmentyear.assessmentName
          this.partyOrgFullName = response.data.djPartyOrg.partyOrgFullName
          this.open = true;
          this.title = "党委评分查看";
        }).then(() => {
          this.getAssessmentScoreList();
          this.getFileList();
        });
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加党组织考核";
      },
      /** 党委评分按钮操作 */
      handleUpdate(row) {
        this.reset()
        this.scoreDisabled = false
        const id = row.id || this.ids
        getAssessment(id).then(response => {
          this.form = response.data;
          this.assessmentName = response.data.assessmentyear.assessmentName
          this.partyOrgFullName = response.data.djPartyOrg.partyOrgFullName
          this.open = true;
          this.title = "党委评分";
        }).then(() => {
          this.getAssessmentScoreList();
          this.getFileList();
        });
      },
      /** 修改分数按钮操作 */
      handleDetailedUpdate(row) {
        let params = [];
        params.push(row)
        updateAssessmentScore(params).then(response => {
          if (response.code === 200) {
            this.msgSuccess("保存成功");
            this.getAssessmentScoreList();
          } else {
            this.msgError(response.msg);
          }
        });
      },
      /** 全部保存操作 */
      allSave() {
        this.assessorScoreRequired = false;
        updateAssessmentScoreList(this.assessmentScoreList).then(response => {
          if (response.code === 200) {
            this.msgSuccess("保存成功");
            this.cancel();
          } else {
            this.msgError(response.msg);
          }
        });
      },
      /** 提交党委评分 */
      submitForAssessor() {
        this.assessorScoreRequired = true;
        this.$nextTick(()=>{
          this.$refs["form"].validate((valid ,object)=> {
            if (valid) {
              updateAssessmentScoreList(this.assessmentScoreList).then(response => {
                if (response.code === 200) {
                  this.form.orgAssessmentStatus = "3";
                  this.form.assessmentScore = this.assessmentScore;
                  updateAssessment(this.form).then(response => {
                    if (response.code === 200) {
                      this.msgSuccess("提交成功");
                      this.cancel();
                    } else {
                      this.msgError(response.msg);
                    }
                  })
                }
              })
            }
          })
        })
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          this.msgSuccess(valid);
          if (valid) {
            if (this.form.id != undefined) {
              updateAssessmentScore(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                  this.getAssessmentScoreList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addAssessmentScore(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("新增成功");
                  this.open = false;
                  this.getList();
                  this.getAssessmentScoreList();
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
        const ids = row.id || this.ids;
        this.$confirm('是否确认删除党组织考核编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delAssessment(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
      /** 删除按钮操作 */
      handleDeleteScore(row) {
        const ids = row.id || this.ids;
        this.$confirm('是否确认删除党组织考核编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delAssessmentScore(ids);
        }).then(() => {
          this.getList();
          this.getAssessmentScoreList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有党组织考核数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return exportAssessment(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function () {
        });
      },
      setFileNameTip(file) {
        return file.name;
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
        formData.append("uuid", this.form.assessmentUuid);
        formData.append("file", file.file);
        formData.append("fileType", "assessment");
        upload(formData).then(response => {
          if (response.code === 200) {
            // this.getFileList();
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
      handlePictureCardPreview(file) {
        //this.dialogImageUrl = file.url;
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
      getFileList() {
        this.fileList = [];
        listFile({'uuid': this.form.assessmentUuid}).then(response => {
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
