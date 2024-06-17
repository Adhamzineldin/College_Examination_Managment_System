package org.system.database.Exam;

import java.io.Serializable;

public class Option implements Serializable {
    String option;
    int option_number;

    public Option(String option) {
        this.option = option;
    }

    public String getOption() {
        return option;
    }

    public int getOption_number() {
        return option_number;
    }

    public void setOption_number(int option_number) {
        this.option_number = option_number;
    }


}
