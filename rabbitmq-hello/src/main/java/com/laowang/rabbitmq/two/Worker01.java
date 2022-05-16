package com.laowang.rabbitmq.two;

import com.laowang.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

/**
 * @CreateTime 2022/5/14-14 23:38
 * @Author laowang
 * @Description
 */
public class Worker01 {
    //��������
    public static final String QUEUE_NAME = "hello";


    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();

        //������Ϣ
        DeliverCallback deliverCallback = (consumerTag,message) -> {
            System.out.println("���յ�����Ϣ"+new String(message.getBody()));
        };

        //��Ϣ���ձ�ȡ��ʱ����ִ����������
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println(consumerTag+"��Ϣ��ȡ�����ѽӿڻص��߼�");
        };


        /**
         * ������������Ϣ
         * ����1�������ĸ�����
         * ����2�����ѳɹ����Ƿ�Ҫ�Զ�Ӧ��
         * ����3��������Ϊ�ɹ����ѵĻص�
         * ����4��������¼ȡ��Ϣ�Ļص�
         */
        System.out.println("C1�ȴ�������Ϣ......");
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
