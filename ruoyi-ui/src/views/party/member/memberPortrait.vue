<template>
  <div>
    <el-dialog :title="title" :visible.sync="open" width="90%" append-to-body >
      <div :style="bodyStyle">
        <el-row :gutter="60">
          <el-col :span="8">

            <div class="portrait-base">
              <el-row>
                <el-col :span="24" class="base-title portrait-title">
                  基本信息
                </el-col>
              </el-row>

              <el-row>
                <el-col :span="8" class="base-label portrait-label">
                  姓名:
                </el-col>
                <el-col :span="16" class="base-content portrait-content">
                  {{member.memberName}}
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="8" class="base-label portrait-label">
                  手机号:
                </el-col>
                <el-col :span="16" class="base-content portrait-content">
                  {{member.mobile}}
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="8" class="base-label portrait-label">
                  出生日期:
                </el-col>
                <el-col :span="16" class="base-content portrait-content">
                  {{ parseTime(member.birthday, '{y}-{m}-{d}') }}
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="8" class="base-label portrait-label">
                  民族:
                </el-col>
                <el-col :span="16" class="base-content portrait-content">
                  {{ nationFormat(member.nation)}}
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="8" class="base-label portrait-label">
                  学历:
                </el-col>
                <el-col :span="16" class="base-content portrait-content">
                  {{ educationFormat(member.education)}}
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="8" class="base-label portrait-label">
                  学位:
                </el-col>
                <el-col :span="16" class="base-content portrait-content">
                  {{ academicDegreeFormat(member.academicDegree)}}
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="8" class="base-label portrait-label">
                  所在单位:
                </el-col>
                <el-col :span="16" class="base-content portrait-content">
                  {{ member.companyName}}
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="8" class="base-label portrait-label">
                  行政组织:
                </el-col>
                <el-col :span="16" class="base-content portrait-content">
                  {{ deptIdFormat(member)}}
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="8" class="base-label portrait-label">
                  行政职务:
                </el-col>
                <el-col :span="16" class="base-content portrait-content">
                  {{ administrativePositionFormat(member.administrativePosition)}}
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="8" class="base-label portrait-label">
                  参加工作日期:
                </el-col>
                <el-col :span="16" class="base-content portrait-content">
                  {{ parseTime(member.workingDate, '{y}-{m}-{d}') }}
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="8" class="base-label portrait-label">
                  电子邮件:
                </el-col>
                <el-col :span="16" class="base-content portrait-content">
                  {{ member.email}}
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="8" class="base-label portrait-label">
                  家庭住址:
                </el-col>
                <el-col :span="16" class="base-content portrait-content">
                  {{ member.homeAddress}}
                </el-col>
              </el-row>




            </div>

            <div class="portrait-promise">
              <el-row>
                <el-col :span="24" class="promise-title portrait-title">
                  入党宣誓词
                </el-col>
              </el-row>

              <el-row>
                <el-col :span="24" class="promise-content">
                  {{portraitContent}}
                </el-col>
              </el-row>
            </div>

          </el-col>
          <el-col :span="8">
            <div class="portrait-center">
              <div class="portrait-rank">
                <div class="portrait-rank-no">
                  <span class="rank-no" >NO.</span>
                  <span class="rank-num">67</span>
                </div>
                <div class="portrait-rank-score">
                  <span class="rank-score" >1分</span>
                </div>
              </div>
              <div :class="member.sex =='0' ?'portrait-avatar-man' :'portrait-avatar-woman'">
              </div>
            </div>
          </el-col>
          <el-col :span="8">

            <div class="portrait-member">
              <el-row>
                <el-col :span="24" class="member-title portrait-title">
                  党员信息
                </el-col>
              </el-row>

              <el-row>
                <el-col :span="8" class="member-label portrait-label">
                  所属党组织:
                </el-col>
                <el-tooltip class="item" effect="dark"
                            placement="bottom"
                            :content=" member.djPartyOrg.partyOrgFullName">
                  <el-col :span="16" class="member-content portrait-content">
                    {{ member.djPartyOrg.partyOrgFullName}}
                  </el-col>
                </el-tooltip>
              </el-row>
              <el-row>
                <el-col :span="8" class="member-label portrait-label">
                  党员类型:
                </el-col>
                <el-col :span="16" class="member-content portrait-content">
                  {{ memberTypeFormat(member.memberType)}}
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="8" class="member-label portrait-label">
                  状态:
                </el-col>
                <el-col :span="16" class="member-content portrait-content">
                  {{ memberStatusFormat(member.memberStatus)}}
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="8" class="member-label portrait-label">
                  加入党支部日期:
                </el-col>
                <el-col :span="16" class="member-content portrait-content">
                  {{ parseTime(member.joinBranchData, '{y}-{m}-{d}') }}
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="8" class="member-label portrait-label">
                  入党日期:
                </el-col>
                <el-col :span="16" class="member-content portrait-content">
                  {{ parseTime(member.joinData, '{y}-{m}-{d}') }}
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="8" class="member-label portrait-label">
                  转为正式党员日期:
                </el-col>
                <el-col :span="16" class="member-content portrait-content">
                  {{ parseTime(member.formalData, '{y}-{m}-{d}') }}
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="8" class="member-label portrait-label">
                  党内职务:
                </el-col>
                <el-col :span="16" class="member-content portrait-content">
                  {{ partyPositionTypeFormat(member.partyPositionType)}}
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="8" class="member-label portrait-label">
                  党龄:
                </el-col>
                <el-col :span="16" class="member-content portrait-content">
                  {{ getPartyAge(member.formalData) }}
                </el-col>
              </el-row>


            </div>

            <div class="portrait-radar">
              <el-row>
                <el-col :span="24" class="radar-title portrait-title">
                  党员统计
                </el-col>
              </el-row>

              <el-row>
                <el-col :span="24" class="promise-content">
                  <div ref="radarChart" :style="{ height: '240px'}"></div>
                </el-col>
              </el-row>
            </div>

          </el-col>
        </el-row>
      </div>
      <div slot="footer" class="dialog-footer" :style="{textAlign:'center'}">
        <el-button @click="cancel">取 消</el-button>
      </div>

    </el-dialog>
  </div>
</template>

<style >
  .portrait-title{
    color: white;
    padding: 0px 20px;
    font-size: 16px;
    font-weight: 600;
    line-height: 40px;
  }
  .portrait-label{
    color: black;
    padding: 0px 10px;
    font-size: 14px;
    font-weight: 600;
    height: 30px;
    line-height: 30px;
    text-align: right;
  }
  .portrait-content{
    color: black;
    padding: 0px 10px;
    font-size: 14px;
    height: 30px;
    line-height: 30px;
    overflow: hidden;
    text-overflow:ellipsis;
    white-space: nowrap;
  }

  .portrait-base{
    margin-bottom: 20px;
  }
  .base-title{
    background-color: #D9B300;
  }
  .base-label{
    background-color: #FFF4C1;
    border-left:1px solid #FFD306;
    border-bottom:1px solid #FFD306;
  }
  .base-content{
    background-color: #FFF8D7;
    border-left:1px solid #FFD306;
    border-bottom:1px solid #FFD306;
    border-right:1px solid #FFD306;
  }


  .portrait-center{
    text-align: -webkit-center; /*Chrome*/
  }

  .portrait-rank{
    width: 150px;
    height: 150px;
    background: url("../../../assets/image/rank.png") no-repeat center;
    background-size: 200px 200px;
    float: left;
  }
  .portrait-rank-no{
    position: relative;
    top:40px;
    text-align: center;
  }
  .rank-no{
    color: red;
    font-weight: 600;
    font-size: 16px;
  }
  .rank-num{
    color: red;
    font-weight: bold;
    font-size: 35px;
  }
  .portrait-rank-score{
    position: relative;
    top:90px;
    text-align: center;
  }
  .rank-score{
    color: red;
    font-weight: 600;
    font-size: 16px;
  }

  .portrait-avatar-man{
    width: 350px;
    height: 580px;
    position: relative;
    top:80px;
    background: url("../../../assets/image/man.png") no-repeat ;
    background-size: 100%  ;

  }

  .portrait-avatar-woman{
    width: 250px;
    height: 580px;
    position: relative;
    top:80px;
    background: url("../../../assets/image/woman.jpg") no-repeat ;
    background-size: 100% ;
  }

  .portrait-promise{
    min-height: 230px;
    background-color: #DFFFDF;
    border:1px solid #00BB00;
  }
  .promise-title{
    background-color: #00BB00;
  }
  .promise-content{
    padding: 20px;
    text-indent:2em;
    font-size:14px;
    line-height: 25px;
    color: black;
  }


  .portrait-member{
    margin-bottom: 20px;
  }
  .member-title{
    background-color: #0072E3;
  }
  .member-label{
    background-color: #97CBFF;
    border-left:1px solid #66B3FF;
    border-bottom:1px solid #66B3FF;
  }
  .member-content{
    background-color: #D2E9FF;
    border-left:1px solid #66B3FF;
    border-bottom:1px solid #66B3FF;
    border-right:1px solid #66B3FF;
  }



  .portrait-radar{
    min-height: 355px;
    background-color: #FFECEC;
    border:1px solid #FF2D2D;
  }
  .radar-title{
    background-color: #CE0000;
  }

</style>

<script>
  import echarts from 'echarts'
  require('echarts/theme/macarons')
  import { getPartyMember } from "@/api/party/member";


  export default {
    name: 'MemberPortrait',
    components: {

    },
    data() {
      return {
        open: false,
        title:undefined,
        member:{
          djPartyOrg:{},
          sysDept:{},
        },
        bodyStyle: {
          overflowY: 'auto',
          height: '',
          marginLeft: '5%',
          paddingRight: '5%',
        },
        // 民族字典
        nationOptions: [],
        // 学历字典
        educationOptions: [],
        // 学位字典
        academicDegreeOptions: [],
        // 职务字典
        administrativePositionOptions: [],
        // 党员类型字典
        memberTypeOptions: [],
        // 党员状态字典
        memberStatusOptions: [],
        // 党内职务字典
        partyPositionTypeOptions: [],
        portraitContent: undefined,
      }
    },
    created(){
      window.addEventListener('resize', this.getHeight);
      this.getHeight()
      this.getDicts("nation_type").then(response => {
        this.nationOptions = response.data;
      });
      this.getDicts("education_type").then(response => {
        this.educationOptions = response.data;
      });
      this.getDicts("academic_degree_type").then(response => {
        this.academicDegreeOptions = response.data;
      });
      this.getDicts("administrative_position_type").then(response => {
        this.administrativePositionOptions = response.data;
      });
      this.getDicts("party_member_type").then(response => {
        this.memberTypeOptions = response.data;
      });
      this.getDicts("party_member_status").then(response => {
        this.memberStatusOptions = response.data;
      });
      this.getDicts("party_position_type").then(response => {
        this.partyPositionTypeOptions = response.data;
      });
      this.getConfigKey("portrait.content").then(response => {
        this.portraitContent= response.msg;
      });
    },
    mounted() {

    },
    methods: {
      init(memberId){
        getPartyMember(memberId).then(response => {
          this.member = response.data;
          this.open = true;
          this.title =  "党员画像";
        }).then(()=>{
          this.$nextTick(() => {
            this.initChart()
          })
        });
      },
      initChart() {
        let indicator = [
          { name: '组织生活', max: 85}, //max 活动次数
          { name: '学习教育', max: 100}, //max
          { name: '先锋模范', max: 10}, //max 10
          { name: '党员特长', max: 10}, // max 10
          { name: '民主评议', max: 1}, // 点评占比
        ];
        let data =  [
          {
            value: [56, 56, 4, 8, 0.56],
          },
        ];

        this.chart =  echarts.init(this.$refs.radarChart, 'macarons')
        this.chart.setOption({
          tooltip: {},
          legend: {
          },
          radar: {
            // shape: 'circle',
            name: {
              textStyle: {
                color: '#000000',
              }
            },
            indicator: indicator
          },
          series: [{
            name: '党员统计',
            type: 'radar',
            // areaStyle: {normal: {}},
            data: data,
            animationDuration: 3000,
          }]
        })
      },
      // 民族字典翻译
      nationFormat(val) {
        return this.selectDictLabel(this.nationOptions, val);
      },
      // 学历字典翻译
      educationFormat(val) {
        return this.selectDictLabel(this.educationOptions, val);
      },
      // 学位字典翻译
      academicDegreeFormat(val) {
        return this.selectDictLabel(this.academicDegreeOptions, val);
      },
      // 职务字典翻译
      administrativePositionFormat(val) {
        return this.selectDictLabel(this.administrativePositionOptions, val);
      },
      /** 对话框自适应高度 */
      getHeight() {
        this.bodyStyle.height = window.innerHeight - 281 + 'px';
      },
      // 党员类型字典翻译
      memberTypeFormat(val) {
        return this.selectDictLabel(this.memberTypeOptions, val);
      },
      // 党员状态字典翻译
      memberStatusFormat(val) {
        return this.selectDictLabel(this.memberStatusOptions, val);
      },
      // 党内职务字典翻译
      partyPositionTypeFormat(val) {
        return this.selectDictLabel(this.partyPositionTypeOptions,val);
      },
      deptIdFormat(member){
        if(member.sysDept!=null){
          return member.sysDept.deptFullName;
        }else{
          return "";
        }
      },
      getPartyAge(formalData){
        if(formalData){
          let sDate = new Date(Date.parse(formalData.replace(/-/g, "/")));
          let eDate = new Date();
          let sY  = sDate.getFullYear();
          let eY  = eDate.getFullYear();
          return eY-sY;
        }else{
          return "";
        }
      },
      // 取消按钮
      cancel() {
        this.open = false;
      }
    }
  }
</script>

