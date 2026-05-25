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
