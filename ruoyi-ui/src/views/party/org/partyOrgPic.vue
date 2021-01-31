<template>
  <div class="app-container">
    <el-dialog :title="title" :visible.sync="open" width="90%" append-to-body
               @open="getHeight" :close-on-click-modal="false">
      <div class="text-center" :style="bodyStyle" >
        <vue2-org-tree ref="orgTree"
                       :data="data"
                       :horizontal="horizontal"
                       :collapsable="collapsable"
                       :label-class-name="labelClassName"
                       selected-class-name="bg-red"
                       selected-key="selectedKey"
                       :render-content="renderContent"
                       @on-expand="onExpand"
                       @on-node-click="onNodeClick"
        />
      </div>
      <div slot="footer" class="dialog-footer" :style="{textAlign:'center'}">
        <el-button @click="cancel">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import {partyOrgTree} from "@/api/party/org";


  export default {
    data() {
      return {
        title:'组织架构图',
        // 是否显示弹出层
        open: false,
        data: {},
        horizontal: false,  //  横向还是竖向
        collapsable: true,  //是否可以展开
        expandAll: true,    //全部展开
        labelClassName: "bg-white", //节点Class
        bodyStyle:{
          overflowY:'auto',
          height: '',
          marginLeft:'2%' ,
          paddingRight:'2%',
        },
      };
    },
    mounted() {
      this.getData();
      window.addEventListener('resize', this.getHeight);
    },
    watch:{
      data(){
        this.toggleExpand(this.data, this.expandAll);
      }
    },
    methods: {
      init(){
        this.getData();
        this.open = true;
      },
      /** 对话框自适应高度 */
      getHeight(){
        this.bodyStyle.height=window.innerHeight-281+'px';
      },
      // 取消按钮
      cancel() {
        this.open = false;
      },
      getData() {
        this.data = {};
        partyOrgTree(this.queryParams).then(response => {
          this.data = response.data;
        });
      },
      renderContent(h, data) {
        return data.label;
      },
      onExpand(e, data) {
        if ('expand' in data) {
          data.expand = !data.expand
          if (!data.expand && data.children) {
            this.collapse(data.children)
          }
        } else {
          this.$set(data, 'expand', true)
        }
      },
      onNodeMouseOver(e, data) {
        console.log('MOUSE OVER', e);
      },
      onNodeMouseOut(e, data) {
        console.log('MOUSE OUT', e);
      },
      onNodeClick(e, data) {
        this.cleanSelectNode(this.data);
        this.$set(data, 'selectedKey', !data['selectedKey']);
        this.$refs.orgTree.$forceUpdate()
      },
      cleanSelectNode(data) {
        data.selectedKey = false;
        let children = data.children;
        if (children != undefined) {
          for (let i = 0; i < children.length; i++) {
            let data = children[i];
            this.cleanSelectNode(data);
          }
        }
      },
      collapse(list) {
        var _this = this;
        list.forEach(function (child) {
          if (child.expand) {
            child.expand = false;
          }
          child.children && _this.collapse(child.children);
        });
      },
      expandChange() {
        this.toggleExpand(this.data, this.expandAll);
      },
      toggleExpand(data, val) {
        var _this = this;
        if (Array.isArray(data)) {
          data.forEach(function (item) {
            _this.$set(item, "expand", val);
            if (item.children) {
              _this.toggleExpand(item.children, val);
            }
          });
        } else {
          this.$set(data, "expand", val);
          if (data.children) {
            _this.toggleExpand(data.children, val);
          }
        }
      }
    }
  };
</script>
<style type="text/css">
  .org-tree-node-label {
    white-space: nowrap;
  }
  .org-tree-node-label-inner{
    writing-mode:tb-rl !important;
    letter-spacing:3px;
  }

  .bg-white {
    background-color: white;
  }

  .bg-red {
    background-color: #f10006;
    color: white !important;
  }
  .bg-white {
    color: black ;
  }

  .text-center {
    width: 98%;
    margin-left: 1%;
    overflow: auto;
  }


</style>
