import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class GraphEditor {
	public static boolean createNode = false;
	public static boolean flag = true;
	public static boolean longestPathFlag = false;
	public static int nodeValue, nodeDuration;
	public static JFrame frame;
	public static JButton addLink, addNode, deleteNode, clear, longestPath;
	private static JPanel controlPanel;
	public static DrawingPanel drawingPanel;

	public static void main(String[] args) {

		// Changing the look and feel for the application.
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
		}

		frame = new JFrame("Graph Editor");
		frame.setSize(800, 650);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				int choice;
				choice = JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit the program?",
						"Exit Confirmation", JOptionPane.YES_NO_CANCEL_OPTION);
				if (choice == JOptionPane.YES_OPTION) {
					System.exit(0);
				} else {
					frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				}
			}
		});

		drawingPanel = new DrawingPanel();
		drawingPanel.setBackground(SystemColor.window);
		frame.getContentPane().add(drawingPanel);

		controlPanel = new JPanel();
		frame.getContentPane().add(controlPanel, BorderLayout.SOUTH);
		addNode = new JButton("");
		addNode.setMnemonic('n');
		addNode.setToolTipText("Add a node");
		addNode.setIcon(new ImageIcon(GraphEditor.class.getResource("addNode.png")));
		addNode.setFont(new Font("Tahoma", Font.BOLD, 16));
		addNode.setFocusable(false);
		controlPanel.add(addNode);

		addLink = new JButton("");
		addLink.setMnemonic('l');
		addLink.setToolTipText("Add a link");
		addLink.setIcon(new ImageIcon(GraphEditor.class.getResource("addLink.png")));
		addLink.setFont(new Font("Tahoma", Font.BOLD, 16));
		addLink.setFocusable(false);
		controlPanel.add(addLink);

		deleteNode = new JButton("");
		deleteNode.setMnemonic('d');
		deleteNode.setToolTipText("Delete a node");
		deleteNode.setIcon(new ImageIcon(GraphEditor.class.getResource("deleteNode.png")));
		deleteNode.setFont(new Font("Tahoma", Font.BOLD, 16));
		deleteNode.setFocusable(false);
		controlPanel.add(deleteNode);

		clear = new JButton("");
		clear.setMnemonic('c');
		clear.setToolTipText("Clear the drawing area");
		clear.setIcon(new ImageIcon(GraphEditor.class.getResource("clear.png")));
		clear.setFocusable(false);
		controlPanel.add(clear);

		longestPath = new JButton("");
		longestPath.setMnemonic('p');
		longestPath.setToolTipText("Find the longest path");
		longestPath.setIcon(new ImageIcon(GraphEditor.class.getResource("LongestPath.png")));
		longestPath.setFocusable(false);
		controlPanel.add(longestPath);

		ButtonHandler buttonHandler = new ButtonHandler();
		addNode.addActionListener(buttonHandler);
		addLink.addActionListener(buttonHandler);
		deleteNode.addActionListener(buttonHandler);
		clear.addActionListener(buttonHandler);
		longestPath.addActionListener(buttonHandler);

		frame.setVisible(true);
	}
}