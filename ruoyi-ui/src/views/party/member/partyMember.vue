<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="6" :xs="24">
        <div class="head-container">
          <el-input
            v-model="partyOrgName"
            placeholder="请输入党组织架构名称"
            clearable
            size="small"
            prefix-icon="el-icon-search"
            style="margin-bottom: 20px"
          />
        </div>
        <div class="head-container">
          <el-tree
            :data="partyOrgOptions"
            :props="defaultProps"
            :expand-on-click-node="false"
            :filter-node-method="filterNode"
            ref="tree"
            default-expand-all
            @node-click="handleNodeClick"
          />
        </div>
      </el-col>
      <el-col :span="18" :xs="24">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
          <el-form-item label="党员姓名" prop="memberName">
            <el-input
              v-model="queryParams.memberName"
              placeholder="请输入党员姓名"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="党员类型" prop="memberType">
            <el-select v-model="queryParams.memberType" placeholder="请选择党员类型" clearable size="small">
              <el-option
                v-for="dict in memberTypeOptions"
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
              v-hasPermi="['party:member:add']"
            >新增
            </el-button>
          </el-col>
          <!--<el-col :span="1.5">
            <el-button
              type="warning"
              icon="el-icon-download"
              size="mini"
              @click="handleExport"
              v-hasPermi="['party:member:export']"
            >导出
            </el-button>
          </el-col>-->
        </el-row>
        <el-table v-loading="loading" :data="partyMemberList" @selection-change="handleSelectionChange">
          <el-table-column label="党员姓名" align="center" prop="memberName"/>
          <el-table-column label="手机号" align="center" prop="mobile"/>
          <el-table-column label="部门" align="center" prop="deptId" :formatter="deptIdFormat" />
          <el-table-column label="党组织" align="center" prop="partyOrgId" :formatter="partyOrgIdFormat" />
          <el-table-column label="党员类型" align="center" prop="memberType" :formatter="memberTypeFormat"/>
          <el-table-column label="党员状态" align="center" prop="memberStatus" :formatter="memberStatusFormat"/>
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
                v-if="showHandleUpdate(scope.row)"
                v-hasPermi="['party:member:edit']"
              >修改
              </el-button>
              <el-button
                size="small"
                type="text"
                icon="el-icon-delete"
                @click="chooseAuditUser1(scope.row)"
                v-if="showHandleUpdate(scope.row)"
                v-hasPermi="['party:member:remove']"
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
      </el-col>
    </el-row>
    <!-- 添加或修改党员信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="90%" append-to-body
               @open="getHeight" :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" :style="bodyStyle" label-width="150px">
        <el-card shadow="always" style="margin-bottom: 30px;">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">基本信息</span>
          </div>
          <el-row>
            <el-col :span="16">
              <el-row>
                <el-col :span="12">
                  <el-form-item label="党员姓名" prop="memberName">
                    <el-input :disabled="disabled"v-model="form.memberName" placeholder="请输入党员姓名"/>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="手机号" prop="mobile">
                    <el-input :disabled="disabled"v-model="form.mobile" placeholder="请输入手机号"/>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="性别" prop="sex">
                    <el-select :disabled="disabled"
                               v-model="form.sex"
                               style="width: 100%" placeholder="请选择性别">
                      <el-option
                        v-for="dict in sexOptions"
                        :key="dict.dictValue"
                        :label="dict.dictLabel"
                        :value="dict.dictValue"
                      ></el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="工号" prop="workNo">
                    <el-input :disabled="disabled"v-model="form.workNo" placeholder="请输入工号"/>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="身份证号" prop="identityCard">
                    <el-input :disabled="disabled" v-model="form.identityCard" placeholder="请输入身份证号"/>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="出生日期" prop="birthday">
                    <el-date-picker :disabled="disabled"
                                    clearable size="small" style="width: 100%"
                                    v-model="form.birthday"
                                    type="date"
                                    value-format="yyyy-MM-dd"
                                    :picker-options="afterTimeOption"
                                    placeholder="选择出生日期">
                    </el-date-picker>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-col>
            <el-col :span="8">
              <el-upload
                :disabled="disabled"
                class="avatar-upload"
                action="#"
                :show-file-list="false"
                :http-request="uploadAtt"
                accept="image/jpeg,image/jpg,image/gif,image/png,">
                <div class="avatar">
                  <img v-if="form.avatar" :src="avatarUrl"  >
                  <div v-else class="avatar-background" ></div>
                </div>
              </el-upload>

            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="所在单位" prop="companyName">
                <el-input :disabled="disabled" v-model="form.companyName" placeholder="请输入所在单位"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="部门" prop="deptId">
                <select-tree :value="form.deptId"
                             :disabled="disabled"
                             :options="deptOptions"
                             vModel="deptId"
                             @selected="setVModelValue"
                             placeholder="请选择部门"
                />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="职务" prop="administrativePosition">
                <el-select :disabled="disabled"
                           v-model="form.administrativePosition"
                           style="width: 100%" placeholder="请选择职务">
                  <el-option
                    v-for="dict in administrativePositionOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="职称" prop="title">
                <el-input :disabled="disabled" v-model="form.title" placeholder="请输入职称"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="岗位" prop="postId">
                <el-select :disabled="disabled"
                           v-model="form.postId"
                           style="width: 100%" placeholder="请选择岗位">
                  <el-option
                    v-for="item in postOptions"
                    :key="item.postId"
                    :label="item.postName"
                    :value="item.postId"
                    :disabled="item.status == 1"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="参加工作日期" prop="workingDate">
                <el-date-picker :disabled="disabled"
                                clearable size="small"
                                style="width: 100%"
                                v-model="form.workingDate"
                                type="date"
                                value-format="yyyy-MM-dd"
                                :picker-options="afterTimeOption"
                                placeholder="选择参加工作日期">
                </el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="民族" prop="nation">
                <el-select :disabled="disabled"
                           v-model="form.nation"
                           style="width: 100%" placeholder="请选择民族">
                  <el-option
                    v-for="dict in nationOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="政治面貌" prop="polity">
                <el-select :disabled="disabled"
                           v-model="form.polity"
                           style="width: 100%" placeholder="请选择政治面貌">
                  <el-option
                    v-for="dict in polityOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="身份">
                <el-select :disabled="disabled"
                           v-model="form.workIdentity"
                           style="width: 100%" placeholder="请选择身份">
                  <el-option
                    v-for="dict in workIdentityOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="学历" prop="education">
                <el-select :disabled="disabled"
                           v-model="form.education"
                           style="width: 100%" placeholder="请选择学历">
                  <el-option
                    v-for="dict in educationOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="学位">
                <el-select :disabled="disabled"
                           v-model="form.academicDegree"
                           style="width: 100%" placeholder="请选择学位">
                  <el-option
                    v-for="dict in academicDegreeOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="籍贯" prop="nativePlace">
                <el-input :disabled="disabled"v-model="form.nativePlace" placeholder="请输入籍贯"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="家庭住址" prop="homeAddress">
                <el-input :disabled="disabled"v-model="form.homeAddress" placeholder="请输入家庭住址"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="固定电话" prop="housePhone">
                <el-input :disabled="disabled"v-model="form.housePhone" placeholder="请输入固定电话"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="电子邮箱" prop="email">
                <el-input :disabled="disabled" v-model="form.email" placeholder="请输入电子邮箱"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="QQ" prop="qq">
                <el-input :disabled="disabled"v-model="form.qq" placeholder="请输入QQ"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="微信号码" prop="wechat">
                <el-input :disabled="disabled" v-model="form.wechat" placeholder="请输入微信号码"/>
              </el-form-item>
            </el-col>
          </el-row>
        </el-card>

        <el-card shadow="always" style="margin-bottom: 30px;">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">党员信息</span>
          </div>
          <el-row>
            <el-col :span="8">
              <el-form-item label="党组织" prop="partyOrgId">
                <select-tree :value="form.partyOrgId"
                             :disabled="disabled"
                             :options="orgOptions"
                             vModel="partyOrgId"
                             @selected="setVModelValue"
                             placeholder="请选择党组织"
                             />
                <!--<treeselect :disabled="disabled"
                            v-model="form.partyOrgId"
                            :options="partyOrgOptions"
                            :max-height="130"
                            placeholder="请选择党组织" />-->
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="党员类型" prop="memberType">
                <el-select :disabled="disabled"
                           v-model="form.memberType" style="width: 100%" placeholder="请选择党员类型">
                  <el-option
                    v-for="dict in memberTypeOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="党员状态" prop="memberStatus">
                <el-select :disabled="disabled"
                           v-model="form.memberStatus"
                           style="width: 100%" placeholder="请选择党员状态">
                  <el-option
                    v-for="dict in memberStatusOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="加入党支部日期" prop="joinBranchData">
                <el-date-picker :disabled="disabled"
                                clearable size="small" style="width: 100%"
                                v-model="form.joinBranchData"
                                type="date"
                                value-format="yyyy-MM-dd"
                                :picker-options="afterTimeOption"
                                placeholder="选择加入党支部日期">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="加入党日期" prop="joinData">
                <el-date-picker :disabled="disabled"
                                clearable size="small" style="width: 100%"
                                v-model="form.joinData"
                                type="date"
                                value-format="yyyy-MM-dd"
                                :picker-options="afterTimeOption"
                                placeholder="选择加入党日期">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="转为正式党员日期" prop="formalData">
                <el-date-picker :disabled="disabled"
                                clearable size="small" style="width: 100%"
                                v-model="form.formalData"
                                type="date"
                                value-format="yyyy-MM-dd"
                                :picker-options="afterTimeOption"
                                placeholder="选择转为正式党员日期">
                </el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="流动党员">
                <el-select :disabled="disabled"
                           v-model="form.floatingType"
                           style="width: 100%" placeholder="请选择流动党员">
                  <el-option
                    v-for="dict in floatingTypeOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="党员分组">
                <el-select :disabled="disabled"
                           v-model="form.memberGroup"
                           style="width: 100%" placeholder="请选择党员分组">
                  <el-option
                    v-for="dict in memberGroupOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-card>

        <el-card shadow="always" style="margin-bottom: 30px;">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">生活困难情况</span>
          </div>
          <el-row>
            <el-col :span="8">
              <el-form-item label="生活困难">
                <el-select :disabled="disabled"
                           v-model="form.lifeDifficulty"
                           style="width: 100%"
                           placeholder="请选择生活困难">
                  <el-option
                    v-for="dict in lifeDifficultyOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="组织认定">
                <el-select :disabled="disabled"
                           v-model="form.cognizance"
                           style="width: 100%" placeholder="请选择是否组织认定">
                  <el-option
                    v-for="dict in cognizanceOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="经济状况">
                <el-select :disabled="disabled"
                           v-model="form.economicSituation"
                           style="width: 100%" placeholder="请选择经济状况">
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
          <el-row>
            <el-col :span="8">
              <el-form-item label="身体健康情况">
                <el-select :disabled="disabled" v-model="form.physicalHealth" style="width: 100%" placeholder="请选择身体健康情况">
                  <el-option
                    v-for="dict in physicalHealthOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="生活困难类型">
                <el-select :disabled="disabled" v-model="form.lifeDifficultyType" style="width: 100%" placeholder="请选择生活困难类型">
                  <el-option
                    v-for="dict in lifeDifficultyTypeOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="享受帮扶">
                <el-select :disabled="disabled" v-model="form.enjoyHelp" style="width: 100%" placeholder="请选择是否享受帮扶">
                  <el-option
                    v-for="dict in enjoyHelpOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="补助情况" prop="helpInfo">
                <el-input :disabled="disabled" v-model="form.helpInfo" placeholder="请输入补助情况"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="具体情况描述" prop="detail">
                <el-input :disabled="disabled" v-model="form.detail" type="textarea"
                          :autosize="{ minRows: 4, maxRows: 4}" placeholder="请输入具体情况描述"/>
              </el-form-item>
            </el-col>
          </el-row>
        </el-card>

      </el-form>
      <div slot="footer" class="dialog-footer" :style="{textAlign:'center'}">
        <el-button v-show="!disabled" type="primary" @click="chooseAuditUser">提交审批</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <choose-audit-user ref="chooseAuditUser"  @ok="submitForm" @del="handleDelete"/>
  </div>
</template>

<style lang="scss" scoped>
  .head-container {
    overflow-x: hidden;
  }

  .avatar-upload {
    margin-left: 40%;
  }

  .avatar {
    width: 120px;
    height: 120px;
    align-items: center;
    border: 1px solid #e6ebf5;
    img{
      width:100%;
      height:100%;
    }
    div {
      width:100%;
      height:100%;
      background-image: url("../../../assets/image/avatar.png");
      background-size: cover;
    }
  }

</style>

<script>
  import {
    listPartyMember,
    getPartyMember,
    delPartyMember,
    addPartyMember,
    updatePartyMember,
    exportPartyMember,
    uploadAvatar,
    checkPartyMemberUnique,
  } from "@/api/party/member";
  import { listPartyMemberChange } from "@/api/party/memberChange";
  import { postOptionSelect } from "@/api/system/post";
  import { partyOrgTreeselect,partyOrgTreeselectByEdit, getPartyOrg } from "@/api/party/org";
  import { getDept } from "@/api/system/dept";
  import { treeselect } from "@/api/system/dept";
  import Treeselect from "@riophae/vue-treeselect";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";
  import selectTree from '../../components/selectTree';
  import { getUserProfile } from "@/api/system/user";
  import ChooseAuditUser from "../../audit/chooseAuditUser";
  import { listLog, getLog, delLog, addLog, updateLog, exportLog } from "@/api/sys/log";

  export default {
    name: "PartyMember",
    components: {ChooseAuditUser, Treeselect,selectTree},
    data() {
      let checkIdentityCard = (rule, value, callback) => {
        if (!value) {
          callback();
        } else {
          const reg = /^\d{6}(18|19|20)?\d{2}(0[1-9]|1[0-2])(([0-2][1-9])|10|20|30|31)\d{3}(\d|X|x)$/;
          const card = reg.test(value);
          if (!card) {
            //判断座机为12位
            callback(new Error("身份证格式如:423024xxxx0216xxxx"));
          } else {
            let param={};
            param.memberId = this.form.memberId;
            param.identityCard  = value;
            checkPartyMemberUnique(param).then(response => {
              if (response.code === 200 && response.msg.indexOf("已存在")!=-1) {
                callback(new Error(response.msg));
              } else {
                callback();
              }
            });
          }
        }
      };
      let checkPhone = (rule, value, callback) => {
        if (!value) {
          callback();
        } else {
          const telReg = /^1(3[0-9]|4[5,7]|5[0,1,2,3,5,6,7,8,9]|6[2,5,6,7]|7[0,1,7,8]|8[0-9]|9[1,8,9])\d{8}$/;
          const isTelPhone = telReg.test(value);
          const reg = /^((0\d{2,3}-\d{7,8})|(1[3584]\d{9}))$/;
          const isPhone = reg.test(value);
          if (!isTelPhone && !isPhone) {
            callback(new Error("请输入正确手机号或座机"));
          } else {
            callback();
          }
        }
      };
      let checkMobile = (rule, value, callback) => {
        if (!value) {
          callback();
        } else {
          const telReg = /^1(3[0-9]|4[5,7]|5[0,1,2,3,5,6,7,8,9]|6[2,5,6,7]|7[0,1,7,8]|8[0-9]|9[1,8,9])\d{8}$/;
          const isTelPhone = telReg.test(value);
          if (!isTelPhone ) {
            callback(new Error("请输入正确手机号"));
          } else {
            let param={};
            param.memberId = this.form.memberId;
            param.mobile = value;
            checkPartyMemberUnique(param).then(response => {
              if (response.code === 200 && response.msg.indexOf("已存在")!=-1) {
                callback(new Error(response.msg));
              } else {
                callback();
              }
            });
          }
        }
      };
      let checkEmail = (rule, value, callback) => {
        if (!value) {
          callback();
        } else {
          let param={};
          param.memberId = this.form.memberId;
          param.email = value;
          checkPartyMemberUnique(param).then(response => {
            if (response.code === 200 && response.msg.indexOf("已存在")!=-1) {
              callback(new Error(response.msg));
            } else {
              callback();
            }
          });
        }
      };
      let checkMemberName = (rule, value, callback) => {
        if (!value) {
          return new Error("请输入党员姓名");
        } else {
          let param={};
          param.memberId = this.form.memberId;
          param.memberName = value;
          checkPartyMemberUnique(param).then(response => {
            if (response.code === 200 && response.msg.indexOf("已存在")!=-1) {
              callback(new Error(response.msg));
            } else {
              callback();
            }
          });
        }
      };
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
        // 党员信息表格数据
        partyMemberList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 用户性别字典
        sexOptions: [],
        // 职务字典
        administrativePositionOptions: [],
        // 岗位选项
        postOptions: [],
        // 民族字典
        nationOptions: [],
        // 政治面貌字典
        polityOptions: [],
        // 身份字典
        workIdentityOptions: [],
        // 学历字典
        educationOptions: [],
        // 学位字典
        academicDegreeOptions: [],
        // 党员类型字典
        memberTypeOptions: [],
        // 党员状态字典
        memberStatusOptions: [],
        // 流动党员字典
        floatingTypeOptions: [],
        // 党员分组字典
        memberGroupOptions: [],
        // 生活困难字典
        lifeDifficultyOptions: [],
        // 组织认定字典
        cognizanceOptions: [],
        // 经济状况字典
        economicSituationOptions: [],
        // 身体健康情况字典
        physicalHealthOptions: [],
        // 生活困难类型字典
        lifeDifficultyTypeOptions: [],
        // 党组织架构树选项
        orgOptions: [],
        partyOrgOptions: [],
        partyOrgOptionsByEdait: [],
        // 部门树选项
        deptOptions: [],
        // 享受帮扶字典
        enjoyHelpOptions: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          memberName: undefined,
          memberType: undefined,
        },
        defaultProps: {
          value: 'id',          // 唯一标识
          label: 'label',       // 标签显示
          children: 'children', // 子级
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          memberName: [
            {required: true, message: "党员姓名不能为空", trigger: "blur"},
            {validator: checkMemberName, trigger: 'blur'}
          ],
          sex: [
            {required: true, message: "性别不能为空", trigger: "blur"},
          ],
          mobile: [
            /*{required: true, message: "手机号不能为空", trigger: "blur"},*/
            { validator: checkMobile, trigger: "blur" }
          ],
          identityCard: [
            { required: true, message: "身份证号不能为空", trigger: "blur" },
            {validator: checkIdentityCard, trigger: 'blur'}
          ],
          birthday: [
            { required: true, message: "出生日期不能为空", trigger: "blur" },
          ],
          companyName: [
            { required: true, message: "所在单位不能为空", trigger: "blur" },
          ],
          deptId: [
            { required: true, message: "部门不能为空", trigger: "blur" },
          ],
          administrativePosition: [
            { required: true, message: "职务不能为空", trigger: "blur" },
          ],
          postId: [
            { required: true, message: "岗位不能为空", trigger: "blur" },
          ],
          workingDate: [
           { required: true, message: "参加工作日期不能为空", trigger: "blur" },
          ],
          nativePlace: [
           /* { required: true, message: "籍贯不能为空", trigger: "blur" },*/
          ],
          polity: [
            { required: true, message: "政治面貌不能为空", trigger: "blur" },
          ],
          education: [
            { required: true, message: "学历不能为空", trigger: "blur" },
          ],
          nation: [
            { required: true, message: "民族不能为空", trigger: "blur" },
          ],
          joinData: [
            { required: true, message: "入党日期不能为空", trigger: "blur" },
          ],
          formalData: [
            { required: true, message: "转为正式党员日期不能为空", trigger: "blur" },
          ],
          housePhone: [
            { validator: checkPhone, trigger: "blur" }
          ],
          email: [
            {
              type: "email",
              message: "请输入正确的邮箱地址",
              trigger: ["blur", "change"]
            },
            {validator: checkEmail, trigger: 'blur'}
          ],
          partyOrgId: [
            { required: true, message: "党组织不能为空", trigger: "blur" }
          ],
          memberType: [
            { required: true, message: "党员类型不能为空", trigger: "blur" }
          ],
          memberStatus: [
            { required: true, message: "党员状态不能为空", trigger: "blur" }
          ],

        },
        bodyStyle: {
          overflowY: 'auto',
          height: '',
          marginLeft: '2%',
          paddingRight: '2%',
        },
        // 党组织架构名称
        partyOrgName: undefined,
        partyOrg:{
          partyOrgId:undefined,
          partyOrgName:undefined,
        },
        avatarUrl:'',
        disabled:false,
        afterTimeOption: {
          disabledDate(date) {
            //disabledDate 文档上：设置禁用状态，参数为当前日期，要求返回 Boolean
            return date.getTime() > Date.now();
          }
        },
        user: {},
      };
    },
    mounted() {
      window.addEventListener('resize', this.getHeight);
    },
    watch: {
      // 根据名称筛选部门树
      partyOrgName(val) {
        this.$refs.tree.filter(val);
      },
      formalData(val){
        this.getDateYearSub(val,this.getNowFormatDate)
      }

    },
    created() {
      this.getUser();
      this.getPartyOrgTreeSelect();
      this.getPartyOrgTreeSelectByEdit();
      this.getDeptTreeselect();
      this.postOptionSelect();
      this.getDicts("sys_user_sex").then(response => {
        this.sexOptions = response.data;
      });
      this.getDicts("administrative_position_type").then(response => {
        this.administrativePositionOptions = response.data;
      });
      this.getDicts("nation_type").then(response => {
        this.nationOptions = response.data;
      });
      this.getDicts("polity_type").then(response => {
        this.polityOptions = response.data;
      });
      this.getDicts("work_identity_type").then(response => {
        this.workIdentityOptions = response.data;
      });
      this.getDicts("education_type").then(response => {
        this.educationOptions = response.data;
      });
      this.getDicts("academic_degree_type").then(response => {
        this.academicDegreeOptions = response.data;
      });
      this.getDicts("party_member_type").then(response => {
        this.memberTypeOptions = response.data;
      });
      this.getDicts("party_member_status").then(response => {
        this.memberStatusOptions = response.data;
      });
      this.getDicts("sys_yes_no").then(response => {
        this.floatingTypeOptions = response.data;
      });
      this.getDicts("party_member_group").then(response => {
        this.memberGroupOptions = response.data;
      });
      this.getDicts("sys_yes_no").then(response => {
        this.lifeDifficultyOptions = response.data;
      });
      this.getDicts("sys_yes_no").then(response => {
        this.cognizanceOptions = response.data;
      });
      this.getDicts("economic_situation_type").then(response => {
        this.economicSituationOptions = response.data;
      });
      this.getDicts("physical_health_type").then(response => {
        this.physicalHealthOptions = response.data;
      });
      this.getDicts("life_difficulty_type").then(response => {
        this.lifeDifficultyTypeOptions = response.data;
      });
      this.getDicts("sys_yes_no").then(response => {
        this.enjoyHelpOptions = response.data;
      });

    },
    methods: {
      //下拉树选择后设置值
      setVModelValue(vModel,val){
        if(val!=null){
          this.form[vModel] =  val;
        }else{
          this.form[vModel] =  undefined;
        }
      },
      /**附件上传*/
      uploadAtt(file) {
        const loading = this.$loading({
          lock: true,
          text: '上传中……',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });
        let formData = new FormData();
        formData.append("avatarfile", file.file);
        uploadAvatar(formData).then(response => {
          if (response.code === 200) {
            this.form.avatar = response.msg
            this.avatarUrl=process.env.VUE_APP_BASE_API + response.msg;
            loading.close();
            this.msgSuccess("上传成功！")
          } else {
            loading.close();
            this.msgError(response.msg);
          }
        }).catch(function (err) {
          loading.close();
        });
      },
      // 筛选节点
      filterNode(value, data) {
        if (!value) return true;
        return data.label.indexOf(value) !== -1;
      },
      // 节点单击事件
      handleNodeClick(data) {
        this.queryParams.partyOrgId = data.id;
        this.partyOrg.partyOrgId = data.id;
        this.partyOrg.partyOrgName = data.label;
        this.getList();
      },
      /** 对话框自适应高度 */
      getHeight() {
        this.bodyStyle.height = window.innerHeight - 281 + 'px';
      },
      /** 查询岗位下拉结构 */
      postOptionSelect(){
        postOptionSelect().then(response => {
          this.postOptions = response.data;
        });
      },
      /** 查询部门下拉树结构 */
      getDeptTreeselect() {
        treeselect().then(response => {
          this.deptOptions = response.data;
        });
      },
      /** 查询党组织下拉树结构 */
      getPartyOrgTreeSelect() {
        partyOrgTreeselect().then(response => {
          this.partyOrgOptions = this.treeInitData(response.data);
        });
      },
      /** 修改时查询党组织下拉树结构 */
      getPartyOrgTreeSelectByEdit() {
        partyOrgTreeselectByEdit().then(response => {
          this.partyOrgOptionsByEdait = this.treeInitData(response.data);
        });
      },
      /** 查询党员信息列表 */
      getList() {
        this.loading = true;
        listPartyMember(this.queryParams).then(response => {
          this.partyMemberList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 用户性别字典翻译
      sexFormat(row, column) {
        return this.selectDictLabel(this.sexOptions, row.sex);
      },
      // 职务字典翻译
      administrativePositionFormat(row, column) {
        return this.selectDictLabel(this.administrativePositionOptions, row.administrativePosition);
      },
      // 民族字典翻译
      nationFormat(row, column) {
        return this.selectDictLabel(this.nationOptions, row.nation);
      },
      // 政治面貌字典翻译
      polityFormat(row, column) {
        return this.selectDictLabel(this.polityOptions, row.polity);
      },
      // 身份字典翻译
      workIdentityFormat(row, column) {
        return this.selectDictLabel(this.workIdentityOptions, row.workIdentity);
      },
      // 学历字典翻译
      educationFormat(row, column) {
        return this.selectDictLabel(this.educationOptions, row.education);
      },
      // 学位字典翻译
      academicDegreeFormat(row, column) {
        return this.selectDictLabel(this.academicDegreeOptions, row.academicDegree);
      },
      // 部门id翻译
      deptIdFormat(row, column,value){
        if(row.sysDept!=null){
          return row.sysDept.deptName;
        }else{
          return "";
        }
      },
      // 党组织id翻译
      partyOrgIdFormat(row, column){
        return row.djPartyOrg.partyOrgName;
      },
      // 党员类型字典翻译
      memberTypeFormat(row, column) {
        return this.selectDictLabel(this.memberTypeOptions, row.memberType);
      },
      // 党员状态字典翻译
      memberStatusFormat(row, column) {
        return this.selectDictLabel(this.memberStatusOptions, row.memberStatus);
      },
      // 流动党员字典翻译
      floatingTypeFormat(row, column) {
        return this.selectDictLabel(this.floatingTypeOptions, row.floatingType);
      },
      // 党员分组字典翻译
      memberGroupFormat(row, column) {
        return this.selectDictLabel(this.memberGroupOptions, row.memberGroup);
      },
      // 生活困难字典翻译
      lifeDifficultyFormat(row, column) {
        return this.selectDictLabel(this.lifeDifficultyOptions, row.lifeDifficulty);
      },
      // 组织认定字典翻译
      cognizanceFormat(row, column) {
        return this.selectDictLabel(this.cognizanceOptions, row.cognizance);
      },
      // 经济状况字典翻译
      economicSituationFormat(row, column) {
        return this.selectDictLabel(this.economicSituationOptions, row.economicSituation);
      },
      // 身体健康情况字典翻译
      physicalHealthFormat(row, column) {
        return this.selectDictLabel(this.physicalHealthOptions, row.physicalHealth);
      },
      // 生活困难类型字典翻译
      lifeDifficultyTypeFormat(row, column) {
        return this.selectDictLabel(this.lifeDifficultyTypeOptions, row.lifeDifficultyType);
      },
      // 享受帮扶字典翻译
      enjoyHelpFormat(row, column) {
        return this.selectDictLabel(this.enjoyHelpOptions, row.enjoyHelp);
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
          partyOrgId: undefined,
          workNo: undefined,
          avatar: undefined,
          memberName: undefined,
          sex: undefined,
          mobile: undefined,
          identityCard: undefined,
          birthday: undefined,
          companyName: undefined,
          deptId: undefined,
          administrativePosition: undefined,
          title: undefined,
          postId: undefined,
          workingDate: undefined,
          nation: undefined,
          polity: undefined,
          workIdentity: undefined,
          education: undefined,
          academicDegree: undefined,
          nativePlace: undefined,
          homeAddress: undefined,
          housePhone: undefined,
          email: undefined,
          qq: undefined,
          wechat: undefined,
          remark: undefined,
          memberType: undefined,
          memberStatus: undefined,
          joinBranchData: undefined,
          joinData: undefined,
          formalData: undefined,
          floatingType: undefined,
          memberGroup: undefined,
          lifeDifficulty: undefined,
          cognizance: undefined,
          economicSituation: undefined,
          physicalHealth: undefined,
          lifeDifficultyType: undefined,
          enjoyHelp: undefined,
          helpInfo: undefined,
          detail: undefined,
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
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.disabled = false;
        this.orgOptions= this.partyOrgOptions;
        this.form.partyOrgId = Number(this.partyOrg.partyOrgId);
        this.open = true;
        this.title = "添加党员信息";
      },
      /** 查看按钮操作 */
      handleSee(row){
        this.reset();
        this.disabled = true;
        const memberId = row.memberId || this.ids
        this.orgOptions= this.partyOrgOptionsByEdait;
        getPartyMember(memberId).then(response => {
          this.form = response.data;
          this.avatarUrl=process.env.VUE_APP_BASE_API + this.form.avatar;
          this.open = true;
          this.title = "查看党员信息";
        });
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        this.disabled = false;
        const memberId = row.memberId || this.ids
        this.orgOptions= this.partyOrgOptionsByEdait;
        getPartyMember(memberId).then(response => {
          this.form = response.data;
          this.avatarUrl=process.env.VUE_APP_BASE_API + this.form.avatar;
          this.open = true;
          this.title = "修改党员信息";
        });
      },
      chooseAuditUser(){
        this.$refs["form"].validate(valid => {
          if (valid) {
            if(this.form.memberId){
              listPartyMemberChange({"partyMemberId":this.form.memberId, "auditState":"2"}).then(response => {
                if(response.rows&&response.rows.length >0){
                  this.msgSuccess("该党员变更正在审批中！")
                }else{
                  this.$refs.chooseAuditUser.init(6,"edit",this.form.memberId)
                }
              });
            }else{
              this.$refs.chooseAuditUser.init(6,"add",this.form.memberId)
            }
          }
        });



      },
      /** 提交按钮 */
      submitForm: function (form) {
        this.$refs["form"].validate(valid => {
          if (valid) {
            this.form.auditUserId = form.auditUserId;
            this.form.operReason = form.reason;
            if (this.form.memberId != undefined) {
              updatePartyMember(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("提交审批成功");
                  this.open = false;
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addPartyMember(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("提交审批成功");
                  this.open = false;
                } else {
                  this.msgError(response.msg);
                }
              });
            }
          }else{
            setTimeout(() => {
              var isError = document.getElementsByClassName("is-error");
              isError[0].querySelector('input').focus();
            }, 100);
            return false;
          }
        });
      },
      chooseAuditUser1(row){
        listPartyMemberChange({"partyMemberId":row.memberId, "auditState":"2"}).then(response => {
          if(response.rows&&response.rows.length >0){
            this.msgSuccess("该党员变更正在审批中！")
          }else{
            this.$refs.chooseAuditUser.init(6,'del',row.memberId)
          }
        });
      },
      /** 删除按钮操作 */
      handleDelete(form) {
        delPartyMember(form).then(response => {
          if (response.code === 200) {
            this.msgSuccess("提交审批成功");
            this.getList();
          } else {
            this.msgError(response.msg);
          }
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有党员信息数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return exportPartyMember(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function () {
        });
      },
      getUser() {
        getUserProfile().then(response => {
          this.user = response.data;
          if(this.user.djPartyMember){
            this.queryParams.partyOrgId = this.user.djPartyMember.partyOrgId;
          }
        }).then(()=>{
          this.getList();
        });
      },
      showHandleUpdate(row){
        let showFlag = false;
        let roles = this.user.roles;
        if(roles && roles.length!=0){
          for(let i=0;i<roles.length;i++){
            //管理员角色
            if(roles[i].roleId == 1){
              showFlag = true;
              break;
            }
          }
        }
        if(this.user.djPartyMember && this.user.djPartyMember.partyOrgId == row.partyOrgId){
          showFlag = true;
        }
        return showFlag ;
      }
    }
  };
</script>
