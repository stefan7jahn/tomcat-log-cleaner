
/**
 * Class for read in textfile, remove specified lines and output cleaned textfile.
 *
 * @author Stefan Jahn
 * @since 10.07.15
 */
public class Main {

    public static void main(String[] args) {
        String inputFile = "test.txt";
        String outputFile = "output.txt";
        String lineSeparator = "2016";

        ReaderWriter readerWriter = new ReaderWriter(inputFile, outputFile, lineSeparator);

        // single lines
        readerWriter.addToSingleLineArray("com.jahn.logcleaner");

        // also removing stacktrace
        readerWriter.addToStackTraceArray("com.jahn.logcleaner");



        readerWriter.readAndWriteLogfile();

    }
}
