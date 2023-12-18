#db : HMBank

show databases;
use HMBank;
-- task 2 
-- Write SQL scripts to create the mentioned tables with appropriate data types, constraints,  and relationships.  
 create table Customers (
    customer_id int primary key,
	first_name char(120),
    last_name char(120),
    DOB date,
    email varchar(120),
    phone_number int,
    address varchar(255));
desc customers;

 create table Accounts (
	 account_id bigint primary key,
	 customer_id int,
	 acount_type varchar(120),
	 balance float,
	 foreign key(customer_id) references customers(customer_id));
 desc accounts;
 
 create table Transactions (
	transaction_id int primary key,
    account_id bigint,
    transaction_type char(255),
    amount float,
    transaction_date date,
    foreign key(account_id) references Accounts(account_id));
desc transactions;
 -- Tasks 3: Data Manipulation Language (DML): 
INSERT INTO Customers (customer_id, first_name, last_name, DOB, email, phone_number, address)
VALUES 
    (19, 'Minji', 'Park', '1993-06-10', 'minji.park@email.com', 1012345678, '123 Gangnam Street, Seoul'),
    (20, 'Sungwoo', 'Kim', '1988-11-25', 'sungwoo.kim@email.com', 1098765432, '456 Banpo-dong, Seocho-gu, Seoul'),
    (21, 'Haeun', 'Lee', '1997-04-03', 'haeun.lee@email.com', 1056781234, '789 Sajik-dong, Jongno-gu, Seoul'),
    (22, 'Seojun', 'Choi', '1985-09-15', 'seojun.choi@email.com', 1033339999, '321 Seogyo-dong, Mapo-gu, Seoul'),
    (23, 'Jiwon', 'Jung', '1991-12-20', 'jiwon.jung@email.com', 1044444444, '567 Jamsil-dong, Songpa-gu, Seoul'),
    (24, 'Jaehyun', 'Han', '1980-02-28', 'jaehyun.han@email.com', 1022227777, '987 Hwagok-dong, Gangseo-gu, Seoul'),
    (25, 'Yuna', 'Kang', '1995-08-07', 'yuna.kang@email.com', 1066661111, '654 Beon-dong, Gangbuk-gu, Seoul'),
    (26, 'Hyunwoo', 'Yoon', '1983-10-12', 'hyunwoo.yoon@email.com', 1077772222, '321 Cheonho-dong, Gangdong-gu, Seoul'),
    (27, 'Eunseo', 'Ryu', '1998-03-18', 'eunseo.ryu@email.com', 1088883333, '789 Sinsa-dong, Gangnam-gu, Seoul'),
    (28, 'Joonhee', 'Lim', '1987-07-22', 'joonhee.lim@email.com', 1099994444, '456 Yangjae-dong, Seocho-gu, Seoul');
    
select * from customers;

INSERT INTO Accounts (account_id, customer_id, acount_type, balance)
VALUES 
    (1, 19, 'savings', 5000.00),
    (2, 20, 'current', 7500.50),
    (3, 21, 'savings', 3000.25),
    (4, 22, 'zero_balance', 0.00),
    (5, 23, 'current', 12000.75),
    (6, 24, 'savings', 100.00),
    (7, 25, 'current', 5500.80),
    (8, 26, 'savings', 430.20),
    (9, 27, 'current', 9600.60),
    (10, 28, 'zero_balance', 0.00);
    
select * from accounts;

INSERT INTO Transactions (transaction_id, account_id, transaction_type, amount, transaction_date)
VALUES 
    (101, 1, 'deposit', 1000.00, '2023-01-05'),
    (102, 2, 'withdrawal', 500.50, '2023-02-10'),
    (103, 3, 'deposit', 800.25, '2023-03-15'),
    (104, 4, 'deposit', 1500.00, '2023-04-20'),
    (105, 5, 'withdrawal', 2000.75, '2023-05-25'),
    (106, 6, 'deposit', 50.00, '2023-06-30'),
    (107, 7, 'withdrawal', 1500.80, '2023-07-05'),
    (108, 8, 'deposit', 200.20, '2023-08-10'),
    (109, 9, 'withdrawal', 1000.60, '2023-09-15'),
    (110, 10, 'deposit', 500.00, '2023-10-20');
select * from transactions;
    
select * from accounts;
select * from customers;

-- task 3 queries
-- 1 Write a SQL query to retrieve the name, accounttype and email of all customers.

SELECT C.first_name, C.last_name, A.acount_type, C.email
FROM Customers C
JOIN Accounts A ON C.customer_id = A.customer_id;

-- 2. Write a SQL query to list all transaction corresponding customer. 

SELECT T.transaction_id, T.account_id, T.transaction_type, T.amount, T.transaction_date, C.first_name, C.last_name, C.email
FROM Transactions T
JOIN Accounts A ON T.account_id = A.account_id
JOIN Customers C ON A.customer_id = C.customer_id;
-- 3. Write a SQL query to increase the balance of a specific account by a certain amount. 
UPDATE Accounts
SET balance = balance + 1000.00
WHERE account_id = 5;
-- 4. Write a SQL query to Combine first and last names of customers as a full_name.
SELECT CONCAT(first_name, ' ', last_name) AS full_name FROM Customers;
-- 5. Write a SQL query to remove accounts with a balance of zero where the account  type is savings. 
DELETE FROM Accounts WHERE balance = 0.0 AND acount_type = 'savings';
-- 6. Write a SQL query to Find customers living in a specific city.
SELECT * FROM Customers WHERE address LIKE '%seocho-gu%';
-- 7. Write a SQL query to Get the account balance for a specific account. 
SELECT balance FROM Accounts WHERE account_id = 3;
-- 8. Write a SQL query to List all current accounts with a balance greater than $1,000.
SELECT * FROM Accounts WHERE acount_type = 'current' AND balance > 1000.00;
-- 9. Write a SQL query to Retrieve all transactions for a specific account. 
SELECT * FROM Transactions WHERE account_id = 7;
-- 10. Write a SQL query to Calculate the interest accrued on savings accounts based on a  given interest rate. 
SELECT account_id, balance * 0.05 AS interest_accrued FROM Accounts WHERE acount_type = 'savings';
-- 11. Write a SQL query to Identify accounts where the balance is less than a specified  overdraft limit.
SELECT * FROM Accounts WHERE balance < 500.00;
-- 12. Write a SQL query to Find customers not living in a specific city. 
SELECT * FROM Customers WHERE address NOT LIKE '%seocho-gu%';

use hmbank;
-- task 4
-- 1 : Write a SQL query to Find the average account balance for all customers.
SELECT AVG(balance) AS average_balance FROM Accounts;
-- 2 : Write a SQL query to Retrieve the top 10 highest account balances. 
SELECT * FROM Accounts ORDER BY balance DESC LIMIT 10;
-- 3 : Write a SQL query to Calculate Total Deposits for All Customers in specific date.
SELECT SUM(amount) AS total_deposits FROM Transactions WHERE transaction_type = 'deposit' AND transaction_date = '2023-06-30';
-- 4 : Write a SQL query to Find the Oldest and Newest Customers
(SELECT * FROM Customers ORDER BY DOB LIMIT 1) UNION ( SELECT * FROM Customers ORDER BY DOB DESC LIMIT 1); 
-- 5 : Write a SQL query to Retrieve transaction details along with the account type. 
SELECT T.*, A.acount_type FROM Transactions T JOIN Accounts A ON T.account_id = A.account_id;
-- 6 : Write a SQL query to Get a list of customers along with their account details.
SELECT C.*, A.account_id, A.acount_type,  A.balance FROM Customers C JOIN Accounts A ON C.customer_id = A.customer_id;
-- 7 : Write a SQL query to Retrieve transaction details along with customer information for a  specific account. 
SELECT T.*, C.*, A.acount_type, A.balance FROM Transactions T JOIN Accounts A ON T.account_id = A.account_id JOIN Customers C ON A.customer_id = C.customer_id WHERE T.account_id = 3;
-- 8 : Write a SQL query to Identify customers who have more than one account. 
SELECT customer_id, COUNT(*) AS num_accounts FROM Accounts GROUP BY customer_id HAVING COUNT(*) > 1;
-- 9 : Write a SQL query to Calculate the difference in transaction amounts between deposits and  withdrawals. Doubt
select account_id, sum(amount * (transaction_type = 'deposit') - amount * (transaction_type ='withdrawal')) as transaction_difference from Transactions group by account_id;
-- 10 : Write a SQL query to Calculate the average daily balance for each account over a specified  period. Doubt
select c.customer_id, avg(a.balance) as avg_balance from Customers c join Accounts a on c.customer_id = a.customer_id group by c.customer_id;

-- 11 : Calculate the total balance for each account type.
SELECT acount_type, SUM(balance) AS total_balance FROM Accounts GROUP BY acount_type;
 
-- 12 : Identify accounts with the highest number of transactions order by descending order
SELECT account_id, COUNT(*) AS num_transactions FROM Transactions
GROUP BY account_id ORDER BY num_transactions DESC;

-- 13 : List customers with high aggregate account balances, along with their account types.
SELECT C.customer_id,C.first_name,C.last_name,A.acount_type, SUM(A.balance) AS total_balance
FROM Customers C JOIN Accounts A ON C.customer_id = A.customer_id
GROUP BY C.customer_id, A.acount_type HAVING SUM(A.balance) > 4000 ORDER BY total_balance DESC;
 
-- 14 : Identify and list duplicate transactions based on transaction amount, date, and account.
SELECT *
FROM Transactions
WHERE (amount, transaction_date, account_id) IN (
    SELECT amount, transaction_date, account_id
    FROM Transactions
    GROUP BY amount, transaction_date, account_id
    HAVING COUNT(*) > 1
)
ORDER BY amount, transaction_date, account_id;



-- task 5


INSERT INTO Customers (customer_id, first_name, last_name, DOB, email, phone_number, address)
VALUES 
    (29, 'Sangmi', 'Park', '1990-05-12', 'sangmi.park@email.com', 1010101010, '123 Hongdae Street, Mapo-gu, Seoul'),
    (30, 'Hyunjin', 'Kim', '1993-09-28', 'hyunjin.kim@email.com', 1010102020, '456 Sinchon-dong, Mapo-gu, Seoul');


INSERT INTO Accounts (account_id, customer_id, acount_type, balance)
VALUES 
    (11, 29, 'savings', 4000.00),
    (12, 29, 'current', 6500.25),
    (13, 30, 'savings', 300.00),
    (14, 30, 'zero_balance', 0.00);

INSERT INTO Transactions (transaction_id, account_id, transaction_type, amount, transaction_date)
VALUES 
    (111, 11, 'deposit', 1200.00, '2023-11-05'),
    (112, 12, 'withdrawal', 400.25, '2023-12-10'),
    (113, 13, 'deposit', 50.00, '2023-11-15'),
    (114, 14, 'withdrawal', 0.00, '2023-12-20');
    
INSERT INTO Accounts (account_id, customer_id, acount_type, balance)
VALUES 
    (15, 29, 'current', 7000.00),
    (16, 30, 'savings', 2500.50);



select * from transactions;
    
select * from accounts;
select * from customers;


-- 1 : Retrieve the customer(s) with the highest account balance. ok
SELECT customer_id, balance FROM Accounts WHERE balance = ( SELECT MAX(balance) FROM Accounts );

-- 2 : Calculate the average account balance for customers who have more than one account. ok
 select avg(balance),customer_id from accounts where customer_id in (select customer_id from accounts group by customer_id having count(*)>1) group by customer_id ;
 
 -- 3 : Retrieve accounts with transactions whose amounts exceed the average transaction amount. ok
 #select avg(amount) from transactions;
select account_id from transactions where amount > (select avg(amount) from transactions);

-- 4: Identify customers who have no recorded transactions. ok
select first_name, last_name, customer_id from customers c where not exists
	(select * from accounts a join transactions on a.customer_id = c.customer_id);
    
-- 5 : Calculate the total balance of accounts with no recorded transactions. ok
select sum(balance) as total_balance from accounts a where not exists ( select * from transactions t where t.account_id = a.account_id);
						-- or --
select sum(balance) as total_balance from accounts where account_id not in ( select account_id from transactions);
						-- or --
select sum(balance), c.customer_id, c.first_name, c.last_name from Customers c 
	left join accounts a on c.customer_id = a.customer_id
	left join transactions t on a.account_id = t.account_id
	where transaction_id is NULL group by  c.customer_id, c.first_name, c.last_name;-- using joins showing customers who own the accounts that have no recorded transaction


-- 6 : Retrieve transactions for accounts with the lowest balance. ok
select account_id, transaction_id,transaction_date from transactions where account_id in (select account_id from accounts where balance in (select min(balance) from accounts));

-- 7 : Identify customers who have accounts of multiple types. ok
SELECT customer_id, first_name, last_name FROM Customers WHERE customer_id IN (
    SELECT customer_id FROM Accounts GROUP BY customer_id HAVING COUNT(DISTINCT acount_type) > 1;

    
-- 8 : Calculate the percentage of each account type out of the total number of accounts. ok

SELECT acount_type, COUNT(*) AS num_accounts, (COUNT(*) / (SELECT COUNT(*) FROM Accounts)*100) AS percentage FROM Accounts GROUP BY acount_type;

-- 9 : Retrieve all transactions for a customer with a given customer_id. ok
select * from transactions where account_id in (select account_id from accounts where customer_id = 23);


-- 10 : Calculate the total balance for each account type, including a subquery within the SELECT clause. ok
select acount_type, sum(balance) as total_balance from accounts where balance in (select balance from accounts) group by acount_type;











