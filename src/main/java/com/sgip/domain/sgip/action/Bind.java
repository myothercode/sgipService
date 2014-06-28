package com.sgip.domain.sgip.action;

import com.sgip.domain.sgip.part.Head;

import java.nio.ByteBuffer;
import static com.sgip.domain.VO.SPConfig.*;

public class Bind {
	/*1B ��¼���͡�
	  1��SP��SMG���������ӣ����ڷ�������
	  2��SMG��SP���������ӣ����ڷ�������
	  3��SMG֮�佨�������ӣ�����ת������
	  4��SMG��GNS���������ӣ�����·�ɱ�ļ�����ά��
	  5��GNS��SMG���������ӣ�����·�ɱ�ĸ���
	  6������GNS֮�佨�������ӣ���������·�ɱ��һ����
	  11��SP��SMG�Լ�SMG֮�佨���Ĳ������ӣ����ڸ��ٲ�������������*/
	public byte loginType=1;
	public String bLoginName=loginName;
	public String bLoginPassWord=loginPassWord;
	public String Reserve;
	public Head head= null;
	
	public static int STRUCT_SIZE=41;
	
	public Bind(){
		Head head1=new Head();
		head1.cmdId=SGIP_BIND;
		head1.msgLen=head1.STRUCT_SIZE+this.STRUCT_SIZE;
		this.head=head1;
	}
	
	public ByteBuffer getBuffer(){
		ByteBuffer buffer=ByteBuffer.allocate(STRUCT_SIZE);
		buffer.order(byteOrder);
		buffer.put(loginType);
		buffer.put(bLoginName.getBytes());
		for(int i=bLoginName.getBytes().length;i<16;i++){
			buffer.put((byte)0);
		}
		buffer.put(bLoginPassWord.getBytes());
		for(int i=bLoginPassWord.getBytes().length;i<16;i++){
			buffer.put((byte)0);
		}
		for(int i=0;i<8;i++){
			buffer.put((byte)0);
		}
		buffer.flip();
		return buffer;
	}
	
	public ByteBuffer getAllBindBuffer(){
		//ByteBuffer buffer = ByteBuffer.allocate(this.head.getBuffer().capacity()+this.getBuffer().capacity());
		ByteBuffer buffer = ByteBuffer.allocate(this.head.msgLen);
		buffer.order(byteOrder);
		buffer.put(this.head.getBuffer());
		buffer.put(this.getBuffer());
		buffer.flip();
		return buffer;
	}

}
