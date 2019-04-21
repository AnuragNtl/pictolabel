package com.anuragntl.pictolabellib;

import java.awt.Image;
import java.awt.image.BufferedImage;

public class ImageCropper {
	private BufferedImage image;
public ImageCropper(BufferedImage image)
{
	this.image=image;
}
public BufferedImage crop(int x1,int y1,int width,int height)
{
	return image.getSubimage(x1, y1, width,height);
}
public static BufferedImage crop(BufferedImage image,int x1,int y1,int width, int height)
{
	return new ImageCropper(image).crop(x1, y1, width, height);
}
}
