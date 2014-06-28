package com.sgip.domain.VO;

/**
 * Created with IntelliJ IDEA.
 * User: chace.cai
 * Date: 13-10-16
 * Time: 下午4:21
 * To change this template use File | Settings | File Templates.
 */
public class SMSBody {
    private long innum;
    private String serviceid;
    private String mobile_no;
    private String msg;
    private String reserve;
    private  int createTime;

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public long getInnum() {
        return innum;
    }

    public void setInnum(long innum) {
        this.innum = innum;
    }

    public String getServiceid() {
        return serviceid;
    }

    public void setServiceid(String serviceid) {
        this.serviceid = serviceid;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getReserve() {
        return reserve;
    }

    public void setReserve(String reserve) {
        this.reserve = reserve;
    }
}
