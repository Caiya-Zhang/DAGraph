package com.edu.windows;


import com.edu.analysis.Analyzer;
import com.edu.exception.MyException;
import com.edu.panels.Content;
import com.edu.panels.Footer;
import com.edu.panels.Header;
import com.edu.utils.HttpClientUtils;
import com.google.gson.JsonArray;

import org.jfree.chart.ChartPanel;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
/**
 * main window
 *
 */
public class MainWindow extends JFrame {


	private static final long serialVersionUID = 1L;
	
	//get countries URL
	private static final String COUNTRY_NAMES_URL = "http://api.worldbank.org/v2/country?format=json";
	//This is a custom header component
    private Header header;
    //This is a custom footer component
    private Footer footer;
    //This is a custom content component
    private Content content;
    //This is the main panel of the window
    private JPanel mainPanel;
    //National Assembly
    private static List<Country> countries = new ArrayList<>();
    //Parser list
    private Map<String, Analyzer> methods = new HashMap<>();
    //Picture list
    private Map<String, ChartPanel> results = new HashMap<>();
    //begin year
    private Integer beginYear;

    static {
    	//get countries from the Internet
        JsonArray jsonArray =  HttpClientUtils.getJSONObject(COUNTRY_NAMES_URL).get(1).getAsJsonArray();
        for( int i = 0; i < jsonArray.size(); i ++){
        	//get country name
            String name = jsonArray.get(i).getAsJsonObject().get("name").getAsString();
            //get country ISO code
            String iso2Code = jsonArray.get(i).getAsJsonObject().get("iso2Code").getAsString();
            //Instantiate country objects
            Country country = new Country();
            //set name
            country.setName(name);
            //set code
            country.setIso2Code(iso2Code);
            //add country to countries list
            countries.add(country);

        }
    }

    /**
     * construct to main window
     * @param beginYear begin year to set
     */
    public MainWindow(int beginYear){
    	//set begin year
        this.beginYear = beginYear;
        //Initialization component
        init();
        //set style to component
        setStyles();
    }

    /**
     * Register parser
     * @param methodName parser name
     * @param analyzer Parser
     */
    public void addMethods(String methodName, Analyzer analyzer){
    	//add parser to parser list
        methods.put(methodName,analyzer);
        //add parser name list to footer component parser name list
        footer.addMethod(methodName);
    }
    
    /**
     * Initialization component
     */
    private void init(){
    	//Instantiate custom header component
        header = new Header(countries,beginYear,this);
        //Instantiate custom footer component
        footer = new Footer(this);
        //Instantiate custom images panel component
        content = new Content();
        //Instantiate  main panel 
        this.mainPanel = new JPanel();
    }



    /**
     * set style to components
     */
    private void setStyles(){
    	//set main panel to this window
        this.setContentPane(this.mainPanel);
        //set main panel layout manager is border layout
        this.mainPanel.setLayout(new BorderLayout());
        //set this window size
        this.setMinimumSize(new Dimension(1240,865));
        //The size cannot be changed
        this.setResizable(true);
        //set custom images panel component absolute positioning layout
        content.setLayout(null);
        //set custom images panel component background color is white
        content.setBackground(Color.white);
        //set custom header component to header
        this.mainPanel.add(header,BorderLayout.NORTH);
        //set custom images panel component to center
        this.mainPanel.add(content,BorderLayout.CENTER);
        //set custom footer component to footer
        this.mainPanel.add(footer,BorderLayout.SOUTH);
        //set window show center in screen
        this.setLocationRelativeTo(null);
        //close window exit application
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * change chart 
     */
    public void changeData(){
    	//if custom images panel component have zero chart ,No change is required
        if(content.itemsNumber() == 0){
            return;
        }
        //Initialize parser
        initAnalyzer();
        //Get the existing chart panel list
        Map<String,ChartPanel> panels=content.getPanels();
        for (String key :
                panels.keySet()) {
        	//Convenience set merge change value
            panels.put(key,results.get(key));
            
        }
        //Re render
        content.init();


    }

    /**
     * Initialize parser
     */
    private void initAnalyzer(){
    	//Get the selected country 
        String country = header.getIso2code();
        //Get the selected begin year
        int fromDate = header.getFromYear();
        //Get the selected end year
        int toDate = header.getToYear();
        //Prompt if the start year is greater than the end year 
        if (fromDate > toDate){
            JOptionPane.showMessageDialog(null,"begin date bigger than end date");
            return;
        }
        //Get the selected parser name
        String method = footer.getMethod();
      //Get  parser by parser name
        Analyzer analyzer = methods.get(method);
        //set end year
        analyzer.setToDate(toDate);
      //set begin year
        analyzer.setFromDate(fromDate);
      //set country
        analyzer.setCountry(country);
        try {
        	//Parsing data
            analyzer.analysis();
            //get result from parser
            results = analyzer.getResult();
            //change data info
            content.changeInfo(analyzer.getData());
            //get chart types
            Set<String> types = analyzer.getResult().keySet();
            //add chart types to footer component
            footer.addViewTypes(types);
        }catch (MyException e){
            JOptionPane.showMessageDialog(null,e.getMessage(),"error",1);
            return;
        }catch (Exception e){
        	e.printStackTrace();
            JOptionPane.showMessageDialog(null,"the year or the country no data","error",1);
            return;
        }
    }

    /**
     * add chart image panel to images panel
     * @param type chart type
     */
    public void addView(String type){
    	//get chart panel
        ChartPanel panel = results.get(type);
        //add chart panel to show
        content.addView(type,panel);
    }

    /**
     * remove chart image panel to images panel
     * @param type chart type
     */
    public void removeView(String type){
    	//remove chart panel to show
        content.removeView(type);
    }

    /**
     * Reanalysis of data
     */
    public void rebuild(){
    	//reset images panel
        content.resetPanels();
        //Initialize parser
        initAnalyzer();
    }
    
    //Inner class
    public static class Country {
    	//country name
        private String name;
        //country code
        private String iso2Code;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIso2Code() {
            return iso2Code;
        }

        public void setIso2Code(String iso2Code) {
            this.iso2Code = iso2Code;
        }

        //Override the to String method for easy display
        @Override
        public String toString() {
            return name;
        }
    }



}
