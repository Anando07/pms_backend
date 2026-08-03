package net.java.pms_backend.entity;

public enum AccessLevel {
    FULL,           // Full Access (Create, Read, Update, Delete)
    MINISTRY,       // Ministry Level Access
    PROJECT,        // Project Level Access
    VIEW_ONLY,      // Read Only
    CREATE_EDIT,    // Create and Edit Only
    DATA_ENTRY,     // Limited Data Entry
    NONE            // No Access
}

