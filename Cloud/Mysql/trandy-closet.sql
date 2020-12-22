use Trandy_Closet;

SELECT * FROM Users;
SELECT * FROM Cloths;
SELECT * FROM Cloth_Dairy;

show tables;

INSERT INTO
Cloths (name, sex)
VALUES ('son', 'M'); 


INSERT INTO
Cloths (name, image_number, category, detail, color, season, Url)
VALUES ('son', 1, 'bottom', 'pants', 'black', 'Fall', 'https://s3transferimages1209133644-dev.s3.amazonaws.com/public/2020.11.04_0001.jpg'); 

INSERT INTO
Cloths (name, category, detail, color, season, Url)
VALUES ('son', 'shoes', 'sneakers', 'black', 'All', 'https://s3transferimages1209133644-dev.s3.amazonaws.com/public/2020.11.13_0004.jpg'); 

INSERT INTO
Cloth_Dairy (name, cloth_number, top, bottom, coat, dress, shoes, date, title)
VALUES ('son', 1, 3, 5, 9, 10, 19, 20201222, 'asdasd'); 

INSERT INTO
Cloth_Dairy (name, top, bottom, coat, dress, shoes, date, title)
VALUES ('son', 3, 5, 9, 10, 19, 20201222, 'asdasd'); 


CREATE TABLE Users ( 
    user_id VARCHAR(30) NOT NULL, 
    name VARCHAR(30) NOT NULL, 
    sex ENUM('M','F'),
    PRIMARY KEY (name)
);
CREATE TABLE Cloths (  
    name VARCHAR(30) NOT NULL, 
    image_number INT(30) auto_increment,
    category VARCHAR(30),
    detail VARCHAR(30),
    color VARCHAR(30),
    season VARCHAR(30),
    Url VARCHAR(100),
    PRIMARY KEY (image_number),
    FOREIGN KEY (name) REFERENCES Users (name)
);
CREATE TABLE Cloth_Dairy (  
    name VARCHAR(30) NOT NULL, 
    cloth_number INT(30) NOT NULL auto_increment,
    top VARCHAR(30),
    bottom VARCHAR(30),
    coat VARCHAR(30),
    dress VARCHAR(30),
    shoes VARCHAR(30),
    date datetime,
    title VARCHAR(30),
    primary key(cloth_number),
    FOREIGN KEY(name) REFERENCES Users(name)
);


DROP tables Users;
DROP tables Cloths;
DROP tables Cloth_Dairy;