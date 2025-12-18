import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class Main {
    public static void main(String[] args) {
        StudentManager mgr = new StudentManager();
        JFrame frame = new JFrame("Student Manager (Swing) - 40312013");
        CardLayout card = new CardLayout();
        JPanel mainPanel = new JPanel(card);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        form.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        JTextField nameField = new JTextField(20);
        form.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        form.add(new JLabel("Phone (11 digits, starts 09):"), gbc);
        gbc.gridx = 1;
        JTextField phoneField = new JTextField(20);
        form.add(phoneField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addBtn = new JButton("Add");
        JButton gotoList = new JButton("Go to List");
        btns.add(addBtn); btns.add(gotoList);
        form.add(btns, gbc);

        gbc.gridy = 3;
        JLabel status = new JLabel("");
        form.add(status, gbc);

        JPanel listPanel = new JPanel(new BorderLayout());
        JTextArea listArea = new JTextArea();
        listArea.setEditable(false);
        JScrollPane sp = new JScrollPane(listArea);
        listPanel.add(sp, BorderLayout.CENTER);

        JPanel listTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backBtn = new JButton("Back");
        JButton saveBtn = new JButton("Save");
        JButton loadBtn = new JButton("Load");
        listTop.add(backBtn); listTop.add(saveBtn); listTop.add(loadBtn);
        listPanel.add(listTop, BorderLayout.NORTH);

        addBtn.addActionListener(e -> {
            String n = nameField.getText().trim();
            String p = phoneField.getText().trim();
            if (n.isEmpty()) { status.setText("Name is required"); status.setForeground(Color.RED); return; }
            if (p.length() != 11 || !p.startsWith("09") || !p.chars().allMatch(Character::isDigit)) { status.setText("Invalid phone"); status.setForeground(Color.RED); return; }
            mgr.add(new Student(n, p));
            status.setText("Added"); status.setForeground(Color.GREEN);
            nameField.setText(""); phoneField.setText("");
        });

        gotoList.addActionListener(e -> {
            updateListArea(listArea, mgr);
            card.show(mainPanel, "list");
        });

        backBtn.addActionListener(e -> card.show(mainPanel, "form"));

        saveBtn.addActionListener(e -> {
            try { mgr.saveToFile("students.csv"); JOptionPane.showMessageDialog(frame, "Saved"); } catch (Exception ex) { JOptionPane.showMessageDialog(frame, "Save error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
        });

        loadBtn.addActionListener(e -> {
            try { mgr.loadFromFile("students.csv"); updateListArea(listArea, mgr); JOptionPane.showMessageDialog(frame, "Loaded"); } catch (Exception ex) { JOptionPane.showMessageDialog(frame, "Load error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
        });

        mainPanel.add(form, "form");
        mainPanel.add(listPanel, "list");
        frame.add(mainPanel);
        frame.setSize(700, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    static void updateListArea(JTextArea area, StudentManager mgr) {
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (Student s : mgr.getAll()) sb.append(i++).append(". ").append(s.toString()).append("\n");
        area.setText(sb.toString());
    }
}
