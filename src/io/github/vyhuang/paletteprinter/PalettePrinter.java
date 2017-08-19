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
 *    java -jar paletteprinter [R] [G] [B] ?[-f/-i] ?[input.gpl/output.png]
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
  public PalettePrinter(int[] cellDim, BitPalette bitPal) {
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
    } catch (IOException e) {
      System.out.println("Image failed to be saved.");
    }
  }
  public void writePng() {
    try {
      File outputFile = new File("default.png");
      ImageIO.write(image, "png", outputFile);
    } catch (IOException e) {
      System.out.println("Image failed to be saved.");
    }
  }
 
  private static void badArgsMessage() {
      System.out.println(
          "java -jar paletteprinter.jar [input.gpl] ?[HSB/CIELCH] \n \t OR \n" +
          "java -jar paletteprinter.jar [R] [G] [B] ?([-f/-i] [output.gpl/output.png])"
          );
  }


  public static void main(String[] args) {
 
    boolean createPng = true;
    boolean defaultOutput = true;
    int[] cellDimensions;
    
    String inputFileName;

    BitPalette bitPal;
    PalettePrinter palPrinter;
    int[] colors;

    //remove later
    cellDimensions = new int[]{50,50};

    switch (args.length) {
      // test code runs here
      case 0:   break;

      // Resulting grid has input.gpl-specified number of columns
      case 1:   inputFileName = args[0];
                break;

      // HSB/CIELCH ordering
      case 2:   if (args[1] != "HSB" || args[1] != "CIELCH") {
                  badArgsMessage();
                  return;
                }
                inputFileName = args[0];
                palPrinter = new PalettePrinter(cellDimensions);
                // call paletteOrder()
                break;

      // default bit palette print
      case 3:   colors = new int[]{
                  Integer.parseInt(args[0]), 
                  Integer.parseInt(args[1]), 
                  Integer.parseInt(args[2])
                };
                bitPal = new BitPalette(colors);
                palPrinter = new PalettePrinter(cellDimensions,bitPal);
                break;

      // specified bit palette creation/print
      case 5:   colors = new int[] {
                  Integer.parseInt(args[0]), 
                  Integer.parseInt(args[1]), 
                  Integer.parseInt(args[2])
                };
                bitPal = new BitPalette(colors);
                palPrinter = new PalettePrinter(cellDimensions,bitPal);
                if (args[3] == "-f") {
                  createPng = false;
                  defaultOutput = false;
                } else if (args[3] == "-i") {
                  defaultOutput = false; 
                } else {
                  badArgsMessage();
                  return;
                }
                break;

      default:  badArgsMessage();
                return;
    }


    BitPalette testbitPal = new BitPalette(new int[]{2,2,2});
    System.out.printf("new palette created with rgb%d%d%d values \n",
        testbitPal.redBits(), testbitPal.blueBits(), testbitPal.greenBits());
    System.out.printf("%d rows, %d columns\n",testbitPal.paletteRows(),testbitPal.paletteCols());

    palPrinter = new PalettePrinter(new int[]{50,50}, testbitPal);

    /* Test code begin */
    palPrinter.drawGrid(testbitPal);
    
    palPrinter.writePng();
    /* end Test code */
    return;
  }
}
