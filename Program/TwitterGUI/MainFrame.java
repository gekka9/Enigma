package TwitterGUI;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.ScrollPane;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;

import twitter4j.Status;

public class MainFrame extends JFrame{

  private ClientModel model;
  private DefaultListModel listModel;
  private JList list;
  private PostField postField;
  
  public MainFrame(ClientModel model){
    this.model=model;
    this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
    this.setMinimumSize(new Dimension(420, 600));
    //this.setMaximumSize(new Dimension(400, 600));
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLocation(50,50);
    this.postField=new PostField(this.model.getTwitter());
    this.getContentPane().add(this.postField);
  
    this.listModel = new DefaultListModel();
    this.list = new JList(this.listModel);
    this.list.setLayoutOrientation(JList.VERTICAL);
    this.list.setCellRenderer(new TweetRenderer());
    //this.list.setBounds(0, 0, 400, 600);
    JScrollPane scrollPane = new JScrollPane(this.list);
    scrollPane.setPreferredSize(new Dimension(400,600));
    this.getContentPane().add(scrollPane);
    this.pack();
    this.setVisible(true);
  } 

  public void addPost(Status status){
    this.listModel.add(0,new TweetData(status,this.model));
  }

  public PostField getPostField() {
    return this.postField;
  }
}
