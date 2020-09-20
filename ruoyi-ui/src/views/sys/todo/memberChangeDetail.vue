<template>
  <div style="padding: 30px">
    <el-form ref="form" :model="form" :rules="rules" label-width="150px">
      <el-card shadow="always" style="margin-bottom: 30px;">
        <div slot="header" style="height: 25px">
          <span style="font-weight: bold;font-size: 16px">基本信息</span>
        </div>
        <el-row>
          <el-col :span="16">
            <el-row>
              <el-col :span="12">
                <el-form-item label="党员姓名" prop="memberName">
                  <el-input :disabled="disabled" v-model="form.memberName" placeholder="请输入党员姓名"/>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="手机号" prop="mobile">
                  <el-input :disabled="disabled" v-model="form.mobile" placeholder="请输入手机号"/>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="用户性别">
                  <el-select :disabled="disabled"
                             v-model="form.sex"
                             style="width: 100%" placeholder="请选择用户性别">
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
                  <el-input :disabled="disabled" v-model="form.workNo" placeholder="请输入工号"/>
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
                <img v-if="form.avatar" :src="avatarUrl">
                <div v-else class="avatar-background"></div>
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
            <el-form-item label="行政组织" prop="deptId">
              <select-tree :value="form.deptId"
                           :disabled="disabled"
                           :options="deptOptions"
                           vModel="deptId"
                           @selected="setVModelValue"
                           placeholder="请选择行政组织"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="行政职务">
              <el-select :disabled="disabled"
                         v-model="form.administrativePosition"
                         style="width: 100%" placeholder="请选择行政职务">
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
            <el-form-item label="岗位">
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
            <el-form-item label="民族">
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
            <el-form-item label="政治面貌">
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
            <el-form-item label="学历">
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
              <el-input :disabled="disabled" v-model="form.nativePlace" placeholder="请输入籍贯"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="家庭住址" prop="homeAddress">
              <el-input :disabled="disabled" v-model="form.homeAddress" placeholder="请输入家庭住址"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="固定电话" prop="housePhone">
              <el-input :disabled="disabled" v-model="form.housePhone" placeholder="请输入固定电话"/>
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
              <el-input :disabled="disabled" v-model="form.qq" placeholder="请输入QQ"/>
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
                           :options="partyOrgOptions"
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
              <el-select :disabled="disabled" v-model="form.lifeDifficultyType" style="width: 100%"
                         placeholder="请选择生活困难类型">
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
      <el-card shadow="always" style="margin-bottom: 30px;">
        <div slot="header" style="height: 25px">
          <span style="font-weight: bold;font-size: 16px">审批记录</span>
        </div>
        <el-table v-loading="loading" :data="logList">
          <el-table-column label="操作名称" align="center" prop="stepName"/>
          <el-table-column label="操作人" align="center" prop="sysUser.nickName"/>
          <el-table-column label="操作" align="center" prop="operResult"/>
          <el-table-column label="操作时间" align="center" prop="operTime" width="180">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.operTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="备注" align="center" prop="operReason"/>
        </el-table>

      </el-card>
    </el-form>

    <div :style="{textAlign:'center'}">
      <el-button v-if="todoStatus=='0'" type="primary" @click="auditResultOpen">审 核</el-button>
      <el-button @click="cancel">关 闭</el-button>
    </div>

    <audit-result ref="auditResult" @ok="submitAudit"/>
  </div>
</template>

<style lang="scss" scoped>


  .avatar-upload {
    margin-left: 40%;
  }

  .avatar {
    width: 120px;
    height: 120px;
    align-items: center;
    border: 1px solid #e6ebf5;

    img {
      width: 100%;
      height: 100%;
    }

    div {
      width: 100%;
      height: 100%;
      background-image: url("../../../assets/image/avatar.png");
      background-size: cover;
    }
  }

</style>

<script>
  import {
    listPartyMemberChange,
    getPartyMemberChange,
    getPartyMemberChangeByMemberUuid,
    delPartyMemberChange,
    addPartyMemberChange,
    updatePartyMemberChange,
    exportPartyMemberChange
  } from "@/api/memberChange/partyMemberChange";
  import {postOptionSelect} from "@/api/system/post";
  import {partyOrgTreeselect, getPartyOrg} from "@/api/party/org";
  import {getDept} from "@/api/system/dept";
  import {treeselect} from "@/api/system/dept";
  import Treeselect from "@riophae/vue-treeselect";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";
  import selectTree from '../../components/selectTree';
  import {listLog} from "@/api/sys/log";
  import {listTodo, getTodo, delTodo, addTodo, updateTodo, exportTodo} from "@/api/sys/todo";
  import auditResult from "../../audit/auditResult";


  export default {
    name: "PartyMemberChange",
    components: {Treeselect, selectTree,auditResult},
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
        // 党员变更表表格数据
        partyMemberChangeList: [],
        // 变更类型字典
        changeTypeOptions: [],
        // 审批状态字典
        auditStateOptions: [],
        // 用户性别字典
        sexOptions: [],
        // 行政职务字典
        administrativePositionOptions: [],
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
        // 享受帮扶字典
        enjoyHelpOptions: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          memberUuid: undefined,
          changeType: undefined,
          partyMemberId: undefined,
          auditState: undefined,
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
        },
        // 表单参数
        form: {},
        // 表单参数
        partyMember: {},
        // 表单校验
        rules: {
          memberUuid: [
            {required: true, message: "用户唯一uuid不能为空", trigger: "blur"}
          ],
          changeType: [
            {required: true, message: "党员变更类型（add：新增 ；edit：修改；del：删除）不能为空", trigger: "blur"}
          ],
          partyMemberId: [
            {required: true, message: "关联党组织成员id不能为空", trigger: "blur"}
          ],
          auditState: [
            {required: true, message: "审批状态不能为空", trigger: "blur"}
          ],
          partyOrgId: [
            {required: true, message: "党组织ID不能为空", trigger: "blur"}
          ],
          workNo: [
            {required: true, message: "工号不能为空", trigger: "blur"}
          ],
          avatar: [
            {required: true, message: "党员照片不能为空", trigger: "blur"}
          ],
          memberName: [
            {required: true, message: "党员姓名不能为空", trigger: "blur"}
          ],
          sex: [
            {required: true, message: "用户性别（0男 1女 2未知）不能为空", trigger: "blur"}
          ],
          mobile: [
            {required: true, message: "手机号不能为空", trigger: "blur"}
          ],
          identityCard: [
            {required: true, message: "身份证号不能为空", trigger: "blur"}
          ],
          birthday: [
            {required: true, message: "出生日期不能为空", trigger: "blur"}
          ],
          companyName: [
            {required: true, message: "所在单位不能为空", trigger: "blur"}
          ],
          deptId: [
            {required: true, message: "行政组织不能为空", trigger: "blur"}
          ],
          administrativePosition: [
            {required: true, message: "行政职务不能为空", trigger: "blur"}
          ],
          title: [
            {required: true, message: "职称不能为空", trigger: "blur"}
          ],
          postId: [
            {required: true, message: "岗位不能为空", trigger: "blur"}
          ],
          workingDate: [
            {required: true, message: "参加工作日期不能为空", trigger: "blur"}
          ],
          nation: [
            {required: true, message: "民族不能为空", trigger: "blur"}
          ],
          polity: [
            {required: true, message: "政治面貌不能为空", trigger: "blur"}
          ],
          workIdentity: [
            {required: true, message: "身份不能为空", trigger: "blur"}
          ],
          education: [
            {required: true, message: "学历不能为空", trigger: "blur"}
          ],
          academicDegree: [
            {required: true, message: "学位不能为空", trigger: "blur"}
          ],
          nativePlace: [
            {required: true, message: "籍贯不能为空", trigger: "blur"}
          ],
          homeAddress: [
            {required: true, message: "家庭住址不能为空", trigger: "blur"}
          ],
          housePhone: [
            {required: true, message: "固定电话不能为空", trigger: "blur"}
          ],
          email: [
            {required: true, message: "电子邮箱不能为空", trigger: "blur"}
          ],
          qq: [
            {required: true, message: "qq不能为空", trigger: "blur"}
          ],
          wechat: [
            {required: true, message: "微信号码不能为空", trigger: "blur"}
          ],
          remark: [
            {required: true, message: "备注不能为空", trigger: "blur"}
          ],
          memberType: [
            {required: true, message: "党员类型不能为空", trigger: "blur"}
          ],
          memberStatus: [
            {required: true, message: "党员状态不能为空", trigger: "blur"}
          ],
          joinBranchData: [
            {required: true, message: "加入党支部日期不能为空", trigger: "blur"}
          ],
          joinData: [
            {required: true, message: "加入党日期不能为空", trigger: "blur"}
          ],
          formalData: [
            {required: true, message: "转为正式党员日期不能为空", trigger: "blur"}
          ],
          floatingType: [
            {required: true, message: "流动党员（1：是  0：否）不能为空", trigger: "blur"}
          ],
          memberGroup: [
            {required: true, message: "党员分组不能为空", trigger: "blur"}
          ],
          lifeDifficulty: [
            {required: true, message: "生活困难（1：是  0：否）不能为空", trigger: "blur"}
          ],
          cognizance: [
            {required: true, message: "组织认定（1：是  0：否）不能为空", trigger: "blur"}
          ],
          economicSituation: [
            {required: true, message: "经济状况不能为空", trigger: "blur"}
          ],
          physicalHealth: [
            {required: true, message: "身体健康情况不能为空", trigger: "blur"}
          ],
          lifeDifficultyType: [
            {required: true, message: "生活困难类型不能为空", trigger: "blur"}
          ],
          enjoyHelp: [
            {required: true, message: "享受帮扶不能为空", trigger: "blur"}
          ],
          helpInfo: [
            {required: true, message: "补助情况不能为空", trigger: "blur"}
          ],
          detail: [
            {required: true, message: "具体情况描述不能为空", trigger: "blur"}
          ],
        },
        bodyStyle: {
          overflowY: 'auto',
          height: '',
          marginLeft: '2%',
          paddingRight: '2%',
        },
        avatarUrl: '',
        disabled: false,
        afterTimeOption: {
          disabledDate(date) {
            //disabledDate 文档上：设置禁用状态，参数为当前日期，要求返回 Boolean
            return date.getTime() > Date.now();
          }
        },
        // 党组织架构树选项
        partyOrgOptions: [],
        // 岗位选项
        postOptions: [],
        // 部门树选项
        deptOptions: [],
        logList: [],
        memberUuid: undefined,
        sysLogId: undefined,
        todoId: undefined,
        todoStatus: undefined,
      };
    },
    mounted() {
      window.addEventListener('resize', this.getHeight);
    },
    created() {
      this.init();
      this.getPartyOrgTreeSelect();
      this.postOptionSelect();
      this.getDeptTreeselect();
      this.getDicts("change_type").then(response => {
        this.changeTypeOptions = response.data;
      });
      this.getDicts("audit_state").then(response => {
        this.auditStateOptions = response.data;
      });
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
      init() {
        this.todoId = this.$route.query.todoId;
        getTodo(this.todoId).then(response => {
          if (response.data == undefined) {
            this.$store.dispatch('tagsView/delView', this.$route)
            this.$router.go(-1)
          }
          this.todoStatus = response.data.status
          let params = eval('(' + response.data.urlParams + ')');
          this.memberUuid = params.memberUuid;
          this.sysLogId = params.sysLogId;

        }).then(() => {
          this.reset();
          this.disabled = true;
          getPartyMemberChangeByMemberUuid(this.memberUuid).then(response => {
            this.form = response.data;
            this.avatarUrl = process.env.VUE_APP_BASE_API + this.form.avatar;
            this.getLogList();
          }).then(()=>{
            if(this.form.changeType == "edit"){

            }
          });
        });
      },
      getLogList() {
        this.loading = true;
        listLog({"uuid": this.form.memberUuid}).then(response => {
          this.logList = response.rows;
          this.loading = false;
        });
      },
      /** 查询行政组织下拉树结构 */
      getDeptTreeselect() {
        treeselect().then(response => {
          this.deptOptions = response.data;
        });
      },
      /** 查询岗位下拉结构 */
      postOptionSelect() {
        postOptionSelect().then(response => {
          this.postOptions = response.data;
        });
      },
      /** 查询党组织下拉树结构 */
      getPartyOrgTreeSelect() {
        partyOrgTreeselect().then(response => {
          this.partyOrgOptions = this.treeInitData(response.data);
        });
      },
      //下拉树选择后设置值
      setVModelValue(vModel, val) {
        if (val != null) {
          this.form[vModel] = val;
        } else {
          this.form[vModel] = undefined;
        }
      },
      // 表单重置
      reset() {
        this.form = {
          memberId: undefined,
          memberUuid: undefined,
          changeType: undefined,
          partyMemberId: undefined,
          auditState: undefined,
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
          memberStatus: "0",
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
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.memberId != undefined) {
              updatePartyMemberChange(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addPartyMemberChange(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("新增成功");
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            }
          }
        });
      },
      /**附件上传*/
      uploadAtt(file) {

      },
      cancel() {
        this.$store.dispatch('tagsView/delView', this.$route)
        this.$router.go(-1)
      },
      auditResultOpen(){
        this.$refs.auditResult.init() ;
      },
      submitAudit(form){
        this.form.state = form.state;
        this.form.reason =  form.state;
        this.form.sysLogId = this.sysLogId ;
        updatePartyMemberChange(this.form).then(response => {
          if (response.code === 200) {
            this.msgSuccess("审批成功");
          } else {
            this.msgError(response.msg);
          }
        }).then(()=>{
          updateTodo({"todoId": this.todoId, "status": "1"}).then(response => {
            if (response.code === 200) {
              this.$store.dispatch('tagsView/delView', this.$route)
              this.$router.go(-1)
            } else {
              this.msgError(response.msg);
            }
          });
        });
      }
    }
  };
</script>
