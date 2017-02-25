import ConvChain.Generator;
import processing.core.PApplet;
import processing.core.PImage;

public class Main extends PApplet {
    public static void main(String[] args) {
        PApplet.main(Main.class.getCanonicalName());
    }

    public void settings() {
        size(256, 128);
        noSmooth();
    }

    boolean[][] toArray(PImage image) {
        boolean[][] result = new boolean[image.width][image.height];
        for (int x = 0; x < image.width; ++x)
            for (int y = 0; y < image.height; ++y) {
                int color = image.pixels[y * image.width + x];
                if (color == -16777216)
                    result[x][y] = false; // black
                else
                    result[x][y] = true; // white
            }
        return result;
    }

    PImage toPImage(boolean[][] array, PImage result) {
        if (result == null)
            result = createImage(array.length, array[0].length, RGB);

        for (int x = 0; x < array.length; ++x)
            for (int y = 0; y < array[0].length; ++y) {
                if (array[x][y])
                    result.pixels[y * result.width + x] = color(1);
                else
                    result.pixels[y * result.width + x] = color(0);
            }
        result.updatePixels();
        return result;
    }


    PImage image;
    Generator generator;
    PImage output;

    void updateOutput() {
        output = toPImage(generator.field, output);
    }

    public void setup() {
        colorMode(RGB, 1, 1, 1);
        image = loadImage("img/Less Rooms.bmp");
        generator = new Generator(toArray(image), 3, 1.2, 32, 32);
        updateOutput();
    }

    public void draw() {
        image(image, 0, 0, 128, 128);
        image(output, 128, 0, 128, 128);
    }

    @Override
    public void keyPressed() {
        generator.iterate();
        updateOutput();
    }
}
