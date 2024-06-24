package org.system.Modules.Student;

import org.system.database.Exam.Exam;
import org.system.database.Exam.ExamDatabase;
import org.system.database.Subject.Subject;
import org.system.database.Subject.SubjectDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class Student {
    private String name;
    private int id;
    private ArrayList<Subject> courses = new ArrayList<Subject>();
    private HashMap<Subject, Integer> grades = new HashMap<Subject, Integer>();
    private HashMap<Integer, Integer> gradesById = new HashMap<Integer, Integer>();

    public Student(String name, int id) {
        this.name = name;
        this.id = id;
        this.courses = studentCourses();

    }


    private ArrayList<Subject> studentCourses() {

        return SubjectDatabase.getAllStudentSubjects(this.getId());

    }

    public void calculateGrades() {
        for (Subject subject : this.getCourses()) {
            int i = 0;
            int total = 0;
            int average = 0;
            for (Exam exam : ExamDatabase.getExamsBySubjectId(subject.getSubject_id())) {
                try {
                    i++;
                    total += exam.getGrades().get(this.getId());
                } catch (Exception e) {
                    total += 0;
                }
                average = total / i;
                grades.put(subject, average);
                gradesById.put(subject.getSubject_id(), average);
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

    public ArrayList<Subject> getCourses() {
        return courses;
    }


    public HashMap<Subject, Integer> getGrades() {
        calculateGrades();
        return grades;
    }

    public HashMap<Integer, Integer> getGradesById() {
        calculateGrades();
        return gradesById;
    }

    public void setGrades(HashMap<Subject, Integer> grades) {
        this.grades = grades;
    }


}
