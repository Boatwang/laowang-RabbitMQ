package com.laowang.rabbitmq.eight;

import com.laowang.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;

/**
 * @CreateTime 2022/5/17-17 19:51
 * @Author laowang
 * @Description
 */
public class Producer {

    public static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();

        //������Ϣ�����ù���ʱ�� ��λ�Ǻ���
        AMQP.BasicProperties properties =
                new AMQP.BasicProperties()
                        .builder().expiration("10000").build();

        for (int i = 0; i < 10; i++) {
            String message = "" + i;
            channel.basicPublish(NORMAL_EXCHANGE,"zhangsan",properties,message.getBytes(StandardCharsets.UTF_8));
        }
    }
}
