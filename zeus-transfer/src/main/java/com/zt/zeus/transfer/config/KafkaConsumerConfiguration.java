package com.zt.zeus.transfer.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2022/12/15
 * description:
 */
@Configuration
@EnableKafka
public class KafkaConsumerConfiguration {

    @Value("${spring.kafka.bootstrap-servers:load.balance.com:9290}")
    private String servers;
    @Value("${spring.kafka.consumer.enable-auto-commit:true}")
    private boolean enableAutoCommit;
    @Value("${spring.kafka.consumer.auto-commit-interval:100}")
    private String autoCommitInterval;
    @Value("${spring.kafka.consumer.group-id:test}")
    private String groupId;
    @Value("${spring.kafka.consumer.auto-offset-reset:earliest}")
    private String autoOffsetReset;
    @Value("${spring.kafka.consumer.session.timeout.ms:120000}")
    private String sessionTimeout;
    @Value("${spring.kafka.consumer.request.timeout.ms:120000}")
    private String requestTimeout;
    @Value("${spring.kafka.consumer.concurrency:5}")
    private int concurrency;

    @Resource
    private KafkaProperties kafkaProperties;
    //构造消费者属性map，ConsumerConfig中的可配置属性比spring boot自动配置要多
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> propsMap = new HashMap<>();
        //kafka集群信息
        propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        //是否自动提交偏移量，默认值是true,为了避免出现重复数据和数据丢失，可以把它设置为false,然后手动提交偏移量
        propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
        //自动提交的时间间隔 在spring boot 2.X 版本中这里采用的是值的类型为Duration 需要符合特定的格式，如1S,1M,2H,5D
        propsMap.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, autoCommitInterval);
        propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        //注意该值不要改得太大，如果Poll太多数据，而不能在下次Poll之前消费完，则会触发一次负载均衡，产生卡顿。
        propsMap.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 5);
        //# 消费者组
        propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        //earliest ：在偏移量无效的情况下，消费者将从起始位置读取分区的记录  当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，从头开始消费
        propsMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        // #如果在这个时间内没有收到心跳，该消费者会被踢出组并触发{组再平衡 rebalance}
        //两次Poll之间的最大允许间隔。
        //消费者超过该值没有返回心跳，服务端判断消费者处于非存活状态，服务端将消费者从Consumer Group移除并触发Rebalance，默认30s。 单位毫秒
        propsMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeout);
        propsMap.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, requestTimeout);

        propsMap.putAll(kafkaProperties.getSecurity().buildProperties());
        propsMap.putAll(kafkaProperties.getProperties());
        return propsMap;
    }
    /**
     * 不使用spring boot默认方式创建的DefaultKafkaConsumerFactory，重新定义创建方式
     * @return
     */
    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    /**
     * 它具有并发属性。例如， container.setConcurrency(3) 创建了三个KafkaMessageListenerContainer实例。     *
     * 如果您提供了六个TopicPartition实例并且并发数为 3；每个容器有两个分区。对于五个 TopicPartition 实例，
     * 两个容器获得两个分区，第三个获得一个分区。如果并发数大于 TopicPartitions 的数量，则向下调整并发性，使每个容器获得一个分区。
     * 配置批处理侦听器
     *
     * 从版本1.1开始，可以将@KafkaListener方法配置为接收从消费者调查接收的整批消费者记录。配置监听器容器工厂创建一批听众，
     * 设置的的batchListener属性ConcurrentKafkaListenerContainerFactory来true。
     *
     * 我们可以选择BatchErrorHandler使用ConcurrentKafkaListenerContainerFactory#getContainerProperties().setBatchErrorHandler()
     * 并提供批处理错误处理程序来创建一个。
     *
     * 我们可以通过将Spring Kafka设置为ConsumerConfig.MAX_POLL_RECORDS_CONFIG适合您的值来配置Spring Kafka来设置批量大小的上限。默认情况下，
     * 动态计算每批中接收的记录数。在以下示例中，我们将上限配置为5。
     *
     *
     *
     */
//    @Bean
//    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory());
//        factory.setConcurrency(concurrency);
//        //设置批量消费
//        factory.setBatchListener(true);
//        factory.setMissingTopicsFatal(false);
//        factory.getContainerProperties().setPollTimeout(1500);
//        factory.setBatchListener(true);
//        return factory;
//    }

//    @Bean
//    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory(){
//        ConcurrentKafkaListenerContainerFactory<String,String> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory());
//        factory.setConcurrency(10);
//        factory.getContainerProperties().setPollTimeout(1500);
//        factory.setBatchListener(true);//设置为批量消费，每个批次数量在Kafka配置参数中设置
//        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);//设置手动提交ackMode
//        return factory;
//
//    }
}
