package com.laowang.rabbitmq.four;

import com.laowang.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @CreateTime 2022/5/16-16 12:06
 * @Author laowang
 * @Description 发布确认模式，比较以下三种哪个效率最高：
 *                  1.单个确认
 *                  2.批量确认
 *                  3.异步批量确认
 */
public class ConfirmMessage {

    //批量发消息的个数
    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {

        //1.单个确认
//        ConfirmMessage.publishMessageIndividually();//591毫秒
        //2.批量确认
//        ConfirmMessage.publishMessageBatch();//70毫秒
        //3.异步批量确认
        ConfirmMessage.publishMessageAsync();//36毫秒
    }

    /**
     *
     * @throws Exception
     * 单个确认
     */
    public static void publishMessageIndividually() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //获取一个随机队列名称
        String queueName = UUID.randomUUID().toString();
        //声明队列
        channel.queueDeclare(queueName,true,false,false,null);
        //开启发布确认
        channel.confirmSelect();
        //开启时间
        long begin = System.currentTimeMillis();
        //发送1000条消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("",queueName,null,message.getBytes());
            //单个消息就马上发布确认
            boolean flag = channel.waitForConfirms();
            if(flag){
                System.out.println("消息发送成功");
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("发送" + MESSAGE_COUNT + "条单独确认消息耗费的时间为：" + (end - begin) + "毫秒");

    }

    /**
     * 批量确认消息
     */

    public static void publishMessageBatch() throws Exception{

        Channel channel = RabbitMqUtils.getChannel();
        //获取一个随机队列名称
        String queueName = UUID.randomUUID().toString();
        //声明队列
        channel.queueDeclare(queueName,true,false,false,null);
        //开启发布确认
        channel.confirmSelect();
        //开启时间
        long begin = System.currentTimeMillis();
        //批量确认消息的大小
        int batchSize = 100;
        //批量发布消息，批量发布确认
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("",queueName,null,message.getBytes());
            //达到100条时，确认一次
            if(i%batchSize == 0){
                channel.waitForConfirms();
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("发送" + MESSAGE_COUNT + "条批量确认消息耗费的时间为：" + (end - begin) + "毫秒");
    }

    public static void publishMessageAsync() throws Exception{

        Channel channel = RabbitMqUtils.getChannel();
        //获取一个随机队列名称
        String queueName = UUID.randomUUID().toString();
        //声明队列
        channel.queueDeclare(queueName,true,false,false,null);
        //开启发布确认
        channel.confirmSelect();

        /**
         * 线程有序的一个哈希表，适用于高并发情况
         * 1.可以将序号和消息进行关联
         * 2.可以批量删除条目，只要给到序列号
         * 3.支持高并发（多线程）
         */

        ConcurrentSkipListMap<Long,String> outStandingConfirms = new ConcurrentSkipListMap<Long, String>();

        //开启时间
        long begin = System.currentTimeMillis();
        //准备消息监听器，监听那些消息成功和失败
        /**
         * 消息成功回调函数
         * 参数1.消息的标记
         * 参数2.是否批量确认
         */
        ConfirmCallback ackCallback = (long deliveryTag, boolean multiple) -> {
            //删除已确认的消息，剩下就是未确认的是消息
            if (multiple){
                ConcurrentNavigableMap<Long, String> confirmed = outStandingConfirms.headMap(deliveryTag);
                confirmed.clear();
            }else {
                outStandingConfirms.remove(deliveryTag);
            }
            System.out.println("确认的消息：" + deliveryTag);
        };

        /**
         * 消息失败回调函数
         * 参数1.消息的标记
         * 参数2.是否批量确认
         */
        ConfirmCallback nackCallback = (long deliveryTag, boolean multiple) -> {
            String Message = outStandingConfirms.get(deliveryTag);

            System.out.println("未确认的消息是："+Message+"::::::::"+"未确认消息tag:" + deliveryTag);
        };

        channel.addConfirmListener(ackCallback,nackCallback);

        //批量发送消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message  = i + "消息";
            channel.basicPublish("",queueName,null,message.getBytes());
            outStandingConfirms.put(channel.getNextPublishSeqNo(),message);
        }

        long end = System.currentTimeMillis();
        System.out.println("发送" + MESSAGE_COUNT + "条异步确认消息耗费的时间为：" + (end - begin) + "毫秒");

    }

}
