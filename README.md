# Schedule Management Library

## Overview

This project is a library designed to manage schedules with two different implementations. The library includes a specification (API) defining all operations on schedules, such as adding, removing, and searching for schedule entries. The specification has two implementations, each with a different internal representation of schedule data. The project leaves room for creative solutions and flexible implementation of ideas.

## Features

### Schedule API
- **Two-Dimensional Schedule:** Handles time and space dimensions. Each entry can have associated data (e.g., course name, instructor, type of class, groups attending).
- **Extendable Data:** Allows custom data association with each schedule entry. Space can have attributes like capacity, equipment (computers, projectors), and additional data.

### Schedule Creation
- **Initialization:** Initialize a new schedule.
- **Add Rooms:** Add rooms with attributes (capacity, computers, projectors).
- **Add Entry:** Add new entries with checks for availability.
- **Delete Entry:** Remove an occupied entry.
- **Move Entry:** Delete and add an entry with the same associated data at a different time.

### Schedule Viewing (Search)
- **Check Availability:** Verify if a time slot and space are available.
- **List Free Slots:** List available slots based on various criteria (exact date, day of the week, period, start and end time, or start time and duration).
- **Filter by Room:** List free rooms with specific attributes (e.g., computer-equipped, projector, capacity).
- **Filter by Associated Data:** List entries based on associated data (e.g., all entries for a specific instructor).

### File Operations
- **Load Schedule from File:** Implement operations to load schedule data from different file formats (e.g., JSON, CSV).
  - **Model Configuration:** The model must support arbitrary data with required time and place.
  - **Date Range:** Include start and end dates, and exclude specific days.
- **Save Schedule to File:** Save schedule data to files in CSV, JSON, and PDF formats with different configurations (e.g., for a specific period, grouped by days, or for a specific associated data).

### First Implementation - Collection of Entries
- **Entry Collection:** Store the schedule as a collection of specific time-space entries (e.g., Wednesday, 18.10.2023, 10-12h, Room S1).

### Second Implementation - Weekly Schedule
- **Weekly Grouping:** Store the schedule on a weekly basis for a given period, treating entries as recurring events on specific days of the week.
  - **Exclusion Days:** Define exceptions when the weekly schedule does not apply.

### Command-Line Program
- **Program Functionality:** Implement a command-line program using the library to manage the RAF schedule.
  - **Load Schedule:** Load schedule data from CSV (RAF schedule) and room data from a TXT file.
  - **Specify Period:** Define the schedule period and excluded days via console input or file.
  - **Schedule Operations:** Support viewing, searching, booking, moving entries, and saving the schedule to a file (for groups, rooms, instructors, or the entire schedule).
