import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class Main {
    private JFrame frame;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private StudentManager manager = new StudentManager();

    private JTextField nameField;
    private JTextField phoneField;
    private JLabel statusLabel;
    private JTextArea listArea;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main m = new Main();
            m.createAndShowGUI();
        });
    }

    private void createAndShowGUI() {
        frame = new JFrame("Student Manager (Swing) - 40312013");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 450);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(buildAddPanel(), "add");
        mainPanel.add(buildListPanel(), "list");

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton toAdd = new JButton("Add Student");
        JButton toList = new JButton("List / Save / Load");
        toAdd.addActionListener(e -> show("add"));
        toList.addActionListener(e -> { refreshList(); show("list"); });
        top.add(toAdd);
        top.add(toList);

        frame.add(top, BorderLayout.NORTH);
        frame.add(mainPanel, BorderLayout.CENTER);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel buildAddPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(20);
        panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Phone (11 digits, starts 09):"), gbc);
        gbc.gridx = 1;
        phoneField = new JTextField(20);
        panel.add(phoneField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addBtn = new JButton("Add");
        JButton goList = new JButton("Go to List");
        btns.add(addBtn);
        btns.add(goList);
        panel.add(btns, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.BLUE);
        panel.add(statusLabel, gbc);

        addBtn.addActionListener(this::onAddClicked);
        goList.addActionListener(e -> { refreshList(); show("list"); });

        return panel;
    }

    private JPanel buildListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        listArea = new JTextArea();
        listArea.setEditable(false);
        JScrollPane sp = new JScrollPane(listArea);
        panel.add(sp, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refresh = new JButton("Refresh");
        JButton saveCSV = new JButton("Save CSV");
        JButton saveTXT = new JButton("Save TXT");
        JButton saveJSON = new JButton("Save JSON");
        JButton importCSV = new JButton("Import CSV");
        JButton back = new JButton("Back");

        bottom.add(refresh);
        bottom.add(saveCSV);
        bottom.add(saveTXT);
        bottom.add(saveJSON);
        bottom.add(importCSV);
        bottom.add(back);

        refresh.addActionListener(e -> refreshList());
        saveCSV.addActionListener(e -> {
            try {
                manager.saveToCSV("students.csv");
                JOptionPane.showMessageDialog(frame, "Saved students.csv");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error saving CSV: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        saveTXT.addActionListener(e -> {
            try {
                manager.saveToTXT("students.txt");
                JOptionPane.showMessageDialog(frame, "Saved students.txt");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error saving TXT: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        saveJSON.addActionListener(e -> {
            try {
                manager.saveToJSON("students.json");
                JOptionPane.showMessageDialog(frame, "Saved students.json");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error saving JSON: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        importCSV.addActionListener(e -> {
            try {
                manager.loadFromCSV("students.csv");
                refreshList();
                JOptionPane.showMessageDialog(frame, "Imported students.csv");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error importing CSV: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        back.addActionListener(e -> show("add"));

        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    private void onAddClicked(ActionEvent e) {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Name cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            statusLabel.setText("Name cannot be empty");
            statusLabel.setForeground(Color.RED);
            return;
        }

        if (!isValidPhone(phone)) {
            JOptionPane.showMessageDialog(frame, "Phone must be 11 digits and start with 09", "Error", JOptionPane.ERROR_MESSAGE);
            statusLabel.setText("Invalid phone");
            statusLabel.setForeground(Color.RED);
            return;
        }

        manager.addStudent(name, phone);
        statusLabel.setText("Added student");
        statusLabel.setForeground(new Color(0, 128, 0));
        nameField.setText("");
        phoneField.setText("");
    }

    private void refreshList() {
        listArea.setText(manager.allStudentsAsText());
    }

    private boolean isValidPhone(String p) {
        if (p.length() != 11) return false;
        if (!p.startsWith("09")) return false;
        for (int i = 0; i < p.length(); i++) {
            if (!Character.isDigit(p.charAt(i))) return false;
        }
        return true;
    }

    private void show(String name) {
        cardLayout.show(mainPanel, name);
    }
}
