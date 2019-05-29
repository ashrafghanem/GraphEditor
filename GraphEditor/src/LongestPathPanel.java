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

public class LongestPathPanel extends JPanel {

	private static JTextField dataTF;
	private JButton submit, quit;
	private static Border textFieldBorder;
	private static boolean dataFlag;

	public LongestPathPanel() {
		GraphEditor.drawingPanel.longestPathPanelShowing = true;
		setPreferredSize(new Dimension(GraphEditor.frame.getWidth(), 70));
		setLayout(null);

		dataTF = new JTextField();
		dataTF.setFont(new Font("Times New Roman", Font.BOLD, 14));
		dataTF.setHorizontalAlignment(SwingConstants.LEFT);
		dataTF.setBounds(80, 30, 86, 30);
		add(dataTF);
		dataTF.setColumns(10);

		textFieldBorder = dataTF.getBorder();
		ErrorChecker errorChecker = new ErrorChecker(textFieldBorder);
		errorChecker.start();

		JLabel startingNodeLabel = new JLabel("Starting Node");
		startingNodeLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
		startingNodeLabel.setBounds(10, 11, 93, 14);
		add(startingNodeLabel);

		JLabel data = new JLabel("Data");
		data.setHorizontalAlignment(SwingConstants.RIGHT);
		data.setFont(new Font("Times New Roman", Font.BOLD, 14));
		data.setBounds(20, 34, 47, 20);
		add(data);

		submit = new JButton("Submit");
		submit.setFont(new Font("Times New Roman", Font.BOLD, 16));
		submit.setMnemonic('s');
		submit.setBounds(192, 39, 79, 30);
		add(submit);

		quit = new JButton("Quit");
		quit.setFont(new Font("Times New Roman", Font.BOLD, 16));
		quit.setMnemonic('q');
		quit.setBounds(281, 39, 79, 30);
		add(quit);

		Handler handler = new Handler();
		submit.addActionListener(handler);
		quit.addActionListener(handler);
	}

	private class Handler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			if (event.getSource() == submit) {
				LongestPathPanel.dataFlag = true;
				int startingNode = 0;

				LongestPath longestPath = new LongestPath();
				String str = dataTF.getText();

				try {
					startingNode = Integer.parseInt(str);
				} catch (Exception exception) {
					dataTF.setBorder(new LineBorder(Color.RED, 2, true));
					LongestPathPanel.dataFlag = false;
				}
				if (LongestPathPanel.dataFlag) {
					for (int i = 0; i < GraphEditor.drawingPanel.nodesData.size(); i++) {
						if (startingNode == GraphEditor.drawingPanel.nodesData.get(i)) {
							LongestPathPanel.dataFlag = true;
							break;
						} else {
							LongestPathPanel.dataFlag = false;
						}
					}
					if (LongestPathPanel.dataFlag) {
						GraphEditor.longestPathFlag = true;
						longestPath.initialize(GraphEditor.drawingPanel.nodes, GraphEditor.drawingPanel.nodes.size(),
								longestPath.getNodeIndex(startingNode, false));

						longestPath.path(GraphEditor.drawingPanel.nodes, longestPath.getNodeIndex(startingNode, false),
								longestPath.getNodeIndex(longestPath.getMaxDuration(), true));

						longestPath.gatherPathLinks();
						GraphEditor.drawingPanel.repaint();
					} else {
						dataTF.setBorder(new LineBorder(Color.RED, 2, true));
						JOptionPane.showMessageDialog(GraphEditor.frame, "This node does not exist!", "Starting Node",
								JOptionPane.WARNING_MESSAGE);
					}
				}
			} else if (event.getSource() == quit) {
				GraphEditor.drawingPanel.longestPathPanelShowing = false;
				GraphEditor.addNode.setEnabled(true);
				GraphEditor.addLink.setEnabled(true);
				GraphEditor.deleteNode.setEnabled(true);
				GraphEditor.clear.setEnabled(true);
				GraphEditor.longestPath.setEnabled(true);

				LongestPathPanel.this.setVisible(false);
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
					if (LongestPathPanel.dataTF.getText().equals(""))
						LongestPathPanel.dataTF.setBorder(textFieldBorder);
				} catch (Exception e) {
				}
			}
		}
	}
}
