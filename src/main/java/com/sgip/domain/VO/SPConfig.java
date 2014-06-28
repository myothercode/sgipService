package com.sgip.domain.VO;

import java.nio.ByteOrder;

/**
 * Created with IntelliJ IDEA.
 * User: wula
 * Date: 13-10-19
 * Time: 下午8:55
 * To change this template use File | Settings | File Templates.
 */
public class SPConfig {
    public static final int jieDian = 0xB47CB398; //0xB47CB398/3028071320的十六进制 节点编号，企业编号是71320
    public static final String CorpId="71320";
    public static String loginName="10628997";
    public static String loginPassWord="10628997";
    /**
     * 字节排序;
     */
    public static final ByteOrder byteOrder =ByteOrder.BIG_ENDIAN;
    public static final String charset         = "GB2312";

    /*sgip cmd*/
    public static final int SGIP_BIND         =0x00000001;   //SP用该消息向SMG发出建立连接的请求
    public static final int SGIP_BIND_RESP    =0x80000001;   //SP对Bind命令的应答
    public static final int SGIP_UNBIND       =0x00000002;   //SP用该消息向SMG通知将要断开现有的连接
    public static final int SGIP_UNBIND_RESP  =0x80000002;   //SP对Unbind命令的应答
    public static final int SGIP_SUBMIT       =0x00000003;   //SP用该消息向SMG请求发送短消息
    public static final int SGIP_SUBMIT_RESP  =0x80000003;   //SMG对Submit命令的应答
    public static final int SGIP_DELIVER      =0x00000004;   //SMG发送一条短消息到SP
    public static final int SGIP_DELIVER_RESP =0x80000004;   //SP用该消息对从SMG接收到的Deliver命令作出应答
    public static final int SGIP_REPORT       =0x00000005;   //SMG用该命令通知SP一条Submit命令所发送的MT当前结果
    public static final int SGIP_REPORT_RESP  =0x80000005;   //SP对从SMG接收到的Report命令作出应答

}
