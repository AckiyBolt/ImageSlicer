package ua.bolt.tbot.slicer;

import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by kbelentsov on 22.12.2015.
 */
public class FileUtil {

    private static final String JPG = "jpg";
    private static final String EXTENSION_ZIP = ".zip";
    private static final String EXTENSION_JPG = ".jpg";

    public static BufferedImage read(File file) {
        try {
            return ImageIO.read(file);

        } catch (IOException e) {
            throw new FileProcessingException(e);
        }
    }

    public static File writeAsJpeg(String dir, String filename, BufferedImage img) {
        File file = new File(getPath(dir, filename, EXTENSION_JPG));
        try {
            ImageIO.write(img, JPG, file);

        } catch (IOException e) {
            throw new FileProcessingException(e);
        }
        return file;
    }

    public static File getFileFromWeb(String dir, String filename, String fullFilePath) {
        File fileOnDisc = new File(getPath(dir, filename, EXTENSION_JPG));
        try {
            FileUtils.copyURLToFile(new URL(fullFilePath), fileOnDisc);

        } catch (IOException e) {
            throw new FileProcessingException("Can't download file");
        }
        return fileOnDisc;
    }

    public static File writeZipFile(String dir, String filename, List<File> fileList) {
        File zipFile = new File(getPath(dir, filename, EXTENSION_ZIP));

        try {
            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);

            for (File file : fileList) {
                    addToZip(file.getName(), file, zos);
            }

            zos.close();
            fos.close();

        } catch (IOException e) {
            throw new FileProcessingException(e);
        }

        return zipFile;
    }

    private static void addToZip(String name, File file, ZipOutputStream zos)throws IOException {

        FileInputStream fis = new FileInputStream(file);

        ZipEntry zipEntry = new ZipEntry(name);
        zos.putNextEntry(zipEntry);

        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zos.write(bytes, 0, length);
        }

        zos.closeEntry();
        fis.close();
    }

    private static String getPath(String dir, String filename, String extension) {
        return dir + File.separator + filename + extension;
    }

    public static void deleteDir(String dir) {
        try {
            FileUtils.deleteDirectory(new File(dir));
        } catch (IOException e) {
            throw new FileProcessingException(e);
        }
    }
}
