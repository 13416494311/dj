<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="100px">
      <el-form-item label="党员姓名" prop="memberName">
        <el-input
          v-model="queryParams.memberName"
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

      <el-form-item label="组织认定" prop="cognizance">
        <el-select v-model="queryParams.cognizance" placeholder="请选择组织认定" clearable size="small">
          <el-option
            v-for="dict in cognizanceOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="困难程度" prop="economicSituation">
        <el-select v-model="queryParams.economicSituation" placeholder="请选择困难程度" clearable size="small">
          <el-option
            v-for="dict in economicSituationOptions"
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


    <el-table :stripe="true" :border="true"   v-loading="loading" :data="helpList" @selection-change="handleSelectionChange">
      <el-table-column label="序号" align="center" type="index"/>
      <el-table-column label="党员姓名" align="center" prop="djPartyMember.memberName" />
      <el-table-column label="党组织名称" align="center" prop="djPartyOrg.partyOrgFullName" />
      <el-table-column label="组织认定" align="center" prop="cognizance" :formatter="cognizanceFormat" />
      <el-table-column label="困难程度" align="center" prop="economicSituation" :formatter="economicSituationFormat" />
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
            v-hasPermi="['members:help:edit']"
          >帮扶</el-button>
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

    <!-- 添加或修改党员帮扶对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="90%" append-to-body
               @open="getHeight"  :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" :style="bodyStyle"  label-width="150px">
        <el-card shadow="always" style="margin-bottom: 30px;">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">基本信息</span>
          </div>

          <el-row>
            <el-col :span="12">
              <el-form-item label="党员姓名" prop="partyMemberId">
                <el-input v-model="form.partyMemberName" placeholder="请输入党员姓名" :disabled="true"  />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="党组织名称" prop="partyOrgId">
                <el-input  v-model="form.partyOrgName" placeholder="请输入党组织名称" :disabled="true" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="12">
              <el-form-item label="组织认定" prop="cognizance">
                <el-select v-model="form.cognizance"
                           :disabled="disabled"
                           style="width: 100%"
                           placeholder="请选择组织认定">
                  <el-option
                    v-for="dict in cognizanceOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="困难程度" prop="economicSituation">
                <el-select v-model="form.economicSituation"
                           :disabled="disabled"
                           style="width: 100%"
                           placeholder="请选择困难程度">
                  <el-option
                    v-for="dict in economicSituationOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-card>

        <help-record ref="helpRecord" :disabled ="disabled"/>


      </el-form>
      <div slot="footer" class="dialog-footer"  :style="{textAlign:'center'}">
        <el-button v-show="!disabled" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { listHelp, getHelp, delHelp, addHelp, updateHelp, exportHelp } from "@/api/members/help";
  import HelpRecord from "./helpRecord";
  import {getUserProfile} from "@/api/system/user";
  import {partyOrgTreeselect, getPartyOrg} from "@/api/party/org";
  import selectTree from '../../components/selectTree';

  export default {
    name: "Help",
    components: {HelpRecord ,selectTree},
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
        // 党员帮扶表格数据
        helpList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 组织认定字典
        cognizanceOptions: [],
        // 困难程度字典
        economicSituationOptions: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          helpUuid: undefined,
          partyMemberId: undefined,
          partyOrgId: undefined,
          cognizance: undefined,
          economicSituation: undefined,
          status: undefined,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          partyMemberId: [
            { required: true, message: "党员ID不能为空", trigger: "blur" }
          ],
          partyOrgId: [
            { required: true, message: "党组织id不能为空", trigger: "blur" }
          ],
          cognizance: [
            { required: true, message: "组织认定不能为空", trigger: "blur" }
          ],
          economicSituation: [
            { required: true, message: "困难程度不能为空", trigger: "blur" }
          ],
        },
        bodyStyle:{
          overflowY:'auto',
          height: '',
          marginLeft:'2%' ,
          paddingRight:'2%',
        },
        disabled:false,
        //组织架构
        partyOrgOptions: [],
      };
    },
    mounted () {
      window.addEventListener('resize', this.getHeight);
    },
    created() {
      this.getUser();
      //组织架构树
      this.getPartyOrgTreeSelect();
      this.getDicts("sys_yes_no").then(response => {
        this.cognizanceOptions = response.data;
      });
      this.getDicts("economic_situation_type").then(response => {
        this.economicSituationOptions = response.data;
      });
    },
    methods: {
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
        } else {
          this.queryParams[vModel] = undefined;
        }
        this.handleQuery();
      },
      getUser() {
        getUserProfile().then(response => {
          this.user = response.data;
        }).then(()=>{
          this.getList()
        })
      },
      /** 对话框自适应高度 */
      getHeight(){
        this.bodyStyle.height=window.innerHeight-281+'px';
      },
      /** 查询党员帮扶列表 */
      getList() {
        this.loading = true;

        listHelp(this.queryParams).then(response => {
          this.helpList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 组织认定字典翻译
      cognizanceFormat(row, column) {
        return this.selectDictLabel(this.cognizanceOptions, row.cognizance);
      },
      // 困难程度字典翻译
      economicSituationFormat(row, column) {
        return this.selectDictLabel(this.economicSituationOptions, row.economicSituation);
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          helpId: undefined,
          helpUuid: undefined,
          partyMemberId: undefined,
          partyMemberName: undefined,
          partyOrgId: undefined,
          partyOrgName: undefined,
          cognizance: undefined,
          economicSituation: undefined,
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
        this.ids = selection.map(item => item.helpId)
        this.single = selection.length!=1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.form.helpUuid = this.uuid();
        this.title = "添加党员帮扶";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        this.disabled = false;
        const helpId = row.helpId || this.ids
        getHelp(helpId).then(response => {
          this.form = response.data;
          this.form.partyOrgName = this.form.djPartyOrg.partyOrgName;
          this.form.partyMemberName = this.form.djPartyMember.memberName;
          this.open = true;
          this.title = "修改党员帮扶";
        }).then(()=>{
          this.$refs.helpRecord.init(this.form.helpUuid) ;
        });
      },
      handleSee(row) {
        this.reset();
        this.disabled = true;
        const helpId = row.helpId || this.ids
        getHelp(helpId).then(response => {
          this.form = response.data;
          this.form.partyOrgName = this.form.djPartyOrg.partyOrgName;
          this.form.partyMemberName = this.form.djPartyMember.memberName;
          this.open = true;
          this.title = "查看党员帮扶";
        }).then(()=>{
          this.$refs.helpRecord.init(this.form.helpUuid) ;
        });
      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.helpId != undefined) {
              updateHelp(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addHelp(this.form).then(response => {
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
        const helpIds = row.helpId || this.ids;
        this.$confirm('是否确认删除党员帮扶编号为"' + helpIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delHelp(helpIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有党员帮扶数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportHelp(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
      }
    }
  };
</script>
