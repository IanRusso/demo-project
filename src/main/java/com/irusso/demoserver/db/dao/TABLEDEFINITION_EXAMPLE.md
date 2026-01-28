# TableDefinition Example

This document shows how `TableDefinition` and `ColumnDefinition` eliminate boilerplate code.

## Before: Manual INSERT/UPDATE

Without TableDefinition, you need to write INSERT and UPDATE methods manually:

```java
public class UserDao extends StandardDao<User, Long> {
    
    @Override
    public Long insert(User user) {
        String sql = """
            INSERT INTO users (
                name, email, phone_number, location, education_level,
                summary, profile_picture_url, employment_status,
                background_check_status, user_rating, communication_rating,
                salary_expectations_min, salary_expectations_max, actively_seeking,
                created_at, updated_at
            ) VALUES (
                :name, :email, :phoneNumber, :location, :educationLevel,
                :summary, :profilePictureUrl, :employmentStatus,
                :backgroundCheckStatus, :userRating, :communicationRating,
                :salaryExpectationsMin, :salaryExpectationsMax, :activelySeeking,
                :createdAt, :updatedAt
            ) RETURNING id
            """;

        return jdbi.withHandle(handle ->
            handle.createQuery(sql)
                .bind("name", user.getName())
                .bind("email", user.getEmail())
                .bind("phoneNumber", user.getPhoneNumber())
                .bind("location", user.getLocation())
                .bind("educationLevel", user.getEducationLevel())
                .bind("summary", user.getSummary())
                .bind("profilePictureUrl", user.getProfilePictureUrl())
                .bind("employmentStatus", user.getEmploymentStatus())
                .bind("backgroundCheckStatus", user.getBackgroundCheckStatus())
                .bind("userRating", user.getUserRating())
                .bind("communicationRating", user.getCommunicationRating())
                .bind("salaryExpectationsMin", user.getSalaryExpectationsMin())
                .bind("salaryExpectationsMax", user.getSalaryExpectationsMax())
                .bind("activelySeeking", user.getActivelySeeking())
                .bind("createdAt", Timestamp.from(user.getCreatedAt() != null ? 
                    user.getCreatedAt() : Instant.now()))
                .bind("updatedAt", Timestamp.from(Instant.now()))
                .mapTo(Long.class)
                .one()
        );
    }

    @Override
    public boolean update(Long id, User user) {
        String sql = """
            UPDATE users SET
                name = :name,
                email = :email,
                phone_number = :phoneNumber,
                location = :location,
                education_level = :educationLevel,
                summary = :summary,
                profile_picture_url = :profilePictureUrl,
                employment_status = :employmentStatus,
                background_check_status = :backgroundCheckStatus,
                user_rating = :userRating,
                communication_rating = :communicationRating,
                salary_expectations_min = :salaryExpectationsMin,
                salary_expectations_max = :salaryExpectationsMax,
                actively_seeking = :activelySeeking,
                updated_at = :updatedAt
            WHERE id = :id
            """;

        return jdbi.withHandle(handle -> {
            int rowsAffected = handle.createUpdate(sql)
                .bind("id", id)
                .bind("name", user.getName())
                .bind("email", user.getEmail())
                .bind("phoneNumber", user.getPhoneNumber())
                .bind("location", user.getLocation())
                .bind("educationLevel", user.getEducationLevel())
                .bind("summary", user.getSummary())
                .bind("profilePictureUrl", user.getProfilePictureUrl())
                .bind("employmentStatus", user.getEmploymentStatus())
                .bind("backgroundCheckStatus", user.getBackgroundCheckStatus())
                .bind("userRating", user.getUserRating())
                .bind("communicationRating", user.getCommunicationRating())
                .bind("salaryExpectationsMin", user.getSalaryExpectationsMin())
                .bind("salaryExpectationsMax", user.getSalaryExpectationsMax())
                .bind("activelySeeking", user.getActivelySeeking())
                .bind("updatedAt", Timestamp.from(Instant.now()))
                .execute();
            return rowsAffected > 0;
        });
    }
}
```

**Problems:**
- 90+ lines of repetitive code
- Easy to make typos in column names or parameter names
- Hard to maintain when adding/removing columns
- Must manually handle timestamps
- Duplicate logic between insert and update

## After: TableDefinition

With TableDefinition, you define the table structure once:

```java
public class UserDao extends StandardDao<User, Long> {
    
    private static TableDefinition<User> createTableDefinition() {
        return TableDefinition.<User>builder()
            .tableName("users")
            .idColumn("id")
            .addColumn(ColumnDefinition.<User>builder()
                .columnName("name")
                .javaType(String.class)
                .nullable(false)
                .getter(User::getName)
                .setter((e, v) -> e.setName((String) v))
                .build())
            .addColumn(ColumnDefinition.<User>builder()
                .columnName("email")
                .javaType(String.class)
                .nullable(false)
                .getter(User::getEmail)
                .setter((e, v) -> e.setEmail((String) v))
                .build())
            // ... more columns ...
            .addColumn(ColumnDefinition.<User>builder()
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
            .addColumn(ColumnDefinition.<User>builder()
                .columnName("updated_at")
                .javaType(Timestamp.class)
                .nullable(false)
                .insertable(true)
                .updatable(true)
                .getter(e -> Timestamp.from(Instant.now()))  // Always current time
                .setter((e, v) -> e.setUpdatedAt(
                    v != null ? ((Timestamp) v).toInstant() : null))
                .build())
            .build();
    }
    
    public UserDao(Jdbi jdbi) {
        super(jdbi, createTableDefinition(), USER_MAPPER);
    }
    
    // That's it! insert() and update() are automatic!
}
```

**Benefits:**
- No manual INSERT/UPDATE methods needed
- Declarative, self-documenting structure
- Type-safe with compile-time checking
- Easy to add/remove columns
- Automatic timestamp handling
- Control insertable/updatable per column

## How It Works

When you call `insert(user)`, StandardDao:

1. Gets insertable columns from TableDefinition
2. Generates SQL: `INSERT INTO users (name, email, ...) VALUES (:name, :email, ...) RETURNING id`
3. Binds values using each column's getter
4. Executes and returns the generated ID

When you call `update(id, user)`, StandardDao:

1. Gets updatable columns from TableDefinition
2. Generates SQL: `UPDATE users SET name = :name, email = :email, ... WHERE id = :id`
3. Binds values using each column's getter
4. Executes and returns success/failure

## Adding a New Column

### Before (Manual):
1. Add field to entity
2. Add getter/setter to entity
3. Update INSERT SQL
4. Add bind() call in insert()
5. Update UPDATE SQL
6. Add bind() call in update()

**6 places to change!**

### After (TableDefinition):
1. Add field to entity
2. Add getter/setter to entity
3. Add ColumnDefinition

**3 places to change!**

And the ColumnDefinition is type-safe and self-documenting.

## Column Control

You can control how each column behaves:

```java
.addColumn(ColumnDefinition.<User>builder()
    .columnName("id")
    .javaType(Long.class)
    .insertable(false)  // Don't include in INSERT (auto-generated)
    .updatable(false)   // Don't include in UPDATE (never change ID)
    .getter(User::getId)
    .setter((e, v) -> e.setId((Long) v))
    .build())

.addColumn(ColumnDefinition.<User>builder()
    .columnName("created_at")
    .javaType(Timestamp.class)
    .insertable(true)   // Include in INSERT
    .updatable(false)   // Don't include in UPDATE (set once)
    .getter(e -> Timestamp.from(Instant.now()))
    .setter((e, v) -> e.setCreatedAt(((Timestamp) v).toInstant()))
    .build())

.addColumn(ColumnDefinition.<User>builder()
    .columnName("updated_at")
    .javaType(Timestamp.class)
    .insertable(true)   // Include in INSERT
    .updatable(true)    // Include in UPDATE (always update)
    .getter(e -> Timestamp.from(Instant.now()))
    .setter((e, v) -> e.setUpdatedAt(((Timestamp) v).toInstant()))
    .build())
```

## Real-World Example

See `UserDao.java` for a complete real-world example with 16 columns including:
- String fields (name, email, location)
- BigDecimal fields (ratings, salary expectations)
- Boolean fields (actively_seeking)
- Timestamp fields (created_at, updated_at)

The UserDao uses TableDefinition and requires **zero** manual INSERT/UPDATE code!

