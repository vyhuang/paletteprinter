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
 *    java -jar paletteprinter [R] [G] [B] ?[-gpl/-png] ?[input.gpl/output.png]
 *      where:
 *        R,G,B are the number of bits available for the red, green, and blue channels respectively
 *        The second parameter is optional. If this is absent, create the file "output.png"
 *        containing an image grid where R increases alongside the y-axis, G increases alongside 
 *        the x-axis, and B increases alongside the z-axis.
 *          - if "-f", then create a GIMP palette file with the specified filename where there are
 *          R distinct columns, where R is the unique values of the red channel.
 *          - if "-i", then create the PNG file with the specified filename.
 **/
public class PalettePrinter {
  BufferedImage image;
  int imageWidth, imageHeight;
  int cellWidth, cellHeight;

  Color[][] orderedPalette;

  public static void main(String[] args) {

    // Indicates that output filename will be default.png
    final int MODE_RGB_PNG_DEFAULT  = 0;
    final int MODE_GPL_HSB_DEFAULT  = 1;
    final int MODE_GPL_CIE_DEFAULT  = 2;
    // Indicates that custom filenames have been provided
    final int MODE_RGB_PNG_CUSTOM   = 3;
    final int MODE_GPL_HSB_CUSTOM   = 4;
    final int MODE_GPL_CIE_CUSTOM   = 5;
    // Indicates that .gpl file will be created instead of a .png file
    final int MODE_RGB_GPL_DEFAULT  = 6;
    final int MODE_RGB_GPL_CUSTOM   = 7;

    BitPalette bitPal = new BitPalette();
    PalettePrinter palPrinter = new PalettePrinter();
    String inputFileName;

    int[] colors = new int[0];
    int[] cellDimensions;
 
    if (args.length == 3 || args.length == 5) {
      colors = new int[]{
        Integer.parseInt(args[0]), 
        Integer.parseInt(args[1]), 
        Integer.parseInt(args[2])
      };
    }

    int mode = -1;

    switch (args.length) {
      //test code in main runs here
      //case 0:   break;

      // Resulting grid has input.gpl-specified number of columns
      case 1:   inputFileName = args[0];
                break;

      // HSB/CIELCH ordering
      case 2:   if (args[1].equals("HSB")) {
                  mode = MODE_GPL_HSB_DEFAULT;
                } else if (args[1].equals( "CIELCH")) {
                  mode = MODE_GPL_CIE_DEFAULT;
                } else {
                  badArgsMessage();
                  return;
                }
                inputFileName = args[0];
                // call paletteOrder()
                break;

      // default bit palette print
      case 3:   mode = MODE_RGB_PNG_DEFAULT;
                bitPal = new BitPalette(colors);
                break;

      // specified bit palette creation/print
      case 5:   if (args[3].equals("-gpl")) {
                  mode = MODE_RGB_GPL_CUSTOM;
                } else if (args[3].equals("-png")) {
                  mode = MODE_RGB_PNG_CUSTOM;
                } else {
                  badArgsMessage();
                  return;
                }
 
                System.out.printf("%s %s %s %s %s\n",args[0],args[1],args[2],args[3],args[4]);
                bitPal = new BitPalette(colors);
               break;

      default:  badArgsMessage();
                return;
    }

    switch (mode) {
      case MODE_RGB_PNG_DEFAULT:
                palPrinter.drawGrid(bitPal);
                palPrinter.writePng();
                break;
      case MODE_RGB_PNG_CUSTOM:
                palPrinter.drawGrid(bitPal);
                palPrinter.writePng(args[4]);
                break;
      case MODE_GPL_HSB_DEFAULT:
                break;
      case MODE_GPL_HSB_CUSTOM:
                break;
      case MODE_GPL_CIE_DEFAULT:
                break;
      case MODE_GPL_CIE_CUSTOM:
                break;
      case MODE_RGB_GPL_DEFAULT:
                break;
      case MODE_RGB_GPL_CUSTOM:
                break;
      default:  
                return;
    }
    return;
  }

  public PalettePrinter() {
    cellWidth = cellHeight = 50;
  }
  public PalettePrinter(int[] cellDim) {
    for (int i = 0; i < cellDim.length; i+= 1) {
      if (cellDim[i] <= 0) {
        System.err.printf("%d is an invalid dimensional value. Changing to 1.",cellDim[i]);
        cellDim[i] = 1;
      }
    }
    cellWidth   = cellDim[0];
    cellHeight  = cellDim[1];
  }

  private void drawGrid(BitPalette bitPalette) {
    imageWidth = cellWidth * bitPalette.paletteCols();
    imageHeight = cellHeight * bitPalette.paletteRows();

    image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);

    Graphics2D g2d = image.createGraphics();
    Rectangle.Float cell = new Rectangle.Float(0,0,cellWidth,cellHeight);
    Color currentColor;
    for (int i = 0; i < bitPalette.paletteRows(); i += 1) {
      for (int j = 0; j < bitPalette.paletteCols(); j += 1) {
        currentColor = bitPalette.getColor(i,j);
        g2d.setColor(currentColor);
        cell.setRect(j*cell.getWidth(), i*cell.getHeight(), cell.getWidth(), cell.getHeight());
        //System.out.printf("x,y = %d,%d\n",(int)cell.getX(),(int)cell.getY());
        g2d.fill(cell);
      }
    }
  }
  private void drawGrid() {
  }

  public void writePng(String outputFilename) {
    File outputFile;
    try {
      outputFile = new File(outputFilename);
      ImageIO.write(image, "png", outputFile);
      System.out.printf("Saved to %s.\n", outputFilename);
    } catch (IOException e) {
      System.out.println("Image failed to be saved.");
    }
  }
  public void writePng() {
    try {
      File outputFile = new File("default.png");
      ImageIO.write(image, "png", outputFile);
      System.out.println("Saved to default.png");
    } catch (IOException e) {
      System.out.println("Image failed to be saved.");
    }
  }
 
  private static void badArgsMessage() {
      System.out.println(
          "java -jar paletteprinter.jar [input.gpl] ?[HSB/CIELCH] \n \t OR \n" +
          "java -jar paletteprinter.jar [R] [G] [B] ?([-gpl/-png] [output.gpl/output.png])"
          );
  }


}
