# team4SE:iTrust2
- [Team Details](#team-details)
- [Iteration 1](#iteration-1-2023-10-31)
- [Iteration 2](#iteration-2-2023-11-21)

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

<details>
  <summary markdown="span">iTrust2 실행하기 (by 태헌 in Windows11, 제가 실행시키면서 격었던 문제들과 그 해결책 적어놓겠습니다. 근데 다른 문제가 나타나면 저도 잘 몰라요)</summary>

- Application 버전: JDK 11, MySQL 8.1 (이 때 MySQL 계정 비밀번호를를 대문자+소문자+숫자+기호 포함된 8자이상으로 설정 안하면 오류 뜹니다.)
1. Repo clone한 다음에 가장 먼저 `./iTrust2/src/main/java/edu/nscu/csc/itrsut2` 에서 마지막 `itrust2`를 `iTrust2`로 바꿔주세요.
    - `./iTrust2/test/main/java/edu/nscu/csc/itrsut2`에서도 마지막 `itrust2`를 `iTrust2`로 바꿔주세요.
2. [itrust2-properties](https://github.com/ncsu-csc326/iTrust2/blob/main/docs/Developers-Guide.md#itrust2-properties)에 나온대로 `application.yml` 파일 만들어주고, 계정이름이랑 비번 입력한 다음에, 기존 url 항목에 밑에꺼 넣어주세요 (timezone 설정해주는거 추가됨)
    - url: jdbc:mysql://localhost:3306/iTrust2_test?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true
3. `./itrust2/iTrust2`에서 shell에다가 `mvn spring-boot:run` 입력하면 돌아갈껍니다.
    - 만약 안되면 [Developers-Gude.md](https://github.com/ncsu-csc326/iTrust2/blob/main/docs/Developers-Guide.md##import-itrust2-into-eclipse-workspace)에 나온대로 eclipse에 추가한 다음에 시도해보는걸 추천드립니다.

- 처음에 돌아갈 때 sample user라고 밑에 있는데 누르면 로그인이 안됨. 정확한 이유는 모르겠지만, 처음 돌려서인지 db 생성이 안된건지 모르겠지만, 막 누르면 ban당해서 1시간동안 아무 기능도 확인을 못하게 되기 때문에 조심하세요..

-link: (http://localhost:8080/iTrust2)

</details>


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
    - Each member write the summary in [Use_Case_summary.md](https://34.64.100.199/team4se/itrust2/-/blob/develop/docs/use-cases/summary.md?ref_type=heads)
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

### Meeting#3 (2023-10-26)
What we did:
- Each member chose `Front-end` or `Back-end`.
- Based on the choices, we divided the team into 3, and assigned 2 UCs per team to implement.

| Team 1 | Team 2 | Team 3 |
|--------|--------|--------|
| 강재현 (`Front-end`) |김태헌 (`Front-end`) | 이호재 (`Front-end`)  |
| 김성환 (`Front-end`) |한철원 (`Back-end`) | 홍현기 (`Front-end`)  |
| 유지민 (`Back-end`) |최원재 (`Back-end`) | 반석 (`Back-end`)  |
|       |       | 김윤태 (`Back-end`)  |

    Team 1: UC16 & UC19
    Team 2: UC15 & UC18
    Team 3: UC17 & New UC

- For the presentation of iteration1, we decided presenters and the contents of the presentation.
    - Presenters: 강재현, 김태헌, 이호재
    - Contents:
        - 조원소개
        - 8개 중 선택한 5개 UC 내용 요약
            - User Stories 설명
        - Newly proposed UC 내용 요약
            - User Stories
        - (iTrust2 Demo if time left)
        - Future plan
            - How we divide the team into sub-teams

- We created google docs to share User Stories and Use Case that we propose.
    - We decided the common structure of User Story

            As a ~, I want ~, So that ~
    - We wrote some basic components of our UC in the docs.

ToDo:
- Each sub-team writes User Stories of the assigned UCs before Sunday(10/29) 10PM.
    - Team3 completes newly proposed UC.
- 재현, 태헌, 호재: Prepare presentation before Tuesday(10/31)

### Meeting#4 (2023-10-30)
What we did:
- Based on the work we had done in the google docs, we create [UC23.md](https://34.64.100.199/team4se/itrust2/-/blob/develop/docs/use-cases/UC23.md?ref_type=heads) in the same format as other UCs.
- Create User_Story.md ([team1](https://34.64.100.199/team4se/itrust2/-/blob/develop/docs/use-cases/User_Story_team1.md), [team2](https://34.64.100.199/team4se/itrust2/-/blob/develop/docs/use-cases/User_Story_team2.md), [team3](https://34.64.100.199/team4se/itrust2/-/blob/develop/docs/use-cases/User_Story_team3.md)) for each team and neatly organize each user story and its basic scenario.
- Create [google slides](https://docs.google.com/presentation/d/1tC3bB02xBQjqlFD9e0CbPNYj5uUtUy4dsA_H0H2TqCg/edit?usp=sharing) to present what we have done during iteration 1.

### Meeting#5 (2023-11-01)
What we did:
- Discuss how are we going to cooperate (lead by 원재)
    - All our code implementation is going to be managed in the branch ['develop'](https://34.64.100.199/team4se/itrust2/-/tree/develop?ref_type=heads)
    - Share [ERD](https://dbdiagram.io/d/653b3ea5ffbf5169f08f5cb8)
    - Share [detailed Guildelines](https://34.64.100.199/team4se/itrust2/-/blob/develop/CONTRIBUTING.md?ref_type=heads) to start coding
    - Share & learn [COOP rules](https://34.64.100.199/team4se/itrust2/-/blob/develop/rules.md?ref_type=heads) for easier cooperation
- Finalize our presentation preparation by giving feedbacks


Next Meeting: Decide after the presentation.

## Iteration 2 (~2023-11-21)
- Team leader: 유지민
- Planning leader: 최원재
- QA leader: 김윤태

### Meeting#6 (2023-11-05)
What we did:
- Allocate leader roles for Iteration 2.
- Set initial deadline for UC implementations.
- Discuss whether to use AWS RDS for development & testing.

ToDo:
- Each group individually implement UCs by 2023-11-14.
- Merge implemented features.
- Try to change the dependency of this project (Maven --> Gradle)

From team2

What we did:
- As our team consists of one FE and two BEs, each BE developer decided to implement one UC
    - UC15: 철원, UC18: 원재
    - 태헌 develop FE for both UCs.
- Created prototype of some html pages of each UC to visualize how each UC works, which can help both FE and BE developers to implement new API or scripts.

ToDo:
- BE developers: implement APIs that are necessary and let FE developer know how to use it
- FE developer: develop web-pages based on newly implemented APIs.

### Meeting#7 (2023-11-07)

From Team3

What we did:
- Understanding of UC17
- Understanding of what frontend and backend will do

ToDo:
 - backend -> make Loinc and Procedure Units
 - Frontend -> make Procedure template for admin

### Meeting #8 (2023-11-09)

From Team3

What we did:
- Create, edit, delete Loinc code by Admin

ToDo:
- Frontend -> Create, edit, delete Procedure by HCP
- Backend -> view, edit API for Labtech, view API for patient

From Team2

What we did:
- Add searching functionalities using patient name or MID for UC15
- Talk about what kinds of API is needed to load prescriptions and diagnoses of selected patient.
