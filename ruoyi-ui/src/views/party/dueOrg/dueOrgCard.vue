<template>
  <div>
    <el-card shadow="always" style="margin-bottom: 30px;">
      <div slot="header" style="height: 25px">
        <span style="font-weight: bold;font-size: 16px">党费填报党组织</span>
        <el-button
          v-if="!disabled"
          type="primary"
          icon="el-icon-plus"
          size="mini"
          v-hasPermi="['party:dueOrg:add']"
          @click="handleAdd"
          style="float: right;margin-top: -5px"
        >新增
        </el-button>
      </div>
      <el-table :stripe="true"
                :border="true"
                v-loading="loading" :data="dueOrgList" @selection-change="handleSelectionChange">
        <el-table-column label="序号" align="center" type="index"/>
        <el-table-column label="党组织" align="center" prop="partyOrg" :formatter="partyOrgFormat"/>
        <el-table-column label="状态" align="center" prop="status" :formatter="statusFormat"/>
        <el-table-column label="操作" align="center" width="180" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-search"
              @click="handleSee(scope.row)"
            >查看
            </el-button>
            <el-button
              v-if="!disabled"
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['party:dueOrg:remove']"
            >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <org-transfer  ref="orgTransfer" @chooseOrgs="chooseOrgs" ></org-transfer>

    <!-- 添加或修改党组织党费对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="90%" append-to-body
               @open="getHeight"  :close-on-click-modal="false">

      <div :style="bodyStyle">
        <due-card ref="dueCard" :disabled="disabled1" :dueOrgId="dueOrgId" @ok="cancel" ></due-card>
      </div>


      <div slot="footer" class="dialog-footer"  :style="{textAlign:'center'}">
        <el-button v-if="!disabled" type="primary" @click="submitDue(1)">保 存</el-button>
        <el-button v-if="!disabled" type="primary" @click="submitDue(2)">提 交</el-button>
        <el-button @click="cancel">关 闭</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
  import {listDueOrg, getDueOrg, delDueOrg, addDueOrgList,addDueOrg, updateDueOrg, exportDueOrg} from "@/api/party/dueOrg";
  import OrgTransfer from "../../../components/OrgTransfer/OrgTransfer";
  import DueCard from "../due/dueCard";

  export default {
    name: "DueOrgCard",
    components: {DueCard, OrgTransfer},
    props: {
      disabled: {
        type: Boolean,
        default: () => {
          return false
        }
      },
      duePlanUuid: {
        type: String,
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
        // 党组织党费表格数据
        dueOrgList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          duePlanUuid: undefined,
          partyOrgId: undefined,
          status: undefined,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          duePlanUuid: [
            {required: true, message: "党费计划UUID不能为空", trigger: "blur"}
          ],
          partyOrgId: [
            {required: true, message: "党组织id不能为空", trigger: "blur"}
          ],
          status: [
            {required: true, message: "状态(1：未提交  2：已提交)不能为空", trigger: "blur"}
          ],
        },
        bodyStyle: {
          overflowY: 'auto',
          height: '',
          marginLeft: '2%',
          paddingRight: '2%',
        },
        // 是否提交字典
        statusOptions:[],
        disabled1: false,
        dueOrgId:'',
      };
    },
    mounted() {
      window.addEventListener('resize', this.getHeight);
    },
    created() {
      this.getList();
      this.getDicts("submit_status").then(response => {
        this.statusOptions = response.data;
      });
    },
    watch:{
      'duePlanUuid'(val){
        this.getList();
      }
    },
    methods: {
      partyOrgFormat(row, column) {
        let partyOrgFullName = row.partyOrg.partyOrgFullName;
        return partyOrgFullName.substring(partyOrgFullName.indexOf("/")+1);
      },
      statusFormat(row, column) {
        return this.selectDictLabel(this.statusOptions, row.status);
      },
      chooseOrgs(partyOrgIds){
        let formData = new FormData();
        formData.append("duePlanUuid", this.duePlanUuid);
        formData.append("partyOrgIds", partyOrgIds.join(","));
        addDueOrgList(formData).then(response => {
          if (response.code === 200) {
            this.msgSuccess("新增成功");
            this.getList();
          } else {
            this.msgError(response.msg);
          }
        });
      },
      /** 对话框自适应高度 */
      getHeight() {
        this.bodyStyle.height = window.innerHeight - 281 + 'px';
      },
      /** 查询党组织党费列表 */
      getList() {
        this.loading = true;
        this.queryParams.duePlanUuid = this.duePlanUuid
        listDueOrg(this.queryParams).then(response => {
          this.dueOrgList = response.data;
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
          dueOrgId: undefined,
          duePlanUuid: undefined,
          partyOrgId: undefined,
          status: "0",
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
        this.ids = selection.map(item => item.dueOrgId)
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.$refs.orgTransfer.open = true;
        this.$refs.orgTransfer.getPartyOrgSelect(1);
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const dueOrgId = row.dueOrgId || this.ids
        getDueOrg(dueOrgId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改党组织党费";
        });
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.dueOrgId != undefined) {
              updateDueOrg(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addDueOrg(this.form).then(response => {
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
      handleSee(row) {
        this.reset();
        this.disabled1 = true;
        this.dueOrgId = row.dueOrgId
        this.open = true;
        this.title = "党组织党费查看";
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const dueOrgIds = row.dueOrgId || this.ids;
        this.$confirm('是否确认删除该数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delDueOrg(dueOrgIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有党组织党费数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return exportDueOrg(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function () {
        });
      }
    }
  };
</script>
