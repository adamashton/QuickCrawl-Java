package core.db;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import core.global.CrawlManager;
import core.support.DBSupport;
import core.support.Log;

public class db {
	private static String url = "jdbc:mysql://localhost/QuickCrawl";
	private static String username = "web";
	private static String password = "web";
	
	public static java.sql.Connection conn;
	
	private static String lastError = ""; // what was the last error that occured
	
	private static boolean Connected = false;
	
	public static String getLastError() {
		return lastError;
	}
	
	public static boolean IsConnected() {
		boolean result = conn != null;
		if (result) {
			try {
				result = result
					&& !conn.isClosed() 
					&& conn.isValid(1);
				
				if (!result) {
					// at least try n close before we reconnect
					conn.close();
				}
			} catch (SQLException e) {
				result = false;
			}
		}
		return result;
	}
	
	public static boolean Connect() throws Exception {
		boolean result = true;
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		conn = DriverManager.getConnection(url, username, password);
		Connected = true;		
		return result;
	}
	
	public static boolean Disconnect() {
		boolean result = true;
		try {			
			conn.close();
			Connected = false;
		} catch (Exception ex) {
			result = false;
			System.err.print(ex);
			lastError = ex.getMessage();
		}
		return result;
	}

	public static String Add(CrawlManager manager, String user) throws Exception {
		if (!db.IsConnected()) {
			db.Connect();
		}
		
		// get id
		boolean free = false;
		String id = null;
		while (!free) {
			id = DBSupport.generateID(4);
			
			String sql = "select count(1) as count from crawls where id = ?";
			PreparedStatement p = conn.prepareStatement(sql);
			p.setString(1, id);
			ResultSet rs = p.executeQuery();
			rs.next();
			int count = rs.getInt("count");
			free = count == 0;
		}
		
		
		String sql = "insert into crawls(id, user, manager, log) values (?, ?, ?, ?)";
		PreparedStatement p = conn.prepareStatement(sql);
		p.setString(1, id);
		p.setString(2, user);
		p.setObject(3, manager);
		p.setObject(4, manager.getSavedSettings().Logger);
		p.executeUpdate();
		p.close();
		
		return id;
	}
	
	public static List<String> getGeneratedCrawls() throws Exception {
		if (!db.IsConnected()) {
			db.Connect();
		}
		
		String sql = "select id from crawls";
		PreparedStatement pstmt = conn.prepareStatement(sql);
        
		
        ResultSet rs = pstmt.executeQuery();
        List<String> result = new ArrayList<String>();
        while (rs.next()) {
        	result.add(rs.getString("id"));
        }
		return result;
	}

	public static CrawlManager getCrawlManager(String crawlid) throws Exception {
		if (!db.IsConnected()) {
			db.Connect();
		}
		
		String sql = "select manager, log from crawls where id = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, crawlid);
        
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        byte[] buf = rs.getBytes("manager");
        ObjectInputStream objectIn = new ObjectInputStream(new ByteArrayInputStream(buf));
        Object object = objectIn.readObject();
        CrawlManager result = (CrawlManager) object;
        
        buf = rs.getBytes("log");
        objectIn = new ObjectInputStream(new ByteArrayInputStream(buf));
        object = objectIn.readObject();
        Log logger = (Log)object; 
        
        rs.close();
        pstmt.close();
        
        // deliberately seperated as the manager+log can exceed the size of the blob.
        result.getSavedSettings().Logger = logger;
        
        return result;
	}

	public static void registerUser(String user, String encPassword, String email) throws Exception {
		if (!db.IsConnected()) {
			db.Connect();
		}
		
		String sql = "insert into users (user, password, email) values (?, ?, ?)";
		
		PreparedStatement p = conn.prepareStatement(sql);
		p.setString(1, user);
		p.setObject(2, encPassword);
		p.setObject(3, email);
		
		p.executeUpdate();
		p.close();
	}

	
}
