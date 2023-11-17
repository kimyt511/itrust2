# Use Case Summary

## Implemented UCs (1~14)

### UC1 User Functionality

- Admin이 user를 create, view, update, delete하는 기능에 대한 설명.
- user create시 입력해야하는 정보는 user name, password, confirm password, the role이고 등록하는 role의 종류는 다음과 같다.
- - Patient
  - Health Care Provider (HCP)
  - Optometrist HCP
  - Ophthalmologist HCP
  - Admin
  - Emergency Responder (ER)
  - Lab Tech
- 정해진 data 틀에 맞지 않거나, password가 일치하지 않을 경우 err 발생.
- user name, password, confirm password는 모두 6~20 character data format 가짐.
- create와 delete 시 success 메시지가 display되고 log가 기록된다.

### UC2 Authenticate Users

- UC1에서 등록된 유저가 user name과 password를 통해 시스템에서 역할에 맞는 entry를 받음.
  3번 실패시 60분동안 세션 잠금, 특정 IP주소에서 최근 6번 시도가 실패할 경우 해당 IP에 대한 어떤 유저에 대해 60분 차단.
  차단 이후에 user는 3번, ip는 6번의 기회 받음.
  최근 24시간 안에 user나 ip가 3번 잠기면 해당 계정또는 IP는 system으로부터 ban되며 재인증을 받아야함.
- 로그인 실패시 mistake에 대한 메시지창 안내
- 세션은 10분 이상 비활성화상태이면 terminated. => 인증이 초기화되어야 함.
- 인증 성공시, role에 따른 personalized homepage로 이동, user logout 또는 app 종료시 세션 종료.

### UC3 Log Transactions

- patient 개인정보의 중요성에 따라 patient data에 대한 부적절한 access에 대한 log기록은 매우 중요하다.
  creates, views, edits, or deletes information, Login failures, valid authentication, and log outs 까지
  모두 log 기록 대상이다.
  트랜잭션 1-99는 use case에 대한 접근을 제외한 system과 관련한 log event가 기록되는 곳이고, 이후부터는 100개 단위로 use case에 대한
  log가 기록된다.
  log에는 로그인한 user의 MID와 접근되고 있는 user의 MID, 트랜잭션타입, 현재 시간이 기록된다.

### UC4 Demographics

- user가 개인정보 기록을 열람하고 편집할 수 있다.
- HCP는 본인의 정보 뿐 아니라 환자의 정보도 열람할 수 있는데, 환자의 정보를 볼 때에는 환자의 MID가 필요

### UC5 Hospitals

- administrator가 병원에 대한 정보를 등록, 편집, 삭제

### UC6 Appointments

- 환자가 진료 예약을 신청할 수 있음
- 환자는 자신의 진료 예약 신청을 열람, 삭제할 수 있다
- HCP는 자신에게 온 진료 예약을 승인, 거부할수 있다. 이 경우에 진료 예약은 대기열에서 사라진다.
- HCP는 승인된 진료 예약들을 열람할 수 있다.

### UC7 Office Visit

-Precondition: 환자와 HCP(Health Care Provider)는 iTrust2에 로그인하고 인증 되어야 한다.

- 환자는 자신의 진료기록을 볼 수 있다.

  - 환자는 자신의 진료기록을 **수정할 수 없다**.

- HCP는 환자의 진료내용을 작성할 수 있다.

  - 작성할 항목: 예약여부, 메모, 환자 신원정보, 진료 사유, 방문한 병원 이름, 날짜, 일시.
  - 작성 후 제출 버틀을 눌러야하며, 제출 성공 시 메세지가 뜬다.
  - 작성 실패 시, 오류 발생 항목에 정확한 형식을 입력하라는 팝업 문구가 뜬다

- HCP는 환자에게 추가적인 정보를 제공할 수 있다.

  - General Checkup (전반적인 건강 정보)
    - 기본 건강 지표 (UC 8)
  - 안과 진료 내역 (UC 21)
  - 안과 수술 내역 (UC 22)

- 진료 내용 데이터 작성 형식
  - 메모: 500자 이내
  - 날자: MM/DD/YYYY 2 digit/2 digit/ 4 digit
  - 시간: HH/MM [am/pm] 2 digit/ 2 digit

### UC8 Basic Health Metrics

-Precondition: HCP(Health Care Provider)는 iTrust2에 로그인하고 인증 되어야 한다.
HCP가 환자의 진료내역을 작성하기 시작했다.

- 환자 나이에 따라 3가지 다른 형식의 문서를 사용한다. 만나이를 기준으로 하며 각 문서의 세부 항목은 아래와 같다.
  - 3세 이하
    - 키, 몸무게, 머리 둘레, 가족 흡연 여부
  - 3세 ~ 12세
    - 키, 몸무게, 혈압, 가족 흡연 여부
  - 12세 이상
    - 키, 몸무게, 혈압, 가족 흡연 여부, 본인 흡연 여부, HDL 콜레스테롤 수치, LDL 콜레스텔롤 수치, 중성지방 수치
- 가족 흡연 여부는 아래 옵션 중에서 선택한다
  - 1. Non-smoking
  - 2. Outdoor-smoking
  - 3. indoor-smoking
- 본인 흡연 여부는 아래 옵션 중에서 선택한다

  - 1. Never smoker
  - 2. Former smoker
  - 3. Current some days smoker
  - 4. Current every day smoker
  - 5. Smoker, current status unknown
  - 9. unknown, if never smoked

- 문서 작성 성공 시 성공 메시지가 뜬다.
- 문서 작성 실패 시 틀린 항목에 정확한 형식을 입력하라는 메시지가 뜬다.

- 진료 내용 데이터 작성 형식
  - 키(inches): xxx.x 소수점 한 자릿수까지, 0 이상.
  - 몸무게(pounds): xxxx.x 소수점 한 자릿수까지, 0 이상.
  - 머리 둘레(inches): xxx.x 소수점 한 자릿수까지, 0 이상.
  - 혈압(mmHg): Systolic/Diastolic 정수, 3 자릿수
  - 콜레스테롤(mg/dL):
    - HDL 0 ~ 90
    - LDL 0 ~ 600
    - triglyceride 100 ~ 600
  - 가족 흡연 여부: 1 / 2 / 3 / 중 선택
  - 본인 흡연 여부: 1 / 2 / 3 / 4 / 5 / 9 / 중 선택

### UC9 Prescriptions

- UC9 Summary -
  Pre-requirements: UC7 must be implemented ( Documented office hour visit )

UC9 is connected to UC7 as the documented office visit must be present in order
to add a new prescription to the office visit. This user case is given to register the prescriptions
needed for the patient after their office visit.

==HCP can add a new prescription to the office visit (including drug name, mg, start data, end date, renewals)
==HCP can edit the prescription (drug name, mg, start date, end date, renewals)
==HCP can delete the prescription.

==ADMIN can add a new drug to the list of all drugs categorized. It should include name, code that consists
of 4 digits, a dash ( - ), four digits, a dash ( - ), and two more digits (Ex. 1234-5678-90)
It has to contain name, code and description ( Ex. TestDrug, 1234-5678-90, "Metaphentamine")
==ADMIN can also edit the drug of delete and exisitng drug.

==PATIENT can view table containing current and past prescriptions. THis includes all details such as
drug name, mg, start date, end date, renewals.

Ex:

///// Office Visit + Description + Basic Health Metrics ///// Enter a new prescription -> Select (Name of drug)

- (Drug code) from list -> Edit mg, start date, end date, num of renewals.

Patient -> View prescriptions -> Shows list of current and past prescriptions. Table contains
Drug name, Dose (mg), Start date, end date, num of renewals.

Admin -> Add new NDC (National Drug Code) -> Enters (Num of drug code) + (Name of drug) + (Description)

### UC10 Diagnoses

- UC10 Summary -
  Pre-requirements: UC7 and UC8 must be implemented ( Documented office hour visit and Basic Health Metrics )

This user case grants HCP to record a diagnosis of a patient during an office visit. Patients can
view their current and past diagnoses along with the data associated with the diagnosis.

==ADMIN can update list of diagnoses in the system by adding or removing diagnoses.
==ADMIN can add and delete diagnosis. If add, then admin enters the ICD-10 code and description of the diagnosis.

==HCP select correct diagnoses from menu of diagnoses options labeled with code ICD-10 and descriptions.
==HCP must enter information about the diagnosis (date of office visit, HCP name, and additional notes)

==PATIENT selects to view their diagnoses and data. Cannot edit.

Ex:

///// Office visit + Description + Basic Health Metrics ///// Dr. Vang diagnoses (diagnosis) associated with
ICD-10 code (ICD-10 code) with (descriptions regarding diagnosis)

Patient -> Check diagnosis -> Views past diagnoses as her most recent diagnosis. Includes date of diagnosis
and HCP who perfomed the diagnosis.

ADMIN -> List of diagnoses -> Add new diagnosis -> Enter K35 (ICD-10 code) + "Acute appendicitis" (Description)

### UC11 Password Functionality

- 유저가 자신의 비밀번호를 변경할 수 있다.
- 그 유저가 자신의 비밀번호를 아는 경우:
  - 그 유저가 동일한 비밀번호로 변경 시도할 경우 에러를 출력한다.
  - 그 유저의 "새로운 비번"과 "비번 확인"란이 다를 경우 에러를 출력한다.
- 그 유저가 자신의 비밀번호를 모르는 경우:
  - 그 유저는 본인의 유저 네임을 입력하고, 등록되지 않은 이름이라면 에러를 출력한다.
  - 그 후 그 유저가 등록한 이메일로 복구 비번을 보내주는데, 등록한 이메일이 없다면 에러를 출력하고, 복구 비번이 "만료"된다면 오류를 출력한다.

### UC12 HCP Edit Demographics

- (Preconditions) 이 유저는 등록된 HCP이다.
- HCP는 등록된 환자들 중 선택하여 세부 정보? (demographics)를 편집하도록 한다.
  - 만약 입력한 정보가 [UC4](#uc4-demographics)에 있는 형식에 맞지 않으면 에러를 출력한다.
  - 저장하지 않고 다른 유저의 demographics를 수정하려 하면 확인 메시지를 보낸다.

### UC13 View Access Logs

- Precondition: 시스템에 등록된 유저 + 인증한 유저여야한다. (uc1,2)
- main flow: 1. 로그인하면 access log의 상위항목(최근 10개)이 display되어야 하고 다른 창에서 access log(최근 순으로 모든 로그)를 볼 수 있어야 한다. user는 시작날짜, 종료날짜를 골라 log를 볼 수 있다. 입력이 잘 못된 경우, 이벤트를 표시하지 않고 다른 날짜를 선택할 수 있게 한다.
  user는
- 로그는 다음 항목 포함
  - Name of accessor
  - Role of accessor relative to the patient (if the logged event is for a patient)
  - Date and time of access

### UC14 Alert Users by Email

- Precondition: email alert가 필요한 상황이 발생
- main flow:
  1. 비밀번호 변경 혹은 리셋 시 email alert 발생 -> MID를 포함한 이메일 발송
  2. 요청에 관련된 HCP에 의해 예약 요청 상태가 변경되었으며, 환자에게 예약 상태를 알리는 이메일 발송
  3. 일정 로그인 시도 실패 시, 계정이 잠기고 이메일 발송
     모든 이메일은 application에서 추적, 로그 남음

## UCs to implement (15~22)

### UC15 Emergency Health Records

- HCP와 Emergency Responder는 환자의 이름이나 MID로 환자를 검색해서 환자의 Emergency Health Records를 볼 수 있음.

- Searching

  - 환자의 성, 이름으로 검색 (혹은 그것의 substring)
  - 환자의 username/MID로 검색 (혹은 그것의 substring)
  - 검색한 substring을 포함하는 이름/MID를 가진 환자들의 리스트가 표시됨
  - 표시된 리스트중 특정 환자를 선택하여 Emergency Health Records 페이지를 불러옴.
  - 검색한 substring을 포함하는 환자가 없거나, 검색한 substring이 포맷에 맞지 않으면 환자 리스트가 표시되지 않음

- Emergency Health Records
  - 이름, 나이, 생일, 성별, 혈액형이 표시됨
  - 지난 60일간의 단기 건강검진의 진단 코드가 표시됨 (최근 것 부터)
  - 지난 90일간의 처방 기록이 표시됨 (최근 것 부터)

### UC16 Personal Representatives

- 환자는 다른 환자를 자신의 Personal Representatives(PR, 대리인)으로 지정하여 자신의 의료 기록 접근 권한을 줄 수 있음.

- PR의 등록과 해제

  - 환자는 자신의 대리인으로 등록된 사용자들과 자신이 대리인으로 등록된 사용자들의 리스트를 볼 수 있음.
  - 환자는 이름이나 MID로 다른 환자를 검색하여 자신의 대리인으로 등록할 수 있음.
  - 환자는 자신의 대리인으로 등록된 사용자를 대리인에서 해제할 수 있음.
  - 대리인으로 등록된 사용자가 특정 환자를 대리하기를 원하지 않는다면 그 환자의 대리인에서 해제할 수 있음.
  - HCP는 환자의 대리인을 등록할 수 있고, 특정 환자의 대리인 목록이나 그 환자가 대리인으로 등록된 환자들의 목록을 볼 수 있음.

- 대리인은 환자의 의료 기록, 진단, 예약 페이지를 확인할 수 있음.

### UC17 Lab Procedures

- **Admin**
  - Procedure 목록을 추가/편집/삭제함.
  - Procedure:
    - LOINC 코드 (#####-# 형식)
    - 이름 (~250자)
    - 구성요소 (~250자)
    - 속성 (~250자)
- **HCP**
  - ([Office visit](#uc7-office-visit)에다가) 환자를 위한 Procedure를 목록에서 선택할 수 있음.
    - Procedure를 수행할 Lab Tech를 배정
    - Priority(1~4)를 설정
    - Comment를 추가(~500자)
  - 배정해준 Procedure의 상태가 `1. Assigned`이면 Procedure를 삭제할 수 있음.
- **Lab Tech**
  - 자기한테 배정된 Procedure를 확인할 수 있음.
  - Procedure의 진행 상태/Comment를 변경할 수 있음.
    1. Assigned(초기)
    2. In-Progress
    3. Completed
  - 혹은 다른 Lab Tech한테 떠넘기기 가능.
- **Patient**
  - 자기한테 할당된 Procedure를 구경만 가능.

### UC18 Vaccinations

- **Admin**
  - 백신 목록을 추가/편집/삭제함.
  - 백신:
    - 이름 (~250자, 숫자 및 '-' 허용)
    - 약자 (~10자, 숫자 및 '-' 허용)
    - CPT 코드 (90### 형식)
    - 설명 (~500자)
- **HCP**
  - ([Office visit](#uc7-office-visit)에다가) 백신 목록을 보고 추가함 (0개 이상).
  - 환자 이름으로 검색해서 그 환자의 모든 백신 접종에 대한 기록을 열람할 수 있음.
- **Patient**
  - 환자는 보기만 할 수 있음.
  - 자신의 모든 백신 기록을 볼 수 있음.

### UC19 Food Diary

-Precondition: 환자와 HCP가 iTrust2에 로그인하고 인증 되어야 한다.

- 환자가 직접 자신이 먹은 식단정보를 입력하고, 식단 정보를 보는 기능
  - 환자는 자신껏만 추가하고 자신것만 볼수있음
- HCP는 원하는 환자의 식단 볼수는 있지만, 추가는 안됨
- diary 보이는 순서는 최신꺼부터 쭉
- 이 때 각 날짜별로 해당하는 식단들의 영양소 정보의 합을 display 해줘야됨
- Diary 데이터 형식(모든 항목이 Required):
  - 날자: MM/DD/YYYY 2 digit/2 digit/ 4 digit
  - 식사종류: “Breakfast”, “Lunch”, “Dinner”, or “Snack” 중 1개 (같은 식사종류가 같은 날에 여러개 있어도 됨)
  - 음식이름
  - 반찬개수
  - 칼로리 per 반찬
  - 나트륨(Sodium) per 반찬 (in Miligrams)
  - 지방, 탄수화물, 당, 식이섬유, 단백질 per 반찬 (in Grams)

### UC20 Ophthalmology Appointment Requests

- 용어정리:
  - ophthamology: 안과
  - ophthalmologist: 안과 전문의
  - optometrist: 눈 healthcare 전문의?
- 안과 진료/수술 예약 기능
  - 환자가 예약 요청/ 기존 요청 철회
  - HCP는 들어온 요청을 보고 수락 혹은 거절
- 2명의 HCP role을 추가 (optometrist, ophthalmologist)
- 각 HCP role별 안과관련 진료가능 항목:
  - Optometrist: general checkups / opthalmology office visits
  - Ophthalmologist: general checkups / opthalmology office visits / ophthalmology surgeries
  - other HCPS: general checkups /
- 예약 요청 데이터 형식:
  - HCP: HCP user의 이름
  - 날짜, 시간
  - Comments: Up to 50 characters
  - 종류: "General Checkup" 혹은 "ophthalmology appointment"
  - Name, Abbreviations, CPT Code
  - Comments: Up to 500 characters --> ??? 이게 왜 또있지

---

# UC21 - 안과 진료

## 21.1. 목표

iTrust2는 안과 의사와 검안사가 환자의 눈 건강을 검진하기 위한 예약을 설정하고, 이러한 예약을 관리할 수 있는 기능을 제공해야 합니다.

## 21.2. 전제 조건

iTrust2 사용자는 안과 의사 또는 검안사로 iTrust2에 인증하였고, 그들의 환자 중 하나가 시스템에 존재해야 합니다.

## 21.3. 프로그램 기능 정리

안과 의사와 검안사는 환자의 눈 건강을 검진하기 위한 예약을 설정할 수 있습니다. 이러한 예약은 환자의 기본 건강 지표를 선택적으로 포함할 수 있습니다.

**예약 개요 제출 시 안과 의사 또는 검안사가 입력해야 하는 항목들:**

1. **시력 검사 결과:**

   - **오른쪽 눈 (OD):** 20~200 사이의 정수 값
   - **왼쪽 눈 (OS):** 20~200 사이의 정수 값

2. **구면:**

   - **오른쪽 눈 (OD):** 소수점 두 자리까지의 부동 소수점 숫자
   - **왼쪽 눈 (OS):** 소수점 두 자리까지의 부동 소수점 숫자

3. **실린더 (선택 사항):**

   - **오른쪽 눈 (OD):** 값 (만약 입력된 경우)
   - **왼쪽 눈 (OS):** 값 (만약 입력된 경우)

4. **축:**

   - **오른쪽 눈 (OD):** 실린더가 입력된 경우 1~180도 사이의 값
   - **왼쪽 눈 (OS):** 실린더가 입력된 경우 1~180도 사이의 값

5. **방문 메모:** 최대 500자까지의 일반적인 방문 관련 메모

6. **진단:** 백내장, 연령 관련 황반변성, 사시, 녹내장 중 선택

예약 후 안과 의사와 검안사는 방문의 진단 내용을 확인하고 필요한 경우 편집할 수 있습니다.

HCP (Health Care Provider, 즉, 의료 서비스 제공자)가 유효하지 않은 시력, 구면, 축, 실린더 값을 입력하려고 할 때, 시스템은 HCP에게 입력 값이 유효하지 않다는 알림 메시지를 표시합니다. HCP는 해당 알림을 확인하고 올바른 값을 입력하도록 유도됩니다.

## 21.4. 로깅

| Transaction Code | Verbose Description                                             | Logged In MID | Secondary MID | Transaction Type | Patient Viewable |
| ---------------- | --------------------------------------------------------------- | ------------- | ------------- | ---------------- | ---------------- |
| 2100             | Logging entry for submitting an ophthalmology appointment.      | HCP's MID     | User's MID    | Create           | No               |
| 2102             | Logging entry for editing an ophthalmology appointment.         | HCP's MID     | User's MID    | Edit             | No               |
| 2101             | Logging entry for doctor viewing an ophthalmology appointment.  | HCP's MID     | User's MID    | View             | No               |
| 2110             | Logging entry for patient viewing an ophthalmology appointment. | Patient's MID | NA            | View             | No               |
| 2111             | Logging entry for deleting an ophthalmology appointment.        | Patient's MID | NA            | View             | No               |

## 21.5. 데이터 형식

| Field                 | Format                                                                                    |
| --------------------- | ----------------------------------------------------------------------------------------- |
| Visual Acuity Results | Two 3-digit integers between 20 and 200, inclusive. Required                              |
| Sphere                | Positive or Negative floating point numbers up to two digits. Required                    |
| Cylinder              | Positive or Negative floating point numbers up to two digits.                             |
| Axis                  | Integer between 1 and 180, inclusive. Required if Cylinder exists.                        |
| Notes                 | General visit notes, up to 500 characters.                                                |
| Diagnoses             | Any of the following: cataracts, age-related macular degeneration’, amblyopia or glaucoma |

## 21.6. 프로그램 시나리오

### 21.6.1. 안과 예약 승인

iTrust2 시스템에서 안과 예약을 수락하는 경우입니다.

#### 흐름:

1. **인증:**

   - 안과 의사 (HCP)는 자신의 자격 증명을 사용하여 iTrust2 시스템에 로그인합니다.

2. **탐색:**

   - 인증이 완료되면 "안과 의사 예약 및 요청 보기"라는 전용 페이지 또는 대시보드로 이동합니다.

3. **예약 선택:**

   - HCP는 예약 목록/테이블을 볼 수 있습니다.
   - 테이블의 각 행은 환자 이름, 날짜/시간, 현재 상태와 같은 예약에 대한 세부 정보를 나타냅니다.
   - 각 예약 옆에는 "승인", "거부" 및 "보류 중" 또는 기타 상태와 같은 옵션이 있는 드롭다운 메뉴가 있습니다.

4. **예약 승인:**

   - HCP는 승인하려는 특정 예약의 드롭다운 메뉴를 클릭하고 "승인"을 선택합니다.

5. **데이터베이스 업데이트:**

   - 시스템은 데이터베이스의 `OphthalmologyAppointments` 테이블을 업데이트하여 선택한 예약의 `status`를 "승인됨"으로 변경합니다.

6. **UI 업데이트:**
   - 승인 후 시스템은 목록을 새로 고침하거나 예약을 "보류 중" 섹션에서 "승인됨" 섹션으로 이동해야 합니다(예약이 UI에서 이런 식으로 분류된 경우).

#### 구현 단계:

1. "안과 의사 예약 및 요청 보기"라는 페이지 또는 대시보드를 생성합니다.
2. `status`가 "보류 중"인 `OphthalmologyAppointments` 테이블에서 보류 중인 예약을 가져옵니다.
3. 이 예약을 페이지에 목록/테이블 형식으로 표시합니다.
4. 드롭다운이 "승인"으로 변경되면 데이터베이스의 상태를 업데이트하는 API 호출이 이루어지도록 기능을 추가합니다.
5. 페이지를 새로 고치거나 UI를 동적으로 업데이트하여 상태 변경을 반영합니다.

### 21.6.2. 안과 예약 거부

iTrust2 시스템에서 안과 예약을 거부하는 경우입니다.

#### 흐름:

1. **탐색:** 시나리오 1과 동일합니다.

2. **예약 거부:**

   - HCP는 특정 예약을 거부하려고 드롭다운 메뉴에서 "거부"를 선택합니다.

3. **데이터베이스 업데이트:**

   - 시스템은 `OphthalmologyAppointments` 테이블을 업데이트하여 선택된 예약의 `status`를 "거부됨"으로 변경합니다.

4. **UI 업데이트:**
   - 예약은 목록에서 제거되거나 "거부됨" 섹션으로 이동됩니다.

#### 구현 단계:

1. 시나리오 1에서 이미 생성한 페이지를 사용합니다.
2. 드롭다운이 "거부"로 변경되면 데이터베이스의 상태를 업데이트하는 API 호출이 이루어지도록 기능을 추가합니다.
3. 페이지를 새로 고치거나 UI를 동적으로 업데이트하여 상태 변경을 반영합니다.

### 21.6.3. 안과 예약 성공

iTrust2 시스템에서 올바른 안과 예약을 했을 경우입니다.

#### 흐름:

1. **환자 예약:**

   - 환자는 시스템에 로그인합니다.
   - 환자는 예약 페이지로 이동하여 안과 의사를 선택하고 예약을 원하는 날짜와 시간을 지정합니다.
   - 이 정보는 "보류 중" 상태로 `OphthalmologyAppointments` 테이블에 저장됩니다.

2. **안과 의사 보기:**
   - 안과 의사 (HCP)는 로그인하고 "안과 의사 예약 및 요청 보기" 페이지로 이동합니다.
   - HCP는 예약 목록을 볼 수 있으며 환자가 만든 정확한 날짜와 시간을 가진 예약을 볼 수 있습니다.

#### 구현 단계:

1. 환자가 안과 의사, 날짜, 시간을 선택할 수 있는 예약 페이지를 만듭니다.
2. 제출 시 "보류 중" 상태로 이 데이터를 `OphthalmologyAppointments` 테이블에 저장합니다.
3. HCP의 "안과 의사 예약 및 요청 보기" 페이지에서 데이터베이스에서 예약을 가져와 목록/테이블 형식으로 표시하며 올바른 날짜와 시간이 표시되도록 합니다.

## 21.7. 개발 주요 항목

프로젝트를 개발하기 위해 필요한 주요 항목들입니다.

1. **사용자 인증 시스템**:

   - 로그인 및 로그아웃 기능
   - 사용자 권한 확인 기능 (예: 환자, 안과 의사, 안과 전문의 등)
   - 사용자 정보의 CRUD (생성, 읽기, 업데이트, 삭제) 작업 지원

2. **안과 진료 예약 기능**:

   - 예약 생성: 사용자는 안과 진료 예약을 생성할 수 있어야 합니다.
   - 예약 확인: 의사 및 환자는 예약된 내용을 확인할 수 있어야 합니다.
   - 예약 변경: 환자나 의사가 예약을 수정할 수 있어야 합니다.
   - 예약 취소: 환자나 의사가 예약을 취소할 수 있어야 합니다.

3. **안과 진료 기록 관리 기능**:

   - 눈 건강 검진 기록 생성: 시력 결과, 구면, 실린더, 축, 메모, 진단 등의 정보를 포함하여 기록을 생성합니다.
   - 기록 확인: 의사 및 환자는 특정 진료의 기록을 확인할 수 있습니다.
   - 기록 수정: 의사는 필요한 경우 기록을 수정할 수 있습니다.
   - 기록 삭제: 의사는 기록을 삭제할 수 있어야 합니다. 삭제 로그도 생성됩니다.

4. **데이터 유효성 검증 기능**:

   - 예약, 기록 작성 및 수정 시 입력 데이터의 유효성을 검사합니다. (예: 시력 값의 범위, 구면 값의 형식 등)

5. **로깅 시스템**:

   - 안과 진료와 관련된 모든 중요한 작업 (예: 예약 생성, 기록 수정 등)은 로그로 기록됩니다.
   - 로그에는 거래 코드, 설명, 로그인한 사용자의 MID, 보조 MID, 거래 유형, 환자가 볼 수 있는지 여부 등의 정보가 포함됩니다.

6. **데이터베이스 관리**:

   - 사용자, 예약, 진료 기록 등의 정보를 저장하고 관리하기 위한 데이터베이스 시스템이 필요합니다.
   - 데이터베이스는 데이터의 안정성, 효율성 및 보안을 보장해야 합니다.

7. **사용자 인터페이스**:

   - **대시보드**: 사용자는 대시보드를 통해 현재 예약 상태, 최근 진료 기록 등의 요약 정보를 한눈에 볼 수 있어야 합니다.
   - **설계**: 직관적이며 사용자 친화적인 디자인을 통해 모든 사용자가 쉽게 서비스를 이용할 수 있어야 합니다.
   - **응답성**: 다양한 디바이스 (예: 휴대폰, 태블릿, 데스크톱)에서 웹사이트가 올바르게 동작하고 보여져야 합니다.
   - **알림**: 예약, 진료 기록 업데이트 등 중요한 활동이 있을 때 사용자에게 알림을 제공합니다.

8. **테스트**:

   - **유닛 테스트**: 개별 기능이 올바르게 동작하는지 검증하기 위한 테스트를 진행합니다.
   - **통합 테스트**: 여러 기능이 함께 작동할 때 발생할 수 있는 문제를 찾아내기 위한 테스트를 진행합니다.
   - **성능 테스트**: 시스템이 트래픽이 많을 때도 안정적으로 동작하는지 확인합니다.
   - **보안 테스트**: 데이터 누출, 해킹 등의 보안 위협에 대비한 테스트를 진행합니다.

9. **문서화**:

   - **사용자 매뉴얼**: 사용자가 시스템을 이해하고 사용하는 데 도움을 주는 가이드를 제공합니다.
   - **개발 문서**: 시스템의 아키텍처, 데이터베이스 스키마, 기능별 로직 등의 세부 사항을 기록합니다. 이는 추후 유지보수나 확장 시 참고 자료로 사용됩니다.
   - **API 문서**: 시스템과 연동하거나 통합하기 위한 외부 개발자를 위한 API 문서를 제공합니다.

10. **프로젝트 배포**:

    - **서버 설정**: 웹 서버, 데이터베이스 서버 등 필요한 서버 환경을 설정하고 최적화합니다.
    - **자동화**: 배포 프로세스를 자동화하여 새로운 버전의 소프트웨어가 빠르게 그리고 안전하게 생산 환경에 적용될 수 있도록 합니다.
    - **모니터링**: 서비스의 상태, 트래픽, 오류 등을 실시간으로 모니터링하며, 문제가 발생하면 즉시 알림을 받을 수 있는 시스템을 구축합니다.

## 21.8. 데이터베이스 스키마

프로젝트에 필요한 데이터베이스 스키마입니다.

### 테이블명: `Patients`

1. **환자 ID (`patient_id`)**:

   - 데이터 타입: INTEGER (or BIGINT)
   - 기본 키, 자동 증가 (AUTO_INCREMENT)
   - 환자를 고유하게 식별하는 데 사용됩니다.

2. **이름 (`name`)**:

   - 데이터 타입: VARCHAR(100)
   - 환자의 이름을 저장합니다.

3. **생년월일 (`birthdate`)**:

   - 데이터 타입: DATE
   - 환자의 생년월일을 저장합니다.

4. **성별 (`gender`)**:

   - 데이터 타입: ENUM('Male', 'Female', 'Other')
   - 환자의 성별을 저장합니다.

5. **주소 (`address`)**:

   - 데이터 타입: VARCHAR(200)
   - 환자의 주소를 저장합니다.

6. **연락처 (`contact_number`)**:
   - 데이터 타입: VARCHAR(15)
   - 환자의 연락처를 저장합니다.

### 테이블명: `HealthCareProviders`

1. **의사 ID (`hcp_id`)**:

   - 데이터 타입: INTEGER (or BIGINT)
   - 기본 키, 자동 증가 (AUTO_INCREMENT)
   - 의사 또는 검안사를 고유하게 식별하는 데 사용됩니다.

2. **이름 (`name`)**:

   - 데이터 타입: VARCHAR(100)
   - 의사 또는 검안사의 이름을 저장합니다.

3. **전문 분야 (`specialty`)**:

   - 데이터 타입: VARCHAR(100)
   - 의사 또는 검안사의 전문 분야를 저장합니다.

4. **연락처 (`contact_number`)**:

   - 데이터 타입: VARCHAR(15)
   - 의사 또는 검안사의 연락처를 저장합니다.

5. **근무지 (`hospital_or_clinic`)**:
   - 데이터 타입: VARCHAR(150)
   - 의사 또는 검안사의 근무지를 저장합니다.

### 테이블명: `OphthalmologyAppointments`

1. **예약 ID (`appointment_id`)**:

   - 데이터 타입: INTEGER (or BIGINT)
   - 기본 키, 자동 증가 (AUTO_INCREMENT)
   - 예약을 고유하게 식별하는 데 사용됩니다.

2. **환자 ID (`patient_id`)**:

   - 데이터 타입: INTEGER (or BIGINT)
   - 외래 키 (Foreign Key)로서 `Patients` 테이블을 참조합니다.
   - 예약한 환자를 식별하는 데 사용됩니다.

3. **의사 ID (`hcp_id`)**:

   - 데이터 타입: INTEGER (or BIGINT)
   - 외래 키 (Foreign Key)로서 `HealthCareProviders` 테이블을 참조합니다.
   - 환자가 예약한 의사를 식별하는 데 사용됩니다.

4. **예약 날짜 및 시간 (`appointment_datetime`)**:

   - 데이터 타입: DATETIME
   - 환자가 예약한 날짜 및 시간을 저장합니다.

5. **예약 상태 (`status`)**:

   - 데이터 타입: ENUM('Pending', 'Approved', 'Declined', 'Completed', 'Cancelled')
   - 예약의 현재 상태를 나타냅니다.

6. **예약 메모 (`notes`)**:

   - 데이터 타입: VARCHAR(500)
   - 환자 또는 의사가 추가할 수 있는 예약 관련 메모를 저장합니다. (예: 특별한 요구사항)

7. **예약 생성일 (`created_at`)**:

   - 데이터 타입: DATETIME
   - 예약이 생성된 날짜 및 시간을 저장합니다.

8. **예약 수정일 (`updated_at`)**:
   - 데이터 타입: DATETIME
   - 예약의 마지막 수정 날짜 및 시간을 저장합니다.

### 테이블명: `OphthalmologyResults`

1. **결과 ID (`result_id`)**:

   - 데이터 타입: INTEGER (or BIGINT)
   - 기본 키, 자동 증가 (AUTO_INCREMENT)
   - 진료 결과를 고유하게 식별하는 데 사용됩니다.

2. **예약 ID (`appointment_id`)**:

   - 데이터 타입: INTEGER (or BIGINT)
   - 외래 키 (Foreign Key)로서 `OphthalmologyAppointments` 테이블을 참조합니다.
   - 해당 진료 결과와 관련된 예약을 식별하는 데 사용됩니다.

3. **오른쪽 눈 시력 (`vision_OD`)**:

   - 데이터 타입: INTEGER
   - 오른쪽 눈의 시력 결과를 저장합니다.

4. **왼쪽 눈 시력 (`vision_OS`)**:

   - 데이터 타입: INTEGER
   - 왼쪽 눈의 시력 결과를 저장합니다.

5. **오른쪽 눈 구면 (`sphere_OD`)**:

   - 데이터 타입: DECIMAL(5,2)
   - 오른쪽 눈의 구면 값을 저장합니다.

6. **왼쪽 눈 구면 (`sphere_OS`)**:

   - 데이터 타입: DECIMAL(5,2)
   - 왼쪽 눈의 구면 값을 저장합니다.

7. **오른쪽 눈 실린더 (`cylinder_OD`)**:

   - 데이터 타입: DECIMAL(5,2)
   - 오른쪽 눈의 실린더 값을 저장합니다.

8. **왼쪽 눈 실린더 (`cylinder_OS`)**:

   - 데이터 타입: DECIMAL(5,2)
   - 왼쪽 눈의 실린더 값을 저장합니다.

9. **오른쪽 눈 축 (`axis_OD`)**:

   - 데이터 타입: INTEGER
   - 오른쪽 눈의 축 값을 저장합니다.

10. **왼쪽 눈 축 (`axis_OS`)**:

    - 데이터 타입: INTEGER
    - 왼쪽 눈의 축 값을 저장합니다.

11. **진단 결과 (`diagnosis`)**:
    - 데이터 타입: VARCHAR(300)
    - 안과 진료의 진단 결과를 저장합니다.

---

# UC22 - 안과 수술

## 22.1. 목표

iTrust2는 환자의 안과 수술 방문을 관리하고, 안과 의사가 수술에 대한 정보를 기록하고 수정할 수 있는 기능을 제공해야 합니다.

## 22.2. 전제 조건

iTrust2 사용자는 환자, HCP 또는 안과 의사로서 iTrust2에 인증되어야 합니다.

## 22.3. 프로그램 기능 정리

### 22.3.1. 주요 기능

- **안과 수술 방문 조회**: 환자, HCP, 안과 의사는 기록된 안과 수술 방문을 조회할 수 있어야 합니다.

- **안과 수술 방문 기록**: 안과 의사는 안과 수술 방문을 기록하며, 수술의 세부 사항 및 필요한 경우 기본 건강 지표를 포함시킬 수 있어야 합니다.

- **안과 수술 방문 수정**: 안과 의사만이 이전에 기록한 안과 수술 방문 정보를 편집할 수 있어야 합니다. 다른 사용자는 정보 편집 권한이 없어야 합니다.

### 22.3.2. 상세 기능 및 작동 원리

- **안과 수술 방문 조회**:

  - 환자는 자신의 안과 수술 기록을 조회할 때, 안과 의사가 기록한 모든 정보를 확인할 수 있습니다.
  - 이 때, 환자는 기록을 수정하거나 변경할 수 없어야 합니다.

- **안과 수술 방문 기록**:

  - 안과 의사는 환자의 이름, 방문 날짜 및 시간을 입력하여 안과 수술을 기록할 수 있습니다.
  - 기록 시, 다음과 같은 정보를 입력해야 합니다:
    - 날짜 (기본값: 현재 날짜)
    - 시력 검사 결과 (왼쪽 및 오른쪽 눈)
    - 구면, 원통, 축 (왼쪽 및 오른쪽 눈)
    - 수술 유형
    - 추가 메모
  - 정보를 모두 입력한 후, '수술 방문 제출' 버튼을 누르면 시스템은 올바른 형식으로 정보가 입력되었는지 확인합니다.

- **안과 수술 방문 수정**:
  - 안과 의사는 기존의 안과 수술 방문 정보를 수정하기 위해 환자의 이름, 방문 날짜 및 시간을 선택할 수 있습니다.
  - 수정 후, '수술 방문 수정' 버튼을 누르면 시스템은 올바른 형식으로 정보가 수정되었는지 확인합니다.

### 22.3.3. 예외 처리

- 안과 의사가 수술 정보를 입력하거나 수정할 때, 특정 데이터 필드의 입력 형식이 잘못된 경우 시스템은 적절한 메시지를 표시하며, 해당 데이터 필드의 형식을 수정하도록 안내해야 합니다.

## 22.4. 로깅

| Transaction Code | Verbose Description           | Logged in MID       | Secondary MID | Transaction Type | Patient Viewable |
| ---------------- | ----------------------------- | ------------------- | ------------- | ---------------- | ---------------- |
| 2201             | Ophthalmologist adds surgery  | Ophthalmologist HCP | Patient       | Create           | Yes              |
| 2202             | Ophthalmologist edits surgery | Ophthalmologist HCP | Patient       | Edit             | Yes              |
| 2203             | Ophthalmologist views surgery | Ophthalmologist HCP |               | View             | Yes              |
| 2204             | Patient views surgery         | Patient             |               | View             | Yes              |

## 22.5. 데이터 형식

| Field                 | Format                                                                        |
| --------------------- | ----------------------------------------------------------------------------- |
| Date                  | mm/dd/yyyy. Required                                                          |
| Visual Acuity Results | Two 3-digit integers between 20 and 200, inclusive. Required                  |
| Sphere                | Positive or Negative floating point numbers up to two digits. Required        |
| Cylinder              | Positive or Negative floating point numbers up to two digits.                 |
| Axis                  | Integer between 1 and 180, inclusive. Required if Cylinder exists             |
| Surgery Type          | One of “cataract surgery”, “laser surgery”, or “refractive surgery”. Required |
| Notes                 | Up to 500 characters                                                          |
