package com.sgip.domain.sgip.part;

import java.nio.ByteBuffer;
import java.util.Calendar;
import static com.sgip.domain.VO.SPConfig.*;


public class Head {
	public int msgLen;  //4B ��Ϣ���ܳ��ȣ��ֽڣ�
	public int cmdId;   //4B ����ID��ʾ�� ��SGIPCmd�ж���Ķ���
		
	//��Ϣͷ��seqnum���֣���Ϊ3����
	public static int JD = jieDian;  //4B ����Ľڵ��
	public int createTime;           //4B
	public int Seq_Number;              // 4B���к�
	public static int STRUCT_SIZE = 20;  //��Ϣͷ���Ϊ20B����
	
	//����һ��head
	public Head(){
		setTime();
		setMsecond();
	}
	
	
	public ByteBuffer getBuffer(){
		ByteBuffer buffer = ByteBuffer.allocate(STRUCT_SIZE);
		buffer.order(byteOrder);
		buffer.putInt(msgLen);
		buffer.putInt(cmdId);
		buffer.putInt(JD);
		buffer.putInt(createTime);
		buffer.putInt(Seq_Number);
		buffer.flip();
		return buffer;
	}
	
	/**
	 * ��������ʱ�����ַ���
	 */
	private void setTime(){
		Calendar now = Calendar.getInstance();
		createTime = now.get(Calendar.MONTH);
		createTime = createTime*100+now.get(Calendar.DAY_OF_MONTH);
		createTime = createTime*100+now.get(Calendar.HOUR_OF_DAY);
		createTime = createTime*100+now.get(Calendar.MINUTE);
		createTime = createTime*100+now.get(Calendar.SECOND);		
	}
	/**
	 * �õ�����
	 */
	private void setMsecond(){
		Calendar now = Calendar.getInstance();
		Seq_Number = now.get(Calendar.MILLISECOND);
	}

}
