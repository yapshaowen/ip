# Tyrone User Guide

Welcome to **Tyrone**, a task management chatbot with a graphical user
interface (GUI). Tyrone helps you manage todos, deadlines, and events
efficiently using simple commands.

------------------------------------------------------------------------

## 🚀 Getting Started

1.  Download the `tyrone.jar` file.
2.  Run the application using:

```{=html}
<!-- -->
```
    java -jar tyrone.jar

3.  Enter commands in the text field and press Enter.

------------------------------------------------------------------------

## 📌 Features

### 1️⃣ Add Tasks

#### Todo

Adds a simple task without a date.

    todo <description>

Example:

    todo read book

------------------------------------------------------------------------

#### Deadline

Adds a task with a deadline.

    deadline <description> /by <date>

Example:

    deadline submit report /by 2026-02-20

------------------------------------------------------------------------

#### Event

Adds an event with a start and end time.

    event <description> /from <start> /to <end>

Example:

    event meeting /from 2pm /to 4pm

------------------------------------------------------------------------

### 2️⃣ View Tasks

#### List all tasks

    list

------------------------------------------------------------------------

### 3️⃣ Manage Tasks

#### Mark a task as done

    mark <index>

#### Unmark a task

    unmark <index>

#### Delete a task

    delete <index>

#### Find tasks by keyword

    find <keyword>

------------------------------------------------------------------------

## ❗ Notes

-   Task indices start from 1.
-   Duplicate tasks are not allowed.
-   Commands are case-sensitive.
-   Ensure proper formatting when using `/by`, `/from`, and `/to`.

------------------------------------------------------------------------

## 🏷 Product Information

**Product Name:** Tyrone\
**Platform:** Java (JavaFX GUI)\
**File:** tyrone.jar

------------------------------------------------------------------------

Thank you for using Tyrone!
