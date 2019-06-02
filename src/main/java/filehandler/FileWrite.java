package main.java.filehandler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileWrite {

    /**
     * writeFile takes the output from the GUI and writes it to a file.
     * Authors note: I was originally going to use FileChannel and RandomAccessFile
     * but it seems a bit overkill for what we need. This BufferedWriter should do
     * just fine.
     * We wrap the FileWriter in a BufferedWriter for performance, and also to gain
     * access to the newLine() method. All of our validation is done for us, so all
     * we have to do is write what we're given to the file.
     *
     * @param arguments a list of arguments given from the GUI as an ArrayList.
     * @param filePath the complete file path to write to.
     * @throws IOException
     */
    public void writeFile(List<String> arguments, String filePath) throws IOException {
        if(filePath.isEmpty()||filePath.isBlank()) return;

        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false));
        for (String arg: arguments) {
            writer.write(arg);
            writer.newLine();
        }
        writer.close();
    }
}
