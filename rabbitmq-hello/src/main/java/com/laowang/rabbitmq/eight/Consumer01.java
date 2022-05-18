package com.laowang.rabbitmq.eight;

import com.laowang.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;

/**
 * @CreateTime 2022/5/17-17 19:49
 * @Author laowang
 * @Description ���δ�����ؼ�����
 */
public class Consumer01 {

    //������ͨ������
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    //�������Ž�����
    public static final String DEAD_EXCHANGE = "dead_exchange";

    public static void main(String[] args) throws Exception{

        Channel channel = RabbitMqUtils.getChannel();

        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        //�������Ŷ��������Ž�������
        String deadQueue = "dead_queue";
        channel.queueDeclare(deadQueue,false,false,false,null);
        channel.queueBind(deadQueue,DEAD_EXCHANGE,"lisi");

        //���ò���
        HashMap<String, Object> params = new HashMap<>();
        //���������������Ž�����
        params.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        //�������Ž��������ĸ����Ŷ���֮���routingkey
        params.put("x-dead-letter-routing-key", "lisi");

        //������ͨ��������ͨ��������
        String normalQueue = "normal_queue";
        channel.queueDeclare(normalQueue,false,false,false,params);
        channel.queueBind(normalQueue,NORMAL_EXCHANGE,"zhangsan");

        System.out.println("�ȴ�������Ϣ");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "utf-8");
            System.out.println("Consumer01���յ���Ϣ"+message);
        };

        channel.basicConsume(normalQueue,true,deliverCallback,consumerTag -> {});

    }
}
