package com.clubank.domain;

import java.io.Serializable;

public class CheckinCriteria  extends SoapData implements Serializable  {
    /** 
    * @Fields serialVersionUID : TODO
    */ 
    private static final long serialVersionUID = 1L;
    public String MemNo;
    public String Name;
    public int PageSize;
    public int pageIndex;
    public String OrderKey;
    public int Distance;
}
