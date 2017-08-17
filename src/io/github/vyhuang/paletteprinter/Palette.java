package paletteprinter;

import java.awt.Color;

public class BitPalette {
  int cellWidth, cellHeight;
  char redBits, blueBits, greenBits;
  Color[][] 

  Palette(int cW, int cH, char rB, char bB, char gB) {
    cellWidth = cW;
    cellHeight = cH;
    redBits = rB;
    blueBits = bB;
    greenBits = gB;
  }
}
