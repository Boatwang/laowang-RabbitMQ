package com.laowang.rabbitmq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMqUtils {
    //�õ�һ�����ӵ� channel
    public static Channel getChannel() throws Exception {
        //����һ�����ӹ���
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.73.50");
        factory.setUsername("admin");
        factory.setPassword("123");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        return channel;
    }
}