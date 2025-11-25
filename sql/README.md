## Introduction
This project focuses on learning SQL and understanding how relational databases organize and manage data. I used PostgreSQL and DBeaver to practice the material and worked on  writing and testing different SQL queries. I explored how to retrieve data, update values, delete records, filter results, and use aggregates to answer more complex questions. Along the way, I was introduced to data modeling concepts such as schemas, table relationships, and how data is structured inside an RDBMS. I also looked at simple performance and optimization ideas, like how query structure and indexing can influence results.


## SQL Queries

##### Table Setup (DDL)

```sql
-- Create tables members, facilites and bookings in schema cd
CREATE SCHEMA cd;

CREATE TABLE cd.members (
  memid integer NOT NULL, 
  surname character varying(200) NOT NULL, 
  firstname character varying(200) NOT NULL, 
  address character varying(300) NOT NULL, 
  zipcode integer NOT NULL, 
  telephone character varying(20) NOT NULL, 
  recommendedby integer, 
  joindate timestamp NOT NULL, 
  CONSTRAINT members_pk PRIMARY KEY (memid), 
  CONSTRAINT fk_members_recommendedby FOREIGN KEY (recommendedby) REFERENCES cd.members(memid) ON DELETE 
  SET 
    NULL
);

CREATE TABLE cd.facilities (
  facid integer NOT NULL, 
  name character varying(100) NOT NULL, 
  membercost numeric NOT NULL, 
  guestcost numeric NOT NULL, 
  initialoutlay numeric NOT NULL, 
  monthlymaintenance numeric NOT NULL, 
  CONSTRAINT facilities_pk PRIMARY KEY (facid)
);

CREATE TABLE cd.bookings (
  bookid integer NOT NULL, 
  facid integer NOT NULL, 
  memid integer NOT NULL, 
  starttime timestamp NOT NULL, 
  slots integer NOT NULL, 
  CONSTRAINT bookings_pk PRIMARY KEY (bookid), 
  CONSTRAINT fk_bookings_facid FOREIGN KEY (facid) REFERENCES cd.facilities(facid), 
  CONSTRAINT fk_bookings_memid FOREIGN KEY (memid) REFERENCES cd.members(memid)
);

```
##### Modifying Data
###### Question 1: Insert a new facility called Spa
```sql
INSERT INTO cd.facilities (
  facid, name, membercost, guestcost,
  initialoutlay, monthlymaintenance
)
VALUES
  (9, 'Spa', 20, 30, 100000, 800);
```
###### Question 2: Insert the Spa facility using an auto-generated facid
```sql
INSERT INTO cd.facilities (
  facid, name, membercost, guestcost,
  initialoutlay, monthlymaintenance
)
SELECT
  MAX(facid) + 1,
  'Spa',
  20,
  30,
  100000,
  800
FROM
  cd.facilities;
```

###### Question 3: Correct the initial outlay for the second tennis court
```sql
UPDATE 
    cd.facilities
SET 
    initialoutlay = 10000
WHERE 
    facid = 1;
```

###### Question 4: Increase the member and guest cost of the second tennis court to 10% above the first court's prices
```sql
UPDATE 
    cd.facilities
SET
    membercost = (SELECT membercost * 1.10 FROM cd.facilities WHERE facid = 0),
    guestcost  = (SELECT guestcost  * 1.10 FROM cd.facilities WHERE facid = 0)
WHERE 
    facid = 1;
```

###### Question 5: Delete all bookings
```sql
DELETE FROM cd.bookings;
```

###### Question 6: Delete a member from the cd.members table
```sql
DELETE FROM cd.members
WHERE
  memid = 37;
```

##### Basics
###### Question 7: List facilities where members pay a fee that is less than 1/50th of the monthly maintenance
```sql
SELECT facid,
       name,
       membercost,
       monthlymaintenance
FROM 
    cd.facilities
WHERE 
    membercost > 0
  AND 
    membercost < monthlymaintenance / 50;
```

###### Question 8: List all facilities whose name contains the word 'Tennis'
```sql
SELECT facid,
       name
FROM 
    cd.facilities
WHERE 
    name LIKE '%Tennis%';
```

###### Question 9: Retrieve facility details for facid 1 and 5 without using OR
```sql
SELECT *
FROM 
    cd.facilities
WHERE 
    facid IN (1, 5);
```

###### Question 10: List members who joined after September 1st, 2012
```sql
SELECT memid,
       surname,
       firstname,
       joindate
FROM 
    cd.members
WHERE 
    joindate > '2012-09-01';
```

###### Question 11: Produce a combined list of all member surnames and all facility names
```sql
SELECT 
    surname AS name
FROM 
    cd.members
UNION
SELECT 
    name
FROM 
    cd.facilities;
```

##### Join
###### Question 12: Retrieve the start times of members' bookings
```sql
SELECT 
  starttime 
FROM 
  cd.bookings AS b 
  JOIN cd.members AS m ON b.memid = m.memid 
WHERE 
  m.firstname = 'David' 
  AND m.surname = 'Farrell';
```

###### Question 13: Work out the start times of bookings for tennis courts
```sql
SELECT
  b.starttime AS start,
  f.name
FROM
  cd.bookings b
  JOIN cd.facilities f ON f.facid = b.facid
WHERE
  DATE(b.starttime) = '2012-09-21'
  AND f.name LIKE '%Tennis Court%'
ORDER BY
  b.starttime;
```

###### Question 14: Produce a list of all members, along with their recommender
```sql
SELECT 
  m.firstname AS memfname, 
  m.surname AS memsname, 
  r.firstname AS recfname, 
  r.surname AS recsname 
FROM 
  cd.members m 
  LEFT JOIN cd.members r ON m.recommendedby = r.memid 
ORDER BY 
  m.surname, 
  m.firstname;
```

###### Question 15: Produce a list of all members who have recommended another member
```sql
SELECT
  DISTINCT r.firstname AS firstname,
  r.surname AS surname
FROM
  cd.members m
  JOIN cd.members r ON m.recommendedby = r.memid
ORDER BY
  r.surname,
  r.firstname;
```

###### Question 16: List all members with their recommender  and format names as full-name columns
```sql
SELECT 
  DISTINCT m.firstname || ' ' || m.surname AS member, 
  (
    SELECT 
      r.firstname || ' ' || r.surname 
    FROM 
      cd.members AS r 
    WHERE 
      r.memid = m.recommendedby
  ) AS recommended_by 
FROM 
  cd.members AS m 
ORDER BY 
  member, 
  recommended_by;
```

##### Aggregation
###### Question 17: Count how many recommendations each member has made, ordered by memid
```sql
SELECT
  recommendedby,
  count(*)
FROM
  cd.members
WHERE
  recommendedby is not null
GROUP BY
  recommendedby
ORDER BY
  recommendedby;
```

###### Question 18: List the total slots booked per facility 
```sql
SELECT
  facid,
  sum(slots) AS "Total Slots"
FROM
  cd.bookings
GROUP BY
  facid
ORDER BY
  facid;
```

###### Question 19: List the total slots booked per facility in a given month
```sql
SELECT
  facid,
  SUM(slots) AS "Total Slots"
FROM
  cd.bookings
WHERE
  starttime >= '2012-09-01'
  AND starttime < '2012-10-01'
GROUP BY
  facid
ORDER BY
  "Total Slots";
```

###### Question 20: List the total slots booked per facility per month
```sql
SELECT 
  facid, 
  EXTRACT(MONTH FROM starttime) AS month, 
  SUM(slots) AS "Total Slots"
FROM 
  cd.bookings
WHERE 
  EXTRACT(YEAR FROM starttime) = 2012
GROUP BY 
  facid, 
  month
ORDER BY 
  facid, 
  month;

```

###### Question 21: Find the count of members who have made at least one booking 
```sql
SELECT
  COUNT(distinct memid)
FROM
  cd.bookings;
```

###### Question 22: List each member's first booking after September 1st 2012
```sql
SELECT
  m.surname,
  m.firstname,
  m.memid,
  min(b.starttime) as starttime
FROM
  cd.bookings b
  inner join cd.members m on m.memid = b.memid
WHERE
  starttime >= '2012-09-01'
GROUP BY
  surname,
  firstname,
  m.memid
ORDER BY
  m.memid;
```

###### Question 23: Produce a list of member names, with each row containing the total member count
```sql
SELECT 
  COUNT (*) OVER () as COUNT, 
  firstname, 
  surname 
FROM 
  cd.members 
ORDER BY 
  joindate;
```

###### Question 24: Produce a numbered list of members
```sql
SELECT
  ROW_NUMBER() OVER(
    ORDER BY
      joindate
  ) AS row_number,
  firstname,
  surname
FROM
  cd.members
ORDER BY
  row_number;
```

###### Question 25: Return the facid with the highest total number of booked slots, including ties
```sql
SELECT facid, total
FROM
  (
    SELECT
      facid,
      sum(slots) total,
      rank() OVER (
        ORDER BY
          sum(slots) desc
      ) rank
    FROM
      cd.bookings
    GROUP BY
      facid
  ) as ranked
WHERE 
  rank = 1
```
##### String
###### Question 26: Format the names of members
```sql
SELECT 
  surname || ', ' || firstname as name 
FROM 
  cd.members
```

###### Question 27: Find telephone numbers with parentheses
```sql
SELECT 
  memid, 
  telephone 
FROM 
  cd.members 
WHERE 
  telephone ~ '[()]';
```

###### Question 28: Count the number of members whose surname starts with each letter of the alphabet
```sql
SELECT 
  SUBSTR(surname, 1, 1) AS letter, 
  COUNT(*) AS count 
FROM 
  cd.members 
GROUP BY 
  letter 
ORDER BY 
  letter;
```
