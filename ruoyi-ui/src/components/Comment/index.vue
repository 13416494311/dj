<template>
  <div class="app-container">
    <el-collapse v-model="activeNames" @change="handleChange">
      <el-collapse-item :title="commentTitle1" name="1">
        <el-button
          @click="thumbUp"
          :type="commentFlag?'danger':''"
          class="el-icon-star-off"
          circle/>
        <div class="comment-name">{{ commentName }}</div>
      </el-collapse-item>

      <el-collapse-item :title="commentTitle2" name="2">
        <comment  class="comment-class" ref="commentRef"
                  :avatar="avatar"
                  commentWidth="80%"
                  :label="label"
                  :authorId="authorId"
                  :commentList="commentList"
                  :commentNum="commentNum"
                  @doSend="doSend"
                  @doChidSend="doChidSend"/>
      </el-collapse-item>

    </el-collapse>

    <websocket ref="websocket" :groupId="businessUuid" @receive="receiveMessage"/>
  </div>
</template>

<script>
  import comment from 'bright-comment'
  import { listCommentTree,addComment,delComment } from "@/api/system/comment";
  import Websocket from "../WebSocket/websocket";
  import { getUserProfile } from "@/api/system/user";
  export default {
    name: 'Index',
    components: {
      Websocket,
      comment
    },
    props: {
      businessUuid: {
        required: true,
        type: String,
        default: ''
      },
      authorId: {
        required: true,
        type: Number,
        default: 0
      },
    },
    data() {
      return {
        activeNames: ['1','2'],
        commentFlag:false,
        commentName:'',
        label:'政治生日党员',
        commentList:[],
        commentNum:0,
        commentTitle1:'点赞（'+this.commentNum+'）',
        commentTitle2:'评论（'+this.commentNum+'）',
        user: {},
        commentId:undefined,
        avatar:undefined,
      }
    },
    created(){
      this.init();
    },
    watch:{
      // 根据名称筛选部门树
      'businessUuid'(val) {
        this.init();
      },
    },
    methods: {
      init() {
        getUserProfile().then(response => {
          this.user = response.data;
          this.avatar = this.user.avatar == '' ? require('@/assets/image/profile.jpg') : process.env.VUE_APP_BASE_API + this.user.avatar
        }).then(()=>{
          this.getList1();
          this.getList2();
          this.openWebsocket();
        })
      },
      thumbUp(){
        if(!this.commentFlag){
          let commentForm = {}
          commentForm.businessUuid = this.businessUuid;
          commentForm.commentType = '1'
          addComment(commentForm).then(response => {
            if (response.code === 200) {
              this.msgSuccess("点赞成功");
            } else {
              this.msgError(response.msg);
            }
          });
        }else{
          delComment(this.commentId).then(response => {
            if (response.code === 200) {
              this.msgSuccess("取消点赞");
            } else {
              this.msgError(response.msg);
            }
          });
        }


      },
      handleChange(val) {
        console.log(val);
      },
      receiveMessage(data){
        switch (data.commentType) {
          case "1":this.getList1();break;
          case "2":this.getList2();break;
          default:break;
        }
      },
      /**
       * 获取点赞数据
       */
      getList1(){
        let commentForm = {}
        commentForm.businessUuid = this.businessUuid;
        commentForm.commentType = '1'
        listCommentTree(commentForm).then(response => {
          if (response.code === 200) {
            this.commentFlag = false;
            this.commentName = '';
            this.commentTitle1='点赞（'+response.data.commentNum+'）'
            let commentList = response.data.commentList;
            for(var i in commentList){
              this.commentName +="@"+commentList[i].commentUser.nickName;
              if(this.user.userId == commentList[i].commentUserId){
                this.commentFlag = true;
                this.commentId = commentList[i].id
              }
            }
          } else {
            this.msgError(response.msg);
          }
        });
      },
      /**
       * 获取点评数据
       */
      getList2(){
        let commentForm = {}
        commentForm.businessUuid = this.businessUuid;
        commentForm.commentType = '2'
        listCommentTree(commentForm).then(response => {
          if (response.code === 200) {
            this.commentNum = response.data.commentNum;
            this.commentTitle2='评论（'+this.commentNum+'）';
            this.commentList = response.data.commentList;
            this.avatarPre(this.commentList)
          } else {
            this.msgError(response.msg);
          }
        });
      },
      /**
       * 格式化头像路径
       * @param commentList
       */
      avatarPre(commentList){
        for(var i in commentList){
          if(commentList[i].commentUser){
            commentList[i].commentUser.avatar =
              commentList[i].commentUser.avatar == "" ?
                require("@/assets/image/profile.jpg") : process.env.VUE_APP_BASE_API + commentList[i].commentUser.avatar;
          }
          if(commentList[i].targetUser){
            commentList[i].targetUser.avatar =
              commentList[i].targetUser.avatar == "" ?
                require("@/assets/image/profile.jpg") : process.env.VUE_APP_BASE_API + commentList[i].targetUser.avatar;
          }
          if(commentList[i].childrenList && commentList[i].childrenList.length >0){
            this.avatarPre(commentList[i].childrenList)
          }
        }
      },
      /**
       * 发送评论
       * @param content
       */
      doSend(content){
        if(content==''){
          this.msgInfo("评论内容不能为空！");
          return;
        }

        let commentForm = {}
        commentForm.businessUuid = this.businessUuid;
        commentForm.commentType = '2'
        commentForm.content = content
        addComment(commentForm).then(response => {
          if (response.code === 200) {
            this.msgSuccess("发送成功");
            this.$refs.commentRef.cancel(0);
          } else {
            this.msgError(response.msg);
          }
        });
      },
      /**
       * 回复评论
       * @param content
       * @param bid
       * @param pid
       */
      doChidSend(content,bid,pid){
        if(content==''){
          this.msgInfo("回复内容不能为空！");
          return;
        }

        let commentForm = {}
        commentForm.businessUuid = this.businessUuid;
        commentForm.commentType = '2'
        commentForm.targetUserId = bid
        commentForm.parentId = pid
        commentForm.content = content

        addComment(commentForm).then(response => {
          if (response.code === 200) {
            this.msgSuccess("回复成功");
            this.$refs.commentRef.cancel(Number(this.$refs.commentRef.textareaMap.length)-1)
          } else {
            this.msgError(response.msg);
          }
        });

      },
      closeWebsocket(){
        this.$refs.websocket.closeWebsocket();
      },
      openWebsocket(){
        this.$refs.websocket.init();
      },
    }
  }
</script>

<style scoped>

  .comment-name{
    margin-top: 20px;
    color: #409eff;
    letter-spacing:3px;
  }
  .app-container  >>> .el-collapse-item__header{
    font-size: 16px !important;
    font-weight:600 !important;
  }
  .comment-class{
    margin-bottom: 100px;
  }
  .comment-class >>> .OwO-logo {
    width: 70px;
    border: 0px !important;
    background: url("../../assets/image/emoji.png") no-repeat center !important;
  }
  .comment-class >>>.OwO .OwO-items{
    overflow: auto;
  }
  .comment-class >>> .OwO-logo span  {
   display: none;
  }
  .comment-class >>> .comm  {
    display: none;
  }
  .comment-class >>> .btn {
    background-color: red;
  }
  .comment-class >>> .btn-cancel{
    background-color: grey !important;
  }
  .comment-class >>> .su{
    background-color: red;
  }
</style>
