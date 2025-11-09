package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MemberDAO {
    private static final String DB_URL = "jdbc:sqlite:team.db";


    public MemberDAO() {
        init();
    }


    private Connection getConn() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public final void init() {
        final String sql = "CREATE TABLE IF NOT EXISTS team_members (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "create_date TEXT NOT NULL DEFAULT (datetime('now','localtime')), " +
                "student_id TEXT NOT NULL, " +
                "name TEXT NOT NULL, " +
                "position TEXT NOT NULL, " +
                "year INTEGER NOT NULL)";
        try (Connection c = getConn(); Statement st = c.createStatement()) {
            st.execute(sql);
            st.execute("CREATE INDEX IF NOT EXISTS idx_team_members_name ON team_members(name)");
            st.execute("CREATE INDEX IF NOT EXISTS idx_team_members_year ON team_members(year)");
            st.execute("CREATE INDEX IF NOT EXISTS idx_team_members_pos ON team_members(position)");
        } catch (SQLException e) {
            throw new RuntimeException("DB 초기화 실패: " + e.getMessage(), e);
        }
    }