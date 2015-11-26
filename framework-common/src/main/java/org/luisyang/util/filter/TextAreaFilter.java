package org.luisyang.util.filter;

import java.io.IOException;

/**
 * TextArea富文本过滤 
 */
public class TextAreaFilter extends AbstractFilter {
	public TextAreaFilter(Appendable out) {
		super(out);
	}

	public Appendable append(char ch) throws IOException {
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
