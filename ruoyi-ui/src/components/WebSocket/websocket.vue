<template>
  <div>

  </div>
</template>

<script>
  import { getUserProfile } from "@/api/system/user";

  export default {
    name : 'webSocket',
    props: {
      groupId: {
        required: true,
        type: String,
        default: ''
      },
    },
    data() {
      return {
        websocket: null,
        user: {},
      }
    },
    created() {
      //this.init();
    },
    destroyed() {
      this.websocket.close() //离开路由之后断开websocket连接
    },
    methods: {
      init() {
        getUserProfile().then(response => {
          this.user = response.data;
        }).then(()=>{
          this.initWebSocket();
        });
      },
      initWebSocket(){ //初始化weosocket
        let url = window.location.href;
        let path = this.$route.path;
        let ws =url.replace(path,'').replace('http','ws')
        const webSocketUrl = ws+process.env.VUE_APP_BASE_API +"/webSocket/"+this.groupId+"/"+this.user.userId;
        this.websocket = new WebSocket(webSocketUrl);
        this.websocket.onmessage = this.onMessage;
        this.websocket.onopen = this.onOpen;
        this.websocket.onerror = this.onError;
        this.websocket.onclose = this.onClose;

      },
      onOpen(){ //连接建立之后执行send方法发送数据

      },
      onError(){//连接建立失败重连

      },
      onMessage(e){ //数据接收
        const data = JSON.parse(e.data);
        this.$emit('receive', data)
      },
      send(data){//数据发送

      },
      onClose(e){  //关闭
        console.log('断开连接',e);
      },
      closeWebsocket(){
        this.websocket.close();
      }
    },
  }
</script>
<style lang='less'>

</style>
