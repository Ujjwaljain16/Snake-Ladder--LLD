# Snake and Ladder - Low Level Design

A complete implementation of the Snake and Ladder game demonstrating SOLID principles and design patterns through a turn-based board game with multiple players, random dice rolls, and configurable difficulty levels.

## Quick Start

```bash
# Compile all Java files
javac src/*.java

# Run the game
java -cp src Main

# Game prompts for:
#   1. Board size (n for n×n board)
#   2. Number of players
#   3. Difficulty level (easy or hard)
```

Sample input:
```
4
2
easy
```

This starts a game on a 4×4 board with 2 players on easy difficulty.

---

## Class Diagram

```
                        ┌──────────────┐
                        │     Main     │
                        └────────┬─────┘
                                 │
                    ┌────────────┼────────────┐
                    │            │            │
                    ▼            ▼            ▼
        ┌──────────────────┐ ┌──────────┐ ┌──────────────────┐
        │ ConsoleGame      │ │   Game   │ │ BoardFactory     │
        │ Presenter        │ │          │ │                  │
        │ implements       │ │ -board   │ │ +createBoard()   │
        │ GamePresenter    │ │ -players │ └──────────────────┘
        │                  │ │ -dice    │
        │ +displayGameStart│ │ -strategy│
        │ +displayBoardElm │ │ -present │
        │ +displayRoll()   │ └────┬─────┘
        │ +displayMove()   │      │
        │ +displaySnake()  │      │
        │ +displayGameOver │      │
        └──────────────────┘      │
                             ┌────┴────────┐
                             │             │
                    ┌────────┴───┐     ┌───┴─────────┐
                    ▼            ▼     ▼             ▼
              ┌──────────┐  ┌──────────────────┐  ┌─────────────┐
              │  Board   │  │MovementStrategy  │  │    Dice     │
              │          │  │   (interface)    │  │             │
              │-snakes   │  └────┬──────┬──────┘  │ -random     │
              │-ladders  │       │      │         └─────────────┘
              │-snakeMap │       │      │
              │-ladderMap│       ▼      ▼
              └────┬─────┘  ┌─────────┐ ┌──────────────┐
                   │        │  Easy   │ │    Hard      │
                   │        │Movement │ │  Movement    │
              ┌────┴──────┐ │Strategy │ │  Strategy    │
              │           │ └─────────┘ └──────────────┘
              ▼           ▼
          ┌────────┐  ┌────────┐        ┌───────────────────┐
          │ Snake  │  │ Ladder │        │ <<interface>>     │
          │        │  │        │        │ GamePresenter     │
          │-head   │  │-start  │        │                   │
          │-tail   │  │-end    │        │ +displayGameStart │
          └────────┘  └────────┘        │ +displayBoard     │
                                        │ +displayPlayerRoll│
                                        │ +displayPlayerMove│
                                        │ +displaySnake     │
                                        │ +displayLadder    │
                                        │ +displayGameOver  │
                                        └───────────────────┘
```

---

## Design Approach & Reasoning

### Why This Architecture?

**Problem**: Traditional implementations mix game logic with output, making testing and reuse difficult.

**Solution**: Separate concerns using SOLID principles and design patterns.

### SOLID Principles Applied

#### 1. Single Responsibility Principle (SRP)
Each class has one reason to change:
- `Game`: Only changes if game rules change
- `ConsoleGamePresenter`: Only changes if console output format changes
- `Board`: Only changes if board generation logic changes
- `MovementStrategy`: Only changes if movement rules change

**Benefit**: Classes are focused, testable, and maintainable.

#### 2. Open/Closed Principle (OCP)
Classes are open for extension, closed for modification:
- New difficulty levels: Create new `MovementStrategy` implementations without modifying `Game`
- New output formats: Implement `GamePresenter` without touching game logic
- No need to recompile existing code when adding features

**Benefit**: Future features don't break existing code.

#### 3. Liskov Substitution Principle (LSP)
Subclasses are substitutable for parent types:
- `EasyMovementStrategy` and `HardMovementStrategy` can be swapped without breaking `Game`
- Any `GamePresenter` implementation works identically from `Game`'s perspective
- All valid implementations produce correct results

**Benefit**: Polymorphism works reliably; code uses abstractions safely.

#### 4. Interface Segregation Principle (ISP)
Interfaces are focused on specific responsibilities:
- `GamePresenter` only defines display operations
- `MovementStrategy` only defines movement behavior
- No "fat" interfaces forcing unnecessary implementations

**Benefit**: Classes depend only on what they use.

#### 5. Dependency Inversion Principle (DIP)
High-level modules depend on abstractions, not concrete classes:
- `Game` depends on `GamePresenter` interface, not `ConsoleGamePresenter`
- `Game` depends on `MovementStrategy` interface, not concrete implementations
- `Board` is injected via `BoardFactory`, not hardcoded

**Benefit**: Dependencies are flexible and testable; easy to mock.

### Design Patterns Used

#### Strategy Pattern (MovementStrategy)
**Purpose**: Encapsulate different movement algorithms so they're interchangeable.

```
MovementStrategy (interface)
    ├── EasyMovementStrategy (single transitions)
    └── HardMovementStrategy (chained transitions)
```

- **Easy Mode**: Landing on a cell triggers at most one snake/ladder
- **Hard Mode**: Transitions chain (landing on snake at position X might land on ladder at tail)

**Why**: Allows changing behavior at runtime without modifying Game. Easy to add "nightmare" or "expert" modes.

#### Factory Pattern (BoardFactory)
**Purpose**: Centralize and simplify object creation.

```java
Board board = BoardFactory.createBoard(boardSize, difficulty);
```

- Handles initialization of Board with Random
- Encapsulates complexity of board setup
- Single point of change for board creation logic

**Why**: Decouples Game from Board construction details. Easy to change how boards are created.

#### Presenter Pattern (GamePresenter)
**Purpose**: Separate game logic from output concerns.

```
GamePresenter (interface)
    └── ConsoleGamePresenter (current impl)
        (Future: GuiPresenter, RestPresenter, FilePresenter)
```

- Game outputs through interface, not System.out
- ConsoleGamePresenter handles all display operations
- New output formats added by implementing GamePresenter

**Why**: Testable (mock presenter), extensible (new output formats), reusable (game logic independent of UI).

### Architecture Benefits

| Aspect | Benefit |
|--------|---------|
| **Maintainability** | Each class has clear purpose; easy to fix bugs |
| **Testability** | Game can be tested with mock presenter |
| **Extensibility** | New features without modifying existing code |
| **Reusability** | Game logic works with any presentation format |
| **Flexibility** | Features (difficulty, UI) can be changed at runtime |

---

## Core Classes

| Class | Purpose |
|-------|---------|
| `Main` | Entry point, reads user input, creates and starts game |
| `Game` | Turn-by-turn game logic, win/loss conditions |
| `Board` | Board generation, snake/ladder placement, O(1) lookups |
| `Player` | Player state (name, position, finish status) |
| `Snake` | Snake entity (head, tail) |
| `Ladder` | Ladder entity (start, end) |
| `Dice` | Random 1-6 rolls |
| `DifficultyLevel` | Enum (EASY, HARD) with factory method |
| `BoardFactory` | Creates boards with random snake/ladder placement |
| `GamePresenter` | Interface for all display operations |
| `ConsoleGamePresenter` | Console implementation of GamePresenter |
| `MovementStrategy` | Interface for movement rules |
| `EasyMovementStrategy` | Single snake/ladder jump per move |
| `HardMovementStrategy` | Chained jumps allowed |

---

## Game Rules

1. **Board**: n×n grid with cells numbered 1 to n²
2. **Snakes**: n randomly placed snakes, each head > tail (move down)
3. **Ladders**: n randomly placed ladders, each start < end (move up)
4. **No Overlaps**: Snake/ladder start positions don't overlap
5. **No Cycles**: Placements don't create infinite loops

### Turn Sequence

1. Players take turns in order
2. Each turn: Roll dice → Move → Apply snake/ladder
3. Move = current position + dice value
4. Skip if new position > n² (can't move beyond board)
5. Apply snake/ladder based on difficulty
6. If position = n², player finishes and is removed

### Winning

- **Win**: Reach position n² exactly
- **Finish Order**: Tracked as players win
- **Game Over**: Only 1 active player remains
- **Output**: Winners listed in order, remaining players shown

### Difficulty Modes

- **Easy**: Single transition (one snake OR one ladder per cell)
- **Hard**: Chained transitions (landing on snake might trigger ladder at tail, creating chain)
---
`LLM was used to polish the content the idea and approch was original `