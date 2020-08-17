<template>
  <el-dialog :title="title" :visible.sync="open" width="70%" append-to-body
             @open="getHeight" :close-on-click-modal="false">
    <div :style="bodyStyle">
      <tree-transfer
        :title="transferTitle"
        :from_data='fromData'
        :to_data='toData'
        :defaultProps="{label:'label'}"
        @addBtn='add'
        @removeBtn='remove'
        :mode='mode'
        height='95%'
        filter
        openAll
      >
      </tree-transfer>
    </div>
    <div slot="footer" class="dialog-footer" :style="{textAlign:'center'}">
      <el-button type="primary" @click="submit">确 定</el-button>
      <el-button @click="cancel">取 消</el-button>
    </div>
  </el-dialog>

</template>

<script>
  import treeTransfer from 'el-tree-transfer' // 引入
  import { partyOrgTreeselect } from "@/api/party/org";

  export default {
    components: {treeTransfer}, // 注册
    data() {
      return {
        mode: 'transfer',
        fromData: [
          {
            id: "1",
            pid: 0,
            label: "一级 1",
            children: [
              {
                id: "1-1",
                pid: "1",
                label: "二级 1-1",
                disabled: true,
                children: []
              },
              {
                id: "1-2",
                pid: "1",
                label: "二级 1-2",
                children: [
                  {
                    id: "1-2-1",
                    pid: "1-2",
                    children: [],
                    label: "二级 1-2-1"
                  },
                  {
                    id: "1-2-2",
                    pid: "1-2",
                    children: [],
                    label: "二级 1-2-2"
                  }
                ]
              }
            ]
          },
        ],
        toData: [],
        // 弹出层标题
        title: "选择党组织",
        transferTitle:["未选党组织","已选党组织"],
        // 是否显示弹出层
        open: false,
        bodyStyle: {
          overflowY: 'auto',
          height: '',
          marginLeft: '2%',
          paddingRight: '2%',
        },
      }
    },
    mounted() {
      window.addEventListener('resize', this.getHeight);
    },
    created() {
      this.getPartyOrgTreeSelect();
    },
    methods: {
      /** 查询党组织下拉树结构 */
      getPartyOrgTreeSelect() {
        partyOrgTreeselect().then(response => {
          this.fromData = this.treeInitData(response.data);
        });
      },
      /** 对话框自适应高度 */
      getHeight() {
        this.bodyStyle.height = window.innerHeight - 281 + 'px';
      },
      // 切换模式 现有树形穿梭框模式transfer 和通讯录模式addressList
      changeMode() {
        if (this.mode == "transfer") {
          this.mode = "addressList";
        } else {
          this.mode = "transfer";
        }
      },
      // 监听穿梭框组件添加
      add(fromData, toData, obj) {
        // 树形穿梭框模式transfer时，返回参数为左侧树移动后数据、右侧树移动后数据、移动的{keys,nodes,halfKeys,halfNodes}对象
        // 通讯录模式addressList时，返回参数为右侧收件人列表、右侧抄送人列表、右侧密送人列表
        console.log("fromData:", fromData);
        console.log("toData:", toData);
        console.log("obj:", obj);
      },
      // 监听穿梭框组件移除
      remove(fromData, toData, obj) {
        // 树形穿梭框模式transfer时，返回参数为左侧树移动后数据、右侧树移动后数据、移动的{keys,nodes,halfKeys,halfNodes}对象
        // 通讯录模式addressList时，返回参数为右侧收件人列表、右侧抄送人列表、右侧密送人列表
        console.log("fromData:", fromData);
        console.log("toData:", toData);
        console.log("obj:", obj);
      },
      submit(){
        this.$emit("callbackOrg",this.searchKey);
      },
      // 取消按钮
      cancel() {
        this.open = false;
      },
    },

  }
</script>
