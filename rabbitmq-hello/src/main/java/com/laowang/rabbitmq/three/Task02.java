package com.laowang.rabbitmq.three;

import com.laowang.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @CreateTime 2022/5/15-15 19:10
 * @Author laowang
 * @Description ��Ϣ�ֶ�Ӧ�𲻶�ʧ���Żض��У���������ߴ���
 */
public class Task02 {


    public static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();

        //�Ƿ�־û�
        boolean durable = true;

        channel.queueDeclare(TASK_QUEUE_NAME,durable,false,false,null);

        channel.confirmSelect();

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()){
            String message = scanner.next();

            //���������߷�����ϢΪ�־û���Ϣ -> ����3 ��Ϊ��MessageProperties.PERSISTENT_TEXT_PLAIN
            channel.basicPublish("",TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes(StandardCharsets.UTF_8));

            System.out.println("�����߷�����Ϣ:"+message);
        }
    }

}
