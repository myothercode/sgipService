package com.main.task;

import com.sgip.comm.service.impl.GetFromDB;
import com.sgip.domain.QueueAndPools;
import com.sgip.domain.VO.SMSBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wula
 * Date: 13-10-18
 * Time: 下午10:30
 * To change this template use File | Settings | File Templates.
 */
@Service("scheduledTask")
public class ScheduledTask {
   @Autowired
    private GetFromDB getFromDB;

    public void getSmsFronDB() throws Exception{
        //int ii=QueueAndPools.incram.get();
        //QueueAndPools.isRuning.set(m);
        /*判断线程是否还在运行*/
        System.out.println("get from db .......");
        int n = Thread.activeCount();
        Thread[] threads = new Thread[n];
        Thread.enumerate(threads);
        boolean b = false;
        for (int i = 0; i < threads.length; i++) {
            Thread thread = threads[i];
            if (thread.getName().equals("xiancheng1")) {
                b = true;
                break;
            }
        }
        if(!b){QueueAndPools.taskExecutor.execute(new SubmitSMS("xiancheng1"));}

        if(QueueAndPools.smsQueue.isEmpty()){
            List<SMSBody> list= getFromDB.getSmsList();
            if (list!=null){
                for (SMSBody smsBody : list){
                    QueueAndPools.smsQueue.put(smsBody);
                }
            }
        }



        /*System.out.println("put sms............");
        SMSBody smsBody=new SMSBody();
        smsBody.setMobile_no("13550329026");
        smsBody.setInnum(0123L);
        smsBody.setMsg("打豆豆");
        smsBody.setReserve("1212");
        smsBody.setServiceid("106289975");
        QueueAndPools.smsQueue.put(smsBody);
        QueueAndPools.smsQueue.put(smsBody);*/
        /*QueueAndPools.smsQueue.put(smsBody);
        QueueAndPools.smsQueue.put(smsBody);
        QueueAndPools.smsQueue.put(smsBody);
        QueueAndPools.smsQueue.put(smsBody);
        QueueAndPools.smsQueue.put(smsBody);
        QueueAndPools.smsQueue.put(smsBody);
        QueueAndPools.smsQueue.put(smsBody);
        QueueAndPools.smsQueue.put(smsBody);
        QueueAndPools.smsQueue.put(smsBody);
        QueueAndPools.smsQueue.put(smsBody);
        QueueAndPools.smsQueue.put(smsBody);
        QueueAndPools.smsQueue.put(smsBody);*/
       // throw new RuntimeException();

    }
}
