package pero;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TodoListApp extends JFrame {
    private DefaultListModel<Task> taskListModel;
    private JList<Task> taskList;
    private JTextField taskField;
    private JCheckBox dateCheckBox;
    private JCheckBox timeCheckBox;
    private JTextField dateField;
    private JTextField timeField;
    private JButton addButton;
    private JButton removeButton;
    private JButton saveButton;

    private static final String FILE_NAME = "tasks.txt";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public TodoListApp() {
        initializeComponents();
        loadTasksFromFile();
        setupLayout();
        setupListeners();
        setFrameProperties();
    }

    private void initializeComponents() {
        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        taskList.setCellRenderer(new TaskCellRenderer());
        taskField = new JTextField(20);
        dateCheckBox = new JCheckBox("Include Date");
        timeCheckBox = new JCheckBox("Include Time");
        dateField = new JTextField(10);
        timeField = new JTextField(5);
        dateField.setEnabled(false);
        timeField.setEnabled(false);
        addButton = new JButton("Add Task");
        removeButton = new JButton("Remove Task");
        saveButton = new JButton("Save Tasks");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        add(new JScrollPane(taskList), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.add(new JLabel("Task:"));
        panel.add(taskField);
        panel.add(dateCheckBox);
        panel.add(dateField);
        panel.add(timeCheckBox);
        panel.add(timeField);
        panel.add(addButton);
        panel.add(removeButton);
        panel.add(saveButton);
        add(panel, BorderLayout.SOUTH);
    }

    private void setupListeners() {
        dateCheckBox.addActionListener(e -> dateField.setEnabled(dateCheckBox.isSelected()));
        timeCheckBox.addActionListener(e -> timeField.setEnabled(timeCheckBox.isSelected()));

        addButton.addActionListener(this::handleAddButton);
        removeButton.addActionListener(this::handleRemoveButton);
        saveButton.addActionListener(this::handleSaveButton);
    }

    private void handleAddButton(ActionEvent e) {
        String taskText = taskField.getText();
        LocalDateTime dateTime = null;

        if (!taskText.isEmpty()) {
            try {
                if (dateCheckBox.isSelected() && !dateField.getText().isEmpty()) {
                    LocalDate date = LocalDate.parse(dateField.getText(), DATE_FORMATTER);
                    LocalTime time = LocalTime.MIN;
                    if (timeCheckBox.isSelected() && !timeField.getText().isEmpty()) {
                        time = LocalTime.parse(timeField.getText(), TIME_FORMATTER);
                    }
                    dateTime = LocalDateTime.of(date, time);
                }
                Task task = new Task(taskText, dateTime);
                taskListModel.addElement(task);
                sortTasks();
                clearInputFields();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid date or time format.");
            }
        }
    }

    private void handleRemoveButton(ActionEvent e) {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            taskListModel.remove(selectedIndex);
        }
    }

    private void handleSaveButton(ActionEvent e) {
        saveTasksToFile();
    }

    private void clearInputFields() {
        taskField.setText("");
        dateField.setText("");
        timeField.setText("");
    }

    private void sortTasks() {
        List<Task> tasks = Collections.list(taskListModel.elements());
        tasks.sort(Comparator.comparing(Task::getDateTime, Comparator.nullsLast(Comparator.naturalOrder())));
        taskListModel.clear();
        for (Task task : tasks) {
            taskListModel.addElement(task);
        }
    }

    private void loadTasksFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";", 2);
                LocalDateTime dateTime = parts[0].isEmpty() ? null : LocalDateTime.parse(parts[0], DATE_TIME_FORMATTER);
                String taskText = parts[1];
                taskListModel.addElement(new Task(taskText, dateTime));
            }
            sortTasks();
        } catch (IOException e) {
            System.err.println("Error loading tasks: " + e.getMessage());
        }
    }

    private void saveTasksToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (int i = 0; i < taskListModel.getSize(); i++) {
                Task task = taskListModel.getElementAt(i);
                bw.write((task.getDateTime() == null ? "" : task.getDateTime().format(DATE_TIME_FORMATTER)) + ";" + task.getTask());
                bw.newLine();
            }
            JOptionPane.showMessageDialog(this, "Tasks saved successfully.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving tasks: " + e.getMessage());
        }
    }

    private void setFrameProperties() {
        setTitle("To-Do List");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TodoListApp::new);
    }
}

class Task {
    private final String task;
    private final LocalDateTime dateTime;

    public Task(String task, LocalDateTime dateTime) {
        this.task = task;
        this.dateTime = dateTime;
    }

    public String getTask() {
        return task;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public String toString() {
        return (dateTime == null ? "No Date/Time" : dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))) + " - " + task;
    }
}

class TaskCellRenderer extends JLabel implements ListCellRenderer<Task> {
    @Override
    public Component getListCellRendererComponent(JList<? extends Task> list, Task value, int index, boolean isSelected, boolean cellHasFocus) {
        setText(value.toString());
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        setOpaque(true);
        return this;
    }
}
