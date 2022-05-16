package com.laowang.rabbitmq.two;

import com.laowang.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

/**
 * @CreateTime 2022/5/14-14 23:38
 * @Author laowang
 * @Description
 */
public class Worker01 {
    //队列名称
    public static final String QUEUE_NAME = "hello";


    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();

        //接收消息
        DeliverCallback deliverCallback = (consumerTag,message) -> {
            System.out.println("接收到的消息"+new String(message.getBody()));
        };

        //消息接收被取消时，会执行以下内容
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println(consumerTag+"消息者取消消费接口回调逻辑");
        };


        /**
         * 消费者消费消息
         * 参数1：消费哪个队列
         * 参数2：消费成功后是否要自动应答
         * 参数3：消费者为成功消费的回调
         * 参数4：消费者录取消息的回调
         */
        System.out.println("C1等待接收消息......");
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
