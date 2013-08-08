package TwitterGUI;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class Console extends JPanel{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  public static final int CONSOLE_SIZE=80;
  public static final int BUTTON_SIZE=32;
  /*
  private Tweet tweet;
  private Status status;
  private ClientModel model;
  */
  public Console(ClientModel model){
    super();
    this.setPreferredSize(new Dimension(CONSOLE_SIZE,CONSOLE_SIZE));
    this.setMaximumSize(new Dimension(CONSOLE_SIZE,CONSOLE_SIZE));
    this.setLayout(new FlowLayout());
    this.add(createRTButton());
    this.add(createReButton());
    this.add(createFavButton());
    this.add(createOtherButton());
  }
  
  private JButton createRTButton(){
    JButton button = new JButton(new ImageIcon("./gui/rt.png") );
    button.setPreferredSize(new Dimension(BUTTON_SIZE,BUTTON_SIZE));
    button.setMaximumSize(new Dimension(BUTTON_SIZE,BUTTON_SIZE));
    button.addActionListener(new ReTweet());
    return button;
  }
  private JButton createReButton(){
    JButton button = new JButton(new ImageIcon("./gui/re.png") );
    button.setPreferredSize(new Dimension(BUTTON_SIZE,BUTTON_SIZE));
    button.setMaximumSize(new Dimension(BUTTON_SIZE,BUTTON_SIZE));
    //button.addActionListener(new Reply());
    return button;
  } private JButton createFavButton(){
    JButton button = new JButton(new ImageIcon("./gui/Fav.png") );
    button.setPreferredSize(new Dimension(BUTTON_SIZE,BUTTON_SIZE));
    button.setMaximumSize(new Dimension(BUTTON_SIZE,BUTTON_SIZE));
    button.addActionListener(new Favorite());
    return button;
  } private JButton createOtherButton(){
    JButton button = new JButton(new ImageIcon("./gui/other.png") );
    button.setPreferredSize(new Dimension(BUTTON_SIZE,BUTTON_SIZE));
    button.setMaximumSize(new Dimension(BUTTON_SIZE,BUTTON_SIZE));
    return button;
  }
  
  class ReTweet implements ActionListener{
    private Status status;
    private Twitter twitter;
    public ReTweet(){
      super();
    }
    public void setStatus(Status status,Twitter twitter){
      this.twitter=twitter;
      this.status=status;
    }
    @Override
    public void actionPerformed(ActionEvent arg0) {
      if(!this.status.isRetweetedByMe()){
        try {
          this.twitter.retweetStatus(this.status.getId());
        } catch (TwitterException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      };
    }
  }
  
  
  class Favorite implements ActionListener{
    private Status status;
    private Twitter twitter;
    
    public Favorite(){
      super();
    }
    
    public void setStatus(Status status,Twitter twitter){
      this.twitter=twitter;
      this.status=status;
    }
    @Override
    public void actionPerformed(ActionEvent arg0) {
      if(!this.status.isFavorited()){
        try {
          this.twitter.createFavorite(this.status.getId());
        } catch (TwitterException e) {
          // TODO Auto-generated catch block
          try {
            this.twitter.destroyFavorite(this.status.getId());
          } catch (TwitterException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
          }
          e.printStackTrace();
        }
      }
    }
  }

}
