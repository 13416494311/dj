<template>
  <div style="padding: 30px">

    <assessment-card ref="todoCard"
                     :disabled1="disabled1"
                     :disabled2="disabled2"
                     :assessmentId="assessmentId"
                     @ok="cancel"  />

    <div :style="{textAlign:'center',paddingTop:'30px'}">
      <el-button v-if="!disabled1||!disabled2" type="primary" @click="submit(1)">保 存</el-button>
      <el-button v-if="!disabled1||!disabled2" type="primary" @click="submit(2)">提 交</el-button>
      <el-button @click="cancel">关 闭</el-button>
    </div>

  </div>


</template>



<script>

  import {listTodo, getTodo, delTodo, addTodo, updateTodo, exportTodo} from "@/api/sys/todo";
  import AssessmentCard from "../../party/assessment/assessmentCard";



  export default {
    name: "OrgAssessment",
    components: {AssessmentCard },
    data() {
      return {
        todoId: undefined,
        todoStatus: undefined,
        disabled1: true,
        disabled2: true,
        assessmentId:undefined,
        status:undefined,
      };
    },
    mounted() {

    },
    created() {
      this.init();
    },
    watch:{

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
          this.assessmentId = Number(params.assessmentId);
          let orgAssessmentStatus = params.orgAssessmentStatus
          this.statusChange(orgAssessmentStatus);
        });
      },
      statusChange(status){
        if(this.todoStatus ==0){
          switch (status) {
            case "1": this.disabled1 = false ;this.disabled2 = true ;break;
            case "2": this.disabled1 = true ;this.disabled2 = false ;break;
            default:  this.disabled1 = false ;this.disabled2 = false ;break;
          }
        }else{
          this.disabled1 = true ;
          this.disabled2 = true
        }
      },
      // 取消按钮
      cancel(status) {
        if(this.status==2){
          updateTodo({"todoId": this.todoId, "status": "1"})
        }
        this.$store.dispatch('tagsView/delView', this.$route)
        this.$router.go(-1)
      },
      submit(statue){
        this.status = statue
        this.$refs.todoCard.submit(statue);
      },
    }
  };
</script>
