package com.laowang.rabbitmq.two;

import com.laowang.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

/**
 * @CreateTime 2022/5/14-14 23:59
 * @Author laowang
 * @Description 生产者，发送大量消息
 */
public class Task01 {

    //队列名称
    public static final String QUEUE_NAME = "hello";

    //发送大量消息
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();


        /**
         * 声明一个队列
         * 参数1：队列名称
         * 参数2：是否持久化
         * 参数3：是否消息共享
         * 参数4：是否自动删除
         * 参数5：其他参数
         */
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //从控制台接收输入的消息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            System.out.println("发送消息完成"+message);

        }

    }


}
