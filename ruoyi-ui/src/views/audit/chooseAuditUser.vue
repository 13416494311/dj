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
        <el-form-item label="审批人" prop="auditUserId">
          <el-select v-model="form.auditUserId"
                     style="width: 100%" placeholder="请选择审批人">
            <el-option
              v-for="item in auditUserOptions"
              :key="item.userId"
              :label="item.userName"
              :value="item.userId"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="备注" prop="reason">
          <el-input v-model="form.reason" type="textarea" :autosize="{ minRows: 4, maxRows: 4}" placeholder="请输入备注内容"/>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer" :style="{textAlign:'center'}">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { optionselect } from "@/api/system/user";

  export default {
    name: "chooseAuditUser",
    data() {
      return {
        //是否必填
        required: false,
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        auditUserOptions: [],
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          auditUserId: [
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
      //window.addEventListener('resize', this.getHeight);
    },
    created() {
      //this.auditUserOptionSelect();
    },
    methods: {
      init(roleId,type,memberId){
        this.open = true ;
        this.type = type;
        this.memberId = memberId;
        optionselect(roleId).then(response => {
          this.auditUserOptions = response.data;
        });
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
          auditUserId: undefined,
          reason: undefined
        };
        this.resetForm("form");
      },

      /** 新增按钮操作 */
      handleAdd(value) {
        this.reset();
        this.open = true;
        this.title = "选择审批人";
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            this.open = false;
            if(this.type=="del"){
              this.form.memberId = this.memberId;
              this.$emit("del",this.form);
            }else{
              this.$emit("ok",this.form);
            }
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
