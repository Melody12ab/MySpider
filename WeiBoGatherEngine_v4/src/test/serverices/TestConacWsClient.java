package test.serverices;

import java.util.List;

import com.zel.es.manager.ws.client.test.TestServiceManager;
import com.zel.es.pojos.nlp.AnalyzerResultPojo;

public class TestConacWsClient {
	public static void main(String[] args) {
		String content = "天亮舆情工作室";
		List<AnalyzerResultPojo> list = TestServiceManager.getSplit(content);
		for (AnalyzerResultPojo analyzerResultPojo : list) {
			System.out.println(analyzerResultPojo.getValue());
		}

		System.out.println("done!");
	}
}
