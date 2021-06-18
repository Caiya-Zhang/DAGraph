package com.edu.panels;

import javax.swing.*;

import com.edu.windows.MainWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Set;
import java.util.Vector;
/**
 * 
 * Custom footer component
 *
 */
public class Footer extends JPanel {

	private static final long serialVersionUID = 1L;
	//chart type label
	private JLabel viewsLabel;
	//chart type drop down list
    private JComboBox<String> viewsBox;
    //+ button
    private JButton addViewButton;
    //- button
    private JButton subViewButton;
    //parser name list
    private JLabel methodsLabel;
    //drop down parser list
    private JComboBox<String> methods;
    //recalculate button
    private JButton calculateButton;
    //parent window
    private MainWindow parent;
    //data list to parser list
    private Vector<String> comboBoxItems=new Vector<String>();
    //data list to chart types
    private Vector<String> typeBoxItems = new Vector<String>();
    //dataset to parser list
    private DefaultComboBoxModel<String> model;
    //dataset to chart type list
    private DefaultComboBoxModel<String> typesModel;
    //selected parser
    private String method;
    //selected chat type
    private String type;

    /**
     * construct to footer
     * @param parent parent window
     */
    public Footer(MainWindow parent){
        this.parent = parent;
        //Initialization components
        init();
        //add event handler
        addEventHandler();
    }
    /**
     * add parser
     * @param method parser name
     */
    public void addMethod(String method){
    	//add parser name
        this.model.addElement(method);
        //set default selected
        this.method = (String) methods.getItemAt(0);
    }

    /**
     * Initialization components
     */
    private void init(){
    	//Instantiate components
        this.viewsLabel = new JLabel("Available Views:");

        this.addViewButton = new JButton("+");
        this.subViewButton = new JButton("-");

        this.methodsLabel = new JLabel("Choose analysis method:");

        model = new DefaultComboBoxModel<>(comboBoxItems);
        typesModel = new DefaultComboBoxModel<>(typeBoxItems);
        this.viewsBox = new JComboBox<>(typesModel);
        this.methods = new JComboBox<>(model);
        this.calculateButton = new JButton("Recalculate");
        //add components to panel
        this.add(viewsLabel);
        this.add(viewsBox);
        this.add(addViewButton);
        this.add(subViewButton);
        this.add(methodsLabel);
        this.add(methods);
        this.add(calculateButton);

    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * change chart type
     * @param types chart types
     */
    public void addViewTypes(Set<String> types){
        typesModel.removeAllElements();
        for (String type :
                types) {
            typesModel.addElement(type);
        }
        //set default selected
        viewsBox.setSelectedItem(viewsBox.getItemAt(0));
        type = (String) viewsBox.getItemAt(0);
    }

    /**
     * add event handler
     */
    public void addEventHandler(){
    	//change selected parser
        methods.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange()== ItemEvent.SELECTED){
                	//change selected parser
                    method = (String) e.getItem();
                }
            }
        });
        //re calculate data
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	//parent window rebuild
                parent.rebuild();
            }
        });

      //change selected chart type
        viewsBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange()== ItemEvent.SELECTED){
                	//change selected chart type
                    type = (String) e.getItem();
                }
            }
        });
        //add chart panel
        addViewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	//advice parent add chart panel
                parent.addView(type);
            }
        });
      //remove chart panel
        subViewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	//advice parent remove chart panel
                parent.removeView(type);
            }
        });


    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }


}
