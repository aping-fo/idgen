package com.foo;

public class SeqData
{

    public SeqData()
    {

    }

    public SeqData(int seqType, int value)
    {
        this.seqType = seqType;
        this.value = value;
    }

    /**
     * 对应的需要自增字段的表的类型值
     */
    private int seqType;

    /**
     * 自增的下一个字段
     */
    private int value;

    /**
     * 对应的需要自增字段的表的类型值
     */
    public int getSeqType()
    {
        return seqType;
    }

    /**
     * 对应的需要自增字段的表的类型值
     */
    public void setSeqType(int seqType)
    {
        this.seqType = seqType;
    }

    /**
     * 自增的下一个字段
     */
    public int getValue()
    {
        return value;
    }

    /**
     * 自增的下一个字段
     */
    public void setValue(int value)
    {
        this.value = value;
    }

}
