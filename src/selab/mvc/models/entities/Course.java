package selab.mvc.models.entities;

import selab.mvc.models.Model;
//import sun.misc.Regexp;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Course implements Model {
    private String title;
    private String courseNo;
    private String startTime = null;
    private String endTime = null;
    private Weekday weekday;

    private ArrayList<Registration> registrations = new ArrayList<>();

    @Override
    public String getPrimaryKey() {
        return this.courseNo;
    }

    public void setTitle(String value) { this.title = value; }
    public String getTitle() { return this.title; }

    public void setCourseNo(String value) {
        if (!validateCourseNo(value))
            throw new IllegalArgumentException("Format is not correct");

        this.courseNo = value;
    }
    public String getCourseNo() { return this.courseNo; }

    public void setStartTime(String value) {
        if (!validateTime(value))
            throw new IllegalArgumentException("Invalid time format.");

        if (this.endTime != null && compareTime(value, this.endTime) != -1)
            throw new IllegalArgumentException("The start time cannot be past the end time.");

        this.startTime = value;
    }
    public String getStartTime() { return this.startTime; }

    public void setEndTime(String value) {
        if (!validateTime(value))
            throw new IllegalArgumentException("Invalid time format");

        if (this.startTime != null && compareTime(value, this.startTime) != 1)
            throw new IllegalArgumentException("The end time cannot be earlier than the start time.");

        this.endTime = value;
    }

    public String getEndTime() { return this.endTime; }

    public void setWeekday(Weekday value) {
        this.weekday = value;
    }

    public String getWeekday() {
        return this.weekday.name();
    }

    public float getAverage() {
        if (registrations.size() == 0){
            return 0;
        }
        return (float) registrations.stream().map(Registration::getPoint).mapToDouble(x -> x).sum() / registrations.size();
    }

    public String getStudents() {
        return String.join("-", registrations.stream().map(x -> x.getStudent().getName()).collect(Collectors.toList()));
    }

    /**
     *
     * @param value The value to be validated as course number
     * @return true, if value is in a correct format
     */
    private boolean validateCourseNo(String value) {
        Pattern pattern = Pattern.compile("^\\d{5}-\\d$");
        return pattern.matcher(value).find();
    }

    /**
     *
     * @param value The time to be checked
     * @return true, if the format of the input is appropriate for a time, like
     */
    private boolean validateTime(String value) {
        Pattern pattern = Pattern.compile("^((0|1)\\d|2[0-4]):([0-5]\\d)$");
        return pattern.matcher(value).find();
    }

    /**
     *
     * @param time1 First time
     * @param time2 Second time
     * @return If time1 > time2, returns 1, if time1 < time2 returns -1, otherwise returns 0
     */
    private int compareTime(String time1, String time2) {
        int time1Hour = Integer.parseInt(time1.substring(0, 2));
        int time1Minute = Integer.parseInt(time1.substring(3, 5));
        int time2Hour = Integer.parseInt(time2.substring(0, 2));
        int time2Minute = Integer.parseInt(time2.substring(3, 5));

        if (time1Hour > time2Hour || (time1Hour == time2Hour && time1Minute > time2Minute))
            return 1;
        else if (time1Hour == time2Hour && time1Minute == time2Minute)
            return 0;
        else
            return -1;
    }

    public void addRegistration(Registration registration){
        if (!registration.getCourse().getPrimaryKey().equals(this.getPrimaryKey()))
            throw new RuntimeException("Course number mismatch");

        for (Registration existingRegistration: registrations) {
            if (existingRegistration.getStudent().getPrimaryKey().equals(registration.getStudent().getPrimaryKey())){
                throw new RuntimeException("Such student already exists");
            }
        }
        registrations.add(registration);
    }
}
