package com.laowang.rabbitmq.seven;

import com.laowang.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

public class ReceiveLogsTopic01 {
    //��������������
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] argv) throws Exception {
        //��ȡ�ŵ�
        Channel channel = RabbitMqUtils.getChannel();
        //�����ŵ�
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        //���� Q1 ������󶨹�ϵ
        String queueName="Q1";
        //��������
        channel.queueDeclare(queueName, false, false, false, null);
        //�󶨶��кͽ�����
        channel.queueBind(queueName, EXCHANGE_NAME, "*.orange.*");
        System.out.println("�ȴ�������Ϣ.....");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" ���ն��� :"+queueName+" �� ����:"+delivery.getEnvelope().getRoutingKey()+",��Ϣ:"+message);
        };

        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }
}
