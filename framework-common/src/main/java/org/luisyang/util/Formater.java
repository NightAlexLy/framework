package org.luisyang.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Formater {
	protected static final DecimalFormat RATE_FORMAT = new DecimalFormat("#0.00");
	protected static final DecimalFormat RATE_PERCENT_FORMAT = new DecimalFormat("#0.00%");
	protected static final DecimalFormat PROESS_FORMAT = new DecimalFormat("#0%");
	protected static final DecimalFormat AMOUNT_SPLIT_FORMAT = new DecimalFormat("#,##,##0.00");
	protected static final DecimalFormat AMOUNT_FORMAT = new DecimalFormat("#####0.00");
	protected static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	protected static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
	protected static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static String formatDate(Date date) {
		if (date == null) {
			return "";
		}
		return DATE_FORMAT.format(date);
	}

	public static String formatTime(Date date) {
		if (date == null) {
			return "";
		}
		return TIME_FORMAT.format(date);
	}

	public static String formatDateTime(Date date) {
		if (date == null) {
			return "";
		}
		return DATETIME_FORMAT.format(date);
	}

	public static String formatRate(float number) {
		return RATE_PERCENT_FORMAT.format(number);
	}

	public static String formatRate(double number) {
		return RATE_PERCENT_FORMAT.format(number);
	}

	public static String formatRate(BigDecimal number) {
		return RATE_PERCENT_FORMAT.format(number);
	}

	public static String formatRate(float number, boolean includePercent) {
		return includePercent ? RATE_PERCENT_FORMAT.format(number) : RATE_FORMAT.format(number * 100.0F);
	}

	public static String formatRate(double number, boolean includePercent) {
		return includePercent ? RATE_PERCENT_FORMAT.format(number) : RATE_FORMAT.format(number * 100.0D);
	}

	public static String formatRate(BigDecimal number, boolean includePercent) {
		return includePercent ? RATE_PERCENT_FORMAT.format(number)
				: RATE_FORMAT.format(number.multiply(new BigDecimal(100)));
	}

	public static int roundProgress(double number) {
		double v = number * 100.0D;
		if (v <= 0.0D) {
			return 0;
		}
		if (v <= 1.0D) {
			return 1;
		}
		return (int) (Math.floor(number * 100.0D) / 100.0D);
	}

	public static int roundProgress(float number) {
		double v = number * 100.0F;
		if (v <= 0.0D) {
			return 0;
		}
		if (v <= 1.0D) {
			return 1;
		}
		return (int) (Math.floor(number * 100.0F) / 100.0D);
	}

	public static String formatProgress(double number) {
		double v = number * 100.0D;
		if (v <= 0.0D) {
			return "0%";
		}
		if (v <= 1.0D) {
			return "1%";
		}
		return PROESS_FORMAT.format(Math.floor(number * 100.0D) / 100.0D);
	}

	public static String formatProgress(float number) {
		double v = number * 100.0F;
		if (v <= 0.0D) {
			return "0%";
		}
		if (v <= 1.0D) {
			return "1%";
		}
		return PROESS_FORMAT.format(Math.floor(number * 100.0F) / 100.0D);
	}

	public static String formatAmount(int number) {
		return AMOUNT_SPLIT_FORMAT.format(number);
	}

	public static String formatAmount(long number) {
		return AMOUNT_SPLIT_FORMAT.format(number);
	}

	public static String formatAmount(float number) {
		return AMOUNT_SPLIT_FORMAT.format(number);
	}

	public static String formatAmount(double number) {
		return AMOUNT_SPLIT_FORMAT.format(number);
	}

	public static String formatAmount(BigDecimal number) {
		if (number == null) {
			return "";
		}
		return AMOUNT_SPLIT_FORMAT.format(number);
	}

	public static String formatAmount(BigInteger number) {
		if (number == null) {
			return "";
		}
		return AMOUNT_SPLIT_FORMAT.format(number);
	}

	public static String formatAmount(int number, boolean split) {
		return split ? AMOUNT_SPLIT_FORMAT.format(number) : AMOUNT_FORMAT.format(number);
	}

	public static String formatAmount(long number, boolean split) {
		return split ? AMOUNT_SPLIT_FORMAT.format(number) : AMOUNT_FORMAT.format(number);
	}

	public static String formatAmount(float number, boolean split) {
		return split ? AMOUNT_SPLIT_FORMAT.format(number) : AMOUNT_FORMAT.format(number);
	}

	public static String formatAmount(double number, boolean split) {
		return split ? AMOUNT_SPLIT_FORMAT.format(number) : AMOUNT_FORMAT.format(number);
	}

	public static String formatAmount(BigDecimal number, boolean split) {
		if (number == null) {
			return "";
		}
		return split ? AMOUNT_SPLIT_FORMAT.format(number) : AMOUNT_FORMAT.format(number);
	}

	public static String formatAmount(BigInteger number, boolean split) {
		if (number == null) {
			return "";
		}
		return split ? AMOUNT_SPLIT_FORMAT.format(number) : AMOUNT_FORMAT.format(number);
	}
}
