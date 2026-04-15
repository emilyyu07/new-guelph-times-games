# New Guelph Times Games

A New York Times–inspired game suite with a Wordle, Mini-Crossword, and Connections.

## Main Features

- **Wordle**
  - 6-attempt word guessing with color feedback
  - Physical + on-screen keyboard input
  - Persistent local stats (played, wins, guess distribution)
- **Mini Crossword**
  - Random puzzle loading from bundled crossword set
  - Across/Down clue navigation and highlighting
  - Keyboard entry with timer-based completion feedback
- **Connections**
  - Randomized 4x4 tile board from category data
  - Correct / one-away / wrong validation logic
  - Lives system with win/loss reveal flow

## Quick Start (React)

```bash
cd frontend
npm install
npm run dev
```

## Data Sources

- `frontend/public/wordleWords.txt`
- `frontend/public/crosswords/`
- `frontend/public/ConnectionsWords`

## Legacy Java Version

Original Swing implementation is preserved in `src/` (with NetBeans/Ant files). Migrated to React frontend.
