package parser;

import MTDalgorithm.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class OutputFileWriter {

    private static final String LOCATION = System.getProperty("user.dir");

    private File outputFile;
    private List<String> lineList;

    public OutputFileWriter(String fileName) {
        lineList = new ArrayList<>();
        outputFile = new File(LOCATION + "\\" + fileName);
        Logger.log("create file " + outputFile.getPath());
        try {
            outputFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addLine(String line) {
        lineList.add(line);
    }

    public void writeToFile() {
        try {
            Files.write(outputFile.toPath(), lineList, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
