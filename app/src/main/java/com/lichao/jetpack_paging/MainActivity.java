package com.lichao.jetpack_paging;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Button buttonPopulate, buttonClear;
    StudentDao studentDao;
    StudentsDatabase studentsDatabase;
    MyPagedAdapter pagedAdapter;
    LiveData<PagedList<Student>> allStudentsLivePaged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        pagedAdapter = new MyPagedAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        // 分隔符
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(pagedAdapter);

        // 数据获取
        studentsDatabase = StudentsDatabase.getInstance(this);
        studentDao = studentsDatabase.getStudentDao();

        allStudentsLivePaged = new LivePagedListBuilder<>(studentDao.getAllStudents(), 20).build();

        allStudentsLivePaged.observe(this, new Observer<PagedList<Student>>() {
            @Override
            public void onChanged(PagedList<Student> students) {
                // 数据刷新
                pagedAdapter.submitList(students);
                // 监听数据库加载状态
                students.addWeakCallback(null, new PagedList.Callback() {
                    @Override
                    public void onChanged(int position, int count) {

                    }

                    @Override
                    public void onInserted(int position, int count) {

                    }

                    @Override
                    public void onRemoved(int position, int count) {

                    }
                });
            }
        });

        buttonPopulate = findViewById(R.id.buttonPopulate);
        buttonClear = findViewById(R.id.buttonClear);

        buttonPopulate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Student[] students = new Student[1000];
                for (int i = 0; i < 1000; i++) {
                    Student student = new Student();
                    student.setStudentNumber(i);
                    students[i] = student;
                }
                new InsertAsyncTask(studentDao).execute(students);
            }
        });

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ClearAsyncTask(studentDao).execute();
            }
        });
    }

    // 数据库操作异步处理
    static class InsertAsyncTask extends AsyncTask<Student, Void, Void> {
        StudentDao studentDao;

        public InsertAsyncTask(StudentDao studentDao) {
            this.studentDao = studentDao;
        }

        @Override
        protected Void doInBackground(Student... students) {
            studentDao.insertStudents(students);
            return null;
        }
    }

    static class ClearAsyncTask extends AsyncTask<Void, Void, Void> {
        StudentDao studentDao;

        public ClearAsyncTask(StudentDao studentDao) {
            this.studentDao = studentDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            studentDao.deleteAllStudents();
            return null;
        }
    }
}
