package com.dmilusheva.zip.splitter;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import static com.dmilusheva.zip.splitter.Constants.PART_POSTFIX;
import static com.dmilusheva.zip.splitter.Constants.ZIP_FILE_EXTENSION;

public class ZipSplitter {

    private static Path zipPath;
    private static long maxSizeInMb;
    private static Path outputPath;
    private static int currentChunkIndex = 1;

    public ZipSplitter() { }

    public ZipSplitter(Path zipPath, long maxSizeInMb, Path outputPath) {
        this.zipPath = zipPath;
        this.maxSizeInMb = maxSizeInMb;
        this.outputPath = outputPath;
    }

    protected static void splitZip() throws IOException {
        System.out.println("Process the whole zip file..");

        ZipEntry entry = null;
        int currentChunkedIndex = 1;
        long currentSize = 0;

        //using just to get the uncompressed size of the zipEntries
        long entryCompressedSize = 0;
        ZipFile zipFile = new ZipFile(zipPath.toFile());
        Enumeration enumeration = zipFile.entries();


        String zipCoreName = zipPath.getFileName().toString().replace(ZIP_FILE_EXTENSION, "");
        String splitZipPathStr = new File(
                outputPath.toString(),
                zipCoreName + PART_POSTFIX + currentChunkIndex + ZIP_FILE_EXTENSION).toString();

        ZipOutputStream zipOutputStream = null;

        try (FileInputStream fileInputStream = new FileInputStream(zipPath.toString()); 
             ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
             FileOutputStream fileOutputStream = new FileOutputStream(splitZipPathStr);
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream)) {

            zipOutputStream = new ZipOutputStream(bufferedOutputStream);

            while ((entry = zipInputStream.getNextEntry()) != null && enumeration.hasMoreElements()) {
                ZipEntry zipEntry = (ZipEntry) enumeration.nextElement(); 
                System.out.println("Processing zip entry " + zipEntry.getName() + " with size " + zipEntry.getSize());
                entryCompressedSize = zipEntry.getCompressedSize();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                if ((currentSize + entryCompressedSize) >= maxSizeInMb) {
                    zipOutputStream.close();
                    currentChunkIndex++;
                    zipOutputStream = getOutputStream(zipCoreName, outputPath);
                    currentSize = 0;
                }

                currentSize += entryCompressedSize;
                zipOutputStream.putNextEntry(new ZipEntry(entry.getName()));
                byte[] buffer = new byte[8192];
                int length = 0;
                while ((length = zipInputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }

                byte[] unzippedFile = outputStream.toByteArray();
                zipOutputStream.write(unzippedFile);
                unzippedFile = null;
                outputStream.close();
                zipInputStream.closeEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            zipOutputStream.close();
            zipFile.close();
        }
    }

    private static ZipOutputStream getOutputStream(String zipCoreName, Path outputPath) throws IOException {
        ZipOutputStream zipOutputStream = new ZipOutputStream(
                new FileOutputStream(
                        new File(
                                outputPath.toString(),
                                zipCoreName + PART_POSTFIX + currentChunkIndex + ZIP_FILE_EXTENSION).toString()));
        return zipOutputStream;
    }
}
