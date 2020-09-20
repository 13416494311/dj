<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" @tab-click="handleTabClick">
      <el-tab-pane label="待办" name="0">
        <to-do ref="toDo"/>
      </el-tab-pane>
      <el-tab-pane label="已办" name="1">
        <have-done ref="haveDone"/>
      </el-tab-pane>
      <el-tab-pane label="待阅" name="2">
        <to-read ref="toRead"/>
      </el-tab-pane>
      <el-tab-pane label="已阅" name="3">
        <have-read ref="haveRead"/>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
  import toDo from "./sys/todoType/toDo";
  import haveDone from "./sys/todoType/haveDone";
  import toRead from "./sys/todoType/toRead";
  import haveRead from "./sys/todoType/haveRead";

  export default {
    name: "index",
    components: { toDo,haveDone,toRead, haveRead},
    data() {
      return {
        activeTab: "0",
      };
    },
    mounted() {
    },
    created() {
      this.init();
    },
    methods: {
      init(){
        setTimeout(()=>{
          this.$refs.toDo.init("0"); //待办
          this.$refs.haveDone.init("1"); //已办
          this.$refs.toRead.init("2"); //待阅
          this.$refs.haveRead.init("3"); //已阅
        },10)
      },
      handleTabClick(tab, event){
        switch (tab.name) {
          case "0":
            this.$refs.toDo.init("0"); //待办
            break;
          case "1":
            this.$refs.haveDone.init("1"); //已办
            break;
          case "2":
            this.$refs.toRead.init("2"); //待阅
            break;
          case "3":
            this.$refs.haveRead.init("3"); //已阅
            break;
          default:
            break;
        }
      },


    }
  };
</script>
