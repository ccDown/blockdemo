package com.ccdown.blockdemo.p2p;

import com.alibaba.fastjson.JSON;
import com.ccdown.blockdemo.enums.MessageEnum;
import com.ccdown.blockdemo.enums.VoteEnum;
import com.ccdown.blockdemo.pojo.dto.BaseMessage;
import com.ccdown.blockdemo.pojo.dto.Message;
import com.ccdown.blockdemo.pojo.dto.PingMessage;
import com.ccdown.blockdemo.pojo.dto.PongMessage;
import com.ccdown.blockdemo.pojo.dto.VoteInfo;
import com.ccdown.blockdemo.utils.SimpleMerkleTree;

import org.apache.logging.log4j.util.Strings;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

/**
 * @author kuan
 * Created on 21/4/28.
 * @description
 */
@Component
@Slf4j
public class P2pPointServer {

    private int port = 7001;

    private List<WebSocket> localSockets = new ArrayList<>();

    public List<WebSocket> getLocalSockets() {
        return localSockets;
    }

    public void setLocalSockets(List<WebSocket> localSockets) {
        this.localSockets = localSockets;
    }

    /**
     * @return void
     * @Author kuan
     * @Date 10:24 21/4/28
     * @Description 初始化 P2P Serer
     * @Param [Server 端的端口号 port]
     **/
    @PostConstruct
    @Order(1)
    public void initServer() {
        /**
         * @Author kuan
         * @Date 10:24 21/4/28
         * @Description 初始化WebSocket的服务端定义内部类对象socketServer, 源于WebSocketSetver;
         * @Param []
         * @return void
         **/

        final WebSocketServer socketServer = new WebSocketServer(new InetSocketAddress(port)) {

            @Override
            public void onOpen(WebSocket conn, ClientHandshake handshake) {
                PingMessage pingMessage = new PingMessage(" 服务创建成功");
                sendMessage(conn, pingMessage);
                localSockets.add(conn);
            }

            @Override
            public void onClose(WebSocket conn, int code, String reason, boolean remote) {
                PingMessage pingMessage = new PingMessage(" 服务端关闭");
                sendMessage(conn, pingMessage);
                localSockets.remove(conn);
            }

            @Override
            public void onMessage(WebSocket conn, String content) {

                if (Strings.isBlank(content)) {
                    return;
                }

                BaseMessage message = JSON.parseObject(content, BaseMessage.class);
                log.info("服务端接收到客户端 {} 消息： {}", conn.getRemoteSocketAddress(), message);
                if (message.getMessageType() == MessageEnum.BLOCK.getCode()) {

                } else if (message.getMessageType() == MessageEnum.CONSENSUS.getCode()) {
                    dealWithConsensusMessage(conn, message);
                } else if (message.getMessageType() == MessageEnum.PING.getCode()) {
                    PongMessage pongMessage = new PongMessage("PONG");
                    sendMessage(conn, pongMessage);
                } else if (message.getMessageType() == MessageEnum.PONG.getCode()) {
                    return;
                }

            }

            @Override
            public void onError(WebSocket conn, Exception ex) {
                log.info(" {} 客户端链接错误！", conn.getRemoteSocketAddress());
                localSockets.remove(conn);
            }

            @Override
            public void onStart() {
                log.info(" 客户端启动！");
            }
        };

        socketServer.start();
        log.info("WebSocket Server启动 {}", port);
    }

    private void dealWithConsensusMessage(WebSocket conn, Message message) {
        VoteInfo voteInfo = JSON.parseObject(message.getMessage(), VoteInfo.class);

        if (!voteInfo.getHash().equals(SimpleMerkleTree.getRootHash(voteInfo.getList()))) {
            return;
        }

        if (voteInfo.getCode() == VoteEnum.PREPREPARE.getCode()) {
            voteInfo.setCode(VoteEnum.PREPARE.getCode());
            message.setMessage(JSON.toJSONBytes(voteInfo));
            sendMessage(conn, message);
        } else if (voteInfo.getCode() == VoteEnum.PREPARE.getCode()) {
            voteInfo.setCode(VoteEnum.COMMIT.getCode());
            message.setMessage(JSON.toJSONBytes(voteInfo));
            sendMessage(conn, message);
        } else if (voteInfo.getCode() == VoteEnum.COMMIT.getCode()) {
            //服务端不处理commit交易
        }
    }

    private boolean blockVerify(String message) {
        return Strings.isNotBlank(message) && "".equals(message);
    }


    public void sendMessage(WebSocket webSocket, Message message) {
        log.info("发送给 {} 的p2p消息： {}", webSocket.getRemoteSocketAddress().getPort(), JSON.toJSONString(message));
        sendMessage(webSocket, JSON.toJSONString(message));
    }

    /**
     * @return void
     * @Author kuan
     * @Date 10:30 21/4/28
     * @Description 向连接到本机的某客户端发送消息
     * @Param [webSocket, message]
     **/
    public void sendMessage(WebSocket webSocket, String message) {
        webSocket.send(message);
    }

    /**
     * @return void
     * @Author kuan
     * @Date 10:31 21/4/28
     * @Description 向所有连接到本机的客户端广播消息
     * @Param [message]
     **/
    public void broatcast(String message) {
        if (localSockets.size() == 0 || Strings.isBlank(message)) {
            return;
        }

        log.info("Glad to say broaccast to clients being startted!");

        for (WebSocket webSocket :
                localSockets) {
            this.sendMessage(webSocket, message);
        }
        log.info("Glad to say broatcast to clients has overred!");
    }

}
