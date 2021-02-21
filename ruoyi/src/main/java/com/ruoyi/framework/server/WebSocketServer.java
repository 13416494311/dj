package com.ruoyi.framework.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author:万登枫
 * @date: 2020/11/13
 */
@Slf4j
@Component
@ServerEndpoint(value = "/webSocket/{groupId}/{userId}")
public class WebSocketServer {

    /*
     * 持有每个webSocket对象，以key-value存储到线程安全ConcurrentHashMap，
     */
    private static ConcurrentHashMap<String, ConcurrentHashMap<String, WebSocketServer>> concurrentHashMap = new ConcurrentHashMap<>(12);

    /**
     * 会话对象
     **/
    private Session session;


    /*
     * 客户端创建连接时触发
     * */
    @OnOpen
    public void onOpen(Session session, @PathParam("groupId") String groupId, @PathParam("userId") String userId) throws Exception {
        //每新建立一个连接，就把当前客户id为key,this为value存储到map中
        this.session = session;
        ConcurrentHashMap<String, WebSocketServer> groupHashMap = concurrentHashMap.get(groupId);
        if(groupHashMap==null){
            groupHashMap = new ConcurrentHashMap<>(12);
        }
        groupHashMap.put(userId, this);
        concurrentHashMap.put(groupId, groupHashMap);
        log.info("Open a websocket. groupId={}  userId={}",groupId, userId);
    }

    /**
     * 客户端连接关闭时触发
     **/
    @OnClose
    public void onClose(Session session, @PathParam("groupId") String groupId, @PathParam("userId") String userId) throws Exception {
        //客户端连接关闭时，移除map中存储的键值对
        ConcurrentHashMap<String, WebSocketServer> groupHashMap = concurrentHashMap.get(groupId);
        groupHashMap.remove(userId);
        log.info("close a websocket, concurrentHashMap remove sessionId={}  {}" ,groupId, userId);
    }

    /**
     * 接收到客户端消息时触发
     */
    @OnMessage
    public void onMessage(String message, @PathParam("id") String id) {
        log.info("receive a message from client id={},msg={}", id, message);
    }

    /**
     * 连接发生异常时候触发
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("Error while websocket. ", error);
    }

    /**
     * 发送消息到指定客户端
     * @param groupId
     * @param userId
     * @param message
     * @throws Exception
     */
    public void sendMessageToUser(String groupId,String userId, String message) throws Exception {
        //根据id,从map中获取存储的webSocket对象
        ConcurrentHashMap<String, WebSocketServer> groupHashMap = concurrentHashMap.get(groupId);
        WebSocketServer webSocketProcess = groupHashMap.get(userId);
        if (!ObjectUtils.isEmpty(webSocketProcess)) {
            //当客户端是Open状态时，才能发送消息
            if (webSocketProcess.session.isOpen()) {
                webSocketProcess.session.getBasicRemote().sendText(message);
            } else {
                log.error("websocket session={} {} is closed ",groupId, userId);
            }
        } else {
            log.error("websocket session={} {} is not exit ", groupId, userId);
        }
    }

    /**
     * 发送消息到所有客户端
     *
     * */
    public void sendMessageToGroup(String groupId,String msg) throws Exception {
        log.info("online client count={}", concurrentHashMap.get(groupId).size());
        Set<Map.Entry<String, WebSocketServer>> entries = concurrentHashMap.get(groupId).entrySet();
        for (Map.Entry<String, WebSocketServer> entry : entries) {
            String cid = entry.getKey();
            WebSocketServer webSocketProcess = entry.getValue();
            boolean sessionOpen = webSocketProcess.session.isOpen();
            if (sessionOpen) {
                webSocketProcess.session.getBasicRemote().sendText(msg);
            } else {
                log.info("cid={} is closed,ignore send text", cid);
            }
        }
    }

    public ConcurrentHashMap<String, WebSocketServer> getWebSocketuGroup(String groupId) throws Exception {
        return  concurrentHashMap.get(groupId);
    }

    public WebSocketServer getWebSocketuUser(String groupId,String userId) throws Exception {
        ConcurrentHashMap<String, WebSocketServer> groupHashMap = concurrentHashMap.get(groupId);
        if(groupHashMap!=null){
            return  groupHashMap.get(userId);
        }else{
            return  null;
        }

    }
}

