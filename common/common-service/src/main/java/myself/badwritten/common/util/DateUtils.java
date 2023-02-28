package myself.badwritten.common.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 日期帮助类
 * 
 */
public class DateUtils {

	private Calendar local;

	private DateUtils(Date date) {
		this.local = Calendar.getInstance();
		this.local.setTime(date);
	}

	/** 一天毫秒数 */
	public static final long MILLISECOND_DAY = 24 * 60 * 60 * 1000;

	/** 一小时毫秒数 */
	public static final long MILLISECOND_HOUR = 60 * 60 * 1000;

	/**
	 * 每个线程设置缓存map保证多线程同步
	 */
	private static final ThreadLocal<Map<String, SimpleDateFormat>> formats = new ThreadLocal<Map<String, SimpleDateFormat>>();

	/**
	 * 获取改变后的时间
	 * 
	 * @return
	 */
	public Date getTime() {
		return local.getTime();
	}

	/**
	 * 获取改变后的时间字符串
	 * 
	 * @param format
	 * @return
	 */
	public String toString(String format) {
		return format(getTime(), format);
	}

	/**
	 * 获取unix时间戳
	 * @return
	 */
	public long toUnix() {
		return getTime().getTime() / 1000;
	}

	/**
	 * 获取时间戳
	 * @return
	 */
	public Timestamp toTimestamp() {
		return new Timestamp(getTime().getTime());
	}

	/**
	 * 生成SimpleDateFormat
	 * 
	 * @param format
	 * @return
	 */
	public static SimpleDateFormat getDateFormat(String format) {

		Map<String, SimpleDateFormat> map = formats.get();
		if (map == null) {
			synchronized (DateUtils.class) {
				map = formats.get();
				if (map == null) {
					map = new ConcurrentHashMap<String, SimpleDateFormat>();
					formats.set(map);
				}
			}
		}

		if (map.containsKey(format)) {
			return map.get(format);
		} else {
			SimpleDateFormat df = new SimpleDateFormat(format);
			map.put(format, df);
			return df;
		}
	}

	/**
	 * 获取当前时间.
	 * 
	 * @return 当前时间
	 */
	public static DateUtils now() {
		return new DateUtils(new Date());
	}

	/**
	 * 指定时间.
	 * 
	 * @param date
	 * @return
	 */
	public static DateUtils load(Date date) {
		return date == null ? null : new DateUtils(date);
	}

	/**
	 * 指定时间.
	 * 
	 * @param date
	 * @return
	 */
	public static DateUtils load(Long date) {
		return new DateUtils(new Date(date));
	}

	/**
	 * 尝试各种方式转换日期
	 * 
	 * @param date
	 * @return
	 */
	public static DateUtils load(Object date) {
		return new DateUtils(parse(date));
	}

	/**
	 * 指定时间.
	 * 
	 * @param date
	 * @return
	 */
	public static DateUtils load(String date, String format) {
		return new DateUtils(parse(date, format));
	}

	/**
	 * 指定日期
	 * 
	 * @param year
	 * @param month
	 * @param date
	 * @return
	 */
	public static DateUtils load(int year, int month, int date) {
		DateUtils o = DateUtils.now();
		o.local.set(year, month - 1, date);
		o.local.set(Calendar.HOUR, 0);
		o.local.set(Calendar.MINUTE, 0);
		o.local.set(Calendar.SECOND, 0);
		o.local.set(Calendar.MILLISECOND, 0);
		return o;
	}

	/**
	 * 将unix时间戳转化为时间
	 * 
	 * @param unixTime
	 * @return
	 */
	public static DateUtils loadUnixTime(long unixTime) {
		return new DateUtils(new Date(unixTime * 1000));
	}

	/**
	 * 修改基础时间的指定字段获取指定时间
	 *
	 *            基础时间,为空则为当前时间
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param day
	 *            日
	 * @param hour
	 *            小时
	 * @param minute
	 *            分钟
	 * @param second
	 *            秒
	 * @param millisecond
	 *            毫秒
	 * @return
	 */
	public DateUtils set(Integer year, Integer month, Integer day, Integer hour, Integer minute, Integer second, Integer millisecond) {
		if (year != null) {
			local.set(Calendar.YEAR, year);
		}
		if (month != null) {
			local.set(Calendar.MONTH, month - 1);
		}
		if (day != null) {
			local.set(Calendar.DATE, day);
		}
		if (hour != null) {
			local.set(Calendar.HOUR_OF_DAY, hour);
		}
		if (minute != null) {
			local.set(Calendar.MINUTE, minute);
		}
		if (second != null) {
			local.set(Calendar.SECOND, second);
		}
		if (millisecond != null) {
			local.set(Calendar.MILLISECOND, millisecond);
		}
		return this;
	}

	/**
	 * 将时间部分设置为0
	 * 
	 * @return
	 */
	public DateUtils clearTime() {
		return set(null, null, null, 0, 0, 0, 0);
	}

	/**
	 * 获取给定日期对象的年
	 * 
	 * @return
	 */
	public int getYear() {
		return local.get(Calendar.YEAR);
	}

	/**
	 * 获取给定日期对象的月
	 * 
	 * @return
	 */
	public int getMonth() {
		return local.get(Calendar.MONTH) + 1;
	}

	/**
	 * 获取给定日期对象的天
	 * 
	 * @return
	 */
	public int getDay() {
		return local.get(Calendar.DATE);
	}

	/**
	 * 获取给定日期对象的星期几
	 * 
	 * @return
	 */
	public int getWeek() {
		return local.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 获取给定日期对象的小时
	 * 
	 * @return
	 */
	public int getHour() {
		return local.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 获取给定日期对象的分
	 * 
	 * @return
	 */
	public int getMinute() {
		return local.get(Calendar.MINUTE);
	}

	/**
	 * 获取给定日期对象的秒
	 * 
	 * @return
	 */
	public int getSecond() {
		return local.get(Calendar.SECOND);
	}

	/**
	 * 获取给定日期对象的毫秒
	 * 
	 * @return
	 */
	public int getMillisecond() {
		return local.get(Calendar.MILLISECOND);
	}

	/**
	 * 获取指定字段
	 * 
	 * @param field
	 *            (Calendar.*)
	 * @return
	 */
	public int get(int field) {
		return local.get(field);
	}

	/**
	 * 设置指定字段
	 * 
	 * @param field
	 *            (Calendar.*)
	 * @param value
	 * @return
	 */
	public DateUtils set(int field, int value) {
		local.set(field, value);
		return this;
	}

	/**
	 * 将某个日期增加指定年数
	 * 
	 * @param ammount
	 * @return
	 */
	public DateUtils addYear(int ammount) {
		local.add(Calendar.YEAR, ammount);
		return this;
	}

	/**
	 * 将某个日期增加指定月数
	 * 
	 * @param ammount
	 * @return
	 */
	public DateUtils addMonth(int ammount) {
		local.add(Calendar.MONTH, ammount);
		return this;
	}

	/**
	 * 将某个日期增加指定天数
	 * 
	 * @param ammount
	 * @return
	 */
	public DateUtils addDay(int ammount) {
		local.add(Calendar.DATE, ammount);
		return this;
	}

	/**
	 * 将某个日期增加指定小时
	 * 
	 * @param ammount
	 * @return
	 */
	public DateUtils addHour(int ammount) {
		local.add(Calendar.HOUR_OF_DAY, ammount);
		return this;
	}

	/**
	 * 将某个日期增加指定分钟
	 * @param ammount
	 * @return
	 */
	public DateUtils addMinute(int ammount) {
		local.add(Calendar.MINUTE, ammount);
		return this;
	}
	
	/**
	 * 将某个日期增加指定秒
	 * @param ammount
	 * @return
	 */
	public DateUtils addSecond(int ammount) {
		local.add(Calendar.SECOND, ammount);
		return this;
	}

	/**
	 * 获取月最后一天
	 * 
	 * @return
	 */
	public DateUtils monthLastDay() {
		return this.set(null, null, 1, null, null, null, null).addMonth(1).addDay(-1);
	}

	/**
	 * 返回给定的beforeDate比afterDate早的天数.
	 * 
	 * @param beforeDate
	 * @param afterDate
	 * @return
	 */
	public static int beforeDays(Date beforeDate, Date afterDate) {

		Date bd = DateUtils.load(beforeDate).set(null, null, null, 0, 0, 0, 0).getTime();
		Date ed = DateUtils.load(afterDate).set(null, null, null, 0, 0, 0, 0).getTime();

		long day = (ed.getTime() - bd.getTime()) / MILLISECOND_DAY;
		return Integer.valueOf(String.valueOf(day));
	}

	/**
	 * 返回给定的beforeDate比afterDate早的小时.
	 * 
	 * @param beforeDate
	 * @param afterDate
	 * @return
	 */
	public static int beforeHours(Date beforeDate, Date afterDate) {

		Date bd = DateUtils.load(beforeDate).set(null, null, null, 0, 0, 0, 0).getTime();
		Date ed = DateUtils.load(afterDate).set(null, null, null, 0, 0, 0, 0).getTime();

		long hour = (ed.getTime() - bd.getTime()) / MILLISECOND_HOUR;
		return Integer.valueOf(String.valueOf(hour));
	}

	/**
	 * 格式化日期
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String format(Date date, String pattern) {
		return getDateFormat(pattern).format(date);
	}

	/**
	 * 将传入的字符串转换成日期对象
	 * 
	 * @param dateStr
	 * @param pattern
	 * @return
	 */
	public static Date parse(String dateStr, String pattern) {
		try {
			return getDateFormat(pattern).parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 将对象转换为日期对象
	 * 
	 * @param obj
	 *            支持字符串(yyyy-MM-dd HH:mm:ss,yyyy-MM-dd)、long
	 * @return
	 */
	public static Date parse(Object obj) throws IllegalArgumentException {

		if (obj == null) {
			return null;
		}

		Date date = null;
		if (obj instanceof Date) {
			date = (Date) obj;

		} else if (obj instanceof String) {
			date = parse((String) obj, "yyyy-MM-dd HH:mm:ss");
			if (date == null) {
				date = parse((String) obj, "yyyy-MM-dd'T'HH:mm:ss");
			}
			if (date == null) {
				date = parse((String) obj, "yyyy/MM/dd HH:mm:ss");
			}
			if (date == null) {
				date = parse((String) obj, "yyyy-MM-dd");
			}
			if (date == null) {
				date = parse((String) obj, "yyyy/MM/dd");
			}
			if (date == null) {
				date = parse((String) obj, "HH:mm:ss");
			}
			if (date == null) {
				date = parse((String) obj, "HH:mm");
			}
			if (date == null) {
				try {
					Long millis = ConvertUtils.convert(obj, Long.class);
					date = new Date(millis);
				} catch (Exception e) {
				}
			}

		} else if (obj instanceof Number) {
			Long millis = ConvertUtils.convert(obj, Long.class);
			date = new Date(millis);

		} else if (obj instanceof java.sql.Date) {
			date = new Date(((java.sql.Date) obj).getTime());

		}
		if (date == null) {
			throw new IllegalArgumentException("Can not convert " + String.valueOf(obj) + " to Date");
		}
		return date;
	}

	/**
	 * 将unix时间戳转化为时间
	 * 
	 * @param unixTime
	 * @return
	 */
	public static Date getDateByUnixTime(long unixTime) {
		return new Date(unixTime * 1000);
	}

	/**
	 * 获取unix时间戳
	 * 
	 * @param date
	 * @return
	 */
	public static long toUnix(Date date) {
		return date.getTime() / 1000;
	}

	//获得过去的几年
	public static String getOneYear(int o){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.YEAR, -o);
		Date m = c.getTime();
		return format.format(m);
	}

}