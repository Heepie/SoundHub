package com.heepie.soundhub.utils;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.heepie.soundhub.Interfaces.SearchDao;
import com.heepie.soundhub.domain.model.Search;

/**
 * Created by eunmin on 2017-12-18.
 */

@Database(entities = {Search.class}, version = 1, exportSchema = false)
public abstract class DBHelper extends RoomDatabase {
    private static DBHelper INSTANCE = null;
    private static final Object sLock = new Object();
    public static DBHelper getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room
                        .databaseBuilder(context.getApplicationContext()
                                , DBHelper.class
                                , "soundhub.db")
                        .allowMainThreadQueries()
                        .build();
            }
            return INSTANCE;
        }
    }

    // Dao 선언
    public abstract SearchDao searchDao();
}
