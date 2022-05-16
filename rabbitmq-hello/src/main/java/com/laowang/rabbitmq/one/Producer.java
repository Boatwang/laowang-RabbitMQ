package com.laowang.rabbitmq.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

/**
 * @CreateTime 2022/5/14-14 17:56
 * @Author laowang
 * @Description
 */
public class Producer {

    //队列名称
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {

        //创建一个链接工厂
        ConnectionFactory Factory = new ConnectionFactory();
        //配置链接参数
        Factory.setHost("192.168.73.50");
        Factory.setUsername("admin");
        Factory.setPassword("123");
        //获取链接
        Connection connection = Factory.newConnection();
        //获取信道
        Channel channel = connection.createChannel();

        /**
         * 声明一个队列
         * 参数1：队列名称
         * 参数2：是否持久化
         * 参数3：是否消息共享
         * 参数4：是否自动删除
         * 参数5：其他参数
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        //发消息
        String message = "hello world";

        /**
         * 发送一个消息
         * 参数1：发送到哪个交换机
         * 参数2：路由器的key值是哪个，本次是队列名称
         * 参数3：其他参数
         * 参数4：发送消息的消息体
         */
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());

    }

}
