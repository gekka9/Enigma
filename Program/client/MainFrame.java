package client;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import twitter4j.Status;

public class MainFrame extends JFrame{
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
    this.postField=new PostField(this.model);
    this.model.setField(this.postField);
    this.getContentPane().add(this.postField);

    this.listModel = new DefaultListModel();
    this.list = new JList(this.listModel);
    this.list.setLayoutOrientation(JList.VERTICAL);
    CellButtonsMouseListener cbml = new CellButtonsMouseListener();
    list.addMouseListener(cbml);
    list.addMouseMotionListener(cbml);
    this.list.setCellRenderer(new TweetRenderer(model.getMode()));
    this.list.setEnabled(false);
    JScrollPane scrollPane = new JScrollPane(this.list);
    scrollPane.setPreferredSize(new Dimension(SCROLLPANE_WIDTH,SCROLLPANE_HEIGHT));
    this.getContentPane().add(scrollPane);
    this.pack();
    
    EnigmaMenu menu = new EnigmaMenu(listModel,this.model);
    this.setMenuBar(menu);
  
    this.setVisible(true);
  } 

  public void addPost(Status status){
    this.listModel.add(0,new TweetData(status,this.model));
  }

  public PostField getPostField() {
    return this.postField;
  } 
}

class CellButtonsMouseListener extends MouseAdapter{
  
  public CellButtonsMouseListener(){
  }

  @Override public void mousePressed(MouseEvent e) {
      JList list = (JList)e.getComponent();
      Point pt = e.getPoint();
      int index  = list.locationToIndex(pt);
      if(index>=0) {
          JButton button = getButton(list, pt, index);
          if(button != null) {
              TweetRenderer renderer = (TweetRenderer)list.getCellRenderer();
              renderer.pressedIndex = index;
              renderer.button = button;
              list.repaint(list.getCellBounds(index, index));
          }
      }
  }
  @Override public void mouseReleased(MouseEvent e) {
      JList list = (JList)e.getComponent();
      Point pt = e.getPoint();
      int index  = list.locationToIndex(pt);
      if(index>=0) {
          JButton button = getButton(list, pt, index);
          if(button != null) {
              TweetRenderer renderer =( TweetRenderer)list.getCellRenderer();
              renderer.pressedIndex = -1;
              renderer.button = null;
              button.doClick();
              Rectangle r = list.getCellBounds(index, index);
              if(r!=null) {
                  list.repaint(r);
              }
          }
      }
  }
  
  private JButton getButton(JList list, Point pt, int index) {
    System.out.println(pt);
      int cellIndex = list.locationToIndex(pt);
      TweetData data= (TweetData) list.getModel().getElementAt(cellIndex);
      Container c = (Container)list.getCellRenderer().getListCellRendererComponent(list, data, index, false, false);
      Rectangle r = list.getCellBounds(index, index);
      c.setBounds(r);
      pt.translate(0,-r.y);
      Component b = SwingUtilities.getDeepestComponentAt(c, pt.x, pt.y);
      if(b instanceof JButton) {
          return (JButton)b;
      }else{
          return null;
      }
  }
}