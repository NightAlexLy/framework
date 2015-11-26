package org.luisyang.util.filter;

import java.io.IOException;

/**
 * CVS文件过滤
 */
public class CVSFilter extends AbstractFilter {
	public CVSFilter(Appendable out) {
		super(out);
	}

	public Appendable append(char ch) throws IOException {
		if (Character.isISOControl(ch)) {
			return this;
		}
		if (ch == '"') {
			this.out.append('"');
			this.out.append('"');
		}
		return this;
	}
}
