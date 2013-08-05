package TwitterGUI;

import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;

import twitter4j.Status;

public class MainFrame extends JFrame{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private final static int MINIMUM_WIDTH=420;
  private final static int MINIMUM_HEIGHT=600;
  private final static int LOCATION_WIDTH=50;
  private final static int LOCATION_HEIGHT=50;
  private final static int SCROLLPANE_WIDTH=400;
  private final static int SCROLLPANE_HEIGHT=600;
  private ClientModel model;
  private DefaultListModel listModel;
  private JList list;
  private PostField postField;
  
  public MainFrame(ClientModel model){
    this.model=model;
    this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
    this.setMinimumSize(new Dimension(MINIMUM_WIDTH, MINIMUM_HEIGHT));
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLocation(LOCATION_WIDTH,LOCATION_HEIGHT);
    this.postField=new PostField(this.model.getTwitter());
    this.getContentPane().add(this.postField);
  
    this.listModel = new DefaultListModel();
    this.list = new JList(this.listModel);
    this.list.setLayoutOrientation(JList.VERTICAL);
    this.list.setCellRenderer(new TweetRenderer());
    JScrollPane scrollPane = new JScrollPane(this.list);
    scrollPane.setPreferredSize(new Dimension(SCROLLPANE_WIDTH,SCROLLPANE_HEIGHT));
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
