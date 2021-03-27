<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
<!--      <el-form-item label="加减分uuid" prop="addsubtractUuid">-->
<!--        <el-input-->
<!--          v-model="queryParams.addsubtractUuid"-->
<!--          placeholder="请输入加减分uuid"-->
<!--          clearable-->
<!--          size="small"-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="考核uuid" prop="assessmentUuid">-->
<!--        <el-input-->
<!--          v-model="queryParams.assessmentUuid"-->
<!--          placeholder="请输入考核uuid"-->
<!--          clearable-->
<!--          size="small"-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
      <el-form-item label="党组织" prop="partyOrgId">
        <select-tree :value="queryParams.partyOrgId"
                     :options="partyOrgOptions"
                     vModel="partyOrgId"
                     @selected="setVModelValue"
                     placeholder="请选择党组织"
        />
      </el-form-item>
      <el-form-item label="类型" prop="addsubtractType">
        <el-select v-model="queryParams.addsubtractType" placeholder="请选择类型" clearable size="small">
          <el-option
            v-for="dict in addsubtractTypeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          ></el-option>
        </el-select>
      </el-form-item>
<!--      <el-form-item label="考核年份" prop="addsubtractYear">-->
<!--        <el-input-->
<!--          v-model="queryParams.addsubtractYear"-->
<!--          placeholder="请输入考核年份"-->
<!--          clearable-->
<!--          size="small"-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="事项" prop="addsubtractTheme">-->
<!--        <el-input-->
<!--          v-model="queryParams.addsubtractTheme"-->
<!--          placeholder="请输入事项"-->
<!--          clearable-->
<!--          size="small"-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="分数" prop="addsubtractScore">-->
<!--        <el-input-->
<!--          v-model="queryParams.addsubtractScore"-->
<!--          placeholder="请输入分数"-->
<!--          clearable-->
<!--          size="small"-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="指标完成情况是否较差 0:否，1：是" prop="completionEffects">-->
<!--        <el-input-->
<!--          v-model="queryParams.completionEffects"-->
<!--          placeholder="请输入指标完成情况是否较差 0:否，1：是"-->
<!--          clearable-->
<!--          size="small"-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="是否有重大不良影响 0:否，1：是" prop="adverseEffects">-->
<!--        <el-input-->
<!--          v-model="queryParams.adverseEffects"-->
<!--          placeholder="请输入是否有重大不良影响 0:否，1：是"-->
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

    <el-row v-if="!see" :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['party:addsubtract:add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['party:addsubtract:edit']"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['party:addsubtract:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['party:addsubtract:export']"
        >导出
        </el-button>
      </el-col>
    </el-row>

    <el-table :stripe="true" :border="true" v-loading="loading" :data="addsubtractList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="序号" align="center" type="index"/>
<!--      <el-table-column label="id" align="center" prop="addsubtractId"/>-->
<!--      <el-table-column label="加减分uuid" align="center" prop="addsubtractUuid"/>-->
      <el-table-column label="考核项目" align="center" prop="djOrgAssessment.assessmentName"/>
      <el-table-column label="党组织" align="center" prop="djPartyOrg.partyOrgName"/>
      <el-table-column label="类型" width="70" align="center" prop="addsubtractType" :formatter="addsubtractTypeFormat"/>
      <el-table-column label="考核年份" width="80" align="center" prop="djOrgAssessment.assessment_year"/>
      <el-table-column label="事项" align="center" prop="addsubtractTheme"/>
<!--      <el-table-column label="描述" align="center" prop="addsubtractContent"/>-->
<!--      <el-table-column label="分数" align="center" prop="addsubtractScore"/>-->
<!--      <el-table-column label="指标完成情况是否较差 0:否，1：是" align="center" prop="completionEffects"/>-->
<!--      <el-table-column label="是否有重大不良影响 0:否，1：是" align="center" prop="adverseEffects"/>-->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-view"-->
<!--            @click="handleSee(scope.row)"-->
<!--          >查看-->
<!--          </el-button>-->
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['party:addsubtract:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['party:addsubtract:remove']"
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

    <!-- 添加或修改党组织加减分审核对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="80%" append-to-body
               @open="getHeight" :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" :style="bodyStyle" label-width="100px">

        <el-card shadow="always" style="margin-bottom: 30px;">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">党支部考核基本资料</span>
          </div>

        <el-form-item label="党组织" prop="partyOrgId">
          <select-tree :value="form.partyOrgId"
                       :options="partyOrgOptions"
                       vModel="partyOrgId"
                       @selected="setVModelValueEdit"
                       placeholder="请选择党组织"
          />
        </el-form-item>

        <el-form-item label="考核名称" prop="assessmentUuid">
          <el-select v-model="form.assessmentUuid"  >
                <el-option
                  v-for="item in assementList"
                  :key="item.assessmentUuid"
                  :label="item.assessmentName"
                  :value="item.assessmentUuid">
                </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="类型" prop="addsubtractType">
          <el-select v-model="form.addsubtractType" placeholder="请选择类型">
            <el-option
              v-for="dict in addsubtractTypeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
<!--        <el-form-item label="考核年份" prop="addsubtractYear">-->
<!--          <el-input v-model="form.addsubtractYear" placeholder="请输入考核年份"/>-->
<!--        </el-form-item>-->
        <el-form-item label="事项" prop="addsubtractTheme">
          <el-input v-model="form.addsubtractTheme" placeholder="请输入事项" maxlength="50" show-word-limit/>
        </el-form-item>
        <el-form-item label="描述" prop="addsubtractContent">
          <el-input v-model="form.addsubtractContent" type="textarea" :autosize="{ minRows: 4, maxRows: 6}"
                    placeholder="请输入内容" maxlength="500" show-word-limit/>
        </el-form-item>
        <el-form-item label="分数" prop="addsubtractScore">
<!--          <el-input v-model="form.addsubtractScore"  placeholder="请输入分数" onkeyup="this.value = this.value.replace(/[^\d.]/g,'');"-->
<!--                    maxlength="2"/>-->
          <el-input-number v-if="1" v-model="form.addsubtractScore" value="0" :precision="1" :step="0.5" :min="0" :max = "100"></el-input-number>

        </el-form-item>
        <el-form-item label="指标完成情况是否较差" prop="completionEffects">
          <el-radio v-model="form.completionEffects" label="0">否</el-radio>
          <el-radio v-model="form.completionEffects" label="1">是</el-radio>
        </el-form-item>
        <el-form-item label="是否有重大不良影响" prop="adverseEffects">
          <el-radio v-model="form.adverseEffects" label="0">否</el-radio>
          <el-radio v-model="form.adverseEffects" label="1">是</el-radio>
        </el-form-item>
<!--        <el-form-item label="删除标志" prop="delFlag">-->
<!--          <el-input v-model="form.delFlag" placeholder="请输入删除标志"/>-->
<!--        </el-form-item>-->
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
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    listAddsubtract,
    getAddsubtract,
    delAddsubtract,
    addAddsubtract,
    updateAddsubtract,
    exportAddsubtract
  } from "@/api/party/addsubtract";

  import {listAssessment} from "../../../api/party/assessment";
  import {partyOrgTreeselect, getPartyOrg} from "@/api/party/org";
  import partyMember from "../../party/org/partyMemberChoose";
  import addressMap from "../../party/org/addressMap";
  import selectTree from '../../components/selectTree';
  import ElImageViewer from 'element-ui/packages/image/src/image-viewer'
  import ChooseAuditUser from "../../audit/chooseAuditUser";
  import UploadAllFile from "../../upload/uploadAllFile";

  export default {
    name: "Addsubtract",
    components: {
      UploadAllFile,
      partyMember, addressMap, selectTree, ElImageViewer,ChooseAuditUser
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
        // 党组织加减分审核表格数据
        addsubtractList: [],
        // 党组织加减分审核表格数据
        assementList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          addsubtractUuid: undefined,
          assessmentUuid: undefined,
          partyOrgId: undefined,
          addsubtractType: undefined,
          addsubtractYear: undefined,
          addsubtractTheme: undefined,
          addsubtractContent: undefined,
          addsubtractScore: undefined,
          completionEffects: undefined,
          adverseEffects: undefined,
        },
        // 查询参数
        queryAssementParams: {
          assessmentUuid: undefined,
          partyOrgId: undefined,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          partyOrgId: [
            { required: true, message: "党组织不能为空", trigger: "blur" },
          ],
          assessmentUuid: [
            { required: true, message: "考核项目名称不能为空", trigger: "blur" },
          ],
          addsubtractType: [
            { required: true, message: "类型不能为空", trigger: "blur" },
          ],
          addsubtractTheme: [
            { required: true, message: "事项不能为空", trigger: "blur" },
          ],
          addsubtractScore: [
            { required: true, message: "分数不能为空", trigger: "blur" },
          ],
        },
        bodyStyle: {
          overflowY: 'auto',
          height:
            '',
          marginLeft:
            '20%',
          paddingRight:
            '20%',
        },
        //组织架构
        partyOrgOptions: [],
        // 状态数据字典
        addsubtractTypeOptions: [],
        addsubtractUuidEdit: undefined,
      };
    },
    mounted() {
      window.addEventListener('resize', this.getHeight);
    },
    created() {
      this.getList();
      this.getAssessmentList();
      this.getDicts("addsubtract_type").then(response => {
        this.addsubtractTypeOptions = response.data;
      });

      //组织架构树
      this.getPartyOrgTreeSelect();
    },
    methods: {
      /** 对话框自适应高度 */
      getHeight() {
        this.bodyStyle.height = window.innerHeight - 281 + 'px';
      },
      /** 查询党组织加减分审核列表 */
      getList() {
        this.loading = true;
        listAddsubtract(this.queryParams).then(response => {
          this.addsubtractList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },

      /** 查询党组织加减分审核列表 */
      getAssessmentList() {
        this.loading = true;
        listAssessment(this.queryAssementParams).then(response => {
          this.assementList = response.rows;
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
          addsubtractId: undefined,
          addsubtractUuid: undefined,
          assessmentUuid: undefined,
          partyOrgId: undefined,
          addsubtractType: undefined,
          addsubtractYear: undefined,
          addsubtractTheme: undefined,
          addsubtractContent: undefined,
          addsubtractScore:undefined,
          completionEffects:"0",
          adverseEffects:"0",
          delFlag:undefined,
          createBy:undefined,
          createTime:undefined,
          updateBy:undefined,
          updateTime:undefined
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
        this.ids = selection.map(item => item.addsubtractId)
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      /** 查看按钮操作 */
      handleSee(row) {
        this.reset();
        const addsubtractid = row.addsubtractid || this.ids
        this.disabled = true;
        getAddsubtract(addsubtractid).then(response => {
          this.form = response.data;
          this.form.partyOrgName = this.form.djPartyOrg.partyOrgName;
          this.open = true;
          this.title = "查看加减项";
        }).then(()=>{
          this.$refs.uploadAllFile.init(this.form.addsubtractUuid, 'addsubtract') ;
        });
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.disabled = false;
        this.open = true;
        this.title = "添加党组织加减分审核";
        this.form.addsubtractUuid = this.uuid();
        this.$nextTick(function () {
          this.$refs.uploadAllFile.init(this.form.addsubtractUuid, 'addsubtract') ;
        })
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const addsubtractId = row.addsubtractId || this.ids
        getAddsubtract(addsubtractId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改党组织加减分审核";
        }).then(()=>{
          this.$refs.uploadAllFile.init(this.form.addsubtractUuid, 'addsubtract') ;
        });
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.addsubtractId != undefined) {
              updateAddsubtract(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addAddsubtract(this.form).then(response => {
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
        const addsubtractIds = row.addsubtractId || this.ids;
        this.$confirm('是否确认删除党组织加减分审核编号为"' + addsubtractIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delAddsubtract(addsubtractIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有党组织加减分审核数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return exportAddsubtract(queryParams);
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
      //新增货修改页面下拉树选择后设置值
      setVModelValueEdit(vModel, val) {
        this.addsubtractUuidEdit = this.form.addsubtractUuid;
        this.reset();
        if (val != null) {
          this.queryParams[vModel] = val;
          this.queryAssementParams[vModel] = val;
        } else {
          this.queryParams[vModel] = undefined;
          this.queryAssementParams[vModel];
        }
        this.form.addsubtractUuid = this.addsubtractUuidEdit;
        this.form.partyOrgId = this.queryAssementParams.partyOrgId;
        this.getAssessmentList();
      },
      //获取组织架构树
      getPartyOrgTreeSelect() {
        partyOrgTreeselect().then(response => {
          this.partyOrgOptions = this.treeInitData(response.data);
        });
      },
      addsubtractTypeFormat(row, column) {
        return this.selectDictLabel(this.addsubtractTypeOptions, row.addsubtractType);
      },

    }
  };
</script>
