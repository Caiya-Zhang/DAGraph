package com.edu.analysis;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Year;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.edu.constants.Contants;
import com.google.gson.JsonArray;
/**
 * 
 * Ratio of Government expenditure on education, total (% of GDP) vs Current health expenditure (% of GDP) Analysis
 *
 */
public class EducationandHealth extends AbstractAnlyzer{
	

    

	@Override
    /**
	 * do analysis
	 */

	public void analysis() {
		data = "Ratio of Government expenditure on education, total (% of GDP) vs Current health expenditure (% of GDP)\n";					
		JsonArray education = getResponse(EDU_EXPENDITURE_STR);               //get data from network
		JsonArray health = getResponse(HEATH_TYPE_STR);					
		

		createLineChart(education, health);
		createBarChart(education, health);
		createScattChart(education, health);
		
	}

	
		private void createLineChart(JsonArray educationArray, JsonArray healthArray){
			
	        XYSeriesCollection dataset1 =  new XYSeriesCollection();			                 //create a xy dataset
	        XYSeriesCollection dataset2 =  new XYSeriesCollection();

	        JsonArray array1 = educationArray.get(1).getAsJsonArray();
	        JsonArray array2 = healthArray.get(1).getAsJsonArray();

	        XYSeries series1 = new XYSeries("Government expenditure on education");			     //set series name
	        XYSeries series2 = new XYSeries("Current health expenditure");
	        
	        for (int i = 0 ; i < array1.size(); i++){											 //add data to dataset and append data string
	            int year = array1.get(i).getAsJsonObject().get("date").getAsInt();
	            
	            double value1 = array1.get(i).getAsJsonObject().get("value").getAsDouble();
	            double value2 = array2.get(i).getAsJsonObject().get("value").getAsDouble();

	            //add health value to series1
	            series1.add(year,value1);
	            //add mortality value to series2
	            series2.add(year,value2);
	            //append data info
	            data+= value1 + "Government expenditure on education" + year + "";
	            data+=value2 + "Current health expenditure" + year + "";
	            
	        }

	        //add health series to dataset1
	        dataset1.addSeries(series1);
	        //add mortality series to dataset2
	        dataset2.addSeries(series2);

	        JFreeChart chart = ChartFactory.createXYLineChart(	        							//create jfreechart instance
	        		"Ratio of Government expenditure on education, total (% of GDP) vs Current health expenditure (% of GDP)",
	                "year",
	                "Government expenditure on education",
	                dataset1,
	                PlotOrientation.VERTICAL,
	                true,
	                true,
	                false);
	        
	        
	        XYPlot plot = chart.getXYPlot();								//get plot from jfreechart
	        NumberAxis axis2 = new NumberAxis("Current health expenditure");
	        
	        axis2.setAxisLinePaint(Color.BLUE);								//set color 
	        axis2.setLabelPaint(Color.BLUE);
	        axis2.setTickLabelPaint(Color.BLUE);        

	        plot.setRangeAxis(1, axis2);							        //set second series range	        
	        plot.setDataset(1, dataset2);									//set second series dataset
	        plot.mapDatasetToRangeAxis(1, 1);

	        XYLineAndShapeRenderer render2 =  new XYLineAndShapeRenderer();
	        render2.setSeriesPaint(0, Color.BLUE);
	        plot.setRenderer(1, render2);						        			//render plot
	        result.put("Line chart",new ChartPanel(chart));					        //add chart panel to result set
	        

	    }
	    


    private void createBarChart(JsonArray educationArray,JsonArray healthArray) {
    	
    	DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();				     //create dataset
    	DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
    
        JsonArray array1 = educationArray.get(1).getAsJsonArray();						 //get data for each json array
        JsonArray array2 = healthArray.get(1).getAsJsonArray();
        for (int i = array1.size()-1 ; i >= 0; i--){
            int year = array1.get(i).getAsJsonObject().get("date").getAsInt();					        //get year
            double value1 = array1.get(i).getAsJsonObject().get("value").getAsDouble();					// get education expenditure value
            double value2 = array2.get(i).getAsJsonObject().get("value").getAsDouble();					// get health expenditure value
            
            //add value to data set
            dataset1.setValue(value1,"Government expenditure on education",year+"");
            dataset2.setValue(value2,"Current health expenditure",year+"");

        }
       
        CategoryPlot plot = new CategoryPlot();									//create bar chart	
        									
		BarRenderer barrenderer1 = new BarRenderer();							//create new render
		BarRenderer barrenderer2 = new BarRenderer();

		plot.setDataset(0, dataset1);
		plot.setRenderer(0, barrenderer1);
		CategoryAxis domainAxis = new CategoryAxis("Year");						// set x-axis 
		plot.setDomainAxis(domainAxis);
		plot.setRangeAxis(new NumberAxis("%"));		                            // set y-axis of dataset1

		plot.setDataset(1, dataset2);
		plot.setRenderer(1, barrenderer2);
		plot.setRangeAxis(1, new NumberAxis("%"));								// set y-axis of dataset2
		
		plot.setRenderer(barrenderer1);										    //set the max bar width
		barrenderer1.setMaximumBarWidth(0.2);
		
		plot.mapDatasetToRangeAxis(0, 0);									    // 1st dataset to 1st y-axis
		plot.mapDatasetToRangeAxis(1, 1); 									    // 2nd dataset to 2nd y-axis

		JFreeChart barChart = new JFreeChart("Ratio of Government expenditure on education, total (% of GDP) vs Current health expenditure (% of GDP)", new Font("Times New Roman", java.awt.Font.BOLD, 24), plot, true);
		
		ChartPanel chartPanel = new ChartPanel(barChart);
		chartPanel.setSize(new Dimension(400, 300));								// set size
		chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));		// set border
		chartPanel.setBackground(Color.white);
		
		result.put("Bar chart",new ChartPanel(barChart));
    	
    	
    }
	

    
    private void createScattChart(JsonArray educationArray, JsonArray healthArray){

    	XYSeriesCollection dataset1 =  new XYSeriesCollection();					// create dataset
    	XYSeriesCollection dataset2 =  new XYSeriesCollection();
    	   	
    	JsonArray array1 = educationArray.get(1).getAsJsonArray();				    //get data for each json array
    	JsonArray array2 = healthArray.get(1).getAsJsonArray();
    	
        XYSeries series1 = new XYSeries("Government expenditure on education");		//set series name
        XYSeries series2 = new XYSeries("Current health expenditure");
    	for (int i = 0 ; i < array1.size(); i++){
    		
    		int year = array1.get(i).getAsJsonObject().get("date").getAsInt();							//get year
    		double value1 = array1.get(i).getAsJsonObject().get("value").getAsDouble();					//get education expenditure value
    		double value2 = array2.get(i).getAsJsonObject().get("value").getAsDouble();					//get health expenditure value
    		
    		series1.add(year,value1);						//add into series
    		series2.add(year,value2);
    	}
    	
    	dataset1.addSeries(series1);						// add into dataset
    	dataset2.addSeries(series2);
    	
		XYPlot plot = new XYPlot();							//create Scatter chart
		XYItemRenderer itemrenderer1 = new XYLineAndShapeRenderer(false, true);				//create new render
		XYItemRenderer itemrenderer2 = new XYLineAndShapeRenderer(false, true);

		plot.setDataset(0, dataset1);
		plot.setRenderer(0, itemrenderer1);
		
		DateAxis domainAxis = new DateAxis("Year");											// set x-axis
		plot.setDomainAxis(domainAxis);
		plot.setRangeAxis(new NumberAxis("%"));					                            // set y-axis of dataset1

		plot.setDataset(1, dataset2);
		plot.setRenderer(1, itemrenderer2);
		plot.setRangeAxis(1, new NumberAxis("%"));											// set y-axis of dataset1	
		
		plot.mapDatasetToRangeAxis(0, 0);						                            // 1st dataset to 1st y-axis
		plot.mapDatasetToRangeAxis(1, 1); 						                            // 2nd dataset to 2nd y-axis

		JFreeChart scatterChart = new JFreeChart("Ratio of Government expenditure on education, total (% of GDP) vs Current health expenditure (% of GDP)", new Font("Times New Roman", java.awt.Font.BOLD, 24),plot, true);	// create jfreechart

		ChartPanel chartPanel = new ChartPanel(scatterChart);
		chartPanel.setSize(new Dimension(400, 300));									     //set size
		chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));				 //set border
		chartPanel.setBackground(Color.white);
		
		result.put("Scatter",new ChartPanel(scatterChart));
    }

}