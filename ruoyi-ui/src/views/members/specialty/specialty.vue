<template>
  <div class="app-container">
    <el-form v-if="!see" :model="queryParams" ref="queryForm" :inline="true" label-width="100px">
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
      <el-form-item label="特长类别" prop="specialtyCategory">
        <el-select v-model="queryParams.specialtyCategory"
                   style="width:100%;"
                   clearable
                   placeholder="选择特长类别">
          <el-option
            v-for="dict in specialtyTypeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row v-if="!see" :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['members:specialty:add']"
        >新增
        </el-button>
      </el-col>
      <!--
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['members:specialty:edit']"
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
          v-hasPermi="['members:specialty:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['members:specialty:export']"
        >导出
        </el-button>
      </el-col>-->
    </el-row>

    <el-table v-loading="loading" :data="specialtyList" @selection-change="handleSelectionChange"
              :border="true"  :stripe="true">
      <!-- <el-table-column type="selection" width="55" align="center"/>-->
      <el-table-column label="序号" align="center" type="index" />
      <el-table-column v-if="!see" label="党员姓名" align="center" prop="djPartyMember.memberName"/>
      <el-table-column v-if="!see" label="党组织名称" align="center" prop="djPartyOrg.partyOrgFullName" />
      <el-table-column label="特长类别" align="center" prop="specialtyCategory"
                       :formatter="specialtyTypeFormat">
      </el-table-column>
      <el-table-column label="特长描述" align="center" prop="specialtyContent"/>
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
            size="small"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-if=" !see && user.userId == scope.row.createBy "
            v-hasPermi="['members:specialty:edit']"
          >修改
          </el-button>
          <el-button
            size="small"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-if=" !see && user.userId == scope.row.createBy "
            v-hasPermi="['members:specialty:remove']"
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

    <!-- 添加或修改党员特长对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="90%" append-to-body
               @open="getHeight" :close-on-click-modal="false">
      <div style="height: 50px;"></div>
      <el-form ref="form" :model="form" :rules="rules" :style="bodyStyle" label-width="150px">
        <!--<el-form-item label="用户ID" prop="memberId">
          <el-input v-model="form.memberId" placeholder="请输入用户ID"/>
        </el-form-item> -->

        <!-- <el-form-item label="党组织id" prop="partyOrgId">
          <el-input v-model="form.partyOrgId" placeholder="请输入党组织id"/>
        </el-form-item> -->
        <el-row>
          <el-col :span="12">
            <el-form-item label="党员姓名" prop="memberName">
              <!--<el-input v-model="form.memberName" placeholder="" :disabled="true"/>-->
              <el-input :disabled="true" v-model="form.memberName" placeholder="请选择党员">
                <el-button :disabled="disabled" slot="append" icon="el-icon-search"
                           @click="openMemberChoose"></el-button>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="党组织" prop="partyOrgID">
              <el-input v-model="form.partyOrgName" placeholder="" :disabled="true"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="特长类别" prop="specialtyCategory">
              <el-select v-model="form.specialtyCategory"
                         style="width:100%;"
                         :disabled="disabled"
                         clearable
                         placeholder="选择特长类别">
                <el-option
                  v-for="dict in specialtyTypeOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="特长描述" prop="specialtyContent">
              <el-input v-model="form.specialtyContent" type="textarea"
                        :disabled="disabled" :autosize="{ minRows: 8, maxRows: 8}"
                        placeholder="请输入特长内容"/>
            </el-form-item>
          </el-col>
        </el-row>



      </el-form>
      <div slot="footer" class="dialog-footer" :style="{textAlign:'center'}">
        <el-button v-if="!disabled" type="primary" @click="submitForm">提 交</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <party-member ref="partyMember" @callbackMember="setMember"/>
  </div>
</template>

<script>
  import {
    listSpecialty,
    getSpecialty,
    delSpecialty,
    addSpecialty,
    updateSpecialty,
    exportSpecialty
  } from "@/api/members/specialty";

  import {partyOrgTreeselect, getPartyOrg} from "@/api/party/org";
  import selectTree from '../../components/selectTree';
  import {getPartyMember} from "@/api/party/member";
  import partyMember from "../../party/org/partyMemberChoose";
  import {getUserProfile} from "@/api/system/user";

  export default {
    name: "Specialty",
    components: {
      partyMember, selectTree
    },
    props: {
      see: {
        type: Boolean,
        default: () => {
          return false
        }
      },
      partyOrgId: {
        type: Number,
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
        // 党员特长表格数据
        specialtyList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          memberId: undefined,
          memberName: undefined,
          partyOrgId: undefined,
          partyOrgName: undefined,
          specialtyCategory: undefined,
          specialtyContent: undefined,
        },
        //组织架构
        partyOrgOptions: [],
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          memberName: [
            {required: true, message: "名字不能为空", trigger: "blur"}
          ],
          // partyOrgID: [
          //   {required: true, message: "党组织不能为空", trigger: "blur"}
          // ],
          specialtyCategory: [
            {required: true, message: "特长类别不能为空", trigger: "blur"}
          ],
          specialtyContent: [
            {required: true, message: "特长描述不能为空", trigger: "blur"}
          ],
        },
        bodyStyle: {
          overflowY: 'auto',
          height: '',
          marginLeft: '2%',
          paddingRight: '2%',
        },
        //特长类别
        specialtyTypeOptions: [],
        disabled: false,
        user:{},
      };
    },
    mounted() {
      window.addEventListener('resize', this.getHeight);
    },
    created() {
      this.getUser();
      //组织架构树
      this.getPartyOrgTreeSelect();
      this.getDicts("specialty_type").then(response => {
        this.specialtyTypeOptions = response.data;
      });
    },
    methods: {
      init(memberId){
        this.queryParams.memberId = memberId
        this.getList();
      },
      getUser() {
        getUserProfile().then(response => {
          this.user = response.data;
        }).then(()=>{
          this.getList()
        })
      },
      /** 对话框自适应高度 */
      getHeight() {
        this.bodyStyle.height = window.innerHeight - 281 + 'px';
      },
      /** 查询党员特长列表 */
      getList() {
        this.loading = true;

        listSpecialty(this.queryParams).then(response => {
          this.specialtyList = response.rows;
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
          specialtyId: undefined,
          memberId: undefined,
          memberName: undefined,
          partyOrgId: undefined,
          partyOrgName: undefined,
          specialtyCategory: undefined,
          specialtyContent: undefined,
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
        this.ids = selection.map(item => item.specialtyId)
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.disabled = false;
        this.title = "添加党员特长";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const specialtyId = row.specialtyId || this.ids
        this.disabled = false;
        getSpecialty(specialtyId).then(response => {
          this.form = response.data;
          this.form.partyOrgName = this.form.djPartyOrg.partyOrgName;
          this.form.memberName = this.form.djPartyMember.memberName;
          this.open = true;
          this.title = "修改党员特长";
        });
      },
      /** 查看按钮操作 */
      handleSee(row) {
        this.reset();
        const specialtyId = row.specialtyId || this.ids;
        this.disabled = true;
        getSpecialty(specialtyId).then(response => {
          this.form = response.data;
          this.form.partyOrgName = this.form.djPartyOrg.partyOrgName;
          this.form.memberName = this.form.djPartyMember.memberName;
          this.open = true;
          this.title = "查看党员特长";
        });
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.specialtyId != undefined) {
              updateSpecialty(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addSpecialty(this.form).then(response => {
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
        const specialtyIds = row.specialtyId || this.ids;
        this.$confirm('是否确认删除党员特长编号为"' + specialtyIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delSpecialty(specialtyIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有党员特长数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return exportSpecialty(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function () {
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
      specialtyTypeFormat(row, column) {
        return this.selectDictLabel(this.specialtyTypeOptions, row.specialtyCategory);
      },
      //获取组织架构树
      getPartyOrgTreeSelect() {
        partyOrgTreeselect().then(response => {
          this.partyOrgOptions = this.treeInitData(response.data);
        });
      },
      //选择党员
      setMember(member) {
        this.form.memberId = member.memberId;
        this.form.memberName = member.memberName;
        getPartyMember(member.memberId).then(response => {
          let partyMember = response.data;
          this.form.partyOrgName = partyMember.djPartyOrg.partyOrgName;
          this.form.partyOrgId = partyMember.partyOrgId;
          this.$forceUpdate();
        });
      },
      openMemberChoose() {
        this.$refs.partyMember.open = true;
        this.$refs.partyMember.title = "选择党员";
      },
    }
  };
</script>
