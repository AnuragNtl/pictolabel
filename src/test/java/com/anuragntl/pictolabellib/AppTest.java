package com.anuragntl.pictolabellib;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    private static BufferedImage resize(BufferedImage img, int height, int width) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }

    public void testApp()throws Exception
    {
    	BufferedImage image=ImageIO.read(new File("I:\\temp\\F1.jpg"));
    	ModelProperties props=new ModelProperties();
    	image=resize(image,props.getInputWidth(),props.getInputHeight());
    	//image=ImageCropper.crop(image, 0, 0, props.getInputWidth(), props.getInputHeight());
    	//ImageIO.write(image,"JPG",new File("I:\\temp\\F1.jpg"));
    	PicToLabel picToLabel=new PicToLabel(props,
    			new File("I:\\temp\\F1.jpg"));
    	Map<String,Rectangle> areas=picToLabel.getCroppableAreas(picToLabel.getDetectedObjects(0.1F));
    	for(Iterator<String> iterator=areas.keySet().iterator();iterator.hasNext();)
    	{
    		String label=iterator.next();
    		Rectangle rect=areas.get(label);
    		System.out.println(label+" "+rect);
    		if(rect.x<0)
    			rect.x=0;
    		if(rect.y<0)
    			rect.y=0;
    		if(rect.x+rect.width>=416)
    			rect.width=416-rect.x-1;
    		if(rect.y+rect.height>=416)
    			rect.height=416-rect.y-1;
    		ImageIO.write(ImageCropper.crop(image,rect.x,rect.y,rect.width,rect.height),
    				"JPG",new File("I:\\temp\\"+label+".jpg"));
    	}
    	System.out.println(areas);
        assertTrue( true );
    }
}
