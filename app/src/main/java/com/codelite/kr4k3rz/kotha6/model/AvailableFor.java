package com.codelite.kr4k3rz.kotha6.model;

import java.io.Serializable;

public class AvailableFor implements Serializable{
    private boolean men;
    private boolean women;
    private boolean couple;
    private boolean students;
    private boolean professionals;

    public boolean isMen() {
        return men;
    }

    public void setMen(boolean men) {
        this.men = men;
    }

    public boolean isWomen() {
        return women;
    }

    public void setWomen(boolean women) {
        this.women = women;
    }

    public boolean isCouple() {
        return couple;
    }

    public void setCouple(boolean couple) {
        this.couple = couple;
    }

    public boolean isStudents() {
        return students;
    }

    public void setStudents(boolean students) {
        this.students = students;
    }

    public boolean isProfessionals() {
        return professionals;
    }

    public void setProfessionals(boolean professionals) {
        this.professionals = professionals;
    }
}
