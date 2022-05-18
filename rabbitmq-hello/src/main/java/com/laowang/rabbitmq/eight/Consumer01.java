package com.laowang.rabbitmq.eight;

import com.laowang.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;

/**
 * @CreateTime 2022/5/17-17 19:49
 * @Author laowang
 * @Description 本次代码最关键的是
 */
public class Consumer01 {

    //命名普通交换机
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    //命名死信交换机
    public static final String DEAD_EXCHANGE = "dead_exchange";

    public static void main(String[] args) throws Exception{

        Channel channel = RabbitMqUtils.getChannel();

        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        //声明死信队列与死信交换机绑定
        String deadQueue = "dead_queue";
        channel.queueDeclare(deadQueue,false,false,false,null);
        channel.queueBind(deadQueue,DEAD_EXCHANGE,"lisi");

        //设置参数
        HashMap<String, Object> params = new HashMap<>();
        //正常队列设置死信交换机
        params.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        //设置死信交换机与哪个死信队列之间的routingkey
        params.put("x-dead-letter-routing-key", "lisi");
        //设置普通队列的接收消息的长度

        //声明普通队列与普通交换机绑定
        String normalQueue = "normal_queue";
        channel.queueDeclare(normalQueue,false,false,false,params);
        channel.queueBind(normalQueue,NORMAL_EXCHANGE,"zhangsan");

        System.out.println("等待接收消息");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "utf-8");
            if (message.equals("info5")){
                System.out.println("Consumer01接收到的消息是"+message+":此消息是被Consumer01拒绝的");
                channel.basicReject(delivery.getEnvelope().getDeliveryTag(),false);
            }else {
                System.out.println("Consumer01接收到消息"+message);
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
            }
        };

        channel.basicConsume(normalQueue,false,deliverCallback,consumerTag -> {});

    }
}
