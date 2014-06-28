package com.sgip.domain.VO;

/**
 * Created with IntelliJ IDEA.
 * User: wula
 * Date: 13-10-19
 * Time: 下午9:27
 * To change this template use File | Settings | File Templates.
 */
public class BuesinessVO {
    private String bCode;//业务编码
    private String bName;//业务名称代码
    private byte jftype;  //1B 计费类型 1免费,2,按条计费,3包月计费
    private String jfValue;//计费值
    private byte reson; //1B 引起MT消息的原因0-MO点播引起的第一条MT消息；1-MO点播引起的非第一条MT消息；2-非MO点播引起的MT消息；3-系统反馈引起的MT消息

    public String getbCode() {
        return bCode;
    }

    public void setbCode(String bCode) {
        this.bCode = bCode;
    }

    public String getbName() {
        return bName;
    }

    public void setbName(String bName) {
        this.bName = bName;
    }

    public byte getJftype() {
        return jftype;
    }

    public void setJftype(byte jftype) {
        this.jftype = jftype;
    }

    public String getJfValue() {
        return jfValue;
    }

    public void setJfValue(String jfValue) {
        this.jfValue = jfValue;
    }

    public byte getReson() {
        return reson;
    }

    public void setReson(byte reson) {
        this.reson = reson;
    }
}
