package post;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import minitwit.*;

public class PostStore implements IPostStore {
    Connection conn;

    public PostStore(Connection conn) {
        this.conn = conn;
    }

    public void addPost(int userId, String text) throws SQLException {
        try (var insert = conn.prepareStatement(
                "insert into message (author_id, text, pub_date, flagged)\n" +
                        "values (?, ?, ?, 0)")) {

            long unixTime = System.currentTimeMillis() / 1000L;

            insert.setInt(1, userId);
            insert.setString(2, text);
            insert.setLong(3, unixTime);

            insert.execute();
        }
    }

    public ArrayList<HashMap<String, Object>> getLatestMessages(int perPage) throws SQLException {
        try (var messageStmt = conn.prepareStatement("""
                select message.*, \"user\".* from message, \"user\"
                where message.flagged = 0 and message.author_id = \"user\".user_id
                order by message.pub_date desc limit ?""")) {
            messageStmt.setInt(1, perPage);
            var messages = new ArrayList<HashMap<String, Object>>();
            var messageRs = messageStmt.executeQuery();
            while (messageRs.next()) {
                HashMap<String, Object> result = new HashMap<>();
                result.put("message_id", messageRs.getInt("message_id"));
                result.put("author_id", messageRs.getInt("author_id"));
                result.put("text", messageRs.getString("text"));
                result.put("pub_date", messageRs.getInt("pub_date"));
                result.put("flagged", messageRs.getInt("flagged"));
                result.put("username", messageRs.getString("username"));
                result.put("email", messageRs.getString("email"));
                messages.add(result);
            }

            return messages;
        }
    }

    public ArrayList<HashMap<String, Object>> getLatestUserMessages(int perPage, int userId) throws SQLException {
        try (var statement = conn.prepareStatement("""
                select message.*, \"user\".* from message, 
                \"user\" where message.flagged = 0 and message.author_id = \"user\".user_id 
                and (\"user\".user_id = ? or \"user\".user_id 
                in (select whom_id from follower where who_id = ?)) 
                order by message.pub_date desc limit ?""")) {

            statement.setInt(1, userId);
            statement.setInt(2, userId);
            statement.setInt(3, perPage);
            ResultSet rs = statement.executeQuery();

            var results = new ArrayList<HashMap<String, Object>>();
            while (rs.next()) {
                HashMap<String, Object> result = new HashMap<>();
                result.put("message_id", rs.getInt("message_id"));
                result.put("author_id", rs.getInt("author_id"));
                result.put("text", rs.getString("text"));
                result.put("pub_date", rs.getString("pub_date")); // Type?
                result.put("flagged", rs.getInt("flagged"));
                result.put("username", rs.getString("username"));
                result.put("email", rs.getString("email"));
                result.put("pw_hash", rs.getString("pw_hash"));
                results.add(result);
            }

            return results;
        }
    }
}
