import java.util.*;
import java.io.*;
public class StudentManager {
    private List<Student> students = new ArrayList<>();
    public void add(Student s) { students.add(s); }
    public List<Student> getAll() { return new ArrayList<>(students); }
    public void saveToFile(String path) throws IOException {
        try (FileWriter fw = new FileWriter(path)) {
            for (Student s : students) fw.write(s.getName() + "," + s.getPhone() + "\n");
        }
    }
    public void loadFromFile(String path) throws IOException {
        students.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] a = line.split(",", 2);
                if (a.length == 2) students.add(new Student(a[0], a[1]));
            }
        }
    }
}
