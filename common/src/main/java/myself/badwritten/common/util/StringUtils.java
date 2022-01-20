package myself.badwritten.common.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

	/**
	 * 是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(CharSequence str) {
		return (str == null || "".equals(str));
	}

	/**
	 * 是否不为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(CharSequence str) {
		return !isEmpty(str);
	}

	/**
	 * 是否为空白字符
	 * 
	 * @param str
	 * @return
	 */
	public static boolean hasText(String str) {
		return str != null && !"".equals(str.trim());
	}

	/**
	 * 去除所有空白字符
	 * 
	 * @param str
	 * @return
	 */
	public static String trimAllWhitespace(String str) {
		if (isEmpty(str)) {
			return str;
		}
		StringBuilder sb = new StringBuilder(str);
		int index = 0;
		while (sb.length() > index) {
			if (Character.isWhitespace(sb.charAt(index))) {
				sb.deleteCharAt(index);
			} else {
				index++;
			}
		}
		return sb.toString();
	}

	/**
	 * 去除开头空白字符
	 * 
	 * @param str
	 * @return
	 */
	public static String trimLeadingWhitespace(String str) {
		if (isEmpty(str)) {
			return str;
		}
		StringBuilder sb = new StringBuilder(str);
		while (sb.length() > 0 && Character.isWhitespace(sb.charAt(0))) {
			sb.deleteCharAt(0);
		}
		return sb.toString();
	}

	/**
	 * 去除结尾空白字符
	 * 
	 * @param str
	 * @return
	 */
	public static String trimTrailingWhitespace(String str) {
		if (isEmpty(str)) {
			return str;
		}
		StringBuilder sb = new StringBuilder(str);
		while (sb.length() > 0 && Character.isWhitespace(sb.charAt(sb.length() - 1))) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	/**
	 * 是否以指定字符串开头
	 * 
	 * @param sb
	 * @param prefix
	 * @return
	 */
	public static boolean startsWith(StringBuilder sb, String prefix) {
		if (sb == null || prefix == null) {
			return false;
		}
		if (sb.length() < prefix.length()) {
			return false;
		}
		if (prefix.length() == 0) {
			return true;
		}
		String lcStr = sb.substring(0, prefix.length());
		String lcPrefix = prefix;
		return lcStr.equals(lcPrefix);
	}

	/**
	 * 是否以指定字符串开头,忽略大小写
	 * 
	 * @param sb
	 * @param prefix
	 * @return
	 */
	public static boolean startsWithIgnoreCase(StringBuilder sb, String prefix) {
		if (sb == null || prefix == null) {
			return false;
		}
		if (sb.length() < prefix.length()) {
			return false;
		}
		if (prefix.length() == 0) {
			return true;
		}
		String lcStr = sb.substring(0, prefix.length()).toLowerCase();
		String lcPrefix = prefix.toLowerCase();
		return lcStr.equals(lcPrefix);
	}

	/**
	 * 是否以指定字符串开头,忽略大小写
	 * 
	 * @param str
	 * @param prefix
	 * @return
	 */
	public static boolean startsWithIgnoreCase(String str, String prefix) {
		if (str == null || prefix == null) {
			return false;
		}
		if (str.startsWith(prefix)) {
			return true;
		}
		if (str.length() < prefix.length()) {
			return false;
		}
		if (prefix.length() == 0) {
			return true;
		}
		String lcStr = str.substring(0, prefix.length()).toLowerCase();
		String lcPrefix = prefix.toLowerCase();
		return lcStr.equals(lcPrefix);
	}

	/**
	 * 是否以指定字符串结尾,忽略大小写
	 * 
	 * @param str
	 * @return
	 */
	public static boolean endsWithIgnoreCase(String str, String suffix) {
		if (str == null || suffix == null) {
			return false;
		}
		if (str.endsWith(suffix)) {
			return true;
		}
		if (str.length() < suffix.length()) {
			return false;
		}
		if (suffix.length() == 0) {
			return true;
		}
		String lcStr = str.substring(str.length() - suffix.length()).toLowerCase();
		String lcSuffix = suffix.toLowerCase();
		return lcStr.equals(lcSuffix);
	}

	/**
	 * 是否以指定字符串结尾
	 * 
	 * @param sb
	 * @return
	 */
	public static boolean endsWith(StringBuilder sb, String suffix) {
		if (sb == null || suffix == null) {
			return false;
		}
		if (sb.length() < suffix.length()) {
			return false;
		}
		if (suffix.length() == 0) {
			return true;
		}
		String lcStr = sb.substring(sb.length() - suffix.length());
		String lcSuffix = suffix;
		return lcStr.equals(lcSuffix);
	}

	/**
	 * 是否以指定字符串结尾,忽略大小写
	 * 
	 * @param sb
	 * @return
	 */
	public static boolean endsWithIgnoreCase(StringBuilder sb, String suffix) {
		if (sb == null || suffix == null) {
			return false;
		}
		if (sb.length() < suffix.length()) {
			return false;
		}
		if (suffix.length() == 0) {
			return true;
		}
		String lcStr = sb.substring(sb.length() - suffix.length()).toLowerCase();
		String lcSuffix = suffix.toLowerCase();
		return lcStr.equals(lcSuffix);
	}

	/**
	 * 判断字子符串是否在指定位置
	 * 
	 * @param str
	 *            源字符串
	 * @param substring
	 *            子字符串
	 * @param index
	 *            位置
	 * @return
	 */
	public static boolean isIndexOf(CharSequence str, CharSequence substring, int index) {
		for (int j = 0; j < substring.length(); j++) {
			int i = index + j;
			if (i >= str.length() || str.charAt(i) != substring.charAt(j)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 统计子字符串出现的次数
	 * 
	 * @param str
	 * @param sub
	 * @return
	 */
	public static int count(String str, String sub) {
		if (str == null || sub == null || str.length() == 0 || sub.length() == 0) {
			return 0;
		}
		int count = 0;
		int pos = 0;
		int idx;
		while ((idx = str.indexOf(sub, pos)) != -1) {
			++count;
			pos = idx + sub.length();
		}
		return count;
	}

	/**
	 * 添加单引号
	 * 
	 * @param str
	 * @return
	 */
	public static String quote(String str) {
		return (str != null ? "'" + str + "'" : null);
	}

	/**
	 * 如果是String类型的就添加单引号
	 * 
	 * @param obj
	 * @return
	 */
	public static Object quoteIfString(Object obj) {
		return (obj instanceof String ? quote((String) obj) : obj);
	}

	/**
	 * 取得字符串后缀
	 * 
	 * @param s
	 * @param separator
	 * @return
	 */
	public static String suffix(String s, String separator) {

		if (s.length() == 0 && separator.length() == 0) {
			return "";
		}

		int index = s.lastIndexOf(separator);
		if (index < 0) {
			return null;
		} else {
			return s.substring(index + 1);
		}
	}

	/**
	 * 首字母大写
	 * 
	 * @param str
	 * @return
	 */
	public static String capitalize(String str) {
		return changeFirstCharacterCase(str, true);
	}

	/**
	 * 首字母小写
	 * 
	 * @param str
	 * @return
	 */
	public static String uncapitalize(String str) {
		return changeFirstCharacterCase(str, false);
	}

	private static String changeFirstCharacterCase(String str, boolean capitalize) {
		if (str == null || str.length() == 0) {
			return str;
		}
		StringBuilder sb = new StringBuilder(str.length());
		if (capitalize) {
			sb.append(Character.toUpperCase(str.charAt(0)));
		} else {
			sb.append(Character.toLowerCase(str.charAt(0)));
		}
		sb.append(str.substring(1));
		return sb.toString();
	}

	/**
	 * 格式化路径
	 * 
	 * @param path
	 * @param folderSeparator
	 * @return
	 */
	public static String formatPath(String path, String folderSeparator) {
		if (path == null || !hasText(folderSeparator)) {
			return null;
		}

		folderSeparator = folderSeparator.replace("\\", "\\\\");
		path = path.replace("\\", "/");
		int index = path.indexOf("://");
		if (index < path.indexOf("/")) {
			StringBuilder sb = new StringBuilder(path.substring(0, index + 3));
			sb.append(path.substring(sb.length()).replaceAll("/+", folderSeparator));
			path = sb.toString();
		} else {
			path = path.replaceAll("/+", folderSeparator);
		}
		return path;
	}

	/**
	 * 将路径转化为WEB路径
	 * 
	 * @param path
	 * @return
	 */
	public static String formatWebPath(String path) {
		return formatPath(path, "/");
	}

	/**
	 * 通过路径获取文件名
	 * 
	 * @param path
	 * @return
	 */
	public static String getFileName(String path) {
		if (path == null) {
			return null;
		}
		int separatorIndex = path.replace("\\", "/").lastIndexOf("/");
		return (separatorIndex != -1 ? path.substring(separatorIndex + 1) : path);
	}

	/**
	 * 通过路径获取文件扩展名
	 * 
	 * @param path
	 * @return
	 */
	public static String getFileExtension(String path) {
		if (path == null) {
			return null;
		}
		path = formatPath(path, File.separator);
		int extIndex = path.lastIndexOf(".");
		if (extIndex == -1) {
			return "";
		}
		int folderIndex = path.lastIndexOf(File.separator);
		if (folderIndex > extIndex) {
			return "";
		}
		return path.substring(extIndex + 1);
	}

	/**
	 * 去除文件扩展名
	 * 
	 * @param path
	 *            路径或文件名
	 * @return
	 */
	public static String stripFileExtension(String path) {
		if (path == null) {
			return null;
		}
		path = formatPath(path, "/");
		int extIndex = path.lastIndexOf(".");
		if (extIndex == -1) {
			return path;
		}
		int folderIndex = path.lastIndexOf("/");
		if (folderIndex > extIndex) {
			return path;
		}
		return path.substring(0, extIndex);
	}

	/**
	 * 在给定的路径上添加相对路径
	 * 
	 * @param path
	 *            原始路径
	 * @param relativePath
	 *            相对路径
	 * @return
	 */
	public static String applyRelativePath(String path, String relativePath) {
		path = formatPath(path, "/");
		relativePath = formatPath(relativePath, "/");
		int separatorIndex = path.lastIndexOf("/");
		if (separatorIndex != -1) {
			String newPath = path.substring(0, separatorIndex);
			if (!relativePath.startsWith("/")) {
				newPath += "/";
			}
			return newPath + relativePath;
		} else {
			return relativePath;
		}
	}

	/**
	 * 以指定字符分割字符串,只分割首次出现位置
	 * 
	 * @param str
	 *            源字符串
	 * @param delimiter
	 *            分隔符
	 * @return
	 */
	public static String[] splitFirst(String str, String delimiter) {
		if (isEmpty(str) || isEmpty(delimiter)) {
			return null;
		}
		int offset = str.indexOf(delimiter);
		if (offset < 0) {
			return null;
		}
		String beforeDelimiter = str.substring(0, offset);
		String afterDelimiter = str.substring(offset + delimiter.length());
		return new String[] { beforeDelimiter, afterDelimiter };
	}

	/**
	 * 分割字符串
	 * 
	 * @param str
	 *            源字符串
	 * @param delimiter
	 *            分隔符
	 * @param trimTokens
	 *            去除每个元素空白字符
	 * @param ignoreEmptyTokens
	 *            去除空白元素
	 * @return
	 */
	public static String[] split(String str, String delimiter, boolean trimTokens, boolean ignoreEmptyTokens) {

		if (str == null) {
			return new String[0];
		} else if (isEmpty(str)) {
			return new String[] { str };
		}

		String[] ss = null;
		if (ignoreEmptyTokens) {
			ss = str.split(delimiter);
		} else {
			ss = str.split(delimiter, -1);
		}

		List<String> tokens = new ArrayList<String>(ss.length + 1);
		for (String s : ss) {
			if (trimTokens) {
				s = s.trim();
			}
			if (!ignoreEmptyTokens || hasText(s)) {
				tokens.add(s);
			}
		}
		if (str.endsWith(delimiter) && !ignoreEmptyTokens) {
			tokens.add("");
		}

		return tokens.toArray(new String[tokens.size()]);
	}

	/**
	 * 集合转化为字符串
	 * 
	 * @param collection
	 *            集合
	 * @param delimiter
	 *            分隔符
	 * @return
	 */
	public static String toString(Collection<?> collection, String delimiter) {
		if (collection == null || collection.size() == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		Iterator<?> it = collection.iterator();
		while (it.hasNext()) {
			Object o = it.next();
			if (isNotEmpty(sb.toString()) && !endsWith(sb, delimiter)) {
				sb.append(delimiter);
			}
			if (o != null) {
				sb.append(o);
			}
		}
		return sb.toString();
	}

	/**
	 * 数组转字符串
	 * 
	 * @param array
	 *            数组
	 * @param delimiter
	 *            分隔符
	 * @return
	 */
	public static String toString(Object[] array, String delimiter) {
		if (array == null || array.length == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (Object o : array) {
			if (!endsWith(sb, delimiter)) {
				sb.append(delimiter);
			}
			if (o != null) {
				sb.append(o);
			}
		}
		return sb.toString();
	}

	/**
	 * 字符数组转字符串
	 * 
	 * @param array
	 * @param delimiter
	 * @return
	 */
	public static String toString(char[] array, String delimiter) {
		if (array == null || array.length == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (char c : array) {
			if (!endsWith(sb, delimiter)) {
				sb.append(delimiter);
			}
			sb.append(c);
		}
		return sb.toString();
	}

	/**
	 * int数组转字符串
	 * 
	 * @param array
	 * @param delimiter
	 * @return
	 */
	public static String toString(int[] array, String delimiter) {
		if (array == null || array.length == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (int c : array) {
			if (!endsWith(sb, delimiter)) {
				sb.append(delimiter);
			}
			sb.append(c);
		}
		return sb.toString();
	}

	/**
	 * long数组转字符串
	 * 
	 * @param array
	 * @param delimiter
	 * @return
	 */
	public static String toString(long[] array, String delimiter) {
		if (array == null || array.length == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (long c : array) {
			if (!endsWith(sb, delimiter)) {
				sb.append(delimiter);
			}
			sb.append(c);
		}
		return sb.toString();
	}

	/**
	 * double数组转字符串
	 * 
	 * @param array
	 * @param delimiter
	 * @return
	 */
	public static String toString(double[] array, String delimiter) {
		if (array == null || array.length == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (double c : array) {
			if (!endsWith(sb, delimiter)) {
				sb.append(delimiter);
			}
			sb.append(c);
		}
		return sb.toString();
	}

	/**
	 * float数组转字符串
	 * 
	 * @param array
	 * @param delimiter
	 * @return
	 */
	public static String toString(float[] array, String delimiter) {
		if (array == null || array.length == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (float c : array) {
			if (!endsWith(sb, delimiter)) {
				sb.append(delimiter);
			}
			sb.append(c);
		}
		return sb.toString();
	}

	/**
	 * boolean数组转字符串
	 * 
	 * @param array
	 * @param delimiter
	 * @return
	 */
	public static String toString(boolean[] array, String delimiter) {
		if (array == null || array.length == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (boolean c : array) {
			if (!endsWith(sb, delimiter)) {
				sb.append(delimiter);
			}
			sb.append(c);
		}
		return sb.toString();
	}

	/**
	 * 格式化数字
	 * 
	 * @param number
	 * @param format
	 *            (#,#.00,000.00)
	 * @return
	 */
	public static String toString(Number number, String format) {
		DecimalFormat df = new DecimalFormat(format);
		return df.format(number);
	}

	/**
	 * 对象转字符串(为null则返回空字符串)
	 * 
	 * @param o
	 * @return
	 */
	public static String parseEmpty(Object o) {
		if (o == null) {
			return "";
		} else {
			return o.toString();
		}
	}

	/**
	 * 对象转字符串(为null则返回null)
	 * 
	 * @param o
	 * @return
	 */
	public static String parseNull(Object o) {
		if (o == null) {
			return null;
		} else {
			return o.toString();
		}
	}

	/**
	 * URL encode
	 * 
	 * @param url
	 *            URL
	 * @param encoding
	 *            编码
	 * @return 编码结果
	 */
	public static String urlEncode(String url, String encoding) {
		try {
			return URLEncoder.encode(url, encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * URL decode
	 * 
	 * @param url
	 *            URL
	 * @param encoding
	 *            编码
	 * @return 解码结果
	 */
	public static String urlDecode(String url, String encoding) {
		try {
			return URLDecoder.decode(url, encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 字符串的第一个字符
	 * 
	 * @param string
	 *            字符串
	 * @return 第一个字符
	 */
	public static String first(String string) {
		return left(string, 1);
	}

	/**
	 * 获取开头给定数目字符
	 * 
	 * @param string
	 *            字符串
	 * @param count
	 *            多少个字符
	 * @return 指定数目的字符
	 */
	public static String left(String string, int count) {
		if (string.length() < count) {
			return string;
		} else {
			return string.substring(0, count);
		}
	}

	/**
	 * 获取结尾给定数目字符
	 * 
	 * @param string
	 *            字符串
	 * @param count
	 *            多少个字符
	 * @return 指定数目的字符
	 */
	public static String right(String string, int count) {
		return string.substring(string.length() - count);
	}

	/**
	 * 结束字符
	 * 
	 * @param string
	 *            字符串
	 * @return 最后一个字符
	 */
	public static String last(String string) {
		return right(string, 1);
	}

	/**
	 * 去除开头字符
	 * 
	 * @param string
	 * @param count
	 * @return
	 */
	public static String removeFirst(String string, int count) {
		if (string == null) {
			return null;
		}
		if (string.length() <= count) {
			return "";
		} else {
			return string.substring(count);
		}
	}

	/**
	 * 去除开头一个字符
	 * 
	 * @param string
	 * @return
	 */
	public static String removeFirst(String string) {
		return removeFirst(string, 1);
	}

	/**
	 * 去除结尾字符
	 * 
	 * @param string
	 *            字符串
	 * @param count
	 *            去除的数目
	 * @return 去除结尾之后的字符串
	 */
	public static String removeLast(String string, int count) {
		if (string == null) {
			return null;
		}
		if (string.length() <= count) {
			return "";
		} else {
			return string.substring(0, string.length() - count);
		}
	}

	/**
	 * 去除结尾一个字符
	 * 
	 * @param string
	 *            字符串
	 * @return 去除结尾之后的字符串
	 */
	public static String removeLast(String string) {
		return removeLast(string, 1);
	}

	/**
	 * 去除每个元素的首尾空白字符
	 * 
	 * @param array
	 * @return
	 */
	public static String[] trim(String[] array) {
		if (array == null) {
			return new String[0];
		}
		String[] result = new String[array.length];
		for (int i = 0; i < array.length; i++) {
			String element = array[i];
			result[i] = (element != null ? element.trim() : null);
		}
		return result;
	}

	/**
	 * 去除首尾
	 * 
	 * @param string
	 *            字符串
	 * @param remove
	 *            首尾要去除的字符串
	 * @return 去除首尾之后的字符串
	 */
	public static String trim(String string, String remove) {
		if (string != null && string.length() > 0) {
			boolean b = string.indexOf(remove) == 0;
			if (b) {
				string = string.substring(remove.length());
			}
			boolean e = string.length() >= remove.length() && string.lastIndexOf(remove) == string.length() - remove.length();
			if (e) {
				string = string.substring(0, string.length() - remove.length());
			}
			if (b || e) {
				string = trim(string, remove);
			}
		}
		return string;
	}

	/**
	 * 字节数组转十六进制字符串
	 * 
	 * @param b
	 *            字节数组
	 * @return 十六进制字符串
	 */
	public static String byte2hex(byte[] b) {
		String hex = "";
		String stmp = "";
		for (int i = 0; i < b.length; i++) {
			stmp = Integer.toHexString(b[i] & 0xff);
			if (stmp.length() == 1) {
				hex = hex + "0" + stmp;
			} else {
				hex = hex + stmp;
			}
		}
		return hex.toLowerCase();
	}

	/**
	 * 十六进制字符串转字节数组
	 * 
	 * @param string
	 *            十六进制字符串
	 * @return 字节数组
	 */
	public static byte[] hex2byte(String string) {
		byte[] b = string.getBytes();
		byte[] bytes = new byte[b.length / 2];
		for (int i = 0; i < b.length; i += 2) {
			String item = new String(b, i, 2);
			bytes[i / 2] = (byte) Integer.parseInt(item, 16);
		}
		return bytes;
	}

	/**
	 * 判断以指定分隔的字符串是否包含指定的子字符串
	 * 
	 * @param string
	 *            原字符串
	 * @param s
	 *            包含字符串
	 * @param split
	 *            分隔符
	 * @return 包含返回true,否则返回false
	 */
	public static boolean contains(String string, String s, String split) {
		if (string == null || s == null) {
			return false;
		}
		string = split + trim(string, split) + split;
		s = split + trim(s, split) + split;
		return string.contains(s);
	}

	/**
	 * 转为unicode字符串
	 * 
	 * @param string
	 *            原字符串
	 * @return 转换后字符串
	 */
	public static String toUnicode(String string) {
		StringBuilder sb = new StringBuilder();
		char[] cs = string.toCharArray();
		for (char c : cs) {
			String tmp = Integer.toHexString(c);
			sb.append(tmp.length() == 4 ? "\\u" + tmp : c);
		}
		return sb.toString();
	}

	/**
	 * 按指定编码返回字节数组
	 * 
	 * @param str
	 * @param charsetName
	 * @return
	 */
	public static byte[] getBytes(String str, String charsetName) {
		try {
			return str.getBytes(charsetName);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("the named charset is not supported : " + charsetName);
		}
	}

	/**
	 * 按指定编码构造字符串
	 * 
	 * @param bytes
	 * @param charsetName
	 * @return
	 */
	public static String newString(byte[] bytes, String charsetName) {
		try {
			return new String(bytes, charsetName);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("the named charset is not supported : " + charsetName);
		}
	}

	/**
	 * 重新解码字符串
	 * 
	 * @param str
	 * @param deCodeCharsetName
	 * @param enCodeCharsetName
	 * @return
	 */
	public static String reCode(String str, String deCodeCharsetName, String enCodeCharsetName) {
		return newString(getBytes(str, deCodeCharsetName), enCodeCharsetName);
	}

	/**
	 * 替换字符串中的参数
	 * 
	 * @param str
	 * @param parameters
	 * @return
	 */
	public static String replaceParameter(String str, Map<String, ?> parameters) {

		if (parameters == null) {
			return str.replaceAll("\\$\\{.*?\\}", "");
		}

		for (String key : parameters.keySet()) {
			String value = parseEmpty(parameters.get(key));
			str = str.replaceAll("[$][{]" + key + "[}]", value == null ? "" : value);
		}

		Pattern p = Pattern.compile("\\$\\{(.*?)\\}", Pattern.MULTILINE);
		Matcher m = p.matcher(str);

		while (m.find()) {

			String el = m.group();
			String exp = m.group(1).trim();
			Object value = ClassUtils.invokeExpression(exp, parameters);

			str = str.replace(el, value == null ? "" : value.toString());
			m = p.matcher(str);
		}

		return str;
	}

	/**
	 * 替换字符串中的参数
	 * 
	 * @param str
	 * @param key
	 * @param value
	 * @return
	 */
	public static String replaceParameter(String str, String key, String value) {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(key, value);
		return replaceParameter(str, parameters);
	}

	/**
	 * 替换字符串中的参数
	 * 
	 * @param str
	 * @param keyValues
	 * @return
	 */
	public static String replaceParameter(String str, String... keyValues) {
		if (keyValues == null) {
			return str;
		}

		if (keyValues.length % 2 != 0) {
			throw new RuntimeException("The parameter (keyValues) length must be an even number");
		}

		Map<String, String> parameters = new HashMap<String, String>();
		for (int i = 0; i < keyValues.length; i += 2) {
			parameters.put(keyValues[i], String.valueOf(keyValues[i + 1]));
		}
		return replaceParameter(str, parameters);
	}

	/**
	 * 删除字符串
	 * 
	 * @param str
	 * @param array
	 * @return
	 */
	public static String delete(String str, String[] array) {
		if (isEmpty(str) || array == null || array.length == 0) {
			return str;
		}

		boolean replaced = true;

		while (replaced) {
			replaced = false;
			for (String r : array) {
				if (r != null && str.contains(r)) {
					replaced = true;
					str = str.replace(r, "");
				}
			}
		}
		return str;
	}

	/**
	 * 删除字符串,正则表达式
	 * 
	 * @param str
	 * @param array
	 * @return
	 */
	public static String delete(String str, char[] array) {
		if (isEmpty(str) || array == null || array.length == 0) {
			return str;
		}
		String regex = "[" + toString(array, "") + "]";
		return str.replaceAll(regex, "");
	}

	/**
	 * 删除指定元素以外的字符
	 * 
	 * @param str
	 * @param array
	 * @return
	 */
	public static String deleteWithOut(String str, char[] array) {
		if (isEmpty(str) || array == null || array.length == 0) {
			return str;
		}
		String regex = "[^" + toString(array, "") + "]";
		return str.replaceAll(regex, "");
	}

	/**
	 * 清空StringBuilder
	 * 
	 * @param sb
	 * @return
	 */
	public static StringBuilder clear(StringBuilder sb) {
		return sb.delete(0, sb.length());
	}

	/**
	 * 清空StringBuffer
	 * 
	 * @param sb
	 * @return
	 */
	public static StringBuffer clear(StringBuffer sb) {
		return sb.delete(0, sb.length());
	}

	/**
	 * 将字符串转换为java格式的,string_name -> stringName
	 * 
	 * @param sqlName
	 * @return
	 */
	public static String toJavaName(String sqlName) {
		String[] ss = StringUtils.split(sqlName.toLowerCase(), "_", true, true);
		StringBuilder sb = new StringBuilder();
		for (String s : ss) {
			sb.append(StringUtils.capitalize(s));
		}
		return StringUtils.uncapitalize(sb.toString());
	}

	/**
	 * 将字符串转换为SQL格式的,stringName -> string_name
	 * 
	 * @param javaName
	 * @return
	 */
	public static String toSqlName(String javaName) {
		StringBuilder sb = new StringBuilder();
		for (char c : javaName.toCharArray()) {
			if (c >= 'a' && c <= 'z') {
				sb.append((char) (((int) c) - 32));
			} else if (c >= 'A' && c <= 'Z') {
				if (sb.length() > 0) {
					sb.append("_");
				}
				sb.append(c);
			} else if (c >= '0' && c <= '9') {
				char last = sb.charAt(sb.length() - 1);
				if (last > '9') {
					sb.append("_");
				}
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * 尝试以utf-8或者以gbk解码参数
	 * 
	 * @param s
	 * @return
	 */
	public static String encode(String s) {
		if (s == null) {
			return s;
		}
		try {
			boolean b = s.matches("^(?:[\\x00-\\x7f]|[\\xfc-\\xff][\\x80-\\xbf]{5}|[\\xf8-\\xfb][\\x80-\\xbf]{4}|[\\xf0-\\xf7][\\x80-\\xbf]{3}|[\\xe0-\\xef][\\x80-\\xbf]{2}|[\\xc0-\\xdf][\\x80-\\xbf])+$");
			byte[] bs = s.getBytes("iso-8859-1");
			return new String(bs, b ? "utf-8" : "gbk");
		} catch (Exception e) {
			return s;
		}
	}

}
