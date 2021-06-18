package com.edu.analysis;

import com.google.gson.JsonArray;
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
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;

import javax.swing.BorderFactory;
//. Current health expenditure per capita (current US$) VS Mortality rate, infant (per 1,000 live births)
public class HeathCapitaAndMortalityAnaysis extends AbstractAnlyzer{


    @Override
    /**
     * do analysis
     */
    public void analysis() {
    	//initial data
        data = "Current health expenditure per capita (current US$) vs Mortality rate, infant (per 1,000 live births):";
        //get data from network
        JsonArray heathArray= getResponse(HEATH_TYPE_STR);
        JsonArray mortalityArray=getResponse(MORTALITY_TYPE_STR_2);
        //create line chart
        createLineChart(heathArray,mortalityArray);
        createBarChart(heathArray,mortalityArray);
        createScattChart(heathArray,mortalityArray);
        createMixChart(heathArray,mortalityArray);

    }


    /**
     * create line chart
     * @param heathArray health data
     * @param mortalityArray mortality data
     */
    private void createLineChart(JsonArray heathArray,JsonArray mortalityArray){
    	//create data set to health
        XYSeriesCollection mDataset1 =  new XYSeriesCollection();
        //create data set to mortality
        XYSeriesCollection mDataset2 =  new XYSeriesCollection();
        
        //get data and for each json array
        JsonArray array = heathArray.get(1).getAsJsonArray();
        JsonArray array1 = mortalityArray.get(1).getAsJsonArray();
        XYSeries series1 = new XYSeries("heath per");
        XYSeries series2 = new XYSeries("Mortality rate");
        for (int i = 0 ; i < array.size(); i++){
        	//get date
            int year = array.get(i).getAsJsonObject().get("date").getAsInt();
            //get health data
            double value = array.get(i).getAsJsonObject().get("value").getAsDouble();
            //get mortality data
            double value1 = array1.get(i).getAsJsonObject().get("value").getAsDouble();
            //add health value to series1
            series1.add(year,value);
            //add mortality value to series2
            series2.add(year,value1);
            //append data info
            data+="  int year "+ year+":";
            data+="  heath per capita is "+value +", Mortality rate is "+value1+"\n";
        }
        //add health series to dataset1
        mDataset1.addSeries(series1);
        //add mortality series to dataset2
        mDataset2.addSeries(series2);
        //create line chart
        JFreeChart chart = ChartFactory.createXYLineChart("heath per capita vs mortality rate ",
                "year",
                "health per capita ",
                mDataset1,
                PlotOrientation.VERTICAL,
                true,
                true,
                false);
        //get plot
        XYPlot plot = chart.getXYPlot();
        //create a x axis
        NumberAxis axis2 = new NumberAxis("Mortality rate");
        //modify second axis show style
        axis2.setAxisLinePaint(Color.BLUE);
        //set second label style
        axis2.setLabelPaint(Color.BLUE);
        axis2.setTickLabelPaint(Color.BLUE);
        
        //set axis range
        plot.setRangeAxis(1, axis2);
        //set second axis data set
        plot.setDataset(1, mDataset2);
        plot.mapDatasetToRangeAxis(1, 1);
        
        //create second y axis
        XYLineAndShapeRenderer render2 =  new XYLineAndShapeRenderer();
        //set color
        render2.setSeriesPaint(0, Color.BLUE);
        //add render to plot
        plot.setRenderer(1, render2);
        //add chart panel to result set
        result.put("Line chart",new ChartPanel(chart));
    }
    /**
     * create line chart
     * @param heathArray health data
     * @param mortalityArray mortality data
     */
    private void createScattChart(JsonArray heathArray,JsonArray mortalityArray){
    	//create data set to health
    	XYSeriesCollection mDataset1 =  new XYSeriesCollection();
    	//create data set to mortality
    	XYSeriesCollection mDataset2 =  new XYSeriesCollection();
    	
    	//get data and for each json array
    	JsonArray array = heathArray.get(1).getAsJsonArray();
    	JsonArray array1 = mortalityArray.get(1).getAsJsonArray();
    	//set series name
        XYSeries series1 = new XYSeries("health per capita");
        XYSeries series2 = new XYSeries("Mortality rate");
        
    	for (int i = 0 ; i < array.size(); i++){
    		//get date
    		int year = array.get(i).getAsJsonObject().get("date").getAsInt();
    		//get health data
    		double value = array.get(i).getAsJsonObject().get("value").getAsDouble();
    		//get mortality data
    		double value1 = array1.get(i).getAsJsonObject().get("value").getAsDouble();
    		//add health value to series1
    		series1.add(year,value);
    		//add mortality value to series2
    		series2.add(year,value1);
    	}
    	//add health series to data set1
    	mDataset1.addSeries(series1);
    	//add mortality series to data set2
    	mDataset2.addSeries(series2);
    	//create Scatter chart
    	//get plot
		XYPlot plot = new XYPlot();
		//create y axis
		XYItemRenderer itemrenderer1 = new XYLineAndShapeRenderer(false, true);
		XYItemRenderer itemrenderer2 = new XYLineAndShapeRenderer(false, true);
		//set axis data set
		plot.setDataset(0, mDataset1);
		//add render to plot
		plot.setRenderer(0, itemrenderer1);
		DateAxis domainAxis = new DateAxis("Year");
		plot.setDomainAxis(domainAxis);
		//add axis name
		plot.setRangeAxis(new NumberAxis("health per capita"));
		//set axis data set
		plot.setDataset(1, mDataset2);
		//add render to plot
		plot.setRenderer(1, itemrenderer2);
		//add axis name
		plot.setRangeAxis(1, new NumberAxis("Mortality rate"));
		//set 1st data set to 1st y-axis
		plot.mapDatasetToRangeAxis(0, 0);
		//set 2nd data set to 2nd y-axis
		plot.mapDatasetToRangeAxis(1, 1);
		//create scatter chart
		JFreeChart chart = new JFreeChart("Health per capita vs Mortality rate",
				new Font("Times New Roman", java.awt.Font.BOLD, 24), plot, true);

		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(400, 300));
		chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		chart.getLegend().setFrame(new BlockBorder(Color.WHITE));
		chartPanel.setBackground(Color.white);
		//add chart panel to result set
		result.put("Scatter chart",new ChartPanel(chart));
    }
    
    /**
     * create mixed chart
     * @param heathArray  heath data
     * @param mortalityArray mortality data
     */
    private void createMixChart(JsonArray heathArray,JsonArray mortalityArray) {
    	//create data set to heath per capita
    	DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
    	//create data set to Mortality rate
    	DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
        //for each json array
        JsonArray array = heathArray.get(1).getAsJsonArray();
        JsonArray array2 = mortalityArray.get(1).getAsJsonArray();
        for (int i = array.size()-1 ; i >= 0; i--){
        	//date
            int year = array.get(i).getAsJsonObject().get("date").getAsInt();
            int year1 = array2.get(i).getAsJsonObject().get("date").getAsInt();
            //value
            double value = array.get(i).getAsJsonObject().get("value").getAsDouble();
            double value1 = array2.get(i).getAsJsonObject().get("value").getAsDouble();
            //add value to data set
            dataset1.setValue(value,"health per capita",year+"");
            dataset2.setValue(value1,"Mortality rate",year1+"");
        }
        //create mixed chart 
        JFreeChart chart = ChartFactory.createBarChart("Current health expenditure per capita (current US$) vs Mortality rate", "", "current US$", dataset1);
        //get plot
        CategoryPlot categoryplot = (CategoryPlot) chart.getPlot();
        //set mortality data set
        categoryplot.setDataset(1, dataset2);
        //set mortality axis
		categoryplot.mapDatasetToRangeAxis(1, 1);
		// x 
		CategoryAxis categoryaxis = categoryplot.getDomainAxis();
		categoryaxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		// right for y axis
		NumberAxis numberaxis = new NumberAxis("Mortality rate(%)");
		categoryplot.setRangeAxis(1, numberaxis);
		// hide y axis
		numberaxis.setAxisLineVisible(false);
		numberaxis.setTickMarksVisible(false);

		// set line chat style
		LineAndShapeRenderer lineRenderer = new LineAndShapeRenderer();
		lineRenderer.setSeriesPaint(0, new Color(255, 185, 1));
		categoryplot.setRenderer(1, lineRenderer);
		//set z-index for set line chart in top
		categoryplot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
		chart.getLegend().setFrame(new BlockBorder(Color.WHITE));
		
		result.put("Mixed chart",new ChartPanel(chart));
    	
    }
    
    /**
     * create bar chart
     * @param heathArray  heath data
     * @param mortalityArray mortality data
     */
    private void createBarChart(JsonArray heathArray,JsonArray mortalityArray) {
    	//create data set to heath per capita
    	DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
    	//create data set to Mortality rate
    	DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
        //for each json array
        JsonArray array = heathArray.get(1).getAsJsonArray();
        JsonArray array2 = mortalityArray.get(1).getAsJsonArray();
        for (int i = array.size()-1 ; i >= 0; i--){
        	//date
            int year = array.get(i).getAsJsonObject().get("date").getAsInt();
            //value
            double value = array.get(i).getAsJsonObject().get("value").getAsDouble();
            double value1 = array2.get(i).getAsJsonObject().get("value").getAsDouble();
            //add value to data set
            dataset1.setValue(value,"health per capita",year+"");
            dataset2.setValue(value1,"Mortality rate",year+"");
        }
        
        //create bar chart
        //get plot
		CategoryPlot plot = new CategoryPlot();
		BarRenderer barrenderer1 = new BarRenderer();
		BarRenderer barrenderer2 = new BarRenderer();
		//set beds data set
		plot.setDataset(0, dataset1);
		plot.setRenderer(0, barrenderer1);
		//set x axis
		CategoryAxis domainAxis = new CategoryAxis("Year");
		plot.setDomainAxis(domainAxis);
		// left for y axis
		plot.setRangeAxis(new NumberAxis("current US$"));
		//set mortality data set
		plot.setDataset(1, dataset2);
		plot.setRenderer(1, barrenderer2);
		// right for y axis
		plot.setRangeAxis(1, new NumberAxis("Mortality rate(%)"));

		plot.setRenderer(barrenderer1);
		barrenderer1.setMaximumBarWidth(0.2);
		//set 1st data set to 1st y-axis
		plot.mapDatasetToRangeAxis(0, 0);
		//set 2nd data set to 2nd y-axis
		plot.mapDatasetToRangeAxis(1, 1);
		//create bar chart
		JFreeChart chart = new JFreeChart("Current health expenditure per capita (current US$) vs Mortality rate",
				new Font("Times New Roman", java.awt.Font.BOLD, 24), plot, true);
		
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(400, 300));
		chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		chartPanel.setBackground(Color.white);
		//add chart panel to result set
		result.put("Bar chart",new ChartPanel(chart));
    }
    

}
