package io.github.vyhuang.paletteprinter;

import java.io.*;
import javax.imageio.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import java.awt.Rectangle;
/**
 * Outputs an image grid containing colors of a palette.
 *  Can take input in the two following forms:
 *    java -jar paletteprinter [input.gpl] ?[HSB/CIELCH]
 *      where:
 *        input.gpl is a valid GIMP palette file
 *        The second (optional) parameter can take on two values:
 *          - if "HSB", a color's row is determined by its hue, and its column by its brightness
 *          - if "CIELCH", a color's row is determined by its hue, and it's column by its lightness
 *        If this is absent, create an grid with the number of columns specified in the gpl file
 *    java -jar paletteprinter [R] [G] [B] ?[-f] ?[input.gpl]
 *      where:
 *        R,G,B are the number of bits available for the red, green, and blue channels respectively
 *        The second parameter is optional:
 *          - if "-f", then create a GIMP palette file with the specified filename where there are
 *          R distinct columns, where R is the unique values of the red channel.
 *        If this is absent, create an image grid where R increases alongside the y-axis, G
 *        increases alongside the x-axis, and B increases alongside the z-axis.
 **/
public class PalettePrinter {
  public static void main(String[] args) {
    File inputfile, outputfile;
    BufferedImage image;
    int cellWidth, cellHeight;

    BitPalette bitPal = new BitPalette(new int[]{1,1,1});
    System.out.printf("new palette created with rgb%d%d%d values \n",
        bitPal.redBits(), bitPal.blueBits(), bitPal.greenBits());

    /* Test code begin */

    /*
    Graphics2D g2d = image.createGraphics();
    g2d.setColor(Color.red);
    g2d.fill(new Rectangle.Float(10,10,25,25));
    g2d.dispose();
    */

    Color temp;

    System.out.printf("rgb levels in created palette: %d:%d:%d\n",
        bitPal.redLevels(),bitPal.greenLevels(),bitPal.blueLevels());

    for (int i = 0; i < 2; i += 1) {
      for (int j = 0; j < 4; j += 1) {
        System.out.println(i + ":" + j);
        temp = bitPal.getColor(i,j);
        System.out.println(temp.getRed() + "," + temp.getGreen() + "," + temp.getBlue());
      }
    }

    System.out.println("Creating erroneous palette...");
    BitPalette errPal = new BitPalette(new int[]{-1,0,10});


    /* end Test code */

    /*
    try {
      outputfile = new File("image_grid.png");
      ImageIO.write(image, "png", outputfile);
    } catch (IOException e) {
      System.out.println("Image failed to be saved.");
    }
    */

    return;
  }

  private void drawGrid() {
    /*
    for (int i = 0; i < cellDim.length; i+= 1) {
      if (cellDim[i] <= 0) {
        System.err.printf("%d is an invalid dimensional value. Changing to 1.",cellDim[i]);
        cellDim[i] = 1;
      }
    }
    cellWidth   = cellDim[0];
    cellHeight  = cellDim[1];
     */
  }


}
