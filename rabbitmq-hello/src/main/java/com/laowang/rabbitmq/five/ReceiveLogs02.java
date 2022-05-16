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

    //����������������
    private static final String EXCHANGE_NAME = "logs";


    public static void main(String[] args) throws Exception{

        //��ȡһ���ŵ�
        Channel channel = RabbitMqUtils.getChannel();
        //����һ��������
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        /**
         * ����һ����ʱ����
         * ���е������������
         * �������߶Ͽ���������ӵ�ʱ�򣬶����Զ�ɾ��
         */
        String queue = channel.queueDeclare().getQueue();

        /**
         * �󶨽����������
         * ����1.��������
         * ����2.����������
         * ����3.����
         */
        channel.queueBind(queue,EXCHANGE_NAME,"");
        System.out.println("�ȴ�������Ϣ���ѽ��յ�����Ϣ��ӡ����Ļ��......");

        DeliverCallback deliverCallback = (String consumerTag, Delivery message) -> {
            System.out.println("ReceiveLogs02����̨��ӡ���յ�����Ϣ��"+ new String(message.getBody(),"utf-8"));
        };

        channel.basicConsume(queue,true,deliverCallback,consumerTag -> {});
    }

}
