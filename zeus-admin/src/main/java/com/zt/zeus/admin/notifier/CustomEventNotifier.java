package com.zt.zeus.admin.notifier;

import com.alibaba.fastjson.JSONObject;
import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.domain.events.InstanceStatusChangedEvent;
import de.codecentric.boot.admin.server.notify.AbstractEventNotifier;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2021/8/30 17:05
 * description:
 */
@Component
@Slf4j
public class CustomEventNotifier extends AbstractEventNotifier {

    private static final String template = "服务名:%s(%s) \n状态:%s(%s) \n服务ip:%s";

    @Value("${spring.admin.ding-talk-token}")
    private String dingTalkToken;


    protected CustomEventNotifier(InstanceRepository repository) throws Exception {
        super(repository);

    }

    @Override
    protected Mono<Void> doNotify(InstanceEvent event, Instance instance) {
        return Mono.fromRunnable(() -> {
            if (event instanceof InstanceStatusChangedEvent) {
                log.info("Instance {} ({}) is {}", instance.getRegistration().getName(), event.getInstance(),
                        ((InstanceStatusChangedEvent) event).getStatusInfo().getStatus());
                String status = ((InstanceStatusChangedEvent) event).getStatusInfo().getStatus();
                String messageText = null;
                switch (status) {
                    // 健康检查没通过
                    case "DOWN":
                        log.info("发送 健康检查没通过 的通知！");
                        messageText = String.format(template, instance.getRegistration().getName(), event.getInstance(), ((InstanceStatusChangedEvent) event).getStatusInfo().getStatus(), "健康检查没通过", instance.getRegistration().getServiceUrl());
                        this.sendMessage(messageText);
                        break;
                    // 服务离线
                    case "OFFLINE":
                        log.info("发送 服务离线 的通知！");
                        messageText = String.format(template, instance.getRegistration().getName(), event.getInstance(), ((InstanceStatusChangedEvent) event).getStatusInfo().getStatus(), "服务离线", instance.getRegistration().getServiceUrl());
                        this.sendMessage(messageText);
                        break;
                    //服务上线
                    case "UP":
                        log.info("发送 服务上线 的通知！");
                        messageText = String.format(template, instance.getRegistration().getName(), event.getInstance(), ((InstanceStatusChangedEvent) event).getStatusInfo().getStatus(), "服务上线", instance.getRegistration().getServiceUrl());
                        this.sendMessage(messageText);
                        break;
                    // 服务未知异常
                    case "UNKNOWN":
                        log.info("发送 服务未知异常 的通知！");
                        messageText = String.format(template, instance.getRegistration().getName(), event.getInstance(), ((InstanceStatusChangedEvent) event).getStatusInfo().getStatus(), "服务未知异常", instance.getRegistration().getServiceUrl());
                        this.sendMessage(messageText);
                        break;
                    default:
                        break;
                }
            } else {
                log.info("Instance {} ({}) {}", instance.getRegistration().getName(), event.getInstance(), event.getType());
            }
        });
    }

    /**
     * 发送消息
     *
     * @param messageText
     */
    private void sendMessage(String messageText) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost("https://oapi.dingtalk.com/robot/send?access_token=" + dingTalkToken);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("content", messageText);
            JSONObject params = new JSONObject();
            params.put("msgtype", "text");
            params.put("text", jsonObject);
            httpPost.setEntity(new StringEntity(params.toJSONString(), ContentType.APPLICATION_JSON));
            String responseBody = httpClient.execute(httpPost, httpResponse -> {
                int status = httpResponse.getStatusLine().getStatusCode();
                if (status < 200 || status >= 300) {
                    return "接口状态错误";
                }
                HttpEntity entity = httpResponse.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            });
            log.info(responseBody);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
