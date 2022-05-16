package com.laowang.rabbitmq.one;

import com.rabbitmq.client.*;

/**
 * @CreateTime 2022/5/14-14 21:49
 * @Author laowang
 * @Description �����ߣ�������Ϣ
 */
public class Consumer {

    public static final String QUEUE_NAME = "hello";


    public static void main(String[] args) throws Exception {
        //������Ϣ����
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("admin");
        factory.setPassword("123");
        factory.setHost("192.168.73.50");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //����������Ϣ
        DeliverCallback deliverCallback = (consumerTag,message)->{
            System.out.println(new String(message.getBody()));
        };

        //ȡ��������Ϣ�Ļص�
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("������Ϣ���ж�");
        };
        /**
         * ������������Ϣ
         * ����1�������ĸ�����
         * ����2�����ѳɹ����Ƿ�Ҫ�Զ�Ӧ��
         * ����3��������Ϊ�ɹ����ѵĻص�
         * ����4��������¼ȡ��Ϣ�Ļص�
         */
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);

    }
}
