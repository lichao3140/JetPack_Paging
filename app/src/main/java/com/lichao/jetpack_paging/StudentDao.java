package com.lichao.jetpack_paging;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface StudentDao {

    // 添加
    @Insert
    void insertStudents(Student...students);

    // 删除所有
    @Query("DELETE FROM student_table")
    void deleteAllStudents();

    // 查询所有
    @Query("SELECT * FROM student_table ORDER BY id")
    DataSource.Factory<Integer, Student> getAllStudents();
}
