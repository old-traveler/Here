package com.here.bean;

/**
 * Created by hyc on 2017/7/13 10:34
 */

public class Community {

    private static Propaganda[] propagandas;

    public static int TYPE_VIEW_PAGE = 0x21;

    public static int TYPE_COMMUNITY = 0x22;

    public static int TYPE_APPOINTMENT = 0x23;

    public static int TYPE_SHARE = 0x24;

    public static int TYPE_TIPS = 0x25;

    private int type;

    private String describe;

    private Mood mood;

    private Appointment appointment;

    public static Propaganda[] getPropagandas() {
        return propagandas;
    }

    public static void setPropagandas(Propaganda[] propagandas) {
        Community.propagandas = propagandas;
    }

    public Community(int type){
        this.type = type;
    }

    public Community(){

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public Mood getMood() {
        return mood;
    }

    public void setMood(Mood mood) {
        this.mood = mood;
    }
}
