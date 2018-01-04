package com.heepie.soundhub.Interfaces;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.heepie.soundhub.domain.model.Search;

import java.util.List;

/**
 * Created by eunmin on 2017-12-18.
 */

@Dao
public interface SearchDao {
    @Query("select distinct id, search from Search order by id desc")
    List<Search> getAll();

    @Insert
    void insertItem(Search model);

    @Delete
    void deleteItem(Search model);

    @Query("delete from Search WHERE id NOT IN (SELECT MAX(id) FROM Search GROUP BY search)")
    void deleteItems();
}
