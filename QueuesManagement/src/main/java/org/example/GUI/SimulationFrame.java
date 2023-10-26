package org.example.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SimulationFrame implements ActionListener {
    JFrame window = new JFrame("Queue Management System");
    private JLabel numClientsLabel, numQueuesLabel, simIntervalLabel, minArrivalLabel, maxArrivalLabel, minServiceLabel, maxServiceLabel;
    private JTextField numClientsField, numQueuesField, simIntervalField, minArrivalField, maxArrivalField, minServiceField, maxServiceField;
    private JButton startButton;
    ArrayList<Integer> parameters;
    JTextArea log;
    volatile boolean inputValid;

    public SimulationFrame() {

        inputValid = false;
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(500, 350);
        window.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(7, 2));

        numClientsLabel = new JLabel("Number of clients (N): ");
        numClientsField = new JTextField();
        inputPanel.add(numClientsLabel);
        inputPanel.add(numClientsField);

        numQueuesLabel = new JLabel("Number of queues (Q): ");
        numQueuesField = new JTextField();
        inputPanel.add(numQueuesLabel);
        inputPanel.add(numQueuesField);

        simIntervalLabel = new JLabel("Simulation interval (MAX): ");
        simIntervalField = new JTextField();
        inputPanel.add(simIntervalLabel);
        inputPanel.add(simIntervalField);

        minArrivalLabel = new JLabel("Minimum arrival time (MIN): ");
        minArrivalField = new JTextField();
        inputPanel.add(minArrivalLabel);
        inputPanel.add(minArrivalField);

        maxArrivalLabel = new JLabel("Maximum arrival time (MAX): ");
        maxArrivalField = new JTextField();
        inputPanel.add(maxArrivalLabel);
        inputPanel.add(maxArrivalField);

        minServiceLabel = new JLabel("Minimum service time (MIN): ");
        minServiceField = new JTextField();
        inputPanel.add(minServiceLabel);
        inputPanel.add(minServiceField);

        maxServiceLabel = new JLabel("Maximum service time (MAX): ");
        maxServiceField = new JTextField();
        inputPanel.add(maxServiceLabel);
        inputPanel.add(maxServiceField);

        window.add(inputPanel, BorderLayout.CENTER);

        startButton = new JButton("Start Simulation");
        window.add(startButton, BorderLayout.SOUTH);
        startButton.addActionListener(this);

        window.setVisible(true);

        parameters = new ArrayList<>();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (numClientsField.getText().matches(".*[^0-9].*|^$")) {
                JOptionPane.showMessageDialog(new JFrame(), "Bad Input numClientsField ",
                        "Info", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (numQueuesField.getText().matches(".*[^0-9].*|^$")) {
                JOptionPane.showMessageDialog(new JFrame(), "Bad Input numQueuesField ",
                        "Info", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (simIntervalField.getText().matches(".*[^0-9].*|^$")) {
                JOptionPane.showMessageDialog(new JFrame(), "Bad Input simIntervalField ",
                        "Info", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (minServiceField.getText().matches(".*[^0-9].*|^$")) {
                JOptionPane.showMessageDialog(new JFrame(), "Bad Input minServiceField ",
                        "Info", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (maxArrivalField.getText().matches(".*[^0-9].*|^$")) {
                JOptionPane.showMessageDialog(new JFrame(), "Bad Input maxArrivalField ",
                        "Info", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (minArrivalField.getText().matches(".*[^0-9].*|^$")) {
                JOptionPane.showMessageDialog(new JFrame(), "Bad Input minArrivalField ",
                        "Info", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (maxServiceField.getText().matches(".*[^0-9].*|^$")) {
                JOptionPane.showMessageDialog(new JFrame(), "Bad Input maxServiceField ",
                        "Info", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException exception) {
            System.out.println("Bad input");
        }

        int numClients = Integer.parseInt(numClientsField.getText());
        int numQueues = Integer.parseInt(numQueuesField.getText());
        int simInterval = Integer.parseInt(simIntervalField.getText());
        int minArrival = Integer.parseInt(minArrivalField.getText());
        int maxArrival = Integer.parseInt(maxArrivalField.getText());
        int minService = Integer.parseInt(minServiceField.getText());
        int maxService = Integer.parseInt(maxServiceField.getText());
        parameters.add(numClients);
        parameters.add(numQueues);
        parameters.add(simInterval);
        parameters.add(minArrival);
        parameters.add(maxArrival);
        parameters.add(minService);
        parameters.add(maxService);
        inputValid = true;

    }

    public void simFrame() {
        window.dispose();
        window = new JFrame("Simulation has started");
        window.setSize(750, 500);
        window.setLayout(null);
        log = new JTextArea();
        log.setEditable(false);
        log.setBounds(50, 50, 600, 300);
        window.add(log);
        window.setVisible(true);
    }

    public void showProgress(String t) {
        log.setText(t);
    }

    public ArrayList<Integer> getParameters() {
        while (!inputValid) {
            Thread.onSpinWait();
            //wait for inputValid
        }
        simFrame();
        return parameters;
    }
}