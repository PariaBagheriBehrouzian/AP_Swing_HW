public class Student {
    private String name;
    private String phone;
    public Student(String n, String p) { this.name = n; this.phone = p; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String toString() { return "Name: " + name + ", Phone: " + phone; }
}
