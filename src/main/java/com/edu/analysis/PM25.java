package com.edu.analysis;

import java.awt.Color;
import java.text.DecimalFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.RangeType;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.google.gson.JsonArray;

public class PM25 extends AbstractAnlyzer{

    
    @Override
    /**
     * do analysis
     */
    public void analysis() {
        data = "PM2.5 air pollution, mean annual exposure (micrograms per cubic meter):\n";
        JsonArray array = getResponse(PM25_Air_Polution_STR_0);
        createLineChart(array);
        createBarChart(array);
        createScattChart(array);
    }
    

    /**
     * create line chart	
     * @param jsonArray data
     */
    private void createLineChart(JsonArray jsonArray){
    	//create data set
        DefaultCategoryDataset mDataset = new DefaultCategoryDataset();
        //for each json array
        JsonArray array = jsonArray.get(1).getAsJsonArray();
        for (int i = 0 ; i < array.size(); i++){
        	//get date
            int year = array.get(i).getAsJsonObject().get("date").getAsInt();
            //get value
            double value = array.get(i).getAsJsonObject().get("value").getAsDouble();
            //add value to data set
            mDataset.addValue(value,"PM2.5 air pollution(micrograms per cubic meter)",year+"");
            //join value to data
            data+= year+": PM2.5 air pollution is "+value+"\n";
        }
        //create line chart
        JFreeChart mChart = ChartFactory.createLineChart(
                "PM2.5 air pollution ",
                "year",
                "micrograms per cubic meter",
                mDataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false);
        //add char panel to result set
        result.put("Line chart ",new ChartPanel(mChart));

    }

    /**
     * create bar chart
     * @param jsonArray data
     */
    private void createBarChart(JsonArray jsonArray){
    	//create data set
        DefaultCategoryDataset  mDataset = new DefaultCategoryDataset();
        //for each json array
        JsonArray array = jsonArray.get(1).getAsJsonArray();
        for (int i = 0 ; i < array.size(); i++){
        	//get date
            int year = array.get(i).getAsJsonObject().get("date").getAsInt();
            //get value
            double value = array.get(i).getAsJsonObject().get("value").getAsDouble();
            //set value to data set
            mDataset.setValue(value,"PM2.5 air pollution (micrograms per cubic meter)",year+"");
        }


//        create bar chart
        JFreeChart chart = ChartFactory.createBarChart("PM2.5 air pollution ",
                "year",
                "micrograms per cubic meter",
                mDataset,
                PlotOrientation.VERTICAL,
                true,
                false,
                false);
        //get plot to chart
        CategoryPlot categoryPlot = chart.getCategoryPlot();
        //get x axis 
        NumberAxis numberAxis = (NumberAxis) categoryPlot.getRangeAxis();
        //set auto tick false
        numberAxis.setAutoTickUnitSelection(false);
        //set tick unit 10
        numberAxis.setTickUnit(new NumberTickUnit(10));
        //set auto range true
        numberAxis.setAutoRangeStickyZero(true);
        //set range type is positive
        numberAxis.setRangeType(RangeType.POSITIVE);
        //set number format is 0.00
        numberAxis.setNumberFormatOverride(new DecimalFormat("0.00"));
        //get bar render
        BarRenderer renderer = (BarRenderer) categoryPlot.getRenderer();
        //set label
        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}",new DecimalFormat("0.00")));
      //add chart panel to result set
        result.put("Bar chart",new ChartPanel(chart));

    }
    
    private void createScattChart(JsonArray emArray){
    	//create data set 
    	XYSeriesCollection mDataset1 =  new XYSeriesCollection();
    	
    	//get data and for each json array
    	JsonArray array = emArray.get(1).getAsJsonArray();
    
    	XYSeries series1 = new XYSeries("PM2.5 air pollution");
    	for (int i = 0 ; i < array.size(); i++){
    		//get date
    		int year = array.get(i).getAsJsonObject().get("date").getAsInt();
    		//get data
    		double value = array.get(i).getAsJsonObject().get("value").getAsDouble();
    		//add health value to series1
    		series1.add(year,value);
    	}
    	//add series to data set1
    	mDataset1.addSeries(series1);
    	//create line chart
    	JFreeChart chart = ChartFactory.createScatterPlot("PM2.5 air pollution", "year", "micrograms per cubic meter", mDataset1);
    	//get plot
    	XYPlot plot = chart.getXYPlot();
    	

    	plot.mapDatasetToRangeAxis(1, 1);
    	
    	//create second y axis
    	XYLineAndShapeRenderer render2 =  new XYLineAndShapeRenderer();
    	//set color
    	render2.setSeriesPaint(0, Color.BLUE);
    	//add render to plot
    	plot.setRenderer(1, render2);
    	render2.setDefaultFillPaint(Color.blue, false);
    	result.put("Scatter chart",new ChartPanel(chart));
    }
}

