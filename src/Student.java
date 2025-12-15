public class Student {
    private String studentId;
    private String name;
    private String phone;

    public Student(String studentId, String name, String phone) {
        this.studentId = studentId;
        this.name = name;
        this.phone = phone;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return String.format("ID: %s, Name: %s, Phone: %s", studentId, name, phone);
    }
}
