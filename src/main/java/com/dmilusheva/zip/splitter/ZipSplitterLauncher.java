package com.dmilusheva.zip.splitter;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;
import static com.dmilusheva.zip.splitter.Constants.ARGS_DELIMITER;
import static com.dmilusheva.zip.splitter.Constants.HELP_ARG_KEY;
import static com.dmilusheva.zip.splitter.Constants.MAX_SIZE_IN_MB_ARG_KEY;
import static com.dmilusheva.zip.splitter.Constants.OUTPUT_PATH_ARG_KEY;
import static com.dmilusheva.zip.splitter.Constants.ZIP_PATH_ARG_KEY;

/**
 * This is the main class, responsible for parsing cli arguments
 * and calling applicable function - split zip
 */
public class ZipSplitterLauncher {

    private static Logger logger = Logger.getLogger(ZipSplitterLauncher.class.getName());

    public static void main(String args[]) throws IOException {
        validateArgumentsInput(args);
        Path zipPath = extractZipPathFromArguments(args[0]);
        long maxSizeInMb = extractMaxSizeInMbFromArguments(args[1]);
        Path outputPath = zipPath.getParent();

        boolean isOutputPathProvided = false;
        for (String arg : args) {
            if (arg.contains(OUTPUT_PATH_ARG_KEY)) {
                isOutputPathProvided = true;
                break;
            }
        }

        if (isOutputPathProvided) {
            outputPath = extractOutputPathFromArguments(args[2]);
        }

        // TODO: add validation of the input for zipPath and maxSizeInMb

        ZipSplitter zipSplitter = new ZipSplitter(zipPath, maxSizeInMb, outputPath);
        zipSplitter.splitZip();
    }

    /**
     * Check the program arguments input: <br>
     *    - if one argument is given as an input, and if it is {@HELP_ARG Constants#HELP_ARG} - help menu will be printed
     *    - if two arguments are given as an input - Zip Splitter will pass successfully
     *
     * @param args an array from the given arguments
     */
    protected static void validateArgumentsInput(String args[]) {
        if (args[0].equals(HELP_ARG_KEY)) {
            printHelp();
            return;
        }

        boolean isZipPathProvided = false;
        boolean isMaxSizeInMbProvided = false;
        for (String arg : args) {
            if (arg.contains(ZIP_PATH_ARG_KEY)) {
                isZipPathProvided = true;
            } else if (arg.contains(MAX_SIZE_IN_MB_ARG_KEY)) {
                isMaxSizeInMbProvided = true;
            }
        }

        if (!(isZipPathProvided && isMaxSizeInMbProvided)) {
            printHelp();
            throw new IllegalArgumentException(
                    "The Zip Splitter should be run by specifying the zip path and the max size in MB as an arguments.");
        }
    }

    /**
     * Extract the zip path from the input arguments and check if it is correct.
     *
     * @param zipPathArgument input argument for the zipPath
     * @return a zip path
     */
    protected static Path extractZipPathFromArguments(String zipPathArgument) {
        String[] zipPathArgumentPair = zipPathArgument.split(ARGS_DELIMITER);
        if (zipPathArgumentPair.length < 2 || !ZIP_PATH_ARG_KEY.equals(zipPathArgumentPair[0])) {
            printHelp();
            throw new IllegalArgumentException(
                    "The zipPath argument must be in form: -zipPath=<zipPath>. " +
                            "Illegal input argument: " + zipPathArgument);
        }
        return Paths.get(zipPathArgumentPair[1]);
    }

    /**
     * Extract the maximum size in MB from the input arguments and check if it is correct.
     *
     * @param maxSizeInMbArgument input arguments for tha maxSizeInMbArgument
     * @return a maximum size in MB
     */
    protected static long extractMaxSizeInMbFromArguments(String maxSizeInMbArgument) {
        String[] maxSizeInMbArgumentPair = maxSizeInMbArgument.split(ARGS_DELIMITER);
        if (maxSizeInMbArgumentPair.length < 2 || !MAX_SIZE_IN_MB_ARG_KEY.equals(maxSizeInMbArgumentPair[0])) {
            printHelp();
            throw new IllegalArgumentException(
                    "The maxSizeInMb argument must be in form: -maxSizeInMbArgument=<maxSizeInMbArgument>. " +
                            "Illegal input argument: " + maxSizeInMbArgument);
        }
        return Long.parseLong(maxSizeInMbArgumentPair[1]);
    }

    /**
     * Extract the output path from the input arguments and check if it is correct.
     *
     * @param outputPath input arguments for tha maxSizeInMbArgument
     * @return an output path
     */
    protected static Path extractOutputPathFromArguments(String outputPath) {
        String[] outputPathArgumentPair = outputPath.split(ARGS_DELIMITER);
        if (outputPathArgumentPair.length < 2 || !OUTPUT_PATH_ARG_KEY.equals(outputPathArgumentPair[0])) {
            printHelp();
            throw new IllegalArgumentException(
                    "The maxSizeInMb argument must be in form: -maxSizeInMbArgument=<maxSizeInMbArgument>. " +
                            "Illegal input argument: " + outputPathArgumentPair);
        }
        return Paths.get(outputPathArgumentPair[1]);
    }

    /**
     * Print help about how to execute the program
     */
    protected static void printHelp() {
        StringBuilder sb = new StringBuilder("Zip Splitter");
        sb.append("\n\n");
        sb.append("Usage:");
        sb.append("\n");
        sb.append(ZIP_PATH_ARG_KEY + "={Absolute path to the zip file (1st parameter)}");
        sb.append("\n");
        sb.append(MAX_SIZE_IN_MB_ARG_KEY + "={Maximum size of the newly split zip files in MB (2nd parameter)}");
        sb.append("\n");
        sb.append(OUTPUT_PATH_ARG_KEY + "={Absolute path to the place where files will be saved (by default equals the input's zip path ) - optional }");
        sb.append("\n");
        sb.append(HELP_ARG_KEY + " - Display this usage information");

        System.out.print(sb);
    }
}
