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

  protected enum PrinterMode {
    // Indicates that output filename will be default.png
    RGB_PNG_DEFAULT,
    GPL_HSB_DEFAULT,
    GPL_CIE_DEFAULT,
    // Indicates that custom filenames have been provided
    RGB_PNG_CUSTOM,
    GPL_HSB_CUSTOM,
    GPL_CIE_CUSTOM,
    // Indicates that .gpl file will be created instead of a .png file
    RGB_GPL_DEFAULT,
    RGB_GPL_CUSTOM,
    // Initialized value. 
    NONE

  }

  public static void main(String[] args) {
    Palette palette = null;
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

    PrinterMode mode = PrinterMode.NONE;

    switch (args.length) {
      //test code in main runs here
      //case 0:   break;

      // Resulting grid has input.gpl-specified number of columns
      case 1:   inputFileName = args[0];
                break;

      // HSB/CIELCH ordering
      case 2:   if (args[1].equals("HSB")) {
                  mode = PrinterMode.GPL_HSB_DEFAULT;
                } else if (args[1].equals( "CIELCH")) {
                  mode = PrinterMode.GPL_CIE_DEFAULT;
                } else {
                    break;
                }
                inputFileName = args[0];
                // call paletteOrder()
                break;

      // default bit palette print
      case 3:   mode = PrinterMode.RGB_PNG_DEFAULT;
                palette = new BitPalette(colors);
                break;

      // specified bit palette creation/print
      case 5:   if (args[3].equals("-gpl")) {
                  mode = PrinterMode.RGB_GPL_CUSTOM;
                } else if (args[3].equals("-png")) {
                  mode = PrinterMode.RGB_PNG_CUSTOM;
                } else {
                    break;
                }
                System.out.printf("%s %s %s %s %s\n",args[0],args[1],args[2],args[3],args[4]);
                palette = new BitPalette(colors);
               break;
    }

    if (palette == null) {
      mode = PrinterMode.NONE;
    }
    else {
      palette.initializePalette();
    }

    switch (mode) {
      case RGB_PNG_DEFAULT:
                palPrinter.drawGrid(palette);
                palPrinter.writePng();
                break;
      case RGB_PNG_CUSTOM:
                palPrinter.drawGrid(palette);
                palPrinter.writePng(args[4]);
                break;
      case GPL_HSB_DEFAULT:
                break;
      case GPL_HSB_CUSTOM:
                break;
      case GPL_CIE_DEFAULT:
                break;
      case GPL_CIE_CUSTOM:
                break;
      case RGB_GPL_DEFAULT:
                break;
      case RGB_GPL_CUSTOM:
                break;
      default:  
                badArgsMessage();
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

  private void drawGrid(Palette palette) {

    if (palette.paletteColumns() > 76) {
      cellWidth = 3840 / palette.paletteColumns();
      cellWidth = (cellWidth > 0) ? cellWidth : 1;
    }
    if (palette.paletteRows() > 43) {
      cellHeight = 2160 / palette.paletteRows();
    }

    imageWidth = cellWidth * palette.paletteColumns();
    imageHeight = cellHeight * palette.paletteRows();

    image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);

    Graphics2D g2d = image.createGraphics();
    Rectangle.Float cell = new Rectangle.Float(0,0,cellWidth,cellHeight);
    Color currentColor;
    for (int i = 0; i < palette.paletteRows(); i += 1) {
      for (int j = 0; j < palette.paletteColumns(); j += 1) {
        currentColor = palette.getColor(i,j);
        g2d.setColor(currentColor);
        cell.setRect(j*cell.getWidth(), i*cell.getHeight(), cell.getWidth(), cell.getHeight());
        //System.out.printf("x,y = %d,%d\n",(int)cell.getX(),(int)cell.getY());
        g2d.fill(cell);
      }
    }
  }

  public void writePng() {
    writePng("default.png");
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

  private static void badArgsMessage() {
      System.out.println(
          "java -jar paletteprinter.jar [input.gpl] ?[HSB/CIELCH] \n \t OR \n" +
          "java -jar paletteprinter.jar [R] [G] [B] (=png/-gpl) [=output.<filetype>])"
          );
  }


}
