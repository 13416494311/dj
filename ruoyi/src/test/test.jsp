<!DOCTYPE HTML>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport"
          content="maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,initial-scale=1.0"/>
    <meta name="format-detection" content="telephone=no,email=no,date=no,address=no">
    <title>AUI快速完成布局</title>
    <link rel="stylesheet" type="text/css" href="../css/aui/aui.css"/>
    <style type="text/css">
        .aui-bar-tab .aui-active {
            color: red;
        }
    </style>
</head>
<body>
<header class="aui-bar aui-bar-nav aui-bg-danger" id="aui-header">
    <div class="aui-title" id="headerTitle"></div>
</header>

<footer class="aui-bar aui-bar-tab" id="footer">
    <div id="hometabbar" class="aui-bar-tab-item aui-active" tapmode onclick="randomSwitchBtn(this,'首页',0)">
        <i class="aui-iconfont aui-icon-home"></i>
        <div class="aui-bar-tab-label">首页</div>
    </div>
    <div id="worktabbar" class="aui-bar-tab-item" tapmode onclick="randomSwitchBtn(this,'组织生活',1)">
        <i class="aui-iconfont aui-icon-like"></i>
        <div class="aui-bar-tab-label">组织生活</div>
    </div>
    <div id="messagetabbar" class="aui-bar-tab-item" tapmode onclick="randomSwitchBtn(this,'消息',2)">
        <%--<div id='message' class="aui-badge"></div>--%>
        <i class="aui-iconfont aui-icon-comment"></i>
        <div class="aui-bar-tab-label">消息</div>
    </div>
    <div id="mytabbar" class="aui-bar-tab-item" tapmode onclick="randomSwitchBtn(this,'我是党员',3)">
        <%--<div class="aui-dot"></div>--%>
        <i class="aui-iconfont aui-icon-my"></i>
        <div class="aui-bar-tab-label">我是党员</div>
    </div>
</footer>
</body>
<script type="text/javascript" src="../script/api.js"></script>

<script type="text/javascript">
    var push;
    apiready = function () {
        api.parseTapmode();
        push = api.require('push');
        fnBindPush();
        fnListenerPush();
        fnOpenMainCenterFrameGroup();
    }

    function fnBindPush() {
        push.bind({
            userName: $api.getStorage('loginUser').nickName,
            userId: $api.getStorage('loginUser').userId
        }, function (ret, err) {

        });

    }

    //监听消息  监听不了通知但是接受到消息之后 可以显示到通知
    function fnListenerPush() {
        push.setListener(function (ret, err) {
            if (ret) {
                console.log(JSON.stringify(ret));
                //var message = $api.byId('message');
                //message.innerHTML = '1';

                api.notification({
                    notify: {
                        title: '【系统消息】',
                        content: ret.data
                    }
                });

                api.setAppIconBadge({
                    badge: 1
                });
            }
        });
    }


    function fnOpenMainCenterFrameGroup() {
        var header = $api.dom('header'); // 获取 header 标签元素
        var footer = $api.dom('footer'); // 获取 footer 标签元素

        var headerH = $api.fixStatusBar(header);
        var footerH = $api.fixTabBar(footer);

        api.openFrameGroup({
            name: 'footer_tab_frame',
            scrollEnabled: true,
            rect: { // 推荐使用Margin布局，用于适配屏幕的动态变化
                marginTop: headerH, // main页面距离win顶部的高度
                marginBottom: footerH, // main页面距离win底部的高度
                w: 'auto' // main页面的宽度 自适应屏幕宽度
            },
            index: 0,
            preload: 3,
            frames: [{
                name: 'footer_tab_home',
                url: 'main-bak.html',
                bounces: false
            }, {
                name: 'footer_tab_work',
                url: 'work_frm.html',
                bounces: false
            }, {
                name: 'footer_tab_message',
                url: 'message_frm.html',
                bounces: false
            }, {
                name: 'footer_tab_my',
                url: 'my_frm.html',
                bounces: false
            }]
        }, function (ret, err) {
            var footer = $api.byId('footer');
            var footerAct = $api.dom(footer, '.aui-bar-tab-item.aui-active');
            $api.removeCls(footerAct, 'aui-active');

            if (ret.index == '0') {
                $api.text($api.byId('headerTitle'), '首页');
                $api.addCls($api.byId('hometabbar'), 'aui-active');
            } else if (ret.index == '1') {
                $api.text($api.byId('headerTitle'), '组织生活');
                $api.addCls($api.byId('worktabbar'), 'aui-active');
            } else if (ret.index == '2') {
                $api.text($api.byId('headerTitle'), '消息');
                $api.addCls($api.byId('messagetabbar'), 'aui-active');
            } else if (ret.index == '3') {
                $api.text($api.byId('headerTitle'), '我是党员');
                $api.addCls($api.byId('mytabbar'), 'aui-active');
            }

        });
    }


    function randomSwitchBtn(obj, name, index) {
        $api.text($api.byId('headerTitle'), name);
        var footer = $api.byId('footer');
        var footerAct = $api.dom(footer, '.aui-bar-tab-item.aui-active');
        $api.removeCls(footerAct, 'aui-active');
        $api.addCls(obj, 'aui-active');
        api.setFrameGroupIndex({
            name: 'footer_tab_frame',
            index: index,
            scroll: true
        });

    }
</script>
</html>
