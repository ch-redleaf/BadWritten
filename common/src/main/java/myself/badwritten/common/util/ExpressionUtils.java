package myself.badwritten.common.util;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class ExpressionUtils {

	private static ExpressionParser parser = new SpelExpressionParser();
	private static Pattern elPattern = Pattern.compile("\\$\\{\\s*([a-zA-Z0-9_\\-\\.\\[\\]#\\(\\)]+)\\s*\\}");
	private static Log logger = LogFactory.getLog(ExpressionUtils.class);

	public static EvaluationContext parseContext(Map<String, Object> context) {
		EvaluationContext ctx = new StandardEvaluationContext();
		for (String key : context.keySet()) {
			ctx.setVariable(key, context.get(key));
		}
		return ctx;
	}

	public static Object getValue(String el, Map<String, Object> context) {
		EvaluationContext ctx = parseContext(context);

		if (!el.startsWith("#")) {
			el = "#" + el;
		}

		return parser.parseExpression(el).getValue(ctx);
	}

	public static String parseExpression(String source, Map<String, Object> context) {
		return parseExpression(source, context, false);
	}

	public static String parseExpression(String source, Map<String, Object> context, boolean urlEncoderValue) {
		EvaluationContext ctx = parseContext(context);
		return parseExpression(source, ctx, urlEncoderValue);
	}

	public static String parseExpression(String source, EvaluationContext context) {
		return parseExpression(source, context, false);
	}

	public static String parseExpression(String source, EvaluationContext context, boolean urlEncoderValue) {
		while (true) {
			Matcher m = elPattern.matcher(source);
			if (!m.find()) {
				break;
			} else {
				String el = m.group(1);
				
				if (!el.startsWith("#")) {
					el = "#" + el;
				}
				
				try {
					String elValue = parser.parseExpression(el).getValue(context, String.class);
					if (elValue == null) {
						elValue = "";
					}
					if (urlEncoderValue) {
						elValue = StringUtils.urlEncode(elValue, "utf-8");
					}
					source = source.replace(m.group(), elValue);
				} catch (RuntimeException e) {
					logger.error("EL表达式执行异常 - " + e.getMessage(), e);
					throw e;
				}
			}
		}
		return source;
	}

}
