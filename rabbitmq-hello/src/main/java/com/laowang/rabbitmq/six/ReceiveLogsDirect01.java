package com.laowang.rabbitmq.six;

import com.laowang.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

/**
 * @CreateTime 2022/5/16-16 20:24
 * @Author laowang
 * @Description
 */
public class ReceiveLogsDirect01 {

    public static final String EXHANGE_NAME = "direct_logs";

    public static final String QUEUE_NAME = "console";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();

        channel.exchangeDeclare(EXHANGE_NAME, BuiltinExchangeType.DIRECT);

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        channel.queueBind(QUEUE_NAME, EXHANGE_NAME, "warning");
        channel.queueBind(QUEUE_NAME, EXHANGE_NAME, "info");

        DeliverCallback deliverCallback = (String consumerTag, Delivery message) -> {
            System.out.println("ReceiveLogs01控制台打印接收到的消息：" + new String(message.getBody(), "utf-8"));
        };


        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
        });

    }

}
