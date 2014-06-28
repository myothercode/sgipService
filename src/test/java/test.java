import com.sgip.comm.service.TestService;
import com.sgip.domain.VO.SMSBody;
import com.utils.CommUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.apache.commons.beanutils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/*
 * Created with IntelliJ IDEA.
 * User: wula
 * Date: 13-10-4
 * Time: 下午4:47
 * To change this template use File | Settings | File Templates.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class test {
    @Autowired
    private TestService  testService;

    @Test
    public void test1() throws Exception{
        //String s= QueueAndPools.threadPoolNum;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
       // Class cl=Class.forName(info[0], true, classLoader)
        SMSBody smsBody=new SMSBody();
        /*Method method= smsBody.getClass().getMethod("setInnum",long.class);
        Method[] dd=smsBody.getClass().getMethods() ;
        method.invoke(smsBody,98);*/
        Map map=new HashMap();
        map.put("innum",90L);

        BeanUtils.populate(smsBody,map);

        testService.test();
    }

}
