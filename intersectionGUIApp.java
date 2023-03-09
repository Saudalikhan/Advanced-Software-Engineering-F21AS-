import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class IntersectionSimulationGUI extends JFrame implements ActionListener {
    // GUI components
    private JTable vehicleTable;
    private JTable phaseTable;
    private JTable statisticsTable;
    private JTextField vehicleTextField;
    private JTextField typeTextField;
    private JTextField crossingTimeTextField;
    private JTextField directionTextField;
    private JTextField lengthTextField;
    private JTextField emissionTextField;
    private JTextField statusTextField;
    private JTextField segmentTextField;
    private JButton addButton;
    private JButton cancelButton;
    private JButton exitButton;

    // Data structures for holding intersection data
    private ArrayList<Phase> phases;
    private ArrayList<Vehicle> vehicles;
    private ArrayList<SegmentStatistics> statistics;

    // DefaultTableModel instances for the tables
    private DefaultTableModel vehicleTableModel;
    private DefaultTableModel phaseTableModel;
    private DefaultTableModel statisticsTableModel;

    // Constructor
    public IntersectionSimulationGUI() {
        // Set the window title
        setTitle("Intersection Simulation");

        // Initialize the GUI components
        vehicleTable = new JTable();
        phaseTable = new JTable();
        statisticsTable = new JTable();
        vehicleTextField = new JTextField();
        typeTextField = new JTextField();
        crossingTimeTextField = new JTextField();
        directionTextField = new JTextField();
        lengthTextField = new JTextField();
        emissionTextField = new JTextField();
        statusTextField = new JTextField();
        segmentTextField = new JTextField();
        addButton = new JButton("Add");
        cancelButton = new JButton("Cancel");
        exitButton = new JButton("Exit");

        // Set the layout manager
        setLayout(new BorderLayout());

        // Create panels for each section of the GUI
        JPanel vehiclePanel = new JPanel();
        JPanel phasePanel = new JPanel();
        JPanel statisticsPanel = new JPanel();
        JPanel inputPanel = new JPanel();
        JPanel buttonPanel = new JPanel();

        // Set the layout managers for the panels
        vehiclePanel.setLayout(new BorderLayout());
        phasePanel.setLayout(new BorderLayout());
        statisticsPanel.setLayout(new BorderLayout());
        inputPanel.setLayout(new GridLayout(8, 2));
        buttonPanel.setLayout(new FlowLayout());

        // Add the components to the input panel
        inputPanel.add(new JLabel("Vehicle:"));
        inputPanel.add(vehicleTextField);
        inputPanel.add(new JLabel("Type:"));
        inputPanel.add(typeTextField);
        inputPanel.add(new JLabel("Crossing Time:"));
        inputPanel.add(crossingTimeTextField);
        inputPanel.add(new JLabel("Direction:"));
        inputPanel.add(directionTextField);
        inputPanel.add(new JLabel("Length:"));
        inputPanel.add(lengthTextField);
        inputPanel.add(new JLabel("Emission:"));
        inputPanel.add(emissionTextField);
        inputPanel.add(new JLabel("Status:"));
        inputPanel.add(statusTextField);
        inputPanel.add(new JLabel("Segment:"));
        inputPanel.add(segmentTextField);

        // Add the components to the button panel
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(exitButton);

        // Add the input panel and button panel to the south of the window
        add(inputPanel, BorderLayout.SOUTH);
        add(buttonPanel, BorderLayout.PAGE_END);

        // Read the intersection data from the file
        phases = new ArrayList<Phase>();
        vehicles = new ArrayList<Vehicle>();
        statistics = new ArrayList<SegmentStatistics>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("intersection.csv"));
            String line = br.readLine();
            while (line != null) {
                String[] parts=line.split(",");
                            // Check if the line contains phase data
            if (parts.length == 2) {
                // Create a new phase object and add it to the list of phases
                Phase phase = new Phase(parts[0], Integer.parseInt(parts[1]));
                phases.add(phase);
            } else if (parts.length == 8) {
                // Create a new vehicle object and add it to the list of vehicles
                Vehicle vehicle = new Vehicle(parts[0], parts[1], Integer.parseInt(parts[2]),
                        parts[3], Double.parseDouble(parts[4]), Double.parseDouble(parts[5]),
                        parts[6], Integer.parseInt(parts[7]));
                vehicles.add(vehicle);
            } else if (parts.length == 4) {
                // Create a new segment statistics object and add it to the list of statistics
                SegmentStatistics segmentStatistics = new SegmentStatistics(parts[0],
                        Double.parseDouble(parts[1]), Double.parseDouble(parts[2]),
                        Integer.parseInt(parts[3]));
                statistics.add(segmentStatistics);
            }

            // Read the next line
            line = br.readLine();
        }
        br.close();
    } catch (IOException e) {
        // Display an error message if there was a problem reading the file
        JOptionPane.showMessageDialog(this, "Error reading intersection data file.", "Error",
                JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }

    // Set up the vehicle table
    vehicleTableModel = new DefaultTableModel();
    vehicleTableModel.addColumn("Vehicle");
    vehicleTableModel.addColumn("Type");
    vehicleTableModel.addColumn("Crossing Time");
    vehicleTableModel.addColumn("Direction");
    vehicleTableModel.addColumn("Length");
    vehicleTableModel.addColumn("Emission");
    vehicleTableModel.addColumn("Status");
    vehicleTableModel.addColumn("Segment");
    vehicleTable.setModel(vehicleTableModel);
    vehiclePanel.add(new JScrollPane(vehicleTable), BorderLayout.CENTER);
    vehiclePanel.add(new JLabel("Vehicles"), BorderLayout.NORTH);
    add(vehiclePanel, BorderLayout.CENTER);

    // Set up the phase table
    phaseTableModel = new DefaultTableModel();
    phaseTableModel.addColumn("Phase");
    phaseTableModel.addColumn("Duration");
    phaseTable.setModel(phaseTableModel);
    phasePanel.add(new JScrollPane(phaseTable), BorderLayout.CENTER);
    phasePanel.add(new JLabel("Phases"), BorderLayout.NORTH);
    add(phasePanel, BorderLayout.WEST);

    // Set up the statistics table
    statisticsTableModel = new DefaultTableModel();
    statisticsTableModel.addColumn("Segment");
    statisticsTableModel.addColumn("Waiting Time");
    statisticsTableModel.addColumn("Waiting Length");
    statisticsTableModel.addColumn("Cross Time");
    statisticsTable.setModel(statisticsTableModel);
    statisticsPanel.add(new JScrollPane(statisticsTable), BorderLayout.CENTER);
    statisticsPanel.add(new JLabel("Statistics"), BorderLayout.NORTH);
    add(statisticsPanel, BorderLayout.EAST);

    // Set the size of the window and make it visible
    setSize(1000, 500);
    setVisible(true);
}

// Handle button clicks
public void actionPerformed(ActionEvent e) {
    if (e.getSource() == addButton) {
        // Add the new vehicle to the list of vehicles and update the vehicle table
        Vehicle vehicle = new Vehicle(vehicleTextField.getText(), typeTextField.getText(),
                Integer.parseInt(crossingTimeTextField.getText()), directionTextField.getText(),
                Double.parseDouble(lengthTextField.getText()), Double.parseDouble(emissionTextField.getText()),
                statusTextField.getText(), Integer.parseInt(segmentTextField.getText()));
        vehicles.add(vehicle);
        vehicleTableModel.addRow(new Object[] {vehicle.getVehicle(), vehicle.getType(),
                vehicle.getCrossingTime(), vehicle.getDirection(), vehicle.getLength(), vehicle.getEmission(),
                vehicle.getStatus(), vehicle.getSegment()});
    } else if (e.getSource() == cancelButton) {
        // Clear the input fields
        vehicleTextField.setText("");
        typeTextField.setText("");
       


