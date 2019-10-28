# Final Challenge - RoboInterviewer

The RoboInterviewer is designed to automate the job selection and interviewing process to prevent companies from having to conduct in-person interviews. This can save them time and money and in the run increase productivity. This program is designed to provide a highly automated workflow that can eliminate much of the labor involved in the hiring process.

The program has the following features:
* Provides a user Home Page that is personalized to each user and shows their relevant job and status information
* User Home Page has a dynamically arranged set of cards for each job the user has applied.
* Each card contains: Job Posted Date, Status of the Job application, Interview Schedule link, Cancel link to dismiss the applicaiton,
  Interview link to start the interview, Interview Start Date/Time, Job Title
* Allow users to submit resumes for specific jobs
* Automatically analyzes the resume of the applicant and using keywords determine if their skills matches the requirements of the position at least 80%.
* Automatically send the decision of the above analyses whether to reject the application or offer an interview.
* If the application for a job is rejected the applicant is given the ability to appeal the decision via an email to the Hiring Manager
* If an interview is offered the applicant is notified and presented with a link for scheduling an interview.
* Automatic notification by status indication and alerts of any pending interview(s)
* During the interview time-window the user will be instructed to select a link that starts the interview.
* Automatic capture of interview results and sending them by email to the hiring manager (Employer).
* User Roles: User, Admin, Supervisor that are used to control access to various content and functions iaw their authorization
* Secure login ability with encrypted password challenge.
* Access control to content and functions is also effected for users based upon their login validation.
* Ability to Add users and jobs interactively
* Ability to manage user accounts by an Administrator
* Ability to delete or Update jobs by the owner of the job creator.
* Automatic notification (via an alert) for any pending interviews.
* Dataloader that is used to automically pre-populate an initial set of users and jobs.

Prepopulated Data: 
Users: 
  Joe - User Role
  admin - Admin Role
  jim - User role
Jobs
Senior Java Developer
Senior Solutions Architect

Resumes
Embedded File: Resumes and Job Descriptions with keywords.
Three resumes have been copied into the application directory for easy resume selection

Heroku Link:

Operational Description:
Initially when the user logs in they are presented with a list of available jobs. 
The user can select apply for any of these jobs.  When 'Apply' is selected for a job
their resume is immediately analyzed and a decision is made to reject the application
or offer an interview.
When the user selects the link to their Personal Home Page (i.e., the Welcome link) they 
will be brought to their home page.  Here they will receive an alert if any interviews
are currently scheduled.  The user will see a list of cards.  Each card contains 
pertinent status information and presents only links that are valid for the current
status of the application (i.e., Cancel, Schedule, Interview, Appeal, Accept). This completes 
the basic application/interview workflow.  

In addition the Admin can view all the user accounts and delete accounts that are no longer
required. There is also a Job Search capability that is currently only available to someone
with Administrative privileges.


Developers:
John Anderjaska (Solutions Architect/Developer) - MSEE, CISSP
Joe Huang - Senior Year Engineering Student
Katrina Campbel - student



