# User Story: Team1

UC16 & UC19

## UC16

Feature:

- Patients can view and manage their PR (personal representatives) list
- HCPs can assign one patient as PR for another patient and view PRs and assignees.

| 1          |                                                                                                        |
| ---------- | ------------------------------------------------------------------------------------------------------ |
| User Story | As a patient,I want to be able to view my PRs, so that I can know who my personal representatives are. |

|       |                                                          |
| ----- | -------------------------------------------------------- |
| Given | Both the patient and PR is authenticated user in iTrust2 |
| When  | Navigate to PR page                                      |
| Then  | a list of the patient's PRs shows up                     |

| 2          |                                                                                                                  |
| ---------- | ---------------------------------------------------------------------------------------------------------------- |
| User Story | As a patient, I want to be able to declare one or few of my PRs so that I can easily manage my PR relationships. |

|       |                                                          |
| ----- | -------------------------------------------------------- |
| Given | Both the patient and PR is authenticated user in iTrust2 |
| When  | Navigate to PR page                                      |
| And   | Click on "Declare a Representative" button               |
| And   | Type in partial or full name, or MID of a patient        |
| Then  | A list of matching patients appears                      |
| And   | Select a PR from the list                                |
| And   | Click "Confirm Declaration"                              |
| Then  | A success message appear                                 |

| 3          |                                                                                                                    |
| ---------- | ------------------------------------------------------------------------------------------------------------------ |
| User Story | As a patient, I want to be able to undeclare one or few of my PRs so that I can easily manage my PR relationships. |

|       |                                                          |
| ----- | -------------------------------------------------------- |
| Given | Both the patient and PR is authenticated user in iTrust2 |
| When  | Navigate to PR page                                      |
| And   | Select the target PRs to undeclare                       |
| And   | Click on "Undeclare Representatives" button              |
| Then  | A confirmation message is shown                          |

| 4          |                                                                                                                        |
| ---------- | ---------------------------------------------------------------------------------------------------------------------- |
| User Story | As a patient, I want to be able to view the list of patients for whom i am a PR so that I can unassign myself as a PR. |

|       |                                                          |
| ----- | -------------------------------------------------------- |
| Given | Both the patient and PR is authenticated user in iTrust2 |
| When  | Navigate to PR page                                      |
| And   | Select the patient's name                                |
| And   | Click on unassign button                                 |
| Then  | A confirmation message is shown                          |

| 5          |                                                                                                                     |
| ---------- | ------------------------------------------------------------------------------------------------------------------- |
| User Story | As an HCP, I want to view the list of PRs and assignees so that I can facilitate the relationship between patients. |

|       |                                                     |
| ----- | --------------------------------------------------- |
| Given | HCP is authenticated user in iTrust2                |
| When  | Navigate to PR page                                 |
| Then  | A list of RP relationship of all patients shows up. |

| 6          |                                                                                                                                 |
| ---------- | ------------------------------------------------------------------------------------------------------------------------------- |
| User Story | As an HCP, I want to assign one patient as a PR for another patient so that I can facilitate the relationship between patients. |

|       |                                            |
| ----- | ------------------------------------------ |
| Given | HCP is authenticated user in iTrust2       |
| When  | Navigate to PR page                        |
| And   | Select a patient as patient                |
| And   | Select a patient as PR                     |
| And   | Click on "Assign as Person Representative" |
| Then  | A confirmation message shows up            |

## UC19

Feature:

- Patients can view and manage their own food diary
- HCPs can view patients’ food diary

| 1          |                                                                                                          |
| ---------- | -------------------------------------------------------------------------------------------------------- |
| User Story | As a patient,I want to be able to view my food diary, so that I can fit the daily nutrition requirement. |

|       |                                                                      |
| ----- | -------------------------------------------------------------------- |
| Given | The patient is authenticated user in iTrust2                         |
| When  | Navigate to "My Food Dairy" page                                     |
| Then  | Every diary entry sorted by date with detailed information shows up. |

| 2          |                                                                                                                 |
| ---------- | --------------------------------------------------------------------------------------------------------------- |
| User Story | As a patient,I want to be able to create a new food diary, so that I can adjust my daily nutrition requirement. |

|       |                                              |
| ----- | -------------------------------------------- |
| Given | The patient is authenticated user in iTrust2 |
| When  | Navigate to "My Food Dairy" page             |
| And   | Click on "Add Dairy" button                  |
| Then  | A format with specific columns shows up      |
| And   | Fill in the blanks                           |
| And   | Click on "Finish" button                     |
| Then  | A confirmation message shows up              |

| 3          |                                                                                                                                               |
| ---------- | --------------------------------------------------------------------------------------------------------------------------------------------- |
| User Story | As a HCP, I want to be able to view every patient’s food dairy, so that I can know if the patient is meeting the daily nutrition requirement. |

|       |                                                            |
| ----- | ---------------------------------------------------------- |
| Given | The patient and HCP are authenticated user in iTrust2      |
| When  | Navigate to "Patient" column                               |
| And   | Click on "View Food Diary"                                 |
| Then  | A list of all patients shows up                            |
| And   | Type in partial or full name, or MID of a patient          |
| And   | Click on the patient                                       |
| Then  | A matching patient with his or her food dairy in displayed |
