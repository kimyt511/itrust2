# User Story: Team2

UC15 & UC18

## UC16
Feature:
- HCP or Emergency Responder can access  the patient's Emergency Health Records by searching with their name or MID.

|1 | |
|------|-----------------------------------------------------------------------------------------------------------|
|User Story| As an HCP or Emergency Responder, I want to be able to search for a patient's Emergency Health Records by their name or MID(username), so that I can quickly access their critical health information in an emergency situation.|

| | |
|------|---|
|Given| HCP is authenticated user in iTrust2 and patient's name or MID|
|When| Navigate to EHR page |
|And| Type in partial or full name, or MID of a patient|
|Then| A list of matching patients appears|

|2 | |
|------|-----------------------------------------------------------------------------------------------------------|
|User Story| As an HCP or Emergency Responder, I want to see a clear message if no matching patients are found in the search results, so that I can quickly determine when a search query has no results.|

| | |
|------|---|
|Given| HCP is authenticated user in iTrust2 and patient's name or MID|
|When| Navigate to EHR page |
|And| Type in partial or full name, or MID of a patient|
|Then| Clear message there is no matching patient|
 
|3 | |
|------|-----------------------------------------------------------------------------------------------------------|
|User Story| As an HCP or Emergency Responder, I want to view a patient's Emergency Health Records after selecting them from the search results, so that I can access their name, age, date of birth, gender, blood type, recent diagnoses, and prescriptions for immediate medical assistance.|

| | |
|------|---|
|Given| HCP is authenticated user in iTrust2 and patient's name or MID|
|When| Navigate to EHR page with matched patients list |
|And| Xelect patient from the list|
|Then| EHR appears on the pop-up page|

## UC18
Feature:
- Administrator can manage the vaccine's information
- HCPs can assign vaccines at office visit and view assigned vaccines of patients
- Patients can view previous vaccinations

|1 | |
|------|-----------------------------------------------------------------------------------------------------------|
|User Story| As an administrator, I want to be able to add a new vaccine to the system, providing details such as the name, abbreviation, CPT code, and comments, so that HCPs can document patient vaccinations. |

| | |
|------|---|
|Given| The administrator is authenticated user in iTrust2|
|When|  Navigate to "Admin>Manage Vaccines" page|
|Then| A vaccine list is displayed and blanks to enter are shown|
|And| Fill in the blanks (name, abbreviation, CPT code, comments)|
|And| Click on "Submit" button|
|When| The formats are all in correct|
|Then| A confirmation message shows up.|
|Then| log "Administrator adds vaccine" is recorded.|
|When| Some formats are wrong|
|Then| An Error message is displayed.|

|2 | |
|------|-----------------------------------------------------------------------------------------------------------|
|User Story| As an administrator, I want to be able to edit an existing vaccine's information, such as its name, abbreviation, CPT code, or comments, so that I can keep the vaccine information up to date. |

| | |
|------|---|
|Given| The administrator is authenticated user in iTrust2|
|When|  Navigate to "Admin>Manage Vaccines" page|
|Then| A vaccine list is displayed and blanks to enter are shown|
|And| Click on "Edit" button next to the vaccine to edit|
|Then| The displayed vaccine information is editable, and "Save" and "Cancel" buttons are displayed |
|And| Edit some information and Click on "Save" button|
|When| The formats are all in correct|
|Then| A confirmation message shows up.|
|Then| log "Administrator edits vaccine" is recorded.|
|When| Some formats are wrong|
|Then| An Error message is displayed.|

|3 | |
|------|-----------------------------------------------------------------------------------------------------------|
|User Story| As an administrator, I want to be able to delete a vaccine from the system, ensuring that outdated or unnecessary vaccines can be removed. |

| | |
|------|---|
|Given| The administrator is authenticated user in iTrust2|
|When|  Navigate to "Admin>Manage Vaccines" page|
|Then| A vaccine list is displayed and blanks to enter are shown|
|And| Click on "Delete" button next to the vaccine to delete|
|Then| The vaccine gets deleted and a confirmation message displayed|
|Then| log "Administrator deletes vaccine" is recorded.|

|4 | |
|------|-----------------------------------------------------------------------------------------------------------|
|User Story| As an HCP, add one or more vaccinations to a patient's office visit record, so that the patient’s vaccination history is accurately documented. |

| | |
|------|---|
|Given| The HCP is authenticated user in iTrust2|
|When|  Navigate to "Patient>Document Office Visit" page|
|Then| An office visit page is displayed, and a section "Vaccination" is displayed|
|And| Click on vaccinated vaccines from the list of vaccines|
|And| Click on "Submit Office Visit" button|
|When| The formats are all in correct|
|Then| A confirmation message shows up.|
|Then| log "HCP adds vaccinations to patient record" is recorded.|
|When| Some formats are wrong|
|Then| An Error message is displayed.|

|5 | |
|------|-----------------------------------------------------------------------------------------------------------|
|User Story| As an HCP, I want to view a patient's office visit and see a list of all vaccinations administered during the visit, so that I can review the patient's recent vaccinations quickly. |

| | |
|------|---|
|Given| The HCP is authenticated user in iTrust2|
|When|  Navigate to "Edit>Edit Office Visit" page|
|Then| A list of patients is displayed |
|And| Click on a patient|
|Then| A list of date and time of previous office visits is displayed|
|And|Click on an office visit|
|Then|The recorded information in the office visit is displayed, including the vaccination information|
|And|Edit vaccination information|
|And|Click on "Edit Office Visit" button|
|When| The formats are all in correct|
|Then| A confirmation message shows up.|
|When| Some formats are wrong|
|Then| An Error message is displayed.|

|6 | |
|------|-----------------------------------------------------------------------------------------------------------|
|User Story| As an HCP, I want to select a patient and view all vaccinations administered to that patient, so that I can review a patient's vaccination history for their healthcare needs. |

| | |
|------|---|
|Given| The HCP is authenticated user in iTrust2|
|When|  Navigate to "Patient>View Vaccinations" page|
|Then| A list of patients is displayed |
|And| Click on a patient and click on "Select" button|
|Then| A list of all vaccinations given to the patient is displayed|
|Then| log "HCP views patient's vaccinations" is recorded|

|7 | |
|------|-----------------------------------------------------------------------------------------------------------|
|User Story| As a patient, I want to view my past office visit and see a list of all vaccinations administered during the visit, so that I can see when I get vaccinated. |

| | |
|------|---|
|Given| The Patient is authenticated user in iTrust2|
|When|  Navigate to "Appointments>Past Office Visits" page|
|Then|A list of date and time of previous office visits is displayed |
|And| Click on an office visit|
|Then| The recorded information in the office visit is displayed, including the vaccination information.|

|8 | |
|------|-----------------------------------------------------------------------------------------------------------|
|User Story| As a patient, I want to view a complete list of all the vaccinations I've received, including details like the name, abbreviation, CPT code, and the date administered, so that I can stay informed about my vaccination history. |

| | |
|------|---|
|Given| The Patient is authenticated user in iTrust2|
|When|  Navigate to "Appointments>Vaccinations" page|
|Then| A list of vaccines that are vaccinated are displayed|
|Then| log "Patient view vaccinations" is recorded.|
