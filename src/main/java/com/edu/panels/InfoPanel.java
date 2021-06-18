package com.edu.panels;


import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
/**
 * 
 *show info in this panel
 */
public class InfoPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	//text area component
	private JTextArea infos;
	//content
    @SuppressWarnings("unused")
	private String info;

    
    //construct to info panel
    public InfoPanel(){
    	//Initialization component
        this.init();
    }

    /**
     * Initialization component
     */
    private void init(){
    	//set text area size
        this.infos = new JTextArea(20,20);
        //Panel with scroll bar
        JScrollPane scroll = new JScrollPane(this.infos);
        //add horizontal scroll bar
        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      //add vertical scroll bar
        scroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        //add scroll to this panel
        this.add(scroll);
        //set background color is white
        this.setBackground(Color.white);
        //set border color and type
        this.infos.setBorder(new LineBorder(new java.awt.Color(127,157,185), 1, false));


    }
    
    //set data
    public void setText(String info){
        this.infos.setText(info);
    }
}
