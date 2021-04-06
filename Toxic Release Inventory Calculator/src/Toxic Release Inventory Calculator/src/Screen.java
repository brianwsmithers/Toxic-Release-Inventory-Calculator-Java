import javax.swing.*;

public class Screen {
    private JPanel topPanel;
    private JTextField specificGravityTextField;
    private JTextField concentrationTextField;
    private JLabel specificGravityLabel;
    private JLabel concentrationLabel;
    private JLabel canSizeLabel;
    private JRadioButton a12FlOzRadioButton;
    private JRadioButton a15FlOzRadioButton;
    private JLabel numberOfUnitsLabel;
    private JTextField numberOfUnitsTextField;
    private JButton calculateButton;
    private JButton clearButton;
    private JCheckBox gallonsCheckBox;
    private JLabel thresholdLabel;
    private JRadioButton a10000LbsRadioButton;
    private JRadioButton a25000LbsRadioButton;
    private JCheckBox poundsCheckBox;

    private double units; // this variable is the final calculated value
    private double poundsPerGallon;
    double specificGravity;
    double concentration;

    // default threshold value is to calculate for 10,000 lbs, alternative is 25,000 lbs.
    private double threshold;

    // Action listeners
    public Screen() {
        // Calculate button calculates the number of units needed depending on the checkbox and/or radiobutton selected
        calculateButton.addActionListener(e -> {
            textFieldCalculatorCheck();
        });
        // 12 fl. oz. can calculation
        a12FlOzRadioButton.addActionListener(e -> {
            unitSelector(true,false,false,false);
            resetMainVariables();
            textFieldCalculatorCheck();
        });
        // 15 fl. oz. can calculation
        a15FlOzRadioButton.addActionListener(e -> {
            unitSelector(false,true,false,false);
            resetMainVariables();
            textFieldCalculatorCheck();
        });
        // clear all text fields
        clearButton.addActionListener(e -> {
            specificGravityTextField.setText("");
            concentrationTextField.setText("");
            numberOfUnitsTextField.setText("");
            resetMainVariables();
        });
        // Changes calculation to gallons.
        gallonsCheckBox.addActionListener(e -> {
            unitSelector(false,false,true,false);
            resetMainVariables();
            textFieldCalculatorCheck();
        });
        // Changes threshold calculation to 10,000 lbs
        a10000LbsRadioButton.addActionListener(e -> {
            setThresholdSelection(true, false);
            resetMainVariables();
            helperCalculator(); // calculates based on radiobutton or checkbox selection
        });
        // Changes threshold calculation to 25,000 lbs
        a25000LbsRadioButton.addActionListener(e -> {
            setThresholdSelection(false, true);
            resetMainVariables();
            helperCalculator(); // calculates based on radiobutton or checkbox selection
        });
        // Changes threshold calculation to pounds.
        poundsCheckBox.addActionListener(e -> {
            unitSelector(false,false,false,true);
            resetMainVariables();
            textFieldCalculatorCheck();
        });
    }
    // Starts the GUI and sets default parameters...
    public void displayScreen() {
        JFrame frame = new JFrame("TRI Calculator"); // Frame title
        frame.setContentPane(topPanel);
        frame.pack(); // fit all content within frame neatly
        frame.setLocationRelativeTo(null); // center frame to computer screen
        frame.setResizable(false); // do not allow frame to be resizable
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // end program on close
        unitSelector(true,false,false,false); // default is 12 fl. oz.
        setThresholdSelection(true,false); // default is 10,000 lbs
        frame.setAlwaysOnTop(true); // frame will always be on top of screen unless manually reduced
        frame.setVisible(true); // make frame visible on screen
    }

    public void twelveOunceCanCalculation() {
        units = threshold * (1.0/poundsPerGallon) * 128 * (1.0/12.0);
        setUnitsLabelAndTextField("No. Cans");
    }

    public void fifteenOunceCanCalculation() {
        units = threshold * (1.0/poundsPerGallon) * 128 * (1.0/15.0);
        setUnitsLabelAndTextField("No. Cans:");
    }

    public void gallonsCalculation() {
        units = threshold * (1.0/poundsPerGallon);
        setUnitsLabelAndTextField("No. Gal:");
    }

    public void poundsCalculation() {
        units = threshold / poundsPerGallon;
        setUnitsLabelAndTextField("No. Lbs:");
    }
    // This method's if statement is used in both 10,000 lbs and 25,000 lbs radiobutton action listener methods
    public void helperCalculator() {
        if (poundsPerGallon == 0.0) { // checks that this value has been changed from the default value
            poundsPerGallonCalculation(); // calculates pounds per gallon
        }
        if (poundsPerGallon > 0.0 // verify again that this value is not 0.0 and is a valid double
                && !specificGravityTextField.getText().isEmpty() // verify these two fields are not empty
                && !concentrationTextField.getText().isEmpty()) {
            // the if statements confirm that a measurement is selected before calculating.
            if (a12FlOzRadioButton.isSelected()) {
                twelveOunceCanCalculation();
            }
            else if (a15FlOzRadioButton.isSelected()) {
                fifteenOunceCanCalculation();
            }
            else if (gallonsCheckBox.isSelected()) {
                gallonsCalculation();
            }
            else if (poundsCheckBox.isSelected()) {
                poundsCalculation();
            }
        }
    }
    // calculates pounds per gallon
    public void poundsPerGallonCalculation() {
        // Get specific gravity and concentration
        try {
            specificGravity = Double.parseDouble(specificGravityTextField.getText());
            if (specificGravity > 0.0) { // this cannot be negative or 0
                specificGravityTextField.setText(String.format("%,.2f", specificGravity));
            }
            concentration = Double.parseDouble(concentrationTextField.getText());
            if (concentration > 0.0 && concentration <= 1.0) { // this cannot be -, 0 or greater than 1
                concentrationTextField.setText(String.format("%,.2f", concentration));
            }
        } catch (NumberFormatException e) { // if either value is not a number, set text field to empty.
            resetMainVariables();
            specificGravityTextField.setText("");
            concentrationTextField.setText("");
        }
        // Calculate pounds per gallon for the chemical desired if both inputs are greater than 0.0
        if (specificGravity > 0.0 && concentration > 0.0 && concentration <= 1.00) {
            poundsPerGallon = (specificGravity * 8.34) * concentration;
        }
        else { // catches incorrect specific gravity and concentration inputs
            if (specificGravity <= 0.0) {
                specificGravity = 0.0; // reset
                specificGravityTextField.setText("");
            }
            else {
                concentration = 0.0; // reset
                concentrationTextField.setText("");
            }
            numberOfUnitsTextField.setText("");
        }
    }
    // Setting these variables to 0.0 will restart the calculation.
    public void resetMainVariables() {
        specificGravity = 0.0;
        concentration = 0.0;
        poundsPerGallon = 0.0;
    }
    // Checks to make sure the main text fields have data
    public void textFieldCalculatorCheck() {
        if (!specificGravityTextField.getText().isEmpty() && !concentrationTextField.getText().isEmpty()) {
            helperCalculator(); // calculates based on radiobutton and/or checkbox selection
        }
    }
    // Sets the units label text and units text field value that is calculated
    public void setUnitsLabelAndTextField(String jLabelText) {
        numberOfUnitsLabel.setText(jLabelText); // change units to No. Cans
        numberOfUnitsTextField.setText(String.valueOf(String.format("%,.2f", units)));
        resetMainVariables();
    }
    // Sets radio buttons for threshold and threshold calculation
    public void setThresholdSelection(boolean tenThousand, boolean twentyFiveThousand) {
        a10000LbsRadioButton.setSelected(tenThousand);
        a25000LbsRadioButton.setSelected(twentyFiveThousand);
        // Change threshold
        if (tenThousand) {
            threshold = 10000.0;
        }
        else {
            threshold = 25000.0;
        }
    }
    // Selects the correct checkbox or radiobutton for units.
    public void unitSelector
            (boolean calculate12FlOz, boolean calculate15FLOz, boolean calculateGal, boolean calculateLbs) {
        a12FlOzRadioButton.setSelected(calculate12FlOz);
        a15FlOzRadioButton.setSelected(calculate15FLOz);
        gallonsCheckBox.setSelected(calculateGal);
        poundsCheckBox.setSelected(calculateLbs);
    }
}
