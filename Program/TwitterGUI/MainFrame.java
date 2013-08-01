package TwitterGUI;

import java.awt.Dimension;
import java.awt.ScrollPane;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import twitter4j.Status;

public class MainFrame extends JFrame{

  private ClientModel model;
  private Box postBox;
  private PostField postField;
  
  public MainFrame(ClientModel model){
    this.model=model;
    this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
    this.setMinimumSize(new Dimension(420, 600));
    this.setMaximumSize(new Dimension(400, 600));
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLocation(50,50);
    this.pack();
    this.postField=new PostField(this.model.getTwitter());
    this.getContentPane().add(this.postField);
  
    this.postBox = new Box(BoxLayout.Y_AXIS);
    this.postBox.setBounds(0, 0, 400, 600);
    JScrollPane scrollPane = new JScrollPane(this.postBox);
    scrollPane.setPreferredSize(new Dimension(400,600));
    this.getContentPane().add(scrollPane);
    this.setVisible(true);
  }  
  
  public Box getPostBox(){
    return this.postBox;
  }

  public PostField getPostField() {
    return this.postField;
  }
}
