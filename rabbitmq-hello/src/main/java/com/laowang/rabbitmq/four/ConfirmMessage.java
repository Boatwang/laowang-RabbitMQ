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
 * @Description ����ȷ��ģʽ���Ƚ����������ĸ�Ч����ߣ�
 *                  1.����ȷ��
 *                  2.����ȷ��
 *                  3.�첽����ȷ��
 */
public class ConfirmMessage {

    //��������Ϣ�ĸ���
    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {

        //1.����ȷ��
//        ConfirmMessage.publishMessageIndividually();//591����
        //2.����ȷ��
//        ConfirmMessage.publishMessageBatch();//70����
        //3.�첽����ȷ��
        ConfirmMessage.publishMessageAsync();//36����
    }

    /**
     *
     * @throws Exception
     * ����ȷ��
     */
    public static void publishMessageIndividually() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //��ȡһ�������������
        String queueName = UUID.randomUUID().toString();
        //��������
        channel.queueDeclare(queueName,true,false,false,null);
        //��������ȷ��
        channel.confirmSelect();
        //����ʱ��
        long begin = System.currentTimeMillis();
        //����1000����Ϣ
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("",queueName,null,message.getBytes());
            //������Ϣ�����Ϸ���ȷ��
            boolean flag = channel.waitForConfirms();
            if(flag){
                System.out.println("��Ϣ���ͳɹ�");
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("����" + MESSAGE_COUNT + "������ȷ����Ϣ�ķѵ�ʱ��Ϊ��" + (end - begin) + "����");

    }

    /**
     * ����ȷ����Ϣ
     */

    public static void publishMessageBatch() throws Exception{

        Channel channel = RabbitMqUtils.getChannel();
        //��ȡһ�������������
        String queueName = UUID.randomUUID().toString();
        //��������
        channel.queueDeclare(queueName,true,false,false,null);
        //��������ȷ��
        channel.confirmSelect();
        //����ʱ��
        long begin = System.currentTimeMillis();
        //����ȷ����Ϣ�Ĵ�С
        int batchSize = 100;
        //����������Ϣ����������ȷ��
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("",queueName,null,message.getBytes());
            //�ﵽ100��ʱ��ȷ��һ��
            if(i%batchSize == 0){
                channel.waitForConfirms();
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("����" + MESSAGE_COUNT + "������ȷ����Ϣ�ķѵ�ʱ��Ϊ��" + (end - begin) + "����");
    }

    public static void publishMessageAsync() throws Exception{

        Channel channel = RabbitMqUtils.getChannel();
        //��ȡһ�������������
        String queueName = UUID.randomUUID().toString();
        //��������
        channel.queueDeclare(queueName,true,false,false,null);
        //��������ȷ��
        channel.confirmSelect();

        /**
         * �߳������һ����ϣ�������ڸ߲������
         * 1.���Խ���ź���Ϣ���й���
         * 2.��������ɾ����Ŀ��ֻҪ�������к�
         * 3.֧�ָ߲��������̣߳�
         */

        ConcurrentSkipListMap<Long,String> outStandingConfirms = new ConcurrentSkipListMap<Long, String>();

        //����ʱ��
        long begin = System.currentTimeMillis();
        //׼����Ϣ��������������Щ��Ϣ�ɹ���ʧ��
        /**
         * ��Ϣ�ɹ��ص�����
         * ����1.��Ϣ�ı��
         * ����2.�Ƿ�����ȷ��
         */
        ConfirmCallback ackCallback = (long deliveryTag, boolean multiple) -> {
            //ɾ����ȷ�ϵ���Ϣ��ʣ�¾���δȷ�ϵ�����Ϣ
            if (multiple){
                ConcurrentNavigableMap<Long, String> confirmed = outStandingConfirms.headMap(deliveryTag);
                confirmed.clear();
            }else {
                outStandingConfirms.remove(deliveryTag);
            }
            System.out.println("ȷ�ϵ���Ϣ��" + deliveryTag);
        };

        /**
         * ��Ϣʧ�ܻص�����
         * ����1.��Ϣ�ı��
         * ����2.�Ƿ�����ȷ��
         */
        ConfirmCallback nackCallback = (long deliveryTag, boolean multiple) -> {
            String Message = outStandingConfirms.get(deliveryTag);

            System.out.println("δȷ�ϵ���Ϣ�ǣ�"+Message+"::::::::"+"δȷ����Ϣtag:" + deliveryTag);
        };

        channel.addConfirmListener(ackCallback,nackCallback);

        //����������Ϣ
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message  = i + "��Ϣ";
            channel.basicPublish("",queueName,null,message.getBytes());
            outStandingConfirms.put(channel.getNextPublishSeqNo(),message);
        }

        long end = System.currentTimeMillis();
        System.out.println("����" + MESSAGE_COUNT + "���첽ȷ����Ϣ�ķѵ�ʱ��Ϊ��" + (end - begin) + "����");

    }

}
