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
        <el-form-item label="处理情况" prop="reason">
          <el-input v-model="form.reason" type="textarea" :autosize="{ minRows: 4, maxRows: 4}" placeholder="请输入内容"/>
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

  export default {
    name: "AuditResultPass",
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
          reason: [
            { required: true, message: "处理情况不能为空", trigger: "blur" }
          ],
        },
        bodyStyle: {
          overflowY: 'auto',
          height: '',
          marginLeft: '20%',
          paddingRight: '20%',
        },
      };
    },
    mounted() {

    },
    created() {

    },
    methods: {
      init(){
        this.reset();
        this.open=true;
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
          state: "4",
          reason: undefined
        };
        this.resetForm("form");
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            this.$emit("ok",this.form);
            this.open = false;
          }
        });
      },
    }
  };
</script>
