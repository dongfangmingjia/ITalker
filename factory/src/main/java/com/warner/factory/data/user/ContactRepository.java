package com.warner.factory.data.user;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.warner.factory.data.BaseDBRepository;
import com.warner.factory.data.DataSource;
import com.warner.factory.model.db.User;
import com.warner.factory.model.db.User_Table;
import com.warner.factory.persistence.Account;

import java.util.List;

/**
 * Created by warner on 2018/2/6.
 */

public class ContactRepository extends BaseDBRepository<User> implements ContactDataSource {

    @Override
    public void load(DataSource.SuccessCallback<List<User>> callback) {
        super.load(callback);


        SQLite.select()
                .from(User.class)
                .where(User_Table.isFollow.eq(true))
                .and(User_Table.id.notEq(Account.getUserId()))
                .orderBy(User_Table.name, true)
                .limit(100)
                .async()
                .queryListResultCallback(this).execute();
    }

    /**
     * 检查一个User是否是我需要关注的数据
     * @param user
     * @return
     */
    @Override
    protected boolean isRequired(User user) {
        return user != null && user.isFollow() && !user.getId().equals(Account.getUserId());
    }
}
