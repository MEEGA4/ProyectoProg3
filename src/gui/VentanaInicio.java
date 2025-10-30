package gui;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class VentanaInicio extends JFrame{
	private static final long serialVersionUID = 1L;
	
	public VentanaInicio() {
		
		ImageIcon im = new ImageIcon("resources/images/logo.jpeg");
		setIconImage(im.getImage());
        
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(300, 200, 600, 400);
        setLocationRelativeTo(null);
		
		
		
		
		setVisible(true);
	}
}
