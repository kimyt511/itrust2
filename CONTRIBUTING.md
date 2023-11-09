# 협업 규칙

---

## 브랜치 규칙

### 브랜치 네이밍

- `main`: 배포용 브랜치
- `develop`: 개발용 브랜치
- `feature/*`: 기능 개발 브랜치 (ex. feature/login 또는 이슈번호를 붙여 feature/#12)

### 개발 flow

`develop`에서 `feature/*` 브랜치를 생성하여 작업하고 `develop`에 Pull Request(Merge Request)를 보낸다.

### 브랜치 관리

- `main`과 `develop` 브랜치는 protected 브랜치로 설정하여 직접적으로 코드를 push하지 못하도록 하고 Pull Request(Merge Request)를 통해서만 코드를 병합한다.
- `develop`에는 항상 배포 가능한 상태의 코드가 있어야 한다.
- `main`에는 항상 배포된 상태의 코드가 있어야 한다.
- `main` 브랜치는 배포 시 태그를 붙여 관리한다.
- `feature/*` 브랜치와 `main` 브랜치 모두 `develop` 브랜치에서 생성한다.

---

## 커밋 메시지 규칙

- 제목과 본문을 빈 행으로 구분한다.
- 제목은 50글자 이내로 제한한다.
- 제목의 첫 글자는 대문자로 작성한다.
- 제목 끝에는 마침표를 넣지 않는다.
- 제목은 명령문으로 사용하며 과거형을 사용하지 않는다.
- 본문의 각 행은 72글자 내로 제한한다.
- 어떻게 보다는 무엇과 왜를 설명한다.

### 커밋 메시지 구조

```bash
// Header, Body, Footer는 빈 행으로 구분한다.
타입(스코프): 주제(제목) // Header(헤더)

본문 // Body(바디)

바닥글 // Footer
Header는 필수이며 스코프는 생략 가능하다.
```

타입은 해당 커밋의 성격을 나타내며 아래 중 하나여야 한다.

| 타입 이름 |                         내용                          |
| :-------: | :---------------------------------------------------: |
|   feat    |                새로운 기능에 대한 커밋                |
|    fix    |                 버그 수정에 대한 커밋                 |
|   build   | 빌드 관련 파일 수정 / 모듈 설치 또는 삭제에 대한 커밋 |
|   chore   |             그 외 자잘한 수정에 대한 커밋             |
|    ci     |             ci 관련 설정 수정에 대한 커밋             |
|   docs    |                 문서 수정에 대한 커밋                 |
|   style   |         코드 스타일 혹은 포맷 등에 관한 커밋          |
| refactor  |               코드 리팩토링에 대한 커밋               |
|   test    |             테스트 코드 수정에 대한 커밋              |
|   perf    |                 성능 개선에 대한 커밋                 |

**Body**는 Header에서 표현할 수 없는 **상세한 내용**을 적는다.

Header에서 충분히 표현할 수 있다면 **생략 가능**하다.

**Footer**는 바닥글로 어떤 이슈에서 왔는지 같은 **참조 정보들을 추가**하는 용도로 사용한다.

예를 들어 특정 이슈를 참조하려면 Issues #1234 와 같이 작성하면 된다.

Footer는 **생략 가능**하다.

참고: [Github 이슈와 커밋 메시지 연결](https://www.lesstif.com/gitbook/github-push-pr-pull-request-issue-129008869.html)

작성 예시:

```bash
fix: Safari에서 모달을 띄웠을 때 스크롤 이슈 수정

모바일 사파리에서 Carousel 모달을 띄웠을 때,
모달 밖의 상하 스크롤이 움직이는 이슈 수정.

resolves: #1137
```

**중요: 커밋은 "1 Action, 1 Commit" 규칙을 지켜 작은 단위로 해주세요.**

---

## 이슈 규칙

이슈를 아이디어 공유, 피드백, 태스크, 버그 관리로 사용합니다.

이슈를 하나의 작업(task) 단위로 설정하고 사용하며 이슈 템플릿을 활용하여 작성합니다.

- 예시:

  제목: [기능] Todo 생성 기능 구현

  ```markdown
  ### 만들고자 하는 기능이 무엇인가요?

  ex) Todo 생성 기능

  ### 해당 기능을 구현하기 위해 할 일이 무엇인가요?

  1. [ ] Job1
  2. [ ] Job2
  3. [ ] Job3

  ### 예상 작업 시간

  ex) 3h
  ```

- 권장사항: 커밋 메시지에 이슈 번호를 추가하여 이슈와 커밋을 연결합니다.

---

### Pull Request(Merge Request) 규칙

- Pull Request(Merge Request)는 베이스 코드를 보호하고 코드 충돌을 막고 코드 리뷰를 통해 코드 품질을 높이기 위해 사용합니다.
- 리뷰어는 해당 코드의 오너, 혹은 해당 코드에 대해 잘 알고 있는 팀원과 QA Leader를 선택합니다.
- Pull Request(Merge Request)는 최소 2명 이상의 팀원의 승인이 있어야 Merge가 가능합니다.
- Pull Request(Merge Request)는 최대한 작은 단위로 나누어 작성합니다.
- Pull Request(Merge Request)는 작성 시 [Pull Request(Merge Request) 템플릿](/.gitlab/MERGE_REQUEST_TEMPLATE.md)을 활용합니다.

---

### 추가 고려사항

- IDE 통일: IntelliJ
- CI/CD: GitLab CI
- 코드 퀄리티
  - 테스트 코드: JUnit5
  - 코드 컨벤션: Google Java Style Guide
  - 코드 정적 분석: SonarQube
  - 코드 커버리지: JaCoCo
- 컨테이너화: Docker, Docker Compose
- Migration (Optional)
  - Maven -> Gradle
  - JSP -> React
  - JUnit4 -> JUnit5
  - Java8 -> Java21(17) (LTS version)
  - SpringBoot 2.3.4 -> SpringBoot 3.1.5
  - Spring 5.2.9 -> Spring 6.0.13
