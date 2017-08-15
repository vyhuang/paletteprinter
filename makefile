SHELL = /bin/sh
.SUFFIXES:
.SUFFIXES: .java .class

JFLAGS = -g
JC = javac
JVM = java

srcdir = src/io/github/vyhuang/paletteprinter
targetdir = target

all: classes

classes: paletteprinter.class

paletteprinter.class: $(srcdir)/PalettePrinter.java
	$(JC) $(JFLAGS) $(srcdir)/PalettePrinter.java -d $(targetdir)

run: classes
	$(JVM) -cp $(targetdir) PalettePrinter
