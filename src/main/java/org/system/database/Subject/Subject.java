package org.system.database.Subject;

import java.util.ArrayList;

public class Subject {
    private int subject_id;
    private String subject_name;
    private int lecturer_id;
    private ArrayList<Integer> student_ids;


    public Subject(String subject_name, int lecturer_id, ArrayList<Integer> student_ids) {
        this.subject_name = subject_name;
        this.lecturer_id = lecturer_id;
        this.student_ids = student_ids;
    }

    public Subject(Integer subject_id, String subject_name, int lecturer_id, ArrayList<Integer> student_ids) {
        this.subject_id = subject_id;
        this.subject_name = subject_name;
        this.lecturer_id = lecturer_id;
        this.student_ids = student_ids;
    }

    public int getSubject_id() {
        return subject_id;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
        SubjectDatabase.updateSubject(this);
    }

    public int getLecturer_id() {
        return lecturer_id;

    }

    public void setLecturer_id(int lecturer_id) {
        this.lecturer_id = lecturer_id;
        SubjectDatabase.updateSubject(this);
    }

    public ArrayList<Integer> getStudent_ids() {
        return student_ids;
    }

    public void setStudent_ids(ArrayList<Integer> student_ids) {
        this.student_ids = student_ids;
        SubjectDatabase.updateSubject(this);
    }

    public void addStudent_id(Integer student_id) {
        this.student_ids.add(student_id);
        SubjectDatabase.updateSubject(this);
    }

    public void removeStudent_id(Integer student_id) {
        this.student_ids.remove(student_id);
        SubjectDatabase.updateSubject(this);
    }


}
