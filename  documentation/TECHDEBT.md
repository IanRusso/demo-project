Why do only some of the db/model classes have @JsonProperty annotations on the fields while others have it on the methods?
Do all of them even have those annotations?
If unnecessary, remove them all. Otherwise, add them to the rest of the classes and standardize the way they're used.

Update tests to cover everything that isn't already covered.

Generalize the ImportResult class to a single reusable class

Generalize the ImportRequest class to a single reusable class

These createTableDefinition() methods are massive and repetitive. Is there a way to clean them up?

The Row Mappers repeat a few logical patterns such as this one
Timestamp createdAt = rs.getTimestamp(COL_CREATED_AT);
if (createdAt != null) {
profession.setCreatedAt(createdAt.toInstant());
}
Can we create some common reusable methods

Is it possible to make a generic batch insert method in the StandardDao?