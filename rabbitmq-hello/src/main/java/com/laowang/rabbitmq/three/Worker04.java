package com.laowang.rabbitmq.three;

import com.laowang.rabbitmq.utils.RabbitMqUtils;
import com.laowang.rabbitmq.utils.SleepUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @CreateTime 2022/5/15-15 19:49
 * @Author laowang
 * @Description
 */
public class Worker04 {

    public static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();

        System.out.println("C2�ȴ�������Ϣ����ʱ��ϳ�");

        //�Ƿ��Զ�Ӧ��
        boolean autoAsk = false;

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            //��˯30��
            SleepUtils.sleep(30);
            System.out.println("���յ�����Ϣ" + new String(message.getBody(), "utf-8"));
            /**
             * 1.��Ϣ�ı��
             * 2.�Ƿ�����Ӧ��
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };

        //���ò���ƽ�ַ���������Ϊ1ʱ��Ϊ����ƽ�ַ�
        int prefetchCount = 1;
        channel.basicQos(prefetchCount);

        CancelCallback cancelCallback = consumerTag -> {
            System.out.println(consumerTag + "���M��ȡ�����M�ӿڻ��{߉݋");
        };

        channel.basicConsume(TASK_QUEUE_NAME, autoAsk, deliverCallback, cancelCallback);
    }
}
