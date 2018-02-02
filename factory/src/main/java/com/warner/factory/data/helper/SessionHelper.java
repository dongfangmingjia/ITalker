package com.warner.factory.data.helper;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.warner.factory.model.db.Session;
import com.warner.factory.model.db.Session_Table;

/**
 * Created by warner on 2018/2/2.
 */

public class SessionHelper {

    /**
     * 从本地查询Session
     * @param id
     * @return
     */
    public static Session findFromLocal(String id) {
        return SQLite.select().from(Session.class).where(Session_Table.id.eq(id)).querySingle();
    }
}
