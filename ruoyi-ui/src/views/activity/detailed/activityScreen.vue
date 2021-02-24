<template>
  <div class="activity_screen">
    <el-row>
      <el-col :span="24">
        <div class="activity_title">
          <span>
               中铁三局广东公司智慧党建系统
          </span>
        </div>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="8">
        <div class="activity_qrcode" ref="qrCode">
        </div>
      </el-col>
      <el-col :span="16">
        <el-row>
          <el-col :span="24">
            <div class="activity_detail_title">
              {{activityDetailed.djActivityPlan.activityTheme}}
            </div>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <div class="activity_detail">
              <div class="activity_detail_label">
                活动党组织：
              </div>
              <div class="activity_detail_content">
                {{activityDetailed.djPartyOrg.partyOrgFullName}}
              </div>
            </div>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <div class="activity_detail">
              <div class="activity_detail_label">
                活动地点：
              </div>
              <div class="activity_detail_content">
                {{activityDetailed.venue}}
              </div>
            </div>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <div class="activity_detail">
              <div class="activity_detail_label">
                活动类型：
              </div>
              <div class="activity_detail_content">
                {{activityTypeFormat(activityDetailed.djActivityPlan.activityType)}}
              </div>
            </div>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <div class="activity_detail">
              <div class="activity_detail_label">
                活动计划召开时间：
              </div>
              <div class="activity_detail_content">
                {{parseTime(activityDetailed.activityPlanStartTime, '{y}-{m}-{d}  {h}:{i}:{s}')}}
              </div>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="activity_detail">
              <div class="activity_detail_label">
                活动计划结束时间：
              </div>
              <div class="activity_detail_content">
                {{parseTime(activityDetailed.activityPlanEndTime, '{y}-{m}-{d}  {h}:{i}:{s}')}}
              </div>
            </div>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <div class="activity_detail">
              <div class="activity_detail_label">
                负责人：
              </div>
              <div class="activity_detail_content">
                {{activityDetailed.djPartyMember.memberName}}
              </div>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="activity_detail">
              <div class="activity_detail_label">
                到会指导人员：
              </div>
              <div class="activity_detail_content">
                {{activityDetailed.mentors}}
              </div>
            </div>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <div class="activity_detail">
              <div class="activity_detail_label">
                主持人：
              </div>
              <div class="activity_detail_content">
                {{activityDetailed.presenter}}
              </div>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="activity_detail">
              <div class="activity_detail_label">
                主讲人及身份：
              </div>
              <div class="activity_detail_content">
                {{activityDetailed.speaker}}
              </div>
            </div>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <div class="activity_detail">
              <div class="activity_detail_label">
                活动内容：
              </div>
              <div class="activity_detail_content">
                {{activityDetailed.activityContent}}
              </div>
            </div>
          </el-col>
        </el-row>
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="18" style="padding-left: 50px;">
        <div v-for="(activityMember,index) in activityMemberList" :key="activityMember.memberId" >
          <div :class="(activityMember.status ==2 || activityMember.status ==3 || activityMember.status ==4)?'member-pic':'member-pic no_arrive'" >
            <div class="member-avatar">
              <el-image
                v-if="activityMember.djPartyMember.avatar"
                style="width: 100%;height: 100%"
                :src="activityMember.djPartyMember.avatar"
                fit="fill"></el-image>
            </div>
            <div class="member-position" v-if="activityMember.djPartyMember.partyPositionType != null">
              {{partyPositionTypeFormat(activityMember.djPartyMember.partyPositionType)}}
            </div>
            <div class="member-position" v-else>
              {{memberTypeFormat(activityMember.djPartyMember.memberType)}}
            </div>
            <div class="member-name">{{activityMember.djPartyMember.memberName}}</div>
          </div>
        </div>
      </el-col>

      <el-col :span="6">

        <el-row>
          <el-col :span="14" :offset="10">
            <div class="member_info member_label plan_label">
              应到人数
            </div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="14" :offset="10">
            <div class="member_info member_count plan_count">
              {{planCount}}
            </div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="14" :offset="10">
            <div class="member_info member_label arrive_label">
              已到人数
            </div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="14" :offset="10">
            <div class="member_info member_count arrive_count">
              {{arriveCount}}
            </div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="14" :offset="10">
            <div class="member_info member_label late_label">
              未到人数
            </div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="14" :offset="10">
            <div class="member_info member_count late_count">
              {{noArriveCount}}
            </div>
          </el-col>
        </el-row>


      </el-col>
    </el-row>

    <websocket ref="websocket" :groupId="activityDetailed.detailedUuid" @receive="receiveMessage"/>
  </div>
</template>

<script>
  import QRCode from 'qrcodejs2'
  import {getDetailed} from "@/api/activity/detailed";
  import {listForScreen} from "@/api/activity/member";
  import Websocket from "../../../components/WebSocket/websocket";
  export default {
    name: 'Index',
    components: {Websocket,},
    data() {
      return {
        detailedId:undefined,
        activityDetailed:{
          djActivityPlan:{},
          djPartyMember:{},
          djPartyOrg:{}
        },
        activityMemberList: [],
        // 党员活动类型字典
        activityTypeOptions: [],
        // 职务字典
        partyPositionTypeOptions: [],
        // 党员类型字典
        memberTypeOptions: [],
        planCount:undefined,
        arriveCount:undefined,
        noArriveCount:undefined,
      }
    },
    created() {
      this.getActivityDetailed();
      this.getDicts("activity_type").then(response => {
        this.activityTypeOptions = response.data;
      });
      this.getDicts("party_position_type").then(response => {
        this.partyPositionTypeOptions = response.data;
      });
      this.getDicts("party_member_type").then(response => {
        this.memberTypeOptions = response.data;
      });

    },
    methods: {
      getActivityDetailed(){
        this.reset()
        this.detailedId = this.$route.path.replace('/activityScreen/','');
        getDetailed(this.detailedId).then(response => {
          this.activityDetailed = response.data;
        }).then(()=>{
          this.getActivityMemberList();
          this.createQrCode();
          this.openWebsocket();
        })
      },
      getActivityMemberList(){
        listForScreen({"detailedUuid": this.activityDetailed.detailedUuid}).then(response => {
          this.activityMemberList = response.data.activityMemberList;
          for(let i in this.activityMemberList){
            this.activityMemberList[i].djPartyMember.avatar =
              this.activityMemberList[i].djPartyMember.avatar == null ? undefined : process.env.VUE_APP_BASE_API + this.activityMemberList[i].djPartyMember.avatar
          }
          this.planCount = response.data.planCount;
          this.arriveCount = response.data.arriveCount;
          this.noArriveCount = response.data.noArriveCount;
        });
      },
      // 党员活动类型字典翻译
      activityTypeFormat(activityType) {
        return this.selectDictLabel(this.activityTypeOptions, activityType);
      },
      partyPositionTypeFormat(partyPositionType) {
        return this.selectDictLabel(this.partyPositionTypeOptions, partyPositionType);
      },
      memberTypeFormat(memberType) {
        return this.selectDictLabel(this.memberTypeOptions, memberType);
      },
      createQrCode() {
        this.$nextTick(function () {
          new QRCode(this.$refs.qrCode, {
            text: this.getBasePath()+process.env.VUE_APP_BASE_API+"/activity/member/signIn/"+this.activityDetailed.detailedUuid,
            width: 400,
            height: 400,
            colorDark: "#000000", //二维码颜色
            colorLight: "#ffffff", //二维码背景色
            correctLevel: QRCode.CorrectLevel.L//容错率，L/M/H
          })
        })
      },
      reset(){
        this.detailedId = undefined;
        this.activityDetailed = {
          djActivityPlan:{},
          djPartyMember:{},
          djPartyOrg:{}
        }
        this.planCount=undefined;
        this.arriveCount=undefined;
        this.noArriveCount=undefined;
      },
      receiveMessage(data){
        this.getActivityMemberList();
      },
      closeWebsocket(){
        this.$refs.websocket.closeWebsocket();
      },
      openWebsocket(){
        this.$refs.websocket.init();
      },

    }
  }
</script>

<style>
  @import '../../../assets/styles/member-avatar.css';
  @import '../../../assets/styles/activityScreen/activityScreen.css';
</style>
