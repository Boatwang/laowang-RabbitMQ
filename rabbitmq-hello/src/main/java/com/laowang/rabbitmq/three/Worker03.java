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

        System.out.println("C1�ȴ�������Ϣ����ʱ��϶�");

        //�Ƿ��Զ�Ӧ��
        boolean autoAsk = false;

        DeliverCallback deliverCallback = (consumerTag,message) ->{
            //��˯һ��
            SleepUtils.sleep(1);
            System.out.println("���յ�����Ϣ"+new String(message.getBody(),"utf-8"));

            /**
             * 1.��Ϣ�ı��
             * 2.�Ƿ�����Ӧ��
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);

        };

        //���ò���ƽ�ַ���������Ϊ1ʱ��Ϊ����ƽ�ַ�
        int prefetchCount = 1;
        channel.basicQos(prefetchCount);

        CancelCallback cancelCallback = consumerTag -> {
            System.out.println(consumerTag + "���M��ȡ�����M�ӿڻ��{߉݋");
        };

        channel.basicConsume(TASK_QUEUE_NAME,autoAsk,deliverCallback,cancelCallback);
    }
}
