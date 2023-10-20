# team4SE:iTrust2
## Team details
| Name | GitLab Username | e-mail                    |
|------|-----------------|---------------------------| 
| 강재현 | KangJaehyun | luxmea1005@yonsei.ac.kr   |
| 김태헌 | thkim0305 | thkim0305@yonsei.ac.kr    |
| 이호재 | 2018147582 | unikskyseed@yonsei.ac.kr  |
| 김성환 | 2019147562 | happysnail06@yonsei.ac.kr |
| 김윤태 | kimyuntae | kimyt0511@yonsei.ac.kr    |
| 반  석 | 2019147568 | semient_97@naver.com      |
| 유지민 | yxxjimin | jmy0819@yonsei.ac.kr      |
| 최원재 | wonj | jaja1012@yonsei.ac.kr     |
| 한철원 | 2019147566 | hchulwon3080@gmail.com    |
| 홍현기 | Hyunki Hong | htk77733377@yonsei.ac.kr  |

## iTrust2 실행하기 (by 태헌 in Windows11, 제가 실행시키면서 격었던 문제들과 그 해결책 적어놓겠습니다. 근데 다른 문제가 나타나면 저도 잘 몰라요)
- Application 버전: JDK 11, MySQL 8.1 (이 때 MySQL 계정 비밀번호를를 대문자+소문자+숫자+기호 포함된 8자이상으로 설정 안하면 오류 뜹니다.)
1. Repo clone한 다음에 가장 먼저 `./iTrust2/src/main/java/edu/nscu/csc/itrsut2` 에서 마지막 `itrust2`를 `iTrust2`로 바꿔주세요.
    - `./iTrust2/test/main/java/edu/nscu/csc/itrsut2`에서도 마지막 `itrust2`를 `iTrust2`로 바꿔주세요.
2. [itrust2-properties](https://github.com/ncsu-csc326/iTrust2/blob/main/docs/Developers-Guide.md#itrust2-properties)에 나온대로 `application.yml` 파일 만들어주고, 계정이름이랑 비번 입력한 다음에, 기존 url 항목에 밑에꺼 넣어주세요 (timezone 설정해주는거 추가됨)
    - url: jdbc:mysql://localhost:3306/iTrust2_test?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true
3. `./itrust2/iTrust2`에서 shell에다가 `mvn spring-boot:run` 입력하면 돌아갈껍니다.
    - 만약 안되면 [Developers-Gude.md](https://github.com/ncsu-csc326/iTrust2/blob/main/docs/Developers-Guide.md##import-itrust2-into-eclipse-workspace)에 나온대로 eclipse에 추가한 다음에 시도해보는걸 추천드립니다.

- 근데 일단 돌아가기는 하는데 로그인 창에서부터 뭐가 안됨 ㅋ

## Iteration 1 (~2023-10-31)
- Team leader: 강재현
- Planning leader: 김태헌
- QA leader: 이호재

### Meeting#1 (2023-10-14)
What we did:
- Each member introduces himself to the team
- Undertsand the goal of iteration 1
- Decide to interpret Use Cases of what are alreadly implemented and what are the choices of functionality that we have.
    - each member should read and summarize 2 or 3 UCs.
    - We assigned UCs using 사다리타기

ToDo:
- Summarize all (implemented/unimplemented) UCs
    - Each member write the summary in `Use_Case_summary.md`
- Suggest one new UC to implement at the next meeting

- Next meeting: 2023-10-20
    - Although in `team_contract.md`, we agree to meet twice a week, since it is near the mid-term exam, we decide to meet once a week for iteration 1.

### Meeting#2 (2023-10-20)
What we did:
- Each member described UCs that they read
    - We understand what functionalities are available.
    - We understand what kinds of functionality should be added.
- After understanding UCs, we decided 5 UCs to implement among UC15~22.
    - After a long discussion, our final decision is to implement UC15, 16, 17, 18, and 19.
        - Instead of implementing functionalities related to specific expertise (= ophthalmology), we decide to implement general functionalities.
- We also decided a newly proposed UC of our team.
    - Candidates:
        1. Rating system: patient can leave rate(e.g. star out of 5) and comment/review of HCP/Hospital that he or she met. HCP can leave a comment to the review.
        2. Allowing patient to view upcomming/rejected appointments and to edit approved appointmenets.
        3. HCP Shcedule: HCP decides duration of each appointments when he or she approves it, patient can see the schedule(available/unavailable time) of chosen HCP when requesting appointments.
        4. related to UC2, when a user gets banned and receives an email, allow a user to remove the ban by approving himself and then change password. 
        5. Drug guidelines: admin/HCP can register a guideline of drugs, patient can (only) read the guideline of prescripted drugs.
        6. New drug creation: related to Lab tech role, Lab tech can create/register new drug after its lab study.
    - Among these ideas, we decided to propose 1 Rating system (decided by majority voting). 
    - We decided to make detail about the new UC at the next meeting.
- Decide that each member tries to run `iTrust2` project on local machine separately and checks each UC.

ToDo:
- Study hard for Midterm Exams
- Think of detail/specification of UC15,16,17,18,19 and our newly proposed UC
- Run `iTrust2` on the local machine

- Next meeting: 2023-10-26
