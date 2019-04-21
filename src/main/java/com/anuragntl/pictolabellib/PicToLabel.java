package com.anuragntl.pictolabellib;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.transform.ColorConversionTransform;
import org.deeplearning4j.nn.layers.objdetect.DetectedObject;
import org.deeplearning4j.nn.layers.objdetect.Yolo2OutputLayer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;

import static org.bytedeco.javacpp.opencv_imgproc.COLOR_BGR2RGB;

public class PicToLabel {
private ModelProperties modelProperties;
private File image;
	public PicToLabel(ModelProperties modelProperties,File image)
{
	this.modelProperties=modelProperties;
	this.image=image;
}
	private INDArray processImage()throws IOException
	{
		NativeImageLoader loader=new NativeImageLoader(modelProperties.getInputWidth(),
				modelProperties.getInputHeight(),modelProperties.getInputChannels(),
				new ColorConversionTransform(COLOR_BGR2RGB));
		INDArray asMatrix=loader.asMatrix(image);
		ImagePreProcessingScaler scaler=new ImagePreProcessingScaler(0,1);
		scaler.transform(asMatrix);
		return asMatrix;
	}
	private INDArray getOutput(INDArray input) throws IOException
	{
		INDArray output=modelProperties.getModel().outputSingle(input);
		return output;
	}
	public List<DetectedObject> getDetectedObjects(double threshold)throws IOException
	{
		INDArray input=processImage();
		INDArray output=getOutput(input);
		Yolo2OutputLayer outputLayer=(Yolo2OutputLayer)modelProperties.getModel().getOutputLayer(0);
		List<DetectedObject> detectedObjects=outputLayer.getPredictedObjects(output,threshold);
		return detectedObjects;
	}
	public Map<String,Rectangle> getCroppableAreas(List<DetectedObject> detectedObjects)
	{
		Map<String,Rectangle> croppableAreas=new HashMap<String,Rectangle>();
		String modelClasses[]=modelProperties.getModelClasses();
		int inputWidth=modelProperties.getInputWidth(),
				inputHeight=modelProperties.getInputHeight(),
				gridWidth=modelProperties.getGridWidth(),
				gridHeight=modelProperties.getGridHeight();
		for(DetectedObject detectedObject : detectedObjects)
		{
			double topLeft[]=detectedObject.getTopLeftXY();
			double rightBottom[]=detectedObject.getBottomRightXY();
			int x1=(int)Math.round(inputWidth*topLeft[0]/gridWidth);
			int y1=(int)Math.round(inputHeight*topLeft[1]/gridHeight);
			int x2=(int)Math.round(inputWidth*rightBottom[0]/gridWidth);
			int y2=(int)Math.round(inputHeight*rightBottom[1]/gridHeight);
			int width=x2-x1,height=y2-y1;
			if(x1<0)
				x1=0;
			if(y1<0)
				y1=0;
			if(width>=modelProperties.getGridWidth())
				width=modelProperties.getGridWidth()-1;
			if(height>=modelProperties.getGridHeight())
				height=modelProperties.getGridHeight()-1;
			croppableAreas.put(modelClasses[detectedObject.getPredictedClass()],new Rectangle(x1,y1,width,height));
		}
		return croppableAreas;
	}
}
