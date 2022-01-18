CREATE SCHEMA IF NOT EXISTS bulletin;

DROP TABLE IF EXISTS EVENTS;

CREATE TABLE EVENTS
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

DROP TABLE IF EXISTS CHALLENGES;

CREATE TABLE CHALLENGES
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

DROP TABLE IF EXISTS SUBMISSIONS;

CREATE TABLE SUBMISSIONS
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
    ICON_KEY VARCHAR(255),
    SOURCE_CODE_URL VARCHAR(512),
    SOURCE_CODE_KEY VARCHAR(255),
    CHALLENGE_ID BIGSERIAL NOT NULL,
    CONSTRAINT FK_SUBMISSION_CHALLENGE
      FOREIGN KEY(CHALLENGE_ID)
	    REFERENCES CHALLENGES(CHALLENGE_ID)
	    ON DELETE SET NULL
);

DROP TABLE IF EXISTS ACCOLADES;

CREATE TABLE ACCOLADES
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

DROP TABLE IF EXISTS QUESTIONS;

CREATE TABLE QUESTIONS
(
    QUESTION_ID BIGSERIAL PRIMARY KEY,
    CHALLENGE_ID BIGSERIAL NOT NULL,
    DESCRIPTION TEXT NOT NULL,
    CONSTRAINT FK_CHALLENGE_QUESTION
      FOREIGN KEY(CHALLENGE_ID)
      REFERENCES CHALLENGES(CHALLENGE_ID)
      ON DELETE CASCADE
);

/*
DROP TABLE IF EXISTS SUBMISSION_QUESTIONS;

CREATE TABLE SUBMISSION_QUESTIONS
(
    SUBMISSION_QUESTION_ID BIGSERIAL PRIMARY KEY,
    QUESTION_ID BIGSERIAL NOT NULL,
    CONSTRAINT FK_SUBMISSION_QUESTION
      FOREIGN KEY(QUESTION_ID)
      REFERENCES QUESTIONS(QUESTION_ID)
      ON DELETE CASCADE,
    SUBMISSION_ID BIGSERIAL NOT NULL,
    CONSTRAINT FK_QUESTION_SUBMISSION
      FOREIGN KEY(SUBMISSION_ID)
      REFERENCES SUBMISSIONS(SUBMISSION_ID)
      ON DELETE CASCADE,
    CONSTRAINT UNIQUE_SUBMISSION_QUESTION
      UNIQUE (SUBMISSION_ID, QUESTION_ID),
    ANSWER TEXT NOT NULL
);
*/