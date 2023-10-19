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

### UC12 HCP Edit Demographics 

### UC13 View Access Logs

### UC14 Alert Users by Email

## UCs to implement (15~22)

### UC15 Emergency Health Records

### UC16 Personal Representatives

### UC17 Lab Procedures

### UC18 Vaccinations

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
