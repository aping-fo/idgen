package com.foo;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foo.SeqType;
import com.foo.SeqData;
public class UUIDMgr
{
    /**
     * 日志记录
     */
    private static final Logger Log = LoggerFactory.getLogger(UUIDMgr.class.getName());

    /**
     * 每次获取数量
     */
    private static final int uniqueIdAdd = 100;

    /**
     * 键值表格
     */
    private static Map<Integer, UniqueIdData> uniqueIdsMap = new HashMap<>();

    /**
     * 服务器进程id,保持唯一
     */
    private static final int serverId = GlobalConfigManager.getInstance().getServerId();

//    /**
//     * 键值表格
//     */
//    private static Map<Integer, UniqueIdIntData> uniqueIdIntsMap = new HashMap<>();

    public static boolean init()
    {
        return reload();
    }

    private static int generateSeqKey(int i)
    {
        return serverId + i * 10000;
    }
    
    public static boolean reload()
    {
        try
        {
            uniqueIdsMap.clear();
//            uniqueIdIntsMap.clear();

            for (int i = 1; i < SeqType.NUM_SEQ; i++)
            {
                int key = generateSeqKey(i);
                SeqData seqData = DaoManager.getSeqTableDao().getBySeqType(key); //此处读表，
                if (seqData == null)
                {
                    seqData = new SeqData();
                    seqData.setSeqType(key);
                    seqData.setValue(100);
                    DaoManager.getSeqTableDao().add(seqData);
                }
                
                uniqueIdsMap.put(seqData.getSeqType(), new UniqueIdData(seqData.getSeqType()));
            }

            return true;
        }
        catch (Exception e)
        {
            Log.error("UUIDMgr init exception", e);
            return false;
        }
    }

    /**
     * @param seqType
     * @return
     */
    public static long getCurrentId(int seqType)
    {
        int keyId = generateSeqKey(seqType);
        long uuid = serverId + uniqueIdsMap.get(keyId).value() * 10000L;
        return uuid;
    }

//    public static int getIntegerCurrentId(int seqType)
//    {
//        int keyId = generateSeqKey(seqType);
//        int uuid = uniqueIdIntsMap.get(keyId).value();
//        return uuid;
//    }

    private static long getLastId(int seqType)
    {
        return DaoManager.getSeqTableDao().getByKey(seqType).getValue();
    }

//    private static int getIntLastId(int seqType)
//    {
//        return DaoManager.getSeqTableDao().getByKey(seqType).getValue();
//    }

    private static class UniqueIdData
    {
        /**
         * 索引类型
         */
        private int uniqueId;

        /**
         * id计数器
         */
        private long currentId;

        /**
         * 最大索引
         */
        private long upperLimit;

        /**
         * 同步对象
         */
        private Object lock = new Object();

        private UniqueIdData(int uniqueId)
        {
            this.uniqueId = uniqueId;
            this.currentId = 0;
            this.upperLimit = 0;

            update();
        }

        /**
         * @return
         */
        public long value()
        {
            synchronized (lock)
            {
                long value = ++currentId;
                if (value >= upperLimit)
                    update();

                return value;
            }
        }

        /**
         * 每次获取指定数量100索引
         */
        private void update()
        {
            upperLimit = getLastId(uniqueId);
            currentId = upperLimit - uniqueIdAdd;
        }
    }
}
