<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <!--      <el-form-item label="考核uuid" prop="assessmentUuid">-->
      <!--        <el-input-->
      <!--          v-model="queryParams.assessmentUuid"-->
      <!--          placeholder="请输入考核uuid"-->
      <!--          clearable-->
      <!--          size="small"-->
      <!--          @keyup.enter.native="handleQuery"-->
      <!--        />-->
      <!--      </el-form-item>-->
      <!--      <el-form-item label="assessmentYear_uuid" prop="assessmentyearUuid">-->
      <!--        <el-input-->
      <!--          v-model="queryParams.assessmentyearUuid"-->
      <!--          placeholder="请输入assessmentYearuuid"-->
      <!--          clearable-->
      <!--          size="small"-->
      <!--          @keyup.enter.native="handleQuery"-->
      <!--        />-->
      <!--      </el-form-item>-->
      <!--      <el-form-item label="党组织" prop="partyOrgId">-->
      <!--        <el-input-->
      <!--          v-model="queryParams.partyOrgId"-->
      <!--          placeholder="请输入党组织id"-->
      <!--          clearable-->
      <!--          size="small"-->
      <!--          @keyup.enter.native="handleQuery"-->
      <!--        />-->
      <!--      </el-form-item>-->

      <el-form-item label="考核年份" prop="assessment_year">
        <el-input
          v-model="queryParams.assessment_year"
          placeholder="请输入考核年份"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!--      <el-form-item label="考评等级结果 ，1：一级、2：二级、3：三级" prop="assessmentResult">-->
      <!--        <el-input-->
      <!--          v-model="queryParams.assessmentResult"-->
      <!--          placeholder="请输入考评等级结果 ，1：一级、2：二级、3：三级"-->
      <!--          clearable-->
      <!--          size="small"-->
      <!--          @keyup.enter.native="handleQuery"-->
      <!--        />-->
      <!--      </el-form-item>-->
      <!--      <el-form-item label="考核状态 0：党支部自评中、1：党委考核中、2考核完毕" prop="orgAssessmentStatus">-->
      <!--        <el-select v-model="queryParams.orgAssessmentStatus" placeholder="请选择考核状态 0：党支部自评中、1：党委考核中、2考核完毕" clearable-->
      <!--                   size="small">-->
      <!--          <el-option label="请选择字典生成" value=""/>-->
      <!--        </el-select>-->
      <!--      </el-form-item>-->
      <!--      <el-form-item label="排名" prop="rank">-->
      <!--        <el-input-->
      <!--          v-model="queryParams.rank"-->
      <!--          placeholder="请输入排名"-->
      <!--          clearable-->
      <!--          size="small"-->
      <!--          @keyup.enter.native="handleQuery"-->
      <!--        />-->
      <!--      </el-form-item>-->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!--    <el-row :gutter="10" class="mb8">-->
    <!--      <el-col :span="1.5">-->
    <!--        <el-button-->
    <!--          type="primary"-->
    <!--          icon="el-icon-plus"-->
    <!--          size="mini"-->
    <!--          @click="handleAdd"-->
    <!--          v-hasPermi="['party:assessment:add']"-->
    <!--        >新增-->
    <!--        </el-button>-->
    <!--      </el-col>-->
    <!--      <el-col :span="1.5">-->
    <!--        <el-button-->
    <!--          type="success"-->
    <!--          icon="el-icon-edit"-->
    <!--          size="mini"-->
    <!--          :disabled="single"-->
    <!--          @click="handleUpdate"-->
    <!--          v-hasPermi="['party:assessment:edit']"-->
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
    <!--          v-hasPermi="['party:assessment:remove']"-->
    <!--        >删除-->
    <!--        </el-button>-->
    <!--      </el-col>-->
    <!--      <el-col :span="1.5">-->
    <!--        <el-button-->
    <!--          type="warning"-->
    <!--          icon="el-icon-download"-->
    <!--          size="mini"-->
    <!--          @click="handleExport"-->
    <!--          v-hasPermi="['party:assessment:export']"-->
    <!--        >导出-->
    <!--        </el-button>-->
    <!--      </el-col>-->
    <!--    </el-row>-->

    <el-table :stripe="true" :border="true" v-loading="loading" :data="assessmentList"
              :header-cell-style="{'text-align':'center'}"
              :default-sort="{prop: 'assessment_year', order: 'ascending'}">
      <!--      <el-table-column type="selection" width="55" align="center"/>-->
      <!--      <el-table-column label="id" align="center" prop="id"/>-->
      <!--      <el-table-column label="考核uuid" align="center" prop="assessmentUuid"/>-->
      <!--      <el-table-column label="assessmentYear_uuid" align="center" prop="assessmentyearUuid"/>-->
      <el-table-column label="序号" align="center" type="index"/>
      <el-table-column label="党组织" width="300" align="left" prop="djPartyOrg.partyOrgFullName"/>
      <el-table-column label="考核年份" width="80" align="center" prop="assessment_year"/>
      <el-table-column label="考核名称" align="center" prop="assessmentName"/>
      <!--      <el-table-column label="考评等级" align="center" prop="assessmentResult"/>-->
      <el-table-column label="考核状态" align="center" prop="orgAssessmentStatus" :formatter="orgAssessmentStatusFormat"/>
      <!--      <el-table-column label="排名" align="center" prop="assessment_rank"/>-->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.orgAssessmentStatus =='2'"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['party:assessment:edit']"
          >党委评审
          </el-button>
          <!--          <el-button-->
          <!--            v-if="scope.row.orgAssessmentStatus =='1'"-->
          <!--            size="mini"-->
          <!--            type="text"-->
          <!--            icon="el-icon-edit"-->
          <!--            @click="handleUpdate(scope.row)"-->
          <!--            v-hasPermi="['party:assessment:edit']"-->
          <!--          >自评-->
          <!--          </el-button>-->
          <el-button
            v-if="scope.row.orgAssessmentStatus !='2'"
            size="mini"
            type="text"
            icon="el-icon-search"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['party:assessment:edit']"
          >查看
          </el-button>
          <!--          <el-button-->
          <!--            size="mini"-->
          <!--            type="text"-->
          <!--            icon="el-icon-delete"-->
          <!--            @click="handleDelete(scope.row)"-->
          <!--            v-hasPermi="['party:assessment:remove']"-->
          <!--          >删除-->
          <!--          </el-button>-->
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
      <el-form ref="form" :model="form" :rules="rules" :style="bodyStyle" label-width="100px">
        <!--        <el-form-item label="考核uuid" prop="assessmentUuid">-->
        <!--          <el-input v-model="form.assessmentUuid" placeholder="请输入考核uuid"/>-->
        <!--        </el-form-item>-->
        <!--        <el-form-item label="assessmentYear_uuid" prop="assessmentyearUuid">-->
        <!--          <el-input v-model="form.assessmentyearUuid" placeholder="请输入assessmentYear_uuid"/>-->
        <!--        </el-form-item>-->
        <!--        <el-form-item label="党组织id" prop="partyOrgId">-->
        <!--          <el-input v-model="form.partyOrgId" placeholder="请输入党组织id"/>-->
        <!--        </el-form-item>-->
        <!--        <el-form-item label="考核年份" prop="assessment_year">-->
        <!--          <el-input v-model="form.assessment_year" placeholder="请输入考核年份"/>-->
        <!--        </el-form-item>-->
        <!--        <el-form-item label="考核名称" prop="assessmentName">-->
        <!--          <el-input v-model="form.assessmentName" type="textarea" :autosize="{ minRows: 3, maxRows: 6}"-->
        <!--                    placeholder="请输入内容"/>-->
        <!--        </el-form-item>-->
        <!--        <el-form-item label="考评等级结果 ，1：一级、2：二级、3：三级" prop="assessmentResult">-->
        <!--          <el-input v-model="form.assessmentResult" placeholder="请输入考评等级结果 ，1：一级、2：二级、3：三级"/>-->
        <!--        </el-form-item>-->
        <!--        <el-form-item label="考核状态 0：党支部自评中、1：党委考核中、2考核完毕">-->
        <!--          <el-radio-group v-model="form.orgAssessmentStatus">-->
        <!--            <el-radio label="1">请选择字典生成</el-radio>-->
        <!--          </el-radio-group>-->
        <!--        </el-form-item>-->
        <!--        <el-form-item label="排名" prop="assessment_rank">-->
        <!--          <el-input v-model="form.assessment_rank" placeholder="请输入排名"/>-->
        <!--        </el-form-item>-->
        <!--        <el-form-item label="删除标志" prop="delFlag">-->
        <!--          <el-input v-model="form.delFlag" placeholder="请输入删除标志"/>-->
        <!--        </el-form-item>-->
        <!--      </el-form>-->
        <el-card shadow="always" style="margin-bottom: 30px;">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">党支部自评</span>
          </div>
          <el-table height="600" show-summary :stripe="true" :border="true" v-loading="loading"
                    key="assessmentScoreList"
                    :data="assessmentScoreList" @selection-change="handleSelectionChange"
                    :header-cell-style="{'text-align':'center'}">
            <el-table-column type="selection" width="30" align="center"/>
            <!--      <el-table-column label="id" align="center" prop="id"/>-->
            <!--      <el-table-column label="assessment_uuid" align="center" prop="assessmentUuid"/>-->
            <el-table-column label="序号" align="center" type="index"/>
            <el-table-column label="考核项目" width="100" align="center" prop="item"/>
            <el-table-column label="考核内容" width="100" align="left" prop="content"/>
            <el-table-column label="考核指标" align="left" prop="quota"/>
            <el-table-column label="分值" width="50" align="center" prop="score"/>
            <el-table-column label="评分标准" align="left" prop="criteria"/>
            <el-table-column label="自评分数" width="55" align="center" prop="selfScore"/>
            <el-table-column label="党委评分" width="155" align="center" prop="assessorScore">
              <template slot-scope="scope">
                <el-input-number v-if ="1" v-model="scope.row.assessorScore" size="small" controls-position="right"
                                 :precision="1" :step="0.5" :min="0" :max=scope.row.score></el-input-number>
                <span v-else>{{scope.row.assessorScore}}</span>
              </template>
            </el-table-column>
<!--            <el-table-column label="排序" align="center" prop="orderNum"/>-->
            <el-table-column label="操作" width="70" align="center" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-edit"
                  @click="handleDetailedUpdate(scope.row)"
                  v-hasPermi="['party:assessmentScore:edit']"
                >保存
                </el-button>
                <!--            <el-button-->
                <!--              size="mini"-->
                <!--              type="text"-->
                <!--              icon="el-icon-delete"-->
                <!--              @click="handleDeleteScore(scope.row)"-->
                <!--              v-hasPermi="['party:assessmentScore:remove']"-->
                <!--            >删除-->
                <!--            </el-button>-->
              </template>
            </el-table-column>
          </el-table>
        </el-card>

        <el-card shadow="always" style="margin-bottom: 30px;">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">评审资料</span>
          </div>
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
          <!--<el-dialog :visible.sync="dialogVisible"
                     append-to-body
                     :close-on-click-modal="false">
            <img width="100%" :src="dialogImageUrl" alt="">
          </el-dialog>-->

          <el-image-viewer
            v-if="dialogVisible"
            :on-close="closeViewer"
            :url-list="srcList"/>

        </el-card>
      </el-form>


      <div slot="footer" class="dialog-footer" :style="{textAlign:'center'}">
        <el-button v-if="this.form.orgAssessmentStatus=='2'" type="primary" @click="submitForAssessor">提交审核</el-button>
        <el-button v-if="this.form.orgAssessmentStatus=='2'" type="primary" @click="allSave">保 存</el-button>
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
    exportAssessmentScore
  } from "@/api/party/assessmentScore";

  import {partyOrgTreeselect, getPartyOrg} from "@/api/party/org";
  import {listFile, upload, delFile} from "@/api/system/file";
  import {downLoadZip} from "@/utils/zipdownload";
  import ElImageViewer from 'element-ui/packages/image/src/image-viewer'
  import UploadAllFile from "../../upload/uploadAllFile";


  export default {
    name: "Assessment",
    components: {
      UploadAllFile,
      ElImageViewer,
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
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {},
        bodyStyle: {
          overflowY: 'auto',
          height: '',
          marginLeft: '5%',
          paddingRight: '5%',
        },
        //组织架构
        partyOrgOptions: [],
        fileList: [],
        assessmentScoreListSelections: [],

      };
    },
    mounted() {
      window.addEventListener('resize', this.getHeight);
    },
    created() {
      this.getList();
      //获取评分列表
      this.getAssessmentScoreList()

      //组织架构树
      this.getPartyOrgTreeSelect();
      //党组织考核状态
      this.getDicts("org_assessment_status").then(response => {
        this.orgAssessmentStatusOptions = response.data;
      });
    },
    methods: {
      /** 查询考核评价评分列表 */
      getAssessmentScoreList() {
        this.orgLoading = true;
        listAssessmentScore({"assessmentUuid": this.form.assessmentUuid}).then(response => {
          this.assessmentScoreList = response.rows;
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
        const id = row.id || this.ids
        getAssessment(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "党支部自评";
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
      /** 自评按钮操作 */
      handleUpdate(row) {
        this.reset()
        const id = row.id || this.ids
        getAssessment(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "党支部自评";
        }).then(() => {
          this.getAssessmentScoreList();
          this.getFileList();
        });
      },
      /** 修改分数按钮操作 */
      handleDetailedUpdate(row) {
        updateAssessmentScore(row).then(response => {
          if (response.code === 200) {
            this.msgSuccess("修改成功");
            this.getList();
            this.getAssessmentScoreList();
          } else {
            this.msgError(response.msg);
          }
        });
      },
      /** 全部保存操作 */
      allSave() {
        for (var i = 0; i < this.assessmentScoreListSelections.length; i++) {
          updateAssessmentScore(this.assessmentScoreListSelections[i]);
        }
        this.msgSuccess("修改成功");
        this.getList();
        this.getAssessmentScoreList();

      },
      /** 提交党委评分 */
      submitForAssessor() {
        this.form.orgAssessmentStatus = "3";
        if (this.form.id != undefined) {
          updateAssessment(this.form).then(response => {
            if (response.code === 200) {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
              this.getAssessmentScoreList();
            } else {
              this.msgError(response.msg);
            }
          });
        }
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
