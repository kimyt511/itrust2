# User Case Summary

## Implemented UCs (1~14)

### UC1

### UC2

### UC3

### UC4

### UC5

### UC6

### UC7

### UC8

### UC9
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
+ (Drug code) from list -> Edit mg, start date, end date, num of renewals.


Patient -> View prescriptions -> Shows list of current and past prescriptions. Table contains
Drug name, Dose (mg), Start date, end date, num of renewals.


Admin -> Add new NDC (National Drug Code) -> Enters (Num of drug code) + (Name of drug) + (Description)

### UC10
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

### UC11

### UC12

### UC13

### UC14

## UCs to implement (15~22)

### UC15

### UC16

### UC17

### UC18

### UC19: Food Diary
- 환자가 직접 자신이 먹은 식단정보를 입력하고, 식단 정보를 보는 기능
- 환자는 자신껏만 추가하고 자신것만 볼수있음
- HCP는 원하는 환자의 식단 볼수는 있지만, 추가는 안됨
- diary 보이는 순서는 최신꺼부터 쭉
- 이 때 각 날짜별로 해당하는 식단들의 영양소 정보의 합을 display 해줘야됨

### UC20: Ophthalmology Appointment Requests
- 안과 진료/수술 예약 기능
- 2명의 HCP role을 추가 (optometrist, ophthalmologist)

- 예약 type도 좀 이상함
- main flow에는 general checkup, ophthalmology appointment, ophthalmology surgery가 있는데
- patient가 할수있는 타입에는 general checkup과 appointment밖에 없음

아니 이건 근데 설명이 좀 많이 부족함, 부족한 부분을 우리가 직접 채우라는 건가???

ophthamology: 안과
ophthalmologist: 안과 전문의
optometrist: 눈 healthcare 전문의?

### UC21

### UC22
