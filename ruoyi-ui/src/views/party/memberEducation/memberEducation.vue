<template>
  <div>
    <el-card shadow="always" style="margin-bottom: 30px;">
      <div slot="header" style="height: 25px">
        <span style="font-weight: bold;font-size: 16px">教育经历</span>
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="small"
          @click="handleAdd"
          v-hasPermi="['party:memberEducation:add']"
          style="float: right;margin-top: -5px"
          v-show="!disabled"
        >新增
        </el-button>
      </div>

      <el-table :border="true"  :stripe="true"
                v-loading="loading" :data="memberEducationList" @selection-change="handleSelectionChange">
        <el-table-column label="序号" align="center" type="index" />
        <el-table-column label="学历" align="center" prop="educationType" :formatter="educationTypeFormat"/>
        <el-table-column label="学校" align="center" prop="school"/>
        <el-table-column label="入学时间" align="center" prop="startDate" width="180">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.startDate, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="毕业时间" align="center" prop="endDate" width="180">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.endDate, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="专业" align="center" prop="major"/>
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
              v-show="!disabled"
              size="small"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['party:memberEducation:edit']"
            >修改
            </el-button>
            <el-button
              v-show="!disabled"
              size="small"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['party:memberEducation:remove']"
            >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

    </el-card>

    <!-- 添加或修改教育经历对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="80%" append-to-body
               :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" :style="bodyStyle" label-width="150px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="学历">
              <el-select :disabled="disabled1" v-model="form.educationType"
                         style="width: 100%" placeholder="请选择学历">
                <el-option
                  v-for="dict in educationTypeOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学校" prop="school">
              <el-input :disabled="disabled1" v-model="form.school" placeholder="请输入学校"/>
            </el-form-item>
          </el-col>
        </el-row>


        <el-row>
          <el-col :span="12">
            <el-form-item label="入学时间" prop="startDate">
              <el-date-picker :disabled="disabled1" clearable size="small" style="width: 100%"
                              v-model="form.startDate"
                              type="date"
                              value-format="yyyy-MM-dd"
                              placeholder="选择入学时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="毕业时间" prop="endDate">
              <el-date-picker  :disabled="disabled1" clearable size="small" style="width: 100%"
                              v-model="form.endDate"
                              type="date"
                              value-format="yyyy-MM-dd"
                              placeholder="选择毕业时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="专业" prop="major">
              <el-input  :disabled="disabled1" v-model="form.major" placeholder="请输入专业"/>
            </el-form-item>
          </el-col>
        </el-row>

      </el-form>
      <div slot="footer" class="dialog-footer" :style="{textAlign:'center'}">
        <el-button v-if="!disabled1"  type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    pageMemberEducation,
    listMemberEducation,
    getMemberEducation,
    delMemberEducation,
    addMemberEducation,
    updateMemberEducation,
    exportMemberEducation
  } from "@/api/party/memberEducation";

  export default {
    name: "MemberEducation",
    props: {
      disabled: {
        type: Boolean,
        default: () => {
          return true
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
        // 教育经历表格数据
        memberEducationList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 学历字典
        educationTypeOptions: [],
        // 查询参数
        queryParams: {
          educationUuid: undefined,
          educationType: undefined,
          school: undefined,
          startDate: undefined,
          endDate: undefined,
          major: undefined,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          educationType: [
            {required: true, message: "学历不能为空", trigger: "blur"}
          ],
          school: [
            {required: true, message: "学校不能为空", trigger: "blur"}
          ],
          startDate: [
            {required: true, message: "入学时间不能为空", trigger: "blur"}
          ],
          endDate: [
            {required: true, message: "毕业时间不能为空", trigger: "blur"}
          ],
          /*major: [
            {required: true, message: "专业不能为空", trigger: "blur"}
          ],*/
        },
        bodyStyle: {
          overflowY: 'auto',
          height: '',
          marginLeft: '2%',
          paddingRight: '2%',
        },
        educationUuid:undefined,
        disabled1:false,
      };
    },
    mounted() {
      //window.addEventListener('resize', this.getHeight);
    },
    created() {
      this.getDicts("education_type1").then(response => {
        this.educationTypeOptions = response.data;
      });
    },
    methods: {
      init(educationUuid){
        this.educationUuid = educationUuid
        this.getList();
      },
      /** 对话框自适应高度 */
      getHeight() {
        this.bodyStyle.height = window.innerHeight - 281 + 'px';
      },
      /** 查询教育经历列表 */
      getList() {
        this.loading = true;
        this.queryParams.educationUuid = this.educationUuid;
        listMemberEducation(this.queryParams).then(response => {
          console.log(response.data);
          this.memberEducationList = response.data;
          this.loading = false;
        });
      },
      // 学历字典翻译
      educationTypeFormat(row, column) {
        return this.selectDictLabel(this.educationTypeOptions, row.educationType);
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          educationId: undefined,
          educationUuid: undefined,
          educationType: undefined,
          school: undefined,
          startDate: undefined,
          endDate: undefined,
          major: undefined,
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
        this.ids = selection.map(item => item.educationId)
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.form.educationUuid = this.educationUuid;
        this.open = true;
        this.title = "添加教育经历";
        this.disabled1 =false;
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const educationId = row.educationId || this.ids
        getMemberEducation(educationId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改教育经历";
          this.disabled1 =false;
        });
      },
      handleSee(row) {
        this.reset();
        const educationId = row.educationId || this.ids
        getMemberEducation(educationId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "查看教育经历";
          this.disabled1 =true;
        });
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.educationId != undefined) {
              updateMemberEducation(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addMemberEducation(this.form).then(response => {
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
        const educationIds = row.educationId || this.ids;
        this.$confirm('是否确认删除教育经历该数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delMemberEducation(educationIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有教育经历数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return exportMemberEducation(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function () {
        });
      }
    }
  };
</script>
