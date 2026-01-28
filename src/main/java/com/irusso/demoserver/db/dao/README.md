# Data Access Objects (DAOs)

This package contains Data Access Objects for database operations using JDBI3.

## Overview

The DAO pattern provides an abstraction layer between the application and the database. All DAOs extend the `StandardDao` abstract class which provides common CRUD operations.

The framework uses `TableDefinition` and `ColumnDefinition` to automatically generate INSERT and UPDATE statements, eliminating boilerplate code.

## StandardDao

The `StandardDao<T, ID>` abstract class provides generic database operations:

### Standard CRUD Operations

- `Optional<T> findById(ID id)` - Find entity by primary key
- `List<T> findAll()` - Get all entities
- `List<T> findAll(int limit, int offset)` - Get entities with pagination
- `long count()` - Count total entities
- `ID insert(T entity)` - Insert new entity (automatically generated from TableDefinition)
- `boolean update(ID id, T entity)` - Update entity (automatically generated from TableDefinition)
- `boolean delete(ID id)` - Delete entity by ID
- `boolean exists(ID id)` - Check if entity exists
- `int deleteAll()` - Delete all entities (use with caution!)

### Protected Helper Methods

- `List<T> executeQuery(String sql)` - Execute custom query
- `List<T> executeQuery(String sql, Object... params)` - Execute query with parameters
- `int executeUpdate(String sql, Object... params)` - Execute update/insert/delete
- `TableDefinition<T> getTableDefinition()` - Get the table definition
- `String getTableName()` - Get the table name
- `String getIdColumn()` - Get the ID column name
- `Jdbi getJdbi()` - Get the JDBI instance

## TableDefinition and ColumnDefinition

The `TableDefinition` class describes the structure of a database table, including:
- Table name
- ID column name
- List of `ColumnDefinition` objects

Each `ColumnDefinition` describes a column with:
- Column name (database)
- Java property name (for parameter binding)
- Java type
- Nullable flag
- Insertable flag (include in INSERT statements)
- Updatable flag (include in UPDATE statements)
- Getter function (extract value from entity)
- Setter function (set value on entity)

This metadata enables StandardDao to automatically generate INSERT and UPDATE SQL statements.

## Creating a New DAO

To create a DAO for a new table:

### 1. Create the Entity Model

Create a model class in `db/model/` representing the database table:

```java
package com.irusso.demoserver.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

public class MyEntity {
    private Long id;
    private String name;
    private Instant createdAt;
    private Instant updatedAt;

    // Getters and setters with @JsonProperty annotations
    @JsonProperty
    public Long getId() { return id; }

    @JsonProperty
    public void setId(Long id) { this.id = id; }

    @JsonProperty
    public String getName() { return name; }

    @JsonProperty
    public void setName(String name) { this.name = name; }

    // ... more getters/setters
}
```

### 2. Create the DAO Class

Create a DAO class extending `StandardDao` with a `TableDefinition`:

```java
package com.irusso.demoserver.db.dao;

import com.irusso.demoserver.db.model.MyEntity;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;
import java.sql.Timestamp;
import java.time.Instant;

public class MyEntityDao extends StandardDao<MyEntity, Long> {

    private static final String TABLE_NAME = "my_entities";
    private static final String ID_COLUMN = "id";

    // Create the table definition
    private static TableDefinition<MyEntity> createTableDefinition() {
        return TableDefinition.<MyEntity>builder()
            .tableName(TABLE_NAME)
            .idColumn(ID_COLUMN)
            .addColumn(ColumnDefinition.<MyEntity>builder()
                .columnName("name")
                .javaType(String.class)
                .nullable(false)
                .getter(MyEntity::getName)
                .setter((e, v) -> e.setName((String) v))
                .build())
            .addColumn(ColumnDefinition.<MyEntity>builder()
                .columnName("created_at")
                .javaType(Timestamp.class)
                .nullable(false)
                .insertable(true)
                .updatable(false)  // Don't update created_at
                .getter(e -> e.getCreatedAt() != null ?
                    Timestamp.from(e.getCreatedAt()) :
                    Timestamp.from(Instant.now()))
                .setter((e, v) -> e.setCreatedAt(
                    v != null ? ((Timestamp) v).toInstant() : null))
                .build())
            .addColumn(ColumnDefinition.<MyEntity>builder()
                .columnName("updated_at")
                .javaType(Timestamp.class)
                .nullable(false)
                .insertable(true)
                .updatable(true)
                .getter(e -> Timestamp.from(Instant.now()))  // Always use current time
                .setter((e, v) -> e.setUpdatedAt(
                    v != null ? ((Timestamp) v).toInstant() : null))
                .build())
            .build();
    }

    // Define the RowMapper
    private static final RowMapper<MyEntity> MAPPER = (rs, ctx) -> {
        MyEntity entity = new MyEntity();
        entity.setId(rs.getLong("id"));
        entity.setName(rs.getString("name"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            entity.setCreatedAt(createdAt.toInstant());
        }

        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            entity.setUpdatedAt(updatedAt.toInstant());
        }

        return entity;
    };

    public MyEntityDao(Jdbi jdbi) {
        super(jdbi, createTableDefinition(), MAPPER);
    }

    // No need to implement insert() or update() - they're automatic!

    // Add custom query methods
    public List<MyEntity> findByName(String name) {
        String sql = "SELECT * FROM my_entities WHERE name = :name";
        return executeQuery(sql, "name", name);
    }
}
```

### 3. Register the DAO

In `GainfullyServerApplication.java`, create and register the DAO:

```java
@Override
public void run(GainfullyServerConfiguration configuration, Environment environment) {
    // ... existing code ...

    // Create DAOs
    final MyEntityDao myEntityDao = new MyEntityDao(jdbi);

    // Pass to resources that need it
    final MyEntityResource myEntityResource = new MyEntityResource(myEntityDao);
    environment.jersey().register(myEntityResource);
}
```

## Benefits of TableDefinition Approach

### ✅ Eliminates Boilerplate
- No need to write INSERT and UPDATE SQL manually
- No need to manually bind parameters
- Automatically handles all columns

### ✅ Type Safety
- Compile-time checking of getters/setters
- Type-safe column definitions
- Prevents typos in column names

### ✅ Maintainability
- Add a column? Just add one ColumnDefinition
- Change a column? Update in one place
- Clear, declarative structure

### ✅ Flexibility
- Control which columns are insertable/updatable
- Handle timestamps automatically
- Custom getter/setter logic per column

### ✅ Consistency
- All DAOs follow the same pattern
- Standardized INSERT/UPDATE behavior
- Predictable code structure

## Example: EmployeeDao

See `EmployeeDao.java` for a complete example implementation with:

- Standard CRUD operations
- Custom query methods (`findByEmail`, `findActivelySeeking`, `findByLocation`)
- Custom update methods (`updateRating`, `updateCommunicationRating`)

## Usage in Resources

Use DAOs in your REST resources:

```java
@Path("/api/employees")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmployeeResource {
    
    private final EmployeeDao employeeDao;
    
    public EmployeeResource(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }
    
    @GET
    public Response getAllEmployees() {
        List<Employee> employees = employeeDao.findAll();
        return Response.ok(employees).build();
    }
    
    @GET
    @Path("/{id}")
    public Response getEmployee(@PathParam("id") Long id) {
        Optional<Employee> employee = employeeDao.findById(id);
        if (employee.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(employee.get()).build();
    }
    
    @POST
    public Response createEmployee(@Valid Employee employee) {
        Long id = employeeDao.insert(employee);
        employee.setId(id);
        return Response.status(Response.Status.CREATED)
            .entity(employee)
            .build();
    }
    
    @PUT
    @Path("/{id}")
    public Response updateEmployee(@PathParam("id") Long id, @Valid Employee employee) {
        if (!employeeDao.exists(id)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        employeeDao.update(id, employee);
        return Response.ok(employee).build();
    }
    
    @DELETE
    @Path("/{id}")
    public Response deleteEmployee(@PathParam("id") Long id) {
        boolean deleted = employeeDao.delete(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}
```

## Best Practices

### 1. Use Text Blocks for SQL

Java 15+ text blocks make SQL more readable:

```java
String sql = """
    SELECT * FROM employees 
    WHERE location = :location 
    AND actively_seeking = true
    ORDER BY updated_at DESC
    """;
```

### 2. Handle Timestamps Properly

Convert between `java.time.Instant` and `java.sql.Timestamp`:

```java
// Reading from database
Timestamp ts = rs.getTimestamp("created_at");
if (ts != null) {
    entity.setCreatedAt(ts.toInstant());
}

// Writing to database
.bind("createdAt", Timestamp.from(entity.getCreatedAt()))
```

### 3. Use RETURNING for Inserts

PostgreSQL's `RETURNING` clause gets the generated ID:

```java
String sql = """
    INSERT INTO employees (name, email) 
    VALUES (:name, :email) 
    RETURNING id
    """;

return jdbi.withHandle(handle ->
    handle.createQuery(sql)
        .bind("name", name)
        .bind("email", email)
        .mapTo(Long.class)
        .one()
);
```

### 4. Add Custom Query Methods

Extend StandardDao with domain-specific queries:

```java
public List<Employee> findActivelySeeking() {
    String sql = "SELECT * FROM employees WHERE actively_seeking = true";
    return executeQuery(sql);
}

public List<Employee> findByLocation(String location) {
    String sql = "SELECT * FROM employees WHERE location ILIKE :location";
    return executeQuery(sql, "location", "%" + location + "%");
}
```

### 5. Use Transactions When Needed

For operations that need to be atomic:

```java
public void transferData(Long fromId, Long toId) {
    jdbi.useTransaction(handle -> {
        // Multiple operations in one transaction
        handle.createUpdate("UPDATE table1 SET ...").execute();
        handle.createUpdate("UPDATE table2 SET ...").execute();
    });
}
```

## Testing DAOs

Example unit test for a DAO:

```java
@Test
public void testInsertAndFindById() {
    Employee employee = new Employee();
    employee.setName("John Doe");
    employee.setEmail("john@example.com");
    employee.setActivelySeeking(true);
    
    Long id = employeeDao.insert(employee);
    assertNotNull(id);
    
    Optional<Employee> found = employeeDao.findById(id);
    assertTrue(found.isPresent());
    assertEquals("John Doe", found.get().getName());
    assertEquals("john@example.com", found.get().getEmail());
}
```

## Common Patterns

### Pagination

```java
// Get page 2 with 20 items per page
int page = 2;
int pageSize = 20;
int offset = (page - 1) * pageSize;

List<Employee> employees = employeeDao.findAll(pageSize, offset);
long total = employeeDao.count();
```

### Search with Multiple Criteria

```java
public List<Employee> search(String location, Boolean activelySeeking, Double minRating) {
    StringBuilder sql = new StringBuilder("SELECT * FROM employees WHERE 1=1");
    
    if (location != null) {
        sql.append(" AND location ILIKE :location");
    }
    if (activelySeeking != null) {
        sql.append(" AND actively_seeking = :activelySeeking");
    }
    if (minRating != null) {
        sql.append(" AND employee_rating >= :minRating");
    }
    
    return jdbi.withHandle(handle -> {
        var query = handle.createQuery(sql.toString()).map(EMPLOYEE_MAPPER);
        
        if (location != null) query.bind("location", "%" + location + "%");
        if (activelySeeking != null) query.bind("activelySeeking", activelySeeking);
        if (minRating != null) query.bind("minRating", minRating);
        
        return query.list();
    });
}
```

## See Also

- [JDBI3 Documentation](https://jdbi.org/)
- [DATABASE_SCHEMA.md](../../../../documentation/DATABASE_SCHEMA.md) - Database schema reference
- [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)

