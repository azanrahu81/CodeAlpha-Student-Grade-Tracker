import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StudentGradeTracker_View extends JFrame {
    // Declare Swing components for Student Grade Tracker
    JTextField nameField, javaField, mathField, scienceField, phpField, totalField, averageField, gradeField;
    JButton calculateButton, viewButton;
    Color color;

    public StudentGradeTracker_View() {
        // Set up the JFrame
        setTitle("Student Grade Tracker");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Set the raised border of the frame
        getRootPane().setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

        // Background color
        color = new Color(173, 216, 230);
        getContentPane().setBackground(color);

        // Title label
        JLabel titleLabel = new JLabel("Student Grade Tracker");
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setBounds(150, 10, 200, 30);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED)); // Raised border for title
        add(titleLabel);

        // Name label and field
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(50, 60, 100, 30);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(150, 60, 250, 30);
        nameField.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED)); // Raised border for text field
        add(nameField);

        // Java marks label and field
        JLabel javaLabel = new JLabel("Java:");
        javaLabel.setBounds(50, 100, 100, 30);
        add(javaLabel);

        javaField = new JTextField();
        javaField.setBounds(150, 100, 250, 30);
        javaField.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED)); // Raised border for text field
        add(javaField);

        // Math marks label and field
        JLabel mathLabel = new JLabel("Math:");
        mathLabel.setBounds(50, 140, 100, 30);
        add(mathLabel);

        mathField = new JTextField();
        mathField.setBounds(150, 140, 250, 30);
        mathField.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED)); // Raised border for text field
        add(mathField);

        // Science marks label and field
        JLabel scienceLabel = new JLabel("Science:");
        scienceLabel.setBounds(50, 180, 100, 30);
        add(scienceLabel);

        scienceField = new JTextField();
        scienceField.setBounds(150, 180, 250, 30);
        scienceField.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED)); // Raised border for text field
        add(scienceField);

        // PHP marks label and field
        JLabel phpLabel = new JLabel("PHP:");
        phpLabel.setBounds(50, 220, 100, 30);
        add(phpLabel);

        phpField = new JTextField();
        phpField.setBounds(150, 220, 250, 30);
        phpField.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED)); // Raised border for text field
        add(phpField);

        // Total marks label and field
        JLabel totalLabel = new JLabel("Total:");
        totalLabel.setBounds(50, 260, 100, 30);
        add(totalLabel);

        totalField = new JTextField();
        totalField.setBounds(150, 260, 250, 30);
        totalField.setEditable(false);
        totalField.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED)); // Raised border for text field
        add(totalField);

        // Average marks label and field
        JLabel averageLabel = new JLabel("Average:");
        averageLabel.setBounds(50, 300, 100, 30);
        add(averageLabel);

        averageField = new JTextField();
        averageField.setBounds(150, 300, 250, 30);
        averageField.setEditable(false);
        averageField.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED)); // Raised border for text field
        add(averageField);

        // Grade label and field
        JLabel gradeLabel = new JLabel("Grade:");
        gradeLabel.setBounds(50, 340, 100, 30);
        add(gradeLabel);

        gradeField = new JTextField();
        gradeField.setBounds(150, 340, 250, 30);
        gradeField.setEditable(false);
        gradeField.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED)); // Raised border for text field
        add(gradeField);

        // Calculate button
        calculateButton = new JButton("Calculate");
        calculateButton.setBackground(color);
        calculateButton.setBounds(150, 380, 150, 30);
        calculateButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED)); // Raised border for button
        add(calculateButton);

        // View Data button
        viewButton = new JButton("View Data");
        viewButton.setBackground(color);
        viewButton.setBounds(150, 420, 150, 30);
        viewButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED)); // Raised border for button
        add(viewButton);

        // Add ActionListener to the calculate button
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateGrades();
            }
        });

        // Add ActionListener to the view button
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViewForm().setVisible(true);
            }
        });
    }

    // Method to calculate total, average, and grade
    private void calculateGrades() {
        try {
            String name = nameField.getText();
            int javaMarks = Integer.parseInt(javaField.getText());
            int mathMarks = Integer.parseInt(mathField.getText());
            int scienceMarks = Integer.parseInt(scienceField.getText());
            int phpMarks = Integer.parseInt(phpField.getText());

            int total = javaMarks + mathMarks + scienceMarks + phpMarks;
            double average = total / 4.0;

            String grade;
            if (average >= 90) {
                grade = "A";
            } else if (average >= 80) {
                grade = "B";
            } else if (average >= 70) {
                grade = "C";
            } else if (average >= 60) {
                grade = "D";
            } else {
                grade = "F";
            }

            totalField.setText(String.valueOf(total));
            averageField.setText(String.format("%.2f", average));
            gradeField.setText(grade);

            // Save data to the database
            saveToDatabase(name, javaMarks, mathMarks, scienceMarks, phpMarks, total, average, grade);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Please enter valid marks.");
        }
    }

    private void saveToDatabase(String name, int javaMarks, int mathMarks, int scienceMarks, int phpMarks, int total, double average, String grade) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // Connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Grade", "username", "password");

            // Insert the student data into the database
            String sql = "INSERT INTO grade (name, java, math, science, php, total, average, grade) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setInt(2, javaMarks);
            pstmt.setInt(3, mathMarks);
            pstmt.setInt(4, scienceMarks);
            pstmt.setInt(5, phpMarks);
            pstmt.setInt(6, total);
            pstmt.setDouble(7, average);
            pstmt.setString(8, grade);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close the database connections
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // ViewForm class to display student data
    private class ViewForm extends JFrame {
        JTable table;
        DefaultTableModel tableModel;
        Color color1;

        public ViewForm() {
            // Set up the JFrame
            setTitle("View Student Grades");
            setSize(500, 500);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLocationRelativeTo(null);

            color1 = new Color(173, 216, 230);
            getContentPane().setBackground(color1);

            // Set up the table
            tableModel = new DefaultTableModel();
            table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);
            add(scrollPane, BorderLayout.CENTER);
            setBackground(color1);

            // Add columns to the table model
            tableModel.addColumn("Name");
            tableModel.addColumn("Java");
            tableModel.addColumn("Math");
            tableModel.addColumn("Science");
            tableModel.addColumn("PHP");
            tableModel.addColumn("Total");
            tableModel.addColumn("Average");
            tableModel.addColumn("Grade");

            // Load data from the database
            loadData();
        }

        private void loadData() {
            Connection conn = null;
            Statement stmt = null;
            ResultSet rs = null;

            try {
                // Connect to the database
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Grade", "username", "password");

                // Execute a query to get all student data
                stmt = conn.createStatement();
                String sql = "SELECT * FROM grade";
                rs = stmt.executeQuery(sql);

                // Iterate through the result set and add rows to the table model
                while (rs.next()) {
                    Object[] row = new Object[8];
                    row[0] = rs.getString("name");
                    row[1] = rs.getInt("java");
                    row[2] = rs.getInt("math");
                    row[3] = rs.getInt("science");
                    row[4] = rs.getInt("php");
                    row[5] = rs.getInt("total");
                    row[6] = rs.getDouble("average");
                    row[7] = rs.getString("grade");
                    tableModel.addRow(row);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                // Close the database connections
                try {
                    if (rs != null) rs.close();
                    if (stmt != null) stmt.close();
                    if (conn != null) conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new StudentGradeTracker_View().setVisible(true);
            }
        });
    }
}
