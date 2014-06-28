package com.sgip.comm.service.impl;

import com.sgip.domain.VO.SMSBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Chace.Cai
 * Date: 13-10-18
 * Time: 下午4:55
 * To change this template use File | Settings | File Templates.
 */
@Service("getFromDB")
@Scope("prototype")
public class GetFromDB {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<SMSBody> getSmsList(){
        String sql="select top 20 sendtb.idnum, sendtb.serviceid,sendtb.mobile_no,sendtb.msg,sendtb.reserve from sms_send_tb sendtb " +
                "left join sms_user_tb usertb on sendtb.user_id = usertb.id " +
                "where sendtb.flag='0' and usertb.totalnum>=5 and usertb.totalnum is not null " +
                "order by sendtb.idnum asc";
        //Object[] params = new Object[]{loginId};
        List<SMSBody> list=jdbcTemplate.query(sql,new DataMapperD());
        if(list==null || list.isEmpty())return null;
        return list;
    }

    public void updateFlag(Long id){
        String sql="update sms_send_tb set flag=flag+1,sendTime=getdate() where flag='0' and idnum=?";
        Object[] params = new Object[]{id};
        jdbcTemplate.update(sql,params);
    }

    public void insertLogNew(SMSBody smsBody){
        String sql="insert into sms_log_new(mobile_no,time,spcode,message,reserve) values(?,?,?,?,?)";
        Object[] params = new Object[]{smsBody.getMobile_no(),smsBody.getCreateTime(),smsBody.getServiceid(),smsBody.getMsg(),smsBody.getReserve()};
        jdbcTemplate.update(sql,params);
    }

}

class DataMapperD implements RowMapper {
    public Object mapRow(ResultSet rs,int rowNum) throws SQLException {
        SMSBody sm=new SMSBody();
        sm.setServiceid(rs.getString("serviceid"));
        sm.setInnum(rs.getLong("idnum"));
        sm.setMobile_no(rs.getString("mobile_no"));
        sm.setMsg(rs.getString("msg"));
        sm.setReserve(rs.getString("reserve"));
        return sm;
    }
}
