package com.edu.analysis;

import com.edu.exception.MyException;
import com.google.gson.JsonArray;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.RangeType;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.*;
import java.text.DecimalFormat;


/**
* Average of Government expenditure on education, total (% of GDP) for the selected yearsAnalysis
*/

public class Education extends AbstractAnlyzer{

    @Override
    /**
	 * do analysis
	 */

    public void analysis() {

    	
        if (!toDate.equals(fromDate)){                                          //check dates
            throw new MyException("must have a selected years ");
        }
        
        data = "Average of Government expenditure on education, total (% of GDP) for the selected years:\n";            //initial data
        
        JsonArray jsonArray = getResponse(EDU_EXPENDITURE_STR);                 //get data from network

        createPieChart(jsonArray);
        createBarChart(jsonArray);
        createLineChart(jsonArray);

    }

   

    private void createPieChart(JsonArray jsonArray){
        int date = jsonArray.get(1).getAsJsonArray().get(0).getAsJsonObject().get("date").getAsInt();           	//get date        
        double persent = jsonArray.get(1).getAsJsonArray().get(0).getAsJsonObject().get("value").getAsDouble();         //get value
        
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();                      //create dataset       
        dataset.setValue("expenditure on education", persent);                              //set value to dataset      
        dataset.setValue("other", 100-persent);                                             //set value to dataset 
        data += "Average of Government expenditure on education, total (% of GDP) for the selected years" + fromDate + ":" + persent + "%";
        
        JFreeChart chart = ChartFactory.createPieChart("Average of Government expenditure on education", dataset, true, true, false);     //create pie chart
        
        chart.setTitle(new TextTitle("Average of Government expenditure on education, total (% of GDP) for the selected years" + date));   //set title      
        result.put("Pie chart", new ChartPanel(chart));                                     //add chart panel to result set

    }


                       
                       
    private void createBarChart(JsonArray jsonArray){
    	
        int date = jsonArray.get(1).getAsJsonArray().get(0).getAsJsonObject().get("date").getAsInt();                   //get date
        double persent = jsonArray.get(1).getAsJsonArray().get(0).getAsJsonObject().get("value").getAsDouble();         //get value
        
        DefaultCategoryDataset  dataset = new DefaultCategoryDataset();                             //create dataset      
        dataset.setValue(persent, "government education expenditure", "" + date);                   //set value to dataset     
        dataset.setValue(100-persent, "other", "" + date);                                          //set value to dataset
        
        JFreeChart chart = ChartFactory.createBarChart(                                             //create bar chart 
                "government expenditure on education",                    
                "Average of Government expenditure on education, total (% of GDP) for the selected years",
                "%",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                false,
                false);
        
        CategoryPlot categoryPlot = chart.getCategoryPlot();                            //get plot
        
        NumberAxis numberAxis = (NumberAxis) categoryPlot.getRangeAxis();               //get x axis 
        numberAxis.setAutoTickUnitSelection(false);                                     //set auto tick false       
        numberAxis.setTickUnit(new NumberTickUnit(10));                                 //set tick unit 10    
        numberAxis.setAutoRangeStickyZero(true);                                        //set auto range true     
        numberAxis.setRangeType(RangeType.POSITIVE);                                    //set range type is positive   
        numberAxis.setNumberFormatOverride(new DecimalFormat("0.00"));                  //set number format is 0.00
        
        BarRenderer renderer = (BarRenderer) categoryPlot.getRenderer();                //get bar render   
        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}",new DecimalFormat("0.00")));                 //set label  
        result.put("Bar chart",new ChartPanel(chart));                                  //add chart panel to result set
                

    }
                       
    
                       

    private void createLineChart(JsonArray jsonArray){
    	
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();                              //create data set
        JsonArray array = jsonArray.get(1).getAsJsonArray();
        for (int i = 0 ; i < array.size(); i++){
            int year = array.get(i).getAsJsonObject().get("date").getAsInt();                       //get year            
            double value = array.get(i).getAsJsonObject().get("value").getAsDouble();               //get value       
            dataset.addValue(value, "Average of Government expenditure on education, total (% of GDP)", year + "");          //add value to data set         
            data += year + ": Average of Government expenditure on education is " + value + "%" + "\n";                      //join value to data
        }
        
        JFreeChart mChart = ChartFactory.createLineChart(                                           //create line chart
                "Average of Government expenditure on education, total (% of GDP) for the selected years",
                "year",
                "%",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false);
       
        result.put("Line chart ",new ChartPanel(mChart));                                           //add char panel to result set

    }


}
