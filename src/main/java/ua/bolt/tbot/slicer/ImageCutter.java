package ua.bolt.tbot.slicer;

import org.imgscalr.Scalr;
import ua.bolt.tbot.slicer.model.CutImageResult;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kbelentsov on 22.12.2015.
 */
public class ImageCutter {

    private static final int COLUMN_COUNT = 6;

    public CutImageResult cutImages(File imageFile, String updateDir) {
        BufferedImage img = FileUtil.read(imageFile);
        int rowCount = calcRowCount(img);
        img = scaleImage(img, rowCount);

        List<File> files = new ArrayList<>(COLUMN_COUNT * rowCount);

        int blockHeight = img.getHeight() / rowCount;
        int blockWeight = img.getWidth() / COLUMN_COUNT;

        for (int v = 0; v != rowCount; v++) {
            for (int h = 0; h != COLUMN_COUNT; h++) {

                int x = blockWeight * h;
                int y = blockHeight * v;

                BufferedImage block = Scalr.crop(img, x, y, blockWeight, blockHeight);

                String blockFilename = String.format("%sx%s", v + 1, h + 1);

                File file = FileUtil.writeAsJpeg(updateDir, blockFilename, toSquare(block));
                files.add(file);
            }
        }
        return CutImageResult.build(rowCount, COLUMN_COUNT, files);
    }

    private BufferedImage toSquare(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();

        int min = Math.min(width, height);
        return Scalr.resize(img, Scalr.Method.ULTRA_QUALITY, min);
    }

    private int calcRowCount(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        int targetWidth = scaleToProportion(width, COLUMN_COUNT);
        int blockWidth = targetWidth / COLUMN_COUNT;

        return height / blockWidth;
    }

    private static BufferedImage scaleImage(BufferedImage img, int rowCount) {
        int width = img.getWidth();
        int height = img.getHeight();
        int targetWidth = scaleToProportion(width, COLUMN_COUNT);
        int targetHeight = scaleToProportion(height, rowCount);

        return Scalr.resize(img, Scalr.Method.ULTRA_QUALITY, targetWidth, targetHeight);
    }

    private static int scaleToProportion(int size, int count) {
        return size - (size % count);
    }
}
