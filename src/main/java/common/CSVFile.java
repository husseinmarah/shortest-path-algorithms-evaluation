package common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;
import com.opencsv.CSVWriter;
/**
 *@author <a href="https://github.com/husseinmarah">Hussein Marah</a>
 */
public class CSVFile {
static Logger logger = Logger.getLogger(CSVFile.class.getName());
static File file;
static FileWriter output;
static CSVWriter writer;

public static void createCSVFile(String name) {
    // create the csv file to store the coordinates
    String filename = String.format("dataset\\%s_results.csv",name);
    file = new File(filename);
    if (file.exists() && !file.isDirectory()) {
        System.out.println("CSV File Is Already Exists: " + filename);
        try {
            output = new FileWriter(filename, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        writer = new CSVWriter(output);
    } else {
        System.out.println("Creating CSV File: " + file);
        try {
            output = new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        writer = new CSVWriter(output);
        String[] header = {"Rows", "Columns", "Number of Cells", "Iteration", "Median Time", "Average Time", "Total Traversal Time"};
        writer.writeNext(header);
    }
}
public static void updateCSV(String[] data) throws IOException {
    // write the coordinates x and y to the csv file
    try {
        writer.writeNext(data);
    } catch (Exception e) {
        e.printStackTrace();
    }
}

public static void closeCSVFile() throws IOException {
    System.out.println("Closing CSV File: "+file);
    //close the cvs file writer
    try {
        if (writer != null) {
            writer.flush();
            writer.close();
        }
    } catch (final Exception e) {
        writer = null;
    }
}
}