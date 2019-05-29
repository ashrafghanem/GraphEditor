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

public class AddLinkPanel extends JPanel {

	private static JTextField data1TF, data2TF;
	private JButton submit, quit;
	private static Border textFieldBorder;
	private static boolean data1Flag, data2Flag;

	public AddLinkPanel() {
		GraphEditor.drawingPanel.addLinkPanelShowing = true;
		setPreferredSize(new Dimension(GraphEditor.frame.getWidth(), 80));
		setLayout(null);

		data1TF = new JTextField();
		data1TF.setFont(new Font("Times New Roman", Font.BOLD, 14));
		data1TF.setHorizontalAlignment(SwingConstants.LEFT);
		data1TF.setBounds(80, 30, 86, 30);
		add(data1TF);
		data1TF.setColumns(10);

		JLabel firstNodeLabel = new JLabel("First Node");
		firstNodeLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
		firstNodeLabel.setBounds(10, 11, 68, 14);
		add(firstNodeLabel);

		JLabel Data1 = new JLabel("Data");
		Data1.setHorizontalAlignment(SwingConstants.RIGHT);
		Data1.setFont(new Font("Times New Roman", Font.BOLD, 14));
		Data1.setBounds(20, 34, 47, 20);
		add(Data1);

		JLabel secondNodeLabel = new JLabel("Second Node");
		secondNodeLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
		secondNodeLabel.setBounds(176, 11, 83, 14);
		add(secondNodeLabel);

		JLabel Data2 = new JLabel("Data");
		Data2.setHorizontalAlignment(SwingConstants.RIGHT);
		Data2.setFont(new Font("Times New Roman", Font.BOLD, 14));
		Data2.setBounds(186, 34, 47, 20);
		add(Data2);

		data2TF = new JTextField();
		data2TF.setHorizontalAlignment(SwingConstants.LEFT);
		data2TF.setFont(new Font("Times New Roman", Font.BOLD, 14));
		data2TF.setColumns(10);
		data2TF.setBounds(246, 30, 86, 30);
		add(data2TF);

		textFieldBorder = data1TF.getBorder();
		ErrorChecker errorChecker = new ErrorChecker(textFieldBorder);
		errorChecker.start();

		submit = new JButton("Submit");
		submit.setFont(new Font("Times New Roman", Font.BOLD, 16));
		submit.setMnemonic('s');
		submit.setBounds(356, 39, 79, 30);
		add(submit);

		quit = new JButton("Quit");
		quit.setFont(new Font("Times New Roman", Font.BOLD, 16));
		quit.setMnemonic('q');
		quit.setBounds(445, 39, 79, 30);
		add(quit);

		Handler handler = new Handler();
		submit.addActionListener(handler);
		quit.addActionListener(handler);
	}

	private class Handler implements ActionListener {
		private int data1, data2;

		@Override
		public void actionPerformed(ActionEvent event) {
			if (event.getSource() == submit) {
				AddLinkPanel.data1Flag = true;
				AddLinkPanel.data2Flag = true;

				String node1Data = data1TF.getText();
				String node2Data = data2TF.getText();

				try {
					data1 = Integer.parseInt(node1Data);
				} catch (Exception exception) {
					AddLinkPanel.data1Flag = false;
					data1TF.setBorder(new LineBorder(Color.RED, 2, true));
				}

				try {
					data2 = Integer.parseInt(node2Data);
				} catch (Exception exception) {
					AddLinkPanel.data2Flag = false;
					data2TF.setBorder(new LineBorder(Color.RED, 2, true));
				}
				// Check whether data1 exists.
				if (AddLinkPanel.data1Flag) {
					for (int i = 0; i < GraphEditor.drawingPanel.nodesData.size(); i++) {
						if (data1 == GraphEditor.drawingPanel.nodesData.get(i)) {
							AddLinkPanel.data1Flag = true;
							break;
						} else
							AddLinkPanel.data1Flag = false;
					}
					// If data1 doesn't exist.
					if (!AddLinkPanel.data1Flag) {
						data1TF.setBorder(new LineBorder(Color.RED, 2, true));
						JOptionPane.showMessageDialog(GraphEditor.frame, "Watch Out!\n The first node does not exist.",
								"Node Linking", JOptionPane.WARNING_MESSAGE);
					}
				}
				// Check whether data2 exists.
				if (AddLinkPanel.data2Flag) {
					for (int i = 0; i < GraphEditor.drawingPanel.nodesData.size(); i++) {
						if (data2 == GraphEditor.drawingPanel.nodesData.get(i)) {
							AddLinkPanel.data2Flag = true;
							break;
						} else
							AddLinkPanel.data2Flag = false;
					}
					// If data2 doesn't exist.
					if (!AddLinkPanel.data2Flag) {
						data2TF.setBorder(new LineBorder(Color.RED, 2, true));
						JOptionPane.showMessageDialog(GraphEditor.frame, "Watch Out!\n The second node does not exist.",
								"Node Linking", JOptionPane.WARNING_MESSAGE);
					}
				}
				if (AddLinkPanel.data1Flag && AddLinkPanel.data2Flag) {
					data1TF.setText("");
					data2TF.setText("");
					Links links = new Links(data1, data2);
					GraphEditor.drawingPanel.links.add(links);
					GraphEditor.drawingPanel.repaint();
				}
			} else if (event.getSource() == quit) {
				GraphEditor.drawingPanel.addLinkPanelShowing = false;
				GraphEditor.addNode.setEnabled(true);
				GraphEditor.addLink.setEnabled(true);
				GraphEditor.deleteNode.setEnabled(true);
				GraphEditor.clear.setEnabled(true);
				GraphEditor.longestPath.setEnabled(true);

				AddLinkPanel.this.setVisible(false);
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
					if (AddLinkPanel.data1TF.getText().equals(""))
						AddLinkPanel.data1TF.setBorder(textFieldBorder);

					if (AddLinkPanel.data2TF.getText().equals(""))
						AddLinkPanel.data2TF.setBorder(textFieldBorder);
				} catch (Exception e) {
				}
			}
		}
	}
}
