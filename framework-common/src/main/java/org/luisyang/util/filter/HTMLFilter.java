package org.luisyang.util.filter;

import java.io.IOException;

/**
 * HTML过滤
 */
public class HTMLFilter extends AbstractFilter {
	
	public HTMLFilter(Appendable out) {
		super(out);
	}

	public Appendable append(char ch) throws IOException {
		if (Character.isISOControl(ch)) {
			return this;
		}
		switch (ch) {
		case '"':
		case '&':
		case '\'':
		case '<':
		case '>':
			this.out.append("&#");
			this.out.append(Integer.toString(ch));
			this.out.append(';');
			break;
		default:
			this.out.append(ch);
		}
		return this;
	}
}