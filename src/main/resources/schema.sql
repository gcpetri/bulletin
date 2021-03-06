CREATE SCHEMA IF NOT EXISTS bulletin;

--DROP TABLE IF EXISTS EVENTS;

CREATE TABLE IF NOT EXISTS EVENTS
(
    EVENT_ID BIGSERIAL PRIMARY KEY,
    NAME VARCHAR(255) NOT NULL UNIQUE,
    DESCRIPTION VARCHAR(1024),
    START_TIME TIMESTAMP NOT NULL,
    END_TIME TIMESTAMP NOT NULL,
    COLOR VARCHAR(6) NOT NULL DEFAULT '632ed2',
    SHOW BOOLEAN NOT NULL,
    IMAGE_URL VARCHAR(512)
);

--DROP TABLE IF EXISTS CHALLENGES;

CREATE TABLE IF NOT EXISTS CHALLENGES
(
    CHALLENGE_ID BIGSERIAL PRIMARY KEY,
    NAME VARCHAR(255) NOT NULL UNIQUE,
    NUM_PLACES INT NOT NULL DEFAULT 3,
    COLOR VARCHAR(6) NOT NULL,
    IMAGE_URL VARCHAR(512),
    EVENT_ID BIGSERIAL NOT NULL,
    CONSTRAINT FK_EVENT_CHALLENGE
      FOREIGN KEY(EVENT_ID) 
	    REFERENCES EVENTS(EVENT_ID)
	    ON DELETE CASCADE,
    CONSTRAINT POSITIVE_NUM_PLACES
      CHECK (NUM_PLACES >= 0)
);

--DROP TABLE IF EXISTS SUBMISSIONS;

CREATE TABLE IF NOT EXISTS SUBMISSIONS
(
    SUBMISSION_ID BIGSERIAL PRIMARY KEY,
    NAME VARCHAR(255) NOT NULL UNIQUE,
    DESCRIPTION TEXT NOT NULL,
    COLOR VARCHAR(6) NOT NULL DEFAULT '632ed2',
    LINKS VARCHAR(512)[],
    TAGS VARCHAR(15)[],
    VIDEO_LINK VARCHAR(512) NOT NULL,
    SHOW BOOLEAN NOT NULL,
    ICON_URL VARCHAR(512),
    SOURCE_CODE_URL VARCHAR(512),
    NUM_LIKES INT NOT NULL DEFAULT 0,
    CHALLENGE_ID BIGSERIAL NOT NULL,
    CONSTRAINT FK_SUBMISSION_CHALLENGE
      FOREIGN KEY(CHALLENGE_ID)
	    REFERENCES CHALLENGES(CHALLENGE_ID)
	    ON DELETE SET NULL,
    CREATED_ON TIMESTAMP NOT NULL,
    UPDATED_ON TIMESTAMP NOT NULL
);

--DROP TABLE IF EXISTS ACCOLADES;

CREATE TABLE IF NOT EXISTS ACCOLADES
(
    ACCOLADE_ID BIGSERIAL PRIMARY KEY,
    NAME VARCHAR(255) NOT NULL UNIQUE,
    DESCRIPTION VARCHAR(1024),
    COLOR VARCHAR(6) NOT NULL DEFAULT '632ed2',
    CHALLENGE_ID BIGSERIAL NOT NULL,
    CONSTRAINT FK_CHALLENGE_ACCOLADE
      FOREIGN KEY(CHALLENGE_ID)
	    REFERENCES CHALLENGES(CHALLENGE_ID)
	    ON DELETE CASCADE,
    SUBMISSION_ID INT,
    CONSTRAINT FK_SUBMISSION_ACCOLADE
      FOREIGN KEY(SUBMISSION_ID)
      REFERENCES SUBMISSIONS(SUBMISSION_ID)
      ON DELETE SET NULL
);

--DROP TABLE IF EXISTS QUESTIONS;

CREATE TABLE IF NOT EXISTS QUESTIONS
(
    QUESTION_ID BIGSERIAL PRIMARY KEY,
    CHALLENGE_ID BIGSERIAL NOT NULL,
    DESCRIPTION TEXT NOT NULL,
    CONSTRAINT FK_CHALLENGE_QUESTION
      FOREIGN KEY(CHALLENGE_ID)
      REFERENCES CHALLENGES(CHALLENGE_ID)
      ON DELETE CASCADE
);

--DROP TABLE IF EXISTS USERS;

CREATE TABLE IF NOT EXISTS USERS
(
    USER_ID BIGSERIAL PRIMARY KEY,
    AUTH_ID VARCHAR(24) NOT NULL UNIQUE,
    DISCORD_INFO VARCHAR(255) NOT NULL UNIQUE,
    FIRST_NAME VARCHAR(255),
    LAST_NAME VARCHAR(255),
    EMAIL VARCHAR(255),
    IS_ADMIN BOOLEAN NOT NULL
);

--DROP TABLE IF EXISTS USER_SUBMISSIONS;

CREATE TABLE IF NOT EXISTS USER_SUBMISSIONS
(
    USER_ID BIGSERIAL, 
    CONSTRAINT USER_SUBMISSIONS_USER
      FOREIGN KEY(USER_ID)
      REFERENCES USERS(USER_ID)
      ON UPDATE CASCADE,
    SUBMISSION_ID BIGSERIAL,
    CONSTRAINT USER_SUBMISSIONS_SUBMISSION
      FOREIGN KEY(SUBMISSION_ID)
      REFERENCES SUBMISSIONS(SUBMISSION_ID)
      ON UPDATE CASCADE,
    PRIMARY KEY (USER_ID, SUBMISSION_ID)
);

/*
DROP TABLE IF EXISTS SUBMISSION_QUESTIONS;

CREATE TABLE SUBMISSION_QUESTIONS
(
    SUBMISSION_QUESTION_ID BIGSERIAL,
    ANSWER TEXT,
    QUESTION_ID BIGSERIAL,
    CONSTRAINT SUBMISSION_QUESTIONS_QUESTION
      FOREIGN KEY(QUESTION_ID)
      REFERENCES QUESTIONS(QUESTION_ID)
      ON UPDATE CASCADE,
    SUBMISSION_ID BIGSERIAL,
    CONSTRAINT SUBMISSION_QUESTIONS_SUBMISSION
      FOREIGN KEY(SUBMISSION_ID)
      REFERENCES SUBMISSIONS(SUBMISSION_ID)
      ON UPDATE CASCADE,
    UNQIUE (QUESTION_ID, SUBMISSION_ID)
);
*/

--DROP TABLE IF EXISTS LIKES;

CREATE TABLE IF NOT EXISTS LIKES
(
    USER_ID BIGSERIAL,
    CONSTRAINT FK_LIKE_USER
      FOREIGN KEY(USER_ID)
      REFERENCES USERS(USER_ID)
      ON UPDATE CASCADE,
    SUBMISSION_ID BIGSERIAL,
    CONSTRAINT FK_LIKE_SUBMISSION
      FOREIGN KEY(SUBMISSION_ID)
      REFERENCES SUBMISSIONS(SUBMISSION_ID)
      ON UPDATE CASCADE,
    PRIMARY KEY (USER_ID, SUBMISSION_ID)
);

--DROP TABLE IF EXISTS COMMENTS;

CREATE TABLE IF NOT EXISTS COMMENTS
(
    COMMENT_ID BIGSERIAL PRIMARY KEY,
    MESSAGE TEXT,
    CREATED_ON TIMESTAMP NOT NULL,
    USER_ID BIGSERIAL NOT NULL,
    CONSTRAINT FK_COMMENT_USER
      FOREIGN KEY(USER_ID)
      REFERENCES USERS(USER_ID)
      ON UPDATE CASCADE,
    SUBMISSION_ID BIGSERIAL NOT NULL,
    CONSTRAINT FK_COMMENT_SUBMISSION
      FOREIGN KEY(SUBMISSION_ID)
      REFERENCES SUBMISSIONS(SUBMISSION_ID)
      ON UPDATE CASCADE
);