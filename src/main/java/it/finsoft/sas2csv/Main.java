package it.finsoft.sas2csv;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Locale;

import com.epam.parso.CSVDataWriter;
import com.epam.parso.SasFileReader;
import com.epam.parso.impl.CSVDataWriterImpl;
import com.epam.parso.impl.SasFileReaderImpl;

public class Main {

	public static void main(String[] args) throws IOException {

		//debug
		// args = new String[] {"c:\\Users\\lucav\\Downloads\\migr_co_st1_2_err.sas7bdat"};
		
		// Usage
		if (args.length == 0 || args[0].equals("-h") || args[0].equals("--help") || args[0].equals("/?")) {
			System.err.println("Usage:");
			System.err.println("  java -jar sas2csv.jar <filename.sas7db>");
			System.err.println("or");
			System.err.println("  sas2csv.bat <filename.sas7db>");
			System.err.println("File output is <filename>.csv");
			return;
		}

		// Input file
		String filename = args[0];
		System.err.println("Reading file: " + filename);
		FileInputStream is;
		try {
			is = new FileInputStream(filename);
		} catch (FileNotFoundException e) {
			System.err.println("File not found: " + filename);
			return;
		}
		SasFileReader sasFileReader = new SasFileReaderImpl(is);

		// Output file
		String outfilename = filename.substring(0, filename.lastIndexOf(".")) + ".csv";
		System.err.println("Writing file: " + outfilename);
		Writer writer = new FileWriter(outfilename);

		// Write header
        // TODO take parameters from CLI
		CSVDataWriter csvDataWriter = new CSVDataWriterImpl(writer, ";", "\r\n", new Locale("it", "it"));
		csvDataWriter.writeColumnNames(sasFileReader.getColumns());

		// Write data
		long numRows = sasFileReader.getSasFileProperties().getRowCount();
		System.err.println("Writing " + numRows + " rows...");
        long writtenRows = 0;
		try {
			for (writtenRows = 0; writtenRows < numRows; ++writtenRows) {
				Object[] row = sasFileReader.readNext();
				csvDataWriter.writeRow(sasFileReader.getColumns(), row);
			}
		} catch (Throwable t) {
			System.err.println(writtenRows + " rows written.");
            throw t;
		}

		// close file
		try {
			is.close();
		} catch (IOException e) {
			// do nothing
		}

		System.err.println("Done.");
	}

}
