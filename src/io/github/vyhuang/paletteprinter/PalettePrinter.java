package paletteprinter;

import java.io.*;
import javax.imageio.*;
import java.awt.image.*;
import java.awt.Color;
/**
**/
public class PalettePrinter {
  public static void main(String[] args) {

    int width, height;
    width = height = 50;
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    Color base = new Color(0,0,255);

    for (int i = 0; i < width; i += 1) {
      for (int j = 0; j < width; j += 1) {
        image.setRGB(i,j, base.getRGB());
      }
    }

    try {
      File outputfile = new File("image_grid.png");
      ImageIO.write(image, "png", outputfile);
    } catch (IOException e) {
      System.out.println("Image failed to be saved.");
    }

    return;
  }
}
