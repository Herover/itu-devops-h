package Latest;

import SqlDatabase.SqlDatabase;

import java.sql.SQLException;

public class LatestStore {
    public int getLatest() throws SQLException {
        var connection = new SqlDatabase().getConnection();

        var statement = connection.createStatement();
        var result = statement.executeQuery("SELECT * FROM counters WHERE kind = 'latest' LIMIT 1");
        int value;

        if (result.next()) {
            value = result.getInt("value");
        } else {
            // If there's no result, silently return -1
            value = -1;
        }

        statement.close();
        connection.close();

        return value;
    }

    public void updateLatest(int latest) throws SQLException {
        var connection = new SqlDatabase().getConnection();

        // Insert a row with the latest value unless there's already a counter with the kind "latest", in that case
        // update it.
        var statement = connection.prepareStatement("INSERT INTO counters (kind, value) \n" +
                "VALUES ('latest', ?) \n" +
                "ON CONFLICT (kind) DO UPDATE \n" +
                "  SET kind = 'latest', \n" +
                "      value = ?");

        statement.setInt(1, latest);
        statement.setInt(2, latest);

        statement.execute();

        statement.close();
        connection.close();
    }
}
