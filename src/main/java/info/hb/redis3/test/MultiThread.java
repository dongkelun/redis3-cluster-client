package info.hb.redis3.demo;

import info.hb.redis3.cluster.client.Redis3ClusterClient;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import redis.clients.jedis.JedisCluster;

public class ThreadsDemo {

	public static void main(String[] args) {
		final JedisCluster jc = Redis3ClusterClient.getCluster(9030, "192.168.32.199");

		// System.out.println("------- cluster nodes --------");
		// int n = jc.getClusterNodes().keySet().size();
		// System.out.println(n);
		// String host[] = new String[n];
		// Map<String, JedisPool> nodes = jc.getClusterNodes();
		// Iterator<Map.Entry<String, JedisPool>> iterNodes = nodes.entrySet()
		// .iterator();
		// int i = 0;
		// while (iterNodes.hasNext()) {
		// Map.Entry<String, JedisPool> entry = iterNodes.next();
		// host[i++] = entry.getKey();
		// }
		// System.out.println(host[0]);
		// String s[] = host[0].split(":");
		// System.out.println(s[0]);
		// System.out.println(s[1]);
		// int j = Integer.parseInt(s[1]);
		//
		// System.out.println(j + 1);
		ThreadPoolExecutor executor = new ThreadPoolExecutor(100, 100, 0L, TimeUnit.MILLISECONDS,
				new ArrayBlockingQueue<Runnable>(100), new ThreadPoolExecutor.CallerRunsPolicy());
		final String values[] = new String[1000];
		for (int i = 0; i < 1000; i++) {
			values[i] = "value" + i;
		}
		long start = System.currentTimeMillis();
		for (int i = 0; i < Math.pow(10, 4); i++) {
			final String key = "key" + i;
			//			final String value = "value"+i;
			//			final Set<String> result = jc.smembers(key);
			//			 System.out.println(result);
			executor.execute(new Runnable() {
				@Override
				public void run() {
					jc.sadd(key, values);
					System.out.println(jc.smembers(key).size());
				}
			});
			System.err.println(i);
		}
		executor.shutdown();
		long end = System.currentTimeMillis();
		System.out.println("100个线程写1000万条数据需要[" + (end - start) / 1000 + "]秒 ..");
	}

}
