package com.laowang.rabbitmq.eight;

import com.laowang.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

/**
 * @CreateTime 2022/5/17-17 21:32
 * @Author laowang
 * @Description
 */
public class Consumer02 {
    public static final String DEAD_EXCHANGE = "dead_exchange";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        String deadQueue = "dead_queue";
        channel.queueDeclare(deadQueue,false,false,false,null);
        channel.queueBind(deadQueue,DEAD_EXCHANGE,"lisi");
        System.out.println("等待接收死信队列消息");


        DeliverCallback deliverCallback = (consumerTag ,delivery) ->{
            String message = new String(deadQueue.getBytes(StandardCharsets.UTF_8));
            System.out.println("Consumer02接收死信队列的消息" + message);
        };
        channel.basicConsume(deadQueue,true,deliverCallback, consumerTag -> {});

    }
}
