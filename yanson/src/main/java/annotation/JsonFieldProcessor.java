package annotation;

import reflection.Invoker;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/25 11:29
 */
public class JsonFieldProcessor implements Processor {

	public static Map<String, Invoker> process(Invoker invoker) {
		Map<String, Invoker> invokerMap = new HashMap<>();

		JsonField jsonField = invoker.getAnnotation(JsonField.class);
		if (null != jsonField) {
			String[] aliasNames = jsonField.aliasNames();
			for (String aliasName : aliasNames) {
				invokerMap.put(aliasName, invoker);
			}
		}

		return invokerMap;
	}

}
