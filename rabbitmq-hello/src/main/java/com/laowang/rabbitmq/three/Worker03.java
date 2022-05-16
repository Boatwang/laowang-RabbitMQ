package com.laowang.rabbitmq.three;

import com.laowang.rabbitmq.utils.RabbitMqUtils;
import com.laowang.rabbitmq.utils.SleepUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

/**
 * @CreateTime 2022/5/15-15 19:49
 * @Author laowang
 * @Description
 */
public class Worker03 {


    public static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception{

        Channel channel = RabbitMqUtils.getChannel();

        System.out.println("C1等待接收消息处理时间较短");

        //是否自动应答
        boolean autoAsk = false;

        DeliverCallback deliverCallback = (consumerTag,message) ->{
            //沉睡一秒
            SleepUtils.sleep(1);
            System.out.println("接收到的消息"+new String(message.getBody(),"utf-8"));

            /**
             * 1.消息的标记
             * 2.是否批量应答
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);

        };

        //设置不公平分发，当参数为1时，为不公平分发
        int prefetchCount = 1;
        channel.basicQos(prefetchCount);

        CancelCallback cancelCallback = consumerTag -> {
            System.out.println(consumerTag + "消費者取消消費接口回調邏輯");
        };

        channel.basicConsume(TASK_QUEUE_NAME,autoAsk,deliverCallback,cancelCallback);
    }
}
