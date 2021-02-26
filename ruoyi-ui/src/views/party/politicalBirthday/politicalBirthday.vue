<template>
  <div class="app-container">
    <el-form v-if="!see":model="queryParams" ref="queryForm" :inline="true" label-width="100px">

      <el-form-item label="党员名称" prop="memberId">
        <el-input
          v-model="queryParams.memberId"
          placeholder="请输入党员名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="党组织名称" prop="partyOrgId">
        <select-tree :value="queryParams.partyOrgId"
                     :options="partyOrgOptions"
                     vModel="partyOrgId"
                     @selected="setVModelValue"
                     placeholder="请选择党组织"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row v-if="!see":gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['party:politicalBirthday:add']"
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
          v-hasPermi="['party:politicalBirthday:edit']"
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
          v-hasPermi="['party:politicalBirthday:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['party:politicalBirthday:export']"
        >导出
        </el-button>
      </el-col>-->
    </el-row>

    <el-table :stripe="true"
              :border="true"
              v-loading="loading" :data="politicalBirthdayList" @selection-change="handleSelectionChange">
      <!--<el-table-column v-if="!see" type="selection" width="55" align="center"/>-->
      <el-table-column label="序号" align="center" type="index" />
      <el-table-column label="主题" align="center" prop="theme"/>
      <el-table-column label="党员名称" align="center" prop="partyMember.memberName"/>
      <el-table-column label="党组织名称" align="center" prop="partyOrg.partyOrgFullName"/>
      <el-table-column label="入党日期" align="center" prop="partyMember.joinData" width="150">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.partyMember.joinData, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="政治生日日期" align="center" prop="politicalBirthday" width="150">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.politicalBirthday, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="党龄" align="center" prop="politicalAge" width="60" :formatter="politicalAgeFormat"/>
      <el-table-column label="发布日期" align="center" prop="sendTime" width="150">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.sendTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="small"
            type="text"
            icon="el-icon-star-off"
            @click="handleSee(scope.row)"
          >点评
          </el-button>
          <el-button
            v-if="!see"
            size="small"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['party:politicalBirthday:edit']"
          >修改
          </el-button>
          <el-button
            v-if="!see"
            size="small"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['party:politicalBirthday:remove']"
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

    <!-- 添加或修改政治生日对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="90%" append-to-body
               @open="getHeight" :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" :style="bodyStyle" label-width="150px">

        <el-row>
          <el-col :span="12">
            <el-form-item label="党员名称" prop="memberId">
              <el-input :disabled="true" v-model="form.memberName" placeholder="请选择换党员名称">
                <el-button :disabled="disabled" slot="append" icon="el-icon-search"
                           @click="openMemberChoose"></el-button>
              </el-input>
            </el-form-item>

          </el-col>
          <el-col :span="12">
            <el-form-item label="党组织名称" prop="partyOrgId">
              <select-tree :disabled="true"
                           :value="form.partyOrgId"
                           :options="partyOrgOptions"
                           vModel="partyOrgId"
                           @selected="setVModelValue"
                           placeholder="请选择党组织"
              />
            </el-form-item>
          </el-col>
        </el-row>


        <el-row>
          <el-col :span="12">
            <el-form-item label="入党日期" prop="politicalBirthday">
              <el-date-picker clearable size="small" style="width: 100%"
                              :disabled="true"
                              v-model="form.joinData"
                              type="date"
                              value-format="yyyy-MM-dd"
                              placeholder="选择入党日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="政治生日日期" prop="politicalBirthday">
              <el-date-picker clearable size="small"  style="width: 100%"
                              v-model="form.politicalBirthday"
                              type="date"
                              @change="calculateAge"
                              value-format="yyyy-MM-dd"
                              placeholder="选择政治生日日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>


        <el-row>
          <el-col :span="12">
            <el-form-item label="党龄" prop="politicalAge">
              <el-input v-model="form.politicalAge":disabled="true" placeholder="请输入党龄"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="主题" prop="theme">
              <el-input v-model="form.theme" placeholder="请输入主题"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发布日期" prop="sendTime">
              <el-date-picker clearable size="small" style="width: 100%"
                              v-model="form.sendTime"
                              type="date"
                              value-format="yyyy-MM-dd"
                              placeholder="选择发布日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="内容">
              <Editor v-model="form.content" />
            </el-form-item>
          </el-col>
        </el-row>

      </el-form>
      <div slot="footer" class="dialog-footer" :style="{textAlign:'center'}">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>


    <party-member ref="partyMember" @callbackMember="setMember"/>

    <political-birthday-view ref="politicalBirthdayView"></political-birthday-view>
  </div>
</template>

<script>
  import {
    listPoliticalBirthday,
    getPoliticalBirthday,
    delPoliticalBirthday,
    addPoliticalBirthday,
    updatePoliticalBirthday,
    exportPoliticalBirthday
  } from "@/api/party/politicalBirthday";
  import selectTree from '../../components/selectTree';
  import {partyOrgTreeselect} from "@/api/party/org";
  import PartyMember from "../org/partyMemberChoose";
  import Editor from '@/components/Editor';
  import { getPartyMember,} from "@/api/party/member";
  import PoliticalBirthdayView from "./politicalBirthdayView";

  export default {
    name: "PoliticalBirthday",
    props: {
      see: {
        type: Boolean,
        default: () => {
          return false
        }
      },
    },
    components: {PoliticalBirthdayView, Editor,selectTree, PartyMember, },
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
        // 政治生日表格数据
        politicalBirthdayList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          birthdayUuid: undefined,
          memberId: undefined,
          partyOrgId: undefined,
          politicalBirthday: undefined,
          politicalAge: undefined,
          sendTime: undefined,
          theme: undefined,
          content: undefined,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          memberId: [
            {required: true, message: "党员ID不能为空", trigger: "blur"}
          ],
          partyOrgId: [
            {required: true, message: "组织机构id不能为空", trigger: "blur"}
          ],
          politicalBirthday: [
            {required: true, message: "政治生日日期不能为空", trigger: "blur"}
          ],
          politicalAge: [
            {required: true, message: "党龄不能为空", trigger: "blur"}
          ],
          sendTime: [
            {required: true, message: "发布日期不能为空", trigger: "blur"}
          ],
          theme: [
            {required: true, message: "主题不能为空", trigger: "blur"}
          ],
          content: [
            {required: true, message: "内容不能为空", trigger: "blur"}
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
        politicalBirthdayContentTemplate: undefined,
        partyMember:{},

      };
    },
    mounted() {
      window.addEventListener('resize', this.getHeight);
    },
    created() {
      this.getList();
      this.getTreeselect();
      this.getConfigKey("politicalBirthday.content").then(response => {
        this.politicalBirthdayContentTemplate= response.msg;
      });
    },
    methods: {
      politicalAgeFormat(row, column,value) {
        return value+"  年";
      },
      setMember(member){
        this.form.memberId = member.memberId;
        this.form.memberName = member.memberName;
        getPartyMember(this.form.memberId).then(response => {
          this.partyMember = response.data;
          this.form.partyOrgId  = this.partyMember.partyOrgId;
          this.form.joinData = this.partyMember.joinData;
          this.form.theme = '党员'+this.partyMember.memberName+'的政治生日'
        }).then(() => {
          this.calculateAge();
        });
      },
      calculateAge(){
        if(this.form.joinData && this.form.politicalBirthday ){
          let sDate = new Date(Date.parse(this.form.joinData.replace(/-/g, "/")));
          let eDate = new Date(Date.parse(this.form.politicalBirthday.replace(/-/g, "/")));
          let sY  = sDate.getFullYear();
          let eY  = eDate.getFullYear();
          this.form.politicalAge = eY-sY
          this.setContent();
        }
        this.$forceUpdate();
      },
      setContent(){
        if(this.form.joinData && this.form.politicalBirthday && this.partyMember  ){
          this.form.content = this.politicalBirthdayContentTemplate.replace("{{memberName}}",this.partyMember.memberName)
            .replace("{{politicalAge}}",this.form.politicalAge)
            .replace("{{sendTime}}",this.parseTime(this.getNowFormatDate(), '{y}-{m}-{d}') )
            .replace("{{partyOrgName}}",this.partyMember.djPartyOrg.partyOrgName)
        }
      },
      openMemberChoose(){
        this.$refs.partyMember.open = true ;
        this.$refs.partyMember.title = "选择党员";
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
        }else{
          this.form[vModel] =  undefined;
        }
      },
      /** 对话框自适应高度 */
      getHeight() {
        this.bodyStyle.height = window.innerHeight - 281 + 'px';
      },
      /** 查询政治生日列表 */
      getList() {
        this.loading = true;
        listPoliticalBirthday(this.queryParams).then(response => {
          this.politicalBirthdayList = response.rows;
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
          birthdayId: undefined,
          birthdayUuid: undefined,
          memberId: undefined,
          memberName: undefined,
          partyOrgId: undefined,
          joinData: undefined,
          politicalBirthday: undefined,
          politicalAge: undefined,
          sendTime: undefined,
          theme: undefined,
          content: undefined,
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
        this.ids = selection.map(item => item.birthdayId)
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.form.birthdayUuid = this.uuid();
        this.title = "添加政治生日";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const birthdayId = row.birthdayId || this.ids
        getPoliticalBirthday(birthdayId).then(response => {
          this.form = response.data;
          this.form.memberName = this.form.partyMember.memberName
          this.form.joinData = this.form.partyMember.joinData
          this.open = true;
          this.title = "修改政治生日";
        });
      },
      /** 查看按钮操作 */
      handleSee(row) {
        this.reset();
        const birthdayId = row.birthdayId || this.ids
        getPoliticalBirthday(birthdayId).then(response => {
          this.form = response.data;
          this.$refs.politicalBirthdayView.init(this.form);
        });
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.birthdayId != undefined) {
              updatePoliticalBirthday(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addPoliticalBirthday(this.form).then(response => {
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
        const birthdayIds = row.birthdayId || this.ids;
        this.$confirm('是否确认删除政治生日编号为"' + birthdayIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delPoliticalBirthday(birthdayIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有政治生日数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return exportPoliticalBirthday(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function () {
        });
      }
    }
  };
</script>
