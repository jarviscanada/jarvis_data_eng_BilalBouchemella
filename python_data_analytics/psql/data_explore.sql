-- Show table schema 
\d+ retail;

-- Show first 10 rows
SELECT * FROM retail limit 10;

-- Check # of records
SELECT COUNT(*) from retail;

-- number of clients (e.g. unique client ID)
SELECT COUNT(DISTINCT customer_id) from retail;

-- invoice date range (e.g. max/min dates)
SELECT MAX(invoice_date), MIN(invoice_date) from retail;

-- number of SKU/merchants (e.g. unique stock code)
SELECT COUNT(DISTINCT stock_code) from retail;

-- average invoice amount excluding invoices with a negative amount
SELECT AVG(amount)
FROM (
    SELECT invoice_no, SUM(quantity * unit_price) AS amount
    FROM retail
    GROUP BY invoice_no
    HAVING SUM(quantity * unit_price) > 0
) t;

-- Calculate total revenue 
SELECT SUM(unit_price * quantity) from retail;

-- Calculate total revenue by YYYYMM 
SELECT
    (EXTRACT(YEAR FROM invoice_date)*100 + EXTRACT(MONTH FROM invoice_date)) AS yyyymm,
    SUM(quantity * unit_price)
FROM retail
GROUP BY yyyymm
ORDER BY yyyymm;
