
-- Modifying Data

-- Insert a new facility called Spa
INSERT INTO cd.facilities (
  facid, name, membercost, guestcost,
  initialoutlay, monthlymaintenance
)
VALUES
  (9, 'Spa', 20, 30, 100000, 800);


-- Insert the Spa facility using an auto-generated facid
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


-- Correct the initial outlay for the second tennis court
UPDATE cd.facilities
SET initialoutlay = 10000
WHERE facid = 1;

-- Increase the member and guest cost of the second tennis court to 10% above the first court's prices
UPDATE cd.facilities
SET
    membercost = (SELECT membercost * 1.10 FROM cd.facilities WHERE facid = 0),
    guestcost  = (SELECT guestcost  * 1.10 FROM cd.facilities WHERE facid = 0)
WHERE facid = 1;

-- Delete all bookings
DELETE FROM cd.bookings;

-- Delete a member from the cd.members table
DELETE FROM cd.members
WHERE
  memid = 37;


-- Basic 

-- List facilities where members pay a fee that is less than 1/50th of the monthly maintenance
SELECT facid,
       name,
       membercost,
       monthlymaintenance
FROM cd.facilities
WHERE membercost > 0
  AND membercost < monthlymaintenance / 50;

-- List all facilities whose name contains the word 'Tennis'
SELECT facid,
       name
FROM cd.facilities
WHERE name LIKE '%Tennis%';

-- Retrieve facility details for facid 1 and 5 without using OR
SELECT *
FROM cd.facilities
WHERE facid IN (1, 5);

-- List members who joined after September 1st, 2012
SELECT memid,
       surname,
       firstname,
       joindate
FROM cd.members
WHERE joindate > '2012-09-01';

-- Produce a combined list of all member surnames and all facility names
SELECT surname AS name
FROM cd.members
UNION
SELECT name
FROM cd.facilities;


-- Join 

-- Retrieve the start times of members' bookings
select
  starttime
from
  cd.bookings as b
  Join cd.members as m on b.memid = m.memid
WHERE
  m.firstname = 'David'
  AND m.surname = 'Farrell';


-- Work out the start times of bookings for tennis courts
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

-- Produce a list of all members, along with their recommender
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

-- Produce a list of all members who have recommended another member
SELECT
  DISTINCT r.firstname AS firstname,
  r.surname AS surname
FROM
  cd.members m
  JOIN cd.members r ON m.recommendedby = r.memid
ORDER BY
  r.surname,
  r.firstname;

-- List all members with their recommender  and format names as full-name columns
SELECT DISTINCT
  m.firstname || ' ' || m.surname AS member,
  (SELECT r.firstname || ' ' || r.surname
   FROM cd.members AS r
   WHERE r.memid = m.recommendedby) AS recommended_by
FROM cd.members AS m
ORDER BY member, recommended_by;


-- Aggregation

-- Count how many recommendations each member has made, ordered by memid
select
  recommendedby,
  count(*)
from
  cd.members
where
  recommendedby is not null
group by
  recommendedby
order by
  recommendedby;

-- List the total slots booked per facility 
select
  facid,
  sum(slots) as "Total Slots"
from
  cd.bookings
group by
  facid
order by
  facid;

-- List the total slots booked per facility in a given month
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

-- List the total slots booked per facility per month
select 
  facid, 
  extract(
    month 
    from 
      starttime
  ) as month, 
  sum(slots) as "Total Slots" 
from 
  cd.bookings 
where 
  extract(
    year 
    from 
      starttime
  ) = 2012 
group by 
  facid, 
  month 
order by 
  facid, 
  month;

-- Find the count of members who have made at least one booking 
SELECT
  COUNT(distinct memid)
FROM
  cd.bookings;

-- List each member's first booking after September 1st 2012
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

-- Produce a list of member names, with each row containing the total member count
SELECT 
  COUNT (*) OVER () as COUNT, 
  firstname, 
  surname 
FROM 
  cd.members 
ORDER BY 
  joindate;

-- Produce a numbered list of members
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

-- Return the facid with the highest total number of booked slots, including ties
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

-- String

-- Format the names of members
SELECT surname || ', ' || firstname as name 
FROM cd.members

-- Find telephone numbers with parentheses
SELECT  memid, telephone 
FROM cd.members 
WHERE telephone ~ '[()]';

-- Count the number of members whose surname starts with each letter of the alphabet
SELECT
  SUBSTR(surname, 1, 1) AS letter,
  COUNT(*) AS count
FROM cd.members
GROUP BY letter
ORDER BY letter;
