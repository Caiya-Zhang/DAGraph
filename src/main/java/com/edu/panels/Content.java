package com.edu.panels;

import org.jfree.chart.ChartPanel;

import javax.swing.*;
import java.awt.*;
import java.util.*;
/**
 * 
 * Custom images panel component
 *
 */
public class Content extends JPanel {

	private static final long serialVersionUID = 1L;
	//chat panels
	private Map<String,ChartPanel> panels = new HashMap<>();
	//data info panel
    private InfoPanel infoPanel;

    //construct to images panel
    public Content(){
    	//set layout manager
        this.setLayout(null);
        //Instantiate information components
        this.infoPanel = new InfoPanel();
        //initialization
        this.init();
    }

    public InfoPanel getInfoPanel() {
        return infoPanel;
    }

    public void setInfoPanel(InfoPanel infoPanel) {
        this.infoPanel = infoPanel;
    }

    public Map<String, ChartPanel> getPanels() {
        return panels;
    }

    public void resetPanels() {
        this.panels = new HashMap<>();
        this.init();
    }
    /**
     * initialization
     */
    public  void init(){
    	//get panel width and height
        int width = this.getWidth();
        int beginX = 10;
        int beginY = 5;
        //remove all chart and info panel
        this.removeAll();
        //Traverse add panel
        for (JPanel panel :
                panels.values()) {
            panel.setBounds(beginX,beginY,400,400);
            beginX = beginX + 10 + 400;
            if(beginX+400 > width){
                beginX = 10;
                beginY = 400+5+beginY;
            }

            panel.setBorder(BorderFactory.createLineBorder(Color.black));
            this.add(panel);
        }
        //set info panel location
        infoPanel.setBounds(beginX,beginY,400,400);
      //set info panel border style
        infoPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        this.add(infoPanel);
        this.repaint();
    }
    
    //get chart panel number
    public int itemsNumber(){
        if (infoPanel == null){
            return panels.size();
        }
        return panels.size()+1;
    }

    //change info panel data
    public void changeInfo(String info){
        infoPanel.setText(info);
    }

    //add chart panel
    public void addView(String type,ChartPanel panel){
        ChartPanel item = panels.get(type);
        if (item == null){
            panels.put(type,panel);
            this.init();
        }
    }
    //remove chart panel
    public void removeView(String type){
        ChartPanel item = panels.get(type);
        if (item != null){
            panels.remove(type,item);
            this.init();
        }
    }
}
