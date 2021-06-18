<template>
  <div style="padding: 30px">
    <due-card ref="dueCard" :disabled="disabled" :dueOrgId="dueOrgId" @ok="cancel" ></due-card>

    <div :style="{textAlign:'center',paddingTop:'30px'}">
      <el-button v-if="!disabled" type="primary" @click="submitDue(1)">保 存</el-button>
      <el-button v-if="!disabled" type="primary" @click="submitDue(2)">提 交</el-button>
      <el-button @click="cancel">关 闭</el-button>
    </div>

  </div>


</template>



<script>

  import {listTodo, getTodo, delTodo, addTodo, updateTodo, exportTodo} from "@/api/sys/todo";
  import DueCard from "../../party/due/dueCard";


  export default {
    name: "PartyMemberDue",
    components: {DueCard },
    data() {
      return {
        todoId: undefined,
        todoStatus: undefined,
        disabled: false,
        dueOrgId:undefined,
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
          this.dueOrgId = Number(params.dueOrgId);

        });
      },
      // 取消按钮
      cancel() {
        if(this.statue==2){
          updateTodo({"todoId": this.todoId, "status": "1"})
        }
        this.$store.dispatch('tagsView/delView', this.$route)
        this.$router.go(-1)
      },
      submitDue(statue){
        this.status = statue
        this.$refs.dueCard.submit(statue);
      },
    }
  };
</script>
