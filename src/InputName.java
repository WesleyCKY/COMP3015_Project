import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

// For user to input name

public class InputName extends JFrame {
	public InputName() {
		this.setSize(new Dimension(320, 240));

		Container container = this.getContentPane(); // Create Container to store text field
		container.setLayout(new GridLayout(3, 0)); // Set layout using FlowLayout

		JLabel label = new JLabel("Please enter your name: ");
		JTextField textField = new JTextField();
		JButton submit = new JButton("Submit");

		container.add(label);
		container.add(textField);
		container.add(submit);

		// Once submit name, activate the UI window
		submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Hi!"+textField.getText());
				
				UI ui = UI.getInstance();			// get the instance of UI
				ui.setData(new int[50][50], 20);	// set the data array and block size. comment this statement to use the default data array and block size.
				ui.setVisible(true);
				
				setVisible(false); // If submitted, close input name window
			}	
		});

	}
}