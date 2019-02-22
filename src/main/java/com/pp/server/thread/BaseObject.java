//package com.pp.server.thread;
//
//import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.function.Predicate;
//
//import com.netflix.infix.lang.infix.antlr.EventFilterParser.boolean_expr_return;
//
//public class BaseObject {
//
//
//
//	public static void main(String[] args) {
//		List<Map<String, Object>> resultList = new ArrayList<>();
//
//		Map<String, Object> tmpMap = new HashMap<>();
//		tmpMap.put("tvalue", "123");
//		tmpMap.put("xvalue", "345");
//		tmpMap.put("avalue", "456");
//		resultList.add(tmpMap);
//		resultList.forEach(k->{
//			String result = BaseObject.filter(BaseObject.getUnUsePredicate(), k);
//			System.out.println(result);
//		});
//	}
//
//	static Predicate<Map<String, Object>> getUnUsePredicate()
//	{
//		return p->{
//			if (!p.containsKey("tvalue")) {
//				return false;
//			}
//			return true;
//		};
//	}
//
//	public static String filter(Predicate<Map<String, Object>> predicate, Map<String, Object> param) {
//		if(predicate.test(param))
//		{
//			return "hello";
//		}
//		return "no";
//	}
//}
