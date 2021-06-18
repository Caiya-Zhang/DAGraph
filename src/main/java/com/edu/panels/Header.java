package com.edu.panels;

import javax.swing.*;

import com.edu.windows.MainWindow;
import com.edu.windows.MainWindow.Country;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 * 
 * Custom header component
 *
 */
public class Header extends JPanel {

	private static final long serialVersionUID = 1L;
	//country label
	private JLabel countryLabel;
	//Drop down list of countries
    private JComboBox<Country> countries;
    //begin year label
    private JLabel fromLabel;
    //end year label
    private JLabel toLabel;
    //Drop down list of begin years
    private JComboBox<Integer> fromYears;
    //Drop down list of end years
    private JComboBox<Integer> toYears;
    
    //selected country code
    private String iso2code;
    //selected begin year
    //selected begin year
    private Integer fromYear;
    //selected end year
    private Integer toYear;
    
    //countries name list
    private List<MainWindow.Country> countryNames = new ArrayList<>();
    //begin years 
    private Integer beginYear;
    //MainWindow is this panel parent window
    private MainWindow parent;
    /**
     * construct 
     * @param countryNames country name list
     * @param beginYear  begin year
     * @param parent  parent window
     */
    public Header(List<MainWindow.Country> countryNames,int beginYear,MainWindow parent){
    	//set data
        this.countryNames = countryNames;
        //set data
        this.beginYear = 1962;
        //set data
        this.parent = parent;
        //Initialization component
        init();
        //add event to component
        addEventHandler();

    }
  /**
   * Initialization component
   */
    private void init(){
        this.countryLabel = new JLabel("choose a country");
        this.fromLabel = new JLabel("from");
        this.toLabel = new JLabel("to");
        this.countries = new JComboBox<>();
        //add country from country name list
        for (MainWindow.Country country :
                this.countryNames) {
            countries.addItem(country);
        }
        //get code to country
        iso2code = ((MainWindow.Country) countries.getItemAt(0)).getIso2Code();
        //Initialization Drop down list of begin years
        this.fromYears = new JComboBox<>();
        int year = LocalDate.now().getYear();
        for(int i =  beginYear; i < year ; i++){
            this.fromYears.addItem(i);
        }
      //Initialization Drop down list of end years
        this.toYears = new JComboBox<>();
        for(int i = beginYear+1;i <= year ; i ++){
            this.toYears.addItem(i);

        }
        //set end year is selected 0
        this.toYears.setSelectedIndex(0);
        //set begin year is selected 0
        this.fromYears.setSelectedIndex(0);
        fromYear = (Integer) this.fromYears.getItemAt(0);
        toYear = (Integer) this.toYears.getItemAt(0);
        //add components to panel
        this.add(this.countryLabel);
        this.add(countries);
        this.add(fromLabel);
        this.add(fromYears);
        this.add(toLabel);
        this.add(toYears);
    }

    /**
     * add event handler
     */
    private void addEventHandler(){
    	//begin years select changed
        fromYears.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange()== ItemEvent.SELECTED){
                	//change from year value
                   fromYear = (Integer) e.getItem();
                   //parent window change data
                   parent.changeData();
                }
            }
        });
      //end years select changed
        toYears.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange()== ItemEvent.SELECTED){
                	//change to year value
                    toYear = (Integer) e.getItem();
                  //parent window change data
                    parent.changeData();
                }
            }
        });
      //country selected changed
        countries.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange()== ItemEvent.SELECTED){
                	//change country value
                    MainWindow.Country country = (MainWindow.Country) e.getItem();
                  //change country code value
                    iso2code = country.getIso2Code();
                  //parent window change data
                    parent.changeData();
                }
            }
        });
    }

    public String getIso2code() {
        return iso2code;
    }

    public void setIso2code(String iso2code) {
        this.iso2code = iso2code;
    }

    public Integer getFromYear() {
        return fromYear;
    }

    public void setFromYear(Integer fromYear) {
        this.fromYear = fromYear;
    }

    public Integer getToYear() {
        return toYear;
    }

    public void setToYear(Integer toYear) {
        this.toYear = toYear;
    }
}
