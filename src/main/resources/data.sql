INSERT INTO EVENTS (NAME, DESCRIPTION, START_TIME, END_TIME, SHOW, COLOR) VALUES ('Test Event', 'Test description.', '2022-01-13T03:15:00', '2022-01-14T03:15:00', true, '3affdd');
INSERT INTO CHALLENGES (NAME, NUM_PLACES, COLOR) VALUES ('Test Challenge', 1, 'ffffff');
INSERT INTO ACCOLADES (NAME, DESCRIPTION, COLOR) VALUES ('1st in Test Challenge', 'This declares that you received 1st place in Test Challenge', 'AAAAAA');
INSERT INTO QUESTIONS (DESCRIPTION) VALUES ('Site any sources (Github Repositories, Medium Articles, Open Source projects, etc.)');
INSERT INTO SUBMISSIONS (NAME, VIDEO_LINK, SHOW, DESCRIPTION, CREATED_ON, UPDATED_ON) VALUES ('Test Submission', 'https://www.youtube.com', true, 'Test Submission description', LOCALTIMESTAMP, LOCALTIMESTAMP);
INSERT INTO USERS (AUTH_ID, EMAIL, FIRST_NAME, LAST_NAME, IS_ADMIN, DISCORD_INFO) VALUES ('5f1a0f7b75ffe70003051e25', 'gcpetri@tamu.edu', 'Gregory', 'Petri', true, 'GregoryPetri#2669');
INSERT INTO USER_SUBMISSIONS (SUBMISSION_ID, USER_ID) VALUES (1,1);