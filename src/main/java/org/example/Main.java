package org.example;

import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class Main {
    private final Scanner sc = new Scanner(System.in);
    private final MemberService service = new MemberService(new MemberDAO());


    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        while (true) {
            printMenu();
            System.out.print("메뉴 선택: ");
            String sel = sc.nextLine().trim();
            switch (sel) {
                case "1":
                    listAll();
                    break; // 전체 조회
                case "2":
                    add();
                    break; // 추가
                case "3":
                    edit();
                    break; // 수정
                case "4":
                    remove();
                    break; // 삭제
                case "5":
                    searchByName();
                    break; // 검색(이름)
                case "6":
                    filter();
                    break; // 필터(학년/직책)
                case "7":
                    sortByCreateDateDesc();
                    break; // 정렬(등록일 내림)
                case "8":
                    statsByYear();
                    break; // 통계(학년별 인원)
                case "9":
                    export();
                    break; // 파일로 내보내기
                case "0":
                    System.out.println("종료합니다.");
                    return;
                default:
                    System.out.println("올바른 번호를 입력하세요.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n==================== 팀모임명단 ====================");
        System.out.println("1. 전체 목록");
        System.out.println("2. 추가 (Create)");
        System.out.println("3. 수정 (Update)");
        System.out.println("4. 삭제 (Delete)");
        System.out.println("5. 이름 검색 (부가기능1)");
        System.out.println("6. 학년/직책 필터 (부가기능2)");
        System.out.println("7. 등록일 내림차순 정렬 (부가기능3)");
        System.out.println("8. 학년별 인원 통계");
        System.out.println("9. 파일로 내보내기 (data_yyyymmdd_hhmm.txt)");
        System.out.println("0. 종료");
        System.out.println("===================================================");
    }


    private void listAll() {
        List<Member> list = service.getAll();
        if (list.isEmpty()) System.out.println("데이터가 없습니다.");
        else list.forEach(System.out::println);
    }

    private void add() {
        System.out.print("학번: ");
        String sid = sc.nextLine().trim();
        System.out.print("이름: ");
        String name = sc.nextLine().trim();
        System.out.print("직책(예: 팀장/서기/회계): ");
        String pos = sc.nextLine().trim();
        System.out.print("학년(숫자): ");
        int year = readIntSafe();


        int rows = service.add(new Member(sid, name, pos, year));
        System.out.println(rows + "건 추가되었습니다.");
    }

    private void edit() {
        System.out.print("수정할 id: ");
        int id = readIntSafe();
        Member m = service.getById(id);
        if (m == null) {
            System.out.println("해당 id가 없습니다.");
            return;
        }


        System.out.printf("학번(%s): ", m.getStudentId());
        String sid = readOrKeep(m.getStudentId());
        System.out.printf("이름(%s): ", m.getName());
        String name = readOrKeep(m.getName());
        System.out.printf("직책(%s): ", m.getPosition());
        String pos = readOrKeep(m.getPosition());
        System.out.printf("학년(%d): ", m.getYear());
        String yrStr = sc.nextLine().trim();
        int year = yrStr.isEmpty() ? m.getYear() : Integer.parseInt(yrStr);


        m.setStudentId(sid);
        m.setName(name);
        m.setPosition(pos);
        m.setYear(year);
        int rows = service.update(m);
        System.out.println(rows + "건 수정되었습니다.");
    }

    private void remove() {
        System.out.print("삭제할 id: ");
        int id = readIntSafe();
        int rows = service.delete(id);
        System.out.println(rows + "건 삭제되었습니다.");
    }

    private void searchByName() {
        System.out.print("이름 키워드: ");
        String kw = sc.nextLine().trim();
        List<Member> list = service.searchByName(kw);
        if (list.isEmpty()) System.out.println("검색 결과가 없습니다.");
        else list.forEach(System.out::println);
    }

    private void filter() {
        System.out.print("필터 유형 선택 (1=학년, 2=직책): ");
        String type = sc.nextLine().trim();
        switch (type) {
            case "1":
                System.out.print("학년(숫자): ");
                int year = readIntSafe();
                service.filterByYear(year).forEach(System.out::println);
                break;
            case "2":
                System.out.print("직책: ");
                String pos = sc.nextLine().trim();
                service.filterByPosition(pos).forEach(System.out::println);
                break;
            default:
                System.out.println("올바른 유형을 입력하세요.");
        }
    }

    private void sortByCreateDateDesc() {
        service.sortByCreateDateDesc().forEach(System.out::println);
    }


    private void statsByYear() {
        Map<Integer, Integer> map = service.countByYear();
        if (map.isEmpty()) {
            System.out.println("데이터가 없습니다.");
            return;
        }
        System.out.println("학년별 인원수:");
        map.forEach((k, v) -> System.out.printf(" %d학년: %d명\n", k, v));
    }

    private void export() {
        String fname = service.exportAllToTimestampedFile();
        System.out.println("내보내기 완료: " + fname);
    }

    private int readIntSafe() {
        while (true) {
            String s = sc.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.print("숫자를 입력하세요: ");
            }
        }
    }


    private String readOrKeep(String current) {
        String s = sc.nextLine().trim();
        return s.isEmpty() ? current : s;
    }
}