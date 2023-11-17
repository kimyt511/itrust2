## Full Meetings

### Meeting 1

**Date**: 2023-10-14

#### What we did:

- Introduced each team member.
- Discussed and understood the goal of iteration 1.
- Decided to review and summarize Use Cases (UCs) that are already implemented and to explore options for additional functionalities.
  - Assigned each member 2 or 3 UCs to read and summarize using 사다리타기 for distribution.

#### ToDo:

- Summarize all implemented and unimplemented UCs.
  - Each member is to write their [use-case summaries](use-cases/summary.md).
- Propose one new UC for consideration at the next meeting.

- **Next meeting**: 2023-10-20
  - Despite agreeing to meet twice a week in [team-contract](team.md#team-contract), we decided to meet only once a week during iteration 1 due to the upcoming mid-term exams.

### Meeting 2

**Date**: 2023-10-20

#### What we did:

- Reviewed the UCs read by each member.
  - Identified available functionalities and potential additions.
- Selected 5 UCs (UC15 to UC22) to implement after a thorough discussion.
  - Chose to focus on general functionalities rather than those specific to ophthalmology.
- Decided on a new UC proposed by our team by majority vote: a Rating system.

#### ToDo:

- Study for Midterm Exams.
- Develop details and specifications for UC15 to UC19 and the newly proposed UC.
- Independently set up and run the `iTrust2` project locally and verify each UC.

- **Next meeting**: 2023-10-26

### Meeting 3

**Date**: 2023-10-26

#### What we did:

- Chose specialization in `Front-end` or `Back-end` for each team member.
- Formed three sub-teams and allocated two UCs to each for implementation.

| Team 1               | Team 2               | Team 3               |
| -------------------- | -------------------- | -------------------- |
| 강재현 (`Front-end`) | 김태헌 (`Front-end`) | 이호재 (`Front-end`) |
| 김성환 (`Front-end`) | 한철원 (`Back-end`)  | 홍현기 (`Front-end`) |
| 유지민 (`Back-end`)  | 최원재 (`Back-end`)  | 반석 (`Back-end`)    |
|                      |                      | 김윤태 (`Back-end`)  |

    Team 1: Implement UC16 & UC19.
    Team 2: Implement UC15 & UC18.
    Team 3: Implement UC17 & the new UC.

- Prepared for the iteration 1 presentation, selecting presenters and content.

  - Presenters: 강재현, 김태헌, 이호재.
  - Content to include: team introductions, summaries of selected UCs and User Stories, the newly proposed UC, and a demo of iTrust2 if time permits.

- Created a shared Google Doc to compile User Stories and the proposed Use Case.
  - Agreed upon a standard format for User Stories:
    - "As a [role], I want [feature], so that [benefit]."
  - Began documenting the basic components of our UCs in the Google Doc.

#### ToDo:

- Each sub-team to complete User Stories for assigned UCs by 10/29 at 10PM.
  - Team 3 to finish the newly proposed UC.
- Prepare for the presentation before 10/31.

### Meeting 4

**Date**: 2023-10-30

#### What we did:

- Finalized and formatted UC23.md to match the style of existing UCs.
- Organized user stories for each team in the corresponding User_Story.md files.
- Developed a Google Slides presentation to showcase our work during iteration 1.

### Meeting 5

**Date**: 2023-11-01

#### What we did:

- Discussed collaboration strategies led by 원재.
  - Decided to manage all code implementations in the 'develop' branch.
  - Shared the ERD, detailed coding guidelines, and cooperative rules to facilitate easier collaboration.
- Concluded the preparation for our presentation by reviewing and providing feedback.

### Meeting 6

**Date**: 2023-11-05

#### Discussion:

- Allocate leader roles for Iteration 2.
- Set initial deadline for UC implementations.
- Discuss about using AWS RDS for development & testing DB. 

### Meeting 7

**Date**: 2023-11-10

#### Discussion:

- Discussed the progress of each team.
  - Team 1: UC16 WIP, will be done by 11/12.
  - Team 2: UC15, UC18 WIP, will be done by 11/12.
  - Team 3: UC17 WIP, will be done by 11/12.
- Discussed about build tool migration from Maven to Gradle.
  - Decided to migrate to Gradle.
  - Dicussed about the migration plan.
  - Troubleshooted the build error.

### Meeting 8

**Date**: 2023-11-17

#### Discussion:

- Discuss the progress of each team
  - Team 1: UC19 WIP
  - Team 2: UC18 WIP
  - Team 3: UC23 WIP
- Discuss about the contents for upcoming presentation of Iteration 2
  - Include implemented features
  - Some details about migrations from original source
- Future plans for Iteration 3
  - Focus on unit testing
  - Features will be merged to `develop` branch after test coverage of 80% or above.

---

## Team 1 Meetings

### Meeting 1

**Date**: 2023.11.8

- UC16 & UC19 Frontend - 김성환, 강재현
- UC16 & UC19 Backend - 유지민
- Generate new branch 16, 19

### Meeting 2
**Date**: 2023.11.11

- Problem solved: SQL connection
- Announcement by team leader, all uppercases be unified to lowercases
- Recognition of each person's role
- Front-end design decisions briefly
- Share API format

### Meeting 3
**Date**: 2023.11.12

- Front-end UC16 design decisions
- Share declare/ undeclare API function

### Meeting 4
**Date**: 2023.11.14
- Front-end UC16 function decisions

### Meeting 5
**Date**: 2023.11.16
- Discuss minor details about UC16 API endpoints
- Backend for UC16 completed
- TODO: Discuss about UC19

---

## Team 2 Meetings

### Meeting 1

**Date**: 2023.11.5

- UC15 & UC18 Frontend - 김태헌
- UC18 Backend - 최원재
- UC15 Backend - 한철원

---

# Team 3 Meetings

### Meeting 1
**Date**: 2023-11-07

What we did:
- Understanding of UC17
- Understanding of what frontend and backend will do

Todo:
 - backend -> make Loinc and Procedure Units
 - Frontend -> make Procedure template for admin

### Meeting 2
**Date**: 2023-11-09
What we did:
- Create, edit, delete Loinc code by Admin

Todo:
- Frontend -> Create, edit, delete Procedure by HCP
- Backend -> view, edit API for Labtech, view API for patient
