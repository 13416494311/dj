<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="100px">
      <el-form-item label="党员姓名" prop="partyMemberId">
        <el-input
          v-model="queryParams.partyMember.memberName"
          placeholder="请输入党员姓名"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="党组织名称" prop="partyOrgId">
        <select-tree :value="queryParams.partyOrgId"
                     style="width:100%;"
                     :options="partyOrgOptions"
                     vModel="partyOrgId"
                     @selected="setVModelValue"
                     placeholder="请选择党组织"
        />
      </el-form-item>
      <el-form-item label="年度" prop="year">
        <el-date-picker
          style="width:100%;"
          v-model="queryParams.duePlan.year"
          type="year"
          format="yyyy"
          value-format="yyyy"
          @change="handleQuery"
          placeholder="选择年度">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="月份" prop="month">
        <el-select v-model="queryParams.month"
                   placeholder="请选择月份"
                   style="width:100%;"
                   @change="handleQuery"
                   clearable size="small">
          <el-option
            v-for="dict in monthOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>


    <el-table :stripe="true"
              :border="true"
              v-loading="loading" :data="dueList" @selection-change="handleSelectionChange">
      <el-table-column label="序号" align="center" type="index"/>
      <el-table-column label="党员姓名" align="center" prop="partyMember.memberName"/>
      <el-table-column label="党组织名称" align="center" prop="partyOrg.partyOrgFullName" width="350"
                       :formatter="partyOrgFormat"/>
      <el-table-column label="年度" align="center" prop="duePlan.year" :formatter="yearFormat"/>
      <el-table-column label="月份" align="center" prop="month" :formatter="monthFormat"/>
      <el-table-column label="工资" align="center" prop="salary" :formatter="salaryFormat"/>
      <el-table-column label="比列" align="center" prop="ratio"/>
      <el-table-column label="党费" align="center" prop="due" :formatter="dueFormat"/>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

  </div>
</template>

<script>
  import {addDue, delDue, exportDue, getDue, listDue, updateDue} from "@/api/party/due";
  import {partyOrgTreeselect, getPartyOrg} from "@/api/party/org";
  import selectTree from '../../components/selectTree';

  export default {
    name: "Due",
    components: {selectTree},
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
        // 党员党费表格数据
        dueList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          dueOrgId: undefined,
          partyMemberId: undefined,
          month: undefined,
          salary: undefined,
          ratio: undefined,
          due: undefined,
          partyOrgId: undefined,
          dueOrg:{
            status:2
          },
          partyOrg: {
            partyOrgId: undefined
          },
          partyMember: {
            memberName: undefined
          },
          duePlan: {
            year: undefined
          }
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          dueOrgId: [
            {required: true, message: "党费计划党组织关联id不能为空", trigger: "blur"}
          ],
          partyMemberId: [
            {required: true, message: "党员ID不能为空", trigger: "blur"}
          ],
          month: [
            {required: true, message: "月份不能为空", trigger: "blur"}
          ],
          salary: [
            {required: true, message: "工资不能为空", trigger: "blur"}
          ],
          ratio: [
            {required: true, message: "比列不能为空", trigger: "blur"}
          ],
          due: [
            {required: true, message: "党费不能为空", trigger: "blur"}
          ],
        },
        bodyStyle: {
          overflowY: 'auto',
          height: '',
          marginLeft: '20%',
          paddingRight: '20%',
        },
        //组织架构
        partyOrgOptions: [],
        //月份
        monthOptions: [],
      };
    },
    mounted() {
      window.addEventListener('resize', this.getHeight);
    },
    created() {
      this.getList();
      //组织架构树
      this.getPartyOrgTreeSelect();
      this.getDicts("month_type").then(response => {
        this.monthOptions = response.data;
      });
    },
    methods: {
      // 年度字典翻译
      yearFormat(row, column) {
        return row.duePlan.year + "年";
      },
      monthFormat(row, column) {
        return row.month + "月";
      },
      partyOrgFormat(row, column) {
        let partyOrgFullName = row.partyOrg.partyOrgFullName;
        return partyOrgFullName.substring(partyOrgFullName.indexOf("/") + 1);
      },
      salaryFormat(row, column) {
        if (row.salary) {
          return row.salary.toFixed(2) + " 元";
        } else {
          return '';
        }
      },
      dueFormat(row, column) {
        if (row.due) {
          return row.due.toFixed(0) + " 元";
        } else {
          return '';
        }
      },
      /** 对话框自适应高度 */
      getHeight() {
        this.bodyStyle.height = window.innerHeight - 281 + 'px';
      },
      /** 查询党员党费列表 */
      getList() {
        this.loading = true;
        console.log(this.queryParams);
        listDue(this.queryParams).then(response => {
          this.dueList = response.rows;
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
          dueMemberId: undefined,
          dueOrgId: undefined,
          partyMemberId: undefined,
          month: undefined,
          salary: undefined,
          ratio: undefined,
          due: undefined,
          delFlag: undefined,
          createBy: undefined,
          createTime: undefined,
          updateBy: undefined,
          updateTime: undefined
        };
        this.resetForm("form");
      },
      //获取组织架构树
      getPartyOrgTreeSelect() {
        partyOrgTreeselect().then(response => {
          this.partyOrgOptions = this.treeInitData(response.data);
        });
      },
      //下拉树选择后设置值
      setVModelValue(vModel, val) {
        if (val != null) {
          this.queryParams[vModel] = val;
          this.queryParams.partyOrg = {
            partyOrgId:val
          }
        } else {
          this.queryParams[vModel] = undefined;
          this.queryParams.partyOrg = {
            partyOrgId:val
          }
        }
        this.handleQuery();
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
          dueOrgId: undefined,
          partyMemberId: undefined,
          month: undefined,
          salary: undefined,
          ratio: undefined,
          due: undefined,
          partyOrgId: undefined,
          partyOrg: {
            partyOrgId: undefined
          },
          partyMember: {
            memberName: undefined
          },
          duePlan: {
            year: undefined
          }
        },
          this.handleQuery();
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.dueMemberId)
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加党员党费";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const dueMemberId = row.dueMemberId || this.ids
        getDue(dueMemberId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改党员党费";
        });
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.dueMemberId != undefined) {
              updateDue(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addDue(this.form).then(response => {
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
        const dueMemberIds = row.dueMemberId || this.ids;
        this.$confirm('是否确认删除党员党费编号为"' + dueMemberIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delDue(dueMemberIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有党员党费数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return exportDue(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function () {
        });
      }
    }
  };
</script>
