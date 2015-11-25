package org.luisyang.util.filter;

import java.io.IOException;

/**
 *  抽象字符串过滤器<br/>
 *  public interface Appendable能够被追加 char 序列和值的对象。如果某个类的实例打算接收来自 Formatter 的格式化输出，那么该类必须实现 Appendable 接口
 *  @author LuisYang
 */
public abstract class AbstractFilter implements Appendable {
	protected final Appendable out;

	public AbstractFilter(Appendable out) {
		this.out = out;
	}

	public Appendable append(CharSequence csq) throws IOException {
		if ((csq == null) || (csq.length() == 0)) {
			return this;
		}
		append(csq, 0, csq.length());
		return this;
	}

	public Appendable append(CharSequence csq, int start, int end) throws IOException {
		if ((csq == null) || (start >= end) || (start >= csq.length()) || (end > csq.length())) {
			return this;
		}
		for (int index = start; index < end; index++) {
			append(csq.charAt(index));
		}
		return this;
	}
}
