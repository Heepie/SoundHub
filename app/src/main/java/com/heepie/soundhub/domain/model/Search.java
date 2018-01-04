package com.heepie.soundhub.domain.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by eunmin on 2017-12-18.
 */

@Entity(tableName = "search")
public class Search {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo
    private String search;

    public Search() {}

    public Search(String search) {
        this.search = search;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
