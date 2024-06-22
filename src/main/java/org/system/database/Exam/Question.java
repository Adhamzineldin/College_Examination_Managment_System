package org.system.database.Exam;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Question implements Serializable {
    String question;
    ArrayList<Option> options = new ArrayList<Option>();
    String question_answer = "0";
    int question_number;

    Question(String question) {
        this.question = question;

    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public LinkedHashMap<Integer, String> getOptions() {
        int i = 1;
        LinkedHashMap<Integer, String> tempOptions = new LinkedHashMap<>();
        for (Option option : options) {
            option.setOption_number(i);
            i++;
            tempOptions.put(option.getOption_number(), option.getOption());
        }
        return tempOptions;
    }

    public void addOption(Option option) {
        options.add(option);
    }

    public String getQuestion_answer() {
        return question_answer;
    }

    public void setQuestion_answer(Option option) {
        getOptions();
        this.question_answer = String.valueOf(option.getOption_number());
    }

    public int getQuestion_number() {
        return question_number;
    }

    public void setQuestion_number(int question_number) {
        this.question_number = question_number;
    }
}
