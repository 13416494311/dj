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
          v-hasPermi="['party:partyChange:add']"
        >新增
        </el-button>
      </el-col>
      <!--<el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['party:partyChange:edit']"
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
          v-hasPermi="['party:partyChange:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['party:partyChange:export']"
        >导出
        </el-button>
      </el-col>-->
    </el-row>

    <el-table v-loading="loading" :data="partyChangeList"  :stripe="true"
              :border="true" @selection-change="handleSelectionChange">
      <!--<el-table-column type="selection" width="55" align="center"/>-->
      <el-table-column label="党组织名称" align="left" prop="partyOrg.partyOrgFullName" width="800"/>
      <el-table-column label="换届时间" align="center" prop="changeTime" >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.changeTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status"
                       :formatter="statusFormat"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-search"
            @click="handleSee(scope.row)"
          >查看
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['party:partyChange:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['party:partyChange:remove']"
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

    <!-- 添加或修改党组织换届对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="90%" append-to-body
               @open="getHeight" :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" :style="bodyStyle" label-width="150px">
        <el-card shadow="always" style="margin-bottom: 30px;">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">基本信息</span>
          </div>
          <el-row>
            <el-col :span="12">
              <el-form-item label="党组织" prop="partyOrgId">
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
              <el-form-item label="换届时间" prop="changeTime">
                <el-date-picker clearable size="small" style="width: 100%"
                                v-model="form.changeTime"
                                :disabled="disabled"
                                type="date"
                                value-format="yyyy-MM-dd"
                                placeholder="选择换届时间">
                </el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
        </el-card>

        <change-detail ref="changeDetail" :disabled="disabled" />
      </el-form>
      <div slot="footer" class="dialog-footer" :style="{textAlign:'center'}">
        <el-button v-show="!disabled" type="primary" @click="submitForm(1)">保 存</el-button>
        <el-button v-show="!disabled" type="primary" @click="chooseAuditUser">提 交</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <choose-audit-user ref="chooseAuditUser"  @ok="handleSubmit" />

  </div>
</template>

<script>
  import {
    listPartyChange,
    getPartyChange,
    delPartyChange,
    addPartyChange,
    updatePartyChange,
    exportPartyChange
  } from "@/api/party/partyChange";
  import {partyOrgTreeselect} from "@/api/party/org";
  import selectTree from '../../components/selectTree';
  import ChangeDetail from "./changeDetail";
  import ChooseAuditUser from "../../audit/chooseAuditUser";
  export default {
    name: "PartyChange",
    components: {ChooseAuditUser,ChangeDetail, selectTree },
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
        // 党组织换届表格数据
        partyChangeList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          changeUuid: undefined,
          partyOrgId: undefined,
          changeTime: undefined,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          partyOrgId: [
            {required: true, message: "党组织不能为空", trigger: "blur"}
          ],
          changeTime: [
            {required: true, message: "换届时间不能为空", trigger: "blur"}
          ],
        },
        bodyStyle: {
          overflowY: 'auto',
          height: '',
          marginLeft: '2%',
          paddingRight: '2%',
        },
        // 党组织架构树选项
        partyOrgOptions: [],
        disabled: false,
        addFlag:false,
      };
    },
    mounted() {
      window.addEventListener('resize', this.getHeight);
    },
    created() {
      this.getList();
      this.getDicts("audit_state").then(response => {
        this.statusOptions = response.data;
      });
      this.getTreeselect()
    },
    watch:{
      'form.changeUuid'(val){
        if(val!= undefined ){
          this.$nextTick(()=>{
            this.$refs.changeDetail.init(val, '') ;
          })
        }
      }
    },
    methods: {
      statusFormat(row, column) {
        return this.selectDictLabel(this.statusOptions, row.status);
      },
      chooseAuditUser(){
        this.$refs.chooseAuditUser.init(9)
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
          this.$refs.changeDetail.init(this.form.changeUuid,val) ;
        }else{
          this.form[vModel] =  undefined;
        }
      },
      /** 查询部门下拉树结构 */
      getTreeselect() {
        partyOrgTreeselect().then(response => {
          this.partyOrgOptions = this.treeInitData(response.data);
        });
      },
      /** 对话框自适应高度 */
      getHeight() {
        this.bodyStyle.height = window.innerHeight - 281 + 'px';
      },
      /** 查询党组织换届列表 */
      getList() {
        this.loading = true;
        listPartyChange(this.queryParams).then(response => {
          this.partyChangeList = response.rows;
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
          changeId: undefined,
          changeUuid: undefined,
          partyOrgId: undefined,
          changeTime: undefined,
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
        this.ids = selection.map(item => item.changeId)
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.disabled = false;
        this.title = "添加党组织换届";
        this.form.changeUuid = this.uuid();
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const changeId = row.changeId || this.ids
        this.disabled = false;
        getPartyChange(changeId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改党组织换届";
        }).then(()=>{
          this.$refs.changeDetail.init(this.form.changeUuid, this.form.partyOrgId) ;
          this.$refs.changeDetail.disabled = this.disabled;
        });
      },
      /** 查看按钮操作 */
      handleSee(row) {
        this.reset();
        const changeId = row.changeId || this.ids
        this.disabled = true;
        getPartyChange(changeId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改党组织换届";
        }).then(()=>{
          this.$refs.changeDetail.init(this.form.changeUuid, this.form.partyOrgId) ;
          this.$refs.changeDetail.disabled = this.disabled;
        });
      },
      handleSubmit(form){
        this.form.params = {}
        this.form.params.auditUserId = form.auditUserId;
        this.form.params.operReason = form.reason;
        this.submitForm(2);
      },
      /** 提交按钮 */
      submitForm(status) {
        this.$refs["form"].validate(valid => {
          if (valid) {
            this.form.status = status;
            if (this.form.changeId != undefined) {
              updatePartyChange(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addPartyChange(this.form).then(response => {
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
        const changeIds = row.changeId || this.ids;
        this.$confirm('是否确认删除党组织换届编号为"' + changeIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delPartyChange(changeIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有党组织换届数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return exportPartyChange(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function () {
        });
      }
    }
  };
</script>
