package com.laowang.rabbitmq.one;

import com.rabbitmq.client.*;

/**
 * @CreateTime 2022/5/14-14 21:49
 * @Author laowang
 * @Description 消费者，接收消息
 */
public class Consumer {

    public static final String QUEUE_NAME = "hello";


    public static void main(String[] args) throws Exception {
        //创建消息工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("admin");
        factory.setPassword("123");
        factory.setHost("192.168.73.50");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //声明接收消息
        DeliverCallback deliverCallback = (consumerTag,message)->{
            System.out.println(new String(message.getBody()));
        };

        //取消接收消息的回调
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("消费消息被中断");
        };
        /**
         * 消费者消费消息
         * 参数1：消费哪个队列
         * 参数2：消费成功后是否要自动应答
         * 参数3：消费者为成功消费的回调
         * 参数4：消费者录取消息的回调
         */
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);

    }
}
