<template>
  <div>
    <el-card shadow="always" style="margin-bottom: 30px;">
      <div slot="header" style="height: 25px">
        <span style="font-weight: bold;font-size: 16px">参与人</span>
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="small"
          @click="handleAdd()"
          v-hasPermi="['party:trainMember:add']"
          style="float: right;margin-top: -5px"
          v-show="!disabled"
        >新增
        </el-button>
      </div>
      <el-table v-loading="loading" :data="trainMemberList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column label="序号" width="55" align="center" type="index" />
        <el-table-column label="党组织名称" align="center" prop="partyOrg.partyOrgFullName" />
        <el-table-column label="党员姓名" align="center" prop="partyMember.memberName" />
        <el-table-column label="状态" align="center" prop="status" :formatter="memberStatusFormat"/>
        <el-table-column v-if="!disabled" label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              type="primary"
              icon="el-icon-delete"
              size="small"
              @click="handleDelete(scope.row)"
              v-hasPermi="['party:trainMember:remove']"
              v-show="!disabled"
            >删除
            </el-button>
            <el-dropdown split-button size="small" type="primary">签到
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item
                  v-for="dict in memberStatusOptions"
                  :key="dict.dictValue"
                  @click.native="updateTrainMember(scope.row,dict.dictValue)"
                >{{dict.dictLabel}}
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
      <div v-if="!disabled" style="margin-top: 10px">
        <el-radio-group v-model="memberStatus">
          <el-radio
            v-for="dict in memberStatusOptions"
            :key="dict.dictValue"
            :label="dict.dictValue"
          >{{dict.dictLabel}}
          </el-radio>
        </el-radio-group>
        <el-button
          :disabled="multiple"
          type="primary"
          size="mini"
          @click="updateTrainMembers"
          style="float:right;top: 5px"
        >签到
        </el-button>
      </div>
    </el-card>

    <member-choose ref="memberChoose" @callbackMember="addTrainMembers" />
  </div>
</template>

<script>
  import { listTrainMember, getTrainMember, delTrainMember,
    addTrainMember,addTrainMembers, updateTrainMember, updateTrainMembers,exportTrainMember } from "@/api/party/trainMember";
  import MemberChoose from "./memberChoose";

  export default {
    name: "TrainMember",
    components: {MemberChoose},
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
        // 参与培训党员表格数据
        trainMemberList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          trainUuid: undefined,
          partyOrgId: undefined,
          partyMemberId: undefined,
          score: undefined,
          status: undefined,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {

        },
        bodyStyle:{
          overflowY:'auto',
          height: '',
          marginLeft:'20%' ,
          paddingRight:'20%',
        },
        memberStatusOptions: [],
        trainUuid:undefined,
        partyOrgId:undefined,
        trainMemberIds: [],
        memberStatus: '1',
      };
    },
    mounted () {
      window.addEventListener('resize', this.getHeight);
    },
    created() {
      this.getDicts("activity_member_status").then(response => {
        this.memberStatusOptions = response.data;
      });
    },
    methods: {
      init(trainUuid,partyOrgId){
        this.trainUuid = trainUuid;
        this.partyOrgId = partyOrgId;
        this.getList();
      },
      addTrainMembers(members){
        this.reset();
        let formData = new FormData();
        formData.append("trainUuid",  this.trainUuid);
        formData.append("partyOrgId",  this.partyOrgId);
        formData.append("trainMemberIds", members);
        addTrainMembers(formData).then(response => {
          if (response.code === 200) {
            this.msgSuccess("新增成功");
            this.getList();
          } else {
            this.msgError(response.msg);
          }
        });
      },
      updateTrainMember(row, status){
        row.status = status
        updateTrainMember(row).then(response => {
          if (response.code === 200) {
            this.msgSuccess("修改成功");
            this.getList();
          } else {
            this.msgError(response.msg);
          }
        });
      },
      updateTrainMembers() {
        let formData = new FormData();
        formData.append("status", this.memberStatus);
        formData.append("trainMemberIds", this.trainMemberIds.join(","));
        updateTrainMembers(formData).then(response => {
          if (response.code === 200) {
            this.msgSuccess("修改成功");
            this.getList();
          } else {
            this.msgError(response.msg);
          }
        });

      },
      memberStatusFormat(row, column) {
        return this.selectDictLabel(this.memberStatusOptions, row.status);
      },
      /** 对话框自适应高度 */
      getHeight(){
        this.bodyStyle.height=window.innerHeight-281+'px';
      },
      /** 查询参与培训党员列表 */
      getList() {
        this.loading = true;
        this.queryParams.trainUuid = this.trainUuid
        listTrainMember(this.queryParams).then(response => {
          this.trainMemberList = response.rows;
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
          trainMemberId: undefined,
          trainUuid: undefined,
          partyOrgId: undefined,
          partyMemberId: undefined,
          score: undefined,
          status: "1",
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
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.trainMemberIds = selection.map(item => item.trainMemberId)
        this.single = selection.length!=1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        if(!this.partyOrgId){
          this.msgInfo("请先选择党组织!");
          return ;
        }
        this.$refs.memberChoose.open = true;
        this.$refs.memberChoose.title = "选择参与人";
        this.$refs.memberChoose.getPartyMemberSelect();

      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const trainMemberId = row.trainMemberId || this.ids
        getTrainMember(trainMemberId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改参与培训党员";
        });
      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.trainMemberId != undefined) {
              updateTrainMember(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addTrainMember(this.form).then(response => {
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
        const trainMemberIds = row.trainMemberId || this.ids;
        this.$confirm('是否确认删除参与培训党员编号为"' + trainMemberIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delTrainMember(trainMemberIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有参与培训党员数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportTrainMember(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
      }
    }
  };
</script>
