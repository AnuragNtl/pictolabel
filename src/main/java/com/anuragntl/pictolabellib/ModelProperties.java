package com.anuragntl.pictolabellib;

import java.io.IOException;

import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.layers.objdetect.Yolo2OutputLayer;
import org.deeplearning4j.util.ModelSerializer;
import org.deeplearning4j.zoo.model.TinyYOLO;

public class ModelProperties {
private String modelPath,modelClasses[];
public static final int INPUT_WIDTH=416,INPUT_HEIGHT=416,INPUT_CHANNELS=3,
GRID_WIDTH=13,GRID_HEIGHT=13;
public static final String MODEL_CLASSES[]={
		"aeroplane", "bicycle", "bird", "boat", "bottle",
		"bus", "car", "cat", "chair", "cow", "diningtable", "dog",
		"horse", "motorbike", "person", "pottedplant", "sheep", "sofa",
		"train", "tvmonitor"
		};
private int inputWidth,inputHeight,inputChannels,gridWidth,gridHeight;
	public ModelProperties()
{
	modelPath=null;
	modelClasses=MODEL_CLASSES;
	inputWidth=INPUT_WIDTH;
	inputHeight=INPUT_HEIGHT;
	inputChannels=INPUT_CHANNELS;
	gridWidth=GRID_WIDTH;
	gridHeight=GRID_HEIGHT;
}
	public String getModelPath() {
		return modelPath;
	}
	public void setModelPath(String modelPath) {
		this.modelPath = modelPath;
	}
	public String[] getModelClasses() {
		return modelClasses;
	}
	public void setModelClasses(String[] modelClasses) {
		this.modelClasses = modelClasses;
	}
	public int getInputWidth() {
		return inputWidth;
	}
	public void setInputWidth(int inputWidth) {
		this.inputWidth = inputWidth;
	}
	public int getInputHeight() {
		return inputHeight;
	}
	public void setInputHeight(int inputHeight) {
		this.inputHeight = inputHeight;
	}
	public int getInputChannels() {
		return inputChannels;
	}
	public void setInputChannels(int inputChannels) {
		this.inputChannels = inputChannels;
	}
	public int getGridWidth() {
		return gridWidth;
	}
	public void setGridWidth(int gridWidth) {
		this.gridWidth = gridWidth;
	}
	public int getGridHeight() {
		return gridHeight;
	}
	public void setGridHeight(int gridHeight) {
		this.gridHeight = gridHeight;
	}
	private static ComputationGraph model;
	public ComputationGraph getModel() throws IOException
	{
		if(model==null)
			return getAndChangeModel();
		else
			return model;
	}
	private ComputationGraph getAndChangeModel()throws IOException
	{
		if(modelPath==null)
		model=(ComputationGraph)TinyYOLO.builder().build().initPretrained();
		else
		{
			model=ModelSerializer.restoreComputationGraph(modelPath);
			if(! (model.getOutputLayer(0) instanceof Yolo2OutputLayer))
				throw new IOException("Not a YOLO Model");
		}
		return model;
	}
	public void init()throws IOException
	{
		getAndChangeModel();
	}
}
