package core.support;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import core.json.JSONObject;

public class ConcurrentRestful {
	private static ConcurrentHashMap<Servers, Long> lastAccess = new ConcurrentHashMap<Servers, Long>(2);
	
	// each server has it's own lock
	private static HashMap<Servers, Object> locks = new HashMap<Servers, Object>(2);
	
	private static Object mainLock = new Object();
	
	private static int waitServerMillis = 100;
	
	public static JSONObject getJSONResult(String url, Servers server, Log log) throws Exception {
		//Log.Debug("attempt GET " + url);
		
		Object serverLock;
		
		synchronized (mainLock) {
			serverLock = locks.get(server);
			
			if (serverLock == null) {
				serverLock = new Object();
				locks.put(server, serverLock);
				lastAccess.put(server, new Long(0));
			}
		}
		
		synchronized (serverLock) {
			// when can we start accessing the server?
			long whenCanAccess = lastAccess.get(server) + waitServerMillis;
			
			// so how long must we wait?
			long wait = whenCanAccess - System.currentTimeMillis();
			if (wait < 0) {
				wait = 0;
			}
			
			//Log.Debug("wait: " + wait + " on " + server);
			
			Thread.sleep(wait);			
			lastAccess.put(server, System.currentTimeMillis());
		}
		
		JSONObject result = downloadJSONResult(url, log);
		return result;
	}
	
	private static JSONObject downloadJSONResult(String address, Log log) throws Exception
	{
		log.Debug("GET " + address);
		URL url = new URL(address);
		DataInputStream dis = new DataInputStream(url.openStream());
		InputStreamReader isr = new InputStreamReader(dis);
		BufferedReader br = new BufferedReader(isr);

		StringBuilder jsonResultSB = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null) {
			jsonResultSB.append(line);
		}
		
		JSONObject result = new JSONObject(jsonResultSB.toString());
		return result;
	}
	
	/*
	private static JSONObject downloadJSONResult(String address) throws InterruptedException {
		System.out.println("GET:" + address);
		Thread.sleep(1000);
		System.out.println("GOT:" + address);
		return new JSONObject();
	}
	*/
}
