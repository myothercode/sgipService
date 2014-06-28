package com.main.task;

import com.main.MainClass;
import com.sgip.comm.service.impl.GetFromDB;
import com.sgip.domain.QueueAndPools;
import com.sgip.domain.VO.BuesinessVO;
import com.sgip.domain.VO.SMSBody;
import com.sgip.domain.sgip.action.Bind;
import com.sgip.domain.sgip.action.Submit;
import com.sgip.domain.sgip.action.UnBind;
import com.sgip.domain.sgip.part.BuesinessClass;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

import static com.sgip.domain.VO.SPConfig.SGIP_UNBIND_RESP;
import static com.sgip.domain.VO.SPConfig.byteOrder;

/**
 * Created with IntelliJ IDEA.
 * User: Chace.Cai
 * Date: 13-10-18
 * Time: 下午4:55
 * To change this template use File | Settings | File Templates.
 */
/*@Service("submitSMS")
@Scope("prototype")*/
public class SubmitSMS implements Runnable{
    private static BuesinessClass buesiness=new BuesinessClass();
    //private static Resource resource = new ClassPathResource("conf/sgipConf.properties");
    private DataOutputStream Dos;
    private DataInputStream Dis;
    private Socket mySocket;
    private String threadName="";

    private GetFromDB getFromDB;

    public SubmitSMS(String threadName){
       this.threadName=threadName;
    }

    public void conectserver() throws Exception{
        try{
            //Properties props = PropertiesLoaderUtils.loadProperties(resource);
            if(mySocket==null||mySocket.isClosed()){
                System.out.println("连接到服务器...............");
                mySocket = new Socket("10.143.4.71",8801);
                //mySocket = new Socket("127.0.0.1",8801);
            }
            Dis=new DataInputStream(mySocket.getInputStream());
            Dos=new DataOutputStream(mySocket.getOutputStream());
            sendBind();//绑定
        }catch(Exception e){e.printStackTrace();}
    }

    @Override
    public void run() {
        Thread.currentThread().setName(this.threadName);
        try {
            /*Thread.sleep(1);
            System.out.println("send sms........");*/
            //QueueAndPools.smsQueue.take();
            SMSBody smsBody=null;
            int m=0;
            while (true){
               // System.out.println("send sms ...");
                try {smsBody = QueueAndPools.smsQueue.take();} catch (InterruptedException e) { e.printStackTrace();}
                try {
                    conectserver();
                    sendSumbit(smsBody);//提交短信
                    m=QueueAndPools.atomicInteger.addAndGet(1);
                    if(QueueAndPools.smsQueue.isEmpty() || m>=20){
                        sendUnBind();
                        QueueAndPools.atomicInteger.set(1);
                    }
                } catch (Exception e) { closeMySocket();e.printStackTrace(); }
                System.out.println("队列中还有"+QueueAndPools.smsQueue.size()+"条");
                if(getFromDB==null){
                    getFromDB = (GetFromDB)MainClass.applicationContext.getBean("getFromDB");
                }
                getFromDB.updateFlag(smsBody.getInnum());//更新状态
                getFromDB.insertLogNew(smsBody);//记录日志
               /*int n= QueueAndPools.incram.addAndGet(1);
                if(n>=1000)QueueAndPools.incram.set(1);*/
            }
            //conectserver();
            //sendBind();//绑定操作
           //sendSumbit();//提交短信
            //sendUnBind();//注销操作
        } catch (Exception e) {
            e.printStackTrace();
            try {
                closeMySocket(); } catch (Exception e1) { e1.printStackTrace();  }
        }

    }
    /*发送绑定信息*/
    public void sendBind() throws Exception{
        Bind bind = new Bind();
        ByteBuffer buffer = bind.getAllBindBuffer();
        Dos.write(buffer.array());
        Dos.flush();
        System.out.println("发送绑定请求，长度为:"+bind.getAllBindBuffer().capacity());
        int messagelength = Dis.readInt();
        byte b[] = new byte[messagelength - 4];
        Dis.readFully(b, 0, messagelength - 4);
        ByteBuffer buffer2=ByteBuffer.allocate(4);
        buffer.order(byteOrder);
        buffer2.put(b[16]);
        buffer2.put((byte)0);
        buffer2.put((byte)0);
        buffer2.put((byte)0);
        buffer2.flip();
        System.out.println("绑定结果是:"+buffer2.getInt());
    }

    /*发送短信*/
    public void sendSumbit(SMSBody smsBody) throws Exception{
       // SMSBody smsBody=new SMSBody();//从数据库获取  TODO: handle exception
        if(smsBody==null|| "".equals(smsBody.getMobile_no()) ) {
            System.out.println("没有数据...");
            return;
        }
        Submit submit=new Submit();
        BuesinessVO buesinessVO=getServiceInfo(smsBody.getServiceid());
        submit.SPNumber=smsBody.getServiceid();
        submit.UserNumber=smsBody.getMobile_no();
        submit.ServiceType=buesinessVO.getbCode();  //业务代码
        submit.FeeType=buesinessVO.getJftype();           //计费类型 1免费,2,按条计费,3包月计费
        submit.FeeValue=buesinessVO.getJfValue();            //计费数量，单位是分
        submit.MorelatetoMTFlag=buesinessVO.getReson();          //1B 引起MT消息的原因0-MO点播引起的第一条MT消息；1-MO点播引起的非第一条MT消息；2-非MO点播引起的MT消息；3-系统反馈引起的MT消息
        submit.MessageContent=smsBody.getMsg();   //短消息内容
        submit.Reserve=(buesinessVO.getJftype()==(byte)3)?"":smsBody.getReserve();             //linkid
        submit.idnum=String.valueOf(smsBody.getInnum());
        smsBody.setCreateTime(submit.head.createTime) ;
        System.out.println(submit.UserNumber+":"+submit.ServiceType+";FeeType:"+submit.FeeType+";FeeValue:"+submit.FeeValue+";MorelatetoMTFlag:"+submit.MorelatetoMTFlag+";Reserve:"+submit.Reserve);

        ByteBuffer buffer=submit.getAllSubmitBuffer();
        ByteBuffer buffer1=buffer;
        try {
            Dos.write(buffer.array());
            Dos.flush();
            System.out.println("write成功！！");
        } catch (IOException e) {
            System.out.println("DOS异常！");
            closeMySocket();
            e.printStackTrace();
        }finally{

        }
        int messagelength;
        try {
            messagelength = Dis.readInt();
            byte b[] = new byte[messagelength - 4];
            Dis.readFully(b, 0, messagelength - 4);
            ByteBuffer buffer2=ByteBuffer.allocate(4);
            buffer2.order(byteOrder);
            buffer2.put(b[16]);
            buffer2.put((byte)0);
            buffer2.put((byte)0);
            buffer2.put((byte)0);
            buffer2.flip();

            System.out.println("发送结果是:"+buffer2.getInt());
        } catch (IOException e) {
            closeMySocket();
            System.out.println("Dis报错！！");
        }finally{
            int a1=buffer1.getInt();
            int a2=buffer1.getInt();
            int a3=buffer1.getInt();
            int a4=buffer1.getInt();
            int a5=buffer1.getInt();

            //int yy=ss.updateFlag(submit.idnum);
            //int insertno = ss.insertLog(submit.UserNumber,String.valueOf(submit.head.createTime) , submit.ServiceType, submit.MessageContent,r[3]);
            //System.out.println("创建时间:"+a4+"::insertCount="+insertno+"updateCount="+yy+"::spcode="+r[0]);
        }
    }


    public BuesinessVO getServiceInfo(String serviceNum){
        BuesinessVO bu=null;
        if("106289977".equals(serviceNum)){
            bu=buesiness.get106289977();
        }else if("106289975".equals(serviceNum)){
            bu=buesiness.get106289975();
        }else if("106558411".equals(serviceNum)){
            bu=buesiness.get106558411();
        }else if("10628997".equals(serviceNum)){
            bu=buesiness.get10628997();
        }else if("106289978".equals(serviceNum)){
            bu=buesiness.get106289978();
        }else if("106289976".equals(serviceNum)){
            bu=buesiness.get106289976();
        }else if("106558412".equals(serviceNum)){
            bu=buesiness.get106558412();
        }else if("106289972".equals(serviceNum)){
            bu=buesiness.get106289972();
        }else if("106289973".equals(serviceNum)){
            bu=buesiness.get106289973();
        }
        return bu;
    }
    public void sendUnBind() throws Exception{
        UnBind UB=new UnBind();
        Dos.write(UB.getUnBindBuffer().array());
        Dos.flush();

        byte b[] = new byte[20];
        Dis.readFully(b, 0, 20);
        ByteBuffer buffer2=ByteBuffer.allocate(20);
        buffer2.order(byteOrder);
        for(int i=0;i<20;i++){
            buffer2.put(b[i]);
        }
        buffer2.flip();
        int a1=buffer2.getInt();
        int a2=buffer2.getInt();
        switch (a2) {
            case SGIP_UNBIND_RESP:
                System.out.println("注销回应");
                closeMySocket();
                break;
            default:
                System.out.println("未知回应...");
                break;
        }


    }

    public void closeMySocket() throws Exception{
        try {
            this.Dis.close();
            this.Dos.close();
            this.mySocket.close();
            System.out.println("socket关闭");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
   /* public void testLanckTimer(){
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                System.out.println("test");
                throw new RuntimeException();
            }
        } ;
        QueueAndPools.scheduExec.scheduleWithFixedDelay(runnable,5,15, TimeUnit.SECONDS);
    }

    public void getSMS(){
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                SMSBody s=new SMSBody();
                s.setInnum(9L);
                s.setMobile_no("1355032");
                System.out.println("sms");
                try {
                    QueueAndPools.smsQueue.put(s);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } ;
        QueueAndPools.scheduExec.scheduleWithFixedDelay(runnable,5,10,TimeUnit.SECONDS);
    }*/
}
