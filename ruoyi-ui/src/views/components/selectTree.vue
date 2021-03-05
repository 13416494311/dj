<!-- 树状选择器 -->
<template>
  <el-select :disabled="disabled"
             ref="treeSelect"
             style="width: 100%"
             :value="valueTitle"
             :clearable="clearable"
             :placeholder="placeholder"
             @visible-change="$forceUpdate()"
             @clear="clearHandle">
    <el-option :value="valueTitle" :label="valueTitle">
      <el-tree  id="tree-option"
                ref="selectTree"
                :accordion="accordion"
                :data="options"
                :props="props"
                :node-key="props.value"
                :default-expanded-keys="defaultExpandedKey"
                @node-click="handleNodeClick">
      </el-tree>
    </el-option>
  </el-select>
</template>

<script>
  export default {
    name: "el-tree-select",
    props:{
      vModel:String,
      disabled: Boolean,
      placeholder:{
        type: String,
        default: ()=>{ return "请选择" }
      },
      width:{
        type: String,
        default: ()=>{ return "100%" }
      },
      /* 配置项 */
      props:{
        type: Object,
        default:()=>{
          return {
            value:'id',             // ID字段名
            label: 'label',         // 显示名称
            children: 'children'    // 子级字段名
          }
        }
      },
      /* 选项列表数据(树形结构的对象数组) */
      options:{
        type: Array,
        default: ()=>{ return [] }
      },
      /* 初始值 */
      value:{
        type: Number,
        default: ()=>{ return null }
      },
      /* 可清空选项 */
      clearable:{
        type:Boolean,
        default:()=>{ return true }
      },
      /* 自动收起 */
      accordion:{
        type:Boolean,
        default:()=>{ return false }
      },
    },
    data() {
      return {
        valueId:this.value,    // 初始值
        valueTitle:'',
        defaultExpandedKey:[],
      }
    },
    mounted(){
      this.initHandle()
    },
    methods: {
      // 初始化值
      initHandle(){
        this.$nextTick(()=>{
          if(this.valueId){

            let parentLabel ="" ;
            let currentNode = this.$refs.selectTree.getNode(this.valueId)
            let parent = currentNode.parent.data
            if(this.vModel == "deptId" && parent.id != 100 ){
              parentLabel = parent.label + "/"
            }
            this.valueTitle = parentLabel + currentNode.data[this.props.label];

            this.$refs.selectTree.setCurrentKey(this.valueId);
            this.defaultExpandedKey = [this.valueId]
          }else{
            this.valueTitle = '';
            this.$refs.selectTree.setCurrentKey(undefined);
            this.defaultExpandedKey = []
          }
          let scrollWrap = document.querySelectorAll('.el-scrollbar .el-select-dropdown__wrap')[0]
          let scrollBar = document.querySelectorAll('.el-scrollbar .el-scrollbar__bar')
          scrollWrap.style.cssText = 'margin: 0px; max-height: none; overflow: hidden;'
          scrollBar.forEach(ele => ele.style.width = 0)
        })

      },
      // 切换选项
      handleNodeClick(node,e){

        let parentLabel ="" ;
        let parent = e.parent.data
        if(this.vModel == "deptId" && parent.id != 100 ){
          parentLabel = parent.label + "/"
        }
        this.valueTitle = parentLabel + node[this.props.label]

        this.valueId = node[this.props.value]
        this.$emit('selected',this.vModel,this.valueId)
        this.defaultExpandedKey = []
        let that = this;
        setTimeout(function() {
          that.$refs.treeSelect.blur();
        }, 50);
      },
      // 清除选中
      clearHandle(){
        this.valueTitle = ''
        this.valueId = null
        this.defaultExpandedKey = []
        this.clearSelected()
        this.$emit('selected',this.vModel,null)
      },
      /* 清空选中样式 */
      clearSelected(){
        let allNode = document.querySelectorAll('#tree-option .el-tree-node')
        allNode.forEach((element)=>element.classList.remove('is-current'))
      }
    },
    watch: {
      value(){
        this.valueId = this.value
        this.initHandle()
      },
    },
  };
</script>

<style scoped>
  .el-scrollbar .el-scrollbar__view .el-select-dropdown__item{
    height: auto;
    max-height: 274px;
    padding: 0;
    overflow: hidden;
    overflow-y: auto;
  }
  .el-select-dropdown__item.selected{
    font-weight: normal;
  }
  ul li >>>.el-tree .el-tree-node__content{
    height:auto;
    padding: 0 20px;
  }
  .el-tree-node__label{
    font-weight: normal;
  }
  .el-tree >>>.is-current .el-tree-node__label{
    color: #f5222d;
    font-weight: normal;
  }
  .el-tree >>>.is-current .el-tree-node__children .el-tree-node__label{
    color:#606266;
    font-weight: normal;
  }
</style>
