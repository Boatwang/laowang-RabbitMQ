package com.laowang.rabbitmq.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

/**
 * @CreateTime 2022/5/14-14 17:56
 * @Author laowang
 * @Description
 */
public class Producer {

    //��������
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {

        //����һ�����ӹ���
        ConnectionFactory Factory = new ConnectionFactory();
        //�������Ӳ���
        Factory.setHost("192.168.73.50");
        Factory.setUsername("admin");
        Factory.setPassword("123");
        //��ȡ����
        Connection connection = Factory.newConnection();
        //��ȡ�ŵ�
        Channel channel = connection.createChannel();

        /**
         * ����һ������
         * ����1����������
         * ����2���Ƿ�־û�
         * ����3���Ƿ���Ϣ����
         * ����4���Ƿ��Զ�ɾ��
         * ����5����������
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        //����Ϣ
        String message = "hello world";

        /**
         * ����һ����Ϣ
         * ����1�����͵��ĸ�������
         * ����2��·������keyֵ���ĸ��������Ƕ�������
         * ����3����������
         * ����4��������Ϣ����Ϣ��
         */
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());

    }

}
