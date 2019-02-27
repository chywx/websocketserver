package com.xhedu.server;

import com.xhedu.entity.SocketEntity;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/websocket/phone/{username}/{source}")
@Component
public class WebSocketServer {

    private static final Logger LOG = LoggerFactory.getLogger(WebSocketServer.class);
    
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static ConcurrentHashMap<SocketEntity,WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
//    private static Map<String,WebSocketServer> webSocketMap = new HashMap<>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    private String username;

    private String source;
    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session,@PathParam("username") String username,@PathParam("source") String source) {
        this.session = session;
        this.username = username;
        this.source = source;
        SocketEntity entity = new SocketEntity(username);
        entity.setSource(source);
        webSocketMap.put(entity,this); //加入set中
        try {
            LOG.debug("连接成功");
            sendMessage("连接成功");
        } catch (IOException e) {
            LOG.debug("websocket IO异常");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() throws IOException {
        Set<SocketEntity> entities = webSocketMap.keySet();
        for(SocketEntity entity : entities){
            if(entity.getUsername().equals(username) && entity.getSource().equals(source)){
                webSocketMap.remove(entity);
            }
        }

//        webSocketMap.remove(username); //从set中删除
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) throws IOException{
        LOG.debug("收到来自窗口"+username+"的信息:"+message);
        JSONObject jsonObject = JSONObject.fromObject(message);
        Object object = jsonObject.get("username");
        LOG.debug("values:"+webSocketMap.values());
//        for (WebSocketServer item : webSocketMap.values()) {
//            if(object.equals(item.username))
//                item.session.getBasicRemote().sendText(jsonObject.get("text").toString());
//        }
        for(SocketEntity entity : webSocketMap.keySet()){
            if(object.equals(entity.getUsername())){
                webSocketMap.get(entity).session.getBasicRemote().sendText(jsonObject.get("text").toString());
            }
        }
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        LOG.debug("发生错误");
        error.printStackTrace();
    }
    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }
}
