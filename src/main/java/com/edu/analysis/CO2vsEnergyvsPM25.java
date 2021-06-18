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
 * Ratio of Hospital beds (per 1,000 people) and Current health expenditure (per 1,000 people) Analysis
 *
 */
public class CO2vsEnergyvsPM25 extends AbstractAnlyzer{
	

    

	@Override
	/**
	 * do analysis
	 */
	public void analysis() {
		data = " CO2 emissions vs Energy use vs PM2.5 air pollution(mean annual exposure)\n";
		//get data from network
		JsonArray emissions = getResponse(Contants.CO2_Emissions_STR_2);
		//get data from network
		JsonArray pm = getResponse(Contants.PM25_Air_Polution_STR_1);
		JsonArray energy = getResponse(Contants.ENERGY_USE_STR_1);
		createLineChart(emissions, energy,pm);
		createBarChart(emissions, energy,pm);
		createScattChart(emissions, energy,pm);
		
	}
	
	/**
	 * create line chart
	 */
	private void createLineChart(JsonArray Arr1,JsonArray Arr2, JsonArray Arr3){

		XYSeriesCollection mDataset1 =  new XYSeriesCollection();
        XYSeriesCollection mDataset2 =  new XYSeriesCollection();
        XYSeriesCollection mDataset3 =  new XYSeriesCollection();

        JsonArray array1 = Arr1.get(1).getAsJsonArray();
        JsonArray array2 = Arr2.get(1).getAsJsonArray();
        JsonArray array3 = Arr3.get(1).getAsJsonArray();
        //set series name
        XYSeries series1 = new XYSeries("CO2 Emission");
        XYSeries series2 = new XYSeries("Energy Use");
        XYSeries series3 = new XYSeries("PM2.5 Air Pollution");
        //add data to data set and append data string
        for (int i = 0 ; i < array1.size(); i++){
            int year = array1.get(i).getAsJsonObject().get("date").getAsInt();
            double value1 = array1.get(i).getAsJsonObject().get("value").getAsDouble();
            double value2 = array2.get(i).getAsJsonObject().get("value").getAsDouble();
            double value3 = array3.get(i).getAsJsonObject().get("value").getAsDouble();
            series1.add(year,value1);
            series2.add(year,value2);
            series3.add(year,value3);
            data+= year+":";
            data+="CO2 Emission is "+value1 +", Energy Use is "+value2+", PM2.5 Air Pollution is "+value3+ "\n";
        }
        //add series to data set
        mDataset1.addSeries(series1);
        mDataset2.addSeries(series2);
        mDataset3.addSeries(series3);


        //create jfreechart instance
        JFreeChart chart = ChartFactory.createXYLineChart("CO2 emissions vs Energy use vs PM2.5 air pollution(mean annual exposure)",
                "year",
                "Enerygy Use(kg of oil equivalent per capita)",
                mDataset2,
                PlotOrientation.VERTICAL,
                true,
                true,
                false);
        
        //get plot from jfreechart
        XYPlot plot = chart.getXYPlot();
        NumberAxis axis2 = new NumberAxis("CO2 Emissions(metric tons per capita)");
        plot.setRangeAxis(1,axis2);
		plot.setDataset(1, mDataset1);
        axis2.setAxisLinePaint(Color.BLUE);
        axis2.setLabelPaint(Color.BLUE);
        axis2.setTickLabelPaint(Color.BLUE);
        plot.mapDatasetToRangeAxis(1, 1);
        
        XYPlot plot2 = chart.getXYPlot();
        NumberAxis axis3 =  new NumberAxis("PM2.5(micrograms per cubic meter)");
        plot2.setRangeAxis(2,axis3);
        plot2.setDataset(2,mDataset3);
        axis3.setAxisLinePaint(Color.GREEN);
        axis3.setLabelPaint(Color.GREEN);
        axis3.setTickLabelPaint(Color.GREEN);
        plot2.mapDatasetToRangeAxis(2, 2);

        XYLineAndShapeRenderer render2 =  new XYLineAndShapeRenderer();
        XYLineAndShapeRenderer render3 =  new XYLineAndShapeRenderer();
        render2.setSeriesPaint(1, Color.BLUE);
        render3.setSeriesPaint(3, Color.GREEN);
        //render plot
        plot.setRenderer(1, render2);
        plot2.setRenderer(2, render3);

        //add chart panel to result set
        result.put("Line chart",new ChartPanel(chart));
    }

    /**
     * create bar chart
     */
    private void createBarChart(JsonArray Arr1,JsonArray Arr2, JsonArray Arr3) {
    	//create data set
    	DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
    	DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
    	DefaultCategoryDataset dataset3 = new DefaultCategoryDataset();
        //for each json array
        JsonArray array1 = Arr1.get(1).getAsJsonArray();
        JsonArray array2 = Arr2.get(1).getAsJsonArray();
        JsonArray array3 = Arr3.get(1).getAsJsonArray();
        for (int i = array1.size()-1 ; i >= 0; i--){
        	//date
            int year = array1.get(i).getAsJsonObject().get("date").getAsInt();
            //value
            double value1 = array1.get(i).getAsJsonObject().get("value").getAsDouble();
            double value2 = array2.get(i).getAsJsonObject().get("value").getAsDouble();
            double value3 = array3.get(i).getAsJsonObject().get("value").getAsDouble();
            //add value to data set
            dataset1.setValue(value1,"CO2 Emissions",year+"");
            dataset2.setValue(value2,"Energy Use",year+"");
            dataset3.setValue(value3,"PM2.5 Air Pollution",year+"");
        }
       
        //create bar chart
        CategoryPlot plot = new CategoryPlot();
        
		BarRenderer barrenderer1 = new BarRenderer();
		BarRenderer barrenderer2 = new BarRenderer();
		BarRenderer barrenderer3 = new BarRenderer();

		CategoryAxis domainAxis = new CategoryAxis("Year");
		plot.setDomainAxis(domainAxis);
		
		NumberAxis axis1 = new NumberAxis("CO2 Emissions");
		NumberAxis axis2 = new NumberAxis("Energy Use");
		NumberAxis axis3 = new NumberAxis("PM2.5 Air Pollution");

		plot.setRangeAxis(0, axis1);
		plot.setDataset(0, dataset1);
		plot.setRenderer(0, barrenderer1);
		barrenderer1.setMaximumBarWidth(0.05);

		plot.setDataset(1, dataset3);
		plot.setRenderer(1, barrenderer3);
		plot.setRangeAxis(1,axis3);
		plot.mapDatasetToRangeAxis(1, 1);
		barrenderer3.setMaximumBarWidth(0.15);

		plot.setDataset(2, dataset2);
		plot.setRenderer(2, barrenderer2);
		plot.setRangeAxis(2,axis2);
		plot.mapDatasetToRangeAxis(2, 2);
		barrenderer2.setMaximumBarWidth(0.3);

		JFreeChart barChart = new JFreeChart("CO2 emissions vs Energy use vs PM2.5 air pollution",
				new Font("Serif", java.awt.Font.BOLD, 18), plot, true);
		
		ChartPanel chartPanel = new ChartPanel(barChart);
		chartPanel.setPreferredSize(new Dimension(400, 300));
		chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		chartPanel.setBackground(Color.white);
		
		result.put("Bar chart",new ChartPanel(barChart));
    	
    }
	
    /**
     * create scatter chart
     */
    private void createScattChart(JsonArray Arr1,JsonArray Arr2, JsonArray Arr3){
    	//create data set 
    	XYSeriesCollection mDataset1 =  new XYSeriesCollection();
    	XYSeriesCollection mDataset2 =  new XYSeriesCollection();
    	XYSeriesCollection mDataset3 =  new XYSeriesCollection();
    	//get data and for each json array
    	JsonArray array1 = Arr1.get(1).getAsJsonArray();
    	JsonArray array2 = Arr2.get(1).getAsJsonArray();
    	JsonArray array3 = Arr3.get(1).getAsJsonArray();
    	//set series name
        XYSeries series1 = new XYSeries("CO2 Emissions");
        XYSeries series2 = new XYSeries("Energy Use");
        XYSeries series3 = new XYSeries("PM2.5 Air Pollution");
    	for (int i = 0 ; i < array1.size(); i++){
    		//get date
    		int year = array1.get(i).getAsJsonObject().get("date").getAsInt();
    		//get data
    		double value1 = array1.get(i).getAsJsonObject().get("value").getAsDouble();
    		double value2 = array2.get(i).getAsJsonObject().get("value").getAsDouble();
    		double value3 = array3.get(i).getAsJsonObject().get("value").getAsDouble();
    		//add value to series
    		series1.add(year,value1);
    		series2.add(year,value2);
    		series3.add(year,value3);
    	}
    	//add series to data set
    	mDataset1.addSeries(series1);
    	mDataset2.addSeries(series2);
    	mDataset3.addSeries(series3);
    	//create Scatter chart
		XYPlot plot = new XYPlot();
		XYItemRenderer itemrenderer1 = new XYLineAndShapeRenderer(false, true);
		XYItemRenderer itemrenderer2 = new XYLineAndShapeRenderer(false, true);
		XYItemRenderer itemrenderer3 = new XYLineAndShapeRenderer(false, true);
		
		plot.setDataset(0, mDataset1);
		plot.setRenderer(0, itemrenderer1);
		DateAxis domainAxis = new DateAxis("Year");
		plot.setDomainAxis(domainAxis);
		plot.setRangeAxis(new NumberAxis(" metric tons per capita"));

		plot.setDataset(1, mDataset2);
		plot.setRenderer(1, itemrenderer2);
		plot.setRangeAxis(1, new NumberAxis("kg of oil equivalent per capita"));

		plot.setDataset(2, mDataset3);
		plot.setRenderer(2, itemrenderer3);
		plot.setRangeAxis(2, new NumberAxis("micrograms per cubic meter"));

		plot.mapDatasetToRangeAxis(0, 0);// 1st dataset to 1st y-axis
		plot.mapDatasetToRangeAxis(1, 1); // 2nd dataset to 2nd y-axis
		plot.mapDatasetToRangeAxis(2, 2);

		JFreeChart scatterChart = new JFreeChart("CO2 emissions vs Energy use vs PM2.5 air pollution",
				new Font("Serif", java.awt.Font.BOLD, 18), plot, true);

		ChartPanel chartPanel = new ChartPanel(scatterChart);
		chartPanel.setPreferredSize(new Dimension(400, 300));
		chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		chartPanel.setBackground(Color.white);
		
		result.put("Scatter",new ChartPanel(scatterChart));
    }
	

}
