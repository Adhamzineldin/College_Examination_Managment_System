package org.system.Modules.Lecturer;

import org.system.database.Exam.Exam;
import org.system.database.Exam.ExamDatabase;
import org.system.database.Subject.Subject;
import org.system.database.Subject.SubjectDatabase;

import java.util.ArrayList;
import java.util.List;

public class Lecturer {
    private String name;
    private int id;
    private final ArrayList<String> courses = new ArrayList<String>();

    public Lecturer(String name, int id) {
        this.name = name;
        this.id = id;
        lecturerCourses();
    }


    private void lecturerCourses() {
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

    public List<Exam> getExams(int subject_id) {
        return ExamDatabase.getExamsBySubjectId(subject_id);
    }

    public void addExam(Exam exam) {
        ExamDatabase.insertExam(exam);
    }

    public void deleteExam(Exam exam) {
        ExamDatabase.deleteExam(exam.getId());
    }

    public void upDateExam(int examID, Exam exam) {
        ExamDatabase.updateExam(examID, exam);
    }
    

    public void removeExam(Exam exam) {

    }
}
