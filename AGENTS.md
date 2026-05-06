# AGENTS.md

# Prague Public Transport Widget App

## Purpose
Android app that complements PID Lítačka by providing a customizable home screen widget for public transport departures.

Primary goal:
Show the most relevant upcoming departures with minimal battery impact and minimal unnecessary API calls.

---

## Product context

Users define travel profiles representing common travel situations.

Examples:
- Work: weekdays around 17:00 from office stop
- Home: weekdays around 07:00 from home stop

The widget selects the best matching profile for the current moment and displays filtered departures.

---

## Technical stack

- Kotlin
- Android Studio
- Android Widgets
- Retrofit for HTTP
- Kotlin coroutines + Flow

Unless explicitly requested otherwise:
- prefer View-based widget APIs or Glance only if it clearly reduces complexity
- prefer Jetpack libraries already common in Android projects
- avoid adding heavy frameworks

---

## Architecture rules

Follow clean architecture with strict separation:

- data layer
  - remote API models
  - local static data readers
  - repositories
- domain layer
  - pure business logic
  - profile selection
  - departure filtering
  - use cases
- presentation layer
  - widget UI state mapping
  - widget rendering
  - background update orchestration

Rules:
- keep business logic out of widget rendering code
- domain logic must be deterministic and unit testable
- do not call network code directly from widget UI classes
- map API DTOs to domain models explicitly
- prefer small classes with single responsibility
- prefer constructor injection
- avoid god objects

---

## Coding rules

- Act as a senior Kotlin Android engineer
- Prefer readability over cleverness
- Use idiomatic Kotlin
- Use immutable data classes whenever possible
- Prefer `val` over `var`
- Keep functions short
- Make nullability explicit and safe
- Avoid hidden side effects
- Use sealed classes when modeling state/result variants
- Use extension functions only when they improve clarity
- Do not introduce abstractions unless they solve a real problem now

When suggesting code:
- explain the reasoning
- point out tradeoffs
- provide a simpler alternative when relevant
- be direct when the current design is wrong

---

## Testing expectations

Prioritize tests for:
- profile selection logic
- departure filtering logic
- time-range matching
- widget state mapping

Prefer:
- pure unit tests for domain logic
- fake repositories over over-mocked tests
- deterministic test inputs

---

## Widget constraints

The widget must be lightweight.

Optimize for:
- low battery usage
- minimal background work
- minimal network calls
- graceful handling of stale or missing data

Rules:
- avoid frequent refreshes unless justified
- cache data when reasonable
- degrade gracefully when API data is unavailable
- never block the main thread
- avoid unnecessary recomposition / redraw / widget updates

---

## Domain model expectations

Profiles include:
- name
- time validity
- selected stop ID/code
- selected lines
- optional filters

Departure filters may include:
- vehicle type
- wheelchair accessibility

Keep domain models clear and decoupled from API response shape.

---

## Data sources

### External API
Golemio / PID Lítačka transit API

Used for:
- real-time departures
- line information

Assume API responses may be delayed, incomplete, or temporarily unavailable.

---

## Non-goals

- do not redesign the whole app unless explicitly asked
- do not add heavy frameworks without strong justification
- do not assume backend control
- do not over-engineer for hypothetical future requirements

---

## Preferred implementation defaults

Unless told otherwise:
- use coroutines for async work
- expose UI/domain streams with Flow where useful
- keep repositories as interfaces only when testability or multiple implementations justify it
- isolate time access behind a clock abstraction for testable selection logic

---

## How to respond

For code review or design feedback:
- identify what is wrong clearly
- explain why
- propose the best option first
- include one reasonable alternative when tradeoffs exist
- keep examples production-oriented, not toy-level