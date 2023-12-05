# UC23 Rate Health Care Provider and Hospital
## 23.1 Preconditions

A patient, an HCP, are registered users of the iTrust2 system (UC1). The patient, or the HCP, has authenticated themselves in the iTrust2 system (UC2).
A hospital is a registered hospital added by the admin (UC5).

## 23.2 Main Flow

A patient can view the reviews of a HCP, or a hospital, in the iTrust2 system.[S1]
A patient can rate and review an HCP who has provided him or her medical treatment or a hospital they have visited.[S2]
The system records the review and updates the HCP's or the hospital's review in the review page [S3].

## 23.3 Sub-flows

   * [S1]: The patient navigates to the review page in the iTrust2 Medical Records system. The patient selects an HCP or a hospital in the review list. The patient sees all reviews of the HCP or the hospital they have selected.
   * [S2]: The patient navigates to a pop-up page where they can write a review for an HCP or a hospital. The patient selects an HCP who has provided medical treatment to them or a hospital they have visited from a dropdown list. The patient selects a rating from 1 to 5 and optionally leaves a comment about their experience. The patient clicks the submit button and submit the review.[E1]
   * [S3]: The system records the review, updates the average rating for the selected HCP or hospital, and add the review in the reviews of the HCP/hospital.

## 23.4 Alternative Flows

   * [E1]: If the patient didn't select a target HCP or hospital, or provide a rating, the system will prompt the user to fill up the review. Otherwise, a success message is displayed.

## 23.5 Logging
| Transaction Code | Verbose Description | Logged In MID | Secondary MID | Transaction Type | Patient Viewable |
|------------------|---------------------|---------------|---------------|------------------|------------------|
| 2301 | Patient rates an HCP | Patient | HCP | Review | Yes |
| 2302 | Patient rates a hospital | Patient | None | Review | Yes |

## 23.6 Data Format
| Field | Format |
|------------------|---------------------|
| Rating | Integer from 0  to 5 |
| Comment | Up to 500 characters, alpha-numeric, and common symbols|

## 23.7 Acceptance Scenarios
**Scenario 1:: A Patient Rates an HCP**  
Patient John Doe authenticates into iTrust2. He navigates to the review page and clicks "write review" button. He selects HCP Dr. Alice Smith from the dropdown, gives her a 4-star rating and writes a comment about his positive experience. He then clicks the submit button and sees a success message. Dr. Alice Smith's average rating is updated and the review is loaded to the reviews of Dr. Alice Smith.

**Scenario 2:: A Patient Rates a Hospital**  
Patient Emily Clark authenticates into iTrust2. She navigates to the review page and clicks "write review" button. She selects 'Greenwood Hospital' in the dropdown list, gives a 5-star rating, and leaves a comment on the excellent facilities. She clicks submit button, sees a success message, and Greenwood Hospital's average rating is updated with the new review. The review is loaded to the reviews of Greenwood Hospital.

**Scenario 3:: A Patient Attempts to Rate Without a Visit**
Patient Alex Johnson authenticates into iTrust2. He tries to rate Dr. Brian Thomas, but he forgot the select the rating. Upon attempting to submit the rating, the system displays an error message prompting him to selects a rating for the HCP.

**Scenario 4:: A Patient sees the reviews of a hospital**
Patient Alex Johnson authenticates into iTrust2. He clicks on Severance Hospital, and a page displaying all the reviews of Severance Hospital pops up.