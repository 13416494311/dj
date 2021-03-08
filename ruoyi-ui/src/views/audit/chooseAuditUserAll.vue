<template>
  <div class="app-container">
    <!-- 添加或修改审批记录对话框 -->
    <el-dialog :title="title"
               :visible.sync="open"
               append-to-body
               :close-on-click-modal="false"
               width="30%"
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">

        <el-form-item label="处理人" prop="auditMemberId">
          <el-input :disabled="true" v-model="form.auditMemberName" placeholder="请选择处理人">
            <el-button  slot="append" icon="el-icon-search"
                       @click="openMemberChoose"></el-button>
          </el-input>
        </el-form-item>

        <el-form-item label="处理情况" prop="reason">
          <el-input v-model="form.reason" type="textarea" :autosize="{ minRows: 4, maxRows: 4}" placeholder="请输入处理情况内容"/>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer" :style="{textAlign:'center'}">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <party-member-choose ref="partyMember" @callbackMember="setMember"/>
  </div>
</template>

<script>
  import { optionselect } from "@/api/system/user";
  import PartyMember from "../party/member/partyMember";
  import PartyMemberChoose from "../party/org/partyMemberChoose";

  export default {
    name: "chooseAuditUserAll",
    components: {PartyMemberChoose, PartyMember},
    data() {
      return {
        //是否必填
        required: false,
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          auditMemberId: [
            {required: true, message: "审批人不能为空", trigger: "blur"},
          ],
        },
        bodyStyle: {
          overflowY: 'auto',
          height: '',
          marginLeft: '20%',
          paddingRight: '20%',
        },
        type: undefined,
        memberId: undefined,
      };
    },
    mounted() {

    },
    created() {

    },
    methods: {
      openMemberChoose() {
        this.$refs.partyMember.open = true;
        this.$refs.partyMember.title = "选择处理人";
      },
      setMember(member) {
        this.form.auditMemberId = member.memberId;
        this.form.auditMemberName = member.memberName;
        this.$forceUpdate();
      },
      init(){
        this.reset();
        this.open = true;
        this.title = "选择处理人";
      },
      /** 对话框自适应高度 */
      getHeight() {
        this.bodyStyle.height = window.innerHeight - 281 + 'px';
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          auditMemberId: undefined,
          auditMemberName: undefined,
          reason: undefined
        };
        this.resetForm("form");
      },

      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            this.open = false;
            this.$emit("ok",this.form);
          }else{
            setTimeout(() => {
              var isError = document.getElementsByClassName("is-error");
              isError[0].querySelector('input').focus();
            }, 100);
            return false;
          }
        });
      },
    }
  };
</script>
