package com.foo;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foo.SeqType;
import com.foo.SeqData;
public class Test
{
    public static void main(String[] args) {
        long id =  UUIDMgr.getCurrentId(SeqType.T_U_HISTORY);
        System.out.println(id);
    }
}
