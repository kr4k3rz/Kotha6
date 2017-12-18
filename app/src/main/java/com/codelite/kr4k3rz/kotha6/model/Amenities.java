package com.codelite.kr4k3rz.kotha6.model;

import java.io.Serializable;

public class Amenities implements Serializable {
    private boolean Wifi;
    private boolean Parking;
    private boolean Smoking;
    private boolean Party;
    private boolean Pets;

    public boolean isWifi() {
        return Wifi;
    }

    public void setWifi(boolean wifi) {
        Wifi = wifi;
    }

    public boolean isParking() {
        return Parking;
    }

    public void setParking(boolean parking) {
        Parking = parking;
    }

    public boolean isSmoking() {
        return Smoking;
    }

    public void setSmoking(boolean smoking) {
        Smoking = smoking;
    }

    public boolean isParty() {
        return Party;
    }

    public void setParty(boolean party) {
        Party = party;
    }

    public boolean isPets() {
        return Pets;
    }

    public void setPets(boolean pets) {
        Pets = pets;
    }
}
