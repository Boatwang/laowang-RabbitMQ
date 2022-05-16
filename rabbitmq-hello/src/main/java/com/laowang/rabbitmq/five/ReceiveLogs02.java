package com.laowang.rabbitmq.five;

import com.laowang.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

/**
 * @CreateTime 2022/5/16-16 17:39
 * @Author laowang
 * @Description
 */
public class ReceiveLogs02 {

    //声明交换机的名称
    private static final String EXCHANGE_NAME = "logs";


    public static void main(String[] args) throws Exception{

        //获取一个信道
        Channel channel = RabbitMqUtils.getChannel();
        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        /**
         * 声明一个临时队列
         * 队列的名称是随机的
         * 当消费者断开与队列链接的时候，队列自动删除
         */
        String queue = channel.queueDeclare().getQueue();

        /**
         * 绑定交换机与队列
         * 参数1.队列名称
         * 参数2.交换机名称
         * 参数3.绑定码
         */
        channel.queueBind(queue,EXCHANGE_NAME,"");
        System.out.println("等待接收消息，把接收到的消息打印在屏幕上......");

        DeliverCallback deliverCallback = (String consumerTag, Delivery message) -> {
            System.out.println("ReceiveLogs02控制台打印接收到的消息："+ new String(message.getBody(),"utf-8"));
        };

        channel.basicConsume(queue,true,deliverCallback,consumerTag -> {});
    }

}
