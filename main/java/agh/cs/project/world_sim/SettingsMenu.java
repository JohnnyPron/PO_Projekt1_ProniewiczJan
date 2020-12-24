package agh.cs.project.world_sim;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsMenu extends JFrame implements ActionListener {
    private JTextField widthText;
    private JTextField heightText;
    private JTextField jungleRatioText;
    private JTextField animNumText;
    private JTextField grassNumText;
    private JTextField startEnergyText;
    private JTextField moveEnergyText;
    private JTextField plantEnergyText;


    public SettingsMenu(Integer[] parameters){
        // ustawianie właściwości samego okna
        this.setTitle("Evolution Simulator");
        setSize(500, 290);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        // ustawienia dla panelu
        // ustawianie właściwości panelu głównego
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        JLabel instruction = new JLabel("Set positive values to the parameters", SwingConstants.CENTER);
        instruction.setPreferredSize(new Dimension(500, 50));
        mainPanel.add(instruction, BorderLayout.NORTH);
        // ustawienie właściwości paneli podrzędnych
        JPanel subPanel1 = new JPanel();
        JPanel subPanel2 = new JPanel();
        JPanel subPanel3 = new JPanel();
        subPanel1.setLayout(new BoxLayout(subPanel1, BoxLayout.Y_AXIS));
        subPanel2.setLayout(new BoxLayout(subPanel2, BoxLayout.Y_AXIS));
        subPanel3.setLayout(new BoxLayout(subPanel3, BoxLayout.Y_AXIS));
        // ustawianie etykiet i pól tekstowych w kolejnych mniejszych panelach
        int columns = 5;  // ustalenie szerokości dla pól tekstowych
        // szerokość mapy
        JPanel width = new JPanel();
        JLabel widthLabel = new JLabel("Map width: ");
        widthText = new JTextField();
        widthText.setColumns(columns);
        widthText.setText(parameters[0].toString());
        widthLabel.setLabelFor(widthText);
        width.add(widthLabel);
        width.add(widthText);
        subPanel1.add(width);
        // długość mapy
        JPanel height = new JPanel();
        JLabel heightLabel = new JLabel("Map height: ");
        heightText = new JTextField();
        heightText.setColumns(columns);
        heightText.setText(parameters[1].toString());
        heightLabel.setLabelFor(heightText);
        height.add(heightLabel);
        height.add(heightText);
        subPanel1.add(height);
        // proporcje dżungli do stepu
        JPanel jungleRatio = new JPanel();
        JLabel jungleRatioLabel = new JLabel("Jungle ratio: ");
        jungleRatioText = new JTextField();
        jungleRatioText.setColumns(columns);
        float jRatio = 1f / (float)parameters[2];
        jungleRatioText.setText(String.valueOf(jRatio));
        jungleRatioLabel.setLabelFor(jungleRatioText);
        jungleRatio.add(jungleRatioLabel);
        jungleRatio.add(jungleRatioText);
        subPanel1.add(jungleRatio);
        // liczba początkowych zwierząt na mapie
        JPanel animNum = new JPanel();
        JLabel animNumLabel = new JLabel("<html><div style='text-align:right'>Num. of initial<br/>animals:</div></html>");
        animNumText = new JTextField();
        animNumText.setColumns(columns);
        animNumText.setText(parameters[3].toString());
        animNumLabel.setLabelFor(animNumText);
        animNum.add(animNumLabel);
        animNum.add(animNumText);
        subPanel2.add(animNum);
        // liczba początkowych kępek trawy na mapie
        JPanel grassNum = new JPanel();
        JLabel grassNumLabel = new JLabel("<html><div style='text-align:right'>Num. of initial<br/>grass tufts:</div></html>");
        grassNumText = new JTextField();
        grassNumText.setColumns(columns);
        grassNumText.setText(parameters[4].toString());
        grassNumLabel.setLabelFor(grassNumText);
        grassNum.add(grassNumLabel);
        grassNum.add(grassNumText);
        subPanel2.add(grassNum);
        // energia początkowa zwierząt
        JPanel startEnergy = new JPanel();
        JLabel startEnergyLabel = new JLabel("<html><div style='text-align:right'>Start animal<br/>energy:</div></html>");
        startEnergyText = new JTextField();
        startEnergyText.setColumns(columns);
        startEnergyText.setText(parameters[5].toString());
        startEnergyLabel.setLabelFor(startEnergyText);
        startEnergy.add(startEnergyLabel);
        startEnergy.add(startEnergyText);
        subPanel3.add(startEnergy);
        // energia, jaką zwierzęta tracą podczas ruchu
        JPanel moveEnergy = new JPanel();
        JLabel moveEnergyLabel = new JLabel("<html><div style='text-align:right'>Amount of energy<br/>lost by moving:</div></html>");
        moveEnergyText = new JTextField();
        moveEnergyText.setColumns(columns);
        moveEnergyText.setText(parameters[6].toString());
        moveEnergyLabel.setLabelFor(moveEnergyText);
        moveEnergy.add(moveEnergyLabel);
        moveEnergy.add(moveEnergyText);
        subPanel3.add(moveEnergy);
        // energia, otrzymywana po zjedzeniu rośliny
        JPanel plantEnergy = new JPanel();
        JLabel plantEnergyLabel = new JLabel("<html><div style='text-align:right'>Energy provided<br/>by grass:</div></html>");
        plantEnergyText = new JTextField();
        plantEnergyText.setColumns(columns);
        plantEnergyText.setText(parameters[7].toString());
        plantEnergyLabel.setLabelFor(plantEnergyText);
        plantEnergy.add(plantEnergyLabel);
        plantEnergy.add(plantEnergyText);
        subPanel3.add(plantEnergy);
        // dodanie podrzędnych paneli do panelu głównego
        mainPanel.add(subPanel1, BorderLayout.WEST);
        mainPanel.add(subPanel2, BorderLayout.CENTER);
        mainPanel.add(subPanel3, BorderLayout.EAST);
        // dodanie guzika "start"
        JButton start = new JButton("Start");
        start.addActionListener(this);
        mainPanel.add(start, BorderLayout.SOUTH);
        // dodanie panelu do okna
        add(mainPanel);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        try{
            SimulationEngine simulator = new SimulationEngine(
                    Integer.parseInt(widthText.getText()),
                    Integer.parseInt(heightText.getText()),
                    Integer.parseInt(animNumText.getText()),
                    Integer.parseInt(grassNumText.getText()),
                    Integer.parseInt(startEnergyText.getText()),
                    Integer.parseInt(moveEnergyText.getText()),
                    Integer.parseInt(plantEnergyText.getText()),
                    Float.parseFloat(jungleRatioText.getText()));
            simulator.startSimulation();
        }catch (IllegalArgumentException ex){
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Oops!", JOptionPane.ERROR_MESSAGE);
        }
    }
}
