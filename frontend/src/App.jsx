import { useCallback, useEffect, useMemo, useState } from "react";
import "./App.css";

const WORDLE_ROWS = 6;
const WORDLE_COLS = 5;
const WORDLE_KEYBOARD_ROWS = ["QWERTYUIOP", "ASDFGHJKL", "ZXCVBNM"];
const STATUS_PRIORITY = { empty: 0, absent: 1, present: 2, correct: 3 };
const CONNECTION_COLORS = ["#ffdc6c", "#a8c45c", "#b8c4ec", "#c084c4"];
const CROSSWORD_FILES = [
  "Crossword.txt",
  "Crossword_1.txt",
  "Crossword_2.txt",
  "Crossword_3.txt",
  "Crossword_4.txt",
  "Crossword_5.txt",
  "Crossword_6.txt",
  "Crossword_7.txt",
  "Crossword_8.txt",
  "Crossword_9.txt",
];

function shuffle(items) {
  const copy = [...items];
  for (let i = copy.length - 1; i > 0; i -= 1) {
    const j = Math.floor(Math.random() * (i + 1));
    [copy[i], copy[j]] = [copy[j], copy[i]];
  }
  return copy;
}

function pickRandomDistinct(max, count) {
  return shuffle(Array.from({ length: max }, (_, index) => index)).slice(
    0,
    count,
  );
}

function evaluateGuess(guess, solution) {
  const statuses = Array(WORDLE_COLS).fill("absent");
  const remaining = {};

  for (let i = 0; i < WORDLE_COLS; i += 1) {
    if (guess[i] === solution[i]) {
      statuses[i] = "correct";
    } else {
      remaining[solution[i]] = (remaining[solution[i]] ?? 0) + 1;
    }
  }

  for (let i = 0; i < WORDLE_COLS; i += 1) {
    if (statuses[i] === "correct") continue;
    const letter = guess[i];
    if ((remaining[letter] ?? 0) > 0) {
      statuses[i] = "present";
      remaining[letter] -= 1;
    }
  }

  return statuses;
}

function loadWordleStats() {
  try {
    const raw = localStorage.getItem("ngt-wordle-stats");
    if (!raw) return { gamesPlayed: 0, gamesWon: 0, guessDistribution: [] };
    const parsed = JSON.parse(raw);
    return {
      gamesPlayed: Number(parsed.gamesPlayed ?? 0),
      gamesWon: Number(parsed.gamesWon ?? 0),
      guessDistribution: Array.isArray(parsed.guessDistribution)
        ? parsed.guessDistribution
        : [],
    };
  } catch {
    return { gamesPlayed: 0, gamesWon: 0, guessDistribution: [] };
  }
}

function saveWordleStats(stats) {
  localStorage.setItem("ngt-wordle-stats", JSON.stringify(stats));
}

function parseClueLine(line) {
  const match = line.trim().match(/^(\d+)\s*(.*)$/);
  if (!match) return { num: 0, text: line.trim() };
  return { num: Number(match[1]), text: match[2].trim() };
}

function parseCrossword(text) {
  const lines = text.split(/\r?\n/).filter((line) => line.trim().length > 0);
  let index = 0;
  const size = Number(lines[index++]);
  const verticalCount = Number(lines[index++]);
  const horizontalCount = Number(lines[index++]);

  const downClues = [];
  for (let i = 0; i < verticalCount; i += 1) {
    downClues.push(parseClueLine(lines[index++]));
  }

  const acrossClues = [];
  for (let i = 0; i < horizontalCount; i += 1) {
    acrossClues.push(parseClueLine(lines[index++]));
  }

  const answers = Array.from({ length: size }, () => Array(size).fill("/"));
  for (let row = 0; row < size; row += 1) {
    const parts = lines[index++].trim().split(/\s+/);
    for (let col = 0; col < size; col += 1) {
      answers[row][col] = (parts[col] ?? "/").toUpperCase();
    }
  }

  const under = (row, col) =>
    row < size - 1 &&
    answers[row][col] !== "/" &&
    answers[row + 1][col] !== "/" &&
    (row === 0 || answers[row - 1][col] === "/");

  const right = (row, col) =>
    col < size - 1 &&
    answers[row][col] !== "/" &&
    answers[row][col + 1] !== "/" &&
    (col === 0 || answers[row][col - 1] === "/");

  const acrossNums = Array.from({ length: size }, () => Array(size).fill(0));
  const downNums = Array.from({ length: size }, () => Array(size).fill(0));
  const clueNums = Array.from({ length: size }, () => Array(size).fill(0));
  let clueIndex = 1;

  for (let row = 0; row < size; row += 1) {
    for (let col = 0; col < size; col += 1) {
      if (answers[row][col] === "/") continue;
      const startsDown = under(row, col);
      const startsAcross = right(row, col);
      if (startsAcross) acrossNums[row][col] = clueIndex;
      if (startsDown) downNums[row][col] = clueIndex;
      if (startsAcross || startsDown) {
        clueNums[row][col] = clueIndex;
        clueIndex += 1;
      }
    }
  }

  const rowPromptNums = Array.from({ length: size }, () => Array(size).fill(0));
  const colPromptNums = Array.from({ length: size }, () => Array(size).fill(0));
  for (let row = 0; row < size; row += 1) {
    for (let col = 0; col < size; col += 1) {
      if (answers[row][col] === "/") continue;
      rowPromptNums[row][col] =
        acrossNums[row][col] || (col > 0 ? rowPromptNums[row][col - 1] : 0);
      colPromptNums[row][col] =
        downNums[row][col] || (row > 0 ? colPromptNums[row - 1][col] : 0);
    }
  }

  return {
    size,
    answers,
    clueNums,
    rowPromptNums,
    colPromptNums,
    acrossClues,
    downClues,
  };
}

function parseConnections(text) {
  const lines = text
    .split(/\r?\n/)
    .map((line) => line.trim())
    .filter(Boolean);
  const groups = [];
  for (let i = 0; i + 4 < lines.length; i += 5) {
    groups.push({
      category: lines[i],
      words: [lines[i + 1], lines[i + 2], lines[i + 3], lines[i + 4]].map(
        (word) => word.toUpperCase(),
      ),
    });
  }
  return groups;
}

function WordleGame({ resetKey }) {
  const [wordList, setWordList] = useState([]);
  const [wordSet, setWordSet] = useState(new Set());
  const [solution, setSolution] = useState("");
  const [guesses, setGuesses] = useState(Array(WORDLE_ROWS).fill(""));
  const [evaluations, setEvaluations] = useState(Array(WORDLE_ROWS).fill(null));
  const [currentRow, setCurrentRow] = useState(0);
  const [currentGuess, setCurrentGuess] = useState("");
  const [statusMessage, setStatusMessage] = useState("Loading word list...");
  const [isGameOver, setIsGameOver] = useState(false);
  const [showStats, setShowStats] = useState(false);
  const [stats, setStats] = useState(loadWordleStats);

  useEffect(() => {
    const loadWords = async () => {
      try {
        const response = await fetch("/wordleWords.txt");
        if (!response.ok) throw new Error("Failed to load words");
        const words = (await response.text())
          .split(/\r?\n/)
          .map((word) => word.trim().toUpperCase())
          .filter((word) => word.length === WORDLE_COLS);
        setWordList(words);
        setWordSet(new Set(words));
        setStatusMessage("");
      } catch {
        setStatusMessage("Could not load Wordle words.");
      }
    };
    loadWords();
  }, []);

  const startNewGame = useCallback(() => {
    if (wordList.length === 0) return;
    const target = wordList[Math.floor(Math.random() * wordList.length)];
    setSolution(target);
    setGuesses(Array(WORDLE_ROWS).fill(""));
    setEvaluations(Array(WORDLE_ROWS).fill(null));
    setCurrentRow(0);
    setCurrentGuess("");
    setStatusMessage("");
    setIsGameOver(false);
  }, [wordList]);

  useEffect(() => {
    if (!solution && wordList.length > 0) {
      startNewGame();
    }
  }, [wordList, solution, startNewGame]);

  useEffect(() => {
    if (wordList.length > 0) startNewGame();
  }, [resetKey, wordList, startNewGame]);

  const submitGuess = useCallback(() => {
    if (isGameOver || !solution) return;
    if (currentGuess.length !== WORDLE_COLS) {
      setStatusMessage("Not enough letters");
      return;
    }
    if (!wordSet.has(currentGuess)) {
      setStatusMessage("Not in word list");
      return;
    }

    const nextGuesses = [...guesses];
    nextGuesses[currentRow] = currentGuess;
    setGuesses(nextGuesses);

    const nextEvaluations = [...evaluations];
    nextEvaluations[currentRow] = evaluateGuess(currentGuess, solution);
    setEvaluations(nextEvaluations);

    if (currentGuess === solution) {
      const nextStats = {
        gamesPlayed: stats.gamesPlayed + 1,
        gamesWon: stats.gamesWon + 1,
        guessDistribution: [...stats.guessDistribution, currentRow + 1],
      };
      setStats(nextStats);
      saveWordleStats(nextStats);
      setStatusMessage("You got it!");
      setIsGameOver(true);
      setCurrentGuess("");
      return;
    }

    if (currentRow === WORDLE_ROWS - 1) {
      const nextStats = { ...stats, gamesPlayed: stats.gamesPlayed + 1 };
      setStats(nextStats);
      saveWordleStats(nextStats);
      setStatusMessage(`Nice try! The Wordle was ${solution}`);
      setIsGameOver(true);
      setCurrentGuess("");
      return;
    }

    setCurrentRow((value) => value + 1);
    setCurrentGuess("");
    setStatusMessage("");
  }, [
    currentGuess,
    currentRow,
    evaluations,
    guesses,
    isGameOver,
    solution,
    stats,
    wordSet,
  ]);

  const addLetter = useCallback((letter) => {
    setCurrentGuess((prev) =>
      prev.length < WORDLE_COLS ? `${prev}${letter}` : prev,
    );
  }, []);

  const removeLetter = useCallback(() => {
    setCurrentGuess((prev) => prev.slice(0, -1));
  }, []);

  useEffect(() => {
    const onKeyDown = (event) => {
      if (isGameOver || !solution) return;
      if (event.key === "Backspace") {
        event.preventDefault();
        removeLetter();
      } else if (event.key === "Enter") {
        event.preventDefault();
        submitGuess();
      } else if (/^[a-zA-Z]$/.test(event.key)) {
        addLetter(event.key.toUpperCase());
      }
    };
    window.addEventListener("keydown", onKeyDown);
    return () => window.removeEventListener("keydown", onKeyDown);
  }, [addLetter, isGameOver, removeLetter, solution, submitGuess]);

  const keyboardStatuses = useMemo(() => {
    const statuses = {};
    evaluations.forEach((result, row) => {
      if (!result) return;
      for (let col = 0; col < WORDLE_COLS; col += 1) {
        const letter = guesses[row][col];
        const current = statuses[letter] ?? "empty";
        if (STATUS_PRIORITY[result[col]] > STATUS_PRIORITY[current]) {
          statuses[letter] = result[col];
        }
      }
    });
    return statuses;
  }, [evaluations, guesses]);

  const guessDistribution = useMemo(() => {
    const buckets = [0, 0, 0, 0, 0, 0];
    stats.guessDistribution.forEach((attempts) => {
      if (attempts >= 1 && attempts <= 6) buckets[attempts - 1] += 1;
    });
    return buckets;
  }, [stats.guessDistribution]);

  const handleScreenKey = (key) => {
    if (isGameOver || !solution) return;
    if (key === "ENTER") {
      submitGuess();
    } else if (key === "BACK") {
      removeLetter();
    } else {
      addLetter(key);
    }
  };

  return (
    <section className="wordle-panel">
      <div className="panel-head">
        <h2>Wordle</h2>
        <button onClick={() => setShowStats((value) => !value)}>
          {showStats ? "Hide Stats" : "Show Stats"}
        </button>
      </div>

      {showStats && (
        <div className="stats-card">
          <p>Games Played: {stats.gamesPlayed}</p>
          <p>Games Won: {stats.gamesWon}</p>
          <div className="distribution">
            {guessDistribution.map((count, index) => (
              <p key={index}>
                {index + 1}: {count}
              </p>
            ))}
          </div>
        </div>
      )}

      <div className="board" role="grid" aria-label="Wordle board">
        {Array.from({ length: WORDLE_ROWS }).map((_, rowIndex) => {
          const rowGuess =
            rowIndex === currentRow ? currentGuess : guesses[rowIndex];
          const rowStatuses = evaluations[rowIndex];
          return (
            <div className="row" key={rowIndex}>
              {Array.from({ length: WORDLE_COLS }).map((__, colIndex) => {
                const letter = rowGuess[colIndex] ?? "";
                const status = rowStatuses ? rowStatuses[colIndex] : "empty";
                return (
                  <div className={`tile ${status}`} key={colIndex}>
                    {letter}
                  </div>
                );
              })}
            </div>
          );
        })}
      </div>

      <p className="status">{statusMessage || "\u00A0"}</p>
      <div className="keyboard">
        {WORDLE_KEYBOARD_ROWS.map((row) => (
          <div className="keyboard-row" key={row}>
            {row.split("").map((letter) => (
              <button
                key={letter}
                className={`key ${keyboardStatuses[letter] ?? "empty"}`}
                onClick={() => handleScreenKey(letter)}
              >
                {letter}
              </button>
            ))}
          </div>
        ))}
        <div className="keyboard-row">
          <button className="key wide" onClick={() => handleScreenKey("ENTER")}>
            ENTER
          </button>
          <button className="key wide" onClick={() => handleScreenKey("BACK")}>
            BACK
          </button>
        </div>
      </div>
    </section>
  );
}

function MiniCrosswordGame({ resetKey }) {
  const [puzzle, setPuzzle] = useState(null);
  const [grid, setGrid] = useState([]);
  const [selected, setSelected] = useState({ row: 0, col: 0 });
  const [across, setAcross] = useState(true);
  const [status, setStatus] = useState("Loading mini crossword...");
  const [elapsed, setElapsed] = useState(0);
  const [won, setWon] = useState(false);

  const createNewGame = useCallback(async () => {
    try {
      const file =
        CROSSWORD_FILES[Math.floor(Math.random() * CROSSWORD_FILES.length)];
      const response = await fetch(`/crosswords/${file}`);
      if (!response.ok) throw new Error("Failed to load crossword");
      const parsed = parseCrossword(await response.text());
      setPuzzle(parsed);
      setGrid(
        parsed.answers.map((row) =>
          row.map((cell) => (cell === "/" ? "/" : "")),
        ),
      );
      setAcross(true);
      setStatus("");
      setElapsed(0);
      setWon(false);
      outer: for (let row = 0; row < parsed.size; row += 1) {
        for (let col = 0; col < parsed.size; col += 1) {
          if (parsed.answers[row][col] !== "/") {
            setSelected({ row, col });
            break outer;
          }
        }
      }
    } catch {
      setStatus("Could not load crossword puzzle.");
    }
  }, []);

  useEffect(() => {
    createNewGame();
  }, [createNewGame, resetKey]);

  useEffect(() => {
    if (!puzzle || won) return undefined;
    const timer = setInterval(() => setElapsed((value) => value + 1), 1000);
    return () => clearInterval(timer);
  }, [puzzle, won]);

  const moveSelection = useCallback(
    (forward, directionAcross = across, start = selected) => {
      if (!puzzle) return start;
      let row = start.row;
      let col = start.col;
      const maxSteps = puzzle.size * puzzle.size;
      for (let step = 0; step < maxSteps; step += 1) {
        if (directionAcross) {
          if (forward) {
            col = (col + 1) % puzzle.size;
            if (col === 0) row = (row + 1) % puzzle.size;
          } else {
            col = (col - 1 + puzzle.size) % puzzle.size;
            if (col === puzzle.size - 1)
              row = (row - 1 + puzzle.size) % puzzle.size;
          }
        } else if (forward) {
          row = (row + 1) % puzzle.size;
          if (row === 0) col = (col + 1) % puzzle.size;
        } else {
          row = (row - 1 + puzzle.size) % puzzle.size;
          if (row === puzzle.size - 1)
            col = (col - 1 + puzzle.size) % puzzle.size;
        }
        if (puzzle.answers[row][col] !== "/") return { row, col };
      }
      return start;
    },
    [across, puzzle, selected],
  );

  const evaluateGrid = useCallback(
    (nextGrid) => {
      if (!puzzle) return;
      let allFilled = true;
      let allCorrect = true;
      for (let row = 0; row < puzzle.size; row += 1) {
        for (let col = 0; col < puzzle.size; col += 1) {
          if (puzzle.answers[row][col] === "/") continue;
          if (!nextGrid[row][col]) allFilled = false;
          if (nextGrid[row][col] !== puzzle.answers[row][col])
            allCorrect = false;
        }
      }
      if (allCorrect) {
        setWon(true);
        setStatus(
          `You win! Solved in ${Math.floor(elapsed / 60)}:${String(elapsed % 60).padStart(2, "0")}`,
        );
      } else if (allFilled) {
        setStatus("Not Quite");
      } else {
        setStatus("");
      }
    },
    [elapsed, puzzle],
  );

  const typeLetter = useCallback(
    (letter) => {
      if (!puzzle || won) return;
      const nextGrid = grid.map((row) => [...row]);
      nextGrid[selected.row][selected.col] = letter;
      setGrid(nextGrid);
      evaluateGrid(nextGrid);
      setSelected(moveSelection(true));
    },
    [
      evaluateGrid,
      grid,
      moveSelection,
      puzzle,
      selected.col,
      selected.row,
      won,
    ],
  );

  const backspace = useCallback(() => {
    if (!puzzle || won) return;
    const nextGrid = grid.map((row) => [...row]);
    if (nextGrid[selected.row][selected.col]) {
      nextGrid[selected.row][selected.col] = "";
      setGrid(nextGrid);
      setStatus("");
      return;
    }
    const previous = moveSelection(false);
    nextGrid[previous.row][previous.col] = "";
    setGrid(nextGrid);
    setSelected(previous);
    setStatus("");
  }, [grid, moveSelection, puzzle, selected.col, selected.row, won]);

  useEffect(() => {
    const onKeyDown = (event) => {
      if (!puzzle || won) return;
      if (event.key === "Backspace") {
        event.preventDefault();
        backspace();
      } else if (event.key === "Tab" || event.key === "Enter") {
        event.preventDefault();
        setAcross((value) => !value);
      } else if (/^[a-zA-Z]$/.test(event.key)) {
        event.preventDefault();
        typeLetter(event.key.toUpperCase());
      }
    };
    window.addEventListener("keydown", onKeyDown);
    return () => window.removeEventListener("keydown", onKeyDown);
  }, [backspace, puzzle, typeLetter, won]);

  const activeClueNumber = puzzle
    ? across
      ? (puzzle.rowPromptNums[selected.row]?.[selected.col] ?? 0)
      : (puzzle.colPromptNums[selected.row]?.[selected.col] ?? 0)
    : 0;

  const activePath = useMemo(() => {
    if (!puzzle) return new Set();
    const ids = new Set();
    const promptNums = across ? puzzle.rowPromptNums : puzzle.colPromptNums;
    const target = promptNums[selected.row]?.[selected.col];
    for (let row = 0; row < puzzle.size; row += 1) {
      for (let col = 0; col < puzzle.size; col += 1) {
        if (
          promptNums[row][col] === target &&
          puzzle.answers[row][col] !== "/"
        ) {
          ids.add(`${row}-${col}`);
        }
      }
    }
    return ids;
  }, [across, puzzle, selected.col, selected.row]);

  if (!puzzle) {
    return (
      <section className="crossword-panel">
        <p>{status}</p>
      </section>
    );
  }

  return (
    <section className="crossword-panel">
      <div className="panel-head">
        <h2>Mini Crossword</h2>
        <p>
          Time: {Math.floor(elapsed / 60)}:
          {String(elapsed % 60).padStart(2, "0")}
        </p>
      </div>
      <p className="status">
        {status ||
          `Direction: ${across ? "Across" : "Down"} (Tab/Enter toggles)`}
      </p>
      <div className="crossword-layout">
        <div
          className="crossword-grid"
          style={{
            gridTemplateColumns: `repeat(${puzzle.size}, minmax(0, 1fr))`,
          }}
        >
          {puzzle.answers.map((row, rowIndex) =>
            row.map((cell, colIndex) => {
              const black = cell === "/";
              const cellKey = `${rowIndex}-${colIndex}`;
              const isSelected =
                selected.row === rowIndex && selected.col === colIndex;
              const isActive = activePath.has(cellKey);
              return (
                <button
                  key={cellKey}
                  className={`cw-cell ${black ? "black" : ""} ${isActive ? "active-path" : ""} ${isSelected ? "selected" : ""}`}
                  onClick={() => {
                    if (black || won) return;
                    if (isSelected) setAcross((value) => !value);
                    else setSelected({ row: rowIndex, col: colIndex });
                  }}
                >
                  {!black && puzzle.clueNums[rowIndex][colIndex] > 0 && (
                    <span className="cw-clue-num">
                      {puzzle.clueNums[rowIndex][colIndex]}
                    </span>
                  )}
                  {!black && (
                    <span className="cw-letter">
                      {grid[rowIndex]?.[colIndex] ?? ""}
                    </span>
                  )}
                </button>
              );
            }),
          )}
        </div>
        <aside className="clues-panel">
          <h3>Across</h3>
          {puzzle.acrossClues.map((clue) => (
            <p
              key={`a-${clue.num}`}
              className={
                across && activeClueNumber === clue.num ? "active-clue" : ""
              }
            >
              <strong>{clue.num}.</strong> {clue.text}
            </p>
          ))}
          <h3>Down</h3>
          {puzzle.downClues.map((clue) => (
            <p
              key={`d-${clue.num}`}
              className={
                !across && activeClueNumber === clue.num ? "active-clue" : ""
              }
            >
              <strong>{clue.num}.</strong> {clue.text}
            </p>
          ))}
        </aside>
      </div>
    </section>
  );
}

function ConnectionsGame({ resetKey }) {
  const [groups, setGroups] = useState([]);
  const [tiles, setTiles] = useState([]);
  const [selected, setSelected] = useState([]);
  const [solvedGroups, setSolvedGroups] = useState([]);
  const [lives, setLives] = useState(2);
  const [status, setStatus] = useState("Loading connections...");
  const [gameOver, setGameOver] = useState(false);

  const createNewGame = useCallback((sourceGroups) => {
    if (sourceGroups.length < 4) return;
    const pickedIndices = pickRandomDistinct(sourceGroups.length, 4);
    const selectedGroups = pickedIndices.map((index, groupId) => ({
      groupId,
      category: sourceGroups[index].category,
      words: sourceGroups[index].words,
    }));

    const generatedTiles = shuffle(
      selectedGroups.flatMap((group) =>
        group.words.map((word, wordIndex) => ({
          id: `${group.groupId}-${wordIndex}-${word}`,
          word,
          groupId: group.groupId,
          category: group.category,
          solved: false,
        })),
      ),
    );

    setTiles(generatedTiles);
    setSelected([]);
    setSolvedGroups([]);
    setLives(2);
    setGameOver(false);
    setStatus("");
  }, []);

  useEffect(() => {
    const load = async () => {
      try {
        const response = await fetch("/ConnectionsWords");
        if (!response.ok) throw new Error("Failed to load connections");
        const parsed = parseConnections(await response.text());
        setGroups(parsed);
        createNewGame(parsed);
      } catch {
        setStatus("Could not load connections data.");
      }
    };
    load();
  }, [createNewGame]);

  useEffect(() => {
    if (groups.length > 0) createNewGame(groups);
  }, [createNewGame, groups, resetKey]);

  const revealAll = useCallback(() => {
    const remaining = [...new Set(tiles.map((tile) => tile.groupId))].filter(
      (groupId) => !solvedGroups.some((group) => group.groupId === groupId),
    );
    const appended = remaining.map((groupId) => {
      const fromTile = tiles.find((tile) => tile.groupId === groupId);
      const words = tiles
        .filter((tile) => tile.groupId === groupId)
        .map((tile) => tile.word);
      return { groupId, category: fromTile?.category ?? "", words };
    });
    setSolvedGroups((current) =>
      [...current, ...appended].sort((a, b) => a.groupId - b.groupId),
    );
    setTiles((current) => current.map((tile) => ({ ...tile, solved: true })));
    setSelected([]);
    setGameOver(true);
  }, [solvedGroups, tiles]);

  const submitSelection = () => {
    if (gameOver) return;
    if (selected.length !== 4) {
      setStatus("Please select four boxes");
      return;
    }

    const chosenTiles = tiles.filter((tile) => selected.includes(tile.id));
    const counts = chosenTiles.reduce((map, tile) => {
      map[tile.groupId] = (map[tile.groupId] ?? 0) + 1;
      return map;
    }, {});
    const matches = Object.values(counts);

    if (matches.includes(4)) {
      const solvedGroupId = Number(
        Object.keys(counts).find((key) => counts[key] === 4),
      );
      const solvedTiles = tiles.filter(
        (tile) => tile.groupId === solvedGroupId,
      );
      setTiles((current) =>
        current.map((tile) =>
          tile.groupId === solvedGroupId ? { ...tile, solved: true } : tile,
        ),
      );
      setSolvedGroups((current) =>
        [
          ...current,
          {
            groupId: solvedGroupId,
            category: solvedTiles[0].category,
            words: solvedTiles.map((tile) => tile.word),
          },
        ].sort((a, b) => a.groupId - b.groupId),
      );
      setSelected([]);
      setStatus("Correct");

      if (solvedGroups.length + 1 === 4) {
        setStatus(`Congratulations, you win with ${2 - lives} mistake(s)`);
        setGameOver(true);
      }
      return;
    }

    const isOneAway = matches.includes(3);
    const nextLives = lives - 1;
    setLives(nextLives);
    setStatus(isOneAway ? "One Away" : "Wrong");

    if (nextLives <= 0) {
      setStatus("You are out of lives");
      revealAll();
    }
  };

  if (tiles.length === 0) {
    return (
      <section className="connections-panel">
        <p>{status}</p>
      </section>
    );
  }

  const unsolvedTiles = tiles.filter((tile) => !tile.solved);

  return (
    <section className="connections-panel">
      <div className="panel-head">
        <h2>Connections</h2>
        <p>Lives: {lives}</p>
      </div>
      <p className="status">{status || "Create four groups of four"}</p>

      <div className="connections-solved">
        {solvedGroups.map((group) => (
          <div
            className="connection-group"
            key={group.groupId}
            style={{
              background:
                CONNECTION_COLORS[group.groupId % CONNECTION_COLORS.length],
            }}
          >
            <strong>{group.category}</strong>
            <p>{group.words.join(", ")}</p>
          </div>
        ))}
      </div>

      <div className="connections-grid">
        {unsolvedTiles.map((tile) => (
          <button
            key={tile.id}
            className={`connection-tile ${selected.includes(tile.id) ? "selected" : ""}`}
            onClick={() => {
              if (gameOver) return;
              setSelected((current) => {
                if (current.includes(tile.id))
                  return current.filter((id) => id !== tile.id);
                if (current.length >= 4) return current;
                return [...current, tile.id];
              });
            }}
          >
            {tile.word}
          </button>
        ))}
      </div>
      <div className="connections-actions">
        <button onClick={submitSelection} disabled={gameOver}>
          Submit
        </button>
      </div>
    </section>
  );
}

function App() {
  const [activeGame, setActiveGame] = useState("wordle");
  const [resetKeys, setResetKeys] = useState({
    wordle: 0,
    crossword: 0,
    connections: 0,
  });

  const resetActiveGame = () => {
    setResetKeys((current) => ({
      ...current,
      [activeGame]: current[activeGame] + 1,
    }));
  };

  return (
    <main className="app">
      <header className={`topbar topbar-${activeGame}`}>
        <div>
          <h1>New Guelph Times Games</h1>
        </div>
        <div className="topbar-actions">
          <button onClick={resetActiveGame}>
            New{" "}
            {activeGame === "wordle"
              ? "Wordle"
              : activeGame === "crossword"
                ? "Crossword"
                : "Connections"}
          </button>
        </div>
      </header>

      <section className="game-shell">
        <aside className="game-nav">
          <h2>Games</h2>
          <button
            className={activeGame === "wordle" ? "active" : ""}
            onClick={() => setActiveGame("wordle")}
          >
            Wordle
          </button>
          <button
            className={activeGame === "crossword" ? "active" : ""}
            onClick={() => setActiveGame("crossword")}
          >
            Mini Crossword
          </button>
          <button
            className={activeGame === "connections" ? "active" : ""}
            onClick={() => setActiveGame("connections")}
          >
            Connections
          </button>
        </aside>

        {activeGame === "wordle" && <WordleGame resetKey={resetKeys.wordle} />}
        {activeGame === "crossword" && (
          <MiniCrosswordGame resetKey={resetKeys.crossword} />
        )}
        {activeGame === "connections" && (
          <ConnectionsGame resetKey={resetKeys.connections} />
        )}
      </section>
    </main>
  );
}

export default App;
