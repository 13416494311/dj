<template>
  <div class="app-container org-info-bg">
    <!--<el-row>
      <el-col :span="24" class="level-center">
        <el-image :src="djPng"></el-image>
      </el-col>
    </el-row>-->
    <el-row style="margin-top: 4.5em">
      <el-col :span="8" class="org-info-lable">
        党组织名称：
      </el-col>
      <el-col :span="16" class="org-info-content">
        {{partyOrg.partyOrgFullName}}
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="8"  class="org-info-lable">
        成立时间：
      </el-col>
      <el-col :span="16" class="org-info-content">
        {{ parseTime(partyOrg.buildTime, '{y}-{m}-{d}') }}
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="8"  class="org-info-lable">
        换届时间：
      </el-col>
      <el-col :span="16" class="org-info-content">
        {{ partyOrg.params.changeTime==null?'':parseTime(partyOrg.params.changeTime, '{y}-{m}-{d}') }}
      </el-col>
    </el-row>
    <el-row >
      <el-col :span="8"  class="org-info-lable">
        党员简介：
      </el-col>
      <el-col :span="16" class="org-info-content">
        正式党员{{partyOrg.params.formalCount}}人，预备党员{{partyOrg.params.prepareCount}}人。
      </el-col>
    </el-row>
    <el-row style="margin-top: 20px">
      <el-col :span="24" class="level-center">

        <div class="org-info" >
          <div class="org-info-pic" v-for="(member,index) in memberList1" :key="index">
            <div class="org-info-avatar">
              <el-image
                v-if="member.avatar"
                style="width: 100%;height: 100%"
                :src="member.avatar"
                fit="fill"></el-image>
            </div>
            <div  class="org-info-position" >
              {{member.partyPositionTypeText}}
            </div>
            <div  class="org-info-name">{{member.memberName}}</div>
          </div>
        </div>

        <div  class="y-line-div" v-if="memberList2.length>0 || memberList3.length>0 ">
          <div v-for="(member,index) in memberList1" :key="index" class="y-line"></div>
        </div>

        <div  class="x-line-div" v-if="memberList2.length>1 ">
          <div v-for="(member,index) in memberList2" :key="index" :class="(index == 0 || index == memberList2.length-1) ?'x-line':'x-line-center'"  ></div>
        </div>

        <div  class="y-line-div" v-if="memberList2.length>0 ">
          <div v-for="(member,index) in memberList2" :key="index" class="y-line"></div>
        </div>

        <div class="org-info" >
          <div class="org-info-pic" v-for="(member,index) in memberList2" :key="index">
            <div class="org-info-avatar">
              <el-image
                v-if="member.avatar"
                style="width: 100%;height: 100%"
                :src="member.avatar"
                fit="fill"></el-image>
            </div>
            <div  class="org-info-position" >
              {{member.partyPositionTypeText}}
            </div>
            <div  class="org-info-name">{{member.memberName}}</div>
          </div>
        </div>

        <div  class="y-line-div" v-if="memberList2.length>0 && memberList3.length>0">
          <div v-for="(member,index) in memberList2" :key="index" class="y-line"></div>
        </div>

        <div  class="x-line-div" v-if="memberList3.length>1">
          <div v-for="(member,index) in memberList3" :key="index" :class="(index == 0 || index == memberList3.length-1) ?'x-line':'x-line-center'" ></div>
        </div>

        <div  class="y-line-div"v-if="memberList3.length>0">
          <div v-for="(member,index) in memberList3" :key="index"  class="y-line"></div>
        </div>

        <div class="org-info" >
          <div class="org-info-pic" v-for="(member,index) in memberList3" :key="index">
            <div class="org-info-avatar">
              <el-image
                v-if="member.avatar"
                style="width: 100%;height: 100%"
                :src="member.avatar"
                fit="fill"></el-image>
            </div>
            <div  class="org-info-position" >
              {{member.partyPositionTypeText}}
            </div>
            <div  class="org-info-name">{{member.memberName}}</div>
          </div>
        </div>


      </el-col>
    </el-row>


  </div>
</template>

<script>
  import djPng from '@/assets/image/dj.png'
  import defaultAvatar from '@/assets/image/dj-avatar.png'
  import TreeChart from "vue-tree-chart";
  import { getPartyOrgInfo } from "@/api/party/org";

  export default {
    name: "OrgInfo",
    components: {
      TreeChart
    },
    data() {
      return {
        djPng,
        defaultAvatar,
        partyOrg:{
          params:{
            formalCount:0,
            prepareCount:0
          }
        },
        memberList1:[],
        memberList2:[],
        memberList3:[],
      };
    },
    computed:{

    },
    mounted() {
    },
    created() {
      this.getOrgInfo();
    },
    methods: {
      getOrgInfo(){
        let partyOrgId = this.$route.path.replace('/orgInfo/','');
        getPartyOrgInfo(partyOrgId).then(response => {
          this.partyOrg = response.data;
          this.createTreeData(response.data.partyPositionMemberList);
        });
      },
      createTreeData(partyPositionMemberList){
        for(let i in partyPositionMemberList){
          let member = partyPositionMemberList[i]
          if(member.partyPositionType =='1'){
            if(member.avatar != null){
              member.avatar = process.env.VUE_APP_BASE_API + member.avatar
            }
            this.memberList1.push(member)
          }
          if(member.partyPositionType =='2'){
            if(member.avatar != null){
              member.avatar = process.env.VUE_APP_BASE_API + member.avatar
            }
            this.memberList2.push(member)
          }
          if(member.partyPositionType =='3'||member.partyPositionType =='4'||member.partyPositionType =='5'){
            if(member.avatar != null){
              member.avatar = process.env.VUE_APP_BASE_API + member.avatar
            }
            this.memberList3.push(member)
          }
        }
        this.$forceUpdate()
      },

    }
  };
</script>

<style scoped>
  .org-info-bg {
    height: 100%;
    width: 100%;
    background: url("../../../assets/image/orgInfo-bg.jpg") no-repeat;
    background-size: 100% 100%;
  }

  .level-center{
    text-align: center; /*IE*/
    text-align: -moz-center; /*Firefox*/
    text-align: -webkit-center; /*Chrome*/
  }

  .org-info-lable{
    text-align: left;
    vertical-align: middle;
    float: left;
    font-size: 14px;
    font-weight: 600;
    color: #000000;
    line-height: 30px;
    padding: 0 12px 0 10px;
    box-sizing: border-box;
  }

  .org-info-content{
    text-align: left;
    vertical-align: middle;
    float: left;
    font-size: 14px;
    color: #606266;
    line-height: 30px;
    padding: 0 12px 0 0;
    box-sizing: border-box;
    padding-left: -20px;
  }

  .tree-chart >>> td{
    padding: 0 0 10px 0 !important;
  }

  .tree-chart >>> .node{
    width:auto !important;
  }

  /*.tree-chart >>> .avat{
    width:3em !important;
    height:3em !important;
  }*/

  .tree-chart >>> .avat img{
    background-color:rgb(165, 27, 27);
  }
  .tree-chart >>> .name{
    font-size: 12px;
    color: #000000;
  }

  .org-info{
    width: 100%;
    display: flex;
    justify-content: center;
  }
  .org-info-pic {
    width: 3.5rem;
    margin: 0rem 1.8rem;
  }

  .org-info-avatar {
    width: 100%;
    height: 3.5rem;
    background-image: url("../../../assets/image/dj-avatar.png");
    background-color: rgb(165, 27, 27);
    background-size: cover;
  }

  .org-info-position {
    padding-top: 0.2rem;
    width: 100%;
    height: 1rem;
    text-align: center;
    background-color: #cf001c;
    color: white;
    font-size: 0.5rem;
    font-weight: 600;
  }

  .org-info-name {
    padding-top: 0.2rem;
    width: 100%;
    height: 1rem;
    text-align: center;
    background-color: #C0C4CC;
    color: black;
    font-size: 0.5rem;
  }



  .y-line-div {
    width: 100%;
    height: 1rem;
    display: flex;
    justify-content: center;
  }
  .x-line-div {
    width: 100% ;
    height: 1px;
    margin-top: -1px;
    display: flex;
    justify-content: center;
  }

  .y-line {
    width: 3.5rem;
    height: 1rem;
    margin: 0rem 1.8rem;
  }

  .y-line:before {
    content: "";
    display: block;
    height: 100%;
    margin-left:50%;
    border-left: 1px solid #C0C4CC;
  }


  .x-line {
    width: 3.5rem;
    height: 1px;
  }
  .x-line:before {
    content: "";
    display: block;
    height: 100%;
    margin-left: -1px;
    margin-right: -1px;
    border-top: 1px solid #C0C4CC;
  }

  .x-line-center {
    width: 3.5rem;
    height: 1px;
    margin: 0rem 1.8rem;
  }
  .x-line-center:before {
    content: "";
    display: block;
    height: 100%;
    margin-left: -1.8rem;
    margin-right: -1.8rem;
    border-top: 1px solid #C0C4CC;
  }


</style>
