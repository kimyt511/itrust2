# User Story: Team3

UC17 & UC23

## UC17
Feature:
- Administrator can update list of procedure codes in system by adding, editing or removing procedures.
- HCP can select procedure for patient during office visit and provide additional data about procedure.
- Lab tech can view all their procedures currently assigned. Update, edit comments when status is Completed and reassign to another lab tech.
- Patient or HCP can view associated procedures when looking at an office visit.

|1 | |
|------|-----------------------------------------------------------------------------------------------------------|
|User Story| As an administrator, I want to be able to add a new procedure to the system by providing the LOINC code, long common name, component, and property. After adding the procedure, I should receive a confirmation message. |

| | |
|------|---|
|Given| The administrator is authenticated user in iTrust2|
|When|  Navigate to "Admin>List of procedures (Add procedure)"|
|Then| Blanks are shown to enter.|
|And| Fill in the blanks (LOINC code, Long Common Name, Component, Property of the procedure)|
|And| Click on "Add Procedure"|
|When| Formats are all correct|
|Then| A message shows procedure was entered correctly|

|2 | |
|------|-----------------------------------------------------------------------------------------------------------|
|User Story| As an administrator, I want to be able to edit an existing procedure, including changing the LOINC code, long common name, component, and property. After making the changes, I should receive a confirmation message. |

| | |
|------|---|
|Given| The administrator is authenticated user in iTrust2|
|When|  Navigate to "Admin>List of procedures"|
|Then| Blanks are shown to enter.|
|And|Selects from the list of possible procedures.|
|And| Click on "Edit Procedure"|
|And|Edit the procedure (LOINC code, Long Common Name, Component, Property of the procedure)|
|And|Click on "Submit"|
|When| Formats are all correct|
|Then| A message shows procedure was entered correctly|

|3 | |
|------|-----------------------------------------------------------------------------------------------------------|
|User Story| As an administrator, I want to be able to delete a procedure from the system. After deletion, I should receive a confirmation message. |

| | |
|------|---|
|Given| The administrator is authenticated user in iTrust2|
|When|  Navigate to "Admin>List of procedures"|
|And| Selects from the list of possible procedures.|
|And| Press "Delete procedure"|

|4 | |
|------|-----------------------------------------------------------------------------------------------------------|
|User Story| As an HCP, I want to document a procedure for a patient during an office visit. I should be able to select the procedure, specify additional details, and receive a confirmation message. |

| | |
|------|---|
|Given| The HCP is registered and authenticated user in iTrust2. HCP has started documenting office visit including basic health metrics|
|When|  Navigate to menu of procedure options labeled by their LOINC codes and descriptions|
|And| Enter additional info about procedure, priority 1~4 (1 being highest), lab tech who will perform procedure and any comments|
|And| Press "Add Procedure"|
|When| Formats are all correct|
|Then| A message shows procedure was entered correctly|

|5 | |
|------|-----------------------------------------------------------------------------------------------------------|
|User Story| As a lab tech, I want to view the list of procedures currently assigned to me. I should be able to update the status of these procedures and edit the associated comments. I can also reassign a procedure to another lab tech. After performing these actions, I should receive confirmation messages. |

| | |
|------|---|
|Given| The lab tech is authenticated user in iTrust2|
|When| Navigate to view assigned procedures and data|
|And| Update the current status of the procedure. (If status is changed to Completed, comments on procedure can be changed. Selecting another lab tech and assign the procedure to them is also possible)|
|When| Actions are taken|
|Then| A message shows confirmation of actions.|

|6 | |
|------|-----------------------------------------------------------------------------------------------------------|
|User Story| As a patient or HCP, I want to view the procedures associated with an office visit, including the LOINC code, priority, date, comments, and status. The HCP should have the ability to delete any procedure with a status of "Assigned." |

| | |
|------|---|
|Given| The patient and HCP is authenticated user in iTrust2. HCP has started documenting office visit including basic health metrics|
|When| Navigate to view an office visit.|
|Then| View all information pertaining to any procedures associated with the office visit, including LOINC code, priority, date, comment, and status. (NOTE: Patient cannot edit information. HCP can delete any procedure with a status of "Assigned")|


## UC23
//////////////////////////////////////////////////////////FEATURES//////////////////////////////////////////////////////////////////////
- As a patient, I want to navigate to the rating and review section in the iTrust2 application. This is the starting point for rating HCPs or hospitals.

- As a patient, I want to be able to select an HCP or hospital I have visited from a dropdown list or through a search interface, so I can specify who or what I'm rating.

- As a patient, I want to select a rating from 1 to 5 stars and optionally leave a comment about my experience. This allows me to provide detailed feedback.

- As a patient, I want to submit my review after selecting the rating and, if desired, providing a comment. This allows me to officially rate and review the HCP or hospital.

- As a patient, I want the system to flag or moderate reviews with inappropriate content to maintain a respectful and professional review environment.

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

|1 | |
|------|-----------------------------------------------------------------------------------------------------------|
|User Story| As a patient, I want to be able to rate and review a Health Care Provider (HCP) or a hospital I have recently visited. I should have access to a rating interface where I can select an HCP or hospital, submit a rating (from 1 to 5 stars), and optionally provide a comment about my experience. After submitting my review, the system should record it, update the average rating for the selected HCP or hospital, and make the review visible to other patients.|

| | |
|------|---|
|Given| The patient is authenticated user in iTrust2|
|When| Navigate to rate and review recent HCP or hospital.|
|And|Selects HCP or hospital to submit rating (1 to 5 stars)|
|And|Optionally provide comments about experience.|
|Amd| Click on "Submit" button |
|When|Review has been submitted correctly|
|Then| System should record it, update the average rating for the selected HCP or hospital and make review visible to other patients.|

|2 | |
|------|-----------------------------------------------------------------------------------------------------------|
|User Story| As a patient, I want the system to display an error message if I try to rate an HCP or hospital I haven't visited. This ensures that only genuine ratings are accepted and prevents misuse of the rating system.|

| | |
|------|---|
|Given| The patient is authenticated user in iTrust2|
|When| Navigate to rate and review recent HCP or hospital.|
|And|Selects HCP or hospital to submit rating (1 to 5 stars)|
|And|Optionally provide comments about experience.|
|Amd| Click on "Submit" button |
|When| Selected HCP or hospital is not a visited event.|
|Then| System should show error message (not visited) to ensure genuine ratings.|

|3 | |
|------|-----------------------------------------------------------------------------------------------------------|
|User Story| As a patient, I want the system to prompt me to save or discard my review if I leave the rating section without submitting. This helps me avoid accidentally losing my review.|

| | |
|------|---|
|Given| The patient is authenticated user in iTrust2|
|When| Navigate to rate and review recent HCP or hospital.|
|And|Selects HCP or hospital to submit rating (1 to 5 stars)|
|And|Optionally provide comments about experience.|
|Amd| Leave the rating section without submitting.|
|When| Rating has been updated and comments were optionally made but has not submitted and tries to leave section.|
|Then| System should prompt message to save or discard review before losing data.|

|4 | |
|------|-----------------------------------------------------------------------------------------------------------|
|User Story| As an administrator, I want to be able to add new HCP or hospital to the list. After adding the respective item, I should receive a confirmation message.|

| | |
|------|---|
|Given| The administrator is authenticated user in iTrust2|
|When| Navigate to list of HCP or hospital to review|
|Then| Blanks are shown to enter|
|And| Fill in the blanks (for HCP, Hospital is required to enter)|
|And| Click on "Add item" button|
|When| Formats are all correct|
|Then| A message shows item was created correctly.|

|5 | |
|------|-----------------------------------------------------------------------------------------------------------|
|User Story| As an administrator, I want to be able to edit existing HCP or hospital to the list. After adding the respective item, I should receive a confirmation message.|

| | |
|------|---|
|Given| The administrator is authenticated user in iTrust2|
|When| Navigate to list of HCP or hospital to review|
|Then| Blanks are shown to enter|
|And| Selects from the list of HCP or hospitals|
|And| Press "Edit item"|
|And| Edit information|
|And| Click on "Save"|
|When| Formats are all correct|
|Then| A message shows item was edited correctly.|

|6 | |
|------|-----------------------------------------------------------------------------------------------------------|
|User Story| As an administrator, I want to be able to delete existing HCP or hospital of the list.|

| | |
|------|---|
|Given| The administrator is authenticated user in iTrust2|
|When| Navigate to list of HCP or hospital to review|
|Then| Blanks are shown to enter|
|And| Selects from the list of HCP or hospitals|
|And| Press "Delete item"|


