package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MemberDAO {
    private static final String DB_URL = "jdbc:sqlite:myData.db";

    public MemberDAO() {
        init();
    }

    private Connection getConn() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public final void init() {
        final String sql = "CREATE TABLE IF NOT EXISTS dataList (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "regdate TEXT NOT NULL DEFAULT (datetime('now','localtime')), " +
                "stu_id TEXT NOT NULL, " +
                "name TEXT NOT NULL, " +
                "position TEXT NOT NULL, " +
                "grade INTEGER NOT NULL)";
        try (Connection c = getConn(); Statement st = c.createStatement()) {
            st.execute(sql);
            st.execute("CREATE INDEX IF NOT EXISTS idx_dataList_name ON dataList(name)");
            st.execute("CREATE INDEX IF NOT EXISTS idx_dataList_grade ON dataList(grade)");
            st.execute("CREATE INDEX IF NOT EXISTS idx_dataList_pos ON dataList(position)");
        } catch (SQLException e) {
            throw new RuntimeException("DB 초기화 실패: " + e.getMessage(), e);
        }
    }

    public int insert(Member m) {
        final String sql = "INSERT INTO dataList(stu_id, name, position, grade) VALUES (?,?,?,?)";
        try (Connection c = getConn(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, m.getStudentId());
            ps.setString(2, m.getName());
            ps.setString(3, m.getPosition());
            ps.setInt(4, m.getYear());
            int rows = ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) m.setId(rs.getInt(1));
            }
            return rows;
        } catch (SQLException e) {
            throw new RuntimeException("추가 실패: " + e.getMessage(), e);
        }
    }

    public int update(Member m) {
        if (m.getId() == null) throw new IllegalArgumentException("id가 필요합니다");
        final String sql = "UPDATE dataList SET stu_id=?, name=?, position=?, grade=? WHERE id=?";
        try (Connection c = getConn(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, m.getStudentId());
            ps.setString(2, m.getName());
            ps.setString(3, m.getPosition());
            ps.setInt(4, m.getYear());
            ps.setInt(5, m.getId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("수정 실패: " + e.getMessage(), e);
        }
    }

    public int delete(int id) {
        final String sql = "DELETE FROM dataList WHERE id=?";
        try (Connection c = getConn(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("삭제 실패: " + e.getMessage(), e);
        }
    }

    public Member findById(int id) {
        final String sql = "SELECT id, regdate, stu_id, name, position, grade FROM dataList WHERE id=?";
        try (Connection c = getConn(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("조회 실패: " + e.getMessage(), e);
        }
        return null;
    }

    public List<Member> findAll() {
        final String sql = "SELECT id, regdate, stu_id, name, position, grade FROM dataList ORDER BY id";
        List<Member> list = new ArrayList<>();
        try (Connection c = getConn(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("전체 조회 실패: " + e.getMessage(), e);
        }
        return list;
    }

    // ====== 부가기능 ======
    public List<Member> searchByName(String keyword) {
        final String sql = "SELECT id, regdate, stu_id, name, position, grade FROM dataList WHERE name LIKE ? ORDER BY name";
        List<Member> list = new ArrayList<>();
        try (Connection c = getConn(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("이름 검색 실패: " + e.getMessage(), e);
        }
        return list;
    }

    public List<Member> filterByYear(int year) {
        final String sql = "SELECT id, regdate, stu_id, name, position, grade FROM dataList WHERE grade=? ORDER BY name";
        List<Member> list = new ArrayList<>();
        try (Connection c = getConn(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, year);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("학년 필터 실패: " + e.getMessage(), e);
        }
        return list;
    }

    public List<Member> filterByPosition(String position) {
        final String sql = "SELECT id, regdate, stu_id, name, position, grade FROM dataList WHERE position=? ORDER BY name";
        List<Member> list = new ArrayList<>();
        try (Connection c = getConn(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, position);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("직책 필터 실패: " + e.getMessage(), e);
        }
        return list;
    }

    public List<Member> sortByCreateDateDesc() {
        final String sql = "SELECT id, regdate, stu_id, name, position, grade FROM dataList ORDER BY datetime(regdate) DESC";
        List<Member> list = new ArrayList<>();
        try (Connection c = getConn(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("정렬 실패: " + e.getMessage(), e);
        }
        return list;
    }

    public Map<Integer, Integer> countByYear() {
        final String sql = "SELECT grade, COUNT(*) AS cnt FROM dataList GROUP BY grade ORDER BY grade";
        Map<Integer, Integer> map = new LinkedHashMap<>();
        try (Connection c = getConn(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) map.put(rs.getInt("grade"), rs.getInt("cnt"));
        } catch (SQLException e) {
            throw new RuntimeException("통계 실패: " + e.getMessage(), e);
        }
        return map;
    }

    private Member map(ResultSet rs) throws SQLException {
        return new Member(
                rs.getInt("id"),
                rs.getString("regdate"),
                rs.getString("stu_id"),
                rs.getString("name"),
                rs.getString("position"),
                rs.getInt("grade")
        );
    }
}
