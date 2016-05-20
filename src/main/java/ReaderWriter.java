import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Class for read in textfile, remove specified lines and output cleaned textfile.
 *
 * @author Stefan Jahn
 * @since 10.07.15
 */
public class ReaderWriter {

    private String fileNameIn;
    private String fileNameOut;
    private boolean removeEmptyLines = true;
    private String newEntryPrefix;
    private ArrayList<String> singleLineArray = new ArrayList<String>();
    private ArrayList<String> stackTraceArray  = new ArrayList<String>();

    public ReaderWriter (String fileNameIn, String fileNameOut, String newEntryPrefix) {
        this.fileNameIn = fileNameIn;
        this.fileNameOut = fileNameOut;
        this.newEntryPrefix = newEntryPrefix;
    }


    public boolean readAndWriteLogfile() {
        // measuring the time for the whole process
        long startTime = System.currentTimeMillis();

        boolean successfull = false;
        BufferedReader bufferedReader;
        BufferedWriter bufferedWriter;
        int removedLines = 0;

        try {
            bufferedReader = new BufferedReader(new FileReader(fileNameIn));
            bufferedWriter = new BufferedWriter(new FileWriter(fileNameOut));
            String line;
            int counter = 1;
            boolean removeNext = false;
            // iterates through all lines
            while ((line = bufferedReader.readLine()) != null) {
                counter += 1;
                // remove empty lines
                if (removeEmptyLines && StringUtils.equals(line, EMPTY)) {
                    // do nothing
                    System.out.println(counter + " Removed empty line.");
                    removedLines += 1;
                } else {
                    // check if we still proceed with removing the next line
                    if (line.startsWith(newEntryPrefix)) {
                        removeNext = false;
                    }
                    // case remove stack trace also after line
                    // here we remove till we got a new entry
                    if (removeNext) {
                        System.out.println(counter + " Removed: " + line);
                        removedLines += 1;

                        // handle cases for array entries.
                    } else {

                        boolean removeForStackTrace = false;
                        for (String entry : stackTraceArray) {
                            removeForStackTrace = false;
                            if (line.contains(entry)) {
                                // set remove next to true and also remove current line.
                                removeNext = true;
                                removeForStackTrace = true;
                                // found one
                                break;
                            }
                        }

                        // case remove only one line
                        boolean removeForSingleLine = false;

                        // line doesn't exist in stackTrace array
                        // so check the others
                        if (!removeForStackTrace) {
                            for (String item : singleLineArray) {
                                removeForSingleLine = false;
                                if (line.contains(item)) {
                                    removeForSingleLine = true;
                                    //found
                                    break;
                                }
                            }
                        }

                        // here we remove or print....
                        if (removeForStackTrace || removeForSingleLine) {
                            System.out.println(counter + " Removed: " + line);
                            removedLines += 1;
                        } else {
                            bufferedWriter.write(line);
                            bufferedWriter.newLine();
                        }
                    }

                }


                // flush
                bufferedWriter.flush();
            }
            bufferedReader.close();
            bufferedWriter.close();
        } catch (FileNotFoundException e) {
            successfull = false;
            System.out.println("ERROR File not Found!");
            e.printStackTrace();
        } catch (IOException e) {
            successfull = false;
            System.out.println("ERROR: An exception occured!");
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        printStatistics(removedLines, executionTime);


        return successfull;
    }

    /**
     * Prints some additional statistics.
     *
     * @param removedLines number of lines that have been removed
     * @param executionTime execution time for the process
     */
    private void printStatistics(int removedLines, long executionTime) {
        System.out.println();
        System.out.println("-----------------------  STATISTICS -----------------------");
        System.out.println();
        System.out.println(removedLines + " lines has been removed.");
        System.out.println("It took " + executionTime + "ms to cleanup the logfile.");

        File fileIn = new File(fileNameIn);
        File fileOut = new File(fileNameOut);
        long fileSizeIn = fileIn.length() / 1000;
        long fileSizeOut = fileOut.length() / 1000;
        long reducedSize = fileSizeIn - fileSizeOut;

        System.out.println(fileNameIn + " has a size of " + fileSizeIn + "kb");
        System.out.println(fileNameOut + " has a size of " + fileSizeOut + "kb");
        System.out.println("Reduced the file by " + reducedSize + "kb");
        System.out.println();
        System.out.println("-----------------------------------------------------------");
    }

    public void addToSingleLineArray(String lineToRemove) {
        singleLineArray.add(lineToRemove);
    }

    public void addToStackTraceArray(String lineToRemove) {
        stackTraceArray.add(lineToRemove);
    }


    public String getFileNameIn() {
        return fileNameIn;
    }

    public void setFileNameIn(String fileNameIn) {
        this.fileNameIn = fileNameIn;
    }

    public String getFileNameOut() {
        return fileNameOut;
    }

    public void setFileNameOut(String fileNameOut) {
        this.fileNameOut = fileNameOut;
    }

    public boolean isRemoveEmptyLines() {
        return removeEmptyLines;
    }

    public void setRemoveEmptyLines(boolean removeEmptyLines) {
        this.removeEmptyLines = removeEmptyLines;
    }

    public String getNewEntryPrefix() {
        return newEntryPrefix;
    }

    public void setNewEntryPrefix(String newEntryPrefix) {
        this.newEntryPrefix = newEntryPrefix;
    }


}
