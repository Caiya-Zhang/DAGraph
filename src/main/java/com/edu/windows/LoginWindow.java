package com.edu.windows;


import javax.swing.*;

import com.edu.analysis.CO2Emissions;
import com.edu.analysis.CO2vsEnergyvsPM25;
import com.edu.analysis.CO2vsGDP;
import com.edu.analysis.CleanFuelsAnalisis;
import com.edu.analysis.EnergyUse;
import com.edu.analysis.FoodProductionAnalisis;
import com.edu.analysis.ForestAnalysis;
import com.edu.analysis.GDPAnalisis;
import com.edu.analysis.HeathCapitaAndMortalityAnaysis;
import com.edu.analysis.HospitalBedsAnalysis;
import com.edu.analysis.HospitalBedsAndHealthExpenditureAnalysis;
import com.edu.analysis.MortalityAnalysis;
import com.edu.analysis.PM25;
import com.edu.analysis.PM25andForestArea;
import com.edu.analysis.Education;
import com.edu.analysis.EducationandHealth;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
/**
 * Login window
 *
 */
public class LoginWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private static  final String FILE_NAME = "users.txt";
    //user name label
    private JLabel usernameLabel;
    //user name input field
    private JTextField usernameField;
    //password label
    private JLabel passwordLabel;
    //password input field
    private JPasswordField passwordField;
    //login button
    private JButton loginButton;
    //exit button
    private JButton exitButton;
    //main panel by login window
    private JPanel content;

    //construct by login window
    public LoginWindow(){
    	//Initialization component
        init();
        //set style to component
        setStyle();
        //add event listen to button
        addEventHandler();
    }
    /**
     * Initialization component
     */
    private void init(){
        usernameLabel = new JLabel("username:");
        usernameField = new JTextField(20);
        passwordLabel = new JLabel("password:");
        passwordField = new JPasswordField(20);
        loginButton = new JButton("login");
        exitButton = new JButton("exit");
        content = new JPanel();
    }

    /**
     * set styles
     */
    private void setStyle(){
//        Container content = this.getContentPane();
    	//set size to window
        this.setSize(new Dimension(300,200));
        //main panel add component
        //main panel add component
        content.add(usernameLabel);
        //main panel add component
        content.add(usernameField);
        //main panel add component
        content.add(passwordLabel);
        //main panel add component
        content.add(passwordField);
        //main panel add component
        content.add(loginButton);
        //main panel add component
        content.add(exitButton);
        //set main panel to window
        this.setContentPane(content);
        //set window show in center to screen
        this.setLocationRelativeTo(null);
        //set close to exit application
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }



    /**
     * add event listen to button
     */
    private void addEventHandler(){
    	//a pointer to login window
        JFrame that = this;
        //login button event 
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	//get user name
                String username = usernameField.getText();
                //if user name is empty,give a message
                if(username == null || username.trim().equals("")){
                    JOptionPane.showMessageDialog(null,"username is empty","error",1);
                    return;
                }
                //get password
                char[] passwordByte = passwordField.getPassword();
              //if password is empty,give a message
                if (passwordByte == null){
                    JOptionPane.showMessageDialog(null,"password is empty","error",1);
                    return;
                }
                String password = new String(passwordByte);
                FileReader reader = null;
                BufferedReader reader1 = null;
                try {
                	//read users info from file
                    reader = new FileReader(new File(LoginWindow.class.getClassLoader().getResource(FILE_NAME).toURI()));
                    reader1 = new BufferedReader(reader);
                    String line = null;
                    while((line = reader1.readLine()) != null){
                        String[] split = line.split(",");
                        //If the user name and password are all correct, execute the login process
                        if (username.equals(split[0]) && password.equals(split[1])){
                        	//set begin year 
                            int beginYear = 2007;
                            //Instantiate the main interface window
                            MainWindow window =  new MainWindow(beginYear);
                            //Add parser
                            window.addMethods("Average Forest area (% of land area) for the selected years",new ForestAnalysis());
                            //Add parser
                            window.addMethods("Mortality rate, under-5 (per 1,000 live births)",new MortalityAnalysis());
                            //Add parser
                            window.addMethods("Hospital beds (per 1,000 people)",new HospitalBedsAnalysis());
                            //Add parser
                            window.addMethods("Food production index (2004-2006 = 100)",new FoodProductionAnalisis());
                            //Add parser
                            window.addMethods("Access to clean fuels and technologies for cooking (% of population)",new CleanFuelsAnalisis());
                            //Add parser
                            window.addMethods("GDP per capita (current US$)",new GDPAnalisis());
//                          //Add parser
                            window.addMethods("Current health expenditure per capita (current US$) vs Mortality rate, infant (per 1,000 live births)",new HeathCapitaAndMortalityAnaysis());
                            //Add parser
                            window.addMethods("Ratio of Hospital beds (per 1,000 people) and Current health expenditure (per 1,000 people)",new HospitalBedsAndHealthExpenditureAnalysis());
                            //Add parser
                            window.addMethods("CO2 emissions (metric tons per capita)",new CO2Emissions());
                            //Add parser
                            window.addMethods("PM2.5 air pollution, mean annual exposure (micrograms per cubic meter)",new PM25());
                            //Add parser
                            window.addMethods("Energy use (kg of oil equivalent per capita)",new EnergyUse());
                           //Add parser
                            window.addMethods("CO2 emissions VS Energy use VS PM2.5 air pollution",new CO2vsEnergyvsPM25());
                            //Add parser
                            window.addMethods("Ratio of CO2 emissions (metric tons per capita) and GDP per capita (current US$)",new CO2vsGDP());
                            //Add parser
                            window.addMethods("PM2.5 air pollution, mean annual exposure (micrograms per cubic meter) vs Forest Area (% of land area)",new PM25andForestArea());
                            //Add parser
                            window.addMethods("Average of Government expenditure on education, total (% of GDP) for the selected years", new Education());
                            //Add parser
                            window.addMethods("Ratio of Government expenditure on education, total (% of GDP) vs Current health expenditure (% of GDP)",new EducationandHealth());
                            //set main window is show
                            window.setVisible(true);
                            //login window destroy
                            that.dispose();
                            return;
                        }
                    }
                    //user name or password is wrong
                    JOptionPane.showMessageDialog(null,"username or password is error","error",1);
                    return;

                }catch (Exception error){
                    error.printStackTrace();
                }finally {
                    if (reader != null){
                        try {
                            reader.close();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }

                    }
                    if (reader1!= null){
                        try {
                            reader1.close();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }
            }
        });
        
        //exit button event
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	//exit application
                that.dispose();
            }
        });
    }
}
