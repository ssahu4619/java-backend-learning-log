# Spring JDBC

## Q1: What is Spring JDBC? How is it different from JDBC?

**Spring JDBC** is an abstraction layer provided by the Spring Framework on top of standard Java JDBC. It simplifies database operations by removing the need to write tedious boilerplate code associated with connecting to a database, executing queries, and handling exceptions.

**Differences between Spring JDBC and standard JDBC:**

| Feature | Standard JDBC | Spring JDBC |
| :--- | :--- | :--- |
| **Boilerplate Code** | High. Requires manually opening/closing connections, statements, and ResultSets. | Low. Spring manages resource creation and release automatically. |
| **Exception Handling** | Throws checked `SQLException` which must be caught or declared everywhere. | Translates `SQLException` into Spring's unchecked `DataAccessException` hierarchy. |
| **Resource Management** | Prone to resource leaks if `finally` blocks are missed. | Safe. Resources are automatically closed by the framework. |
| **Code Readability** | Verbose and repetitive. | Clean, concise, and focused on business logic. |

---

## Q2: What is JdbcTemplate?

`JdbcTemplate` is the central and most important class in the Spring JDBC core package. It simplifies the use of JDBC and helps to avoid common errors. It executes SQL queries, updates statements, and stored procedure calls, performs iteration over `ResultSet`s, and extracts returned parameter values. 

It requires a `DataSource` to connect to the database.

**Code Example:**

```java
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class EmployeeDao {
    private JdbcTemplate jdbcTemplate;

    // Injecting the DataSource to create JdbcTemplate
    public EmployeeDao(DriverManagerDataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // Example of executing an INSERT statement
    public int addEmployee(String name, String department) {
        String sql = "INSERT INTO employees (name, department) VALUES (?, ?)";
        return jdbcTemplate.update(sql, name, department);
    }
    
    // Example of executing a simple SELECT statement to get a count
    public int getEmployeeCount() {
        String sql = "SELECT COUNT(*) FROM employees";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
}
```

---

## Q3: What is RowMapper?

`RowMapper` is an interface used by `JdbcTemplate` for mapping rows of a `ResultSet` on a per-row basis. When you query for multiple objects, Spring JDBC iterates over the `ResultSet` for you, and for each row, it calls the `mapRow` method of your `RowMapper` implementation to map the row data to a Java object.

**Code Example:**

First, let's say we have an `Employee` model:
```java
public class Employee {
    private int id;
    private String name;
    private String department;
    
    // Getters and Setters omitted for brevity
}
```

Now, create a custom `RowMapper`:
```java
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeRowMapper implements RowMapper<Employee> {
    
    @Override
    public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
        Employee employee = new Employee();
        employee.setId(rs.getInt("id"));
        employee.setName(rs.getString("name"));
        employee.setDepartment(rs.getString("department"));
        return employee;
    }
}
```

Using the `RowMapper` with `JdbcTemplate` to fetch a list of employees:
```java
import java.util.List;

public class EmployeeDao {
    private JdbcTemplate jdbcTemplate;

    public EmployeeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Using RowMapper to fetch a list of objects
    public List<Employee> getAllEmployees() {
        String sql = "SELECT id, name, department FROM employees";
        return jdbcTemplate.query(sql, new EmployeeRowMapper());
    }

    // Using RowMapper to fetch a single object
    public Employee getEmployeeById(int id) {
        String sql = "SELECT id, name, department FROM employees WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new EmployeeRowMapper(), id);
    }
}
```
---

## Q4: What is CrudRepository?

`CrudRepository` is a Spring Data interface that provides generic CRUD (Create, Read, Update, Delete) operations for a specific repository. It saves you from writing boilerplate DAO code for standard database operations. You simply extend this interface, and Spring Data automatically provides the implementation at runtime.

**Key Methods:**
- `save(entity)`: Saves or updates an entity.
- `findById(id)`: Retrieves an entity by its ID.
- `findAll()`: Retrieves all entities.
- `deleteById(id)`: Deletes an entity by its ID.

**Code Example:**
```java
import org.springframework.data.repository.CrudRepository;

// <Employee, Integer> represents the Entity class and the type of its primary key
public interface EmployeeRepository extends CrudRepository<Employee, Integer> {
    // Basic CRUD methods are automatically available!
}
```

---

## Q5: What is PagingAndSortingRepository?

`PagingAndSortingRepository` is an extension of `CrudRepository` in Spring Data. It adds additional methods that allow you to retrieve entities with pagination and sorting applied. This is extremely useful when dealing with large datasets where you only want to load a small "page" of data at a time to save memory and improve performance.

**Key Methods:**
- `findAll(Sort sort)`: Retrieves all entities sorted by the given options.
- `findAll(Pageable pageable)`: Retrieves a `Page` of entities meeting the paging restriction.

**Code Example:**
```java
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public interface EmployeePagingRepository extends PagingAndSortingRepository<Employee, Integer> {
}

// Usage in a Service:
// Fetch the first page (page 0), with 10 items per page, sorted by "name"
// Page<Employee> page = employeePagingRepository.findAll(
//     PageRequest.of(0, 10, Sort.by("name").ascending())
// );
```

---

## Q6: Why do we need a DataSource?

A `DataSource` is a factory for connections to the physical database. It is a standard Java interface (`javax.sql.DataSource`) used as an alternative to `DriverManager`. 

We need a `DataSource` for several critical reasons:
1. **Connection Pooling**: Instead of opening and closing a new database connection for every request (which is very slow), a `DataSource` usually provides a connection pool (like HikariCP). It reuses existing connections, drastically improving application performance.
2. **Abstraction**: It abstracts away the database driver details. The application code (like `JdbcTemplate` or Hibernate) only needs the `DataSource` to get a connection, without hardcoding database-specific driver logic.
3. **Configuration Management**: It centrally manages database configuration properties like URL, username, password, and connection limits.

**Code Example (Spring Boot `application.properties`):**
Spring Boot automatically configures a `DataSource` bean based on these properties:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/mydb
spring.datasource.username=root
spring.datasource.password=secret
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Connection Pool Settings (Hikari is default in Spring Boot)
spring.datasource.hikari.maximum-pool-size=10
```

---

# Spring AOP (Aspect-Oriented Programming)

## Q7: What is AOP?
**Aspect-Oriented Programming (AOP)** is a programming paradigm that aims to increase modularity by allowing the separation of cross-cutting concerns. While Object-Oriented Programming (OOP) modularizes your application into classes, AOP modularizes "aspects" — logic that spans across multiple layers and classes.

## Q8: What is a Cross-Cutting Concern?
A **cross-cutting concern** is a functionality or logic that is tangled with the core business logic and affects multiple parts of an application. Instead of duplicating this code across many methods, AOP allows you to extract it into a single, reusable component.
**Common Examples:** 
- Logging
- Security / Authentication
- Transaction Management
- Performance Monitoring
- Error Handling

## Q9: Key AOP Terminology

- **Aspect**: A module (usually a Java class) that encapsulates a cross-cutting concern. For example, a `LoggingAspect` class.
- **Advice**: The actual action taken by an aspect at a particular point. This is the *method* containing the cross-cutting code. Types include:
  - `@Before`: Runs before a method execution.
  - `@After`: Runs after a method execution.
  - `@Around`: Runs before and after a method execution.
- **Joinpoint**: A specific point during the execution of a program where an aspect can be plugged in. In Spring AOP, a joinpoint always represents a **method execution**.
- **Pointcut**: An expression that matches joinpoints. While a joinpoint is any possible execution point, a pointcut specifies *exactly which* joinpoints the advice should be applied to (e.g., "Only execute on methods in the `com.example.service` package").
- **Weaving**: The process of linking aspects with other application objects to create an "advised" object. Spring AOP performs weaving at runtime by creating dynamic proxies.
- **Weaver**: The framework, tool, or compiler that performs the weaving process. In our case, the Spring AOP framework is the weaver.

## Q10: What is an Aspect and a Pointcut in AOP?
- **Aspect**: An aspect is the modularization of a concern that cuts across multiple classes. It is essentially a normal Java class annotated with `@Aspect` that contains **advices** (what to do) and **pointcuts** (where to do it). For example, a `TransactionAspect` manages database transactions across various service layer methods.
- **Pointcut**: A pointcut is an expression that selects one or more Joinpoints (method executions in Spring) where an advice should be applied. While a Joinpoint represents *any* opportunity to apply an aspect, a Pointcut defines the *exact rule* for matching those opportunities. 
  - *Example Expression*: `@Pointcut("execution(* com.example.service.*.*(..))")` — This selects all methods in any class inside the `com.example.service` package.

## Q11: What are the different types of AOP Advice?
Advice is the actual code/action taken by an aspect at a selected Joinpoint. Spring AOP supports five types of advice:
1. **`@Before`**: Executes *before* the joinpoint method runs. It cannot prevent the execution of the target method unless it throws an exception.
2. **`@AfterReturning`**: Executes only if the joinpoint method completes *normally* (returns a value without throwing an exception). You can access the return value inside the advice.
3. **`@AfterThrowing`**: Executes only if the joinpoint method exits by *throwing an exception*. Commonly used for centralized error logging.
4. **`@After` (Finally)**: Executes *regardless* of how the joinpoint method exits (normal return or exception). It acts similar to a `finally` block in standard Java.
5. **`@Around`**: The most powerful type of advice. It completely *surrounds* the target method, allowing you to execute custom behavior both before and after the invocation. You must explicitly call `ProceedingJoinPoint.proceed()` to trigger the actual target method, giving you the power to modify arguments, alter return values, or skip execution entirely.

## Q12: What is Weaving?
**Weaving** is the process of applying Aspects to your target objects to create an "advised" object. It is the mechanism that actually links your cross-cutting aspect code with your core business logic.
Weaving can happen at different stages of an application's lifecycle:
1. **Compile-time weaving**: Aspects are woven directly into the `.class` files by a special compiler (like the AspectJ compiler).
2. **Load-time weaving**: Aspects are woven into the classes dynamically as they are loaded into the JVM by a classloader.
3. **Runtime weaving**: Aspects are woven during the application's execution. **Spring AOP exclusively uses Runtime Weaving.** It achieves this by creating dynamic **Proxy** objects that wrap around your target beans. When a method is called on the proxy, the proxy executes the aspect logic before/after delegating the call to the actual target bean.