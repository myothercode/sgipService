package com.sgip.domain.sgip.action;

import com.sgip.domain.sgip.part.Head;

import java.nio.ByteBuffer;
import static com.sgip.domain.VO.SPConfig.*;

public class UnBind {
	public Head head=null;
	public UnBind(){
		Head head1=new Head();
		head1.cmdId=SGIP_UNBIND;
		head1.msgLen = 20;
		this.head=head1;
	}
	public ByteBuffer getUnBindBuffer(){
		return this.head.getBuffer();
	}

}
