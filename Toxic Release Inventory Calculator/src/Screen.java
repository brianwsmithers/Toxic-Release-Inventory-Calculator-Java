import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Screen {
    private JPanel topPanel;
    private JTextField specificGravityTextfield;
    private JTextField concentrationTextfield;
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

    private double units;
    private double poundsPerGallon;
    double specificGravity;
    double concentration;

    // these variables control the calculation, true = calculate, false = do not calculate
    // only one variable can be true, all others must be false
    private boolean twelveOunceCans = true; // default option is to calculate for 12 oz. cans
    private boolean fifteenOunceCans = false;
    private boolean gallons = false;
    private boolean pounds = false;

    // default threshold value is to calculate for 10,000 lbs, alternative is 25,000 lbs.
    private double threshold = 10000.0;

    // Action listeners
    public Screen() {
        // Calculate button calculates the number of units needed depending on the checkbox and/or radiobutton selected
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // calculate if specific gravity and concentration text fields are not empty
                if (!specificGravityTextfield.getText().isEmpty() && !concentrationTextfield.getText().isEmpty()) {
                    helperCalculator(); // calculates based on radiobutton, checkbox selection
                }
            }
        });
        // 12 fl. oz. can calculation
        a12FlOzRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                a12FlOzRadioButton.setSelected(true); // will not deselect 12 fl. oz.
                a15FlOzRadioButton.setSelected(false); // deselects 15 fl. oz.
                gallonsCheckBox.setSelected(false); // deselects the gallons checkbox
                poundsCheckBox.setSelected(false); // deselects the pounds checkbox
                twelveOunceCans = true; // calculate for 12 oz. cans
                fifteenOunceCans = false; // do not calculate for 15 oz. cans
                gallons = false; // do not calculate for gallons
                pounds = false; // do calculate for gallons
                resetMainVariables();
                // recalculate without pressing calculate button only if text fields are not empty
                if (!specificGravityTextfield.getText().isEmpty() && !concentrationTextfield.getText().isEmpty()) {
                    helperCalculator(); // calculates based on radiobutton or checkbox selection
                }
            }
        });
        // 15 fl. oz. can calculation
        a15FlOzRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                a15FlOzRadioButton.setSelected(true); // will not deselect 15 fl. oz.
                a12FlOzRadioButton.setSelected(false); // deselects 12 fl. oz.
                gallonsCheckBox.setSelected(false); // deselects the gallons checkbox
                poundsCheckBox.setSelected(false); // deselects the pounds checkbox
                fifteenOunceCans = true; // calculate for 15 oz. cans
                twelveOunceCans = false; // do not calculate for 12 oz. cans
                gallons = false; // do not calculate for gallons
                pounds = false; // do not calculate for pounds
                resetMainVariables();
                // recalculate without pressing calculate button
                if (!specificGravityTextfield.getText().isEmpty() && !concentrationTextfield.getText().isEmpty()) {
                    helperCalculator(); // calculates based on radiobutton or checkbox selection
                }
            }
        });
        // clear all text fields
        clearButton.addActionListener(e -> {
            specificGravityTextfield.setText("");
            concentrationTextfield.setText("");
            numberOfUnitsTextField.setText("");
            resetMainVariables();
        });
        // Changes calculation to gallons.
        gallonsCheckBox.addActionListener(e -> {
            gallonsCheckBox.setSelected(true); // will not deselect checkbox
            a12FlOzRadioButton.setSelected(false); // deselects 12 fl. oz.
            a15FlOzRadioButton.setSelected(false); // deselects 15 fl. oz.
            poundsCheckBox.setSelected(false); // deselects pounds
            gallons = true; // calculate for gallons
            twelveOunceCans = false; // do not calculate for 12 oz. cans
            fifteenOunceCans = false; // do not calculate for 15 oz. cans
            pounds = false; // do not calculate for pounds
            resetMainVariables();
            // recalculate without pressing calculate button
            if (!specificGravityTextfield.getText().isEmpty() && !concentrationTextfield.getText().isEmpty()) {
                helperCalculator(); // calculates based on radiobutton or checkbox selection
            }
        });
        // Changes threshold calculation to 10,000 lbs
        a10000LbsRadioButton.addActionListener(e -> {
            a10000LbsRadioButton.setSelected(true); // will not deselect radiobutton
            a25000LbsRadioButton.setSelected(false); // deselects 25,000 lbs radiobutton
            threshold = 10000.00; // set threshold to 10,000 lbs
            resetMainVariables();
            helperCalculator(); // calculates based on radiobutton or checkbox selection
        });
        // Changes threshold calculation to 25,000 lbs
        a25000LbsRadioButton.addActionListener(e -> {
            a25000LbsRadioButton.setSelected(true); // will not deselect radiobutton
            a10000LbsRadioButton.setSelected(false); // deselects 10,000 lbs radiobutton
            threshold = 25000.00; // set threshold to 25,000 lbs
            resetMainVariables();
            helperCalculator(); // calculates based on radiobutton or checkbox selection
        });
        // Changes threshold calculation to pounds.
        poundsCheckBox.addActionListener(e -> {
            poundsCheckBox.setSelected(true); // will not deselect checkbox
            gallonsCheckBox.setSelected(false);  // deselects gallons
            a12FlOzRadioButton.setSelected(false); // deselects 12 fl. oz.
            a15FlOzRadioButton.setSelected(false); // deselects 15 fl. oz.
            pounds = true; // calculate for pounds
            gallons = false; // do not calculate for gallons
            twelveOunceCans = false; // do not calculate for 12 oz. cans
            fifteenOunceCans = false; // do not calculate for 15 oz. cans
            resetMainVariables();
            // recalculate without pressing calculate button
            if (!specificGravityTextfield.getText().isEmpty() && !concentrationTextfield.getText().isEmpty()) {
                helperCalculator(); // calculates based on radiobutton or checkbox selection
            }
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
        a12FlOzRadioButton.setSelected(true); // default option for calculation is set to 12 fl. oz. cans
        a10000LbsRadioButton.setSelected(true); // default option for calculation is set to 10,000 lbs threshold
        frame.setAlwaysOnTop(true); // frame will always be on top of screen unless manually reduced
        frame.setVisible(true); // make frame visible on screen
    }
    // calculation for 12 fl. oz. cans
    public void twelveOunceCanCalculation() {
        units = threshold * (1.0/poundsPerGallon) * 128 * (1.0/12.0);
        numberOfUnitsLabel.setText("No. Cans:"); // change units to No. Cans
        numberOfUnitsTextField.setText(String.valueOf(String.format("%,.2f", units)));
        resetMainVariables();
    }
    // calculation for 15 fl. oz. cans
    public void fifteenOunceCanCalculation() {
        units = threshold * (1.0/poundsPerGallon) * 128 * (1.0/15.0);
        numberOfUnitsLabel.setText("No. Cans:");
        numberOfUnitsTextField.setText(String.valueOf(String.format("%,.2f", units)));
        resetMainVariables();
    }
    // calculation for gallons
    public void gallonsCalculation() {
        units = threshold * (1.0/poundsPerGallon);
        numberOfUnitsLabel.setText("No. Gal:");
        numberOfUnitsTextField.setText(String.valueOf(String.format("%,.2f", units)));
        resetMainVariables();
    }
    // calculation for pounds
    public void poundsCalculation() {
        units = threshold / poundsPerGallon;
        numberOfUnitsLabel.setText("No. Lbs:");
        numberOfUnitsTextField.setText(String.valueOf(String.format("%,.2f", units)));
        resetMainVariables();
    }
    // This method's if statement is used in both 10,000 lbs and 25,000 lbs radiobutton action listener methods
    public void helperCalculator() {
        if (poundsPerGallon == 0.0) { // checks that this value has been changed from the default value
            poundsPerGallonCalculation(); // calculates pounds per gallon
        }
        if (poundsPerGallon > 0.0) { // verify again that this value is not 0.0 and is a valid double
            // the if statements confirm that a value exists and that a unit or measurement is selected before calculating.
            if (a12FlOzRadioButton.isSelected()
                    && !specificGravityTextfield.getText().isEmpty()
                    && !concentrationTextfield.getText().isEmpty()) {
                twelveOunceCanCalculation();
            }
            else if (a15FlOzRadioButton.isSelected()
                    && !specificGravityTextfield.getText().isEmpty()
                    && !concentrationTextfield.getText().isEmpty()) {
                fifteenOunceCanCalculation();
            }
            else if (gallonsCheckBox.isSelected()
                    && !specificGravityTextfield.getText().isEmpty()
                    && !concentrationTextfield.getText().isEmpty()) {
                gallonsCalculation();
            }
            else if (poundsCheckBox.isSelected()
                    && !specificGravityTextfield.getText().isEmpty()
                    && !concentrationTextfield.getText().isEmpty()) {
                poundsCalculation();
            }
        }
    }
    // calculates pounds per gallon
    public void poundsPerGallonCalculation() {
        // Get specific gravity and concentration
        try {
            specificGravity = Double.parseDouble(specificGravityTextfield.getText());
            if (specificGravity > 0.0) { // does not accept negative or zero values
                specificGravityTextfield.setText(String.format("%,.2f", specificGravity));
            }
            concentration = Double.parseDouble(concentrationTextfield.getText());
            if (concentration > 0.0 && concentration <= 1.0) { // does not accept negatives, zero or greater than 1
                concentrationTextfield.setText(String.format("%,.2f", concentration));
            }
        } catch (NumberFormatException e) { // if either value is not a number, set text field to empty.
            resetMainVariables();
            specificGravityTextfield.setText(""); // set text field blank
            concentrationTextfield.setText(""); // set text field blank
        }
        // Calculate pounds per gallon for the chemical desired if both inputs are greater than 0.0
        if (specificGravity > 0.0 && concentration > 0.0 && concentration <= 1.00) {
            poundsPerGallon = (specificGravity * 8.34) * concentration;
        }
        else { // catches incorrect specific gravity and concentration inputs
            // set text field blank
            if (specificGravity <= 0.0) {
                specificGravity = 0.0; // reset
                specificGravityTextfield.setText(""); // set text field blank
            }
            else {
                concentration = 0.0; // reset
                concentrationTextfield.setText(""); // set text field blank
            }
            numberOfUnitsTextField.setText(""); // set text field blank
        }
    }
    public void resetMainVariables() {
        // Setting these variables to 0.0 will restart the calculation to ensure the correct calculation.
        specificGravity = 0.0;
        concentration = 0.0;
        poundsPerGallon = 0.0;
    }
}
