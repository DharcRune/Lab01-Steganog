package edu.neumont.algorithms;

import java.awt.Color;
import java.io.IOException;

import edu.neumont.ui.Picture;

public class Steganog {

	// Takes a clean image and changes the prime-indexed pixels to secretly carry the message
	public Picture embedIntoImage(Picture cleanImage, String message) throws IOException
	{	
		int i = 0;
		int bitMask = 0x03;
		int xValue = 0;
		int yValue = 0;

		PrimeIterator pi = new PrimeIterator(cleanImage.height() * cleanImage.width());

		while(i < message.length())
		{
			if(xValue >= cleanImage.width())
			{
				xValue = 0;
				yValue++;
			}

			yValue = pi.next()/cleanImage.width();
			xValue = pi.next() - cleanImage.width() * yValue;
			Color tempColor = cleanImage.get(xValue, yValue);


			int ascValue = message.charAt(i) - 32;

			int valToSubtract = (tempColor.getRed() & bitMask);
			int newRed = tempColor.getRed() - valToSubtract;
			int redToAdd = ascValue/16;
			ascValue -= 16 * redToAdd;

			valToSubtract = (tempColor.getGreen() & bitMask);
			int newGreen = tempColor.getGreen() - valToSubtract;
			int greenToAdd = ascValue/4;
			ascValue -= 4 * greenToAdd;

			valToSubtract = (tempColor.getBlue() & bitMask);
			int newBlue = tempColor.getBlue() - valToSubtract;
			int blueToAdd = ascValue;

			tempColor = new Color(newRed + redToAdd, newGreen + greenToAdd, newBlue + blueToAdd);
			cleanImage.set(xValue, yValue, tempColor);

			xValue++;
			i++;
		}

		return cleanImage;
	}

	// Retrieves the embedded secret from the secret-carrying image
	public String retreiveFromImage(Picture imageWithSecretMessage) throws IOException
	{
		String returnString = "";
		imageWithSecretMessage.setOriginUpperLeft();
		PrimeIterator pi = new PrimeIterator(imageWithSecretMessage.height() * imageWithSecretMessage.width());
		int tests = pi.next();

		for(int y = 0; y < imageWithSecretMessage.height(); y++)
		{
			for(int x = 0; x < imageWithSecretMessage.width(); x++)
			{
				y = tests/imageWithSecretMessage.width();
				x = tests - imageWithSecretMessage.width() * y;

				Color tempColor = imageWithSecretMessage.get(x, y);
				String red = Integer.toBinaryString(tempColor.getRed());
				String green = Integer.toBinaryString(tempColor.getGreen());
				String blue = Integer.toBinaryString(tempColor.getBlue());
				String lastRed = "";
				String lastGreen = "";
				String lastBlue = "";

				if(red.length() <= 1)
				{ lastRed = red; }
				else
				{ lastRed = red.substring(red.length() - 2, red.length()); }

				if(green.length() <= 1)
				{ lastGreen = green; }
				else
				{ lastGreen = green.substring(green.length() - 2, green.length()); }

				if(blue.length() <= 1)
				{ lastBlue = blue; }
				else
				{ lastBlue = blue.substring(blue.length() - 2, blue.length()); }

				if(lastRed.length() == 1)
				{ lastRed = "0" + lastRed; }

				if(lastGreen.length() == 1)
				{ lastGreen = "0" + lastGreen; }

				if(lastBlue.length() == 1)
				{ lastBlue = "0" + lastBlue; }

				String tempString = lastRed + lastGreen + lastBlue;
				int setAsciiVal = Integer.parseInt(tempString, 2) + 32;

				if((char)setAsciiVal == '[')
				{
					y = imageWithSecretMessage.height();
					x = imageWithSecretMessage.width();
				}
				else
				{
					returnString += (char)setAsciiVal;
				}
			}
		}


		return returnString;
	}
}