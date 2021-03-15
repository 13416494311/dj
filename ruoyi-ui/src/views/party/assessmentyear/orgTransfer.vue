<template>
  <el-dialog :title="title" :visible.sync="open" width="80%" append-to-body
             @open="getHeight" :close-on-click-modal="false">
    <div :style="bodyStyle">
      <el-row :gutter="20">
        <el-col :span="8" :xs="24">
          <el-tree
            :data="partyOrgOptions"
            :props="defaultProps"
            :expand-on-click-node="false"
            :filter-node-method="filterNode"
            ref="tree"
            default-expand-all
            @node-click="handleNodeClick"
          />
        </el-col>
        <el-col :span="16" :xs="24">
          <el-transfer
            class="table-custom-drawer"
            :titles="titles"
            filterable
            :filter-method="filterMethod"
            filter-placeholder="请输入党组织名称"
            height='95%'
            width="100%"
            v-model="value"
            :data="data"
            style="display: inline-block"
            :render-content="transferRenderFunc"
          >
          </el-transfer>
        </el-col>
      </el-row>

    </div>
    <div slot="footer" class="dialog-footer" :style="{textAlign:'center'}">
      <el-button type="primary" @click="submit">确 定</el-button>
      <el-button @click="cancel">取 消</el-button>
    </div>
  </el-dialog>

</template>
<style>
  .el-transfer-panel{
    height: 500px;
  }
  .el-transfer-panel__list.is-filterable{
    height: 400px;
  }
</style>
<script>
  import {childrenListPartyOrg, partyOrgTreeselect} from "@/api/party/org";
  import {addAssessmentList} from "@/api/party/assessment";

  export default {
    data() {
      return {
        assessmentyearUuid:undefined,
        year : undefined,
        assessmentName : undefined,
        planUuid:undefined,
        // 弹出层标题
        title: "选择党组织",
        titles: ["未选党组织", "已选党组织"],
        // 是否显示弹出层
        open: false,
        bodyStyle: {
          overflowY: 'auto',
          height: '',
          marginLeft: '2%',
          paddingRight: '2%',
        },
        data: [],
        value: [],
        // 党组织架构树选项
        partyOrgOptions: [],
        defaultProps: {
          value: 'id',          // 唯一标识
          label: 'label',       // 标签显示
          children: 'children', // 子级
        },
        clickWaitFlag:false,
      }
    },
    mounted() {
      window.addEventListener('resize', this.getHeight);
    },
    created() {
      this.getPartyOrgTreeSelect();
      //this.getPartyOrgSelect(1);
    },
    methods: {
      //双击穿梭
      transferRenderFunc(h, option) {
        return this.h(this.value, h, option);
      },
      h(modelV, h, option) {
        return h(
          'span', {
            attrs: {
              id: option.label
            },
            domProps: {
              innerHTML: option.label
            },
            on: {
              "dblclick": function () {
                //简单的逻辑  如果不在 v-model值数组里面，则push，否则从数组中删除
                if (modelV.includes(option.key)) {
                  let index = modelV.indexOf(option.key)
                  modelV.splice(index, 1);
                } else {
                  modelV.push(option.key)
                }
              },
            }
          }
        )
      },
      // 筛选节点
      filterNode(value, data) {
        if (!value) return true;
        return data.label.indexOf(value) !== -1;
      },
      // 节点单击事件
      handleNodeClick(data) {
        this.getPartyOrgSelect(data.id);
      },
      /** 查询党组织下拉树结构 */
      getPartyOrgTreeSelect() {
        partyOrgTreeselect().then(response => {
          this.partyOrgOptions = this.treeInitData(response.data);
        });
      },
      filterMethod(query, item) {
        return item.label.indexOf(query) > -1;
      },
      /** 查询党组织下拉树结构 */
      getPartyOrgSelect(partyOrgId) {
        if(this.clickWaitFlag == true){
          return;
        }
        this.clickWaitFlag = true;
        this.data = []
        childrenListPartyOrg(partyOrgId).then(response => {
          let partyOrgs = response.data;
          partyOrgs.forEach(partyOrg => {
            this.data.push({
              label: partyOrg.partyOrgName,
              key: partyOrg.partyOrgId,
            });
          });
          this.clickWaitFlag = false;
        });
      },
      /** 对话框自适应高度 */
      getHeight() {
        this.bodyStyle.height = window.innerHeight - 281 + 'px';
      },
      submit(){
        if(this.value.length==0){
          this.msgInfo("请选择参与考核党支部！")
          return;
        }
        let formData = new FormData();
        formData.append("assessmentyearUuid", this.assessmentyearUuid);
        formData.append("assessmentName", this.assessmentName);
        formData.append("year", this.year);
        formData.append("partyOrgIds", this.value.join(","));
        addAssessmentList(formData).then(response => {
          if (response.code === 200) {
            this.msgSuccess("新增成功");
            this.$emit("callbackOrg",this.value);
            this.open = false;
          } else {
            this.msgError(response.msg);
          }
        });


      },
      // 取消按钮
      cancel() {
        this.open = false;
      },
    },

  }
</script>
