package hcmute.edu.vn.tlcn.Models;

import android.provider.BaseColumns;

public class NoticeDB {
    private NoticeDB(){}
    public  static final class NoticeEntry implements BaseColumns{
        public static final String TABLE_NAME = "notices";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_DAY = "day";
        public static final String COLUMN_UID = "uid";
    }
}
