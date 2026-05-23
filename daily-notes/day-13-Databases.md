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