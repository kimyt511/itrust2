# team4SE:iTrust2

## ToDo
1. GitLab Account Creation:
    - Register a GitLab account for your team project using your team name at our server.
2. Private Project Creation:
    - Establish a private project named iTrust2 on GitLab. Ensure that each team member
receives a proper invitation with the necessary permissions.
    - Utilize this project for managing all aspects of your work, including implementations,
documentation, progress reports, and team meeting minutes.
3. `README.md` Initialization:
    - Create a `README.md` file and provide initial details about your team, including team
members with their GitLab IDs and contact information, their experiences, regular
meeting times, etc.
    - This file should serve as a central hub for reporting the progress of your team project.
4. Team Contract Submission:
    - Fill out, sign, and upload your completed Team Contract to the GitLab project.

## iTrust2 실행하기 (by 태헌 in Windows11, 제가 실행시키면서 격었던 문제들과 그 해결책 적어놓겠습니다. 근데 다른 문제가 나타나면 저도 잘 몰라요)
- Application 버전: JDK 11, MySQL 8.1 (이 때 MySQL 계정 비밀번호를를 대문자+소문자+숫자+기호 포함된 8자이상으로 설정 안하면 오류 뜹니다.)
1. Repo clone한 다음에 가장 먼저 './iTrust2/src/main/java/edu/nscu/csc/itrsut2' 에서 마지막 'itrust2'를 'iTrust2'로 바꿔주세요.
    - './iTrust2/test/main/java/edu/nscu/csc/itrsut2'에서도 마지막 'itrust2'를 'iTrust2'로 바꿔주세요.
2. [itrust2-properties](https://github.com/ncsu-csc326/iTrust2/blob/main/docs/Developers-Guide.md#itrust2-properties)에 나온대로 'application.yml' 파일 만들어주고, 계정이름이랑 비번 입력한 다음에, 기존 url 항목에 밑에꺼 넣어주세요 (timezone 설정해주는거 추가됨)
    - url: jdbc:mysql://localhost:3306/iTrust2_test?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true
3. './itrust2/iTrust2'에서 shell에다가 'mvn spring-boot:run' 입력하면 돌아갈껍니다.
    - 만약 안되면 [Developers-Gude.md](https://github.com/ncsu-csc326/iTrust2/blob/main/docs/Developers-Guide.md##import-itrust2-into-eclipse-workspace)에 나온대로 eclipse에 추가한 다음에 시도해보는걸 추천드립니다.

- 근데 일단 돌아가기는 하는데 로그인 창에서부터 뭐가 안됨 ㅋ

## Team details
| Name | GitLab Username | e-mail |
|------|-----------------|--------| 
| 강재현 | KangJaehyun | |
| 김태헌 | thkim0305 | thkim0305@yonsei.ac.kr |
| 이호재 | 2018147582 | unikskyseed@yonsei.ac.kr |
| 김성환 | 2019147562 | happysnail06@yonsei.ac.kr |
| 김윤태 | kimyuntae | kimyt0511@yonsei.ac.kr |
| 반 석 | 2019147568 | semient_97@naver.com |
| 유지민 | yxxjimin | jmy0819@yonsei.ac.kr |
| 최원재 | wonj | jaja1012@yonsei.ac.kr |
| 한철원 | 2019147566 | hchulwon3080@gmail.com |
| 홍현기 | Hyunki Hong | htk77733377@yonsei.ac.kr |


## Iteration 1 (~2023-10-31)
- Team leader: 강재현
- Planning leader: 김태헌
- QA leader: 이호재

### Meeting#1 (2023-10-14)
What we did:

- Next meeting: 2023-10-20

ToDo:
- summarize all (implemented/unimplemented) UCs
- suggest one new UC to implement at the next meeting



