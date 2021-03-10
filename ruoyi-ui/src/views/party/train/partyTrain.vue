<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="100px">
      <el-form-item label="党组织名称" prop="partyOrgId">
        <select-tree :value="queryParams.partyOrgId"
                     :options="partyOrgOptions"
                     vModel="partyOrgId"
                     @selected="setQueryParamsValue"
                     placeholder="请选择党组织"
        />
      </el-form-item>
      <el-form-item label="培训类别" prop="trainType">
        <el-select v-model="queryParams.trainType" placeholder="请选择培训类别" clearable size="small">
          <el-option
            v-for="dict in trainTypeOptions"
            v-show=" dict.dictValue ==6 || dict.dictValue == 7 || dict.dictValue == 8
             || dict.dictValue == 9 || dict.dictValue == 10 || trainTypeShow  "
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="活动名称" prop="activityTheme">
        <el-input
          v-model="queryParams.activityTheme"
          placeholder="请输入活动名称"
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
          v-hasPermi="['party:train:add']"
        >新增</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :stripe="true"
              :border="true" :data="trainList" @selection-change="handleSelectionChange">
      <el-table-column label="序号" align="center" type="index" />
      <el-table-column label="党组织名称" align="center" prop="partyOrg.partyOrgFullName" />
      <el-table-column label="培训类别" align="center" prop="trainType" :formatter="trainTypeFormat" />
      <el-table-column label="活动名称" align="center" prop="activityTheme" />
      <el-table-column label="活动内容" align="center" prop="activityContent" />
      <el-table-column label="开始时间" align="center" prop="activityStartTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.activityStartTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="activityEndTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.activityEndTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="有效学时" align="center" prop="effectiveHours" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="small"
            type="text"
            icon="el-icon-search"
            @click="handleSee(scope.row)"
          >查看</el-button>
          <el-button
            size="small"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['party:train:edit']"
          >修改</el-button>
          <el-button
            size="small"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['party:train:remove']"
          >删除</el-button>
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

    <!-- 添加或修改党组织培训对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="90%" append-to-body
               @open="getHeight"  :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" :style="bodyStyle"  label-width="150px">

        <el-card shadow="always" style="margin-bottom: 30px;">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">基本信息</span>
          </div>
          <el-row>
            <el-col :span="12">
              <el-form-item label="党组织名称" prop="partyOrgId">
                <select-tree :value="form.partyOrgId"
                             :disabled="disabled"
                             :options="partyOrgOptions"
                             vModel="partyOrgId"
                             @selected="setVModelValue"
                             placeholder="请选择党组织"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="培训类别">
                <el-select v-model="form.trainType" :disabled="disabled"
                           style="width: 100%" placeholder="请选择培训类别">
                  <el-option
                    v-for="dict in trainTypeOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>


          <el-row>
            <el-col :span="12">
              <el-form-item label="开始时间" prop="activityStartTime">
                <el-date-picker clearable size="small" style="width: 100%"
                                v-model="form.activityStartTime"
                                type="datetime"
                                :disabled="disabled"
                                value-format="yyyy-MM-dd HH:mm:ss"
                                placeholder="选择开始时间">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="结束时间" prop="activityEndTime">
                <el-date-picker clearable size="small" style="width: 100%"
                                v-model="form.activityEndTime"
                                type="datetime"
                                :disabled="disabled"
                                value-format="yyyy-MM-dd HH:mm:ss"
                                @change="setEffectiveHours"
                                placeholder="选择结束时间">
                </el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="有效学时" prop="effectiveHours">
                <el-input v-model="form.effectiveHours" :disabled="true" placeholder="请输入有效学时" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="24">
              <el-form-item label="活动名称" prop="activityTheme">
                <el-input v-model="form.activityTheme" :disabled="disabled" placeholder="请输入活动名称" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="24">
              <el-form-item label="活动内容" prop="activityContent">
                <el-input v-model="form.activityContent" :disabled="disabled"  type="textarea" :autosize="{ minRows: 6, maxRows: 6}" placeholder="请输入内容" />
              </el-form-item>
            </el-col>
          </el-row>

        </el-card>



        <train-member ref="trainMember" :disabled="disabled" />

        <el-card shadow="always" style="margin-bottom: 30px;">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">活动资料</span>
          </div>
          <upload-all-file ref="uploadAllFile" :disabled="disabled"/>
        </el-card>


      </el-form>
      <div slot="footer" class="dialog-footer"  :style="{textAlign:'center'}">
        <el-button v-show="!disabled" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {addTrain, delTrain, exportTrain, getTrain, listTrain, updateTrain} from "@/api/party/train";
  import {partyOrgTreeselect} from "@/api/party/org";
  import selectTree from '../../components/selectTree';
  import UploadAllFile from "../../upload/uploadAllFile";
  import TrainMember from "./trainMember";
  import { getUserProfile } from "@/api/system/user";

  export default {
    name: "PartyTrain",
    components: {TrainMember, UploadAllFile, selectTree },
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
        // 党组织培训表格数据
        trainList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 培训类别字典
        trainTypeOptions: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          trainUuid: undefined,
          partyOrgId: undefined,
          trainType: undefined,
          activityTheme: undefined,
          activityContent: undefined,
          activityStartTime: undefined,
          activityEndTime: undefined,
          effectiveHours: undefined,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          partyOrgId: [
            { required: true, message: "组织机构不能为空", trigger: "blur" }
          ],
          trainType: [
            { required: true, message: "培训类别不能为空", trigger: "blur" }
          ],
          activityTheme: [
            { required: true, message: "活动名称不能为空", trigger: "blur" }
          ],
          activityContent: [
            { required: true, message: "活动内容不能为空", trigger: "blur" }
          ],
          activityStartTime: [
            { required: true, message: "开始时间不能为空", trigger: "blur" }
          ],
          activityEndTime: [
            { required: true, message: "结束时间不能为空", trigger: "blur" }
          ],
          effectiveHours: [
            { required: true, message: "有效学时不能为空", trigger: "blur" }
          ],
        },
        bodyStyle:{
          overflowY:'auto',
          height: '',
          marginLeft:'2%' ,
          paddingRight:'2%',
        },
        // 党组织架构树选项
        partyOrgOptions: [],
        disabled:false,
        trainTypeShow:false,
      };
    },
    mounted () {
      window.addEventListener('resize', this.getHeight);
    },
    created() {

      this.getList();
      this.getTreeselect()
      this.setTrainTypeShow();
      this.getDicts("train_type").then(response => {
        this.trainTypeOptions = response.data;
      });
    },
    methods: {
      setTrainTypeShow(){
        getUserProfile().then(response => {
          this.trainTypeShow = false;
          this.user = response.data;
          let roles = this.user.roles;
          if(roles){
            for(let i=0;i<roles.length;i++){
              if(roles[i].roleId ==5 || roles[i].roleId ==1){
                this.trainTypeShow = true;
              }
            }
          }
        });
      },
      setEffectiveHours(){
        if(this.form.activityStartTime !=undefined && this.form.activityEndTime !=undefined ){
          this.form.effectiveHours = 8;
        }
      },
      /** 查询部门下拉树结构 */
      getTreeselect() {
        partyOrgTreeselect().then(response => {
          this.partyOrgOptions = this.treeInitData(response.data);
        });
      },
      setQueryParamsValue(vModel, val) {
        if(val!=null){
          this.queryParams[vModel] =  val;
        }else{
          this.queryParams[vModel] =  undefined;
        }
      },
      //下拉树选择后设置值
      setVModelValue(vModel, val) {
        if(val!=null){
          this.form[vModel] =  val;
          this.$refs.trainMember.init(this.form.trainUuid, val) ;
        }else{
          this.form[vModel] =  undefined;
        }
      },
      /** 对话框自适应高度 */
      getHeight(){
        this.bodyStyle.height=window.innerHeight-281+'px';
      },
      /** 查询党组织培训列表 */
      getList() {
        this.loading = true;
        listTrain(this.queryParams).then(response => {
          this.trainList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 培训类别字典翻译
      trainTypeFormat(row, column) {
        return this.selectDictLabel(this.trainTypeOptions, row.trainType);
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          trainId: undefined,
          trainUuid: undefined,
          partyOrgId: undefined,
          trainType: undefined,
          activityTheme: undefined,
          activityContent: undefined,
          activityStartTime: undefined,
          activityEndTime: undefined,
          effectiveHours: undefined,
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
        this.ids = selection.map(item => item.trainId)
        this.single = selection.length!=1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.disabled = false;
        this.title = "添加党组织培训";
        this.form.trainUuid = this.uuid();
        this.$nextTick(function () {
          this.$refs.trainMember.init(this.form.trainUuid, '') ;
          this.$refs.uploadAllFile.init(this.form.trainUuid, 'partyTrain') ;
        })
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const trainId = row.trainId || this.ids
        this.disabled = false;
        getTrain(trainId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改党组织培训";
        }).then(()=>{
          this.$refs.trainMember.init(this.form.trainUuid, this.form.partyOrgId) ;
          this.$refs.uploadAllFile.init(this.form.trainUuid, 'partyTrain') ;
        });
      },
      handleSee(row) {
        this.reset();
        const trainId = row.trainId || this.ids
        this.disabled = true;
        getTrain(trainId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "查看党组织培训";
        }).then(()=>{
          this.$refs.trainMember.init(this.form.trainUuid, this.form.partyOrgId) ;
          this.$refs.uploadAllFile.init(this.form.trainUuid, 'partyTrain') ;
        });
      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.trainId != undefined) {
              updateTrain(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addTrain(this.form).then(response => {
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
        const trainIds = row.trainId || this.ids;
        this.$confirm('是否确认删除党组织培训编号为"' + trainIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delTrain(trainIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有党组织培训数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportTrain(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
      }
    }
  };
</script>
