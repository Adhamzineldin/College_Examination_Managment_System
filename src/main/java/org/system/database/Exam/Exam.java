package org.system.database.Exam;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Exam implements Serializable {
    ArrayList<Question> questions;
    HashMap<Integer, Question> question_map;
    int id;
    String subject;
    int subject_id;
    ArrayList<Integer> id_of_who_joined;
    HashMap<Integer, HashMap<Integer, Integer>> answers;
    HashMap<Integer, Integer> grades;
    int duration;


    public Exam() {
        this.questions = new ArrayList<>();
        this.question_map = new HashMap<>();
        this.id_of_who_joined = new ArrayList<>();
        this.answers = new HashMap<>();
        this.grades = new HashMap<>();
        this.duration = 0;
    }

    public Exam(int subject_id, String subject, ArrayList<Integer> id_of_who_joined, HashMap<Integer, HashMap<Integer, Integer>> answers, HashMap<Integer, Integer> grades, int duration) {
        questions = new ArrayList<>();
        question_map = new HashMap<>();
        this.id_of_who_joined = id_of_who_joined;
        this.answers = answers;
        this.grades = grades;
        this.duration = duration;
        this.subject = subject;
        this.subject_id = subject_id;

    }

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public HashMap<Integer, Question> getQuestions() {
        int i = 1;
        for (Question question : questions) {
            question.setQuestion_number(i);
            question_map.put(question.getQuestion_number(), question);
            i++;
        }

        return question_map;
    }

    public String getExam() {
        LinkedHashMap<LinkedHashMap<Integer, String>, LinkedHashMap<LinkedHashMap<Integer, String>, Integer>> exam = new LinkedHashMap<>();

        for (Integer key : getQuestions().keySet()) {
            LinkedHashMap<Integer, String> questionTemp = new LinkedHashMap<>();
            LinkedHashMap<LinkedHashMap<Integer, String>, Integer> optionsTemp = new LinkedHashMap<>();

            questionTemp.put(question_map.get(key).getQuestion_number(), question_map.get(key).getQuestion());
            optionsTemp.put(question_map.get(key).getOptions(), question_map.get(key).getQuestion_answer());

            exam.put(questionTemp, optionsTemp);
        }

        return exam.toString();
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubjectID(int subject_id) {
        this.subject_id = subject_id;
    }

    public int getSubjectID() {
        return subject_id;
    }

    public void setIdsOfWhoJoined(ArrayList<Integer> idsOfWhoJoined) {
        this.id_of_who_joined = idsOfWhoJoined;
    }

    public ArrayList<Integer> getIdsOfWhoJoined() {
        return id_of_who_joined;
    }

    public void setAnswers(HashMap<Integer, HashMap<Integer, Integer>> answers) {
        this.answers = answers;
    }

    public HashMap<Integer, HashMap<Integer, Integer>> getAnswers() {
        return answers;
    }

    public void setGrades(HashMap<Integer, Integer> grades) {
        this.grades = grades;
    }

    public HashMap<Integer, Integer> getGrades() {
        return grades;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }


    public void printExamDetails() {
        System.out.println("Exam ID: " + id);
        System.out.println("Subject: " + subject);
        System.out.println("Subject ID: " + subject_id);
        System.out.println("Duration: " + duration + "m");

        System.out.println("Students who joined: ");
        for (Integer studentId : id_of_who_joined) {
            System.out.println("  - Student ID: " + studentId);
        }

        System.out.println("Grades: ");
        for (Integer studentId : grades.keySet()) {
            System.out.println("  - Student ID: " + studentId + " - Grade: " + grades.get(studentId));
        }
    }

    public void printExam() {
        this.getExam();
        System.out.println("Subject: " + this.subject);
        System.out.println("Duration: " + this.duration + "m"); // Print duration

        // Rest of the method remains the same
        // Print questions and options
        System.out.println("Exam Structure:");

        for (Question question : this.questions) {
            System.out.println("    Question " + question.getQuestion_number() + ": " + question.getQuestion());

            LinkedHashMap<Integer, String> options = question.getOptions();
            for (Integer optionNumber : options.keySet()) {
                String optionText = options.get(optionNumber);
                int answerNumber = (question.getQuestion_answer() == optionNumber) ? optionNumber : -1; // -1 if not answer

                System.out.println("        Option " + optionNumber + ": " + optionText);
            }
        }
    }


}
