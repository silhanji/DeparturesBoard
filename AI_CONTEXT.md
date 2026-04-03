# Project Context: Prague Public Transport Widget App

## Overview
Android application that enhances the official PID Lítačka app by providing a customizable home screen widget for public transport departures.

The app focuses on showing the most relevant departures based on:
- current time
- user location
- user-defined preferences (profiles)

## Core Concept
Users create "profiles" that define when and from where they typically travel.
The widget dynamically selects the most relevant profile and displays upcoming departures.

Example:
- "Work" profile → weekdays at 17:00 from office stop
- "Home" profile → mornings from home stop

---

## Key Features

### Profiles
Each profile contains:
- name (string)
- time validity (time range or specific time)
- selected stop (stop ID/code)
- selected lines:
    - line number
    - headsign (destination / last stop)

### Filtering
Departures can be filtered by:
- vehicle type (tram, bus, metro, etc.)
- wheelchair accessibility

### Widget
- Displays upcoming departures for the active profile
- Automatically selects profile based on:
    - current time
    - optionally user location (future improvement)
- Must be lightweight and update efficiently

---

## Data Sources

### Static Data
- `res/raw/stops.txt`
    - contains:
        - stop name
        - stop code (ID used in API)

### External API
- Golemio (PID Lítačka transit API)
- Used for:
    - real-time departures
    - line information

---

## Tech Stack

- Kotlin
- Android Studio
- Android Widgets (AppWidgetProvider / Glance if used)
- REST API integration (likely Retrofit)

---

## Architecture Expectations

The code should follow:
- clean architecture principles
- separation of concerns:
    - UI (widget)
    - domain logic (profile selection, filtering)
    - data layer (API + local data)

Prefer:
- small, reusable components
- testable logic (especially profile selection)
- clear data models

---

## Important Constraints

- Widget must be performant (low battery + minimal updates)
- Avoid unnecessary API calls
- Handle missing or delayed API data gracefully
- Keep logic deterministic (no randomness)

---

## What the Agent Should Optimize For

When generating code or suggestions:
- prioritize simplicity and readability
- avoid over-engineering
- prefer Kotlin idioms
- ensure Android best practices
- minimize widget update cost

---

## Typical Tasks for the Agent

- Implement profile selection logic
- Design data models for stops, lines, departures
- Create filtering logic
- Integrate Golemio API
- Build/update widget UI
- Optimize background updates

---

## Non-Goals

- Do NOT redesign the whole app unless asked
- Do NOT introduce heavy frameworks unless necessary
- Do NOT assume backend control (API is external)
