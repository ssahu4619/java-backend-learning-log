# Databases, MySQL & SQL — Comprehensive Interview Prep Guide for Java Developers (5+ YOE)

> A complete reference covering fundamentals to expert internals, with SQL & Java code examples, comparison tables, and 150+ interview Q&A. Treats the reader as a strong Java engineer learning databases deeply.

---

# PART I — DATABASE FUNDAMENTALS

## 1.1 Database, DBMS, RDBMS

- **Database**: organized, persistent, queryable, concurrent-access collection of data.
- **DBMS**: software managing a database (storage, retrieval, security, concurrency). E.g., MySQL, MongoDB.
- **RDBMS**: DBMS organizing data in **tables (relations)** with rows & columns, enforcing relationships via keys, using **SQL**. E.g., MySQL, PostgreSQL, Oracle.

| Aspect | DBMS | RDBMS |
|---|---|---|
| Structure | Any (file/hierarchical) | Tables with rows & columns |
| Relationships | Limited | Enforced via FKs |
| Query language | Proprietary | Standard SQL |
| ACID | Sometimes | Yes (typically) |

## 1.2 Types of Databases

**Relational vs NoSQL**

| Feature | Relational | NoSQL |
|---|---|---|
| Schema | Rigid | Flexible |
| Scaling | Vertical primarily | Horizontal |
| ACID | Strong | Often BASE |
| Joins | Native | Limited |
| Examples | MySQL, PostgreSQL | MongoDB, Cassandra, Redis, Neo4j |

NoSQL subtypes: **Document** (MongoDB), **Key-Value** (Redis, DynamoDB), **Column-family** (Cassandra), **Graph** (Neo4j).

**OLTP vs OLAP**

| Feature | OLTP | OLAP |
|---|---|---|
| Purpose | Day-to-day transactions | Analysis |
| Operations | Many small writes | Few large aggregations |
| Schema | Normalized (3NF) | Denormalized (star/snowflake) |
| Response time | ms | sec–min |
| Examples | MySQL, PostgreSQL | Snowflake, Redshift, BigQuery |

## 1.3 DB Query Optimization

### 1. Indexing (Most Asked!)
- Add indexes on columns used in `WHERE`, `JOIN`, `ORDER BY`, `GROUP BY`
- Use composite indexes for multi-column filters
- Avoid over-indexing (slows down writes)

```sql
CREATE INDEX idx_emp_salary ON employees(salary);
```

### 2. Avoid `SELECT *`
```sql
-- Bad
SELECT * FROM employees;

-- Good
SELECT employee_id, salary FROM employees;
```
- Fetches only needed columns → less I/O

### 3. Use `EXPLAIN` / `EXPLAIN ANALYZE`
```sql
EXPLAIN SELECT * FROM employees WHERE salary > 50000;
```
- Reveals full table scans, missing indexes, cost estimates
- First thing to do when a query is slow

### 4. Avoid N+1 Query Problem
```sql
-- Bad: 1 query per employee
SELECT * FROM departments WHERE id = ?  -- runs N times

-- Good: Single JOIN
SELECT e.*, d.name 
FROM employees e 
JOIN departments d ON e.dept_id = d.id;
```

### 5. Query Rewriting Tips

| Avoid | Use Instead |
| :--- | :--- |
| `OR` on indexed cols | `UNION` |
| `NOT IN` | `NOT EXISTS` |
| Function on column in `WHERE` | Rewrite condition |
| Correlated subquery | `JOIN` |

```sql
-- Bad (function kills index)
WHERE YEAR(created_at) = 2024

-- Good
WHERE created_at BETWEEN '2024-01-01' AND '2024-12-31'
```

### 6. Pagination Optimization
```sql
-- Bad (slow for large offsets)
SELECT * FROM employees LIMIT 10 OFFSET 100000;

-- Good (keyset pagination)
SELECT * FROM employees 
WHERE id > 100000 
LIMIT 10;
```

### 7. Caching
- Use Redis/Memcached for frequently read, rarely changed data
- Query result caching at application layer
- Database-level query cache (MySQL)

### 8. Partitioning & Sharding
- **Partitioning**: Split large table into smaller chunks (by date, range, list)
- **Sharding**: Distribute data across multiple DB servers
- Useful for tables with millions of rows

### 9. Connection Pooling
- Reuse DB connections instead of creating new ones each time
- **Tools**: HikariCP (Java), pgBouncer (PostgreSQL)

### 10. Normalization vs Denormalization
- **Normalize** to reduce redundancy
- **Denormalize** strategically for read-heavy systems (avoid expensive `JOIN`s)

---

### 🎯 Quick Answer Formula for Interviews:
> *"First I'd run `EXPLAIN` to identify bottlenecks, then check for missing indexes, rewrite inefficient subqueries as `JOIN`s, avoid `SELECT *`, use pagination properly, and consider caching for repeated queries."*

### What is difference bettween group by Where and having in sql query.

GROUP BY groups rows that have the same values into summary rows. It's what lets you run aggregate functions (COUNT, SUM, AVG, MAX, MIN) per group rather than over the whole table.
HAVING filters those groups after they've been formed. It's like a WHERE clause, but for aggregated results.
The key difference comes down to WHERE vs HAVING, and that's really what interviewers are testing:

WHERE filters individual rows before grouping happens. It cannot use aggregate functions.
HAVING filters groups after grouping happens. It can use aggregate functions.

A quick example. Say you have an orders table and you want to find customers who've placed more than 5 orders, but only counting orders above ₹100:

SELECT customer_id, COUNT(*) AS order_count
FROM orders
WHERE amount > 100        -- filters rows BEFORE grouping
GROUP BY customer_id       -- groups by customer
HAVING COUNT(*) > 5        -- filters groups AFTER aggregation
ORDER BY order_count DESC;

So the flow is: WHERE trims the raw rows first → GROUP BY buckets them by customer → aggregate (COUNT) runs per bucket → HAVING throws out buckets that don't meet the aggregate condition.
One-liner to remember for the interview: WHERE filters rows before grouping, HAVING filters groups after grouping—and only HAVING can use aggregate functions.
A common follow-up they ask: "Can you use HAVING without GROUP BY?"
 Yes, you can—it then treats the entire result set as one group. For example, SELECT COUNT(*) FROM orders HAVING COUNT(*) > 100 returns the count only if there are more than 100 rows total. Rare in practice, but good to know.

### Difference between DELETE, TRUNCATE, and DROP?
The short version: all three remove things, but at different levels. DELETE removes rows, TRUNCATE removes all rows but keeps the table, DROP removes the entire table itself.
Here's the breakdown:
## DELETE

Removes rows one at a time, and can be filtered with a WHERE clause (DELETE FROM employees WHERE dept = 'Sales').
It's a DML command (Data Manipulation Language).
Logs each row deletion, so it's slower on large tables.
Can be rolled back (it's transactional — wrap it in a transaction and you can undo it).
Fires triggers (e.g., an ON DELETE trigger).
Does not reset the AUTO_INCREMENT / identity counter.

## TRUNCATE

Removes all rows from a table at once — no WHERE clause allowed.
It's a DDL command (Data Definition Language).
Deallocates the data pages instead of logging each row, so it's much faster than DELETE.
Generally cannot be rolled back (in most databases like MySQL it auto-commits; though in some like SQL Server it can be rolled back inside an explicit transaction — worth mentioning if pressed).
Does not fire row-level triggers.
Resets the AUTO_INCREMENT / identity counter back to the start.
Keeps the table structure, columns, indexes, and constraints intact.

## DROP

Removes the entire table — structure, data, indexes, constraints, triggers, everything. The table no longer exists.
It's a DDL command.
Cannot be rolled back (in most databases).
You'd have to recreate the table from scratch to use it again.

One-liner to remember for the interview: DELETE removes rows (and can filter, and can roll back), TRUNCATE wipes all rows fast but keeps the table, DROP destroys the table entirely.
A couple of follow-ups they love to ask:
"Which is faster, DELETE or TRUNCATE, and why?" — TRUNCATE, because it deallocates data pages in one go rather than logging each row deletion individually.
"Can you roll back a TRUNCATE?" — The safe answer: generally no, because it's DDL and auto-commits in most systems. But note that SQL Server allows it within an explicit transaction. This nuance shows depth.
"If you want to delete specific rows, which do you use?" — DELETE with a WHERE clause, since TRUNCATE can't filter.

### What are the different types of JOINs? (INNER, LEFT, RIGHT, FULL OUTER, CROSS, SELF)

Good one—this is the single most asked SQL question, so let's nail it cold. I'll use two sample tables throughout so the queries stay consistent:

`employees` (emp_id, emp_name, dept_id) and `departments` (dept_id, dept_name)

**INNER JOIN**

Returns only rows that have a match in *both* tables. If an employee has no department, or a department has no employees, they're excluded.

```sql
SELECT e.emp_name, d.dept_name
FROM employees e
INNER JOIN departments d
  ON e.dept_id = d.dept_id;
```

**LEFT JOIN** (LEFT OUTER JOIN)

Returns *all* rows from the left table, plus matching rows from the right. Where there's no match, the right side columns come back as NULL. Great for "show me all employees, even those not assigned to a department."

```sql
SELECT e.emp_name, d.dept_name
FROM employees e
LEFT JOIN departments d
  ON e.dept_id = d.dept_id;
```

**RIGHT JOIN** (RIGHT OUTER JOIN)

The mirror image—returns *all* rows from the right table, plus matching rows from the left. Useful for "show me all departments, even empty ones." Less common in practice because most people just flip the tables and use LEFT JOIN.

```sql
SELECT e.emp_name, d.dept_name
FROM employees e
RIGHT JOIN departments d
  ON e.dept_id = d.dept_id;
```

**FULL OUTER JOIN**

Returns *all* rows from both tables. Matches line up; where there's no match on either side, the missing side is NULL. So you get unmatched employees *and* empty departments.

```sql
SELECT e.emp_name, d.dept_name
FROM employees e
FULL OUTER JOIN departments d
  ON e.dept_id = d.dept_id;
```

One gotcha worth knowing: **MySQL doesn't support FULL OUTER JOIN directly**—you simulate it by UNION-ing a LEFT JOIN and a RIGHT JOIN. PostgreSQL, SQL Server, and Oracle support it natively. Interviewers sometimes drop this to see if you know your database quirks.

**CROSS JOIN**

Returns the Cartesian product—every row from the first table paired with every row from the second. No ON condition. If you have 10 employees and 5 departments, you get 50 rows. Rarely used intentionally, but handy for generating combinations (e.g., all size-color pairings of a product).

```sql
SELECT e.emp_name, d.dept_name
FROM employees e
CROSS JOIN departments d;
```

**SELF JOIN**

A table joined to itself, using table aliases to treat it as two separate tables. The classic use case is hierarchical data—like finding each employee's manager when the manager is also a row in the same employees table.

```sql
SELECT e.emp_name AS employee, m.emp_name AS manager
FROM employees e
JOIN employees m
  ON e.manager_id = m.emp_id;
```

Here `e` is the employee and `m` is the manager, both pulled from the same table.

One-liner to remember: **INNER = only matches; LEFT = all left + matches; RIGHT = all right + matches; FULL = everything from both; CROSS = every combination; SELF = table joined to itself.**

The follow-up they almost always ask: *"What's the difference between a LEFT JOIN and an INNER JOIN in terms of result count?"* Answer: a LEFT JOIN returns at least as many rows as INNER, because it keeps the unmatched left rows with NULLs, whereas INNER drops them.

Another favorite: *"How do you find employees with no department?"* That's a LEFT JOIN with a NULL filter:

```sql
SELECT e.emp_name
FROM employees e
LEFT JOIN departments d
  ON e.dept_id = d.dept_id
WHERE d.dept_id IS NULL;
```

That `WHERE ... IS NULL` trick after a LEFT JOIN is a hugely common pattern—worth memorizing.

-------------
Great instinct—seeing actual rows is the fastest way to make JOINs click. Let me set up sample data and show you exactly what each JOIN returns.Perfect—this is exactly the kind of thing that clicks the moment you see real rows. Let me set up two small tables and show you what every JOIN returns.

Here's our sample data. Notice I've deliberately made it messy so the differences show up: Rahul's `dept_id` (30) has no matching department, Sneha has no `dept_id` at all (NULL), and Marketing (40) has no employees.Now here's *why* each result looks the way it does—read these against the tables above:
![alt text](image-1.png)

**INNER JOIN** — only Amit (10) and Priya (20) survive, because their `dept_id` exists in both tables. Rahul (30), Sneha (NULL), and Marketing (40) all get dropped because they have no match on the other side.

**LEFT JOIN** — keeps all 4 employees no matter what. Rahul and Sneha show up with `dept_name = NULL` because there's nothing to match them to. Marketing still vanishes (it's on the right).

**RIGHT JOIN** — flips it. Keeps all 3 departments. Now Marketing appears with `name = NULL` (no employee works there). Rahul and Sneha disappear because they're on the left.

**FULL OUTER JOIN** — keeps everything from both sides. You get the matched pairs, plus Rahul and Sneha with NULL departments, plus Marketing with a NULL name. Nothing is lost.

**CROSS JOIN** — no matching condition at all, so every employee pairs with every department: 4 × 3 = 12 rows. This is why an accidental CROSS JOIN (forgetting your ON clause) can explode a result set.

The mental shortcut: look at where the NULLs land. **NULLs on the right side = unmatched left rows kept (LEFT). NULLs on the left = unmatched right rows kept (RIGHT). NULLs on both = FULL.**

**SELF JOIN** is the odd one out—it needs a different table because it joins a table to itself. Picture an `employees` table where each person's manager is also an employee in the same table:

| emp_id | emp_name | manager_id |
|--------|----------|------------|
| 1 | Amit | 3 |
| 2 | Priya | 3 |
| 3 | Rahul | NULL |

Joining `e.manager_id = m.emp_id` gives you each person next to their manager's name:

| employee | manager |
|----------|---------|
| Amit | Rahul |
| Priya | Rahul |

Rahul drops out here (with an INNER self join) because his `manager_id` is NULL—he's the boss. If you wanted Rahul to appear with a NULL manager, you'd use a LEFT self join instead.

Does seeing the actual rows make it click now? If you want, I can give you a small puzzle—show you two tables and have you predict the output row count for each join before checking.

### Difference between UNION and UNION ALL
Good one—this comes up constantly and there's a performance angle interviewers love to dig into.

Both **UNION** and **UNION ALL** combine the result sets of two or more SELECT queries into one. The queries must have the same number of columns and compatible data types. The difference is what they do with duplicates:

**UNION** removes duplicate rows. It returns only distinct rows across the combined result. To do that, the database has to compare every row against every other row—effectively a sort or hash operation under the hood—which costs extra processing time.

**UNION ALL** keeps everything, including duplicates. It just stacks the result sets on top of each other with no deduplication. Because it skips that comparison step, it's **faster**.

Here's a quick example. Say you have two tables:

`online_customers` → Amit, Priya, Rahul
`store_customers` → Priya, Sneha

**UNION** gives you 4 rows (Priya appears once):

| customer |
|----------|
| Amit |
| Priya |
| Rahul |
| Sneha |

**UNION ALL** gives you 5 rows (Priya appears twice):

| customer |
|----------|
| Amit |
| Priya |
| Rahul |
| Priya |
| Sneha |

One-liner to remember: **UNION removes duplicates and is slower; UNION ALL keeps everything and is faster.**

The follow-ups they almost always ask:

*"Which one should you use by default?"* — Use UNION ALL unless you specifically need to eliminate duplicates. People reach for UNION out of habit and pay the dedup cost even when the data has no duplicates, or when duplicates are actually fine. If you know the result sets won't overlap, UNION ALL is the right call.

*"How would you remove duplicates with UNION ALL if you needed to?"* — You'd wrap it and apply DISTINCT or GROUP BY yourself, but at that point UNION is usually cleaner.

*"Does UNION sort the result?"* — Not guaranteed in any defined order, but because many engines use a sort to deduplicate, the output often *appears* sorted. Never rely on that—if you need ordering, add an explicit ORDER BY.

Want me to fire the next one at you, or quiz you on these to lock them in?
### Code Examples

Here you go—straightforward syntax for both, using the same two-table example.

**UNION** (removes duplicates):

```sql
SELECT customer_name FROM online_customers
UNION
SELECT customer_name FROM store_customers;
```

**UNION ALL** (keeps duplicates):

```sql
SELECT customer_name FROM online_customers
UNION ALL
SELECT customer_name FROM store_customers;
```

That's the core of it. A few practical variations worth knowing, since interviewers like to see you handle real cases:

**Combining specific columns from different tables** (column names don't have to match, but count and types must):

```sql
SELECT emp_name, salary FROM permanent_employees
UNION ALL
SELECT contractor_name, hourly_rate FROM contractors;
```

The result uses the column names from the *first* SELECT (`emp_name`, `salary`).

**Adding a literal to tag where each row came from**—a genuinely useful pattern:

```sql
SELECT customer_name, 'online' AS source FROM online_customers
UNION ALL
SELECT customer_name, 'store'  AS source FROM store_customers;
```

Now every row tells you which table it originated from. This is handy when you UNION ALL and want to keep that distinction.

**With ORDER BY**—the catch here is that ORDER BY goes at the very end and applies to the *whole* combined result, not the individual SELECTs:

```sql
SELECT customer_name FROM online_customers
UNION
SELECT customer_name FROM store_customers
ORDER BY customer_name;
```

You can't put an ORDER BY on the first SELECT—it has to sit at the bottom and sort the final merged set.

**With WHERE on each part**—each SELECT can have its own filtering:

```sql
SELECT customer_name FROM online_customers WHERE city = 'Mumbai'
UNION ALL
SELECT customer_name FROM store_customers  WHERE city = 'Mumbai';
```

The two rules that catch people out: every SELECT must have the **same number of columns** in the **same order with compatible types**, and **ORDER BY only works once, at the end**. Break either one and you get an error.

### What is an index? What are clustered vs non-clustered indexes, and when can an index actually hurt performance?
On the clustered index — yes, one clustered index per table, and it's typically on the primary key. But two refinements:
First, the clustered index doesn't have to be on the primary key—that's just the common default. You can create a clustered index on a different column if you want the data physically sorted some other way. The primary key being clustered is a convention (and the default in SQL Server / InnoDB), not a hard rule.
Second—and this is the bit interviewers poke at—a clustered index can itself span multiple columns (a composite clustered index). So "one clustered index" doesn't mean "one column." It means the table can only be physically sorted one way, but that one way can be defined by several columns together. For example:
CREATE CLUSTERED INDEX idx_orders
ON orders (customer_id, order_date);
Here the data is physically sorted first by customer_id, then by order_date within each customer. Still one clustered index, but two columns.
On non-clustered indexes — here's where I want to clarify your wording. You said a non-clustered index "can have multiple columns." That's true, but there are actually two separate ideas people mix up:
One — a single non-clustered index can be composite (multiple columns in one index), exactly like the clustered example above:
CREATE INDEX idx_emp_dept_salary
ON employees (dept_id, salary);
This is one index built on two columns together.
Two — a table can have many non-clustered indexes, each on different columns:
CREATE INDEX idx_name  ON employees (emp_name);
CREATE INDEX idx_email ON employees (email);
CREATE INDEX idx_city  ON employees (city);

That's three separate indexes.
So the full, precise statement is: a table has at most one clustered index (which may be single or multi-column), but can have many non-clustered indexes (each of which may itself be single or multi-column).
One thing worth knowing about composite indexes, since it's a classic follow-up: column order matters. An index on (dept_id, salary) helps queries that filter on dept_id alone, or on dept_id and salary together—but it does not efficiently help a query that filters on salary alone. This is called the leftmost prefix rule: the index can only be used starting from the left-most column. So you'd order composite index columns by which one you filter on most, putting the most-used (and usually most selective) column first.
Quick interview-ready summary of your question: clustered—one per table, can be multi-column, usually but not necessarily the primary key. Non-clustered—many per table, each can also be multi-column, and column order follows the leftmost prefix rule.
