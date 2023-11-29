package com.zt.zeus.transfer.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

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
public class KafkaProducerConfiguration {
    @Value("${spring.kafka.bootstrap-servers:load.balance.com:9290}")
    private String servers;

    @Value("${spring.kafka.producer.retries:3}")
    private int retries;
    @Value("${spring.kafka.producer.batch-size:16384}")
    private int batchSize;
    @Value("${spring.kafka.producer.linger:1}")
    private int linger;
    @Value("${spring.kafka.producer.buffer-memory:33554432}")
    private int bufferMemory;

    @Resource
    private KafkaProperties kafkaProperties;

    // 创建生产者配置map，ProducerConfig中的可配置属性比spring boot自动配置要多
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        //设置重试次数
        props.put(ProducerConfig.RETRIES_CONFIG, retries);
        //达到batchSize大小的时候会发送消息
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        //延时时间，延时时间到达之后计算批量发送的大小没达到也发送消息
        props.put(ProducerConfig.LINGER_MS_CONFIG, linger);
        //缓冲区的值
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemory);
//		props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, "cn.ztuo.bitrade.kafka.kafkaPartitioner");
        //序列化手段
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

//        props.putAll(kafkaProperties.getSecurity().buildProperties());

//        //producer端的消息确认机制,-1和all都表示消息不仅要写入本地的leader中还要写入对应的副本中
//        props.put(ProducerConfig.ACKS_CONFIG, -1);
//        //单条消息的最大值以字节为单位,默认值为1048576
//        props.put(ProducerConfig.LINGER_MS_CONFIG, 10485760);
//        //设置broker响应时间，如果broker在60秒之内还是没有返回给producer确认消息，则认为发送失败
//        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 60000);
//        //指定拦截器(value为对应的class)
//        props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, "com.te.handler.KafkaProducerInterceptor");
//        //设置压缩算法(默认是木有压缩算法的)
//        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "LZ4");
        props.putAll(kafkaProperties.getSecurity().buildProperties());
        props.putAll(kafkaProperties.getProperties());

        return props;
    }

    /**
     * 不使用spring boot的KafkaAutoConfiguration默认方式创建的DefaultKafkaProducerFactory，重新定义
     * @return
     */
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }
    /**
     * 不使用spring boot的KafkaAutoConfiguration默认方式创建的KafkaTemplate，重新定义
     * @return
     */
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<String, String>(producerFactory());
    }

}
