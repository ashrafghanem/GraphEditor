import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;

public class DeleteNodePanel extends JPanel {
	public static JList<Object> nodesList;
	private static JButton submit, quit;
	private static int size;
	private static String[] arr;

	public DeleteNodePanel() {
		GraphEditor.drawingPanel.deleteNodePanelShowing = true;
		setPreferredSize(new Dimension(195, GraphEditor.frame.getHeight()));
		setLayout(null);

		JLabel label = new JLabel("Choose the node/s to delete:");
		label.setFont(new Font("Times New Roman", Font.BOLD, 14));
		label.setBounds(10, 11, 184, 14);
		add(label);

		submit = new JButton("Submit");
		submit.setFont(new Font("Times New Roman", Font.BOLD, 16));
		submit.setMnemonic('s');
		submit.setBounds(47, 190, 89, 30);
		add(submit);

		quit = new JButton("Quit");
		quit.setFont(new Font("Times New Roman", Font.BOLD, 16));
		quit.setMnemonic('q');
		quit.setBounds(47, 227, 89, 30);
		add(quit);

		nodesList = new JList<Object>();
		nodesList.setFont(new Font("Times New Roman", Font.BOLD, 20));
		nodesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		size = GraphEditor.drawingPanel.nodesData.size();
		arr = new String[size];
		for (int i = 0; i < size; i++) {
			arr[i] = String.valueOf(GraphEditor.drawingPanel.nodesData.get(i));
		}
		nodesList.setListData(arr);

		JScrollPane scroll = new JScrollPane(nodesList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBounds(37, 36, 116, 150);
		add(scroll);

		Handler handler = new Handler();
		submit.addActionListener(handler);
		quit.addActionListener(handler);
	}

	private class Handler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			if (event.getSource() == submit) {

				Object[] selectedValues = (Object[]) DeleteNodePanel.nodesList.getSelectedValuesList().toArray();
				Vector<Object> selectedNodes = new Vector<Object>();
				for (Object obj : selectedValues) {
					selectedNodes.add(obj);
				}

				for (int i = 0; i < GraphEditor.drawingPanel.nodes.size(); i++) {
					for (int j = 0; j < selectedNodes.size(); j++) {

						if (Integer.parseInt((String) selectedNodes.get(j)) == GraphEditor.drawingPanel.nodesData
								.get(i)) {
							GraphEditor.longestPathFlag = false;
							GraphEditor.drawingPanel.nodes.remove(i);
							GraphEditor.drawingPanel.nodesData.remove(i);
							GraphEditor.drawingPanel.duration.remove(i);

							for (int k = 0; k < GraphEditor.drawingPanel.links.size(); k++) {
								if (Integer.parseInt((String) selectedNodes.get(j)) == GraphEditor.drawingPanel.links
										.get(k).getValue1()
										|| Integer.parseInt(
												(String) selectedNodes.get(j)) == GraphEditor.drawingPanel.links.get(k)
														.getValue2()) {
									GraphEditor.drawingPanel.links.remove(k);
									k--; // because when an element is
											// removed,the rest are shifted one
											// step to take the previous index
											// and the size is changing by one.
								}
							}
							selectedNodes.remove(j);
							i--;
							break;
						}
					}
				}
				GraphEditor.drawingPanel.repaint();

				DeleteNodePanel.size = GraphEditor.drawingPanel.nodesData.size();
				DeleteNodePanel.arr = new String[size];
				for (int i = 0; i < size; i++) {
					arr[i] = String.valueOf(GraphEditor.drawingPanel.nodesData.get(i));
				}
				nodesList.setListData(arr);
			} else if (event.getSource() == quit) {
				GraphEditor.drawingPanel.deleteNodePanelShowing = false;
				GraphEditor.addNode.setEnabled(true);
				GraphEditor.addLink.setEnabled(true);
				GraphEditor.deleteNode.setEnabled(true);
				GraphEditor.clear.setEnabled(true);
				GraphEditor.longestPath.setEnabled(true);

				DeleteNodePanel.this.setVisible(false);
			}
		}
	}
}
