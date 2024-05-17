package pero;

import java.awt.LayoutManager;



import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.ActiveEvent;
import javax.swing.*;


public class Project implements ActionListener
{
	
	JFrame Frame1;
	JPanel Panel1;
	JLabel Label1;
	JLabel Label2;
	JLabel Label3;
	JButton Button1;
	JTextField TextField1;
	JPasswordField PasswordField1;
	
	
	
       Project()
       {
    	   
    	Frame1 = new JFrame();
   		Frame1.setTitle("Login Page");
   		Frame1.setBounds(450,250,400,300);
   		Frame1.setDefaultCloseOperation(Frame1.EXIT_ON_CLOSE);
   		Frame1.setVisible(true);
   		
   		
   		Panel1 = new JPanel();
   		Panel1.setBounds(0,0,400,300);
   		Panel1.setLayout(null);
   		Frame1.add(Panel1);
   		
   		
   		Label3 = new JLabel();
		Label3.setBounds(100,180,200,25);
		Label3.setText("<html><font color='red'>*Wrong Username/Password*</font></html>");
		
   		
   		TextField1 = new JTextField();
   		TextField1.setBounds(150, 60, 120, 25);
   		TextField1.setVisible(true);
   		Panel1.add(TextField1);
   		
   		
   		Label1 = new JLabel();
   		Label1.setText("Username");
   		Label1.setBounds(70,60,100,25);
   		Panel1.add(Label1);
   		
   		
   		Label2 = new JLabel();
   		Label2.setText("Password");
   		Label2.setBounds(70,100,100,25);
   		Panel1.add(Label2);
   		
   		
   		PasswordField1 = new JPasswordField();
   		PasswordField1.setBounds(150,100,120,25);
   	    Panel1.add(PasswordField1);
   	    
   	    
   	    Button1 = new JButton();
   	    Button1.setText("Login");
   	    Button1.setBounds(140,140,80,30);
   	    Panel1.add(Button1);
        Panel1.revalidate();
   	    Panel1.repaint();
   	    Button1.addActionListener(this);
   		
       }
       
	public static void main(String[] args) 
	{
		
		new Project();

	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		
		String Username = TextField1.getText();
		String Password = PasswordField1.getText();
		
		
		if(Username.equals("shaan") && Password.equals("open"))
		{
			
			Frame1.dispose();
			TodoListApp newWindow = new TodoListApp();
			
		}
		else
		{
			
			Panel1.add(Label3);
			Panel1.revalidate();
	   	    Panel1.repaint();
	   	    
		}
	}

}
