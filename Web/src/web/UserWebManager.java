package web;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.Cookie;

import core.db.db;
import core.support.Helper;

public class UserWebManager {
	private String user;
	private String email;
	private List<String> crawls;
	
	public UserWebManager(Cookie[] cookies) throws Exception {
		if (cookies != null && cookies.length > 0) {
			// lets look for ours :-)
			String username = null;
			String encPassword = null;
			
			for (int i=0; i<cookies.length; i++) {
				Cookie c = cookies[i];
				if (c.getName().equals("username")) {
					username = c.getValue();
				} else if (c.getName().equals("password")) {
					encPassword = c.getValue();
				}
			}
			
			if (!Helper.nullOrEmpty(username) && !Helper.nullOrEmpty(encPassword)) {
				// try to login
				this.login(username, encPassword); 
			}
		}
	}
	
	public String getUser() {
		return user;
	}
	
	public String getEmail() {
		return email;
	}
	
	public List<String> getCrawls() {
		return crawls;
	}
	
	public List<QuickCrawlWebManager> downloadCrawls() throws Exception {
		List<QuickCrawlWebManager> result = new ArrayList<QuickCrawlWebManager>();
		
		for (String crawlId : crawls) {
			QuickCrawlWebManager toAdd = new QuickCrawlWebManager();
			toAdd.load(crawlId);
			result.add(toAdd);
		}
		
		Comparator<QuickCrawlWebManager> comparator = new Comparator<QuickCrawlWebManager>() {

			@Override
			public int compare(QuickCrawlWebManager a,
					QuickCrawlWebManager b) {
				try {
					if (a.getCrawl().getCreated().after(b.getCrawl().getCreated())) {
						return -1;
					} else {
						return 1;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				return 0;
			}
			
		};
		Collections.sort(result, comparator);
		return result;
	}
	
	public void deleteCrawl(String id) throws Exception {
		if (!db.IsConnected()) {
			db.Connect();
		}
		
		String sql = "delete from crawls where id = ?";
		PreparedStatement pstmt = db.conn.prepareStatement(sql);
        pstmt.setString(1, id);
        pstmt.executeUpdate();
        pstmt.close();
        
        crawls.remove(id);
	}
	
	public boolean isLoggedIn() {
		return (user != null);
	}
	
	/**
	 * 0 = Ok, 1=Username not found, 2=Password Incorrect
	 * @param user
	 * @param password
	 * @return
	 * @throws Exception 
	 */
	public int login(String user, String password) throws Exception {
		if (!db.IsConnected()) {
			db.Connect();
		}
		
		String sql = "select count(1) as count from users where LOWER(user) = ?";
		PreparedStatement pstmt = db.conn.prepareStatement(sql);
        pstmt.setString(1, user.toLowerCase());
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        int count = rs.getInt("count");
        
        int result = 0;
        
        if (count == 1) {
        	sql = "select password from users where LOWER(user) = ?";
        	pstmt = db.conn.prepareStatement(sql);
        	pstmt.setString(1, user.toLowerCase());
        
        	rs = pstmt.executeQuery();
        	rs.next();
        	if (!rs.getString("password").equals(password))
        		result = 2;
        } else {
        	result = 1; // user not found
        }
        
        if (result == 0) {
        	loadUser(user);
        }
        
        rs.close();
        pstmt.close();
                
        return result;
	}
	
	public void loadUser(String user) throws Exception {
		if (!db.IsConnected()) {
			db.Connect();
		}
		
		String sql = "select user, email from users where LOWER(user) = ?";
		PreparedStatement pstmt = db.conn.prepareStatement(sql);
        pstmt.setString(1, user.toLowerCase());
        
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        user = rs.getString("user"); // get the right case from when they registered 
        String email = rs.getString("email");
                
        rs.close();
        pstmt.close();
        
        sql = "select id from crawls where LOWER(user) = ?";
		pstmt = db.conn.prepareStatement(sql);
        pstmt.setString(1, user.toLowerCase());
        
        rs = pstmt.executeQuery();
        List<String> crawls = new ArrayList<String>();
        while (rs.next())
        	crawls.add(rs.getString("id"));

        // set vars
        this.user = user;
        this.email = email;
        this.crawls = crawls;
        
        rs.close();
        pstmt.close();
	}
	
	public void logout() {
		user = null;
	}
	
	public int register(String user, String password, String email) throws Exception {
		if (!db.IsConnected()) {
			db.Connect();
		}
		
		String sql = "select count(*) as count from users where LOWER(user) = ?";
		PreparedStatement pstmt = db.conn.prepareStatement(sql);
        pstmt.setString(1, user.toLowerCase());
        
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        int result = rs.getInt("count");
        rs.close();
        pstmt.close();
        
        if (result > 0) {
        	result = 1; // username already used
        }
        
        if (result == 0) {
        	// lets register
        	sql = "insert into users (user, password, email) values (?, ?, ?)";
        	pstmt = db.conn.prepareStatement(sql);
        	pstmt.setString(1, user);
        	pstmt.setString(2, password);
        	pstmt.setString(3, email);
        	pstmt.executeUpdate();
        }
                
        return result;
	}
	
}
