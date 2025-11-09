package org.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class MemberService implements ICRUD<Member> {
    private final MemberDAO dao;

    public MemberService(MemberDAO dao) {
        this.dao = dao;
    }

    @Override
    public int add(Member item) {
        return dao.insert(item);
    }


    @Override
    public int update(Member item) {
        return dao.update(item);
    }


    @Override
    public int delete(int id) {
        return dao.delete(id);
    }


    @Override
    public Member getById(int id) {
        return dao.findById(id);
    }


    @Override
    public List<Member> getAll() {
        return dao.findAll();
    }

    // ====== 부가기능 래핑 ======
    public List<Member> searchByName(String keyword) {
        return dao.searchByName(keyword);
    }

    public List<Member> filterByYear(int year) {
        return dao.filterByYear(year);
    }

    public List<Member> filterByPosition(String position) {
        return dao.filterByPosition(position);
    }

    public List<Member> sortByCreateDateDesc() {
        return dao.sortByCreateDateDesc();
    }

    public Map<Integer, Integer> countByYear() {
        return dao.countByYear();
    }


    // ====== 파일로 내보내기 ======
    public String exportAllToTimestampedFile() {
        List<Member> list = dao.findAll();
        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));
        String fname = "data_" + ts + ".txt"; // data_yyyymmdd_hhmm.txt
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fname, false))) {
            bw.write("id\tcreate_date\tstudent_id\tname\tposition\tyear\n");
            for (Member m : list) {
                bw.write(String.format("%d\t%s\t%s\t%s\t%s\t%d\n",
                        m.getId(), m.getCreateDate(), m.getStudentId(), m.getName(), m.getPosition(), m.getYear()));
            }
        } catch (IOException e) {
            throw new RuntimeException("파일 내보내기 실패: " + e.getMessage(), e);
        }
        return fname;
    }
}