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
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kuan
 * Created on 21/4/28.
 * @description
 */
@Component
@Slf4j
public class P2pPointClient {

    // P2P网关中的节点即使客户端也是服务端，作为服务端运行在7001
    // 作为客户端通过ws://localhost:7001/连接到服务端
    private String wsUrl = "ws://localhost:7001/";

    // 所有客户端WebSocket的连接池缓存
    private List<WebSocket> localSockets = new ArrayList<>();


    public List<WebSocket> getLocalSockets() {
        return localSockets;
    }

    public void setLocalSockets(List<WebSocket> localSockets) {
        this.localSockets = localSockets;
    }

    @SneakyThrows
    @PostConstruct
    @Order(2)
    public void connectPeer() {

        try {

        final WebSocketClient socketClient = new WebSocketClient(new URI(wsUrl)) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                PingMessage pingMessage = new PingMessage(this.getLocalSocketAddress() + " 成功创建客户端");
                sendMessage(this, pingMessage);
                localSockets.add(this);
            }

            @Override
            public void onMessage(String content) {

                if (Strings.isBlank(content)) {
                    return;
                }

                BaseMessage message = JSON.parseObject(content, BaseMessage.class);
                log.info("{} 收到服务端发送的消息 {} ",this.getLocalSocketAddress(), message);
                if (message.getMessageType() == MessageEnum.BLOCK.getCode()) {

                } else if (message.getMessageType() == MessageEnum.CONSENSUS.getCode()) {
                    dealWithConsensusMessage(this, message);
                } else if (message.getMessageType() == MessageEnum.PING.getCode()){
                    PongMessage pongMessage = new PongMessage("PONG");
                    sendMessage(this,pongMessage);
                } else if (message.getMessageType() == MessageEnum.PONG.getCode()){
                    return;
                }
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                log.info("{} 客户端关闭",this.getLocalSocketAddress());
                localSockets.remove(this);
            }

            @Override
            public void onError(Exception ex) {
                log.info("{} 客户端报错", this.getLocalSocketAddress());
                localSockets.remove(this);
            }
        };

        //客户端开始连接服务端
        socketClient.connect();
        } catch (URISyntaxException e){
            log.info("连接失败 : {}",e.getMessage());
        }
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
            if (getConnecttedNodeCount() == getLeastNodeCount()){

            }
        }
    }

    //已经在连接的节点的个数
    private int getConnecttedNodeCount() {
        //本机测试时，写死为 。实际开发部署多个节点时，按实际情况返回
        return 1;
    }

    // PBFT 消息节点最少确认个数计算
    private int getLeastNodeCount() {
        //本机测试时，写死为 。实际开发部署多个节点时， PBFT 算法中拜占庭节点数
        //总节点数 3f+l
        return 1;
    }

    public void sendMessage(WebSocket webSocket, Message message){
        log.info("发送给 {} 的p2p消息： {}", webSocket.getRemoteSocketAddress().getPort(), JSON.toJSONString(message));
        sendMessage(webSocket, JSON.toJSONString(message));
    }

    private void sendMessage(WebSocket webSocket, String message) {
        webSocket.send(message);
    }

    public void broatcast(String message){
        if (localSockets.size() == 0 || Strings.isBlank(message)){
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
