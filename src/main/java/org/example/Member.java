package org.example;

public class Member {
    private Integer id; // PK (nullable when not inserted)
    private String createDate; // ISO-8601 문자열 (SQLite TEXT)
    private String studentId; // 학번
    private String name; // 이름
    private String position; // 직책 (예: 팀장, 서기, 회계 등)
    private int year; // 학년


    public Member() {
    }


    public Member(Integer id, String createDate, String studentId, String name, String position, int year) {
        this.id = id;
        this.createDate = createDate;
        this.studentId = studentId;
        this.name = name;
        this.position = position;
        this.year = year;
    }

    public Member(String studentId, String name, String position, int year) {
        this(null, null, studentId, name, position, year);
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }


    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }


    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }


    @Override
    public String toString() {
        return String.format("[#%d] %s | 학번:%s | 직책:%s | 학년:%d | 등록:%s",
                id, name, studentId, position, year, createDate);
    }
}
