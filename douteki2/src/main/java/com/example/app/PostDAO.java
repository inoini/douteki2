package com.example.app;

import java.sql.*;

import java.util.*;
import org.springframework.stereotype.Repository;

@Repository
public class PostDAO {

	// 投稿保存

	public void insertPost(String name, String message, String snsUrl, String snsUrl2, String discordName,
			boolean isAdmin) {

		try {

			Connection conn = DBUtil.getConnection();

			String sql = "INSERT INTO posts(name, message, sns_url, sns2, discord_name, is_admin)"
					+ " VALUES(?,?,?,?,?,?)";

			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setString(1, name);
			ps.setString(2, message);
			ps.setString(3, snsUrl);
			ps.setString(4, snsUrl2);
			ps.setString(5, discordName);
			ps.setBoolean(6, isAdmin);

			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	// 一覧取得
	public List<Post> getAllPosts() {

		List<Post> list = new ArrayList<>();

		try {
			Connection conn = DBUtil.getConnection();

			String sql = "SELECT * FROM posts ORDER BY id DESC";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Post p = new Post();
				p.setId(rs.getInt("id"));
				p.setName(rs.getString("name"));
				p.setMessage(rs.getString("message"));
				p.setSnsUrl(rs.getString("sns_url"));
				p.setSnsUrl2(rs.getString("sns2"));
				p.setDiscordName(rs.getString("discord_name"));
				p.setCreatedAt(rs.getString("created_at"));
				p.setAdmin(rs.getBoolean("is_admin"));
				list.add(p);
			}

			rs.close();
			ps.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public Post findById(int id) {

		Post post = null;

		try {
			Connection conn = DBUtil.getConnection();

			String sql = "SELECT * FROM posts WHERE id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setInt(1, id);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				post = new Post();
				post.setId(rs.getInt("id"));
				post.setName(rs.getString("name"));
				post.setMessage(rs.getString("message"));
				post.setSnsUrl(rs.getString("sns_url"));
				post.setSnsUrl2(rs.getString("sns2")); // ←ここ追加
				post.setDiscordName(rs.getString("discord_name"));
				post.setCreatedAt(rs.getString("created_at"));
			}

			rs.close();
			ps.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return post;
	}

//消去メソッド
	public void deletePost(int id) {

		try {
			Connection conn = DBUtil.getConnection();

			String sql = "DELETE FROM posts WHERE id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setInt(1, id);

			int result = ps.executeUpdate();

			ps.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Post> getPostsByPage(int limit, int offset) {

		List<Post> list = new ArrayList<>();

		try {

			Connection conn = DBUtil.getConnection();

			String sql = "SELECT * FROM posts ORDER BY id DESC LIMIT ? OFFSET ?";

			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setInt(1, limit);
			ps.setInt(2, offset);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				Post p = new Post();

				p.setId(rs.getInt("id"));
				p.setName(rs.getString("name"));
				p.setMessage(rs.getString("message"));
				p.setSnsUrl(rs.getString("sns_url"));
				p.setSnsUrl2(rs.getString("sns2"));
				p.setDiscordName(rs.getString("discord_name"));
				p.setCreatedAt(rs.getString("created_at"));
				p.setAdmin(rs.getBoolean("is_admin"));
				list.add(p);
			}

			rs.close();
			ps.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public int countPostsToday(String userId) {

		String sql = "SELECT COUNT(*) FROM posts " + "WHERE name = ? AND DATE(created_at) = CURDATE()";

		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, userId);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return rs.getInt(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	public List<Post> findAll() {

		List<Post> list = new ArrayList<>();

		// テストデータ（動作確認用）
		Post p1 = new Post();
		p1.setId(1);
		p1.setName("テストユーザー");
		p1.setMessage("一覧表示OK");

		list.add(p1);

		return list;
	}

	public List<Post> findAllPaging(int limit, int offset) {

		List<Post> list = new ArrayList<>();

		try {
			Connection conn = DBUtil.getConnection();

			String sql = "SELECT * FROM posts ORDER BY id DESC LIMIT ? OFFSET ?";
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setInt(1, limit);
			ps.setInt(2, offset);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Post p = new Post();
				p.setId(rs.getInt("id"));
				p.setName(rs.getString("name"));
				p.setMessage(rs.getString("message"));
				p.setSnsUrl(rs.getString("sns_url"));
				p.setSnsUrl2(rs.getString("sns2"));
				p.setDiscordName(rs.getString("discord_name"));
				p.setAdmin(rs.getBoolean("is_admin"));

				list.add(p);
			}

			rs.close();
			ps.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public void deleteOldestPostIfOver500() {

		try {
			Connection conn = DBUtil.getConnection();

			String countSql = "SELECT COUNT(*) FROM posts";
			PreparedStatement cps = conn.prepareStatement(countSql);
			ResultSet rs = cps.executeQuery();

			if (rs.next() && rs.getInt(1) > 500) {

				String sql = "DELETE FROM posts ORDER BY id ASC LIMIT 1";
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.executeUpdate();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public List<Post> searchPosts(String keyword, int limit, int offset) {

	    List<Post> list = new ArrayList<>();

	    try {
	        Connection conn = DBUtil.getConnection();

	        String sql =
	            "SELECT * FROM posts " +
	            "WHERE name LIKE ? " +
	            "OR message LIKE ? " +
	            "OR sns_url LIKE ? " +
	            "OR sns2 LIKE ? " +
	            "ORDER BY id DESC " +
	            "LIMIT ? OFFSET ?";

	        PreparedStatement ps = conn.prepareStatement(sql);

	        String safeKeyword = (keyword == null) ? "" : keyword.trim();
	        String like = "%" + safeKeyword + "%";
	        
	        ps.setString(1, like);
	        ps.setString(2, like);
	        ps.setString(3, like);
	        ps.setString(4, like);
	        ps.setInt(5, limit);
	        ps.setInt(6, offset);

	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            Post p = new Post();
	            p.setId(rs.getInt("id"));
	            p.setName(rs.getString("name"));
	            p.setMessage(rs.getString("message"));
	            p.setSnsUrl(rs.getString("sns_url"));
	            p.setSnsUrl2(rs.getString("sns2"));

	            list.add(p);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return list;
	}
	public void delete(int id) {

	    try {
	        Connection conn = DBUtil.getConnection();

	        String sql = "DELETE FROM posts WHERE id=?";

	        PreparedStatement ps =
	                conn.prepareStatement(sql);

	        ps.setInt(1, id);

	        ps.executeUpdate();

	        ps.close();
	        conn.close();

	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	}
}
