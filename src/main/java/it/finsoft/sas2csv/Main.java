package it.finsoft.sas2csv;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import com.epam.parso.CSVDataWriter;
import com.epam.parso.SasFileReader;
import com.epam.parso.impl.CSVDataWriterImpl;
import com.epam.parso.impl.SasFileReaderImpl;

public class Main {

	public static void main(String[] args) throws IOException {

		// Usage
		if (args.length == 0 || args[0].equals("-h") || args[0].equals("--help") || args[0].equals("/?")) {
			System.err.println("Usage: java -jar Sas2Csv-jar <filename.sas7db>");
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
		// CSVMetadataWriter csvMetadataWriter = new CSVMetadataWriterImpl(writer);
		// csvMetadataWriter.writeMetadata(sasFileReader.getColumns());
		CSVDataWriter csvDataWriter = new CSVDataWriterImpl(writer);
		csvDataWriter.writeColumnNames(sasFileReader.getColumns());

		// Write data
		long numRows = sasFileReader.getSasFileProperties().getColumnsCount();
		for (int i = 0; i < numRows; ++i) {
			csvDataWriter.writeRow(sasFileReader.getColumns(), sasFileReader.readNext());
		}

		// close file
		try {
			is.close();
		} catch (IOException e) {
			// do nothing
		}
	}

}
