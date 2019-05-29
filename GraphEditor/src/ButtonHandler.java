import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;

public class ButtonHandler implements ActionListener {
	private Clip clip;

	public void actionPerformed(ActionEvent event) {
		try {
			AudioInputStream audioInputStream = AudioSystem
					.getAudioInputStream(GraphEditor.class.getResource("Button_Sound.wav"));
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
		} catch (UnsupportedAudioFileException e) {
		} catch (IOException e) {
		} catch (LineUnavailableException e) {
		}

		if (event.getSource() == GraphEditor.addNode) {
			clip.start();
			GraphEditor.addNode.setEnabled(false);
			GraphEditor.addLink.setEnabled(false);
			GraphEditor.deleteNode.setEnabled(false);
			GraphEditor.clear.setEnabled(false);
			GraphEditor.longestPath.setEnabled(false);

			AddNodePanel addNodePanel = new AddNodePanel();
			GraphEditor.frame.add(addNodePanel, BorderLayout.NORTH);
			GraphEditor.frame.validate();
		}

		else if (event.getSource() == GraphEditor.addLink) {
			clip.start();

			GraphEditor.addNode.setEnabled(false);
			GraphEditor.addLink.setEnabled(false);
			GraphEditor.deleteNode.setEnabled(false);
			GraphEditor.clear.setEnabled(false);
			GraphEditor.longestPath.setEnabled(false);

			AddLinkPanel addlinkPanel = new AddLinkPanel();
			GraphEditor.frame.add(addlinkPanel, BorderLayout.NORTH);
			GraphEditor.frame.validate();
		} else if (event.getSource() == GraphEditor.deleteNode) {
			clip.start();

			GraphEditor.addNode.setEnabled(false);
			GraphEditor.addLink.setEnabled(false);
			GraphEditor.deleteNode.setEnabled(false);
			GraphEditor.clear.setEnabled(false);
			GraphEditor.longestPath.setEnabled(false);

			DeleteNodePanel deleteNodePanel = new DeleteNodePanel();
			GraphEditor.frame.add(deleteNodePanel, BorderLayout.WEST);
			GraphEditor.frame.validate();
			/*
			 * int nodeData = 0; boolean found = false; GraphEditor.flag = true;
			 * String str = JOptionPane.showInputDialog(GraphEditor.frame,
			 * "Enter the node data to delete:", "Node Deletion",
			 * JOptionPane.QUESTION_MESSAGE); if (str == null) {
			 * GraphEditor.flag = false; } if (GraphEditor.flag) { try {
			 * nodeData = Integer.parseInt(str); } catch (Exception exception) {
			 * JOptionPane.showMessageDialog(GraphEditor.frame,
			 * "Watch Out!\n You must enter an integer value.", "Wrong Input",
			 * JOptionPane.WARNING_MESSAGE); GraphEditor.flag = false; } } if
			 * (GraphEditor.flag) { for (int i = 0; i <
			 * GraphEditor.drawingPanel.nodes.size(); i++) { if (nodeData ==
			 * GraphEditor.drawingPanel.nodesData.get(i)) { found = true;
			 * GraphEditor.longestPathFlag = false;
			 * GraphEditor.drawingPanel.nodes.remove(i);
			 * GraphEditor.drawingPanel.nodesData.remove(i);
			 * GraphEditor.drawingPanel.duration.remove(i);
			 * 
			 * for (int j = 0; j < GraphEditor.drawingPanel.links.size(); j++) {
			 * if (nodeData == GraphEditor.drawingPanel.links.get(j).getValue1()
			 * || nodeData == GraphEditor.drawingPanel.links.get(j).getValue2())
			 * { GraphEditor.drawingPanel.links.remove(j); j--; // because when
			 * an element is removed, the // rest are shifted one step to take
			 * the // previous index and the size is // changing by one.
			 * 
			 * } } GraphEditor.drawingPanel.repaint(); break; } } if (!found)
			 * JOptionPane.showMessageDialog(GraphEditor.frame,
			 * "This node does not exist!", "Node Deletion",
			 * JOptionPane.INFORMATION_MESSAGE); }
			 */
		} else if (event.getSource() == GraphEditor.clear) {
			clip.start();
			int choice = JOptionPane.showConfirmDialog(GraphEditor.frame, "Are you sure you want to clear the graph?",
					"Clear", JOptionPane.YES_NO_CANCEL_OPTION);
			if (choice == JOptionPane.YES_OPTION) {
				GraphEditor.longestPathFlag = false;
				GraphEditor.drawingPanel.nodes.clear();
				GraphEditor.drawingPanel.nodesData.clear();
				GraphEditor.drawingPanel.links.clear();
				GraphEditor.drawingPanel.duration.clear();
				GraphEditor.drawingPanel.repaint();
			}
		} else if (event.getSource() == GraphEditor.longestPath) {
			clip.start();

			GraphEditor.flag = true;
			if (GraphEditor.drawingPanel.nodes.isEmpty() || GraphEditor.drawingPanel.nodes.size() <= 2) {
				JOptionPane.showMessageDialog(GraphEditor.frame, "The graph has to contain more than 2 nodes.",
						"Warning", JOptionPane.WARNING_MESSAGE);
				GraphEditor.flag = false;
			}

			if (GraphEditor.flag) {
				GraphEditor.addNode.setEnabled(false);
				GraphEditor.addLink.setEnabled(false);
				GraphEditor.deleteNode.setEnabled(false);
				GraphEditor.clear.setEnabled(false);
				GraphEditor.longestPath.setEnabled(false);

				LongestPathPanel longestPathPanel = new LongestPathPanel();
				GraphEditor.frame.add(longestPathPanel, BorderLayout.NORTH);
				GraphEditor.frame.validate();
			}
		}
	}
}
