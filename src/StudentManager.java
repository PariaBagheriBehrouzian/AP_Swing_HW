import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StudentManager {
    private List<Student> students = new ArrayList<>();
    private int counter = 1;

    public String generateId() {
        String id = String.format("S%03d", counter);
        counter++;
        return id;
    }

    public void addStudent(Student s) {
        students.add(s);
    }

    public void addStudent(String name, String phone) {
        students.add(new Student(generateId(), name, phone));
    }

    public List<Student> getStudents() {
        return students;
    }

    public String allStudentsAsText() {
        StringBuilder sb = new StringBuilder();
        for (Student s : students) {
            sb.append(s.toString()).append(System.lineSeparator());
        }
        return sb.toString();
    }

    public void saveToCSV(String filename) throws IOException {
        try (FileWriter fw = new FileWriter(filename)) {
            fw.write("studentId,name,phone\n");
            for (Student s : students) {
                fw.write(String.format("%s,%s,%s\n",
                        escapeCsv(s.getStudentId()),
                        escapeCsv(s.getName()),
                        escapeCsv(s.getPhone())));
            }
        }
    }

    private String escapeCsv(String v) {
        if (v.contains(",") || v.contains("\"")) {
            return "\"" + v.replace("\"", "\"\"") + "\"";
        }
        return v;
    }

    public void saveToTXT(String filename) throws IOException {
        try (FileWriter fw = new FileWriter(filename)) {
            fw.write(allStudentsAsText());
        }
    }

    public void saveToJSON(String filename) throws IOException {
        try (FileWriter fw = new FileWriter(filename)) {
            fw.write("{\n  \"students\": [\n");
            for (int i = 0; i < students.size(); i++) {
                Student s = students.get(i);
                fw.write("    {\n");
                fw.write("      \"studentId\": \"" + jsonEscape(s.getStudentId()) + "\",\n");
                fw.write("      \"name\": \"" + jsonEscape(s.getName()) + "\",\n");
                fw.write("      \"phone\": \"" + jsonEscape(s.getPhone()) + "\"\n");
                fw.write("    }");
                if (i < students.size() - 1) fw.write(",");
                fw.write("\n");
            }
            fw.write("  ]\n}\n");
        }
    }

    private String jsonEscape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    public void loadFromCSV(String filename) throws IOException {
        File f = new File(filename);
        if (!f.exists()) throw new FileNotFoundException(filename + " not found");
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String header = br.readLine(); 
            String line;
            int maxIdNum = this.counter - 1;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = splitCsvLine(line);
                if (parts.length >= 3) {
                    String id = parts[0];
                    String name = parts[1];
                    String phone = parts[2];
                    students.add(new Student(id, name, phone));
                    if (id.startsWith("S")) {
                        try {
                            int num = Integer.parseInt(id.substring(1));
                            if (num > maxIdNum) maxIdNum = num;
                        } catch (Exception ignored) {}
                    }
                }
            }
            this.counter = maxIdNum + 1;
        }
    }

    private String[] splitCsvLine(String line) {
        List<String> cols = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    cur.append('"'); 
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                cols.add(cur.toString());
                cur.setLength(0);
            } else {
                cur.append(c);
            }
        }
        cols.add(cur.toString());
        return cols.toArray(new String[0]);
    }
}
