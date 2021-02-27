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

      <el-form-item label="处理状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择是否已处理" clearable size="small">
          <el-option
            v-for="dict in statusOptions"
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

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['members:suggestions:add']"
        >新增</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="suggestionsList" @selection-change="handleSelectionChange">
      <el-table-column label="序号" align="center" type="index" />

      <el-table-column label="党员姓名" align="center" prop="partyMember.memberName" />
      <el-table-column label="党组织名称" align="center" prop="partyOrg.partyOrgFullName" />
      <el-table-column label="创建日期" align="center" prop="recordTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.recordTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="内容" align="center" prop="suggestionsContent" />
      <el-table-column label="处理状态" align="center" prop="status" :formatter="statusFormat" />
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
            v-if="scope.row.status !='Y'"
            v-hasPermi="['members:suggestions:edit']"
          >处理</el-button>
          <!--<el-button
            size="small"
            type="text"
            icon="el-icon-delete"
            v-if="scope.row.status !='Y'"
            @click="handleDelete(scope.row)"
            v-hasPermi="['members:suggestions:remove']"
          >删除</el-button>-->
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

    <!-- 添加或修改党员建议对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="90%" append-to-body
               @open="getHeight"  :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" :style="bodyStyle"  label-width="150px">
        <el-card shadow="always" style="margin-bottom: 30px;">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">基本信息</span>
          </div>
          <el-row>
            <el-col :span="12">
              <el-form-item label="党员姓名" prop="memberId">
                <el-input v-model="form.partyMemberName"
                          placeholder="请输入党员姓名" :disabled="true"  />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="党组织名称" prop="partyOrgId">
                <el-input  v-model="form.partyOrgName"
                           placeholder="请输入党组织名称" :disabled="true" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="内容" prop="suggestionsContent">
                <el-input v-model="form.suggestionsContent" :disabled="seeFlag || !addFlag"
                          type="textarea" :autosize="{ minRows: 6, maxRows: 6}"
                          placeholder="请输入内容" />
              </el-form-item>
            </el-col>

          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="创建日期" prop="recordTime">
                <el-date-picker clearable size="small" style="width: 100%" :disabled="seeFlag || !addFlag"
                                v-model="form.recordTime"
                                type="date"
                                value-format="yyyy-MM-dd"
                                placeholder="选择填写日期">
                </el-date-picker>
              </el-form-item>
            </el-col>

          </el-row>

          <el-row>
            <el-col :span="24">
              <el-form-item label="附件" >
                <upload-all-file  ref="uploadAllFile" :disabled="seeFlag || !addFlag" />
              </el-form-item>
            </el-col>

          </el-row>
        </el-card>

        <el-card v-if="!addFlag" shadow="always" style="margin-bottom: 30px;">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">处理信息</span>
          </div>
          <el-row>
            <el-col :span="12">
              <el-form-item label="处理人" prop="handleMemberId">
                <el-input v-model="form.handlePartyMemberName" :disabled="true"
                          placeholder="请输入处理人" />
              </el-form-item>
            </el-col>
            <el-row>
              <el-col :span="12">
                <el-form-item label="处理日期" prop="handleTime">
                  <el-date-picker clearable size="small" style="width: 100%"
                                  v-model="form.handleTime"
                                  :disabled="seeFlag"
                                  type="date"
                                  value-format="yyyy-MM-dd"
                                  placeholder="选择处理日期">
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="处理情况" prop="handleContent">
                <el-input v-model="form.handleContent" type="textarea"
                          :disabled="seeFlag"
                          :autosize="{ minRows: 6, maxRows: 6}" placeholder="请输入处理情况内容" />
              </el-form-item>
            </el-col>
          </el-row>
        </el-card>
      </el-form>


      <div slot="footer" class="dialog-footer"  :style="{textAlign:'center'}">
        <el-button v-show="!seeFlag" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { listSuggestions, getSuggestions, delSuggestions, addSuggestions, updateSuggestions, exportSuggestions } from "@/api/members/suggestions";
  import {getUserProfile} from "@/api/system/user";
  import UploadAllFile from "../../upload/uploadAllFile";
  import {partyOrgTreeselect, getPartyOrg} from "@/api/party/org";
  import selectTree from '../../components/selectTree';

  export default {
    name: "Suggestions",
    components: {UploadAllFile, selectTree},
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
        // 党员建议/心愿表格数据
        suggestionsList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 是否已处理 Y 是 N 否字典
        statusOptions: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          suggestionsUuid: undefined,
          memberId: undefined,
          partyOrgId: undefined,
          suggestionsContent: undefined,
          recordTime: undefined,
          handleContent: undefined,
          handleMemberId: undefined,
          handleTime: undefined,
          status: undefined,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          suggestionsContent: [
            { required: true, message: "内容不能为空", trigger: "blur" }
          ],
          recordTime: [
            { required: true, message: "创建日期不能为空", trigger: "blur" }
          ],
          handleContent: [
            { required: true, message: "处理情况不能为空", trigger: "blur" }
          ],
          handleTime: [
            { required: true, message: "处理日期不能为空", trigger: "blur" }
          ],
        },
        bodyStyle:{
          overflowY:'auto',
          height: '',
          marginLeft:'2%' ,
          paddingRight:'2%',
        },
        addFlag:false,
        seeFlag:false,
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
        this.statusOptions = response.data;
      });
    },
    methods: {
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
      /** 查询党员建议/心愿列表 */
      getList() {
        this.loading = true;

        if(this.queryParams.memberId ==undefined){
          this.queryParams.memberId = this.user.partyMemberId!=null?this.user.partyMemberId:undefined
          let roles = this.user.roles;
          if(roles && roles.length!=0){
            for(let i=0;i<roles.length;i++){
              //管理员角色 或党委
              if(roles[i].roleId == 1 || roles[i].roleId == 5){
                this.queryParams.memberId = undefined
                break;
              }
            }
          }
        }

        let path = this.$route.path;
        switch (path) {
          case "/party/suggestions/1" :
            this.queryParams.type = "1";
            break;
          case "/party/suggestions/2" :
            this.queryParams.type = "2";
            break;
          default:
            break;
        }


        listSuggestions(this.queryParams).then(response => {
          this.suggestionsList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 是否已处理 Y 是 N 否字典翻译
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
          suggestionsId: undefined,
          suggestionsUuid: undefined,
          memberId: undefined,
          partyOrgId: undefined,
          suggestionsContent: undefined,
          recordTime: undefined,
          handleContent: undefined,
          handleMemberId: undefined,
          handleTime: undefined,
          status: undefined,
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
        this.ids = selection.map(item => item.suggestionsId)
        this.single = selection.length!=1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.form.suggestionsUuid = this.uuid();
        this.open = true;
        this.title = "添加";
        this.addFlag = true;
        this.seeFlag = false;
        this.form.partyMemberName = this.user.djPartyMember!=null?this.user.djPartyMember.memberName:undefined;
        this.form.memberId = this.user.partyMemberId!=null?this.user.partyMemberId:undefined;
        this.form.partyOrgName = this.user.djPartyMember!=null?this.user.djPartyMember.djPartyOrg.partyOrgName:undefined;
        this.form.partyOrgId = this.user.djPartyMember!=null?this.user.djPartyMember.partyOrgId:undefined;
        this.$nextTick(function () {
          this.$refs.uploadAllFile.init(this.form.suggestionsUuid, 'suggestions') ;
        })
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        this.addFlag = false;
        this.seeFlag = false;
        const suggestionsId = row.suggestionsId || this.ids
        getSuggestions(suggestionsId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "处理";
          this.form.partyMemberName = this.form.partyMember.memberName
          this.form.partyOrgName = this.form.partyOrg.partyOrgName
          this.form.handlePartyMemberName = this.user.djPartyMember!=null?this.user.djPartyMember.memberName:undefined;
          this.form.handleMemberId = this.user.partyMemberId!=null?this.user.partyMemberId:undefined;
        }).then(()=>{
          this.$refs.uploadAllFile.init(this.form.suggestionsUuid, 'suggestions') ;
        });
      },
      handleSee(row) {
        this.reset();
        this.addFlag = false;
        this.seeFlag = true;
        const suggestionsId = row.suggestionsId || this.ids
        getSuggestions(suggestionsId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "查看";
          this.form.partyMemberName = this.form.partyMember.memberName
          this.form.partyOrgName = this.form.partyOrg.partyOrgName
          this.form.handlePartyMemberName = this.form.handleMember!=null?this.form.handleMember.memberName:undefined
        }).then(()=>{
          this.$refs.uploadAllFile.init(this.form.suggestionsUuid, 'suggestions') ;
        });
      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if(this.addFlag){
              this.form.status = 'N'
            }else{
              this.form.status = 'Y'
            }
            if (this.form.suggestionsId != undefined) {
              updateSuggestions(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addSuggestions(this.form).then(response => {
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
        const suggestionsIds = row.suggestionsId || this.ids;
        this.$confirm('是否确认删除编号为"' + suggestionsIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delSuggestions(suggestionsIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportSuggestions(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
      }
    }
  };
</script>
