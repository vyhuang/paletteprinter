package paletteprinter;

import java.io.*;
import javax.imageio.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import java.awt.Rectangle;
/**
**/
public class PalettePrinter {
  int cellWidth, cellHeight;
  char redBits, blueBits, greenBits;

  PalettePrinter(int cW, int cH, char rB, char bB, char gB) {
    cellWidth = cW;
    cellHeight = cH;
    redBits = rB;
    blueBits = bB;
    greenBits = gB;
  }

  public static void main(String[] args) {
    File inputfile, outputfile;
    BufferedImage image;

    PalettePrinter pp = new PalettePrinter(50,50,(char)2,(char)2,(char)2);
    image = new BufferedImage(pp.cellWidth, pp.cellHeight, BufferedImage.TYPE_INT_RGB);

    Color base = new Color(0,0,255);

    for (int i = 0; i < pp.cellWidth; i += 1) {
      for (int j = 0; j < pp.cellHeight; j += 1) {
        image.setRGB(i,j, base.getRGB());
      }
    }

    Graphics2D g2d = image.createGraphics();
    g2d.setColor(Color.red);
    g2d.fill(new Rectangle.Float(10,10,25,25));
    g2d.dispose();

    try {
      outputfile = new File("image_grid.png");
      ImageIO.write(image, "png", outputfile);
    } catch (IOException e) {
      System.out.println("Image failed to be saved.");
    }

    return;
  }
}
