import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class NodeModificationPanel extends JPanel {
	private static JTextField textField;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JButton submit, quit;
	private JRadioButton dataRB, durationRB;
	private int nodeIndex;
	private static boolean textFieldFlag;
	private static Border textFieldBorder;

	public NodeModificationPanel(int i) {
		GraphEditor.drawingPanel.nodeModificationPanelShowing = true;
		nodeIndex = i;
		setPreferredSize(new Dimension(GraphEditor.frame.getWidth(), 90));
		setLayout(null);

		textField = new JTextField();
		textField.setFont(new Font("Times New Roman", Font.BOLD, 14));
		textField.setHorizontalAlignment(SwingConstants.LEFT);
		textField.setBounds(214, 44, 86, 30);
		add(textField);
		textField.setEditable(false);
		textField.setColumns(10);

		textFieldBorder = textField.getBorder();
		ErrorChecker errorChecker = new ErrorChecker(textFieldBorder);
		errorChecker.start();

		JLabel label = new JLabel("Edit the value of node");
		label.setFont(new Font("Times New Roman", Font.BOLD, 14));
		label.setBounds(10, 11, 146, 14);
		add(label);

		submit = new JButton("Submit");
		submit.setFont(new Font("Times New Roman", Font.BOLD, 16));
		submit.setMnemonic('s');
		submit.setBounds(310, 42, 89, 30);
		add(submit);

		quit = new JButton("Quit");
		quit.setFont(new Font("Times New Roman", Font.BOLD, 16));
		quit.setMnemonic('q');
		quit.setBounds(404, 42, 89, 30);
		add(quit);

		dataRB = new JRadioButton("Data");
		buttonGroup.add(dataRB);
		dataRB.setFont(new Font("Times New Roman", Font.BOLD, 14));
		dataRB.setBounds(47, 31, 109, 23);
		add(dataRB);
		dataRB.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				if (dataRB.isSelected())
					textField.setEditable(true);
			}
		});

		durationRB = new JRadioButton("Duration");
		buttonGroup.add(durationRB);
		durationRB.setFont(new Font("Times New Roman", Font.BOLD, 14));
		durationRB.setBounds(47, 56, 109, 23);
		add(durationRB);

		durationRB.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				if (durationRB.isSelected())
					textField.setEditable(true);
			}
		});

		Handler handler = new Handler();
		submit.addActionListener(handler);
		quit.addActionListener(handler);

	}

	private class Handler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			if (event.getSource() == submit) {
				String str;
				NodeModificationPanel.textFieldFlag = true;
				int newData = 0, newDuration = 0;

				if (dataRB.isSelected()) {
					str = textField.getText();
					try {
						newData = Integer.parseInt(str);

						for (int j = 0; j < GraphEditor.drawingPanel.nodesData.size(); j++) {
							if (newData == GraphEditor.drawingPanel.nodesData.get(j)) {
								NodeModificationPanel.textFieldFlag = false;
								textField.setBorder(new LineBorder(Color.RED, 2, true));
								JOptionPane.showMessageDialog(GraphEditor.frame,
										"Watch Out!\n A node with this value already exists.", "Repeated Input",
										JOptionPane.WARNING_MESSAGE);
								break; // terminate the internal for
										// loop.
							}
						}
						if (NodeModificationPanel.textFieldFlag) {
							for (int c = 0; c < GraphEditor.drawingPanel.links.size(); c++) {
								if (GraphEditor.drawingPanel.links.get(c)
										.getValue1() == GraphEditor.drawingPanel.nodesData.get(nodeIndex)) {
									GraphEditor.drawingPanel.links.get(c).setValue1(newData);
								}
								if (GraphEditor.drawingPanel.links.get(c)
										.getValue2() == GraphEditor.drawingPanel.nodesData.get(nodeIndex)) {
									GraphEditor.drawingPanel.links.get(c).setValue2(newData);
								}
							}
							GraphEditor.drawingPanel.nodesData.set(nodeIndex, newData);
							GraphEditor.longestPathFlag = false;
							GraphEditor.drawingPanel.repaint();
							NodeModificationPanel.textField.setText("");
						}
					} catch (Exception exception) {
						NodeModificationPanel.textFieldFlag = false;
						textField.setBorder(new LineBorder(Color.RED, 2, true));
					}
				} else if (durationRB.isSelected()) {
					str = textField.getText();
					try {
						newDuration = Integer.parseInt(str);
						if (newDuration < 0)
							throw new Exception();
						GraphEditor.drawingPanel.duration.set(nodeIndex, newDuration);
						GraphEditor.longestPathFlag = false;
						GraphEditor.drawingPanel.repaint();
						NodeModificationPanel.textField.setText("");
					} catch (Exception exception) {
						NodeModificationPanel.textFieldFlag = false;
						textField.setBorder(new LineBorder(Color.RED, 2, true));
					}
				}
			} else if (event.getSource() == quit) {
				GraphEditor.drawingPanel.nodeModificationPanelShowing = false;
				GraphEditor.addNode.setEnabled(true);
				GraphEditor.addLink.setEnabled(true);
				GraphEditor.deleteNode.setEnabled(true);
				GraphEditor.clear.setEnabled(true);
				GraphEditor.longestPath.setEnabled(true);
				GraphEditor.drawingPanel.clicked = false;
				NodeModificationPanel.this.setVisible(false);
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
					if (NodeModificationPanel.textField.getText().equals(""))
						NodeModificationPanel.textField.setBorder(textFieldBorder);
				} catch (Exception e) {
				}
			}
		}
	}
}
