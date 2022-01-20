package myself.badwritten.common.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class FileUtils extends org.apache.commons.io.FileUtils {

	private static Map<String, BufferedWriter> writers = new Hashtable<String, BufferedWriter>();

	/**
	 * 将字符串写入文件
	 * 
	 * @param filePath 文件路径
	 * @param data 数据
	 * @param charset 编码
	 * @param append 是否增加
	 * @param appendLine 是否添加换行符
	 * @param batch 是否批量刷新,true:需要手动调用flushFile(close)方法提交数据
	 */
	public static void writeStringToFile(String filePath, String data, String charset, boolean append, boolean appendLine, boolean batch) throws Exception {

		String s = appendLine ? data + "\n" : data;
		File file = new File(filePath);

		if (!batch) {
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			org.apache.commons.io.FileUtils.writeStringToFile(file, s, charset, append);
		} else {

			synchronized (writers) {
				BufferedWriter writer = writers.get(filePath);
				if (writer == null) {
					if (!file.getParentFile().exists()) {
						file.getParentFile().mkdirs();
					}
					writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append), charset), 102400);
					writers.put(filePath, writer);
				}
				writer.write(s);
			}

		}
	}

	/**
	 * 读取文件内容
	 * 
	 * @param file
	 * @param encoding
	 * @return
	 */
	public static String readFileToString(File file, String encoding) {
		try {
			return org.apache.commons.io.FileUtils.readFileToString(file, encoding);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 读取文件内容
	 * 
	 * @param file
	 * @param encoding
	 * @return
	 */
	public static List<String> readFileToLines(File file, String encoding) {
		try {
			return org.apache.commons.io.FileUtils.readLines(file, encoding);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 读取文件内容
	 * 
	 * @param file
	 * @return
	 */
	public static byte[] readFileToByteArray(File file) {
		try {
			return org.apache.commons.io.FileUtils.readFileToByteArray(file);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 刷新输出流
	 * 
	 * @param close
	 */
	public static void flushFile(boolean close) {
		synchronized (writers) {
			for (String key : writers.keySet()) {
				BufferedWriter writer = writers.get(key);
				try {
					writer.flush();
					if (close) {
						writer.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (close) {
				writers.clear();
			}
		}
	}

	/**
	 * 列出指定目录及其子目录下的所有文件
	 * 
	 * @param root
	 * @return
	 */
	public static List<File> listFiles(File root) {
		List<File> files = new ArrayList<File>();

		if (root.exists()) {

			if (root.isDirectory()) {
				File[] subs = root.listFiles();
				for (File file : subs) {
					files.addAll(listFiles(file));
				}
			} else {
				files.add(root);
			}

			Collections.sort(files, new Comparator<File>() {
				@Override
				public int compare(File o1, File o2) {
					return o1.getAbsolutePath().compareTo(o2.getAbsolutePath());
				}
			});

		}
		return files;
	}

	/**
	 * 转义文件名/路径
	 * 
	 * @param name
	 * @param date
	 * @return
	 */
	public static String parseName(String name, Date date) {

		String dateString = DateUtils.format(date, "yyyy-MM-dd");
		String datePathString = DateUtils.format(date, "yyyy/MM/dd");
		String dateNumString = DateUtils.format(date, "yyyyMMdd");
		String yearSring = DateUtils.format(date, "yyyy");
		String monthSring = DateUtils.format(date, "MM");
		String daySring = DateUtils.format(date, "dd");
		String hourSring = DateUtils.format(date, "HH");
		String minSring = DateUtils.format(date, "mm");
		String secSring = DateUtils.format(date, "ss");

		Map<String, String> map = new HashMap<String, String>();
		map.put("date", dateString);
		map.put("datePath", datePathString);
		map.put("dateNum", dateNumString);
		map.put("year", yearSring);
		map.put("month", monthSring);
		map.put("day", daySring);
		map.put("hour", hourSring);
		map.put("min", minSring);
		map.put("sec", secSring);

		name = StringUtils.replaceParameter(name, map);

		return name;
	}

	public static final Map<String, String> MIMES = new Hashtable<>();

	/**
	 * 获取文件ContentType
	 * 
	 * @param file
	 * @return
	 */
	public static String getContentType(File file) {
		if (MIMES.size() == 0) {
			synchronized (MIMES) {
				if (MIMES.size() == 0) {
					String mm = "323:text/h323;acx:application/internet-property-stream;ai:application/postscript;aif:audio/x-aiff;aifc:audio/x-aiff;aiff:audio/x-aiff;asf:video/x-ms-asf;asr:video/x-ms-asf;asx:video/x-ms-asf;au:audio/basic;avi:video/x-msvideo;axs:application/olescript;bas:text/plain;bcpio:application/x-bcpio;bin:application/octet-stream;bmp:image/bmp;c:text/plain;cat:application/vnd.ms-pkiseccat;cdf:application/x-cdf;cer:application/x-x509-ca-cert;class:application/octet-stream;clp:application/x-msclip;cmx:image/x-cmx;cod:image/cis-cod;cpio:application/x-cpio;crd:application/x-mscardfile;crl:application/pkix-crl;crt:application/x-x509-ca-cert;csh:application/x-csh;css:text/css;dcr:application/x-director;der:application/x-x509-ca-cert;dir:application/x-director;dll:application/x-msdownload;dms:application/octet-stream;doc:application/msword;dot:application/msword;dvi:application/x-dvi;dxr:application/x-director;eps:application/postscript;etx:text/x-setext;evy:application/envoy;exe:application/octet-stream;fif:application/fractals;flr:x-world/x-vrml;gif:image/gif;gtar:application/x-gtar;gz:application/x-gzip;h:text/plain;hdf:application/x-hdf;hlp:application/winhlp;hqx:application/mac-binhex40;hta:application/hta;htc:text/x-component;htm:text/html;html:text/html;htt:text/webviewhtml;ico:image/x-icon;ief:image/ief;iii:application/x-iphone;ins:application/x-internet-signup;isp:application/x-internet-signup;jfif:image/pipeg;jpe:image/jpeg;jpeg:image/jpeg;jpg:image/jpeg;js:application/x-javascript;latex:application/x-latex;lha:application/octet-stream;lsf:video/x-la-asf;lsx:video/x-la-asf;lzh:application/octet-stream;m13:application/x-msmediaview;m14:application/x-msmediaview;m3u:audio/x-mpegurl;man:application/x-troff-man;mdb:application/x-msaccess;me:application/x-troff-me;mht:message/rfc822;mhtml:message/rfc822;mid:audio/mid;mny:application/x-msmoney;mov:video/quicktime;movie:video/x-sgi-movie;mp2:video/mpeg;mp3:audio/mpeg;mpa:video/mpeg;mpe:video/mpeg;mpeg:video/mpeg;mpg:video/mpeg;mpp:application/vnd.ms-project;mpv2:video/mpeg;ms:application/x-troff-ms;mvb:application/x-msmediaview;nws:message/rfc822;oda:application/oda;p10:application/pkcs10;p12:application/x-pkcs12;p7b:application/x-pkcs7-certificates;p7c:application/x-pkcs7-mime;p7m:application/x-pkcs7-mime;p7r:application/x-pkcs7-certreqresp;p7s:application/x-pkcs7-signature;pbm:image/x-portable-bitmap;pdf:application/pdf;pfx:application/x-pkcs12;pgm:image/x-portable-graymap;pko:application/ynd.ms-pkipko;pma:application/x-perfmon;pmc:application/x-perfmon;pml:application/x-perfmon;pmr:application/x-perfmon;pmw:application/x-perfmon;pnm:image/x-portable-anymap;pot,:application/vnd.ms-powerpoint;ppm:image/x-portable-pixmap;pps:application/vnd.ms-powerpoint;ppt:application/vnd.ms-powerpoint;prf:application/pics-rules;ps:application/postscript;pub:application/x-mspublisher;qt:video/quicktime;ra:audio/x-pn-realaudio;ram:audio/x-pn-realaudio;ras:image/x-cmu-raster;rgb:image/x-rgb;rmi:audio/mid;roff:application/x-troff;rtf:application/rtf;rtx:text/richtext;scd:application/x-msschedule;sct:text/scriptlet;setpay:application/set-payment-initiation;setreg:application/set-registration-initiation;sh:application/x-sh;shar:application/x-shar;sit:application/x-stuffit;snd:audio/basic;spc:application/x-pkcs7-certificates;spl:application/futuresplash;src:application/x-wais-source;sst:application/vnd.ms-pkicertstore;stl:application/vnd.ms-pkistl;stm:text/html;svg:image/svg+xml;sv4cpio:application/x-sv4cpio;sv4crc:application/x-sv4crc;swf:application/x-shockwave-flash;t:application/x-troff;tar:application/x-tar;tcl:application/x-tcl;tex:application/x-tex;texi:application/x-texinfo;texinfo:application/x-texinfo;tgz:application/x-compressed;tif:image/tiff;tiff:image/tiff;tr:application/x-troff;trm:application/x-msterminal;tsv:text/tab-separated-values;txt:text/plain;uls:text/iuls;ustar:application/x-ustar;vcf:text/x-vcard;vrml:x-world/x-vrml;wav:audio/x-wav;wcm:application/vnd.ms-works;wdb:application/vnd.ms-works;wks:application/vnd.ms-works;wmf:application/x-msmetafile;wps:application/vnd.ms-works;wri:application/x-mswrite;wrl:x-world/x-vrml;wrz:x-world/x-vrml;xaf:x-world/x-vrml;xbm:image/x-xbitmap;xla:application/vnd.ms-excel;xlc:application/vnd.ms-excel;xlm:application/vnd.ms-excel;xls:application/vnd.ms-excel;xlt:application/vnd.ms-excel;xlw:application/vnd.ms-excel;xof:x-world/x-vrml;xpm:image/x-xpixmap;xwd:image/x-xwindowdump;z:application/x-compress;zip:application/zip";
					String[] mms = mm.split(";");
					Map<String, String> map = new Hashtable<>();
					for (String mime : mms) {
						String[] ss = StringUtils.splitFirst(mime, ":");
						map.put(ss[0], ss[1]);
					}
					MIMES.putAll(map);
				}
			}
		}

		String type = StringUtils.getFileExtension(file.getName()).toLowerCase();
		if (MIMES.containsKey(type)) {
			return MIMES.get(type);
		} else {
			return "application/octet-stream";
		}
	}

}
