<template>
  <div class="app-container">
    <!-- 添加或修改活动参与人对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="80%" append-to-body
               @open="getHeight"  :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" :style="bodyStyle"  label-width="150px">
        <el-form-item label="签到情况">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in statusOptions"
              :key="dict.dictValue"
              :label="dict.dictValue"
            >{{dict.dictLabel}}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer"  :style="{textAlign:'center'}">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { listMember, getMember, delMember, addMember, updateMember, exportMember } from "@/api/activity/member";

  export default {
    name: "Member",
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
        // 活动参与人表格数据
        memberList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 活动参与人参与状态字典
        statusOptions: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          planUuid: undefined,
          partyOrgId: undefined,
          partyMemberId: undefined,
          status: undefined,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          planUuid: [
            { required: true, message: "活动计划ID不能为空", trigger: "blur" }
          ],
          partyOrgId: [
            { required: true, message: "党组织ID不能为空", trigger: "blur" }
          ],
          partyMemberId: [
            { required: true, message: "党组织成员id不能为空", trigger: "blur" }
          ],
          status: [
            { required: true, message: "活动参与人参与状态不能为空", trigger: "blur" }
          ],
        },
        bodyStyle:{
          overflowY:'auto',
          height: '',
          marginLeft:'20%' ,
          paddingRight:'20%',
        },
      };
    },
    mounted () {
      window.addEventListener('resize', this.getHeight);
    },
    created() {
      this.getList();
      this.getDicts("activity_member_status").then(response => {
        this.statusOptions = response.data;
      });
    },
    methods: {
      /** 对话框自适应高度 */
      getHeight(){
        this.bodyStyle.height=window.innerHeight-281+'px';
      },
      /** 查询活动参与人列表 */
      getList() {
        this.loading = true;
        listMember(this.queryParams).then(response => {
          this.memberList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 活动参与人参与状态字典翻译
      statusFormat(row, column) {
        return this.selectDictLabel(this.statusOptions, row.status);
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          memberId: undefined,
          planUuid: undefined,
          partyOrgId: undefined,
          partyMemberId: undefined,
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
        this.ids = selection.map(item => item.memberId)
        this.single = selection.length!=1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加活动参与人";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const memberId = row.memberId || this.ids
        getMember(memberId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "签到";
        });
      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.memberId != undefined) {
              updateMember(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.$emit("callback");
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addMember(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("新增成功");
                  this.open = false;
                  this.$emit("callback");
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
        const memberIds = row.memberId || this.ids;
        this.$confirm('是否确认删除活动参与人编号为"' + memberIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delMember(memberIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有活动参与人数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportMember(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
      }
    }
  };
</script>
