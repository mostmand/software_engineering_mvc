package selab.mvc.models.entities;

import selab.mvc.models.Model;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Student implements Model {
    private String name;
    private String studentNo;

    private ArrayList<Registration> registrations = new ArrayList<>();

    @Override
    public String getPrimaryKey() {
        return this.studentNo;
    }

    public void setName(String value) { this.name = value; }
    public String getName() { return this.name; }

    public void setStudentNo(String value) {
        if (!validateStudentNo(value))
            throw new IllegalArgumentException("The format is not correct");

        this.studentNo = value;
    }
    public String getStudentNo() { return this.studentNo; }

    public float getAverage() {
        if (registrations.size() == 0){
            return 0;
        }
        return (float) registrations.stream().map(Registration::getPoint).mapToDouble(x -> x).sum() / registrations.size();
    }

    public String getCourses() {
        return String.join("-", registrations.stream().map(x -> x.getCourse().getTitle()).collect(Collectors.toList()));
    }

    /**
     *
     * @param studentNo Student number to be checeked
     * @return true, if the format of the student number is correct
     */
    private boolean validateStudentNo(String studentNo) {
        Pattern pattern = Pattern.compile("^[8-9]\\d{7}$");
        return pattern.matcher(studentNo).find();
    }

    public void addRegistration(Registration registration){
        if (!registration.getStudent().getPrimaryKey().equals(this.getPrimaryKey()))
            throw new RuntimeException("Student number mismatch");

        for (Registration existingRegistration: registrations) {
            if (existingRegistration.getCourse().getPrimaryKey().equals(registration.getCourse().getPrimaryKey())){
                throw new RuntimeException("Such course already exists");
            }
        }
        registrations.add(registration);
    }
}
