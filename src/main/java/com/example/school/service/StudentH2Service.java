/*
 * You can use the following import statements
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.http.HttpStatus;
 * import org.springframework.jdbc.core.JdbcTemplate;
 * import org.springframework.stereotype.Service;
 * import org.springframework.web.server.ResponseStatusException;
 * import java.util.ArrayList;
 *
 */

package com.example.school.service;

import com.example.school.model.Student;

import com.example.school.model.StudentRowMapper;
import com.example.school.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class StudentH2Service implements StudentRepository {

    
    @Autowired
    private JdbcTemplate db;


    @Override
    public ArrayList<Student> getAllStudents() {
        List<Student> studentList = db.query("select * from STUDENT", new StudentRowMapper());
        return new ArrayList<>(studentList);
    }

    
    @Override
    public Student getStudentById(int studentId) {
        try {
            Student existingStudent = db.queryForObject("select * from STUDENT where studentId=?", new StudentRowMapper(), studentId);
            return existingStudent;
        } catch (Exception e) {
            
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    
    @Override
    public Student addStudent(Student stud) {
        db.update("insert into STUDENT (studentName,gender,standard) values(?,?,?)", stud.getStudentName(), stud.getGender(), stud.getStandard());
        return db.queryForObject("select * from STUDENT where studentName=? and gender=?", new StudentRowMapper(), stud.getStudentName(), stud.getGender());
        
    }

    @Override
    public String addMultipleStudents(ArrayList<Student> students) {
    int count = 0;
    for (Student stud : students) {
        addStudent(stud);
        count++;
    }
    return ("Successfully added "+ count + " students");
    }

    
    @Override
    public Student updateStudent(int studentId, Student stud) {
        if (stud.getStudentName() != null) {
            db.update("update STUDENT set studentName =? where studentId=?", stud.getStudentName(), studentId);
        }
        if (stud.getGender() != null) {
            db.update("update STUDENT set gender =? where studentId=?", stud.getGender(), studentId);
        }
        if (stud.getStandard() != 0) {
            db.update("update STUDENT set standard=? where studentId=?", stud.getStandard(), studentId);
        }
    
        return getStudentById(studentId);
    }

    
    @Override
    public void deleteStudent(int studentId) {
        db.update("delete from STUDENT where studentId=?", studentId);
    }
}