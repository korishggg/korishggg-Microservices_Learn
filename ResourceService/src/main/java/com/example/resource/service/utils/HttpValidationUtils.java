package com.example.resource.service.utils;

import com.example.resource.service.exceptions.RangeFormatException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpValidationUtils {

	private static final Pattern RANGE_PATTERN = Pattern.compile("^([1-9]\\d{0,18})-([1-9]\\d{0,18})$");

	public static Long[] parseRangeHeaders(String rangeHeader) {
		Matcher matcher = RANGE_PATTERN.matcher(rangeHeader);
		if (matcher.matches()) {
			var start = Long.parseLong(matcher.group(1));
			var end = Long.parseLong(matcher.group(2));
			return new Long[]{start, end};
		} else {
			throw new RangeFormatException();
		}
	}
}
