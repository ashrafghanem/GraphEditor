import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class AddNodePanel extends JPanel {
	public static JTextField dataTF, durationTF;
	private JButton submit, quit;
	private static Border textFieldBorder;
	private static boolean dataFlag, durationFlag;

	public AddNodePanel() {
		GraphEditor.drawingPanel.addNodePanelShowing = true;
		this.setLayout(null);
		setPreferredSize(new Dimension(GraphEditor.frame.getWidth(), 80));

		dataTF = new JTextField();
		dataTF.setFont(new Font("Times New Roman", Font.BOLD, 14));
		dataTF.setHorizontalAlignment(SwingConstants.LEFT);
		dataTF.setBounds(129, 11, 86, 30);
		add(dataTF);
		dataTF.setColumns(15);

		durationTF = new JTextField();
		durationTF.setFont(new Font("Times New Roman", Font.BOLD, 14));
		durationTF.setHorizontalAlignment(SwingConstants.LEFT);
		durationTF.setColumns(15);
		durationTF.setBounds(129, 44, 86, 30);
		add(durationTF);

		textFieldBorder = durationTF.getBorder();
		ErrorChecker errorChecker = new ErrorChecker(textFieldBorder);
		errorChecker.start();

		JLabel data = new JLabel("Node Data");
		data.setHorizontalAlignment(SwingConstants.LEFT);
		data.setFont(new Font("Times New Roman", Font.BOLD, 14));
		data.setBounds(10, 15, 86, 20);
		add(data);

		JLabel duration = new JLabel("Node Duration");
		duration.setFont(new Font("Times New Roman", Font.BOLD, 14));
		duration.setBounds(10, 48, 109, 20);
		add(duration);

		submit = new JButton("Submit");
		submit.setFont(new Font("Times New Roman", Font.BOLD, 16));
		submit.setMnemonic('s');
		submit.setBounds(242, 44, 79, 30);
		add(submit);

		quit = new JButton("Quit");
		quit.setFont(new Font("Times New Roman", Font.BOLD, 16));
		quit.setMnemonic('q');
		quit.setBounds(331, 44, 79, 30);
		add(quit);

		Handler handler = new Handler();
		submit.addActionListener(handler);
		quit.addActionListener(handler);
	}

	private class Handler implements ActionListener {
		private int data, duration;

		@Override
		public void actionPerformed(ActionEvent event) {
			if (event.getSource() == submit) {
				AddNodePanel.dataFlag = true;
				AddNodePanel.durationFlag = true;

				String nodeData = dataTF.getText();
				String nodeDuration = durationTF.getText();

				try {
					data = Integer.parseInt(nodeData);
				} catch (Exception exception) {
					AddNodePanel.dataFlag = false;
					dataTF.setBorder(new LineBorder(Color.RED, 2, true));
				}

				try {
					duration = Integer.parseInt(nodeDuration);
				} catch (Exception exception) {
					AddNodePanel.durationFlag = false;
					durationTF.setBorder(new LineBorder(Color.RED, 2, true));
				}

				if (duration < 0) {
					durationTF.setBorder(new LineBorder(Color.RED, 2, true));
					AddNodePanel.durationFlag = false;
				}

				if (AddNodePanel.dataFlag) {
					for (int i = 0; i < GraphEditor.drawingPanel.nodesData.size(); i++) {
						if (data == GraphEditor.drawingPanel.nodesData.get(i)) {
							AddNodePanel.dataFlag = false;
							JOptionPane.showMessageDialog(GraphEditor.frame,
									"Watch Out!\n A node with 1st data already exists.", "Repeated Input",
									JOptionPane.WARNING_MESSAGE);
							break; // terminate the for loop
						}
					}
				}

				if (AddNodePanel.dataFlag && AddNodePanel.durationFlag) {
					/*
					 * Setting the editable method is to prevent adding new node
					 * while a previously created node is already moving. It's
					 * set editable in the press method.
					 */
					dataTF.setEditable(false);
					durationTF.setEditable(false);
					dataTF.setText("");
					durationTF.setText("");
					GraphEditor.nodeValue = data;
					GraphEditor.nodeDuration = duration;
					GraphEditor.createNode = true;
				}

			} else if (event.getSource() == quit) {
				GraphEditor.drawingPanel.addNodePanelShowing = false;
				GraphEditor.addNode.setEnabled(true);
				GraphEditor.addLink.setEnabled(true);
				GraphEditor.deleteNode.setEnabled(true);
				GraphEditor.clear.setEnabled(true);
				GraphEditor.longestPath.setEnabled(true);

				AddNodePanel.this.setVisible(false);
			}
		}
	}

	private class ErrorChecker extends Thread {
		private Border textFieldBorder;

		public ErrorChecker(Border textFieldBorder) {
			this.textFieldBorder = textFieldBorder;
		}

		public void run() {
			while (true) {
				try {
					if (AddNodePanel.dataTF.getText().equals(""))
						AddNodePanel.dataTF.setBorder(textFieldBorder);

					if (AddNodePanel.durationTF.getText().equals(""))
						AddNodePanel.durationTF.setBorder(textFieldBorder);
				} catch (Exception e) {
				}
			}
		}
	}
}
