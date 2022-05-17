package com.laowang.rabbitmq.seven;

import com.laowang.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * @CreateTime 2022/5/17-17 0:08
 * @Author laowang
 * @Description
 */
public class EmitLogTopic {
    //��������������
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] argv) throws Exception {
            //��ȡһ���ŵ�
            Channel channel = RabbitMqUtils.getChannel();

            //���������������ƺ�����
            channel.exchangeDeclare(EXCHANGE_NAME, "topic");
            /**
             * Q1-->�󶨵���
             * �м�� orange �� 3 �����ʵ��ַ���(*.orange.*)
             * Q2-->�󶨵���
             * ���һ�������� rabbit �� 3 ������(*.*.rabbit)
             * ��һ�������� lazy �Ķ������(lazy.#)
             *
             */
            Map<String, String> bindingKeyMap = new HashMap<>();
            bindingKeyMap.put("quick.orange.rabbit","������ Q1Q2 ���յ�");
            bindingKeyMap.put("lazy.orange.elephant","������ Q1Q2 ���յ�");
            bindingKeyMap.put("quick.orange.fox","������ Q1 ���յ�");
            bindingKeyMap.put("lazy.brown.fox","������ Q2 ���յ�");
            bindingKeyMap.put("lazy.pink.rabbit","��Ȼ���������󶨵�ֻ������ Q2 ����һ��");
            bindingKeyMap.put("quick.brown.fox","��ƥ���κΰ󶨲��ᱻ�κζ��н��յ��ᱻ����");
            bindingKeyMap.put("quick.orange.male.rabbit","���ĸ����ʲ�ƥ���κΰ󶨻ᱻ����");
            bindingKeyMap.put("lazy.orange.male.rabbit","���ĸ����ʵ�ƥ�� Q2");

            for (Map.Entry<String, String> bindingKeyEntry: bindingKeyMap.entrySet()){

                //��ȡ����
                String bindingKey = bindingKeyEntry.getKey();
                //�����͵���Ϣ
                String message = bindingKeyEntry.getValue();
                //������Ϣ
                channel.basicPublish(EXCHANGE_NAME,bindingKey, null, message.getBytes("UTF-8"));

                System.out.println("�����߷�����Ϣ" + message);
            }

    }
}
