package com.JP.HBS.IO;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;

public class JSONFileWriter extends JSONFileReader {
	private ObjectMapper mapper;
	private List<String> recordList;

	public JSONFileWriter(String filename) {
		super(filename);
	}

	public void addRecord(Object input) throws IOException {
		jsonRecord = readFile();
		mapper = new ObjectMapper();
		recordList = new ArrayList<String>();

		for (int i = 0; i < jsonRecord.size(); i++) {
			recordList.add(jsonRecord.get(i).toString());
		}

		recordList.add(mapper.writeValueAsString(input));
		writeFile();
	}

	public void updateRecord(String key, String value, Object input) throws IOException {
		jsonRecord = readFile();
		mapper = new ObjectMapper();
		recordList = new ArrayList<String>();

		for (int i = 0; i < jsonRecord.size(); i++) {
			JSONObject record = (JSONObject) jsonRecord.get(i);
			boolean recordMatched = record.get(key).equals(value);
			String jsonString = recordMatched ? mapper.writeValueAsString(input)
					: jsonRecord.get(i).toString();
			recordList.add(jsonString);
		}

		writeFile();
	}

	public void deleteRecord(String key, String value) throws FileNotFoundException {
		jsonRecord = readFile();
		recordList = new ArrayList<String>();

		for (int i = 0; i < jsonRecord.size(); i++) {
			JSONObject record = (JSONObject) jsonRecord.get(i);
			if (record.get(key).equals(value)) {
				continue;
			}
			recordList.add(jsonRecord.get(i).toString());
		}

		writeFile();
	}

	private void writeFile() throws FileNotFoundException {
		writer = new PrintWriter(filename);
		writer.println("[");

		for (int i = 0; i < recordList.size(); i++) {
			boolean lastIndex = i == recordList.size() - 1;
			writer.println(recordList.get(i) + (lastIndex ? "" : ","));
		}

		writer.println("]");
		writer.close();
	}
}
