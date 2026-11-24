package com.example.myexpenses;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CategoryDao {
    @Insert
    void insert(Category category);

    @Delete
    void delete(Category category);

    @Query("SELECT c.id, c.name, c.budget, COALESCE(SUM(e.amount), 0) AS total " +
            "FROM categories c LEFT JOIN expenses e ON c.id = e.category_id " +
            "GROUP BY c.id")
    LiveData<List<CategoryTotal>> getCategoriesWithTotal();
}