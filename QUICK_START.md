# Role Access Hierarchy - Quick Start Guide

## 🚀 Getting Started

### Prerequisites
- Application must be running with the new code deployed
- Database with migrations applied
- Test tool (Postman, curl, or REST client)

### Default Credentials
After application startup, use these credentials to access the system:

**Super Admin User:**
```
Email: irdbd08@gmail.com
Password: Ird@615ict
Role: Super Admin (Full system access)
```

---

## 📋 Quick Setup Checklist

### 1. ✅ Verify Application Started Successfully
```bash
curl http://localhost:8080/api/roles
# Should return list of roles including Super Admin, Admin, Project Director, etc.
```

### 2. ✅ Check Role Access Hierarchies Were Created
```bash
curl http://localhost:8080/api/role-access-hierarchy
# Should return 4 hierarchies (SUPER_ADMIN, FULL_MINISTRY_ACCESS, PROJECT_DIRECTOR, MONITORING_OFFICER)
```

### 3. ✅ Verify Admin User Exists
```bash
curl http://localhost:8080/api/users | grep irdbd08@gmail.com
# Should show the admin user with Super Admin role
```

---

## 🎯 Common Workflows

### Workflow 1: Set Up a Ministry Administrator

**Step 1: Create or Identify User**
```bash
# Find an existing user or create new one
POST /api/users
{
  "name": "Ministry Admin User",
  "designation": "Ministry Administrator",
  "officeName": "Ministry Office",
  "email": "ministry.admin@example.com",
  "number": "01700000000",
  "minDiv": "Ministry Division",
  "roleId": 2  # Admin role (FULL_MINISTRY_ACCESS)
}
# Response contains userId, e.g., userId = 5
```

**Step 2: Grant Access to Ministry**
```bash
POST /api/access-control/users/5/ministries/1?accessLevel=MINISTRY
# This grants the user access to Ministry 1 at MINISTRY level
# User can now access all projects within this ministry
```

**Step 3: Verify Access**
```bash
GET /api/access-control/users/5/ministries/1/can-access
# Response: true

GET /api/access-control/users/5/ministries/1/access-level
# Response: "MINISTRY"

GET /api/access-control/users/5/ministries
# Lists all ministries accessible to this user
```

---

### Workflow 2: Set Up a Project Director

**Step 1: Create or Identify User**
```bash
POST /api/users
{
  "name": "Project Director",
  "designation": "Project Director",
  "officeName": "Project Office",
  "email": "project.director@example.com",
  "number": "01700000001",
  "minDiv": "Ministry Division",
  "roleId": 3  # Project Director role
}
# Response contains userId, e.g., userId = 6
```

**Step 2: Grant Access to Specific Projects**
```bash
# Grant access to Project 10
POST /api/access-control/users/6/projects/10?accessLevel=PROJECT
# Repeat for other projects this director manages

# Grant access to Project 11
POST /api/access-control/users/6/projects/11?accessLevel=PROJECT
```

**Step 3: Verify Access**
```bash
GET /api/access-control/users/6/projects
# Lists all projects accessible to this user

GET /api/access-control/users/6/projects/10/access-level
# Response: "PROJECT"
```

---

### Workflow 3: Set Up a Data Entry Officer (Monitoring Officer)

**Step 1: Create User**
```bash
POST /api/users
{
  "name": "Data Entry Officer",
  "designation": "Data Entry Officer",
  "officeName": "Data Entry Center",
  "email": "data.entry@example.com",
  "number": "01700000002",
  "minDiv": "Ministry Division",
  "roleId": 6  # Entry User role (MONITORING_OFFICER)
}
# Response contains userId, e.g., userId = 7
```

**Step 2: Grant Limited Access**
```bash
# Grant VIEW_ONLY access to ministry (can see but not modify)
POST /api/access-control/users/7/ministries/2?accessLevel=VIEW_ONLY

# Grant CREATE_EDIT access to project (can create/edit but not delete)
POST /api/access-control/users/7/projects/15?accessLevel=CREATE_EDIT
```

**Step 3: Verify Limited Permissions**
```bash
GET /api/access-control/users/7/ministries/2/access-level
# Response: "VIEW_ONLY"

GET /api/access-control/users/7/projects/15/access-level
# Response: "CREATE_EDIT"
```

---

## 🔍 Access Control API Reference

### Check Access Availability

**Can user access ministry?**
```bash
GET /api/access-control/users/{userId}/ministries/{ministryId}/can-access
# Response: true/false
```

**Can user access project?**
```bash
GET /api/access-control/users/{userId}/projects/{projectId}/can-access
# Response: true/false
```

### Get Access Levels

**User's ministry access level**
```bash
GET /api/access-control/users/{userId}/ministries/{ministryId}/access-level
# Response: "FULL", "MINISTRY", "VIEW_ONLY", "NONE", etc.
```

**User's project access level**
```bash
GET /api/access-control/users/{userId}/projects/{projectId}/access-level
# Response: "FULL", "PROJECT", "CREATE_EDIT", "DATA_ENTRY", "VIEW_ONLY", "NONE"
```

### List User's Accessible Resources

**List all ministries accessible to user**
```bash
GET /api/access-control/users/{userId}/ministries
# Response: Array of UserMinistryAccessDto objects
```

**List all projects accessible to user**
```bash
GET /api/access-control/users/{userId}/projects
# Response: Array of UserProjectAccessDto objects
```

### Manage Access

**Grant/Update Ministry Access**
```bash
POST /api/access-control/users/{userId}/ministries/{ministryId}?accessLevel=FULL
POST /api/access-control/users/{userId}/ministries/{ministryId}?accessLevel=MINISTRY
POST /api/access-control/users/{userId}/ministries/{ministryId}?accessLevel=VIEW_ONLY
```

**Grant/Update Project Access**
```bash
POST /api/access-control/users/{userId}/projects/{projectId}?accessLevel=FULL
POST /api/access-control/users/{userId}/projects/{projectId}?accessLevel=PROJECT
POST /api/access-control/users/{userId}/projects/{projectId}?accessLevel=CREATE_EDIT
POST /api/access-control/users/{userId}/projects/{projectId}?accessLevel=DATA_ENTRY
```

**Revoke Access**
```bash
DELETE /api/access-control/users/{userId}/ministries/{ministryId}
DELETE /api/access-control/users/{userId}/projects/{projectId}
```

---

## 🛡️ Access Levels Explained

### Hierarchy (Highest to Lowest)
```
FULL > PROJECT > MINISTRY > CREATE_EDIT > DATA_ENTRY > VIEW_ONLY > NONE
```

### What Each Level Allows

| Level | Create | Read | Update | Delete | Use Case |
|-------|--------|------|--------|--------|----------|
| **FULL** | ✅ | ✅ | ✅ | ✅ | Super admins, unrestricted access |
| **PROJECT** | ✅ | ✅ | ✅ | ❌ | Project directors (no delete) |
| **MINISTRY** | ✅ | ✅ | ✅ | ❌ | Ministry heads (manage ministries) |
| **CREATE_EDIT** | ✅ | ✅ | ✅ | ❌ | Data entry officers (limited edit) |
| **DATA_ENTRY** | ✅ | ✅ | ❌ | ❌ | Data entry only (no update/delete) |
| **VIEW_ONLY** | ❌ | ✅ | ❌ | ❌ | Read-only access (monitoring) |
| **NONE** | ❌ | ❌ | ❌ | ❌ | No access |

---

## 📊 Example Scenarios

### Scenario 1: Super Admin (Full System Access)
- ✅ Access ALL ministries and projects without assignment
- ✅ Full CRUD permissions everywhere
- ✅ No need to assign ministry or project access

```bash
# Super Admin can access anything
GET /api/access-control/users/1/ministries/ANY_ID/can-access
# Always returns: true

GET /api/access-control/users/1/projects/ANY_ID/can-access
# Always returns: true
```

### Scenario 2: Ministry Administrator
- ✅ Access only assigned ministries
- ✅ Can manage all projects within assigned ministries
- ✅ Cannot access projects from other ministries

```bash
# Example: Ministry Admin with access to Ministry 5 only
GET /api/access-control/users/2/ministries/5/can-access
# Returns: true (assigned)

GET /api/access-control/users/2/ministries/10/can-access
# Returns: false (not assigned)

# Can access all projects in Ministry 5
GET /api/access-control/users/2/projects/20/can-access
# Returns: true (belongs to Ministry 5)

GET /api/access-control/users/2/projects/30/can-access
# Returns: false (belongs to Ministry 10)
```

### Scenario 3: Project Director
- ✅ Access only assigned projects
- ✅ Cannot access ministry-level features
- ✅ Limited to managing assigned projects

```bash
# Example: Project Director with access to Project 10 only
GET /api/access-control/users/3/projects/10/can-access
# Returns: true (assigned)

GET /api/access-control/users/3/projects/15/can-access
# Returns: false (not assigned)

# Ministry access
GET /api/access-control/users/3/ministries/5/can-access
# Returns: false (project director level doesn't grant ministry access)
```

### Scenario 4: Data Entry Officer (Monitoring Officer)
- ✅ View-only access to assigned ministries
- ✅ Create/edit access to assigned projects
- ✅ Cannot delete anything

```bash
# Example: Data Entry Officer
POST /api/access-control/users/4/ministries/3?accessLevel=VIEW_ONLY
POST /api/access-control/users/4/projects/25?accessLevel=CREATE_EDIT

# Ministry access is view-only
GET /api/access-control/users/4/ministries/3/access-level
# Returns: "VIEW_ONLY"

# Project access allows create/edit
GET /api/access-control/users/4/projects/25/access-level
# Returns: "CREATE_EDIT"
```

---

## 🔧 Role Hierarchy Configuration

### View Current Role Hierarchies
```bash
GET /api/role-access-hierarchy

# Response example:
[
  {
    "id": 1,
    "roleType": "SUPER_ADMIN",
    "ministryAccessLevel": "FULL",
    "projectAccessLevel": "FULL",
    "canAccessAllministries": true,
    "canAccessAllProjects": true,
    "description": "Super Admin - Full access to all..."
  },
  {
    "id": 2,
    "roleType": "FULL_MINISTRY_ACCESS",
    "ministryAccessLevel": "MINISTRY",
    "projectAccessLevel": "MINISTRY",
    "canAccessAllministries": false,
    "canAccessAllProjects": false,
    "description": "Full Ministry Access - Access to assigned..."
  }
  // ... more hierarchies
]
```

### Get Specific Role Hierarchy
```bash
GET /api/role-access-hierarchy/role-type/PROJECT_DIRECTOR

# Response:
{
  "id": 3,
  "roleType": "PROJECT_DIRECTOR",
  "ministryAccessLevel": "PROJECT",
  "projectAccessLevel": "PROJECT",
  "canAccessAllministries": false,
  "canAccessAllProjects": false,
  "description": "Project Director - Access to assigned projects only..."
}
```

### Update Role Hierarchy
```bash
PUT /api/role-access-hierarchy/3
{
  "ministryAccessLevel": "MINISTRY",
  "projectAccessLevel": "CREATE_EDIT",
  "description": "Updated Project Director with enhanced access"
}
```

---

## 🎓 Testing with Postman

### Import Collection Steps
1. Open Postman
2. Create new collection: "PMS Access Control"
3. Add these requests:

#### Request 1: Check Access
```
GET http://localhost:8080/api/access-control/users/1/projects/10/can-access
```

#### Request 2: Get Access Level
```
GET http://localhost:8080/api/access-control/users/1/projects/10/access-level
```

#### Request 3: Grant Ministry Access
```
POST http://localhost:8080/api/access-control/users/2/ministries/5?accessLevel=MINISTRY
```

#### Request 4: Grant Project Access
```
POST http://localhost:8080/api/access-control/users/3/projects/10?accessLevel=CREATE_EDIT
```

#### Request 5: List User Access
```
GET http://localhost:8080/api/access-control/users/2/ministries
GET http://localhost:8080/api/access-control/users/3/projects
```

---

## 🐛 Troubleshooting

### Issue: User gets 403 Forbidden when accessing data
**Solution:**
1. Check if user has access assigned: `GET /api/access-control/users/{userId}/projects/{projectId}/can-access`
2. If false, grant access: `POST /api/access-control/users/{userId}/projects/{projectId}?accessLevel=CREATE_EDIT`
3. Verify user's role has appropriate roleType

### Issue: Super Admin still can't access everything
**Solution:**
1. Check user's role: `GET /api/users/{userId}`
2. Verify role has roleType: `GET /api/roles/{roleId}`
3. If roleType is null, update it: `PUT /api/roles/{roleId}` with `"roleType": "SUPER_ADMIN"`

### Issue: Access level query returns "NONE"
**Solution:**
1. User doesn't have access to that resource
2. Grant access: `POST /api/access-control/users/{userId}/[ministries|projects]/{resourceId}?accessLevel=VIEW_ONLY`

### Issue: Can't update access level
**Solution:**
1. POST to the same endpoint to update existing access
2. System will update if record exists, create if not

---

## 📚 Related Documentation

- **ROLE_ACCESS_HIERARCHY.md** - Complete technical documentation
- **IMPLEMENTATION_SUMMARY.md** - Implementation details
- JavaDoc in source files
- API endpoint comments

---

## ✨ Summary

The Role Access Hierarchy system is now ready to use. Key points:

1. **4 Role Types**: Super Admin, Admin, Project Director, Monitoring Officer
2. **Flexible Access**: Assign ministry or project access dynamically
3. **Access Levels**: 7 levels from FULL to NONE
4. **REST API**: Full CRUD operations for access control
5. **Service Integration**: Use AccessControlService in your business logic

Start by creating users, assigning them roles, then use the access control APIs to grant appropriate permissions!

---

**Last Updated**: August 3, 2026
**Status**: Production Ready ✅

