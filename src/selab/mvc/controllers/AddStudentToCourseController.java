package selab.mvc.controllers;

import org.json.JSONObject;
import selab.mvc.models.DataContext;
import selab.mvc.models.DataSet;
import selab.mvc.models.entities.Course;
import selab.mvc.models.entities.Registration;
import selab.mvc.models.entities.Student;
import selab.mvc.views.JsonView;
import selab.mvc.views.View;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AddStudentToCourseController extends Controller {
    private DataSet<Course> courses;
    private DataSet<Student> students;
    private DataSet<Registration> registrations;

    public AddStudentToCourseController(DataContext dataContext) {
        super(dataContext);
        registrations = dataContext.getRegistrations();
        students = dataContext.getStudents();
        courses = dataContext.getCourses();
    }

    @Override
    public View exec(String method, InputStream body) throws IOException {
        if (!method.equals("POST"))
            throw new IOException("Method not supported");

        JSONObject input = readJson(body);
        String studentNo = input.getString("studentNo");
        String courseNo = input.getString("courseNo");
        String points = input.getString("points");

        Course course = courses.get(courseNo);
        Student student = students.get(studentNo);
        Registration registration = new Registration();
        registration.setCourse(course);
        registration.setStudent(student);
        registration.setPoint(Double.parseDouble(points));

        registrations.add(registration);
        course.addRegistration(registration);
        student.addRegistration(registration);

        Map<String, String> result = new HashMap<>();
        result.put("success", "true");
        return new JsonView(new JSONObject(result));
    }
}
