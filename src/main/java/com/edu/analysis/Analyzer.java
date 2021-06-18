package com.edu.analysis;

import org.jfree.chart.ChartPanel;

import com.edu.constants.Contants;

import java.util.Map;
/**
 * interface to parser
 *
 */
public interface Analyzer extends Contants{

	//parse method
    public void analysis();
    //get parse result
    public Map<String, ChartPanel> getResult();
    //set country
    public void setCountry(String country);
    //set begin year
    public void setFromDate(Integer fromDate);
    //set end year
    public void setToDate(Integer toDate);
    //get parse data
    public String getData();

}
