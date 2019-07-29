package selab.mvc.models.entities;

import selab.mvc.models.Model;

public class Registration implements Model {
    private Student student;
    private Course course;
    private double point;

    @Override
    public String getPrimaryKey() {
        return student.getPrimaryKey() + "-" + course.getPrimaryKey();
    }

    public void setStudent(Student student){
        this.student = student;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Course getCourse() {
        return course;
    }

    public Student getStudent() {
        return student;
    }

    public double getPoint() {
        return point;
    }

    public void setPoint(double point) {
        this.point = point;
    }
}
