create database cssms;
use cssms;
drop database cssms;
drop table cssms.shift;
drop table cssms.campus;
drop table cssms.employee;
CREATE TABLE IF NOT EXISTS Employee (id VARCHAR(255) PRIMARY KEY, name VARCHAR(255) NOT NULL, password VARCHAR(255) NOT NULL, role ENUM('MANAGER', 'STAFF') NOT NULL, expectedHourNumber INT, realHourNumber INT);
CREATE TABLE IF NOT EXISTS Campus (campus_id VARCHAR(255) NOT NULL, PRIMARY KEY (campus_id));

INSERT INTO Employee (id, name, password, role, expectedHourNumber, realHourNumber) VALUES 
('1', 'kiet', '0', 'MANAGER', null, null);

SELECT * FROM cssms.Campus;
SELECT * FROM cssms.Employee;
SELECT * FROM cssms.Shift;

ALTER TABLE cssms.shift ADD userCheckInTime VARCHAR(255); 
ALTER TABLE cssms.shift ADD userCheckOutTime VARCHAR(255);
ALTER TABLE cssms.shift ADD job VARCHAR(255);
ALTER TABLE cssms.shift DROP COLUMN job;
ALTER TABLE cssms.campus ADD job VARCHAR(255);
SELECT * FROM cssms.Shift WHERE day = 21 ;
SELECT * FROM cssms.Shift WHERE campus_id = 6 and shift_id = '1-6-2024-2' and day =1;
UPDATE cssms.Employee SET name = 'tuan' WHERE id = '4';
DELETE FROM cssms.Employee WHERE id = '10';
UPDATE cssms.campus SET id = '2' WHERE id = '1';


CREATE TABLE IF NOT EXISTS Campus (campus_id VARCHAR(255) NOT NULL, staff_id VARCHAR(255) NOT NULL, job VARCHAR(255), FOREIGN KEY (staff_id) REFERENCES Employee(id), 	PRIMARY KEY (campus_id, staff_id, job));
CREATE TABLE IF NOT EXISTS Shift (shift_id VARCHAR(255) NOT NULL, staff_id VARCHAR(255) NOT NULL, name VARCHAR(255) NOT NULL, campus_id VARCHAR(255) NOT NULL, day INT NOT NULL, month INT NOT NULL, year INT NOT NULL, checkIn TIME NOT NULL, checkOut TIME NOT NULL, userCheckInTime TIME NULL, userCheckOutTime TIME NULL, job VARCHAR(255) NOT NULL, FOREIGN KEY (campus_id) REFERENCES Campus(campus_id), FOREIGN KEY (staff_id) REFERENCES Employee(id), PRIMARY KEY (shift_id, campus_id, staff_id));


CREATE TABLE IF NOT EXISTS Campus (campus_id VARCHAR(255) NOT NULL, PRIMARY KEY (campus_id));
INSERT INTO Campus (campus_id) VALUES ('1');
INSERT INTO Campus (campus_id) VALUES ('2');
INSERT INTO Campus (campus_id) VALUES ('3');
INSERT INTO Campus (campus_id) VALUES ('4');
INSERT INTO Campus (campus_id) VALUES ('5');



DROP TABLE Shift;
SELECT * FROM cssms.Shift;
SELECT * FROM cssms.Shift WHERE shift_id = '11-6-2024-3';
CREATE TABLE IF NOT EXISTS Shift (
    shift_id VARCHAR(100) NOT NULL,
    staff_id VARCHAR(100) NOT NULL,
    name VARCHAR(255) NOT NULL,
    campus_id VARCHAR(100) NOT NULL,
    day INT NOT NULL,
    month INT NOT NULL,
    year INT NOT NULL,
    checkIn TIME NOT NULL,
    checkOut TIME NOT NULL,
    userCheckInTime TIME DEFAULT NULL,
    userCheckOutTime TIME DEFAULT NULL,
    job VARCHAR(100) NOT NULL,
    scheduleID VARCHAR(100) NOT NULL,
    weekDay  INT NOT NULL,
    totalStaff INT DEFAULT 0,
    FOREIGN KEY (campus_id) REFERENCES Campus(campus_id),
    FOREIGN KEY (staff_id) REFERENCES Employee(id),
    PRIMARY KEY (shift_id, campus_id, staff_id, job)
);
UPDATE cssms.shift SET userCheckOutTime = '09:54:34' WHERE staff_id = '21' and shift_id = '11-6-2024-2';
select userCheckInTime from cssms.shift WHERE staff_id = '21' and shift_id ='11-6-2024-2';
DROP TABLE cssms.Schedule;
SELECT * FROM cssms.Schedule;
CREATE TABLE IF NOT EXISTS schedule (scheduleId INT NOT NULL, shiftId VARCHAR(255) NOT NULL, FOREIGN KEY (shiftId) REFERENCES Shift(shift_id), PRIMARY KEY (scheduleId, shiftId));



