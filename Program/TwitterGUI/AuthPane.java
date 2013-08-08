package TwitterGUI;

import java.awt.BorderLayout;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class AuthPane extends JFrame implements ActionListener{
  private static final long serialVersionUID = 1L;

  AuthPane(){
    JButton infoButton = new JButton("認証ページを開く");
    infoButton.addActionListener(this);

    JPanel p = new JPanel();
    p.add(infoButton);

    getContentPane().add(p, BorderLayout.CENTER);
  }
  public void actionPerformed(ActionEvent e){
  }
}