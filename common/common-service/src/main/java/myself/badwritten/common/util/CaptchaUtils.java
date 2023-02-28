package myself.badwritten.common.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

/**
 * 验证码帮助类
 * 
 */
public class CaptchaUtils {

	public static final int TYPE_CHAR = 1;
	public static final int TYPE_MATH = 2;

	private static Random random = new Random();
	private static Color[] backgroundColors = new Color[] { new Color(247, 254, 235), new Color(248, 248, 248), new Color(238, 246, 251), new Color(216, 242, 227), new Color(214, 229, 245), new Color(237, 214, 255), new Color(245, 216, 206) };
	private static Color[] fontColors = new Color[] { new Color(204, 61, 0), new Color(81, 131, 6), new Color(39, 102, 246), new Color(234, 116, 10), new Color(21, 224, 124), new Color(165, 36, 247) };

	/**
	 * 验证码字符
	 */
	public static class CaptchaCode {
		private String showCode;
		private String realCode;

		public String getShowCode() {
			return showCode;
		}

		public void setShowCode(String showCode) {
			this.showCode = showCode;
		}

		public String getRealCode() {
			return realCode;
		}

		public void setRealCode(String realCode) {
			this.realCode = realCode;
		}

	}

	/**
	 * 验证码图片
	 */
	public static class Captcha {

		private ByteArrayInputStream inputStream;
		private String code;
		private int length;
		private int width;
		private int height;

		public ByteArrayInputStream getInputStream() {
			return inputStream;
		}

		public String getCode() {
			return code;
		}

		public int getLength() {
			return length;
		}

		public int getWidth() {
			return width;
		}

		public int getHeight() {
			return height;
		}

		@Override
		public String toString() {
			return "Captcha [code=" + code + ", length=" + length + ", width=" + width + ", height=" + height + "]";
		}
	}

	/**
	 * 获取验证码
	 * 
	 * @param width
	 * @param height
	 * @param codeMaker
	 * @return
	 */
	public static Captcha getImage(int width, int height, CodeMaker codeMaker) {
		Captcha captcha = new Captcha();
		CaptchaCode code = codeMaker.getCode();
		captcha.code = code.getRealCode();
		captcha.width = width;
		captcha.height = height;

		Color color = backgroundColors[random.nextInt(backgroundColors.length)];

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();

		try {
			int size = height * 72 / 96;
			Font font = new Font(Font.SANS_SERIF, Font.BOLD | Font.ITALIC, size);
			g.setFont(font);

			g.setColor(color);
			g.fillRect(0, 0, width - 1, height - 1);

			color = fontColors[random.nextInt(fontColors.length)];
			g.setColor(color);
			g.drawRect(0, 0, width - 1, height - 1);

			char[] chars = code.getShowCode().toCharArray();
			for (int i = 0; i < chars.length; i++) {
				color = fontColors[random.nextInt(fontColors.length)];
				g.setColor(color);
				int x = width * 90 / 100 / chars.length * i + width * 5 / 100;
				g.drawChars(new char[] { chars[i] }, 0, 1, x, size);
			}

			g.setFont(font);
			for (int i = 0; i < 50; i++) {
				color = fontColors[random.nextInt(fontColors.length)];
				g.setColor(color);
				int x1 = random.nextInt(width);
				int x2 = random.nextInt(width);
				int y1 = random.nextInt(height);
				int y2 = random.nextInt(height);
				int s = random.nextInt(360);
				g.drawArc(x1, y1, x2, y2, s, 50);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			g.dispose();
		}

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ImageOutputStream imageOut = null;
		try {
			imageOut = ImageIO.createImageOutputStream(output);
			ImageIO.write(image, "JPEG", imageOut);
			imageOut.flush();

			byte[] b = output.toByteArray();

			captcha.inputStream = new ByteArrayInputStream(b);
			captcha.length = b.length;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (imageOut != null) {
				try {
					imageOut.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			if (output != null) {
				try {
					output.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}

		return captcha;
	}

	/**
	 * 获取验证码,随机字母
	 * 
	 * @param width 宽度
	 * @param height 高度
	 * @return
	 */
	public static Captcha getImage(int width, int height) {
		return getImage(width, height, TYPE_CHAR);
	}

	/**
	 * 获取验证码
	 * 
	 * @param width 宽度
	 * @param height 高度
	 * @param type 文字类型
	 * @return
	 * @throws Exception
	 */
	public static Captcha getImage(int width, int height, int type) {
		CodeMaker codeMaker;
		switch (type) {
			case TYPE_CHAR:
				codeMaker = new SimpleCharacterCodeMaker();
				break;

			case TYPE_MATH:
				codeMaker = new CalculationCodeMaker();
				break;

			default:
				throw new RuntimeException("没有对应的验证码生成规则");
		}

		return getImage(width, height, codeMaker);
	}

	/**
	 * 验证码字符生成器
	 */
	public static interface CodeMaker {
		public abstract CaptchaCode getCode();
	}

	/**
	 * 普通字符验证码生成器
	 */
	public static class SimpleCharacterCodeMaker implements CodeMaker {

		private static final char[] BASE = "0123456789ABCDEFGHJKLMNPQRSTUVWXYZ".toCharArray();
		private int length;

		public SimpleCharacterCodeMaker() {
			this(4);
		}

		/**
		 * 
		 * @param length 字符长度
		 */
		public SimpleCharacterCodeMaker(int length) {
			this.length = length;
		}

		@Override
		public CaptchaCode getCode() {
			String showCode;
			String realCode;

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < length; i++) {
				sb.append(BASE[random.nextInt(BASE.length)]);
			}
			showCode = sb.toString();
			realCode = showCode;

			CaptchaCode code = new CaptchaCode();
			code.setShowCode(showCode);
			code.setRealCode(realCode);
			return code;
		}
	}

	/**
	 * 十以内的加法或乘法计算验证码生成器
	 */
	public static class CalculationCodeMaker implements CodeMaker {

		public CalculationCodeMaker() {
		}

		@Override
		public CaptchaCode getCode() {
			String showCode;
			String realCode;

			boolean oper = random.nextBoolean();
			int range = oper ? 5 : 9;
			String operChar = oper ? " + " : " × ";
			int num1 = random.nextInt(range);
			int num2 = random.nextInt(range);
			int result = oper ? num1 + num2 : num1 * num2;

			realCode = String.valueOf(result);
			showCode = new StringBuilder().append(num1).append(operChar).append(num2).append(" = ? ").toString();

			CaptchaCode code = new CaptchaCode();
			code.setShowCode(showCode);
			code.setRealCode(realCode);
			return code;
		}
	}

}
