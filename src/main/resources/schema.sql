DROP DATABASE IF EXISTS social_network;
CREATE DATABASE social_network;

USE social_network;

CREATE TABLE user (
    id VARCHAR(10) NOT NULL,
    firstname VARCHAR(60) NOT NULL,
    surname VARCHAR(60) NOT NULL,
    birthdate DATE NOT NULL,
    CONSTRAINT PK_User PRIMARY KEY(id)
);

CREATE TABLE frienship (
    user_id1 VARCHAR(10) NOT NULL,
    user_id2 VARCHAR(10) NOT NULL,
    timestamp DATETIME NOT NULL,
    CONSTRAINT PK_Friendship PRIMARY KEY(user_id1, user_id2),
    FOREIGN KEY (user_id1) REFERENCES user(id),
    FOREIGN KEY (user_id2) REFERENCES user(id)
);

CREATE TABLE post (
    id VARCHAR(10) NOT NULL,
    user_id VARCHAR(10) NOT NULL,
    text VARCHAR(300) NOT NULL,
    timestamp DATETIME NOT NULL,
    CONSTRAINT PK_Post PRIMARY KEY(id)
);

CREATE TABLE like_reaction (
    post_id VARCHAR(10) NOT NULL,
    user_id VARCHAR(10) NOT NULL,
    timestamp DATETIME NOT NULL,
    CONSTRAINT PK_LikeReaction PRIMARY KEY(post_id, user_id),
    FOREIGN KEY (post_id) REFERENCES post(id),
    FOREIGN KEY (user_id) REFERENCES user(id)
);