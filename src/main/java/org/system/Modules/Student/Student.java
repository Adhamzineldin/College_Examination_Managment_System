package org.system.Modules.Student;

import org.system.database.Subject.Subject;
import org.system.database.Subject.SubjectDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class Student {
    private String name;
    private int id;
    private final ArrayList<String> courses = new ArrayList<String>();
    private HashMap<String, Integer> grades;

    public Student(String name, int id) {
        this.name = name;
        this.id = id;
        studentCourses();


    }


    private void studentCourses() {
        ArrayList<Subject> all_subjects = SubjectDatabase.getAllSubjects();
        for (Subject subject : all_subjects) {
            for (Integer id : subject.getStudent_ids()) {
                if (this.id == id) {
                    this.courses.add(subject.getSubject_name());
                }

            }
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<String> getCourses() {
        return courses;
    }


    public HashMap<String, Integer> getGrades() {
        return grades;
    }

    public void setGrades(HashMap<String, Integer> grades) {
        this.grades = grades;
    }


}
