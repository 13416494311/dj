<template>
  <div>
    <el-tabs v-model="activeTab" @tab-click="handleTabClick">
      <el-tab-pane name="0">
        <span slot="label">
          <i class="el-icon-message-solid"></i>
          待办
          <el-badge :max="99" :value="todoCount" />
        </span>
        <to-do ref="toDo"/>
      </el-tab-pane>
      <el-tab-pane label="已办" name="1">
        <have-done ref="haveDone"/>
      </el-tab-pane>
      <el-tab-pane name="2">
        <span slot="label">
          <i class="el-icon-bell"></i>
          待阅
          <el-badge :max="99" :value="toReadCount" />
        </span>
        <to-read ref="toRead"/>
      </el-tab-pane>
      <el-tab-pane label="已阅" name="3">
        <have-read ref="haveRead"/>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
  import toDo from "./toDo";
  import haveDone from "./haveDone";
  import toRead from "./toRead";
  import haveRead from "./haveRead";
  import { getTodoCount } from "@/api/sys/todo";

  export default {
    name: "index",
    components: { toDo,haveDone,toRead, haveRead},
    data() {
      return {
        activeTab: "0",
        todoCount: 0,
        toReadCount: 0,
      };
    },
    mounted() {
    },
    created() {
      this.init();
      this.getTodoCount();
    },
    methods: {
      getTodoCount(){
        this.todoCount = 0;
        this.toReadCount = 0;
        getTodoCount("0").then((respone)=>{
          this.todoCount = respone.data;
        })
        getTodoCount("2").then((respone)=>{
          this.toReadCount = respone.data;
        })
      },
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
