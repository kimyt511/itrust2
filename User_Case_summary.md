# User Case Summary

## Implemented UCs (1~14)

### UC1 User Functionality

### UC2 Authenticate Users

### UC3 Log Transactions

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
-Precondition: 환자와 HCP(Health Care Provider)는  iTrust2에 로그인하고 인증 되어야 한다. 

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
  - 날자: MM/DD/YYYY    2 digit/2 digit/ 4 digit
  - 시간: HH/MM [am/pm]   2 digit/ 2 digit

### UC8 Basic Health Metrics

-Precondition: HCP(Health Care Provider)는  iTrust2에 로그인하고 인증 되어야 한다. 
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
  - 키(inches):         xxx.x             소수점 한 자릿수까지, 0 이상.
  - 몸무게(pounds):     xxxx.x             소수점 한 자릿수까지, 0 이상.
  - 머리 둘레(inches):   xxx.x              소수점 한 자릿수까지, 0 이상.
  - 혈압(mmHg):      Systolic/Diastolic      정수, 3 자릿수
  - 콜레스테롤(mg/dL):    
    - HDL             0 ~ 90
    - LDL             0 ~ 600
    - triglyceride    100 ~ 600
  - 가족 흡연 여부:    1 / 2 / 3 /  중 선택
  - 본인 흡연 여부:   1 / 2 / 3 / 4 / 5 / 9 / 중 선택

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
- main flow: 1. 로그인하면 access log의 상위항목(최근 10개)이 display되어야 하고 다른 창에서 access   log(최근 순으로 모든 로그)를 볼 수 있어야 한다. user는 시작날짜, 종료날짜를 골라 log를 볼 수 있다. 입력이 잘 못된 경우, 이벤트를 표시하지 않고 다른 날짜를 선택할 수 있게 한다.
user는 
- 로그는 다음 항목 포함
   * Name of accessor
   * Role of accessor relative to the patient (if the logged event is for a patient)
   * Date and time of access

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
  - 날자: MM/DD/YYYY    2 digit/2 digit/ 4 digit
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

### UC21 Ophthalmologist Appointment

### UC22 Ophthalmology Surgeries
