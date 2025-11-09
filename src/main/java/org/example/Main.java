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
                case "1": listAll(); break; // 전체 조회
                case "2": add(); break; // 추가
                case "3": edit(); break; // 수정
                case "4": remove(); break; // 삭제
                case "5": searchByName(); break; // 검색(이름)
                case "6": filter(); break; // 필터(학년/직책)
                case "7": sortByCreateDateDesc(); break; // 정렬(등록일 내림)
                case "8": statsByYear(); break; // 통계(학년별 인원)
                case "9": export(); break; // 파일로 내보내기
                case "0": System.out.println("종료합니다."); return;
                default: System.out.println("올바른 번호를 입력하세요.");
            }
        }
    }

    private void listAll() {
        List<Member> list = service.getAll();
        if (list.isEmpty()) System.out.println("데이터가 없습니다.");
        else list.forEach(System.out::println);
    }