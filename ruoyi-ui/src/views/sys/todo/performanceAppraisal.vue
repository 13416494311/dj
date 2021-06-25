<template>
  <div style="padding: 30px">

    <performance-score-card ref="todoCard"
                            :disabled="disabled"
                            :assessmentyearId="assessmentyearId"
                            @ok="cancel"  />

    <div :style="{textAlign:'center',paddingTop:'30px'}">
      <el-button v-if="!disabled" type="primary" @click="submit(1)">保 存</el-button>
      <el-button v-if="!disabled" type="primary" @click="submit(2)">提 交</el-button>
      <el-button @click="cancel">关 闭</el-button>
    </div>

  </div>


</template>



<script>

  import {listTodo, getTodo, delTodo, addTodo, updateTodo, exportTodo} from "@/api/sys/todo";
  import PerformanceScoreCard from "../../party/performanceScore/performanceScoreCard";



  export default {
    name: "PerformanceAppraisal",
    components: {PerformanceScoreCard },
    data() {
      return {
        todoId: undefined,
        todoStatus: undefined,
        disabled: true,
        assessmentyearId:undefined,
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
          this.assessmentyearId = Number(params.assessmentyearId);

          this.statusChange();
        });
      },
      statusChange(){
        if(this.todoStatus ==0){
          this.disabled = false
        }else{
          this.disabled = true
        }
      },
      // 取消按钮
      cancel() {
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
