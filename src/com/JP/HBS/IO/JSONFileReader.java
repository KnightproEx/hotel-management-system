package com.JP.HBS.IO;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.JP.HBS.Model.Booking;

public class JSONFileReader {
	protected String filename;
	protected PrintWriter writer;
	protected JSONArray jsonRecord;

	private JSONParser parser;

	public JSONFileReader(String filename) {
		this.filename = filename;
	}

	public JSONArray readFile() throws FileNotFoundException {
		jsonRecord = new JSONArray();
		parser = new JSONParser();

		try {
			jsonRecord = (JSONArray) parser.parse(new FileReader(filename));
		} catch (IOException | ParseException e) {
			writer = new PrintWriter(filename);
			writer.println("[]");
			writer.close();
		}

		return jsonRecord;
	}

	public JSONArray searchRecord(String value, String... key) throws FileNotFoundException {
		jsonRecord = readFile();

		if (value.isBlank()) {
			return jsonRecord;
		}

		for (int i = 0; i < jsonRecord.size(); i++) {
			JSONObject record = (JSONObject) jsonRecord.get(i);
			boolean isMatch = false;

			for (String s : key) {
				if (record.get(s).toString().contains(value)) {
					isMatch = true;
				}
			}

			if (!isMatch) {
				jsonRecord.remove(i--);
			}
		}

		return jsonRecord;
	}
	
	public boolean isAvailable(String room, LocalDate selected, int duration) throws FileNotFoundException {
		if (selected.isBefore(LocalDate.now())) {
			return false;
		}
		
		jsonRecord = readFile();

		for (int i = 0; i < jsonRecord.size(); i++) {
			JSONObject record = (JSONObject) jsonRecord.get(i);
			Booking booking = Booking.convertObject(record);
			LocalDate starting = LocalDate.parse(booking.getDate());
			LocalDate ending = starting.plusDays(booking.getDuration() - 1);
			LocalDate selectedEnd = selected.plusDays(duration - 1);

			if (!booking.getRoom().equals(room)) {
				continue;
			}
			
			if (!selected.isBefore(starting) && !selectedEnd.isAfter(ending)) return false;
			if (!selected.isAfter(starting) && !selectedEnd.isBefore(starting) && duration > 1) return false;
			if (!selected.isAfter(starting) && !selectedEnd.isBefore(ending) && duration > 1) return false;
			if (!selected.isAfter(ending) && !selectedEnd.isBefore(ending) && duration > 1) return false;
		}
		
		return true;
	}

	public String getBookingID() throws FileNotFoundException {
		final String prefix = "B";
		final String firstNum = "00001";

		jsonRecord = readFile();
		int num = 0;

		if (jsonRecord.isEmpty()) {
			return prefix + firstNum;
		}

		for (int i = 0; i < jsonRecord.size(); i++) {
			JSONObject record = (JSONObject) jsonRecord.get(i);
			String id = record.get("id").toString();
			int currentNum = Integer.parseInt(id.substring(prefix.length(), id.length()));
			num = currentNum > num ? currentNum : num;
		}

		return prefix + String.format("%05d", num + 1);
	}
}
