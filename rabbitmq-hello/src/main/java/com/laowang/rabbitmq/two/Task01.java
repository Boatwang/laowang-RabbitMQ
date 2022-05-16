package com.laowang.rabbitmq.two;

import com.laowang.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

/**
 * @CreateTime 2022/5/14-14 23:59
 * @Author laowang
 * @Description �����ߣ����ʹ�����Ϣ
 */
public class Task01 {

    //��������
    public static final String QUEUE_NAME = "hello";

    //���ʹ�����Ϣ
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();


        /**
         * ����һ������
         * ����1����������
         * ����2���Ƿ�־û�
         * ����3���Ƿ���Ϣ����
         * ����4���Ƿ��Զ�ɾ��
         * ����5����������
         */
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //�ӿ���̨�����������Ϣ
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            System.out.println("������Ϣ���"+message);

        }

    }


}
