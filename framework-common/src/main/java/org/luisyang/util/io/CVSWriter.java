package org.luisyang.util.io;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

import org.luisyang.util.Formater;
import org.luisyang.util.StringHelper;

/**
 * CVS写入
 */
public class CVSWriter {
	protected Appendable out;
	protected transient boolean notFirst = false;

	public CVSWriter(Appendable out) {
		this.out = out;
	}

	/**
	 * 新建一行
	 * @throws IOException
	 */
	public void newLine() throws IOException {
		this.notFirst = false;
		this.out.append('\r');
		this.out.append('\n');
	}

	/**
	 * 写入属性
	 * @param field
	 * @throws IOException
	 */
	public void write(String field) throws IOException {
		if (this.notFirst) {
			this.out.append(',');
		} else {
			this.notFirst = true;
		}
		if (!StringHelper.isEmpty(field)) {
			this.out.append('"');
			int length = field.length();
			for (int index = 0; index < length; index++) {
				char ch = field.charAt(index);
				if (ch == '"') {
					this.out.append('"');
					this.out.append('"');
				} else {
					this.out.append(ch);
				}
			}
			this.out.append('"');
		}
	}

	/**
	 * 写入对象
	 * @param field
	 * @throws IOException
	 */
	public void write(Object field) throws IOException {
		if (field != null) {
			write(field.toString());
		}
	}

	/**
	 * 写入日期
	 * @param field
	 * @throws IOException
	 */
	public void write(Date field) throws IOException {
		if (field != null) {
			write(Formater.formatDate(field));
		}
	}

	/**
	 * 写入时间
	 * @param field
	 * @throws IOException
	 */
	public void write(Time field) throws IOException {
		if (field != null) {
			write(Formater.formatTime(field));
		}
	}

	/**
	 * 写入精确时间
	 * @param field
	 * @throws IOException
	 */
	public void write(Timestamp field) throws IOException {
		if (field != null) {
			write(Formater.formatDateTime(field));
		}
	}

	/**
	 * 写入整型
	 * @param field
	 * @throws IOException
	 */
	public void write(int field) throws IOException {
		write(Integer.toString(field));
	}

	public void write(double field) throws IOException {
		write(Double.toString(field));
	}

	public void write(BigDecimal field) throws IOException {
		if (field != null) {
			write(Formater.formatAmount(field));
		}
	}

	public void write(float field) throws IOException {
		write(Float.toString(field));
	}

	public void write(short field) throws IOException {
		write(Short.toString(field));
	}

	public void write(byte field) throws IOException {
		write(Byte.toString(field));
	}

	public void write(boolean field) throws IOException {
		write(field ? "true" : "false");
	}
}
