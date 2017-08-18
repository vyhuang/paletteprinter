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

    BitPalette bitPal = new BitPalette(new int[]{50,50}, new char[]{1,1,1});
    //System.out.println(bitPal.internalPalette.

    /* Test code begin */

    /*
    Graphics2D g2d = image.createGraphics();
    g2d.setColor(Color.red);
    g2d.fill(new Rectangle.Float(10,10,25,25));
    g2d.dispose();
    */

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

}
